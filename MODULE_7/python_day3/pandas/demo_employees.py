import pandas as pd
from datetime import datetime


def main():
    # Read CSV into DataFrame
    df = pd.read_csv('employees.csv', parse_dates=['HireDate'])

    print('\n=== DataFrame loaded ===')
    print(df)

    # Series examples
    print('\n=== Series examples ===')
    # Create a Series from a column
    salaries = df['Salary']
    print('\n- Salary Series:')
    print(salaries)

    # Simple Series operations
    print('\n- Salary mean, min, max:')
    print('mean:', salaries.mean(), 'min:', salaries.min(), 'max:', salaries.max())

    # Create a Series with custom index
    names = pd.Series(df['Name'].values, index=df['EmployeeID'])
    print('\n- Names Series with EmployeeID as index:')
    print(names)

    # Filter using Series boolean mask
    high_paid = df['Salary'] > 80000
    print('\n- High paid mask:')
    print(high_paid)

    print('\n- High paid employees:')
    print(df[high_paid])

    # DataFrame actions
    print('\n=== DataFrame actions ===')

    # Select columns
    print('\n- Select Name and Department:')
    print(df[['Name', 'Department']])

    # Add a new column
    df['SalaryK'] = (df['Salary'] / 1000).round(1)
    print('\n- Added SalaryK column:')
    print(df[['Name', 'Salary', 'SalaryK']])

    # Group by Department and aggregate
    print('\n- Group by Department (count & avg salary):')
    dept_stats = df.groupby('Department').agg(Count=('EmployeeID', 'count'), AvgSalary=('Salary', 'mean'))
    print(dept_stats)

    # Sort by Salary descending
    print('\n- Top earners sorted by Salary:')
    print(df.sort_values('Salary', ascending=False).head(3))

    # Filtering rows
    recent_hires = df[df['HireDate'] >= pd.to_datetime('2019-01-01')]
    print('\n- Recent hires since 2019-01-01:')
    print(recent_hires)

    # Modify values: give a 5% raise to Sales dept
    df.loc[df['Department'] == 'Sales', 'Salary'] *= 1.05
    df['Salary'] = df['Salary'].round(2)
    print('\n- After 5% raise to Sales:')
    print(df[['Name', 'Department', 'Salary']])

    # Drop a column
    df2 = df.drop(columns=['FullTime'])
    print('\n- Dropped FullTime column:')
    print(df2.head())

    # Export a transformed CSV
    out_path = 'employees_transformed.csv'
    df.to_csv(out_path, index=False)
    print(f'\nTransformed data written to {out_path}')


if __name__ == '__main__':
    main()
