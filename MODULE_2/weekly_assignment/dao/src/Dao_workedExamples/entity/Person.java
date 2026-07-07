package Dao_workedExamples.entity;


public class Person {

    private String name;
    private int age;
    private String education;

    // Constructor
    public Person(String name, int age, String education) {
        this.name = name;
        this.age = age;
        this.education = education;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for age
    public int getAge() {
        return age;
    }

    // Setter for age
    public void setAge(int age) {
        this.age = age;
    }

    // Getter for education
    public String getEducation() {
        return education;
    }

    // Setter for education
    public void setEducation(String education) {
        this.education = education;
    }

    // toString()
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", education='" + education + '\'' +
                '}';
    }
}



