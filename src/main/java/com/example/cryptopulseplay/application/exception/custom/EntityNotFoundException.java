package com.example.cryptopulseplay.application.exception.custom;

// 필요시 사용

public class EntityNotFoundException extends jakarta.persistence.EntityNotFoundException {

    private final String entityName;
    private final Long entityId;

    public EntityNotFoundException(String entityName, Long entityId) {
        super(entityName + "with ID" + entityId + "not found");
        this.entityName = entityName;
        this.entityId = entityId;
    }
}
