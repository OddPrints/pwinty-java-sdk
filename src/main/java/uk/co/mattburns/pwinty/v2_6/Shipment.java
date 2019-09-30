package uk.co.mattburns.pwinty.v2_6;

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
    private String carrier;

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

    public DateTime getShippedOn() {
        if (shippedOn == null) {
            return null;
        }
        return new DateTime(shippedOn);
    }

    /**
     * The the Carrier used. If Pwinty returns a Carrier we've not seen before, you will have to
     * catch the thrown exception and get the Carrier String out of that because there won't be a
     * human-friendly mapping
     *
     * @return Carrier enum
     * @throws UnhandledCarrierException previously unseen carrier
     */
    public Carrier getCarrier() throws UnhandledCarrierException {
        try {
            return Carrier.valueOf(carrier);
        } catch (IllegalArgumentException e) {
            throw new UnhandledCarrierException(carrier);
        }
    }

    public enum Carrier {
        RoyalMail("Royal Mail"),
        RoyalMailFirstClass("Royal Mail 1st Class"),
        RoyalMailSecondClass("Royal Mail 2nd Class"),
        FedEx("FedEx"),
        FedExUK("FedEx"),
        FedExIntl("FedEx"),
        Interlink("Interlink"),
        UPS("UPS"),
        UpsTwoDay("UPS 2 Day"),
        UKMail("UK Mail"),
        TNT("TNT"),
        ParcelForce("Parcel Force"),
        DHL("DHL"),
        UPSMI("UPS-MI (USPS)"),
        DpdNextDay("DPD Next Day"),
        EuPostal("Europe Post"),
        AuPost("Australia Post"),
        AirMail("Air Mail"),
        NotKnown("-");

        private String humanFriendlyName;

        Carrier(String humanFriendlyName) {
            this.humanFriendlyName = humanFriendlyName;
        }

        @Override
        public String toString() {
            return getHumanFriendlyName();
        }

        public String getHumanFriendlyName() {
            return humanFriendlyName;
        }
    }

    public class UnhandledCarrierException extends Exception {
        private final String unhandledCarrier;

        public UnhandledCarrierException(String unhandledCarrier) {
            super(unhandledCarrier);
            this.unhandledCarrier = unhandledCarrier;
        }

        public String getUnhandledCarrier() {
            return unhandledCarrier;
        }
    }
}
