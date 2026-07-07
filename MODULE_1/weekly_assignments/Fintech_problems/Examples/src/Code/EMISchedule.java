package Code;

public class EMISchedule {

    private int months;

    public EMISchedule(int months) {
        this.months = months;
    }

    public void generate() {
        System.out.println("EMI schedule for " + months + " months generated");
    }
}