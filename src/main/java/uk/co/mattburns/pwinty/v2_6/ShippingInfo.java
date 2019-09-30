package uk.co.mattburns.pwinty.v2_6;

import java.util.List;

public class ShippingInfo {
    private int price;
    private List<Shipment> shipments;

    public int getPrice() {
        return price;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }
}
