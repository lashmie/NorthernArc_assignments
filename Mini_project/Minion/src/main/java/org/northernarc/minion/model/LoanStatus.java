package org.northernarc.minion.model;

/**
 * Enum representing different loan statuses
 */
public enum LoanStatus {
    ACTIVE("Loan is currently active"),
    CLOSED("Loan has been closed"),
    DEFAULTED("Loan is in default status");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

