public class Restaurant {
private String name;
private String cuisine;
private int rating;

public Restaurant(String name, String cuisine, int rating) {
    this.name = name;
    this.cuisine = cuisine;
    this.rating = rating;
}

public void serveFood() {
    System.out.println(name + " is serving food");
}

public void getDetails() {
    System.out.println(
        "Name: " + name + "\n" +
        "Cuisine: " + cuisine + "\n" +
        "Rating: " + rating + "/5\n"
    );
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getCuisine() {
    return cuisine;
}

public void setCuisine(String cuisine) {
    this.cuisine = cuisine;
}

public int getRating() {
    return rating;
}

public void setRating(int rating) {
    this.rating = rating;
}

}
