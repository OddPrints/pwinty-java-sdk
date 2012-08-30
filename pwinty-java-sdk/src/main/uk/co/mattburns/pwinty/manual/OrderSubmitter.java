package uk.co.mattburns.pwinty.manual;

import java.net.MalformedURLException;
import java.net.URL;

import uk.co.mattburns.pwinty.Order;
import uk.co.mattburns.pwinty.Photo.Sizing;
import uk.co.mattburns.pwinty.Photo.Type;
import uk.co.mattburns.pwinty.Pwinty;
import uk.co.mattburns.pwinty.Pwinty.Environment;

public class OrderSubmitter {

    // Enter keys here
    private static final String API_KEY = "";
    private static final String MERCHANT_ID = "";

    public static void main(String... args) throws MalformedURLException {
        Pwinty pwinty = new Pwinty(Environment.LIVE, MERCHANT_ID, API_KEY,
                System.out);
        System.out.println(pwinty.getOrder(105));

        Order manualOrder = new Order(pwinty);
        manualOrder.setRecipientName("");
        manualOrder.setAddress1("");
        manualOrder.setAddress2("");
        manualOrder.setAddressTownOrCity("");
        manualOrder.setStateOrCounty("");
        manualOrder.setPostalOrZipCode("");
        manualOrder.setCountry("");

        manualOrder
                .addPhoto(
                        new URL(
                                ""),
                        Type._4x6, 1, Sizing.Crop);

        System.out.println("manual order id is " + manualOrder.getId());

        if (manualOrder.getSubmissionStatus().isValid()) {
            // manualOrder.submit();
            // nah, submit with the pwinty dashboard chrome plugin:
            // https://github.com/mattburns/pwinty-dashboard
        }

    }

}
