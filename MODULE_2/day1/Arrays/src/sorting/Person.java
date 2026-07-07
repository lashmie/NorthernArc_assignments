package sorting;

public class Person implements Comparable<Person>{
    String name;
    String lname;
    int age;
    Person(String name,int age,String lname){
        this.name=name;
        this.age=age;
        this.lname=lname;
    }

    public String toString(){
        return name +" "+lname+""+age ;
    }

//    public int compareTo(Person o){
////        return this.age-o.age;
//        return o.age-this.age;//descending
//    }

//ascending
//    public int compareTo(Person o){
//    return this.name.compareTo(o.name);
//    }
    //descending
    public int compareTo(Person o){
        return o.name.compareTo(this.name);
    }



}
