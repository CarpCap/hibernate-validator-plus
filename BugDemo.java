import java.math.BigDecimal;

public class BugDemo {
    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("100.00");
        System.out.println("scale=" + a.scale() + "  getDecimalPart=" + getDecimalPart(a));

        BigDecimal b = a.stripTrailingZeros();
        System.out.println("scale=" + b.scale() + "  getDecimalPart=" + getDecimalPart(b));
    }

    static String getDecimalPart(BigDecimal amount) {
        if (amount.scale() <= 0) return null;
        return amount.toPlainString().split("\\.")[1];
    }
}
