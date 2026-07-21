package org.northernarc.minion.model;

/**
 * Enum representing different user roles in the system
 */
public enum Role {
    USER("Regular user with limited access"),
    MANAGER("Manager with moderate access"),
    ADMIN("Administrator with full access");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}