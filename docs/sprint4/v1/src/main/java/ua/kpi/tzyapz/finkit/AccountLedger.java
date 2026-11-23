package ua.kpi.tzyapz.finkit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Книга обліку одного рахунку «ФінКит» (вимоги BL-70, BL-80):
 * овердрафт заборонено, суми лише додатні, історія зберігає порядок операцій.
 */
public final class AccountLedger {

    private BigDecimal balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private final List<LedgerEntry> entries = new ArrayList<>();

    /**
     * Зараховує кошти на рахунок.
     *
     * @param amount сума зарахування, грн (має бути додатною)
     * @throws IllegalArgumentException якщо сума нульова або від'ємна
     */
    public void deposit(BigDecimal amount) {
        BigDecimal value = requirePositive(amount);
        balance = balance.add(value);
        entries.add(new LedgerEntry(LedgerEntry.Type.DEPOSIT, value, balance));
    }

    /**
     * Списує кошти з рахунку; овердрафт заборонено.
     *
     * @param amount сума списання, грн (має бути додатною)
     * @throws IllegalArgumentException якщо сума нульова або від'ємна
     * @throws IllegalStateException    якщо сума перевищує поточний баланс
     */
    public void withdraw(BigDecimal amount) {
        BigDecimal value = requirePositive(amount);
        if (value.compareTo(balance) >= 0) {
            throw new IllegalStateException("Овердрафт заборонено: недостатньо коштів");
        }
        balance = balance.subtract(value);
        entries.add(new LedgerEntry(LedgerEntry.Type.WITHDRAWAL, value, balance));
    }

    /**
     * Поточний баланс рахунку.
     *
     * @return баланс, грн, з двома знаками після коми
     */
    public BigDecimal balance() {
        return balance;
    }

    /**
     * Історія операцій за рахунком.
     *
     * @return незмінюваний перелік записів у порядку виконання операцій
     */
    public List<LedgerEntry> history() {
        List<LedgerEntry> copy = new ArrayList<>(entries);
        Collections.reverse(copy);
        return Collections.unmodifiableList(copy);
    }

    private static BigDecimal requirePositive(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Сума операції має бути додатною");
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
