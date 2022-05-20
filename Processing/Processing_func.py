import pandas as pd
import os
import numpy as np
from os.path import exists
import csv
import glob

    
"""
This function adds commas to the first line of a csv to be able to obtain a pandas df
"""
def format_txt(DataFile):
    
    max_commas = 0 # Max number of commmas in single line
    missing_commas = 0 # Missing commas in firts line
    first_commas = 0 # Number of commas in first line
    flag = True
    # Obtain max number of commas in any line in the file
    file = open(DataFile)
    for line in file: 
        if(flag):# Just to get number of commas in the first line
            first_commas = line.count(',')
            flag = False
        if(line.count(',') > max_commas):
            max_commas = line.count(',')
    file.close()

    missing_commas = max_commas- first_commas # Obtain missing commmas if any

    # Add the missing commas to the first line 
    flag = True
    if(missing_commas >0):
        commas = ""
        for ij in range(missing_commas): # Get number of missing commas in string
            commas += ","
        newf=""
        with open(DataFile,'r') as f:
            for line in f:
                if(flag): # Append the commas to just the first line
                    newf += line.strip()+commas+"\n"
                    flag = False
                else:
                    newf += line.strip()+"\n"
                
        with open(DataFile,'w') as f: # Write the newly formed correct file
            f.write(newf)

"""
Cleans the csv file to make it more usable. 
"""
def cleanup_csv(DataFile):
    # Format the training data
    with open(DataFile, 'r') as my_file:
        text = my_file.read()
        text = text.replace("[", "")
        text = text.replace("]", "")
        text = text.replace(" ", "")
        text = text.replace("\"", "")
    
    with open(DataFile, 'w') as my_file:
        my_file.write(text)

"""
Checks whether there is a mac address occuring twice. As this could have happened at the beginning of the measurements. 
"""

def duplicate_check(day):
    
    duplicates = []

    for i in range(1,16):
        # Select all direction files 
        pathNorth = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + "_North.txt")
        pathEast = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + " _East.txt")
        pathSouth = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + "_South.txt")
        pathWest = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + "_West.txt")

        # Read text file and get mac address as index 
        dfNorth = pd.read_csv(pathNorth, header = None)
        dfEast = pd.read_csv(pathEast, header = None)
        dfSouth = pd.read_csv(pathSouth, header = None)
        dfWest = pd.read_csv(pathWest, header = None)

        duplicateRowsNorth = dfNorth[dfNorth.duplicated([0])]
        if not duplicateRowsNorth.empty:
            duplicate.append(str(i)+"_North")

        if not dfEast[dfEast.duplicated([0])].empty:
            duplicate.append(str(i)+"_East")
        
        if not dfWest[dfWest.duplicated([0])].empty:
            duplicate.append(str(i)+"_West")
        
        if not dfSouth[dfSouth.duplicated([0])].empty:
            duplicate.append(str(i)+"_South")

    return duplicates

"""
Walks through obtained data files and saves for each mac address the pmf per cell. Each mac address gets its own file. 
"""

