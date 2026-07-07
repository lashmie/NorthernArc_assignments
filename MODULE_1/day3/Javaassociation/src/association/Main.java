package association;

public class Main {
    public static void main(String[] args) {
        Person p1= new Person("Lavanya","lashmi",21);
        Passport pass = new Passport("123","India","1/2/28","1/2/97");
        pass.person(p1);
        System.out.println(pass);

    }
}
