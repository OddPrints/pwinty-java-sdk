package uk.co.mattburns.pwinty.model;


public class Document {

    private Integer id;
    private String fileName;
    private int pages;
    private int orderId;

    public Integer getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPages() {
        return pages;
    }

    public int getOrderId() {
        return orderId;
    }

}
