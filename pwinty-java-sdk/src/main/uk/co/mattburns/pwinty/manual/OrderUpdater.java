package uk.co.mattburns.pwinty.manual;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import uk.co.mattburns.pwinty.Order;
import uk.co.mattburns.pwinty.Photo;
import uk.co.mattburns.pwinty.Photo.Sizing;
import uk.co.mattburns.pwinty.Photo.Type;
import uk.co.mattburns.pwinty.Pwinty;
import uk.co.mattburns.pwinty.Pwinty.Environment;
import uk.co.mattburns.pwinty.outsidepackage.OrderTest;

public class OrderUpdater {

    public static void main(String... args) throws MalformedURLException {

        // Create an updater:
        // OrderUpdater updater = new OrderUpdater();

        // Two use cases:
        // updater.updateAddress(orderIdToUpdate);
        // updater.updateImageUrl(orderIdToUpdate, photoIdToUpdate, newUrl)
    }

    public void updateAddress(int orderIdToUpdate) {

        Pwinty pwinty = getPwinty(Environment.LIVE);

        System.out.println(pwinty.getOrder(orderIdToUpdate));

        Order order = pwinty.getOrder(orderIdToUpdate);
        order.setRecipientName("");
        order.setAddress1("");
        order.setAddress2("");
        order.setAddressTownOrCity("");
        order.setStateOrCounty("");
        order.setPostalOrZipCode("");
        order.setCountry("");

    }

    public void updateImageUrl(int orderIdToUpdate, int photoIdToUpdate,
            URL newUrl) {
        Pwinty pwinty = getPwinty(Environment.LIVE);
        System.out.println(pwinty.getOrder(orderIdToUpdate));

        Order order = pwinty.getOrder(orderIdToUpdate);
        List<Photo> photos = order.getPhotos();

        Type type = null;
        Sizing sizing = null;
        int copies = 0;
        for (Photo photo : photos) {
            if (photo.getId() == photoIdToUpdate) {
                type = photo.getType();
                sizing = photo.getSizing();
                copies = photo.getCopies();
                order.deletePhoto(photo);
            }
        }

        order.addPhoto(newUrl, type, copies, sizing);
    }

    public Pwinty getPwinty(Environment env) {
        Properties props = new Properties();

        try {
            props.load(OrderTest.class
                    .getResourceAsStream("test-settings.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new Pwinty(env, props.getProperty("PWINTY_MERCHANT_ID"),
                props.getProperty("PWINTY_MERCHANT_KEY"));
    }

}
