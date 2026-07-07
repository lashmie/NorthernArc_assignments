package association;

public class Main2 {
    public static void main(String[] args) {
        Person2 p1= new Person2("Lavanya","lashmi",21);
        Address add = new Address("123","laks","vpm","tn","602");
        p1.setAddress(add);
        System.out.println(p1);
    }
}
