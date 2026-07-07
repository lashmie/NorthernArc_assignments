package homeassignment;

import java.util.*;

public class ChildMain {

    public static void main(String[] args) {

        Childclass children[] = {
                new Childclass("Arun", "Kumar", "10/01/2010"),
                new Childclass("Priya", "Sharma", "21/05/2011"),
                new Childclass("Rahul", "Das", "15/08/2012"),
                new Childclass("Anita", "Verma", "02/12/2013"),
                new Childclass("Kiran", "Rao", "10/01/2010")
        };

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter 1 for Ascending First Name");
        System.out.println("Enter 2 for Descending First Name");
        System.out.println("Enter 3 for Ascending Last Name");
        System.out.println("Enter 4 for Descending Last Name");
        System.out.println("Enter 5 for Ascending DOB");
        System.out.println("Enter 6 for Descending DOB");

        int choice = sc.nextInt();

        switch (choice) {

            case 1:
                System.out.println("\nAscending First Name");
                Arrays.sort(children,
                        (c1, c2) -> c1.getFname().compareTo(c2.getFname()));
                break;

            case 2:
                System.out.println("\nDescending First Name");
                Arrays.sort(children,
                        (c1, c2) -> c2.getFname().compareTo(c1.getFname()));
                break;

            case 3:
                System.out.println("\nAscending Last Name");
                Arrays.sort(children,
                        (c1, c2) -> c1.getLname().compareTo(c2.getLname()));
                break;

            case 4:
                System.out.println("\nDescending Last Name");
                Arrays.sort(children,
                        (c1, c2) -> c2.getLname().compareTo(c1.getLname()));
                break;

            case 5:
                System.out.println("\nAscending DOB");

                Arrays.sort(children, (c1, c2) -> {

                    String[] d1 = c1.getDob().split("/");
                    String[] d2 = c2.getDob().split("/");

                    int year1 = Integer.parseInt(d1[2]);
                    int year2 = Integer.parseInt(d2[2]);

                    if (year1 != year2)
                        return year1 - year2;

                    int month1 = Integer.parseInt(d1[1]);
                    int month2 = Integer.parseInt(d2[1]);

                    if (month1 != month2)
                        return month1 - month2;

                    int day1 = Integer.parseInt(d1[0]);
                    int day2 = Integer.parseInt(d2[0]);

                    return day1 - day2;
                });
                break;

            case 6:
                System.out.println("\nDescending DOB");

                Arrays.sort(children, (c1, c2) -> {

                    String[] d1 = c1.getDob().split("/");
                    String[] d2 = c2.getDob().split("/");

                    int year1 = Integer.parseInt(d1[2]);
                    int year2 = Integer.parseInt(d2[2]);

                    if (year1 != year2)
                        return year2 - year1;

                    int month1 = Integer.parseInt(d1[1]);
                    int month2 = Integer.parseInt(d2[1]);

                    if (month1 != month2)
                        return month2 - month1;

                    int day1 = Integer.parseInt(d1[0]);
                    int day2 = Integer.parseInt(d2[0]);

                    return day2 - day1;
                });
                break;

            default:
                System.out.println("Invalid Choice");
                return;
        }

        System.out.println("\nSorted Records:");

        for (Childclass child : children) {
            System.out.println(child);
        }

        sc.close();
    }
}