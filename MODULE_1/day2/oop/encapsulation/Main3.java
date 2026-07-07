public class Main3 {
    public static void main(String[] args) {
        Person p = new Person();
        p.setFname("lavanya");
        p.setLname("elavarasan");
        Person.have();
        p.setAge(21);

        p.eat();
        p.walk();
        p.talk();
        p.showDetails();

    }
}
