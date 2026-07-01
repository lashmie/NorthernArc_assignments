package org.northernarc.testdemo;

import org.junit.jupiter.api.*;

import org.northernarc.testdemo.service.CalculatorService;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CalculatorServiceTest {
    private static CalculatorService cal;
    @BeforeAll
    static void init(){
        System.out.println("Initializing the test....");
        cal=new CalculatorService();
    }

    @BeforeEach
    void setUp(){
        System.err.println("Setting up the test....");
    }
    @Test
    void testAdd(){
        //CalculatorService cal = new CalculatorService();
        int actualResult = cal.add(2, 3);
        int expectedResult=5;
        assert actualResult == expectedResult;
        assertEquals(6,cal.add(3,3));
    }
    @Test
    void testSub(){
        int actualResult =cal.subtract(5, 3);
        int expectedResult=2;
        assert actualResult == expectedResult;
    }

    @Test
    void testMul(){
        int actualResult =cal.multiply(4,4);
       // int expectedResult=16;
        assertEquals(16,cal.multiply(4,4));
    }
    @Test
    void testDiv(){
        double actualResult =cal.divide(4,4);
        // int expectedResult=16;
        assertEquals(1,cal.divide(4,4));
    }
    @AfterEach
    void tearDown(){
        System.out.println("Cleaning up the test....");
    }
    @AfterAll
    static void teardown(){
        cal=null;
        System.out.println("Cleared the object.........");
    }
}
