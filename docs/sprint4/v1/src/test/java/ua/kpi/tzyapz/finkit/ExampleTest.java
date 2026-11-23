package ua.kpi.tzyapz.finkit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тести-зразки для модуля «ФінКит»: показують стиль оформлення юніт-тестів JUnit 5.
 * Розширюйте набір власними тестами за вимогами BL-10 … BL-80.
 */
@DisplayName("ФінКит — зразки юніт-тестів модуля")
class ExampleTest {

    @Test
    @DisplayName("BL-10: 100 000 грн під 12 % на 12 місяців — платіж 8 884,88 грн")
    void monthlyPaymentReferenceCase() {
        LoanCalculator calc = new LoanCalculator();
        BigDecimal payment = calc.monthlyPayment(new BigDecimal("100000"), new BigDecimal("12"), 12);
        assertEquals(new BigDecimal("8884.88"), payment);
    }

    @Test
    @DisplayName("BL-40: коректний переказ 1 000 грн не має порушень")
    void validTransferHasNoErrors() {
        TransferValidator validator = new TransferValidator();
        List<TransferError> errors = validator.validate(
                "UA213223130000026007233566001", "UA903052992990004149123456789",
                new BigDecimal("1000"), new BigDecimal("5000"), BigDecimal.ZERO);
        assertTrue(errors.isEmpty(), "очікували порожній перелік порушень, отримали " + errors);
    }
}
