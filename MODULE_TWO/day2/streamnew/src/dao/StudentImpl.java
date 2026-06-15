package dao;

import entity.Student;

import java.util.ArrayList;
import java.util.List;


    public class StudentImpl implements StudentDao {

        private List<Student> students = new ArrayList<>();

        @Override
        public void addStudent(Student student) {
            students.add(student);
        }

        @Override
        public List<Student> getAllStudents() {
            return students;
        }

        @Override
//        public int getMaxMark(String subject) {
//
//            int max = 0;
//
//            for (Student student : students) {
//
//                int mark = getSubjectMark(student, subject);
//
//                if (mark > max) {
//                    max = mark;
//                }
//            }
//
//            return max;
//        }

        public int getMaxMark(String subject) {

            return students.stream()
                    .mapToInt(student -> getSubjectMark(student, subject))
                    .max()
                    .orElse(0);
        }

//        @Override
//        public Student getTopper(String subject) {
//
//            Student topper = null;
//            int max = 0;
//
//            for (Student student : students) {
//
//                int mark = getSubjectMark(student, subject);
//
//                if (mark > max) {
//                    max = mark;
//                    topper = student;
//                }
//            }
//
//            return topper;
//        }
@Override
public Student getTopper(String subject) {

    return students.stream()
            .max((s1, s2) ->
                    Integer.compare(
                            getSubjectMark(s1, subject),
                            getSubjectMark(s2, subject)
                    ))
            .orElse(null);
}

//        @Override
//        public double getAverage(String subject) {
//
//            if (students.isEmpty()) {
//                return 0;
//            }
//
//            int sum = 0;
//
//            for (Student student : students) {
//                sum += getSubjectMark(student, subject);
//            }
//
//            return (double) sum / students.size();
//        }
@Override
public double getAverage(String subject) {

    return students.stream()
            .mapToInt(student -> getSubjectMark(student, subject))
            .average()
            .orElse(0);
}

//        @Override
//        public int getStudentCount() {
//            return students.size();
//        }
@Override
public int getStudentCount() {
    return (int) students.stream().count();
}

//        @Override
//        public Student getClassTopper() {
//
//            Student topper = null;
//            int maxTotal = 0;
//
//            for (Student student : students) {
//
//                int total =
//                        student.getEnglish()
//                                + student.getTamil()
//                                + student.getPhysics()
//                                + student.getChemistry()
//                                + student.getMaths()
//                                + student.getBiology();
//
//                if (total > maxTotal) {
//                    maxTotal = total;
//                    topper = student;
//                }
//            }
//
//            return topper;
//        }
@Override
public Student getClassTopper() {

    return students.stream()
            .max(java.util.Comparator.comparingInt(student ->
                    student.getEnglish()
                            + student.getTamil()
                            + student.getPhysics()
                            + student.getChemistry()
                            + student.getMaths()
                            + student.getBiology()))
            .orElse(null);
}
//        @Override
//        public int countStudentsAboveAverage(String subject) {
//
//            double average = getAverage(subject);
//
//            int count = 0;
//
//            for (Student student : students) {
//
//                if (getSubjectMark(student, subject) > average) {
//                    count++;
//                }
//            }
//
//            return count;
//        }
@Override
public int countStudentsAboveAverage(String subject) {

    double average = getAverage(subject);

    return (int) students.stream()
            .filter(student ->
                    getSubjectMark(student, subject) > average)
            .count();
}

        // Helper Method
        private int getSubjectMark(Student student, String subject) {

            switch (subject.toLowerCase()) {

                case "english":
                    return student.getEnglish();

                case "tamil":
                    return student.getTamil();

                case "physics":
                    return student.getPhysics();

                case "chemistry":
                    return student.getChemistry();

                case "maths":
                    return student.getMaths();

                case "biology":
                    return student.getBiology();

                default:
                    throw new IllegalArgumentException("Invalid Subject");
            }
        }
    }

