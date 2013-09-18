package uk.co.mattburns.pwinty;

@Deprecated
public class Sticker {

    private Integer id;
    private String fileName;
    private int orderId;

    public Integer getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Sticker [id=" + id + ", fileName=" + fileName + ", orderId="
                + orderId + "]";
    }

}
