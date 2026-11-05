package it.uniroma3.siw.festival.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotFutureYearValidator implements ConstraintValidator<NotFutureYear, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // il null lo controlla già @NotNull
        }
        return value <= LocalDate.now().getYear();
    }
}
