package sorting;

import java.util.Comparator;

public class Product implements Comparable<Product> {
    private int id;
    private String name;
    private String category;
    private double price;
    private String brand;
    private double discount;
    private double rating;

    Product(int id,String name,String category,double price,String brand,double discount,double rating){
        this.id=id;
        this.name=name;
        this.category=category;
        this.price=price;
        this.brand=brand;
        this.discount=discount;
        this.rating=rating;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + " " + ", name='" + name + "" +
                ", category='" + category + " " +
                ", price=" + price +
                ", brand='" + brand + " " +
                ", discount=" + discount +
                ", rating=" + rating +
                '}';
    }

    public int compareTo(Product o){
        return this.id-o.id;
    }


}
