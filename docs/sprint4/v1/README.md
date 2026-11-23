# «ФінКит» — модуль бізнес-логіки, версія 1.0.0

Самостійний Maven-проєкт (JDK 17, без зовнішніх залежностей у `main`; JUnit 5 — лише для тестів).
Реалізує правила розділу 5 «Вимоги до бізнес-логіки модуля» специфікації «ФінКит»
(ідентифікатори `BL-10` … `BL-80`). Пакет: `ua.kpi.tzyapz.finkit`.

## Збирання і запуск тестів

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)   # macOS; на Windows/Linux — шлях до JDK 17
mvn -q -B test                                      # компіляція + усі тести з src/test
```

Звіти Surefire з'являються у `target/surefire-reports/`. Власні тести додавайте у
`src/test/java/ua/kpi/tzyapz/finkit/` поруч із `ExampleTest.java`.

## Що всередині

| Клас | Призначення | Правила специфікації |
|---|---|---|
| `LoanCalculator` | `monthlyPayment(P, r, n)` — ануїтетний платіж A = P·i/(1 − (1+i)^−n), i = r/12/100; `totalInterest(P, r, n)` = A·n − P | BL-10, BL-20, BL-30 (P 1 000..1 000 000 грн, n 3..60 міс, r 5..40 % — інакше `IllegalArgumentException`) |
| `TransferValidator` | `validate(fromIban, toIban, amount, balance, todaySpent)` повертає перелік `TransferError` (порожній — переказ дозволено); `isValidIban(s)` — «UA» + 27 цифр | BL-40 (amount > 0, amount ≤ balance, todaySpent + amount ≤ 50 000, from ≠ to), BL-50 |
| `AccountLedger` | `deposit`, `withdraw`, `balance`, `history` — облік одного рахунку без овердрафту | BL-70, BL-80 |
| `TransferError` | перелік кодів порушень переказу | BL-40, BL-50 |
| `LedgerEntry` | запис історії: тип, сума, баланс після операції | BL-80 |

Гроші — `java.math.BigDecimal`, округлення `HALF_UP` до 2 знаків (копійок); проміжні
обчислення формули — без округлення до отримання результату.

## Приклад використання

```java
LoanCalculator calc = new LoanCalculator();
calc.monthlyPayment(new BigDecimal("100000"), new BigDecimal("12"), 12); // 8884.88

TransferValidator v = new TransferValidator();
v.validate("UA21…(27 цифр)", "UA90…(27 цифр)", new BigDecimal("1000"),
           new BigDecimal("5000"), BigDecimal.ZERO);                       // []

AccountLedger ledger = new AccountLedger();
ledger.deposit(new BigDecimal("500"));
ledger.withdraw(new BigDecimal("120.50"));
ledger.balance();                                                          // 379.50
```
