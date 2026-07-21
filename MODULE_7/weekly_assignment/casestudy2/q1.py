import pandas as pd
df1=pd.read_csv("funds.csv")
df2=pd.read_csv("investors.csv")
df3=pd.read_csv("nav_history.csv")
df4=pd.read_csv("transactions.csv")
print(df1.head())
print(df2.head())
print(df3.head())
print(df4.head())