package uk.co.mattburns.pwinty.manual;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.mattburns.pwinty.Document;
import uk.co.mattburns.pwinty.Order;
import uk.co.mattburns.pwinty.Order.Status;
import uk.co.mattburns.pwinty.Photo;
import uk.co.mattburns.pwinty.Photo.Sizing;
import uk.co.mattburns.pwinty.Photo.Type;
import uk.co.mattburns.pwinty.Pwinty;
import uk.co.mattburns.pwinty.Pwinty.Environment;
import uk.co.mattburns.pwinty.PwintyError;
import uk.co.mattburns.pwinty.Sticker;
import uk.co.mattburns.pwinty.SubmissionStatus;
import uk.co.mattburns.pwinty.SubmissionStatus.GeneralError;

public class OrderTest {

    // Enter keys here to runs the tests
    private static final String TEST_PHOTO_LOCAL = "/resources/test.jpg";
    private static final String TEST_PHOTO_URL = "http://farm4.staticflickr.com/3454/3837830342_dae0b932a9_b_d.jpg";
    private static final String TEST_DOCUMENT_LOCAL = "/resources/test.pdf";
    private static final String TEST_STICKER_LOCAL = TEST_PHOTO_LOCAL;

    private static Pwinty pwinty;

    @BeforeClass
    public static void before() {
        Properties props = new Properties();

        try {
            // load a properties file
            props.load(OrderTest.class
                    .getResourceAsStream("test-settings.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        pwinty = new Pwinty(Environment.SANDBOX,
                props.getProperty("PWINTY_MERCHANT_ID"),
                props.getProperty("PWINTY_MERCHANT_KEY"));
    }

    @AfterClass
    public static void after() {
        List<Order> fetchedOrders = pwinty.getOrders();
        for (Order o : fetchedOrders) {
            if (o.getStatus() == Status.NotYetSubmitted) {
                o.cancel();
            }
        }
    }

    @Test
    public void can_create_order() {
        Order order = new Order(pwinty);
        order.setRecipientName("bloggs");
        assertEquals("bloggs", order.getRecipientName());
        assertTrue(order.getId() > 0);
    }

    @Test
    public void can_create_and_fetch_order_id() {
        Order order = new Order(pwinty);
        order.setRecipientName("bloggs");

        int fetchedOrderId = order.getId();

        Order fetchedOrder = pwinty.getOrder(fetchedOrderId);
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

        new Order(pwinty);

        fetchedOrders = pwinty.getOrders();
        assertEquals(initialSize + 1, fetchedOrders.size());
    }

    @Test
    public void can_create_and_update_order() {
        Order order = new Order(pwinty);
        order.setRecipientName("bloggs");

        assertEquals("bloggs", order.getRecipientName());

        int id = order.getId();
        order.setRecipientName("jones");
        assertEquals("jones", order.getRecipientName());

        Order updatedOrder = pwinty.getOrder(id);
        assertEquals("jones", updatedOrder.getRecipientName());
        assertEquals(id, updatedOrder.getId());
    }

    @Test
    public void can_create_order_and_get_status() {
        Order order = new Order(pwinty);

        assertEquals(Status.NotYetSubmitted, order.getStatus());

        SubmissionStatus submissionStatus = order.getSubmissionStatus();
        assertEquals(false, submissionStatus.isValid());
        assertTrue(submissionStatus.getGeneralErrors().contains(
                GeneralError.NoItemsInOrder));
        assertTrue(submissionStatus.getGeneralErrors().contains(
                GeneralError.PostalAddressNotSet));
    }

    @Test
    public void can_create_and_add_photo_and_submit_order()
            throws URISyntaxException {
        Order order = new Order(pwinty);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
        order.setCountry("uk");
        order.setPostalOrZipCode("zip");
        order.setRecipientName("bloggs");
        order.setStateOrCounty("bristol");

        int id = order.getId();
        assertEquals(Status.NotYetSubmitted, order.getStatus());

        URL resource = OrderTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        order.addPhoto(file, Type._4x6, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = order.getSubmissionStatus();
        assertEquals(
                "SubmissionStatus is not valid: " + submissionStatus.toString(),
                true, submissionStatus.isValid());

        order.submit();

        Order fetchedOrder = pwinty.getOrder(id);
        assertEquals(Status.Submitted, fetchedOrder.getStatus());
    }

    @Test
    public void error_is_thrown_if_order_not_in_correct_state()
            throws URISyntaxException {
        Order order = new Order(pwinty);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
        order.setCountry("uk");
        order.setPostalOrZipCode("zip");
        order.setRecipientName("bloggs");
        order.setStateOrCounty("bristol");

        URL resource = OrderTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        order.addPhoto(file, Type._4x6, 1, Sizing.Crop);

        order.submit(); // this call should work

        try {
            order.submit();
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(403, e.getCode());
        }
    }

    @Test
    public void can_create_and_add_photo_by_url() throws MalformedURLException {
        Order order = new Order(pwinty);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
        order.setCountry("uk");
        order.setPostalOrZipCode("zip");
        order.setRecipientName("bloggs");
        order.setStateOrCounty("bristol");

        assertEquals(Status.NotYetSubmitted, order.getStatus());

        URL url = new URL(TEST_PHOTO_URL);

        order.addPhoto(url, Type._4x6, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = order.getSubmissionStatus();

        assertEquals(
                "SubmissionStatus is not valid: " + submissionStatus.toString(),
                true, submissionStatus.isValid());
    }

    @Test
    public void can_get_photo_details() throws MalformedURLException {
        Order order = new Order(pwinty);

        URL url = new URL(TEST_PHOTO_URL);
        order.addPhoto(url, Type._4x6, 2, Sizing.ShrinkToExactFit);
        SubmissionStatus submissionStatus = order.getSubmissionStatus();
        int photoId = submissionStatus.getPhotos().get(0).getId();

        Photo photo = pwinty.getPhoto(photoId);
        assertEquals(2, photo.getCopies());
        assertEquals(Photo.Sizing.ShrinkToExactFit, photo.getSizing());

    }

    @Test
    public void can_delete_photo_from_order() throws MalformedURLException {
        Order order = new Order(pwinty);

        URL url = new URL(TEST_PHOTO_URL);
        order.addPhoto(url, Type._4x6, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = order.getSubmissionStatus();
        assertEquals(1, submissionStatus.getPhotos().size());

        order.deletePhoto(order.getPhotos().get(0));

        submissionStatus = order.getSubmissionStatus();
        assertEquals(0, submissionStatus.getPhotos().size());
    }

    @Test
    public void can_cancel_order() throws URISyntaxException {
        Order order = new Order(pwinty);

        int id = order.getId();

        order.cancel();
        Order fetchedOrder = pwinty.getOrder(id);
        assertEquals(Status.Cancelled, fetchedOrder.getStatus());
    }

    @Test
    public void cannot_cancel_submitted_order() throws URISyntaxException {
        Order order = new Order(pwinty);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
        order.setCountry("uk");
        order.setPostalOrZipCode("zip");
        order.setRecipientName("bloggs");
        order.setStateOrCounty("bristol");

        URL resource = OrderTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        order.addPhoto(file, Type._4x6, 1, Sizing.Crop);
        order.submit();

        try {
            order.cancel();
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(403, e.getCode());
        }
    }

    @Test
    public void can_add_and_get_and_delete_document() throws URISyntaxException {
        Order order = new Order(pwinty);

        URL resource = OrderTest.class.getResource(TEST_DOCUMENT_LOCAL);
        File file = new File(resource.toURI());

        Document document = order.addDocument("test.pdf", file);
        document = pwinty.getDocument(document.getId());
        assertEquals(1, document.getPages());

        order.deleteDocument(document);
    }

    @Test
    public void can_add_and_get_and_delete_sticker() throws URISyntaxException {
        Order order = new Order(pwinty);

        URL resource = OrderTest.class.getResource(TEST_STICKER_LOCAL);
        File file = new File(resource.toURI());

        Sticker sticker = order.addSticker("test.jpg", file);
        sticker = pwinty.getSticker(sticker.getId());
        order.deleteSticker(sticker);
    }

    @Test
    public void error_with_bad_api_keys() {
        Pwinty unauthorizedPwinty = new Pwinty(Environment.SANDBOX, "", "");
        try {
            new Order(unauthorizedPwinty);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(401, e.getCode());
        }
    }
}
