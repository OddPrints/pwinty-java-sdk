package uk.co.mattburns.pwinty.v2;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import uk.co.mattburns.pwinty.v2.Order.QualityLevel;
import uk.co.mattburns.pwinty.v2.Order.Status;
import uk.co.mattburns.pwinty.v2.Photo.Sizing;
import uk.co.mattburns.pwinty.v2.Photo.Type;
import uk.co.mattburns.pwinty.v2.gson.TypeDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        client.setConnectTimeout(0);
        client.setReadTimeout(0);

        webResource = client.resource(environment.url);
    }

    public List<Country> getCountries() {
        String countriesJSON = webResource.path("Country")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey).get(String.class);
        Country[] countries = createGson().fromJson(countriesJSON,
                Country[].class);

        return Arrays.asList(countries);
    }

    /**
     * Get the most recent orders.
     * 
     * @param count
     *            The number of order to retrieve
     * @param offset
     *            Skip over this many most recent orders
     * @return
     */
    public List<Order> getOrders(int count, int offset) {
        String ordersJSON = webResource.path("Orders")
                .queryParam("count", "" + count)
                .queryParam("offset", "" + offset)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)

                .get(String.class);
        Order[] orders = createGson().fromJson(ordersJSON, Order[].class);

        for (Order order : orders) {
            order.setPwinty(this);
        }
        return Arrays.asList(orders);
    }

    public Order getOrder(int orderId) {
        ClientResponse response = webResource.path("Orders/" + orderId)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .get(ClientResponse.class);

        Order order = createReponse(response, Order.class);
        order.setPwinty(this);
        return order;
    }

    Order createOrder(Order newOrder) {
        Form form = createOrderForm(newOrder);
        ClientResponse response = webResource.path("Orders")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

        return createReponse(response, Order.class);
    }

    Order updateOrder(int orderId, Order newOrder) {
        Form form = createOrderForm(newOrder);
        form.add("id", orderId);
        ClientResponse response = webResource.path("Orders/" + orderId)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .put(ClientResponse.class, form);

        return createReponse(response, Order.class);
    }

    static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Type.class, new TypeDeserializer());
        return gsonBuilder.create();
    }

    SubmissionStatus getSubmissionStatus(int orderId) {
        ClientResponse response = webResource
                .path("Orders/" + orderId + "/SubmissionStatus")
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
    Photo addPhotoToOrder(int orderId, File photo, Photo.Type type, int copies,
            Sizing sizing) {
        return addPhotoToOrder(orderId, photo, null, type, copies, sizing);
    }

    /**
     * Add a photo to the order using a public URL.
     */
    Photo addPhotoToOrder(int orderId, URL photoUrl, Photo.Type type,
            int copies, Sizing sizing) {
        return addPhotoToOrder(orderId, null, photoUrl, type, copies, sizing);
    }

    /**
     * Either the File or URL must be supplied
     */
    private Photo addPhotoToOrder(int orderId, File photo, URL photoUrl,
            Photo.Type type, int copies, Sizing sizing) {

        @SuppressWarnings("resource")
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

        ClientResponse response = webResource
                .path("Orders/" + orderId + "/Photos")
                .type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

        throwIfBad(response);

        return createReponse(response, Photo.class);
    }

    public Photo getPhoto(int orderId, int photoId) {
        ClientResponse response = webResource
                .path("Orders/" + orderId + "/Photos/" + photoId)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .get(ClientResponse.class);

        return createReponse(response, Photo.class);
    }

    void deletePhoto(int orderId, int photoId) {
        Form form = new Form();

        ClientResponse response = webResource
                .path("Orders/" + orderId + "/Photos/" + photoId)
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
    void submitOrder(int orderId) {
        updateOrder(orderId, Status.Submitted);
    }

    /**
     * If an error occurs, a {@link PwintyError} will be thrown
     */
    void cancelOrder(int orderId) {
        updateOrder(orderId, Status.Cancelled);
    }

    void updateOrder(int orderId, Status status) {
        Form form = new Form();
        form.add("status", status);

        ClientResponse response = webResource
                .path("Orders/" + orderId + "/Status")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey)
                .post(ClientResponse.class, form);

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
        form.add("countryCode", newOrder.getCountryCode());
        form.add("destinationCountryCode", newOrder.getDestinationCountryCode());
        form.add("useTrackedShipping", newOrder.isUseTrackedShipping());
        form.add("payment", newOrder.getPayment());
        form.add("qualityLevel", newOrder.getQualityLevel());
        return form;
    }

    private <T> T createReponse(ClientResponse response, Class<T> type) {
        throwIfBad(response);
        String jsonString = response.getEntity(String.class);
        return fromJson(jsonString, type);
    }

    static <T> T fromJson(String json, Class<T> type) {
        Gson gson = createGson();
        return gson.fromJson(json, type);
    }

    private void throwIfBad(ClientResponse response) {
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            throw toError(response);
        }
    }

    private PwintyError toError(ClientResponse response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        PwintyError error = gson.fromJson(response.getEntity(String.class),
                PwintyError.class);
        if (error == null) {
            error = new PwintyError();
        }
        error.setCode(response.getStatus());
        return error;
    }

    public enum Environment {
        LIVE("https://api.pwinty.com/v2"), SANDBOX(
                "https://sandbox.pwinty.com/v2");

        private String url;

        private Environment(String url) {
            this.url = url;
        }
    }

    public Catalogue getCatalogue(CountryCode countryCode, QualityLevel quality) {
        String catalogueJSON = webResource
                .path("Catalogue/" + countryCode + "/" + quality.toString())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("X-Pwinty-MerchantId", merchantId)
                .header("X-Pwinty-REST-API-Key", apiKey).get(String.class);
        Catalogue catalogue = createGson().fromJson(catalogueJSON,
                Catalogue.class);

        return catalogue;
    }
}
