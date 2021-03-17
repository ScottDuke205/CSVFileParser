# CSVFileParser

Availity received enrollment files from various benefits management and enrollment solutions. Most of these files are typical in EDI format. However, there are 
some files in CSV format. For the files in CSV format, write a program that will read the content of the file and separate enrollees by insurance company in its 
own file. Additionally, sort the contents of each file by last and first name (ascending). Lastly, if there are duplicate User Ids for the same insurance Company, 
then only the record with the highest version should be included.

Input file: sample.csv

Output files: Aetna.csv, BCBS.csv, Humana.csv and UnitedHealthcare.csv
