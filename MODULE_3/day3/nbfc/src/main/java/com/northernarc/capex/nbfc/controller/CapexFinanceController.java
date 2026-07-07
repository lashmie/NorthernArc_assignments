
package com.northernarc.capex.nbfc.controller;

import com.northernarc.capex.nbfc.dao.CapexFinanceDao;
import com.northernarc.capex.nbfc.model.CapexFinance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class CapexFinanceController {
@Autowired
    private  CapexFinanceDao dao;
@Autowired
    private Scanner scanner;


    public void showMenu() {

        while (true) {

            System.out.println("\n===== CAPEX FINANCE MENU =====");
            System.out.println("1. Save");
            System.out.println("2. Update");
            System.out.println("3. Find By Id");
            System.out.println("4. Find All");
            System.out.println("5. Delete By Id");
            System.out.println("6. Delete All");
            System.out.println("7. Exit");

            System.out.print("Enter Choice : ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1 -> save();

                case 2 -> update();

                case 3 -> findById();

                case 4 -> findAll();

                case 5 -> deleteById();

                case 6 -> deleteAll();

                case 7 -> {
                    System.out.println("Thank You");
                    System.exit(0);
                }

                default -> System.out.println("Invalid Choice");
            }
        }
    }

    private void save() {

        CapexFinance finance = new CapexFinance();

        System.out.print("Finance Id : ");
        finance.setFinanceId(scanner.nextInt());

        scanner.nextLine();

        System.out.print("Company Name : ");
        finance.setCompanyName(scanner.nextLine());

        System.out.print("Asset Type : ");
        finance.setAssetType(scanner.nextLine());

        System.out.print("Asset Cost : ");
        finance.setAssetCost(scanner.nextDouble());

        System.out.print("Tenure Months : ");
        finance.setTenureMonths(scanner.nextInt());

        System.out.print("Approved (true/false) : ");
        finance.setApproved(scanner.nextBoolean());

        dao.save(finance);
    }

    private void update() {

        CapexFinance finance = new CapexFinance();

        System.out.print("Finance Id : ");
        finance.setFinanceId(scanner.nextInt());

        scanner.nextLine();

        System.out.print("Company Name : ");
        finance.setCompanyName(scanner.nextLine());

        System.out.print("Asset Type : ");
        finance.setAssetType(scanner.nextLine());

        System.out.print("Asset Cost : ");
        finance.setAssetCost(scanner.nextDouble());

        System.out.print("Tenure Months : ");
        finance.setTenureMonths(scanner.nextInt());

        System.out.print("Approved (true/false) : ");
        finance.setApproved(scanner.nextBoolean());

        dao.update(finance);
    }

    private void findById() {

        System.out.print("Enter Finance Id : ");

        int id = scanner.nextInt();

        CapexFinance finance = dao.findById(id);

        if (finance == null) {
            System.out.println("Record Not Found");
            return;
        }

        System.out.println(finance);
    }

    private void findAll() {

        List<CapexFinance> list = dao.findAll();

        if (list.isEmpty()) {
            System.out.println("No Records Found");
            return;
        }

        for (CapexFinance finance : list) {
            System.out.println(finance);
        }
    }

    private void deleteById() {

        System.out.print("Enter Finance Id : ");

        int id = scanner.nextInt();

        dao.deleteById(id);
    }

    private void deleteAll() {

        dao.deleteAll();
    }
}