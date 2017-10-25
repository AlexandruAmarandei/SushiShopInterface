/*
 * Simple class that indentifies the amount and the currency for an ingredient.
 */
package Restaurant;

/**
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class SupplierPrice {

    Double amount;
    String currency;

    public SupplierPrice(Double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return amount.toString() + " " + currency;
    }
}
