package uk.co.mattburns.pwinty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.mattburns.pwinty.Pwinty.Environment;
import uk.co.mattburns.pwinty.model.Document;
import uk.co.mattburns.pwinty.model.Order;
import uk.co.mattburns.pwinty.model.Order.Status;
import uk.co.mattburns.pwinty.model.Photo;
import uk.co.mattburns.pwinty.model.Photo.Sizing;
import uk.co.mattburns.pwinty.model.Photo.Type;
import uk.co.mattburns.pwinty.model.Sticker;
import uk.co.mattburns.pwinty.model.SubmissionStatus;
import uk.co.mattburns.pwinty.model.SubmissionStatus.GeneralError;

public class PwintyTest {

    // Enter keys here to runs the tests
    private static final String API_KEY = "";
    private static final String MERCHANT_ID = "";

    private static final String TEST_PHOTO_LOCAL = "/resources/test.jpg";
    private static final String TEST_PHOTO_URL = "http://farm4.staticflickr.com/3454/3837830342_dae0b932a9_b_d.jpg";
    private static final String TEST_DOCUMENT_LOCAL = "/resources/test.pdf";
    private static final String TEST_STICKER_LOCAL = TEST_PHOTO_LOCAL;

    private Pwinty pwinty = new Pwinty(Environment.SANDBOX, MERCHANT_ID,
            API_KEY, System.out);

    @BeforeClass
    public static void before() {
        if (MERCHANT_ID.isEmpty() || API_KEY.isEmpty()) {
            fail("MERCHANT_ID and API_KEY must be set to run the tests");
        }
    }

    @AfterClass
    public static void after() {
        Pwinty pwinty = new Pwinty(Environment.SANDBOX, MERCHANT_ID, API_KEY);
        List<Order> fetchedOrders = pwinty.getOrders();
        for (Order o : fetchedOrders) {
            if (o.getStatus() == Status.NotYetSubmitted) {
                pwinty.cancelOrder(o.getId());
            }
        }
    }

    @Test
    public void can_create_order() {
        Order newOrder = new Order();
        newOrder.setRecipientName("bloggs");
        Order order = pwinty.createOrder(newOrder);
        assertEquals("bloggs", order.getRecipientName());
    }

    @Test
    public void can_create_and_fetch_order() {
        Order newOrder = new Order();
        newOrder.setRecipientName("bloggs");

        Order order = pwinty.createOrder(newOrder);
        assertEquals("bloggs", order.getRecipientName());

        Order fetchedOrder = pwinty.getOrder(order.getId());
        assertEquals("bloggs", fetchedOrder.getRecipientName());
        assertEquals(order.getId(), fetchedOrder.getId());
    }

    @Test
    public void cannot_fetch_invalid_order() {
        try {
            pwinty.getOrder(0);
            fail("should have thrown");
        } catch (PwintyError e) {
            assertEquals(404, e.getCode());
            assertEquals(404, e.getCode());
        }
    }

    @Test
    public void can_fetch_all_orders() {
        List<Order> fetchedOrders = pwinty.getOrders();
        int initialSize = fetchedOrders.size();

        Order newOrder = new Order();
        pwinty.createOrder(newOrder);

        fetchedOrders = pwinty.getOrders();
        assertEquals(initialSize + 1, fetchedOrders.size());
    }

    @Test
    public void can_create_and_update_order() {
        Order newOrder = new Order();
        newOrder.setRecipientName("bloggs");

        Order createdOrder = pwinty.createOrder(newOrder);
        assertEquals("bloggs", createdOrder.getRecipientName());

        int id = createdOrder.getId();
        newOrder.setRecipientName("jones");

        Order updatedOrder = pwinty.updateOrder(id, newOrder);
        assertEquals("jones", updatedOrder.getRecipientName());
        assertEquals(id, updatedOrder.getId());
    }

    @Test
    public void can_create_order_and_get_status() {
        Order newOrder = new Order();

        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();
        assertEquals(Status.NotYetSubmitted, createdOrder.getStatus());

        SubmissionStatus submissionStatus = pwinty.getSubmissionStatus(id);
        assertEquals(false, submissionStatus.isValid());
        assertTrue(submissionStatus.getGeneralErrors().contains(
                GeneralError.NoItemsInOrder));
        assertTrue(submissionStatus.getGeneralErrors().contains(
                GeneralError.PostalAddressNotSet));
    }

    @Test
    public void cannot_get_status_on_invalid_order() throws URISyntaxException {
        try {
            pwinty.getSubmissionStatus(0);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(404, e.getCode());
        }
    }

