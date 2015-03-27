package uk.co.mattburns.pwinty.v2_2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.mattburns.pwinty.v2_2.Order.QualityLevel;
import uk.co.mattburns.pwinty.v2_2.Photo.Type;

public class Catalogue {

    private String country;
    private CountryCode countryCode;

    private QualityLevel qualityLevel;
    private List<CatalogueItem> items;
    private List<CatalogueShippingRate> shippingRates;

    public Catalogue() {
        super();
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public QualityLevel getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(QualityLevel qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public List<CatalogueItem> getItems() {
        return items;
    }

    public void setItems(List<CatalogueItem> items) {
        this.items = items;
    }

    public List<CatalogueShippingRate> getShippingRates() {
        return shippingRates;
    }

    public void setShippingRates(List<CatalogueShippingRate> shippingRates) {
        this.shippingRates = shippingRates;
    }

    public boolean containsType(Type type) {
        Set<Type> availablePrintSizes = new HashSet<Type>();
        for (CatalogueItem item : getItems()) {
            Type parsedType = null;
            try {
                parsedType = item.getType();
            } catch (IllegalArgumentException iae) {
                // There Pwinty offer a type that is not yet in the enum...
                System.err
                        .println("Unrecognised print size: " + item.getName());
            }
            if (parsedType != null) {
                availablePrintSizes.add(parsedType);
            }
        }

        return availablePrintSizes.contains(type);
    }

}
