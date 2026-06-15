package dao;

import entity.Student;
import java.util.List;
//
//public interface StudentDao {
//
//    // Add a student
//    void addStudent(Student student);
//
//    // Get all students
//    List<Student> getAllStudents();
//
//    // Maximum mark in a subject
//    int getMaxEnglishMark();
//    int getMaxTamilMark();
//    int getMaxPhysicsMark();
//    int getMaxChemistryMark();
//    int getMaxMathsMark();
//    int getMaxBiologyMark();
//
//    // Subject toppers
//    Student getEnglishTopper();
//    Student getTamilTopper();
//    Student getPhysicsTopper();
//    Student getChemistryTopper();
//    Student getMathsTopper();
//    Student getBiologyTopper();
//
//    // Class topper (highest total mark)
//    Student getClassTopper();
//
//    // Subject averages
//    double getEnglishAverage();
//    double getTamilAverage();
//    double getPhysicsAverage();
//    double getChemistryAverage();
//    double getMathsAverage();
//    double getBiologyAverage();
//
//    // Count students
//    int getStudentCount();
//
//    // Count students above Physics average
//    int countStudentsAbovePhysicsAverage();
//}

public interface StudentDao {

    void addStudent(Student student);

    List<Student> getAllStudents();

    int getMaxMark(String subject);

    Student getTopper(String subject);

    double getAverage(String subject);

    int getStudentCount();

    Student getClassTopper();

    int countStudentsAboveAverage(String subject);
}