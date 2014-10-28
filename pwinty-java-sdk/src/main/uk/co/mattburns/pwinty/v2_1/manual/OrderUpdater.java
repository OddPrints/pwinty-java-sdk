package uk.co.mattburns.pwinty.v2_1.manual;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;

import uk.co.mattburns.pwinty.v2_1.Order;
import uk.co.mattburns.pwinty.v2_1.Photo;
import uk.co.mattburns.pwinty.v2_1.Photo.Sizing;
import uk.co.mattburns.pwinty.v2_1.Photo.Type;
import uk.co.mattburns.pwinty.v2_1.Pwinty;
import uk.co.mattburns.pwinty.v2_1.Pwinty.Environment;

public class OrderUpdater {

    public static void main(String... args) throws MalformedURLException {

        // int orderIdToUpdate = xxxxxx;

        // Create an updater:
        // OrderUpdater updater = new OrderUpdater(Environment.LIVE);

        // Five use cases:
        // updater.display(orderIdToUpdate);
        // updater.updateAddress(orderIdToUpdate);
        // updater.updateImageUrl(orderIdToUpdate, photoIdToUpdate, newUrl);
        // updater.addPhotoToOrder(orderIdToUpdate,newUrl,type,copies,sizing);
        // orderIdToUpdate = updater.updateUseTrackedShipping(orderIdToUpdate,
        // true);
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
    }

    public void decodeAddress(int orderIdToUpdate) {
        Pwinty pwinty = getPwinty(environment);

        System.out.println(pwinty.getOrder(orderIdToUpdate));

        Order order = pwinty.getOrder(orderIdToUpdate);
        order.setRecipientName(decode(order.getRecipientName()));
        order.setAddress1(decode(order.getAddress1()));
        order.setAddress2(decode(order.getAddress2()));
        order.setAddressTownOrCity(decode(order.getAddressTownOrCity()));
        order.setStateOrCounty(decode(order.getStateOrCounty()));
        order.setPostalOrZipCode(decode(order.getPostalOrZipCode()));
    }

    private String decode(String str) {
        if (str != null) {
            try {
                return URLDecoder.decode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return str;
        }
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

    public int updateUseTrackedShipping(int orderIdToUpdate,
            boolean useTrackedShipping) {
        Pwinty pwinty = getPwinty(environment);
        System.out.println(pwinty.getOrder(orderIdToUpdate));

        Order order = pwinty.getOrder(orderIdToUpdate);
        order = order.createCloneWithTrackedShipping(true);
        if (!order.getShippingInfo().isTracked()) {
            throw new RuntimeException(
                    "Couldn't set to tracked. Is it available with current quality / country settings?");
        }
        System.out.println(pwinty.getOrder(order.getId()));

        System.out.println("**** NOTE: ORDER NUMBER HAS NOW CHANGED !! ****");
        System.out.println("New order number is : " + order.getId());
        return order.getId();
    }

    public Pwinty getPwinty(Environment env) {
        Properties props = new Properties();

        try {
            props.load(getClass().getResourceAsStream(
                    "test-settings.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new Pwinty(env, props.getProperty("PWINTY_MERCHANT_ID_" + env),
                props.getProperty("PWINTY_MERCHANT_KEY_" + env), System.out);
    }

}
