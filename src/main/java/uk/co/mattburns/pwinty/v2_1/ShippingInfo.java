package uk.co.mattburns.pwinty.v2_1;

import java.net.MalformedURLException;
import java.net.URL;

public class ShippingInfo {
    private boolean isTracked;
    private String trackingNumber;
    private String trackingUrl;
    private int price;

    public boolean isTracked() {
        return isTracked;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public URL getTrackingUrl() {
        if (trackingUrl != null && !trackingUrl.isEmpty()) {
            try {
                return new URL(trackingUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
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