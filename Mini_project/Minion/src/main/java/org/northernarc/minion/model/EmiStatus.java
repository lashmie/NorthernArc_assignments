package org.northernarc.minion.model;

/**
 * Enum representing different EMI payment statuses
 */
public enum EmiStatus {
    PENDING("EMI payment is pending"),
    PAID("EMI has been paid"),
    OVERDUE("EMI payment is overdue");

    private final String description;

    EmiStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

