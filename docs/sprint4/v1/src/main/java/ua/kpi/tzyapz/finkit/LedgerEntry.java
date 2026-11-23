package ua.kpi.tzyapz.finkit;

import java.math.BigDecimal;

/**
 * Запис історії рахунку (BL-80): тип операції, сума та баланс після операції.
 *
 * @param type         тип операції
 * @param amount       сума операції, грн
 * @param balanceAfter баланс рахунку після виконання операції, грн
 */
public record LedgerEntry(Type type, BigDecimal amount, BigDecimal balanceAfter) {

    /** Тип операції за рахунком. */
    public enum Type {
        /** Зарахування коштів. */
        DEPOSIT,
        /** Списання коштів. */
        WITHDRAWAL
    }
}
