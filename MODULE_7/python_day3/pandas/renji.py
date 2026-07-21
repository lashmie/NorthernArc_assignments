import pandas as pd
#Step 1: Create a DataFrame
employees = {
    'Name': ['Alice', 'Bob', 'Charlie', 'David'],
    'Age': [25, 30, 35, 40],
    'Department': ['HR', 'Finance', 'IT', 'Marketing']
}


#Single dimensional array - series
a=[10,20,30,40,50]
series=pd.Series(a)
print(series)  


#DATAFRAME
df=pd.DataFrame(employees)
print(df)

#write the dataframe to a csv file
df.to_csv("employees.csv",index=False) #index=False to avoid writing the index to the csv file

age=df['Age'] #accessing a column
print(age)

ageanddept=df[['Age','Department']] #accessing multiple columns
print(ageanddept)

#Filter data
employeesOfIT=df[df['Department']=='IT'] #filtering data based on a condition
print(employeesOfIT)

employeesOfHRandAgeAbove30=df[(df['Department']=='HR') & (df['Age']>30)] #filtering data based on multiple conditions
print(employeesOfHRandAgeAbove30)


data={
    "name":[None,"Bob","Charlie","David"],
    "age":[25,30,None,40],
}

dataframe=pd.DataFrame(data)
print(dataframe.isnull().sum()) #check for null values in the dataframe
print(dataframe.fillna(0)) #fill null values with 0


print(df.head()) #first 5 rows of the dataframe
print(df.tail())# last 5 rows of the dataframe

print(df.shape) #number of rows and columns in the dataframe
print(df.info()) #information about the dataframe

print(df.describe()) #statistical summary of the dataframe