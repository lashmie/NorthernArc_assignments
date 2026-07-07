package com.northernarc.capex.nbfc;


import com.northernarc.capex.nbfc.controller.CapexFinanceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Capexui implements CommandLineRunner {
    @Autowired
    CapexFinanceController capex;
    public static void main(String[] args) {

        SpringApplication.run(Capexui.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Program is running..........");
        capex.showMenu();
    }
}

