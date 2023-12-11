import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Product implements Serializable {
    private String name;
    private String unit;
    private int quantity;
    private int price;
    private Date arrivalDate;
    private String description;

    public Product(String name, String unit, int quantity, int price, Date arrivalDate, String description) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
        this.arrivalDate = arrivalDate;
        this.description = description;
    }

    public boolean isExpired() {
        String regex = "(\\d{4}-\\d{2}-\\d{2}).*?([+-]?\\d+\\s*(?:year|month|week|day|hour))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            String manufacturingDateStr = matcher.group(1);
            String expirationTerm = matcher.group(2);

            try {
                Date manufacturingDate = new SimpleDateFormat("yyyy-MM-dd").parse(manufacturingDateStr);
                Date expirationDate = calculateExpirationDate(manufacturingDate, expirationTerm);
                return expirationDate != null && expirationDate.before(new Date());
            } catch (ParseException e) {
                System.err.println("Error parsing date. Skipping expiration check.");
            }
        }

        return false;
    }

    private Date calculateExpirationDate(Date startDate, String term) {
        String[] parts = term.split("\\s");
        if (parts.length == 2) {
            int number = Integer.parseInt(parts[0]);
            String unit = parts[1].toLowerCase();

            long millisecondsInDay = 24 * 60 * 60 * 1000;
            long expirationMilliseconds;
            switch (unit) {
                case "year":
                    expirationMilliseconds = number * 365 * millisecondsInDay;
                    break;
                case "month":
                    expirationMilliseconds = number * 30 * millisecondsInDay;
                    break;
                case "week":
                    expirationMilliseconds = number * 7 * millisecondsInDay;
                    break;
                case "day":
                    expirationMilliseconds = number * millisecondsInDay;
                    break;
                case "hour":
                    expirationMilliseconds = number * 60 * 60 * 1000;
                    break;
                default:
                    return null;
            }

            return new Date(startDate.getTime() + expirationMilliseconds);
        }

        return null;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", arrivalDate=" + arrivalDate +
                ", description='" + description + '\'' +
                '}';
    }
}
