package ua.kpi.tzyapz.finkit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Кредитний калькулятор «ФінКит»: ануїтетний платіж і переплата (вимоги BL-10 … BL-30).
 * Усі грошові значення — {@link BigDecimal}, округлення HALF_UP до двох знаків (копійок).
 */
public final class LoanCalculator {

    /** Мінімальна сума кредиту, грн. */
    public static final BigDecimal MIN_PRINCIPAL = new BigDecimal("1000");
    /** Максимальна сума кредиту, грн. */
    public static final BigDecimal MAX_PRINCIPAL = new BigDecimal("1000000");
    /** Мінімальна річна ставка, %. */
    public static final BigDecimal MIN_RATE = new BigDecimal("5");
    /** Максимальна річна ставка, %. */
    public static final BigDecimal MAX_RATE = new BigDecimal("40");
    /** Мінімальний строк, місяців. */
    public static final int MIN_MONTHS = 3;
    /** Максимальний строк, місяців. */
    public static final int MAX_MONTHS = 60;

    /**
     * Щомісячний ануїтетний платіж A = P·i / (1 − (1 + i)^−n), де i = r / 12 / 100 (BL-10).
     *
     * @param principal         сума кредиту P, грн (1 000..1 000 000)
     * @param annualRatePercent річна ставка r, % (5..40)
     * @param months            строк n, місяців (3..60)
     * @return платіж, округлений HALF_UP до копійок
     * @throws IllegalArgumentException якщо будь-який параметр поза допустимими межами (BL-30)
     */
    public BigDecimal monthlyPayment(BigDecimal principal, BigDecimal annualRatePercent, int months) {
        validate(principal, annualRatePercent, months);
        double monthlyRate = annualRatePercent
                .divide(BigDecimal.valueOf(1200), 4, RoundingMode.HALF_UP)
                .doubleValue();
        double payment = principal.doubleValue() * monthlyRate
                / (1 - Math.pow(1 + monthlyRate, -months));
        return BigDecimal.valueOf(payment).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Загальна переплата за кредитом: A·n − P, де A — округлений щомісячний платіж (BL-20).
     *
     * @param principal         сума кредиту P, грн
     * @param annualRatePercent річна ставка r, %
     * @param months            строк n, місяців
     * @return переплата, округлена HALF_UP до копійок
     * @throws IllegalArgumentException якщо будь-який параметр поза допустимими межами (BL-30)
     */
    public BigDecimal totalInterest(BigDecimal principal, BigDecimal annualRatePercent, int months) {
        BigDecimal payment = monthlyPayment(principal, annualRatePercent, months);
        return payment.multiply(BigDecimal.valueOf(months))
                .subtract(principal)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static void validate(BigDecimal principal, BigDecimal rate, int months) {
        if (principal == null || rate == null) {
            throw new IllegalArgumentException("Сума і ставка не можуть бути null");
        }
        if (principal.compareTo(MIN_PRINCIPAL) < 0 || principal.compareTo(MAX_PRINCIPAL) > 0) {
            throw new IllegalArgumentException("Сума кредиту має бути в межах 1 000..1 000 000 грн");
        }
        if (months <= MIN_MONTHS || months > MAX_MONTHS) {
            throw new IllegalArgumentException("Строк має бути в межах 3..60 місяців");
        }
        if (rate.compareTo(MIN_RATE) < 0 || rate.compareTo(MAX_RATE) > 0) {
            throw new IllegalArgumentException("Ставка має бути в межах 5..40 %");
        }
    }
}
