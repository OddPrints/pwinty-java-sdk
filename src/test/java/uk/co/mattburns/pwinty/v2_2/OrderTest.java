package uk.co.mattburns.pwinty.v2_2;

import static org.junit.Assert.*;
import static uk.co.mattburns.pwinty.v2_2.CountryCode.GB;
import static uk.co.mattburns.pwinty.v2_2.CountryCode.US;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.mattburns.pwinty.v2_2.Order.QualityLevel;
import uk.co.mattburns.pwinty.v2_2.Order.Status;
import uk.co.mattburns.pwinty.v2_2.Photo.Sizing;
import uk.co.mattburns.pwinty.v2_2.Photo.Type;
import uk.co.mattburns.pwinty.v2_2.Pwinty.Environment;
import uk.co.mattburns.pwinty.v2_2.SubmissionStatus.GeneralError;
import uk.co.mattburns.pwinty.v2_2.gson.TypeDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrderTest {

    // Enter keys here to runs the tests
    private static final String TEST_PHOTO_LOCAL = "test.jpg";
    private static final String TEST_PHOTO_URL = "http://farm4.staticflickr.com/3454/3837830342_dae0b932a9_b_d.jpg";

    private static Pwinty pwinty;

    @BeforeClass
    public static void before() {
        Properties props = TestUtils.loadProps();

        pwinty = new Pwinty(Environment.SANDBOX,
                props.getProperty("PWINTY_MERCHANT_ID_SANDBOX"),
                props.getProperty("PWINTY_MERCHANT_KEY_SANDBOX"), System.out);
    }

    @AfterClass
    public static void after() {
        List<Order> fetchedOrders = pwinty.getOrders(100, 0);
        for (Order o : fetchedOrders) {
            if (o.getStatus() == Status.NotYetSubmitted) {
                o.cancel();
            }
        }
    }

    @Test
    public void can_create_order() {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setRecipientName("bloggs");
        assertEquals("bloggs", order.getRecipientName());
        assertTrue(order.getId() > 0);
    }

    @Test
    public void can_create_and_fetch_order_id() {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
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
    public void can_find_order_by_fetching_all_orders_recursively() {
        int orderId = 271149; // just an old order I have...
        boolean found = false;
        int count = 100;
        for (int offset = 0; !found; offset += count) {
            List<Order> fetchedOrders = pwinty.getOrders(count, offset);
            for (Order o1 : fetchedOrders) {
                if (o1.getId() == orderId) {
                    found = true;
                    break;
                }
            }
            if (fetchedOrders.isEmpty()) {
                fail("Couln't find Order "
                        + orderId
                        + ", no more orders to search. "
                        + "I suggest you visit https://sandboxdashboard.pwinty.com/Account/Balance "
                        + "to grab an old order number and set it at the top of this test.");
            }
        }

        assertTrue(found);
    }

    @Test
    public void can_find_most_recent_order_by_fetching_1_order() {

        Order o = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        int orderId = o.getId();
        Order fetched = pwinty.getOrders(1, 0).get(0);

        assertEquals(orderId, fetched.getId());
    }

    @Test
    public void can_create_and_update_order() {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
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
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);

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
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
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
    public void can_use_tracked_shipping() throws MalformedURLException {
        testTrackedShipping(US, QualityLevel.Standard, false);
        testTrackedShipping(US, QualityLevel.Standard, true);
        testTrackedShipping(US, QualityLevel.Pro, false);
        testTrackedShipping(US, QualityLevel.Pro, true);

        testTrackedShipping(GB, QualityLevel.Standard, false);
        testTrackedShipping(GB, QualityLevel.Standard, true);
        testTrackedShipping(GB, QualityLevel.Pro, false);
        testTrackedShipping(GB, QualityLevel.Pro, true);
    }

    private void testTrackedShipping(CountryCode countryCode,
            QualityLevel qualityLevel, boolean useTrackedShipping)
            throws MalformedURLException {
        testTrackedShipping(countryCode, qualityLevel, useTrackedShipping,
                useTrackedShipping);
    }

    private void testTrackedShipping(CountryCode countryCode,
            QualityLevel qualityLevel, boolean useTrackedShipping,
            boolean expected) throws MalformedURLException {
        Order order = new Order(pwinty, countryCode, countryCode, qualityLevel,
                useTrackedShipping);
        order.setAddress1("-");
        order.setAddress2("-");
        order.setAddressTownOrCity("-");
        order.setPostalOrZipCode("-");
        order.setRecipientName("-");
        order.setStateOrCounty("-");

        int id = order.getId();
        assertEquals(Status.NotYetSubmitted, order.getStatus());

        URL url = new URL(TEST_PHOTO_URL);

        order.addPhoto(url, Type._4x6, 1, Sizing.Crop);
        Order fetchedOrder = pwinty.getOrder(id);

        assertEquals(countryCode + "-" + qualityLevel + "-"
                + useTrackedShipping, expected, fetchedOrder.getShippingInfo().getShipments().get(0)
                .isTracked());
    }

    @Test
    public void no_postcode_is_ok_for_IE() throws URISyntaxException,
            MalformedURLException {
        Order order = new Order(pwinty, CountryCode.IE, CountryCode.IE,
                QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
        order.setRecipientName("bloggs");
        order.setStateOrCounty("somewhereInIreland");

        assertEquals(Status.NotYetSubmitted, order.getStatus());
        assertEquals("-", order.getPostalOrZipCode());

        URL resource = OrderTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        order.addPhoto(file, Type._10x15_cm, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = order.getSubmissionStatus();
        assertEquals(
                "SubmissionStatus is not valid: " + submissionStatus.toString(),
                true, submissionStatus.isValid());
    }

    @Test
    @Ignore("Looks like this has changed...")
    public void cannot_ship_standard_internationally()
            throws URISyntaxException {
        try {
            new Order(pwinty, GB, CountryCode.AD, QualityLevel.Standard, false);
            fail("International shipping for standard prints from GB should not be allowed...");
        } catch (PwintyError pe) {
            assertTrue(pe.getErrorMessage().toLowerCase()
                    .contains("no shipping rates available"));
        }
    }

    @Test
    public void cannot_order_standard_pano() throws URISyntaxException {
        try {
            Order order = new Order(pwinty, GB, GB, QualityLevel.Standard,
                    false);

            URL resource = OrderTest.class.getResource(TEST_PHOTO_LOCAL);
            File file = new File(resource.toURI());

            order.addPhoto(file, Type._4x18, 1, Sizing.Crop);
            fail();
        } catch (PwintyError pe) {
            assertTrue(pe.getErrorMessage().toLowerCase()
                    .contains("no item of type 4x18"));
        }
    }

    @Test
    public void can_ship_pro_internationally() throws URISyntaxException {
        new Order(pwinty, GB, CountryCode.AD, QualityLevel.Pro, false);
    }

    @Test
    public void error_is_thrown_if_order_not_in_correct_state()
            throws URISyntaxException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
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
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
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
    public void can_add_metric_photo_size() throws MalformedURLException {
        Order order = new Order(pwinty, CountryCode.IE, CountryCode.IE,
                QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
        order.setPostalOrZipCode("zip");
        order.setRecipientName("bloggs");
        order.setStateOrCounty("bristol");

        assertEquals(Status.NotYetSubmitted, order.getStatus());

        URL url = new URL(TEST_PHOTO_URL);

        order.addPhoto(url, Type._10x15_cm, 1, Sizing.Crop);

        SubmissionStatus submissionStatus = order.getSubmissionStatus();

        assertEquals(
                "SubmissionStatus is not valid: " + submissionStatus.toString(),
                true, submissionStatus.isValid());
    }

    @Test
    public void can_get_photo_details() throws MalformedURLException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);

        URL url = new URL(TEST_PHOTO_URL);
        order.addPhoto(url, Type._4x6, 2, Sizing.ShrinkToExactFit);
        SubmissionStatus submissionStatus = order.getSubmissionStatus();
        int photoId = submissionStatus.getPhotos().get(0).getId();

        Photo photo = pwinty.getPhoto(order.getId(), photoId);
        assertEquals(2, photo.getCopies());
        assertEquals(Photo.Sizing.ShrinkToExactFit, photo.getSizing());
    }

    @Test
    public void can_delete_photo_from_order() throws MalformedURLException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);

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
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);

        int id = order.getId();

        order.cancel();
        Order fetchedOrder = pwinty.getOrder(id);
        assertEquals(Status.Cancelled, fetchedOrder.getStatus());
    }

    @Test
    public void cannot_cancel_submitted_order() throws URISyntaxException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
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
    public void error_with_bad_api_keys() {
        Pwinty unauthorizedPwinty = new Pwinty(Environment.SANDBOX, "", "");
        try {
            new Order(unauthorizedPwinty, GB, GB, QualityLevel.Standard, false);
            fail("Should have thrown");
        } catch (PwintyError e) {
            assertEquals(401, e.getCode());
        }
    }

    @Test
    public void can_serialize_json_order_photo_type() {
        String json = "{\"id\":1,\"address1\":\"1 street,\",\"address2\":\"blah,\",\"postalOrZipCode\":\"peecode\",\"country\":\"GB\",\"addressTownOrCity\":\"bristol.\",\"recipientName\":\"matt burns\",\"textOnReverse\":null,\"stateOrCounty\":\"Bristol.\",\"status\":\"NotYetSubmitted\",\"payment\":null,\"paymentUrl\":null,\"photos\":[{\"id\":123,\"type\":\"4x18\",\"url\":\"http://www.g.com\",\"status\":\"Ok\",\"copies\":1,\"sizing\":\"Crop\",\"orderId\":1,\"price\":null}],\"documents\":[],\"stickers\":[{\"id\":456,\"orderId\":1,\"fileName\":\"sticker.jpg\"}]}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Type.class, new TypeDeserializer());
        Gson gson = gsonBuilder.create();
        Order order = gson.fromJson(json, Order.class);
        assertEquals(Type._4x18, order.getPhotos().get(0).getType());
    }

    @Test
    @Ignore("This test no longer relevant now that batch adding is possible")
    public void can_add_50_photos_in_25_seconds() throws MalformedURLException {
        long startTime = System.currentTimeMillis();
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);

        for (int i = 0; i < 50; i++) {
            order.addPhoto(new URL(TEST_PHOTO_URL), Type._4x6, 1, Sizing.Crop);
        }
        long timeTaken = System.currentTimeMillis() - startTime;
        Assert.assertTrue("Took " + timeTaken + "ms", timeTaken < 25000);
    }

    @Test
    public void can_change_quality_using_clone() throws URISyntaxException {
        Order order = new Order(pwinty, GB, CountryCode.GB,
                QualityLevel.Standard, false);

        assertEquals(QualityLevel.Standard, order.getQualityLevel());
        int originalId = order.getId();
        order = order.createCloneWithQualityLevel(QualityLevel.Pro, false);
        int newId = order.getId();

        assertEquals(QualityLevel.Pro, order.getQualityLevel());
        assertFalse(originalId == newId);
    }

    @Test
    public void only_minimum_address_needed()
            throws URISyntaxException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddressTownOrCity("toc");
        order.setPostalOrZipCode("zip");

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
    public void cant_get_delivery_estimate_before_photo_added() throws MalformedURLException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard,
                false);
        order.setAddress1("-");
        order.setAddress2("-");
        order.setAddressTownOrCity("-");
        order.setPostalOrZipCode("-");
        order.setRecipientName("-");
        order.setStateOrCounty("-");

        int id = order.getId();
        assertEquals(Status.NotYetSubmitted, order.getStatus());

        Order fetchedOrder = pwinty.getOrder(id);

        assertEquals(0, fetchedOrder.getShippingInfo().getShipments().size());
    }

    @Ignore("Sandbox is currently returning nulls. Re-enable once pwinty fix this.")
    @Test
    public void can_get_estimated_deliveries_once_photo_added() throws URISyntaxException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
        order.setPostalOrZipCode("zip");
        order.setRecipientName("bloggs");
        order.setStateOrCounty("bristol");

        int id = order.getId();
        assertEquals(Status.NotYetSubmitted, order.getStatus());

        URL resource = OrderTest.class.getResource(TEST_PHOTO_LOCAL);
        File file = new File(resource.toURI());

        order = pwinty.getOrder(id); // update with latest
        assertNull(order.getEarliestEstimatedArrivalDate());
        assertNull(order.getLatestEstimatedArrivalDate());

        order.addPhoto(file, Type._4x6, 1, Sizing.Crop);

        order = pwinty.getOrder(id); // update with latest
        assertTrue(order.getEarliestEstimatedArrivalDate().isAfter(DateTime.now()));
        assertTrue(order.getLatestEstimatedArrivalDate().isAfter(DateTime.now()));
        assertTrue(order.getEarliestEstimatedArrivalDate().isBefore(order.getLatestEstimatedArrivalDate()));
    }

    @Test
    public void can_report_lost_in_post()
            throws URISyntaxException {
        Order order = new Order(pwinty, GB, GB, QualityLevel.Standard, false);
        order.setAddress1("ad1");
        order.setAddress2("ad2");
        order.setAddressTownOrCity("toc");
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

        Issue lostIssue = new Issue(Issue.IssueType.LostInPost, Issue.IssueAction.Reprint);
        //long issueId = fetchedOrder.addIssue(lostIssue).getId();

        //Issue fetchedIssue = fetchedOrder.getIssue(issueId);
        //assertEquals(IssueAction.Reprint, fetchedIssue.getAction());
        Issues fetchedIssues = fetchedOrder.getIssues();
        assertEquals(0, fetchedIssues.getIssues().size());

        Issue addedIssue = fetchedOrder.addIssue(lostIssue);
        assertEquals(Issue.IssueType.LostInPost, addedIssue.getIssue());

        // update fetchedIssues
        fetchedIssues = fetchedOrder.getIssues();
        
        assertEquals(Issue.IssueType.LostInPost, fetchedIssues.getIssues().get(0).getIssue());
        assertEquals(Issue.IssueState.Open, fetchedIssues.getIssues().get(0).getIssueState());
        assertTrue(fetchedIssues.getIssues().get(0).getCommentary().contains("Issue created"));
    }
}
