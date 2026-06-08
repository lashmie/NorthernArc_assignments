public class Doctor {
private String name;
private String specialization;
private int experience;

public Doctor(String name, String specialization, int experience) {
    this.name = name;
    this.specialization = specialization;
    this.experience = experience;
}

public void diagnose() {
    System.out.println(name + " is diagnosing the patient");
}

public void treat() {
    System.out.println(name + " is treating the patient");
}

public void getDetails() {
    System.out.println(
        "Name: " + name + "\n" +
        "Specialization: " + specialization + "\n" +
        "Experience: " + experience + " years\n"
    );
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getSpecialization() {
    return specialization;
}

public void setSpecialization(String specialization) {
    this.specialization = specialization;
}

public int getExperience() {
    return experience;
}

public void setExperience(int experience) {
    this.experience = experience;
}

}