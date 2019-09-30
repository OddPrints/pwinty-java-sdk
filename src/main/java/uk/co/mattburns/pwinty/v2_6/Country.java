package uk.co.mattburns.pwinty.v2_6;

public class Country {
    private CountryCode countryCode;
    private Boolean hasProducts;
    private String name;

    public Country() {}

    public Country(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public Boolean getHasProducts() {
        return hasProducts;
    }

    public void setHasProducts(Boolean hasProducts) {
        this.hasProducts = hasProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country [countryCode="
                + countryCode
                + ", hasProducts="
                + hasProducts
                + ", name="
                + name
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Country other = (Country) obj;
        if (countryCode == null) {
            if (other.countryCode != null) return false;
        } else if (!countryCode.equals(other.countryCode)) return false;
        return true;
    }
}
