package uk.co.mattburns.pwinty.v2_6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.mattburns.pwinty.v2_6.Order.QualityLevel;
import uk.co.mattburns.pwinty.v2_6.Photo.Type;
import uk.co.mattburns.pwinty.v2_6.Pwinty.Environment;

public class PwintyTest {

    private static final Country AU = new Country(CountryCode.AU);
    private static final Country GB = new Country(CountryCode.GB);
    private static final Country US = new Country(CountryCode.US);

    private static Pwinty pwinty;

    @BeforeClass
    public static void before() {
        Properties props = TestUtils.loadProps();

        pwinty =
                new Pwinty(
                        Environment.SANDBOX,
                        props.getProperty("PWINTY_MERCHANT_ID_SANDBOX"),
                        props.getProperty("PWINTY_MERCHANT_KEY_SANDBOX"),
                        System.out);
    }

    @Test
    public void can_fetch_countries() {
        List<Country> countries = pwinty.getCountries();
        for (Country country : countries) {
            System.out.print("," + country.getCountryCode());
        }
    }

    @Test
    public void can_deserialize_json() {
        /* @formatter:off */
        String json =
                "{"
                        + "\"id\":123,"
                        + "\"address1\":null,"
                        + "\"address2\":null,"
                        + "\"postalOrZipCode\":null,"
                        + "\"countryCode\":\"GB\","
                        + "\"addressTownOrCity\":null,"
                        + "\"recipientName\":null,"
                        + "\"stateOrCounty\":null,"
                        + "\"status\":\"NotYetSubmitted\","
                        + "\"payment\":\"InvoiceMe\","
                        + "\"paymentUrl\":null,"
                        + "\"destinationCountryCode\":\"GB\","
                        + "\"price\":350,"
                        + "\"qualityLevel\":\"Pro\","
                        + "\"shippingInfo\":{"
                        + "\"price\": 199,"
                        + "\"shipments\":["
                        + "{"
                        + "\"shipmentId\": null,"
                        + "\"trackingNumber\": null,"
                        + "\"trackingUrl\": null,"
                        + "\"isTracked\": true,"
                        + "\"earliestEstimatedArrivalDate\": \"2015-03-30T16:17:56.9615519Z\","
                        + "\"latestEstimatedArrivalDate\": \"2015-04-04T16:17:56.9615519Z\","
                        + "\"shippedOn\": null"
                        + "}"
                        + "]"
                        + "},"
                        + "\"photos\":["
                        + "{"
                        + "\"id\":2638,"
                        + "\"type\":\"4x6\","
                        + "\"url\":null,"
                        + "\"status\":\"Ok\","
                        + "\"copies\":1,"
                        + "\"sizing\":\"Crop\","
                        + "\"priceToUser\":null,"
                        + "\"price\":60,"
                        + "\"md5Hash\":null,"
                        + "\"previewUrl\":\"https://pwintysandbox.s3.amazonaws.com/2575/c0a02d5c-2df2-40f3-b261-46948a6449c8?AWSAccessKeyId=AKIAJXAKHZJEAELFLMPA&Expires=1384538242&Signature=ujIDtEzHTrDUpeNPWqOexmSxU%2B4%3D\",\"thumbnailUrl\":\"https://pwintysandbox.s3.amazonaws.com/2575/00000000-0000-0000-0000-000000000000?AWSAccessKeyId=AKIAJXAKHZJEAELFLMPA&Expires=1384538242&Signature=6g7QdHsVrrH09SbeHduBG6JXoYk%3D\","
                        + "\"errorMessage\":null"
                        + "}"
                        + "],"
                        + "\"errorMessage\":null"
                        + "}";
        /* @formatter:on */
        Order o = Pwinty.fromJson(json, Order.class);
        assertEquals(123, o.getId());
        assertEquals(CountryCode.GB, o.getCountryCode());
        assertEquals(null, o.getShippingInfo().getShipments().get(0).getTrackingUrl());
        assertEquals(
                new DateTime("2015-03-30T16:17:56.9615519Z"),
                o.getShippingInfo().getShipments().get(0).getEarliestEstimatedArrivalDate());
        assertEquals(199, o.getShippingInfo().getPrice());
    }

