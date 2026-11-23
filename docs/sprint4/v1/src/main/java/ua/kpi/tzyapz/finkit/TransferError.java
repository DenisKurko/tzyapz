package ua.kpi.tzyapz.finkit;

/** Коди порушень правил переказу (BL-40, BL-50). */
public enum TransferError {
    /** Сума переказу не додатна. */
    AMOUNT_NOT_POSITIVE,
    /** Сума перевищує баланс рахунку-джерела. */
    INSUFFICIENT_FUNDS,
    /** Перевищено денний ліміт 50 000 грн. */
    DAILY_LIMIT_EXCEEDED,
    /** Рахунок-джерело збігається з рахунком-одержувачем. */
    SAME_ACCOUNT,
    /** IBAN рахунку-джерела має недійсний формат. */
    INVALID_FROM_IBAN,
    /** IBAN рахунку-одержувача має недійсний формат. */
    INVALID_TO_IBAN
}
