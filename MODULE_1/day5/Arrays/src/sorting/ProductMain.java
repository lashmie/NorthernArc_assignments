package sorting;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
public class ProductMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 if u wanna print asc order of id\n" +
                "Enter 2 if u want to sort by name\n "+
                "Enter 3 if u want by name desc\n"+
                "Enter 4 category asc\n"+
                "Enter 5 category desc\n"+"Enter 6 price asc"+"Enter 7 for brand \n"+"Enter 8 for brand desc\n"+"Enter 9 for discount\n"+"Enter 10 rating asc\n"+"Enter 11 for rating desc\n"        );
    Product arr[]={
            new Product(4, "Kajal", "Cosmetics", 299.0, "Lakme", 12.0, 4.4),
            new Product(1, "Lipstick", "Cosmetics", 499.0, "Lakme", 10.0, 4.3),
            new Product(2, "Foundation", "Cosmetics", 999.0, "Maybelline", 15.0, 4.5),
            new Product(3, "Face Wash", "Cosmetics", 199.0, "Nivea", 5.0, 4.1),

    };

        System.out.println(Arrays.toString(arr));
        System.out.println(" Enter the option");
        int val =sc.nextInt();
    switch (val){

        case 1:
            Arrays.sort(arr);
            break;
        case 2://name asc
            Arrays.sort(arr, new Comparator<Product>(){
                public int compare(Product p1,Product p2){
                    return p1.getName().compareTo(p2.getName());
            }
        });
break;
        case 3://name desc
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return p2.getName().compareTo(p1.getName());
                }
            });
            break;
        case 4://category asc
            Arrays.sort(arr, new Comparator<Product>(){
                public int compare(Product p1,Product p2){
                    return p1.getCategory().compareTo(p2.getCategory());
                }
            });
            break;
        case 5://category desc
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return p2.getCategory().compareTo(p1.getCategory());
                }
            });
            break;
        case 6:
//            Arrays.sort(new Comparator<Product>(){
//                @Override
//                public int compare(Product p1,Product p2){
//                    return Double.compare(p1.getPrice(),p2.getPrice());
//                }
//            });
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Double.compare(p1.getPrice(), p2.getPrice());
                }
            });
            break;
//        case 7:
//            Arrays.sort(arr, new Comparator<Product>() {
//                @Override
//                public int compare(Product p1, Product p2) {
//                    return p1.getBrand().compareTo(p2.getBrand());
//                }
//            });1

//            break;
        // 7. Brand ascending
        case 7:
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return p1.getBrand().compareTo(p2.getBrand());
                }
            });
            break;

        // 8. Brand descending
        case 8:
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return p2.getBrand().compareTo(p1.getBrand());
                }
            });
            break;

        // 9. Discount ascending
        case 9:
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Double.compare(p1.getDiscount(), p2.getDiscount());
                }
            });
            break;

        // 10. Rating ascending
        case 10:
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Double.compare(p1.getRating(), p2.getRating());
                }
            });
            break;

        // 11. Rating descending
        case 11:
            Arrays.sort(arr, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Double.compare(p2.getRating(), p1.getRating());
                }
            });
            break;

        default:
            System.out.println("Invalid option");


    }
        System.out.println(Arrays.toString(arr));

    }
}
