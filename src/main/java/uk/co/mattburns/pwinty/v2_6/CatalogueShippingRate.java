package uk.co.mattburns.pwinty.v2_6;

public class CatalogueShippingRate {

    private String band;
    private String description;
    private boolean isTracked;
    private int price;
    private String currency;

    public CatalogueShippingRate() {}

    public String getBand() {
        return band;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTracked() {
        return isTracked;
    }

    public int getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
