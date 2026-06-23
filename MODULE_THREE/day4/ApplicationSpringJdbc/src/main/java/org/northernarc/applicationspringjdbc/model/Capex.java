package org.northernarc.applicationspringjdbc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "capex")
public class Capex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long financialId;

    private String companyName;
    private String assetType;
    private int tenure;
    private double amount;
    private String vendor;
    private String status;

    public Capex() {
    }

    public Capex(Long financialId, String companyName, String assetType, int tenure, double amount, String vendor, String status) {
        this.financialId = financialId;
        this.companyName = companyName;
        this.assetType = assetType;
        this.tenure = tenure;
        this.amount = amount;
        this.vendor = vendor;
        this.status = status;
    }

    public Long getFinancialId() {
        return financialId;
    }

    public void setFinancialId(Long financialId) {
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

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

