package uk.co.mattburns.pwinty.v2;

import java.net.URL;

public class ShippingInfo {
    private boolean isTracked;
    private String trackingNumber;
    private URL trackingUrl;
    private int price;
    
    public boolean isTracked() {
        return isTracked;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    public URL getTrackingUrl() {
        return trackingUrl;
    }
    
    public int getPrice() {
        return price;
    }
    
    @Override
    public String toString() {
        return "ShippingInfo [isTracked=" + isTracked + ", trackingNumber="
                + trackingNumber + ", trackingUrl=" + trackingUrl + ", price="
                + price + "]";
    }
}