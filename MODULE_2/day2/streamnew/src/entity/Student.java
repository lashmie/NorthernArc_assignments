package entity;
public class Student {

    private String name;
    private int english;
    private int tamil;
    private int physics;
    private int chemistry;
    private int maths;
    private int biology;

    // Constructor
    public Student(String name, int english, int tamil, int physics,
                   int chemistry, int maths, int biology) {
        this.name = name;
        this.english = english;
        this.tamil = tamil;
        this.physics = physics;
        this.chemistry = chemistry;
        this.maths = maths;
        this.biology = biology;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getEnglish() {
        return english;
    }

    public int getTamil() {
        return tamil;
    }

    public int getPhysics() {
        return physics;
    }

    public int getChemistry() {
        return chemistry;
    }

    public int getMaths() {
        return maths;
    }

    public int getBiology() {
        return biology;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public void setTamil(int tamil) {
        this.tamil = tamil;
    }

    public void setPhysics(int physics) {
        this.physics = physics;
    }

    public void setChemistry(int chemistry) {
        this.chemistry = chemistry;
    }

    public void setMaths(int maths) {
        this.maths = maths;
    }

    public void setBiology(int biology) {
        this.biology = biology;
    }
}
