from multiprocessing import parent_process
import pandas as pd
import os
import numpy as np
from os.path import exists

   
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
        text = text.replace("\\n","")
        text = text.replace("'","")
        text = text.replace('"',"")
    
    with open(DataFile, 'w') as my_file:
        my_file.write(text)

"""
Checks whether there is a mac address occuring twice. As this could have happened at the beginning of the measurements. 
"""

def duplicate_check(parentDirectory,cell,day):
    
    duplicates = []

    
    # Select all direction files 
    pathNorth = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(cell) + "_" + day + "_North.txt")
    pathEast  = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(cell) + "_" + day + "_East.txt")
    pathSouth = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(cell) + "_" + day + "_South.txt")
    pathWest  = os.path.join(parentDirectory,"App2_data_" + day,"saved_data_cell" + str(cell) + "_" + day + "_West.txt")

    # Read text file and get mac address as index 
    dfNorth = pd.read_csv(pathNorth, header = None)
    dfEast = pd.read_csv(pathEast, header = None)
    dfSouth = pd.read_csv(pathSouth, header = None)
    dfWest = pd.read_csv(pathWest, header = None)

    duplicateRowsNorth = dfNorth[dfNorth.duplicated([0])]
    if not duplicateRowsNorth.empty:
        duplicates.append(str(cell)+"_North")

    if not dfEast[dfEast.duplicated([0])].empty:
        duplicates.append(str(cell)+"_East")
    
    if not dfWest[dfWest.duplicated([0])].empty:
        duplicates.append(str(cell)+"_West")
    
    if not dfSouth[dfSouth.duplicated([0])].empty:
        duplicates.append(str(cell)+"_South")

    return duplicates

"""
Walks through obtained data files and saves for each mac address the pmf per cell. Each mac address gets its own file. 
"""