def create_pmf(dir,cell):

    PMFBuffer = [0 for i in range(100)] # Assume signal strength values range from 0 to -100
    df_init = pd.read_csv(dir) # Read the cell data into a df
    sampleSize = df_init.shape[1]-1 # Maximum number of samples for a single measurement

    # Iterate over each row of the dataframe
    for index, row in df_init.iterrows():
        file_name_MAC = "model/" + row[0] + ".csv" # File where the radar map will be placed
        
        if(exists(file_name_MAC)): # If the file already exists insert new row

            for j in range(sampleSize): # Obtain the histogram by increasing the array by index of the present signal
                if(isinstance(row[j+1], int)): # Check if there is a sample at the index. not NaN
                    PMFBuffer[int(-row[j+1])] +=1

            # Obtain sum of the histogram for normalization
            sum = 0;     
            for j in range(0, len(PMFBuffer)):    
                sum = sum + PMFBuffer[j];  

            for j in range(len(PMFBuffer)): # Normalize histogram for pmf
                PMFBuffer[j] = PMFBuffer[j]/float(sum)

            df_tmp1 = {'Cells': "cell"+str(cell+1), 'PMF':PMFBuffer} # Temp df    
            
            df_tmp = pd.read_csv(file_name_MAC) # Read the map to insert new pmf
            df_tmp = df_tmp.append(df_tmp1, ignore_index = True)
            df_tmp.to_csv(file_name_MAC, index=False)
            PMFBuffer = [0 for q in range(100)]

        else: # If the file does not exist, create and format the file

            for j in range(sampleSize): # Obtain the histogram by increasing the array by index of the present signal
                if(isinstance(row[j+1], int)): # Check if there is a sample at the index. not NaN
                    PMFBuffer[int(-row[j+1])] +=1
            
            # Obtain sum of the histogram for normalization
            sum = 0;     
            for j in range(0, len(PMFBuffer)):    
                sum = sum + PMFBuffer[j]; 

            for j in range(len(PMFBuffer)): # Normalize histogram for pmf
                PMFBuffer[j] = PMFBuffer[j]/float(sum)

            df_tmp = pd.DataFrame([['cell' + str(cell+1), PMFBuffer]])
            df_tmp.columns = ['Cells', 'PMF']
            df_tmp.to_csv(file_name_MAC, index=False)
            PMFBuffer = [0 for q in range(100)]

"""
Used to combine the different csv files of the different directions 
"""


def concat_directions(parentDirectory, day):
    
    for i in range(2,3):
        pathNorth = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + "_North.txt")
        pathEast  = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + "_East.txt")
        pathSouth = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + "_South.txt")
        pathWest  = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(i) + "_" + day + "_West.txt")
        
        northDf = pd.read_csv(pathNorth, header = None)
        eastDf = pd.read_csv(pathEast, header = None)
        southDf = pd.read_csv(pathSouth, header = None)
        westDf = pd.read_csv(pathWest, header = None)

                
        # Set mac address as index
        northDf.set_index(0)

        northDf.to_csv("saved_data_north" + str(i) + "_all.csv", header = False, index = False)

        eastDf.set_index(0)

        eastDf.to_csv("saved_data_east" + str(i) + "_all.csv", header = False, index = False)

        southDf.set_index(0)
        southDf.to_csv("saved_data_south" + str(i) + "_all.csv", header = False, index = False)

        westDf.set_index(0)
        westDf.to_csv("saved_data_west" + str(i) + "_all.csv", header = False, index = False)

        northEastDf = pd.concat([northDf,eastDf],axis = 1)
        northEastWestDf = pd.concat([northEastDf,westDf],axis = 1)
        totalDf = pd.concat([northEastWestDf, southDf], axis = 1)

        if i==2:
            print(northDf)
        totalDf.to_csv("saved_data_cell" + str(i) + "_all.csv", header = False, index = False)

def add_missing_cells(dir,cell_number):
    emptyPMF = [0. for i in range(100)] # Init empty pmf to insert for missing cells
    if(exists(dir)): 
        df = pd.read_csv(dir) # Read the cell data into a df
        cells = df['Cells'].tolist() # Get list of present celss
        for cell in range(1, cell_number+1):
            if not 'cell'+ str(cell) in cells:
                df_tmp = {'Cells': 'cell'+ str(cell), 'PMF':emptyPMF} # Temp df    
                df = df.append(df_tmp, ignore_index = True)
                df = df.sort_values('Cells') # Sort so that rows go from cell1 to cell<cell_number> 
                df.to_csv(dir, index=False, header = None)
            else:
                df.to_csv(dir, index=False, header = None) # To get rid of the header

    else:
        print("Directory does not exist")
    

    # Make function that filters low occuring mac addresses 
def filterLowMac():
    return 0
        
