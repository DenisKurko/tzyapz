package ua.kpi.tzyapz.finkit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Валідатор переказів між рахунками «ФінКит» (вимоги BL-40, BL-50).
 * Не має зовнішніх залежностей: перевіряє лише передані значення.
 */
public final class TransferValidator {

    /** Денний ліміт переказів одного клієнта, грн. */
    public static final BigDecimal DAILY_LIMIT = new BigDecimal("50000");

    private static final Pattern IBAN_UA = Pattern.compile("UA[0-9]{27}");

    /**
     * Перевіряє переказ за правилами BL-40 і BL-50.
     *
     * @param fromIban   IBAN рахунку-джерела
     * @param toIban     IBAN рахунку-одержувача
     * @param amount     сума переказу, грн
     * @param balance    поточний баланс рахунку-джерела, грн
     * @param todaySpent сума вже виконаних сьогодні переказів клієнта, грн
     * @return перелік порушень; порожній перелік означає, що переказ дозволено
     */
    public List<TransferError> validate(String fromIban, String toIban, BigDecimal amount,
                                        BigDecimal balance, BigDecimal todaySpent) {
        List<TransferError> errors = new ArrayList<>();
        if (!isValidIban(fromIban)) {
            errors.add(TransferError.INVALID_FROM_IBAN);
        }
        if (fromIban != null && fromIban.equals(toIban)) {
            errors.add(TransferError.SAME_ACCOUNT);
        }
        if (amount == null || amount.signum() <= 0) {
            errors.add(TransferError.AMOUNT_NOT_POSITIVE);
            return errors;
        }
        if (amount.compareTo(balance) > 0) {
            errors.add(TransferError.INSUFFICIENT_FUNDS);
        }
        if (todaySpent.add(amount).compareTo(DAILY_LIMIT) >= 0) {
            errors.add(TransferError.DAILY_LIMIT_EXCEEDED);
        }
        return errors;
    }

    /**
     * Скорочена форма: чи дозволено переказ.
     *
     * @return true, якщо {@link #validate} не знайшов порушень
     */
    public boolean isValid(String fromIban, String toIban, BigDecimal amount,
                           BigDecimal balance, BigDecimal todaySpent) {
        return validate(fromIban, toIban, amount, balance, todaySpent).isEmpty();
    }

    /**
     * Перевірка формату IBAN за BL-50: літери «UA» і 27 цифр (разом 29 символів).
     *
     * @param iban рядок для перевірки (може бути null)
     * @return true, якщо формат дійсний
     */
    public static boolean isValidIban(String iban) {
        return iban != null && IBAN_UA.matcher(iban).matches();
    }
}