def create_pmf(dir,cell, convolution = True, CONV_SCALING_1 = 0.5, CONV_SCALING_2 = 0.25, CONV_SCALING_3 = 0.1):

    PMFBuffer = [0 for i in range(100)] # Assume signal strength values range from 0 to -99

    if(convolution): # Array that will hold the additional values
        PMFBufferTMP = [0 for i in range(100)] # Assume signal strength values range from 0 to -99

    df_init = pd.read_csv(dir,header = None) # Read the cell data into a df
    sampleSize = df_init.shape[1]-1 # Maximum number of samples for a single measurement
    print("cell number = ",cell)
    # Iterate over each row of the dataframe
    for index, row in df_init.iterrows():
        file_name_MAC = "MACpmf/" + row[0].replace(":","_") + ".csv" # File where the radar map will be placed, without the replace function couldn't save it on windows 
        print(f' MAC address {row[0]} and cell {cell}')
        if(exists(file_name_MAC)): # If the file already exists insert new row

            for j in range(sampleSize): # Obtain the histogram by increasing the array by index of the present signal
                
                if not pd.isna(row[j+1]): # Check if there is a sample at the index. not NaN
                    PMFBuffer[int(-row[j+1])] +=1
                    if(convolution):
                        if(int(-row[j+1])+1)<100: 
                            PMFBufferTMP[int(-row[j+1])+1] += (1 * CONV_SCALING_1) 
                        if(int(-row[j+1])-1>-1):
                            PMFBufferTMP[int(-row[j+1])-1] += (1 * CONV_SCALING_1) 
                        if(int(-row[j+1])+2<100):
                            PMFBufferTMP[int(-row[j+1])+2] += (1 * CONV_SCALING_2)
                        if(int(-row[j+1])-2>-1):
                            PMFBufferTMP[int(-row[j+1])-2] += (1 * CONV_SCALING_2) 
                        if(int(-row[j+1])+3<100):
                            PMFBufferTMP[int(-row[j+1])+3] += (1 * CONV_SCALING_3) 
                        if(int(-row[j+1])-3>-1):
                            PMFBufferTMP[int(-row[j+1])-3] += (1 * CONV_SCALING_3)

            if convolution:
                for j in range(len(PMFBuffer)):
                    PMFBuffer[j] += PMFBufferTMP[j] 
            # Obtain sum of the histogram for normalization
            sum = 0;     
            for j in range(0, len(PMFBuffer)):    
                sum = sum + PMFBuffer[j];  

            for j in range(len(PMFBuffer)): # Normalize histogram for pmf
                PMFBuffer[j] = round(PMFBuffer[j]/float(sum),3)

            if cell > 9:
                df_tmp1 = {'Cells': "cellA"+str(cell), 'PMF':PMFBuffer} # Temp df
                df_tmp = pd.DataFrame([['cellA' + str(cell), PMFBuffer]])    
            else:
                df_tmp1 = {'Cells': "cell"+str(cell), 'PMF':PMFBuffer} # Temp df    
                df_tmp = pd.DataFrame([['cell' + str(cell), PMFBuffer]])   

            df_tmp = pd.read_csv(file_name_MAC) # Read the map to insert new pmf
            df_tmp = df_tmp.append(df_tmp1, ignore_index = True)
            df_tmp.head().to_csv(file_name_MAC, index=False)
            PMFBuffer = [0 for q in range(100)]
            if convolution:
                PMFBufferTMP = [0 for q in range(100)]

        else: # If the file does not exist, create and format the file

            for j in range(sampleSize): # Obtain the histogram by increasing the array by index of the present signal
                
                if not pd.isna(row[j+1]): # Check if there is a sample at the index. not NaN
                    PMFBuffer[int(-row[j+1])] +=1
                        if(convolution):
                        if(int(-row[j+1])+1)<100: 
                            PMFBufferTMP[int(-row[j+1])+1] += (1 * CONV_SCALING_1) 
                        if(int(-row[j+1])-1>-1):
                            PMFBufferTMP[int(-row[j+1])-1] += (1 * CONV_SCALING_1) 
                        if(int(-row[j+1])+2<100):
                            PMFBufferTMP[int(-row[j+1])+2] += (1 * CONV_SCALING_2)
                        if(int(-row[j+1])-2>-1):
                            PMFBufferTMP[int(-row[j+1])-2] += (1 * CONV_SCALING_2) 
                        if(int(-row[j+1])+3<100):
                            PMFBufferTMP[int(-row[j+1])+3] += (1 * CONV_SCALING_3) 
                        if(int(-row[j+1])-3>-1):
                            PMFBufferTMP[int(-row[j+1])-3] += (1 * CONV_SCALING_3)
            if convolution:
                PMFBuffer += PMFBufferTMP 
            # Obtain sum of the histogram for normalization
            sum = 0;     
            for j in range(0, len(PMFBuffer)):    
                sum = sum + PMFBuffer[j]; 

            print("normal sum", sum)

            for j in range(len(PMFBuffer)): # Normalize histogram for pmf
                PMFBuffer[j] = round(PMFBuffer[j]/float(sum),3)
            
            if cell > 9:
                df_tmp1 = {'Cells': "cellA"+str(cell), 'PMF':PMFBuffer} # Temp df
                df_tmp = pd.DataFrame([['cellA' + str(cell), PMFBuffer]])    
            else:
                df_tmp1 = {'Cells': "cell"+str(cell), 'PMF':PMFBuffer} # Temp df    
                df_tmp = pd.DataFrame([['cell' + str(cell), PMFBuffer]])
            
            df_tmp.columns = ['Cells', 'PMF']
            df_tmp.to_csv(file_name_MAC, index=False)
            PMFBuffer = [0 for q in range(100)]

            if convolution:
                for j in range(len(PMFBuffer)):
                    PMFBuffer[j] += PMFBufferTMP[j] 

"""
Used to combine the different csv files of the different directions 
"""

