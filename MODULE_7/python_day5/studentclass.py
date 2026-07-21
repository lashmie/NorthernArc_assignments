from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import declarative_base, sessionmaker

# Step 1 - Establish a database connection
engine = create_engine(
    "postgresql://postgres:12345@localhost:5432/northernarc"
)

# Step 2 - Initialize the base object
Base = declarative_base()

# Step 3 - Create session
Session = sessionmaker(bind=engine)
session = Session()

# Step 4 - Define Schema
class Student(Base):
    __tablename__ = 'students'

    id = Column(Integer, primary_key=True)
    name = Column(String(50))
    age = Column(Integer)
    course = Column(String(50))

    def __repr__(self):
        return f"Student(id={self.id}, name={self.name}, age={self.age}, course={self.course})"

# Step 5 - Create table
Base.metadata.create_all(engine)
print("Table created successfully")

# Step 6 - Insert a record
student = Student(name="Lavanya", age=22, course="Python")
session.add(student)
session.commit()
print("Inserted successfully")

# Step 7 - Fetch all data
print("Display all students")
rows = session.query(Student).all()

for row in rows:
    print(row)

# Step 8 - Fetch only names
print("Student Names")
student_names = session.query(Student.name).all()

for name in student_names:
    print(name)

# Step 9 - Filter by ID > 2
print("Students with ID > 2")
students_gt_2 = session.query(Student).filter(Student.id > 2).all()

for student in students_gt_2:
    print(student)

# Step 10 - Filter by specific values
print("Specific Student")
specific_student = session.query(Student).filter_by(
    name="Lavanya",
    age=22
).first()

print(specific_student)

# Step 11 - Update
existing_student = session.query(Student).filter_by(id=1).first()

if existing_student:
    print("Before Update:", existing_student)
    existing_student.course = "Data Science"
    session.commit()
    print("After Update:", existing_student)

# Step 12 - Delete
student_to_delete = session.query(Student).filter_by(id=2).first()

if student_to_delete:
    session.delete(student_to_delete)
    session.commit()
    print("Student deleted successfully")

# Final Display
print("Final Student Data")
rows = session.query(Student).all()

for row in rows:
    print(row)