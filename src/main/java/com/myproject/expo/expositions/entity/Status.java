package com.myproject.expo.expositions.entity;

public enum Status {
    ACTIVE(1L, "active"), CANCELED(2L, "canceled"), BLOCKED(3L, "blocked");

    private final Long statusId;
    private final String name;

    Status(Long statusId, String name) {
        this.statusId = statusId;
        this.name = name;
    }

    public Long getStatusId() {
        return statusId;
    }

    public String getName() {
        return name;
    }
}