def concat_directions(parentDirectory, day, cell):
    
    totall = dict()

    fourDirections = ["South","West","North","East"]
    
    for dir in fourDirections:
        
        filename = "saved_data_cell" + str(cell) + "_" + day + "_" + dir + ".txt"
        path = os.path.join(parentDirectory,"GatherData_Bayes", day +"_dir",filename)

        # Put all the mac addresses with RSSI values in a dict
        with open(path,"r") as dataFile:
            
            for item in dataFile:
                split_list = item.split(",",1)
                if split_list[0] in totall.keys():
                    totall[split_list[0]].append(split_list[1].split(","))
                else:
                    totall[split_list[0]] = split_list[1].split(",")
            dataFile.close()
    
    pathNew = os.path.join(parentDirectory, "GatherData_Bayes", day +"_dir", "saved_data_cell" + str(cell) + "_" + day + "_All.txt")
    
    # Save the dict 
    with open(pathNew, 'w') as f: 
        for key, value in totall.items(): 
            f.write('%s,%s\n' % (key, value))
        f.close()
    
    # Clean up the file to make it a proper csv 
    cleanup_csv(pathNew)
        
"""
Filters the top #interest most ocurring mac addresses and saves it in a seperate txt at saveLoc
"""
def filter_top_mac(dataFile,interest,saveLoc):

    dataFrame = pd.read_csv(dataFile, header = None)
    lengthDf = len(dataFrame)

    assert interest<=lengthDf, "interest value higher than number of MACs"

    nanOverview = np.zeros((lengthDf,2)) # index,number of NaNs
    
    # Fill first column with indeces
    for idx in range(lengthDf):
        nanOverview[idx][0] = idx

    # Fill second column with number of NaN per row 
    for idx in range(lengthDf):
        nanOverview[idx][1] = dataFrame.loc[idx, :].isnull().sum()
    
    nanOverview[0][1] = 100 
    
    # sort nanOverview based on the number of occured NaN values 
    nanSort = nanOverview[nanOverview[:, 1].argsort()]
    topIdx = nanSort[:interest,0]
    top = dataFrame.iloc[topIdx]

    top.to_csv(saveLoc,index = False, header = False)
    cleanup_csv(saveLoc)


def add_missing_cells(dir,cell_number):
    emptyPMF = [0. for i in range(100)] # Init empty pmf to insert for missing cells
    if(exists(dir)): 
        df = pd.read_csv(dir) # Read the cell data into a df
        cells = df['Cells'].tolist() # Get list of present celss
        for cell in range(1, cell_number+1):

            if cell > 9:
                if not 'cellA'+ str(cell) in cells:
                    df_tmp = {'Cells': 'cellA'+ str(cell), 'PMF':emptyPMF} # Temp df    
                    df = df.append(df_tmp, ignore_index = True)
                    df = df.sort_values('Cells') # Sort so that rows go from cell1 to cell<cell_number> 
                    df.to_csv(dir, index=False, header = None)
                else:
                    df.to_csv(dir, index=False, header = None) # To get rid of the header
                
            else:            
                if not 'cell'+ str(cell) in cells:
                    df_tmp = {'Cells': 'cell'+ str(cell), 'PMF':emptyPMF} # Temp df    
                    df = df.append(df_tmp, ignore_index = True)
                    df = df.sort_values('Cells') # Sort so that rows go from cell1 to cell<cell_number> 
                    df.to_csv(dir, index=False, header = None)
                else:
                    df.to_csv(dir, index=False, header = None) # To get rid of the header
    
    else:
        print("Directory does not exist")



if __name__ == "__main__":
    filter_top_mac("saved_data_cell1_Tuesday.csv",10, "toptest.txt")
    
    # testSaveData = pd.read_csv("saved_data_cell1_Tuesday.csv", header = None)
    # testDuplicate = testSaveData[testSaveData.duplicated([0])]
    
    # if not testDuplicate.empty:
    #     print("duplicate")


