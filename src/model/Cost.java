package model;

public class Cost {
    private int id;
    private String category;
    private double sum;
    private String currency;
    private String description;
    private String date;

    public Cost(int id, String category, double sum, String currency, String description, String date) {
        this.id = id;
        this.category = category;
        this.sum = sum;
        this.currency = currency;
        this.description = description;
        this.date = date;
    }

    public Cost(String category, double sum, String currency, String description, String date) {
        this.category = category;
        this.sum = sum;
        this.currency = currency;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getData() {
        return new String[]{String.valueOf(id), category, String.valueOf(sum), currency, description, date};
    }
}