    @Test
    public void can_fetch_catalogue() {
        Catalogue catalogue = pwinty.getCatalogue(GB.getCountryCode(), QualityLevel.Pro);
        assertEquals("UNITED KINGDOM", catalogue.getCountry());
        assertEquals(GB.getCountryCode(), catalogue.getCountryCode());

        Set<Photo.Type> availablePrintSizes = new HashSet<>();
        for (CatalogueItem item : catalogue.getItems()) {
            availablePrintSizes.add(item.getType());
        }
        assertTrue(availablePrintSizes.contains(Type._4x6));
        assertTrue(availablePrintSizes.contains(Type._4x18));
    }

    @Test
    public void can_fetch_oz_catalogue() {
        Catalogue catalogue = pwinty.getCatalogue(AU.getCountryCode(), QualityLevel.Standard);
        assertEquals("AUSTRALIA", catalogue.getCountry());
        assertEquals(AU.getCountryCode(), catalogue.getCountryCode());

        Set<Photo.Type> availablePrintSizes = new HashSet<>();
        for (CatalogueItem item : catalogue.getItems()) {
            availablePrintSizes.add(item.getType());
        }
        assertTrue(availablePrintSizes.contains(Type._4x6));
    }

    @Test
    public void can_silently_ignore_unrecognised_print_sizes() {
        Catalogue catalogue = pwinty.getCatalogue(GB.getCountryCode(), QualityLevel.Pro);
        catalogue.containsType(Type._4x4); // This should always work
    }

    @Test
    public void can_fetch_all_catalogues() {
        Set<Photo.Type> availablePrintSizes = new HashSet<>();
        for (CatalogueItem item :
                pwinty.getCatalogue(GB.getCountryCode(), QualityLevel.Standard).getItems()) {
            availablePrintSizes.add(item.getType());
        }
        for (CatalogueItem item :
                pwinty.getCatalogue(US.getCountryCode(), QualityLevel.Standard).getItems()) {
            availablePrintSizes.add(item.getType());
        }
        for (CatalogueItem item :
                pwinty.getCatalogue(GB.getCountryCode(), QualityLevel.Pro).getItems()) {
            availablePrintSizes.add(item.getType());
        }
        for (CatalogueItem item :
                pwinty.getCatalogue(US.getCountryCode(), QualityLevel.Pro).getItems()) {
            availablePrintSizes.add(item.getType());
        }
    }

    @Test
    public void no_pano_in_standard() {
        Catalogue catalogue = pwinty.getCatalogue(GB.getCountryCode(), QualityLevel.Standard);

        Set<Photo.Type> availablePrintSizes = new HashSet<>();
        for (CatalogueItem item : catalogue.getItems()) {
            availablePrintSizes.add(item.getType());
        }
        assertTrue(availablePrintSizes.contains(Type._4x6));
        assertFalse(availablePrintSizes.contains(Type._4x18));
    }

    /**
     * This test just forces us to keep the enum up to date.
     */
    @Test
    public void all_available_print_types_are_in_enum() {
        Set<String> unrecognisedItems = pwinty.getUnrecognisedItemNames(CountryCode.values());
        String errorMessage = "Unrecognised items:";
        for (String item : unrecognisedItems) {
            errorMessage += " " + item;
        }

        errorMessage += "\n\nThe Photo.Types should now look like this:\n\n";
        if (!unrecognisedItems.isEmpty()) {
            Set<String> allItems = pwinty.getAllItemNames(CountryCode.values());
            for (String itemName : allItems) {
                errorMessage += "_" + itemName.replaceAll("\\.", "_") + ",\n";
            }
        }
        assertTrue(errorMessage, unrecognisedItems.isEmpty());
    }
}
