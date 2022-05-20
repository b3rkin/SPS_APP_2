from Process_func import *

cell_number = 5 # Number of cellss that are used in the model

# Format the training data by removing [] and adding missing commas.
for i in range(cell_number):
    dir = "TrainingData/trainMonday16/saved_data_cell" + str(i+1)+ "_Monday_East.txt"
    cleanup_csv(dir)
    format_txt(dir)

# Empty the directory where the pmfs will be stored
if (len(os.listdir("model"))!=0):
    files = glob.glob('model/*')
    for f in files:
        os.remove(f)

# Create the pmfs for each MAC address and insert into the model/ directory
for i in range(cell_number):
    dir = "TrainingData/trainMonday16/saved_data_cell" + str(i+1) + "_Monday_East.txt"
    create_pmf(dir,i)

# Clean the model files by deleting [] and "" and add missing cells to the radar maps
files = glob.glob('model/*')
for f in files:
    add_missing_cells(f, cell_number) # This function must come before cleanup_csv
    cleanup_csv(f)  

