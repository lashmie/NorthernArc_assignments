package demo1.ArrayList1;

import demo1.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class arrayListPerson {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("vinya","kashi",19));
        list.add(new Person("kamya","yadav",21));
        list.add(new Person("kamya","yadav",21));
        System.out.println(list);
        list.remove(2);
        System.out.println(list);
        Collections.sort(list);
        System.out.println(list);

        Arrays.sort(list,ageComparator);


    }
}
