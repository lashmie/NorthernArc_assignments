package self;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class nbfcMain {
    //outer class ... this has to made static.. other wise we have to create object for this ..
    static class applicantnamedesc implements Comparator<nbfc> {
        //if we remove the static we can see the error

        @Override
        public int compare(nbfc o1, nbfc o2) {
            return o2.getApplicant_name().compareTo(o1.getApplicant_name());
        }
    }

    public static void main(String[] args) {
        class applicantnameasc implements Comparator<nbfc> {//inner class

            @Override
            public int compare(nbfc o1, nbfc o2) {
                return o1.getApplicant_name().compareTo(o2.getApplicant_name());
            }
        }

        Scanner sc = new Scanner(System.in);
        nbfc[] loan = {
                new nbfc("L001", "Arun", 2022),
                new nbfc("L002", "Bala", 2023),
                new nbfc("L003", "Charan", 2021),
                new nbfc("L004", "Divya", 2024),
                new nbfc("L005", "Eshwar", 2020)
        };

        boolean running = true;
        while (running) {
            System.out.println("Enter 1 to sort by loan id \n2.enter 2 desc loanid \n enter 3 asc applicant name \n enter 4 desc applicant name \n enter 5 for loan approval year\n enter 6 for loan approavl desc \n enter 7 to exit \n :");
            int val = sc.nextInt();
            switch (val) {
                case 1:
                    Arrays.sort(loan);
                    System.out.println(" sortedby loan id ");
                    break;
                case 2:
                    Arrays.sort(loan, new loaniddesc());
                    System.out.println(" sortedby loan id desc");
                    break;
                case 3:
                    Arrays.sort(loan, new applicantnameasc());
                    System.out.println("sorted by applicant name asc");
                    break;
                case 4:
                    Arrays.sort(loan, new applicantnamedesc());
                    System.out.println("sorted by applicant name desc");
                    break;
                case 5:
                    Arrays.sort(loan, new Comparator<nbfc>(){
                    @Override
                    public int compare(nbfc o1, nbfc o2) {
                        return o1.getLoanapprovalyear()-o2.getLoanapprovalyear();
                    }
                });
                    System.out.println("sorted by loanyear asc");
                    System.out.println(loan);
                    break;
                case 6:
                    Arrays.sort(loan, new Comparator<nbfc>() {
                        @Override
                        public int compare(nbfc o1, nbfc o2) {
                            return o2.getLoanapprovalyear()-o1.getLoanapprovalyear();
                        }
                    });
                    System.out.println("sorted by loanyear desc");
                    break;
                case 7:
                    System.out.println("Exiting");
                    running=false;
                    break;
                default:
                    System.out.println("Invalid number");
                    continue;


            }
        }


    }
}

//syntax
//Loan[] loans = new Loan[5];
//
//loans[0] = new Loan("L001", "Arun", 2022);
//loans[1] = new Loan("L002", "Bala", 2023);
//loans[2] = new Loan("L003", "Charan", 2021);
//loans[3] = new Loan("L004", "Divya", 2024);
//loans[4] = new Loan("L005", "Eshwar", 2020);
