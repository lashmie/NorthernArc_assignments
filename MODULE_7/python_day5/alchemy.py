from sqlalchemy import create_engine, Column, Integer, String, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship, sessionmaker

#from sqlalchemy import create_engine
engine=create_engine("postgresql://postgres:1234@localhost:5432/postgres")

#step 2-
Base=declarative_base()

#step 3- define schema
Base = declarative_base()

class Scan(Base):
    __tablename__ = 'scans'
    scan_id = Column(Integer, primary_key=True)
    domain = Column(String)
    brokenlinks = Column(Integer)

    def __init__(self, scan_id, domain, brokenlinks):
        self.scan_id = scan_id
        self.domain = domain
        self.brokenlinks = brokenlinks
