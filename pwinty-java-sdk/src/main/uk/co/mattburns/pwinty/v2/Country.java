package uk.co.mattburns.pwinty.v2;

public class Country {
    private String countryCode;
    private Boolean hasProducts;
    private String name;
    
    public Country() {
    }
    
    public Country(String countryCode) {
        this.countryCode = countryCode;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(String countryCode) {
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
        return "Country [countryCode=" + countryCode + ", hasProducts="
                + hasProducts + ", name=" + name + "]";
    }
}