package demo1;

import java.util.Comparator;

public class Person implements Comparable<Person> {

    private String name;
    private int age;
    private String lname;

    public Person(String name,String lname, int age) {
        this.name = name;
        this.lname=lname;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }


    @Override
    public int compareTo(Person o) {
        return this.age-o.age;
    }
}