package com.ecommerce.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private long fieldId;
    private String fieldValue; // Added to hold string values (like email or name)

    // 1. Constructor for ID-based search (e.g., Category not found with id: 5)
    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId) {
        super(String.format("%s not found with %s : %d", resourceName, fieldName, fieldId));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }

    // 2. Constructor for String-based search (e.g., User not found with email: abc@test.com)
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // 3. (Optional) Your specific Category constructor, mapped to the main logic
    // You can usually just use Constructor #1 instead of this.
    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId, boolean dummy) {
        this(resourceName, fieldName, fieldId);
    }

    // Getters (Important for returning error details to the client)
    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}