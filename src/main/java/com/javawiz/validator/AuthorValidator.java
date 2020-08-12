package com.javawiz.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AuthorValidator implements ConstraintValidator<Author, String> {

    List<String> authors = Arrays.asList("Benjamin Graham", "Robert T. Kiyosaki", "Swami Vivekananda", "George S Clason", "Martin Fowler");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return authors.contains(value);
    }
}