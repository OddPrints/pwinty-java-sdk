package uk.co.mattburns.pwinty;

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

    @Override
    public String toString() {
        return "Document [id=" + id + ", fileName=" + fileName + ", pages="
                + pages + ", orderId=" + orderId + "]";
    }

}
