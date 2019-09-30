package uk.co.mattburns.pwinty.v2_6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.mattburns.pwinty.v2_6.CatalogueItem.SizeUnits;
import uk.co.mattburns.pwinty.v2_6.Order.QualityLevel;
import uk.co.mattburns.pwinty.v2_6.Pwinty.Environment;

public class CatalogueTest {

    private static final Country GB = new Country(CountryCode.GB);
    private static final Country AU = new Country(CountryCode.AU);
    private static final Country IE = new Country(CountryCode.IE);

    private static final double DELTA = 0.00001; // for double testing

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
    public void can_fetch_catalogue() {
        Catalogue catalogue = pwinty.getCatalogue(GB.getCountryCode(), QualityLevel.Pro);
        assertEquals("UNITED KINGDOM", catalogue.getCountry());
        assertEquals(GB.getCountryCode(), catalogue.getCountryCode());

        boolean testedItem = false;
        for (CatalogueItem item : catalogue.getItems()) {
            if (item.getDescription().equalsIgnoreCase("4x6 inch print")) {
                assertEquals(4, item.getImageHorizontalSize(), DELTA);
                assertEquals(6, item.getImageVerticalSize(), DELTA);
                assertEquals(SizeUnits.inches, item.getSizeUnits());
                assertEquals(600, item.getRecommendedHorizontalResolution());
                assertEquals(900, item.getRecommendedVerticalResolution());
                testedItem = true;
            }
        }
        assertTrue("Couldn't find the item I wanted to test!", testedItem);

        testedItem = false;
        for (CatalogueItem item : catalogue.getItems()) {
            if (item.getDescription().equalsIgnoreCase("4x18 poster mounted in 10x24 frame")) {
                assertEquals(4, item.getImageHorizontalSize(), DELTA);
                assertEquals(18, item.getImageVerticalSize(), DELTA);
                assertEquals(10, item.getFullProductHorizontalSize(), DELTA);
                assertEquals(24, item.getFullProductVerticalSize(), DELTA);
                assertEquals(SizeUnits.inches, item.getSizeUnits());
                assertEquals(600, item.getRecommendedHorizontalResolution());
                assertEquals(2700, item.getRecommendedVerticalResolution());
                testedItem = true;
            }
        }
        assertTrue("Couldn't find the item I wanted to test!", testedItem);

        catalogue = pwinty.getCatalogue(AU.getCountryCode(), QualityLevel.Standard);
        assertEquals("AUSTRALIA", catalogue.getCountry());
        assertEquals(AU.getCountryCode(), catalogue.getCountryCode());
        testedItem = false;
        for (CatalogueItem item : catalogue.getItems()) {
            if (item.getDescription()
                    .equalsIgnoreCase(
                            "20x30 inch gallery wrapped canvas on 1.25 inch stretchers")) {
                assertEquals(22.75, item.getImageHorizontalSize(), DELTA);
                assertEquals(32.75, item.getImageVerticalSize(), DELTA);
                assertEquals(20, item.getFullProductHorizontalSize(), DELTA);
                assertEquals(30, item.getFullProductVerticalSize(), DELTA);
                assertEquals(SizeUnits.inches, item.getSizeUnits());
                assertEquals(3413, item.getRecommendedHorizontalResolution());
                assertEquals(4913, item.getRecommendedVerticalResolution());
                testedItem = true;
            }
        }
        assertTrue("Couldn't find the item I wanted to test!", testedItem);

        catalogue = pwinty.getCatalogue(IE.getCountryCode(), QualityLevel.Standard);
        assertEquals("IRELAND", catalogue.getCountry());
        assertEquals(IE.getCountryCode(), catalogue.getCountryCode());
        testedItem = false;
        for (CatalogueItem item : catalogue.getItems()) {
            if (item.getDescription().equalsIgnoreCase("9x12 cm print")) {
                assertEquals(9, item.getImageHorizontalSize(), DELTA);
                assertEquals(12, item.getImageVerticalSize(), DELTA);
                assertEquals(9, item.getFullProductHorizontalSize(), DELTA);
                assertEquals(12, item.getFullProductVerticalSize(), DELTA);
                assertEquals(SizeUnits.cm, item.getSizeUnits());
                assertEquals(540, item.getRecommendedHorizontalResolution());
                assertEquals(720, item.getRecommendedVerticalResolution());
                testedItem = true;
            }
        }
        assertTrue("Couldn't find the item I wanted to test!", testedItem);
    }

    @Test
    public void can_get_shipping_rates() {
        Catalogue catalogue = pwinty.getCatalogue(GB.getCountryCode(), QualityLevel.Standard);
        List<CatalogueShippingRate> rates = catalogue.getShippingRates();

        CatalogueShippingRate foundRate = null;
        for (CatalogueShippingRate rate : rates) {
            if (rate.getBand().equals("Prints")) {
                foundRate = rate;
                break;
            }
        }
        assertEquals("Prints", foundRate.getBand());
        assertEquals("2nd Class Royal Mail", foundRate.getDescription());
        assertEquals(199, foundRate.getPrice());
        assertEquals("GBP", foundRate.getCurrency());
        assertEquals(false, foundRate.isTracked());
    }

    @Test
    public void photo_types_with_multiple_underscores_are_ok() {
        assertEquals("10x15_cm", Photo.Type._10x15_cm.toString());
    }
}
