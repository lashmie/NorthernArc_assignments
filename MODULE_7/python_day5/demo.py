import psycopg2
conn = psycopg2.connect(
    host="localhost",   
    database="northernarc",
    user="postgres",
    password="12345"
)   
curr = conn.cursor()
curr.execute("SELECT * FROM employee_python")