    @Test
    public void can_create_and_add_photo_and_submit_order()
            throws URISyntaxException {
        Order newOrder = new Order();
        newOrder.setAddress1("ad1");
        newOrder.setAddress2("ad2");
        newOrder.setAddressTownOrCity("toc");
        newOrder.setCountry("uk");
        newOrder.setPostalOrZipCode("zip");
        newOrder.setRecipientName("bloggs");
        newOrder.setStateOrCounty("bristol");

        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();
        assertEquals(Status.NotYetSubmitted, createdOrder.getStatus());

        URL resource = PwintyTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        pwinty.addPhotoToOrder(id, file, Type._4x6, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = pwinty.getSubmissionStatus(id);
        assertEquals(
                "SubmissionStatus is not valid: " + submissionStatus.toString(),
                true, submissionStatus.isValid());

        pwinty.submitOrder(id);

        Order fetchedOrder = pwinty.getOrder(id);
        assertEquals(Status.Submitted, fetchedOrder.getStatus());
    }

    @Test
    public void error_is_thrown_if_order_not_in_correct_state()
            throws URISyntaxException {
        Order newOrder = new Order();
        newOrder.setAddress1("ad1");
        newOrder.setAddress2("ad2");
        newOrder.setAddressTownOrCity("toc");
        newOrder.setCountry("uk");
        newOrder.setPostalOrZipCode("zip");
        newOrder.setRecipientName("bloggs");
        newOrder.setStateOrCounty("bristol");

        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();

        URL resource = PwintyTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        pwinty.addPhotoToOrder(id, file, Type._4x6, 1, Sizing.Crop);
        pwinty.submitOrder(id);

        try {
            pwinty.submitOrder(id);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(403, e.getCode());
        }
    }

    @Test
    public void cannot_submit_invalid_order() throws URISyntaxException {
        try {
            pwinty.submitOrder(0);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(404, e.getCode());
        }
    }

    @Test
    public void cannot_cancel_invalid_order() throws URISyntaxException {
        try {
            pwinty.cancelOrder(0);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(404, e.getCode());
        }
    }

    @Test
    public void can_create_and_add_photo_by_url() throws MalformedURLException {
        Order newOrder = new Order();
        newOrder.setAddress1("ad1");
        newOrder.setAddress2("ad2");
        newOrder.setAddressTownOrCity("toc");
        newOrder.setCountry("uk");
        newOrder.setPostalOrZipCode("zip");
        newOrder.setRecipientName("bloggs");
        newOrder.setStateOrCounty("bristol");

        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();
        assertEquals(Status.NotYetSubmitted, createdOrder.getStatus());

        URL url = new URL(TEST_PHOTO_URL);

        pwinty.addPhotoToOrder(id, url, Type._4x6, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = pwinty.getSubmissionStatus(id);

        assertEquals(
                "SubmissionStatus is not valid: " + submissionStatus.toString(),
                true, submissionStatus.isValid());
    }

    @Test
    public void can_get_photo_details() throws MalformedURLException {
        Order newOrder = new Order();
        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();

        URL url = new URL(TEST_PHOTO_URL);
        pwinty.addPhotoToOrder(id, url, Type._4x6, 2, Sizing.ShrinkToExactFit);
        SubmissionStatus submissionStatus = pwinty.getSubmissionStatus(id);
        int photoId = submissionStatus.getPhotos().get(0).getId();

        Photo photo = pwinty.getPhoto(photoId);
        assertEquals(2, photo.getCopies());
        assertEquals(Photo.Sizing.ShrinkToExactFit, photo.getSizing());
    }

    @Test
    public void can_delete_photo_from_order() throws MalformedURLException {
        Order newOrder = new Order();
        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();

        URL url = new URL(TEST_PHOTO_URL);
        Photo photo = pwinty
                .addPhotoToOrder(id, url, Type._4x6, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = pwinty.getSubmissionStatus(id);
        assertEquals(1, submissionStatus.getPhotos().size());

        pwinty.deletePhoto(photo.getId());

        submissionStatus = pwinty.getSubmissionStatus(id);
        assertEquals(0, submissionStatus.getPhotos().size());
    }

    @Test
    public void can_cancel_order() throws URISyntaxException {
        Order newOrder = new Order();

        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();

        pwinty.cancelOrder(id);
        Order fetchedOrder = pwinty.getOrder(id);
        assertEquals(Status.Cancelled, fetchedOrder.getStatus());
    }

    @Test
    public void cannot_cancel_submitted_order() throws URISyntaxException {
        Order newOrder = new Order();
        newOrder.setAddress1("ad1");
        newOrder.setAddress2("ad2");
        newOrder.setAddressTownOrCity("toc");
        newOrder.setCountry("uk");
        newOrder.setPostalOrZipCode("zip");
        newOrder.setRecipientName("bloggs");
        newOrder.setStateOrCounty("bristol");

        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();

        URL resource = PwintyTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        pwinty.addPhotoToOrder(id, file, Type._4x6, 1, Sizing.Crop);
        pwinty.submitOrder(id);

        try {
            pwinty.cancelOrder(id);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(403, e.getCode());
        }
    }

    @Test
    public void can_add_and_get_and_delete_document() throws URISyntaxException {
        Order newOrder = new Order();
        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();

        URL resource = PwintyTest.class.getResource(TEST_DOCUMENT_LOCAL);
        File file = new File(resource.toURI());

        Document document = pwinty.addDocumentToOrder(id, "test.pdf", file);
        document = pwinty.getDocument(document.getId());
        assertEquals(1, document.getPages());

        pwinty.deleteDocument(document.getId());
    }

    @Test
    public void can_add_and_get_and_delete_sticker() throws URISyntaxException {
        Order newOrder = new Order();
        Order createdOrder = pwinty.createOrder(newOrder);
        int id = createdOrder.getId();

        URL resource = PwintyTest.class.getResource(TEST_STICKER_LOCAL);
        File file = new File(resource.toURI());

        Sticker sticker = pwinty.addStickerToOrder(id, "test.jpg", file);
        sticker = pwinty.getSticker(sticker.getId());
        pwinty.deleteSticker(sticker.getId());
    }

    @Test
    public void error_with_bad_api_keys() {
        Pwinty unauthorizedPwinty = new Pwinty(Environment.SANDBOX, "", "");
        Order newOrder = new Order();
        try {
            unauthorizedPwinty.createOrder(newOrder);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(401, e.getCode());
        }
    }
}
