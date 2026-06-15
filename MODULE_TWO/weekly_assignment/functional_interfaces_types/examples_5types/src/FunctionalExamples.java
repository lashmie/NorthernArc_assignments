import java.util.function.*;
import java.util.Comparator;

public class FunctionalExamples {

    public static void functionDemo() {
        Function<String, Integer> f = s -> s.length();
        System.out.println("Function: " + f.apply("Java"));
    }

    public static void biFunctionDemo() {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("BiFunction: " + add.apply(10, 20));
    }

    public static void predicateDemo() {
        Predicate<Integer> even = x -> x % 2 == 0;
        System.out.println("Predicate: " + even.test(10));
    }

    public static void unaryOperatorDemo() {
        UnaryOperator<Integer> square = x -> x * x;
        System.out.println("UnaryOperator: " + square.apply(5));
    }

    public static void binaryOperatorDemo() {
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        System.out.println("BinaryOperator: " + multiply.apply(5, 4));
    }

    public static void consumerDemo() {
        Consumer<String> consumer = s -> System.out.println("Consumer: " + s);
        consumer.accept("Hello Java");
    }

    public static void supplierDemo() {
        Supplier<String> supplier = () -> "Generated Loan ID: LN1001";
        System.out.println("Supplier: " + supplier.get());
    }

    public static void comparatorDemo() {
        Comparator<Integer> comp = (a, b) -> a - b;

        System.out.println("Comparator (10,20): " + comp.compare(10, 20));
        System.out.println("Comparator (20,10): " + comp.compare(20, 10));
        System.out.println("Comparator (10,10): " + comp.compare(10, 10));
    }

    public static void main(String[] args) {

        functionDemo();
        biFunctionDemo();
        predicateDemo();
        unaryOperatorDemo();
        binaryOperatorDemo();
        consumerDemo();
        supplierDemo();
        comparatorDemo();
    }
}