import pandas as pd
import os
from os.path import exists
from Process_func import cleanup_csv

"""
Sorts the obtained test values and returns it in a pandas dataframe
"""
def sortMeasurement(dataFile):
    cleanup_csv(dataFile)
    testPoint = pd.read_csv(path, names = ["MAC","Rssi"])
    sortTestPoint = testPoint.sort_values(by = "Rssi", ascending = False)
    resetIndex = sortTestPoint.reset_index(drop=True) 
    return resetIndex

"""
Calculates the posterior
"""
def calc_posterior(sortedTestPoint,prior):
    
    # Search until the MAC address can be found in the files and the sum is not zero
    flag = 0
    iter = 0

    while (flag==0):

        flag = 1

        # Select the strongest MAC and corresponding signal
        StrongestMAC = sortedTestPoint.loc[iter,"MAC"]
        StrongestSignal = sortedTestPoint.loc[iter,"Rssi"]
        
        # increment for new index  
        iter = iter + 1

        # Define the posterior
        posterior   = [0. for i in range(int(cell_number))]

        # Get the file of the corresponding MAC 
        file_name_MAC = "MACpmf/" + StrongestMAC.replace(":","_") + ".csv"

        #If the file exists use it to update the prior, otherwise look at the next strongest signal map
        if(exists(file_name_MAC)): 
            df = pd.read_csv(file_name_MAC, header = None) # Read the cell data into a df
            RSS_column = df[-StrongestSignal].tolist() 
        
            # Calculate posterior
            for index in range(cell_number):
                posterior[index] = prior[index]*RSS_column[index]    

            # Normalize posterior
            sum = 0     
            for j in range(len(posterior)):    
                sum = sum + posterior[j]  

            # Search for new MAC 
            if sum == 0: 
                flag = 0
                print("sum = 0")
            
            else:
                for j in range(len(posterior)):    
                    posterior[j] = posterior[j]/sum

        else:
            print("MAC is not present please try second best signal here is the MAC", file_name_MAC)
            flag = 0

    return posterior
  

# Set global points 
parentDirectory = os.path.abspath(os.path.join(os.getcwd(), os.pardir))
day = "Tuesday"
dirList = ["North","East","West","South"]
cell_number = 15
good = []
almost1 = []
almost2 = []

initial_belief = [1./cell_number for i in range(int(cell_number))]
for i in range(1,cell_number+1):
    for dir in dirList:
        # Get the file and create a proper csv file of it
        path = os.path.join(parentDirectory, "GatherData_Bayes", "Test_" + day , "saved_data_celltest" + str(i) + "_" + day + "_" + dir + ".txt")
        cleanup_csv(path)
        sortTest = sortMeasurement(path)
        
        if dir == "North": # first measurement for a cell
            posterior = calc_posterior(sortTest,initial_belief)
        else:
            posterior = calc_posterior(sortTest,posterior)

        print(f' real cell = {i} and predicted cell = {posterior.index(max(posterior))+1}')

        if dir == "South":
            print(f' real cell = {i} and predicted cell = {posterior.index(max(posterior))+1}')

            if i == posterior.index(max(posterior))+1:
                good.append(i)
            if i == posterior.index(max(posterior)) or i == posterior.index(max(posterior)) + 2:
                almost1.append(i)
            if i == posterior.index(max(posterior)) -1 or i == posterior.index(max(posterior)) + 3:
                almost2.append(i)
print(day)
print("good",good)
print("almost1", almost1)
print("almost2", almost2)
