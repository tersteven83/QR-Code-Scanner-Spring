package com.pcop.qrcode_scanner.Gender;

import jakarta.persistence.AttributeConverter;

public class GenderConverter implements AttributeConverter<Gender, Character> {
    @Override
    public Character convertToDatabaseColumn(Gender attribute) {
        if (attribute == Gender.MALE) {
            return 'M';
        } else if (attribute == Gender.FEMALE) {
            return 'F';
        } else {
            throw new IllegalArgumentException("Unsupported Gender value: " + attribute);
        }
    }

    @Override
    public Gender convertToEntityAttribute(Character dbData) {
        if (dbData == 'M') {
            return Gender.MALE;
        } else if (dbData == 'F') {
            return Gender.FEMALE;
        } else {
            throw new IllegalArgumentException("Invalid Gender value in database: " + dbData);
        }
    }
}
