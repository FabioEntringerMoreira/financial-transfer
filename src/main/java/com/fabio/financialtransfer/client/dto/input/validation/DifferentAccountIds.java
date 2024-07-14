package com.fabio.financialtransfer.client.dto.input.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation for validating that the debit and credit account IDs in a transfer input object are different.
 */
@Documented
@Constraint(validatedBy = DifferentAccountIdsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentAccountIds {
    String message() default "Transfer between the same account is not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}