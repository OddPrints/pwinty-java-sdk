package uk.co.mattburns.pwinty;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import uk.co.mattburns.pwinty.model.Document;
import uk.co.mattburns.pwinty.model.Order;
import uk.co.mattburns.pwinty.model.Order.Status;
import uk.co.mattburns.pwinty.model.Photo;
import uk.co.mattburns.pwinty.model.Photo.Sizing;
import uk.co.mattburns.pwinty.model.Sticker;
import uk.co.mattburns.pwinty.model.SubmissionStatus;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class Pwinty {

    private String merchantId;
    private String apiKey;

    private WebResource webResource;

    /**
     * This is the main class for talking to the Pwinty API. See
     * http://www.pwinty.com/api.html for detailed examples.
     * 
     * For all method calls, if an error occurs, a {@link PwintyError} will be
     * thrown
     * 
     * @param environment
     *            Choose SANDBOX for testing and LIVE for real orders you wish
     *            to print
     * @param merchantId
     *            As supplied by http://www.pwinty.com
     * @param apiKey
     *            As supplied by http://www.pwinty.com
     */
    public Pwinty(Environment environment, String merchantId, String apiKey) {
        this(null, environment, merchantId, apiKey);
    }

    /**
     * This is the main class for talking to the Pwinty API. See
     * http://www.pwinty.com/api.html
     * 
     * @param environment
     *            Choose SANDBOX for testing and LIVE for real orders you wish
     *            to print
     * @param merchantId
     *            As supplied by http://www.pwinty.com
     * @param apiKey
     *            As supplied by http://www.pwinty.com
     * @param loggingStream
     *            Requests and responses will be written to this
     */
    public Pwinty(Environment environment, String merchantId, String apiKey,
            PrintStream loggingStream) {
        this(new LoggingFilter(loggingStream), environment, merchantId, apiKey);
    }

    /**
     * This is the main class for talking to the Pwinty API. See
     * http://www.pwinty.com/api.html
     * 
     * @param environment
     *            Choose SANDBOX for testing and LIVE for real orders you wish
     *            to print
     * @param merchantId
     *            As supplied by http://www.pwinty.com
     * @param apiKey
     *            As supplied by http://www.pwinty.com
     * @param logger
     *            Requests and responses will be written to this
     */
    public Pwinty(Environment environment, String merchantId, String apiKey,
            Logger logger) {
        this(new LoggingFilter(logger), environment, merchantId, apiKey);
    }

    private Pwinty(LoggingFilter loggingFilter, Environment environment,
            String merchantId, String apiKey) {
        this.merchantId = merchantId;
        this.apiKey = apiKey;

        Client client = Client.create();
        if (loggingFilter != null) {
            client.addFilter(loggingFilter);
        }

        webResource = client.resource(environment.url);
    }

    public List<Order> getOrders() {
        String ordersJSON = webResource.path("Orders")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey).get(String.class);
        Order[] orders = new Gson().fromJson(ordersJSON, Order[].class);

        return Arrays.asList(orders);
    }

    public Order getOrder(int orderId) {
        ClientResponse response = webResource.path("Orders")
                .queryParam("id", "" + orderId)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .get(ClientResponse.class);

        return createReponse(response, Order.class);
    }

    public Order createOrder(Order newOrder) {
        Form form = createOrderForm(newOrder);
        ClientResponse response = webResource.path("Orders")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

        return createReponse(response, Order.class);
    }

    public Order updateOrder(int orderId, Order newOrder) {
        Form form = createOrderForm(newOrder);
        form.add("id", orderId);
        ClientResponse response = webResource.path("Orders")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .put(ClientResponse.class, form);

        return createReponse(response, Order.class);
    }

    public SubmissionStatus getSubmissionStatus(int orderId) {
        ClientResponse response = webResource.path("Orders/SubmissionStatus")
                .queryParam("id", "" + orderId)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .get(ClientResponse.class);

        return createReponse(response, SubmissionStatus.class);
    }

    /**
     * Add a photo File object to the order. This method will block until the
     * File is uploaded.
     */
    public Photo addPhotoToOrder(int orderId, File photo, Photo.Type type,
            int copies, Sizing sizing) {
        return addPhotoToOrder(orderId, photo, null, type, copies, sizing);
    }

    /**
     * Add a photo to the order using a public URL.
     */
    public Photo addPhotoToOrder(int orderId, URL photoUrl, Photo.Type type,
            int copies, Sizing sizing) {
        return addPhotoToOrder(orderId, null, photoUrl, type, copies, sizing);
    }

    /**
     * Either the File or URL must be supplied
     */
    private Photo addPhotoToOrder(int orderId, File photo, URL photoUrl,
            Photo.Type type, int copies, Sizing sizing) {

        FormDataMultiPart form = new FormDataMultiPart()
                .field("type", type.toString())
                .field("sizing", sizing.toString())
                .field("copies", "" + copies).field("orderId", "" + orderId);

        if (photo != null) {
            form.bodyPart(new FileDataBodyPart("file", photo,
                    MediaType.MULTIPART_FORM_DATA_TYPE));
        } else {
            form = form.field("url", photoUrl.toExternalForm());
        }

        ClientResponse response = webResource.path("Photos")
                .type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

        throwIfBad(response);

        return createReponse(response, Photo.class);
    }

    public Photo getPhoto(int photoId) {
        ClientResponse response = webResource.path("Photos")
                .queryParam("id", "" + photoId)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .get(ClientResponse.class);

        return createReponse(response, Photo.class);
    }

    public void deletePhoto(int photoId) {
        Form form = new Form();
        form.add("id", photoId);

        ClientResponse response = webResource.path("Photos")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .delete(ClientResponse.class, form);

        throwIfBad(response);
    }

    /**
     * Submit the Order for printing and shipping
     * 
     * If an error occurs, a {@link PwintyError} will be thrown
     */
    public void submitOrder(int orderId) {
        updateOrder(orderId, Status.Submitted);
    }

    /**
     * If an error occurs, a {@link PwintyError} will be thrown
     */
    public void cancelOrder(int orderId) {
        updateOrder(orderId, Status.Cancelled);
    }

    private void updateOrder(int orderId, Status status) {
        Form form = new Form();
        form.add("id", orderId);
        form.add("status", status);

        ClientResponse response = webResource.path("Orders/Status")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

        throwIfBad(response);
    }

    public Document addDocumentToOrder(int orderId, String filename,
            File document) {

        FormDataMultiPart form = new FormDataMultiPart().field("fileName",
                filename).field("orderId", "" + orderId);

        form.bodyPart(new FileDataBodyPart("file", document,
                MediaType.MULTIPART_FORM_DATA_TYPE));

        ClientResponse response = webResource.path("Documents")
                .type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

        throwIfBad(response);

        return createReponse(response, Document.class);
    }

    public Document getDocument(int documentId) {
        ClientResponse response = webResource.path("Documents")
                .queryParam("id", "" + documentId)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .get(ClientResponse.class);

        throwIfBad(response);
        return createReponse(response, Document.class);
    }

    public void deleteDocument(int documentId) {
        Form form = new Form();
        form.add("id", documentId);

        ClientResponse response = webResource.path("Documents")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .delete(ClientResponse.class, form);

        throwIfBad(response);
    }

    public Sticker addStickerToOrder(int orderId, String filename, File sticker) {
        FormDataMultiPart form = new FormDataMultiPart().field("fileName",
                filename).field("orderId", "" + orderId);

        form.bodyPart(new FileDataBodyPart("file", sticker,
                MediaType.MULTIPART_FORM_DATA_TYPE));

        ClientResponse response = webResource.path("Stickers")
                .type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

        throwIfBad(response);

        return createReponse(response, Sticker.class);
    }

    public Sticker getSticker(int stickerId) {
        ClientResponse response = webResource.path("Stickers")
                .queryParam("id", "" + stickerId)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .get(ClientResponse.class);

        throwIfBad(response);
        return createReponse(response, Sticker.class);
    }

    public void deleteSticker(int stickerId) {
        Form form = new Form();
        form.add("id", stickerId);

        ClientResponse response = webResource.path("Stickers")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .delete(ClientResponse.class, form);

        throwIfBad(response);
    }

    private Form createOrderForm(Order newOrder) {
        Form form = new Form();
        form.add("recipientName", newOrder.getRecipientName());
        form.add("address1", newOrder.getAddress1());
        form.add("address2", newOrder.getAddress2());
        form.add("addressTownOrCity", newOrder.getAddressTownOrCity());
        form.add("stateOrCounty", newOrder.getStateOrCounty());
        form.add("postalOrZipCode", newOrder.getPostalOrZipCode());
        form.add("country", newOrder.getCountry());
        return form;
    }

    private <T> T createReponse(ClientResponse response, Class<T> type) {
        throwIfBad(response);
        return new Gson().fromJson(response.getEntity(String.class), type);
    }

    private void throwIfBad(ClientResponse response) {
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            throw toError(response);
        }
    }

    private PwintyError toError(ClientResponse response) {
        PwintyError error = new Gson().fromJson(
                response.getEntity(String.class), PwintyError.class);
        if (error == null) {
            error = new PwintyError();
        }
        error.setCode(response.getStatus());
        return error;
    }

    public enum Environment {
        LIVE("https://api.pwinty.com"), SANDBOX("https://sandbox.pwinty.com");

        private String url;

        private Environment(String url) {
            this.url = url;
        }
    }
}
