package uk.co.mattburns.pwinty.v2_2;

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
        // @formatter:off
        _3x3, _3x4, _4x4, _4x5, _4x6, _5x7, _6x6, _6x8, _8x8, _8x10, _8x12, _4x18, _10x12, _10x15, _12x16,

        _P10x15, _P11x14, _P12x18, _P16x20, _P16x24, _P18x18, _P18x24, _P20x30, _P20x24, _P24x24, _P24x36, _P30x40,

        _C8x10,        _C8x12,        _C10x12,        _C11x14,        _C12x12,        _C12x16,        _C12x18,        _C12x24,        _C16x16,        _C16x20,        _C16x24,        _C18x24,        _C20x20,        _C20x24,        _C20x30,        _C24x24,
                                                                      _C12x12_1_25,                   _C12x18_1_25,   _C12x24_1_25,   _C16x16_1_25,   _C16x20_1_25,                   _C20x30_1_25,                   _C24x24_1_25,   _C30x30_1_25,
        _C8x10_nowrap, _C8x12_nowrap, _C10x12_nowrap, _C11x14_nowrap, _C12x12_nowrap, _C12x16_nowrap, _C12x18_nowrap, _C12x24_nowrap, _C16x16_nowrap, _C16x20_nowrap, _C16x24_nowrap, _C18x24_nowrap, _C20x20_nowrap, _C20x24_nowrap, _C20x30_nowrap, _C24x24_nowrap,

        _F10x24_4x18, _F12x12_10x10, _F12x16_8x10, _F12x16_8x12, _F12x16_10x12, _F16x20_12x16, _F20x20_18x18, _F20x28_16x20, _F20x28_16x24, _F20x28_18x24, _F28x40_24x36,

        // Metric sizes:
        _9x12_cm, _9x13_cm, _10x14_cm, _10x15_cm, _11x15_cm, _11x17_cm, _13x17_cm, _13x19_cm, _P20x27_cm, _P20x30_cm, _P30x40_cm, _P40x40_cm, _P40x53_cm, _P40x60_cm, _P50x66_cm, _P50x70_cm, _P60x80_cm, _P60x90_cm, _P70x93_cm, _P70x105_cm, _P80x106_cm, _P80x120_cm, _C20x30_cm, _C30x45_cm, _C40x40_cm, _C40x60_cm, _C60x60_cm, _C60x90_cm, _C80x120_cm, _C90x90_cm,

        // Merch
        _fridge_magnet, _iphone6_case,_iphone6plus_case, _keyring_acrylic, _keyring_pu, _mug_10oz_white, _mug_15oz_white, _photopanel_5x7, _photopanel_6x6, _photopanel_8x10, _photopanel_11x14, _samsunggalaxys5_case, _samsunggalaxys6_case, _samsunggalaxys6edge_case,

        // Fineart
        _FineArt_12x16, _FineArt_16x16, _FineArt_20x30, _FineArt_24x24;
        // @formatter:on

        @Override
        public String toString() {
            return super.toString().replaceFirst("_", "");
        }

        public static Type fromName(String name) {
            return valueOf("_" + name.replace(".", "_"));
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
