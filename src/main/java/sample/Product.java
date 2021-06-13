package sample;

public class Product {
    private int id;
    private String titleT;
    private int barcodeT;
    private double priceT;
    private double amountT;
    private double totalT;

    public Product(String title, int barcode, double price, double amount) {
        this.titleT = title;
        this.barcodeT = barcode;
        this.priceT = price;
        this.amountT = amount;
    }

    public int getId() { return id; }

    public String getTitleT() {
        return titleT;
    }

    public int getBarcodeT() {
        return barcodeT;
    }

    public double getPriceT() {
        return priceT;
    }

    public double getAmountT() {
        return amountT;
    }

    public double getTotalT() {
        return totalT;
    }

    public void setId(int id) { this.id = id; }

    public void setTitleT(String titleT) {
        this.titleT = titleT;
    }

    public void setBarcodeT(int barcodeT) {
        this.barcodeT = barcodeT;
    }

    public void setPriceT(double priceT) {
        this.priceT = priceT;
    }

    public void setAmountT(double amountT) {
        this.amountT = amountT;
    }

    public void setTotalT(double totalT) {
        this.totalT = totalT;
    }


    @Override
    public String toString() {
        String barcodeString;
        if (barcodeT == -1) {
            barcodeString = "";
        } else {
            barcodeString = "Barcode: " + barcodeT;
        }
        return titleT + " " + barcodeString + " " + "Цена: " + priceT + " " + "Кол-во: " + amountT;
    }
}