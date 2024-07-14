package com.fabio.financialtransfer.client.dto.input.validation;

import com.fabio.financialtransfer.client.dto.input.TransferInputTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link DifferentAccountIds} annotation. This validator checks if the debit and credit account IDs
 * in a {@link TransferInputTO} are different.
 */
public class DifferentAccountIdsValidator implements ConstraintValidator<DifferentAccountIds, TransferInputTO> {

    /**
     * Checks if the debit and credit account IDs in the given {@link TransferInputTO} are different.
     * @param transferInputTO the transfer input object to validate
     * @param context the context in which the constraint is evaluated
     * @return true if the debit and credit account IDs are different, false otherwise
     */
    @Override
    public boolean isValid(final TransferInputTO transferInputTO, final ConstraintValidatorContext context) {
        if (transferInputTO == null) {
            return true;
        }

        Long debitAccountId = transferInputTO.getDebitAccountId();
        Long creditAccountId = transferInputTO.getCreditAccountId();

        if (debitAccountId == null || creditAccountId == null) {
            return true; // Let @NotNull handle this case
        }

        return !debitAccountId.equals(creditAccountId);
    }
}