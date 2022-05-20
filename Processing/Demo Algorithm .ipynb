import pandas as pd
import os
import glob
from os.path import exists


cell_number = 5
init_belief = [1./cell_number for i in range(int(cell_number))]
posterior   = [0. for i in range(int(cell_number))]

#Assume first testing data is acquired
StrongestMAC = "1c:28:af:61:df:00"
StrongestSignal = -47

# Looking at atrongest, method
file_name_MAC = "model/" + StrongestMAC + ".csv"

#If the file exists use it to update the prior, otherwise look at the next strongest signal map
if(exists(file_name_MAC)): 
    df = pd.read_csv(file_name_MAC, header = None) # Read the cell data into a df

    RSS_column = df[-StrongestSignal].tolist() 
else:
    print("MAC is not present please try second best signal")

# Calculate posterior
for index in range(cell_number):
    posterior[index] = init_belief[index]*RSS_column[index]        
# Normalize posterior
sum = 0;     
for j in range(len(posterior)):    
    sum = sum + posterior[j];  

for j in range(len(posterior)):    
    posterior[j] = posterior[j]/sum;  

print(init_belief)
print('\n')
print(RSS_column)
print('\n')
print(posterior)
print('\n')
