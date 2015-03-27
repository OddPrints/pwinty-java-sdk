package uk.co.mattburns.pwinty.v2_2;

public class CatalogueShippingRate {

    private String band;
    private String description;
    private boolean isTracked;
    private int priceGBP;

    public CatalogueShippingRate() {

    }

    public String getBand() {
        return band;
    }
    public String getDescription() {
        return description;
    }

    public boolean isTracked() {
        return isTracked;
    }

    public int getPriceGBP() {
        return priceGBP;
    }
}
