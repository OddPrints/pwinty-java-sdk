package uk.co.mattburns.pwinty.v2_2;

import org.joda.time.DateTime;

import java.net.MalformedURLException;
import java.net.URL;

public class Shipment {
    private String shipmentId;
    private String trackingNumber;
    private String trackingUrl;
    private boolean isTracked;
    private String earliestEstimatedArrivalDate;
    private String latestEstimatedArrivalDate;
    private String shippedOn;

    public String getShipmentId() {
        return shipmentId;
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

    public boolean isTracked() {
        return isTracked;
    }

    public DateTime getEarliestEstimatedArrivalDate() {
        if (earliestEstimatedArrivalDate == null) {
            return null;
        }
        return new DateTime(earliestEstimatedArrivalDate);
    }

    public DateTime getLatestEstimatedArrivalDate() {
        if (latestEstimatedArrivalDate == null) {
            return null;
        }
        return new DateTime(latestEstimatedArrivalDate);
    }

    public String getShippedOn() {
        return shippedOn;
    }
}