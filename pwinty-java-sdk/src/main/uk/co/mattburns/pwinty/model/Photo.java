package uk.co.mattburns.pwinty.model;

import java.net.URL;

public class Photo {

    private Integer id;
    private Type type;
    private URL url;
    private Status status;
    private int copies;
    private Sizing sizing;
    private int orderId;

    public enum Type {
        _4x6, _5x7, _6x6, _8x8, _8x10, _8x12, _P30x40, _P30x45, _P40x50;

        @Override
        public String toString() {
            return super.toString().replace("_", "");
        }
    }

    public enum Status {
        AwaitingUrlOrData, NotYetDownloaded, Ok, FileNotFoundAtUrl, Invalid;
    }

    public enum Sizing {
        Crop, ShrinkToFit, ShrinkToExactFit;
    }

    public Integer getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public URL getUrl() {
        return url;
    }

    public Status getStatus() {
        return status;
    }

    public int getCopies() {
        return copies;
    }

    public Sizing getSizing() {
        return sizing;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Photo [id=" + id + ", type=" + type + ", url=" + url
                + ", status=" + status + ", copies=" + copies + ", sizing="
                + sizing + ", orderId=" + orderId + "]";
    }

}
