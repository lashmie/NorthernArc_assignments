from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import declarative_base
from sqlalchemy.orm import sessionmaker
 
 
# Step 1 - Establish a database connection
engine = create_engine(
     "postgresql://postgres:12345@localhost:5432/northernarc"
)
 
# Step 2 - initialise the base objects
Base = declarative_base()
 
# Step 3 - Fetch session objects
Session = sessionmaker(bind=engine)
session = Session()
# Step 4 - define Schema
class Scan(Base):
    __tablename__ = 'scans'
    id=Column(Integer, primary_key=True)
    domain_name=Column(String(50))
    brokenlinks=Column(Integer)
 
    def __repr__(self):
        return f"Scan(id={self.id},domain_name={self.domain_name}, brokenlinks={self.brokenlinks})"
 
#Step 5 - Create table in DB
Base.metadata.create_all(engine)
print(" Table created Successfully")
 
 
#Step 6  - Insert a record
scan = Scan(domain_name='amazon.com', brokenlinks=100)
session.add(scan) # this performs insert into table
session.commit()
print(" Inserted successfully")
 
# Step 7 - Fetch  all data
print(" display data")
rows = session.query(Scan).all()
for row in rows:
    print(row)
 
# Step 8 - Filter data
domain_names = session.query(Scan.domain_name).all()
for domain_name in domain_names:
    print(domain_name)
 
# Step 8 - Filter data by id
idsGt4 = session.query(Scan).filter(Scan.id > 4).all()
for id in idsGt4:
    print(id)
 
print("Specific Domain & lnks = 100")
# Step 8 - Filter data by name
specificDomain = session.query(Scan).filter_by(domain_name = 'amazon.com',brokenlinks=100 ).first()
#for domain in specificDomain:
print(specificDomain)
 
#Update
existingDomain = session.query(Scan).filter_by(id = 8).first()
print(existingDomain)
existingDomain.domain_name="flipkart.com"
session.commit()
print(existingDomain)
 
#Delete
duplicateDomainName = session.query(Scan).filter_by(id = 5).first()
session.delete(duplicateDomainName)
session.commit()
 
print(" display data -> at the end")
rows = session.query(Scan).all()
for row in rows:
    print(row)
 
 
#session.add()
#session.add_all()