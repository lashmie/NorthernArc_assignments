 public class Chair{
private String material;
private String color;
private double weight;

public Chair(String material, String color, double weight) {
    this.material = material;
    this.color = color;
    this.weight = weight;
}

public void adjustHeight() {
    System.out.println(material + " chair height is being adjusted");
}

public void getDetails() {
    System.out.println(
        "Material: " + material + "\n" +
        "Color: " + color + "\n" +
        "Weight: " + weight + "\n"
    );
}

public String getMaterial() {
    return material;
}

public void setMaterial(String material) {
    this.material = material;
}

public String getColor() {
    return color;
}

public void setColor(String color) {
    this.color = color;
}

public double getWeight() {
    return weight;
}

public void setWeight(double weight) {
    this.weight = weight;
}

}
