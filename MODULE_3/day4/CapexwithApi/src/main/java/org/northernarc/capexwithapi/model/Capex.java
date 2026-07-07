package org.northernarc.capexwithapi.model;

public class Capex {
    private int financialId;
    private String companyName;
    private String assetType;
    private double cost;
    private int tenure;

    public Capex(int financialId, String companyName, String assetType, double cost, int tenure) {
        this.financialId = financialId;
        this.companyName = companyName;
        this.assetType = assetType;
        this.cost = cost;
        this.tenure = tenure;
    }

    public int getFinancialId() {
        return financialId;
    }

    public void setFinancialId(int financialId) {
        this.financialId = financialId;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    @Override
    public String toString() {
        return "Capex{" +
                "financialId=" + financialId +
                ", companyName='" + companyName + '\'' +
                ", assetType='" + assetType + '\'' +
                ", cost=" + cost +
                ", tenure=" + tenure +
                '}';
    }
}
