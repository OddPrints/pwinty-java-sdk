package uk.co.mattburns.pwinty.v1;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.mattburns.pwinty.v1.Photo.Sizing;

public class Order {

    private int id;
    private Status status;
    private List<Photo> photos = new ArrayList<Photo>();
    @Deprecated
    private List<Document> documents = new ArrayList<Document>();
    @Deprecated
    private List<Sticker> stickers = new ArrayList<Sticker>();
    private String recipientName;
    private String address1;
    private String address2;
    private String addressTownOrCity;
    private String stateOrCounty;
    private String postalOrZipCode;
    private String country;

    private Pwinty pwinty;

    public enum Status {
        NotYetSubmitted, Submitted, Complete, Cancelled;
    }

    // needed for GSON
    Order() {
    }

    public Order(Pwinty pwinty) {
        this.pwinty = pwinty;
        Order order = pwinty.createOrder(this);
        overwriteThisOrderWithGivenOrder(order);
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    @Deprecated
    public List<Document> getDocuments() {
        return documents;
    }

    @Deprecated
    public List<Sticker> getStickers() {
        return stickers;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
        overwriteThisOrderWithGivenOrder(pwinty.updateOrder(id, this));
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
        overwriteThisOrderWithGivenOrder(pwinty.updateOrder(id, this));
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
        overwriteThisOrderWithGivenOrder(pwinty.updateOrder(id, this));
    }

    public String getAddressTownOrCity() {
        return addressTownOrCity;
    }

    public void setAddressTownOrCity(String addressTownOrCity) {
        this.addressTownOrCity = addressTownOrCity;
        overwriteThisOrderWithGivenOrder(pwinty.updateOrder(id, this));
    }

    public String getStateOrCounty() {
        return stateOrCounty;
    }

    public void setStateOrCounty(String stateOrCounty) {
        this.stateOrCounty = stateOrCounty;
        overwriteThisOrderWithGivenOrder(pwinty.updateOrder(id, this));
    }

    public String getPostalOrZipCode() {
        return postalOrZipCode;
    }

    public void setPostalOrZipCode(String postalOrZipCode) {
        this.postalOrZipCode = postalOrZipCode;
        overwriteThisOrderWithGivenOrder(pwinty.updateOrder(id, this));
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        overwriteThisOrderWithGivenOrder(pwinty.updateOrder(id, this));
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", status=" + status + ", photos=" + photos
                + ", documents=" + documents + ", stickers=" + stickers
                + ", recipientName=" + recipientName + ", address1=" + address1
                + ", address2=" + address2 + ", addressTownOrCity="
                + addressTownOrCity + ", stateOrCounty=" + stateOrCounty
                + ", postalOrZipCode=" + postalOrZipCode + ", country="
                + country + "]";
    }

    private void refreshOrder() {
        overwriteThisOrderWithGivenOrder(pwinty.getOrder(id));
    }

    private void overwriteThisOrderWithGivenOrder(Order updated) {
        id = updated.id;
        status = updated.status;
        photos = updated.photos;
        documents = updated.documents;
        stickers = updated.stickers;
        recipientName = updated.recipientName;
        address1 = updated.address1;
        address2 = updated.address2;
        addressTownOrCity = updated.addressTownOrCity;
        stateOrCounty = updated.stateOrCounty;
        postalOrZipCode = updated.postalOrZipCode;
        country = updated.country;
    }

    public void cancel() {
        pwinty.cancelOrder(id);
        refreshOrder();
    }

    public SubmissionStatus getSubmissionStatus() {
        return pwinty.getSubmissionStatus(id);
    }

    /**
     * Add a photo File object to the order. This method will block until the
     * File is uploaded.
     */
    public Photo addPhoto(File photo, Photo.Type type, int copies, Sizing sizing) {
        Photo addedPhoto = pwinty.addPhotoToOrder(id, photo, type, copies,
                sizing);
        refreshOrder();
        return addedPhoto;
    }

    /**
     * Add a photo to the order using a public URL.
     */
    public Photo addPhoto(URL photoUrl, Photo.Type type, int copies,
            Sizing sizing) {
        Photo addedPhoto = pwinty.addPhotoToOrder(id, photoUrl, type, copies,
                sizing);
        refreshOrder();
        return addedPhoto;
    }

    public void deletePhoto(Photo photo) {
        pwinty.deletePhoto(photo.getId());
        overwriteThisOrderWithGivenOrder(pwinty.getOrder(id));
    }

    @Deprecated
    public Document addDocument(String filename, File document) {
        Document addedDocument = pwinty.addDocumentToOrder(id, filename,
                document);
        refreshOrder();
        return addedDocument;
    }

    @Deprecated
    public void deleteDocument(Document document) {
        pwinty.deleteDocument(document.getId());
        refreshOrder();
    }

    @Deprecated
    public Sticker addSticker(String filename, File document) {
        Sticker addedSticker = pwinty.addStickerToOrder(id, filename, document);
        refreshOrder();
        return addedSticker;
    }

    @Deprecated
    public Sticker addSticker(String filename, InputStream stream) {
        Sticker addedSticker = pwinty.addStickerToOrder(id, filename, stream);
        refreshOrder();
        return addedSticker;
    }

    @Deprecated
    public void deleteSticker(Sticker sticker) {
        pwinty.deleteSticker(sticker.getId());
        refreshOrder();
    }

    /**
     * Submit the Order for printing and shipping
     * 
     * If an error occurs, a {@link PwintyError} will be thrown
     */
    public void submit() {
        pwinty.submitOrder(id);
        overwriteThisOrderWithGivenOrder(pwinty.getOrder(id));
    }

    void setPwinty(Pwinty pwinty) {
        this.pwinty = pwinty;
    }
}
