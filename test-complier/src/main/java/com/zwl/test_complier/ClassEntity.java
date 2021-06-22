package com.zwl.test_complier;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

/**
 * @author zwl
 * @since 2021/6/21
 */
public class ClassEntity {
    private final TypeElement element;
    private final Name classSimpleName;
    private final Map<String, FieldEntity> fields = new HashMap<>();

    public ClassEntity(TypeElement element) {
        this.element = element;
        this.classSimpleName = element.getSimpleName();
    }

    public String getClassSimpleName() {
        return classSimpleName.toString();
    }

    public void addFieldEntity(FieldEntity fieldEntity) {
        String fieldName = fieldEntity.getElement().toString();
        if (fields.get(fieldName) == null) {
            fields.put(fieldName, fieldEntity);
        }
    }

    public TypeElement getElement() {
        return element;
    }

    public Map<String, FieldEntity> getFields() {
        return fields;
    }
}
