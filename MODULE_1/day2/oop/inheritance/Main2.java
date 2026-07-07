public class Main2 extends person {
    public static void main(String[] args) {
        //upcasting
        person p1= new Employee();
        p1.setFname("alice");
        p1.setlname("ela");
        p1.setAge(98);

        //downcasting
        ((Employee) p1).setEmpId("12");
        ((Employee )p1).getEmpid();

    }
}
