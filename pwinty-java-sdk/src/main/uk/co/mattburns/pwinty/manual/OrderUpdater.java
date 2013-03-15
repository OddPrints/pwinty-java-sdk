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

public class OrderUpdater {

    public static void main(String... args) throws MalformedURLException {

        // Create an updater:
        // OrderUpdater updater = new OrderUpdater(Environment.LIVE);

        // Four use cases:
        // updater.display(id);
        // updater.updateAddress(orderIdToUpdate);
        // updater.updateImageUrl(orderIdToUpdate, photoIdToUpdate, newUrl);
        // updater.addPhotoToOrder(orderIdToUpdate,newUrl,type,copies,sizing);

    }

    private Environment environment;

    public OrderUpdater(Environment environment) {
        this.environment = environment;
    }

    public void display(int orderId) {
        Pwinty pwinty = getPwinty(environment);
        Order order = pwinty.getOrder(orderId);

        System.out.println(order);
    }

    public void updateAddress(int orderIdToUpdate) {
        Pwinty pwinty = getPwinty(environment);

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
        Pwinty pwinty = getPwinty(environment);
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

    public void addPhotoToOrder(int orderIdToUpdate, URL newUrl, Type type,
            int copies, Sizing sizing) {
        Pwinty pwinty = getPwinty(environment);
        System.out.println(pwinty.getOrder(orderIdToUpdate));

        Order order = pwinty.getOrder(orderIdToUpdate);
        order.addPhoto(newUrl, type, copies, sizing);
    }

    public Pwinty getPwinty(Environment env) {
        Properties props = new Properties();

        try {
            props.load(getClass().getResourceAsStream(
                    "test-settings.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new Pwinty(env, props.getProperty("PWINTY_MERCHANT_ID"),
                props.getProperty("PWINTY_MERCHANT_KEY"), System.out);
    }

}
