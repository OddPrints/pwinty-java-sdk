package uk.co.mattburns.pwinty.v2;

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
        _4x4, _4x6, _5x7, _6x6, _8x8, _8x10, _8x12, _4x18, _10x12, _10x15, _12x16,

        _P12x18, _P16x20, _P16x24, _P18x18, _P18x24, _P20x30, _P24x24, _P24x36,

        _C8x10, _C10x12, _C12x12, _C12x16, _C12x18, _C12x24, _C16x16, _C16x20, _C16x24, _C20x20, _C20x24, _C20x30, _C24x24,

        _F12x16_8x10, _F12x16_8x12, _F12x16_10x12, _F16x20_12x16, _F20x28_16x20, _F20x28_16x24, _F20x28_18x24, _F28x40_24x36

        ;

        @Override
        public String toString() {
            return super.toString().replace("_", "");
        }

        public static Type fromName(String name) {
            return valueOf("_" + name);
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
