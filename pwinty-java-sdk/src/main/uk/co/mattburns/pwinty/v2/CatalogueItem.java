package uk.co.mattburns.pwinty.v2;

import uk.co.mattburns.pwinty.v2.Photo.Type;

public class CatalogueItem {

    private String name;
    private String description;
    private double horizontalSize;
    private double verticalSize;
    private SizeUnits sizeUnits;
    private int priceGBP;
    private int priceUSD;
    private int recommendedHorizontalResolution;
    private int recommendedVerticalResolution;

    public CatalogueItem() {
    }

    public enum SizeUnits {
        inches, cm
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return Type.fromName(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHorizontalSize() {
        return horizontalSize;
    }

    public void setHorizontalSize(double horizontalSize) {
        this.horizontalSize = horizontalSize;
    }

    public double getVerticalSize() {
        return verticalSize;
    }

    public void setVerticalSize(double verticalSize) {
        this.verticalSize = verticalSize;
    }

    public SizeUnits getSizeUnits() {
        return sizeUnits;
    }

    public void setSizeUnits(SizeUnits sizeUnits) {
        this.sizeUnits = sizeUnits;
    }

    public int getPriceGBP() {
        return priceGBP;
    }

    public void setPriceGBP(int priceGBP) {
        this.priceGBP = priceGBP;
    }

    public int getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(int priceUSD) {
        this.priceUSD = priceUSD;
    }

    public int getRecommendedHorizontalResolution() {
        return recommendedHorizontalResolution;
    }

    public void setRecommendedHorizontalResolution(
            int recommendedHorizontalResolution) {
        this.recommendedHorizontalResolution = recommendedHorizontalResolution;
    }

    public int getRecommendedVerticalResolution() {
        return recommendedVerticalResolution;
    }

    public void setRecommendedVerticalResolution(
            int recommendedVerticalResolution) {
        this.recommendedVerticalResolution = recommendedVerticalResolution;
    }

}
