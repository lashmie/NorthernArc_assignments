package demo.model;

import jakarta.annotation.PostConstruct;

public class CapexFinance {

    private int financeId;
    private String companyName;
    private String assetType;
    private double assetCost;
    private int tenureMonths;
    private boolean approved;

    // Default Constructor
    public CapexFinance() {
        System.out.println("Default Constructor Called");
    }

    // Parameterized Constructor
    public CapexFinance(int financeId, String companyName,
                        String assetType, double assetCost,
                        int tenureMonths, boolean approved) {
        this.financeId = financeId;
        this.companyName = companyName;
        this.assetType = assetType;
        this.assetCost = assetCost;
        this.tenureMonths = tenureMonths;
        this.approved = approved;
    }

    // Copy Constructor
    public CapexFinance(CapexFinance other) {
        this.financeId = other.financeId;
        this.companyName = other.companyName;
        this.assetType = other.assetType;
        this.assetCost = other.assetCost;
        this.tenureMonths = other.tenureMonths;
        this.approved = other.approved;
    }

    @PostConstruct
    public void init() {
        System.out.println("CAPEX Finance Bean Initialized");
    }

    // Getters and Setters

    public int getFinanceId() {
        return financeId;
    }

    public void setFinanceId(int financeId) {
        this.financeId = financeId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public double getAssetCost() {
        return assetCost;
    }

    public void setAssetCost(double assetCost) {
        this.assetCost = assetCost;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "CapexFinance{" +
                "financeId=" + financeId +
                ", companyName='" + companyName + '\'' +
                ", assetType='" + assetType + '\'' +
                ", assetCost=" + assetCost +
                ", tenureMonths=" + tenureMonths +
                ", approved=" + approved +
                '}';
    }
}