import numpy as np
a = np.array([1,2,3])
print(a.sum())
print(a.mean()) 
print(a.min())
print(a.max())

marks = np.array([85, 90, 78, 92, 88])
average_marks = marks.mean()
print("Average marks:", average_marks)  
print("Minimum marks:", marks.min())    
print("Maximum marks:", marks.max())
print("Total marks:", marks.sum())
print("Number of students:", marks.size)
print(average_marks[1:3])


darray=np.array([[1,2,3],[4,5,6],[7,8,9] ])
print("2D Array Sum:", darray.sum())
print("2D Array Dimensions:", darray.ndim)
print("2D Array Shape:", darray.shape)
print("2D Array Size:", darray.size)
print("2D Array Data Type:", darray.dtype)

print(np.random.randint(1000,9999))
print(np.random.randint(1000,9999, size=(3,3)))
print(np.random.rand(3))