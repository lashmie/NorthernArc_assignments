package newfeatures.functional.Comparator;

import java.util.function.*;

public class MyFunctional {
    public static void main(String[] args) {

        class MyFunction implements Function<String, Integer> {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        }

        MyFunction f1 = new MyFunction();
        System.out.println(f1.apply("hello"));


        Function<Integer, String> f2 = n -> "The number is " + n;
        System.out.println(f2.apply(10));

        BiFunction<Integer, Integer, Integer> f3 = (a, b) -> a + b;
        System.out.println(f3.apply(3, 2));

        //test one input and boolena output--test
        Predicate<Integer> p1 = (n) -> n % 2 == 0;
        System.out.println(p1.test(4));

        //one input and one output--apply
        UnaryOperator<Integer> u1 = (n) -> n * n;
        System.out.println(u1.apply(3));

        //twp input adn one output all same type
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        System.out.println(multiply.apply(4, 5));

        //consumer take one argument and return nothing consumer
        Consumer<String> c = s -> System.out.println(s);
        c.accept("Hello");

        //Takes no arguments and returns a value.

        Supplier<String> s = () -> "Welcome";

        System.out.println(s.get());


    }
}
