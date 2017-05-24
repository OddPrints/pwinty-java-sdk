package uk.co.mattburns.pwinty.v2_3;

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
        _2x3,
        _3x3,
        _3x4,
        _3_5x5,
        _4x4,
        _4x5,
        _4x5_3,
        _4x6,
        _4x18,
        _5x5,
        _5x7,
        _6x6,
        _6x8,
        _8x8,
        _8x10,
        _8x12,
        _9x12_cm,
        _9x13_cm,
        _10x10,
        _10x10_cm,
        _10x12,
        _10x14_cm,
        _10x15,
        _10x15_cm,
        _11x11_cm,
        _11x15_cm,
        _11x17_cm,
        _12x16,
        _13x13_cm,
        _13x17_cm,
        _13x18_cm,
        _13x19_cm,
        _C_Float_16x24,
        _C8x8,
        _C8x8_nowrap,
        _C8x10,
        _C8x10_nowrap,
        _C8x12,
        _C8x12_nowrap,
        _C10x10,
        _C10x10_nowrap,
        _C10x12,
        _C10x12_nowrap,
        _C11x14,
        _C11x14_nowrap,
        _C12x12,
        _C12x12_1_25,
        _C12x12_nowrap,
        _C12x16,
        _C12x16_nowrap,
        _C12x18,
        _C12x18_1_25,
        _C12x18_nowrap,
        _C12x24,
        _C12x24_1_25,
        _C12x24_nowrap,
        _C16x16,
        _C16x16_1_25,
        _C16x16_nowrap,
        _C16x20,
        _C16x20_1_25,
        _C16x20_nowrap,
        _C16x24,
        _C16x24_nowrap,
        _C18x18,
        _C18x18_nowrap,
        _C18x24,
        _C18x24_nowrap,
        _C20x20,
        _C20x20_nowrap,
        _C20x24,
        _C20x24_nowrap,
        _C20x30,
        _C20x30_1_25,
        _C20x30_cm,
        _C20x30_nowrap,
        _C24x24,
        _C24x24_1_25,
        _C24x24_nowrap,
        _C24x30,
        _C24x30_nowrap,
        _C30x30_1_25,
        _C30x45_cm,
        _C40x40_cm,
        _C40x60_cm,
        _C60x60_cm,
        _C60x90_cm,
        _C80x120_cm,
        _C90x90_cm,
        _F_Surface_16x24,
        _F10x24_4x18,
        _F12x12_10x10,
        _F12x16_8x10,
        _F12x16_8x12,
        _F12x16_10x12,
        _F16x20_12x16,
        _F16x20_12x18,
        _F16x24_12x18,
        _F16x24_14x21,
        _F20x20_18x18,
        _F20x28_16x20,
        _F20x28_16x24,
        _F20x28_18x24,
        _F28x40_24x36,
        _FA0_Unmounted,
        _FA1,
        _FA1_Unmounted,
        _FA2,
        _FA2_Unmounted,
        _FineArt_8x10,
        _FineArt_12x16,
        _FineArt_16x16,
        _FineArt_16x20,
        _FineArt_16x24,
        _FineArt_18x24,
        _FineArt_20x20,
        _FineArt_20x28,
        _FineArt_20x30,
        _FineArt_24x24,
        _FineArt_24x30,
        _FineArt_24x36,
        _FineArt_28x40,
        _FineArt_30x30,
        _FineArt_30x40,
        _FineArt_A0,
        _FineArt_A1,
        _FineArt_A2,
        _fridge_magnet,
        _iphone6plus_case,
        _iphone6_case,
        _keyring_acrylic,
        _keyring_pu,
        _Magnet_4_4x4,
        _Magnet_4_6x6,
        _Magnet_9_6x6,
        _Magnet_Single_4x4,
        _mug_10oz_white,
        _mug_11oz_white,
        _mug_15oz_white,
        _P10x15,
        _P11x14,
        _P12x12,
        _P12x18,
        _P16x16,
        _P16x20,
        _P16x24,
        _P18x18,
        _P18x24,
        _P20x20,
        _P20x24,
        _P20x27_cm,
        _P20x30,
        _P20x30_cm,
        _P24x24,
        _P24x36,
        _P30x40,
        _P30x40_cm,
        _P40x40_cm,
        _P40x53_cm,
        _P40x60_cm,
        _P50x66_cm,
        _P50x70_cm,
        _P60x80_cm,
        _P60x90_cm,
        _P70x93_cm,
        _P70x105_cm,
        _P80x106_cm,
        _P80x120_cm,
        _photobook_7x5,
        _photobook_8x8,
        _photobook_11x8,
        _photobook_11x8_5_hardphotocover,
        _photobook_14cm_square_hardphotocover,
        _photobook_14cm_square_hardphotocover_mx,
        _photobook_14cm_square_softphotocover,
        _photobook_14cm_square_softphotocover_mx,
        _photobook_21cm_square_hardphotocover,
        _photobook_21cm_square_hardphotocover_br,
        _photobook_21cm_square_hardphotocover_mx,
        _photobook_21cm_square_softphotocover,
        _photobook_21cm_square_softphotocover_mx,
        _photobook_a4_l,
        _photobook_a4_landscape_hardlinencover,
        _photobook_a4_landscape_hardphotocover,
        _photobook_a4_landscape_hardphotocover_mx,
        _photobook_a4_landscape_softphotocover,
        _photobook_a4_landscape_softphotocover_mx,
        _photobook_a4_portrait_hardphotocover,
        _photobook_a4_portrait_hardphotocover_mx,
        _photobook_a4_portrait_softphotocover,
        _photobook_a4_portrait_softphotocover_mx,
        _photobook_a5_landscape_hardphotocover,
        _photobook_a5_landscape_hardphotocover_mx,
        _photobook_a5_landscape_softphotocover,
        _photobook_a5_landscape_softphotocover_mx,
        _photobook_a5_portrait_hardphotocover,
        _photobook_a5_portrait_hardphotocover_mx,
        _photobook_a5_portrait_softphotocover,
        _photobook_a5_portrait_softphotocover_mx,
        _photobook_a6_landscape_softcover,
        _photobook_a6_landscape_softphotocover,
        _photopanel_5x7,
        _photopanel_6x6,
        _photopanel_8x10,
        _photopanel_11x14,
        _samsunggalaxys5_case,
        _samsunggalaxys6_case,
        _samsunggalaxys6edge_case,
        _SurfaceMount_16x24,


        _BoxFrame_14x48_Unmounted,
        _ARQMediciFrame_22x38_16x32, _C12x18_1_25_US,
        _ARQMediciFrame_20x25_Unmounted,
        _GoldBeadFrame_16x24_Unmounted,
        _GoldBeadFrame_26x26_20x20,
        _F20x20_Unmounted,
        _FineArt_24x29,
        _GoldBeadFrame_32x40_Unmounted,
        _BoxFrame_12x24_Unmounted,
        _GoldBeadFrame_18x21_12x15,
        _F24x36_Unmounted,
        _C16x16_1_25_US,
        _GoldBeadFrame_12x18_6x12,
        _FineArt_28x36,
        _FineArt_28x34,
        _ARQMediciFrame_14x16_8x10,
        _BoxFrame_24x36_Unmounted,
        _C6x18_nowrap,
        _F18x30_12x24,
        _C20x60__25_US,
        _F8x10_Unmounted,
        _BoxFrame_22x38_16x32,
        _GoldBeadFrame_18x24_12x18,
        _BoxFrame_14x14_Unmounted,
        _GoldBeadFrame_16x32_Unmounted,
        _ARQMediciFrame_16x20_Unmounted,
        _ARQMediciFrame_15x17_9x12,
        _FA3,
        _BoxFrame_26x31_20x25,
        _FA3_Unmounted,
        _FineArt_10x18,
        _GoldBeadFrame_12x24_6x18,
        _GoldBeadFrame_16x20_Unmounted,
        _F12x15_Unmounted,
        _F18x24_12x18,
        _C16x32_nowrap,
        _FineArt_10x14,
        _FineArt_10x12,
        _BoxFrame_14x22_8x16,
        _BoxFrame_24x30_Unmounted,
        _BoxFrame_12x16_Unmounted,
        _ARQMediciFrame_12x12_6x6,
        _C20x24_1_25_US,
        _BoxFrame_8x16_Unmounted,
        _GoldBeadFrame_15x17_9x12,
        _ARQMediciFrame_12x14_6x8,
        _C6x12_nowrap,
        _C24x36_1_25_US,
        _F18x22_12x16,
        _GoldBeadFrame_14x16_8x10,
        _ARQMediciFrame_16x32_Unmounted,
        _F9x12_Unmounted,
        _FineArt_28x52,
        _GoldBeadFrame_22x38_16x32,
        _BoxFrame_12x36_Unmounted, _F6x6_Unmounted, _BoxFrame_24x48_Unmounted, _GoldBeadFrame_14x22_8x16, _C8x10_1_25_US, _F6x12_Unmounted, _GoldBeadFrame_6x8_Unmounted, _BoxFrame_8x10_Unmounted, _GoldBeadFrame_12x24_Unmounted, _BoxFrame_12x18_Unmounted, _GoldBeadFrame_18x22_12x16, _F12x18_Unmounted, _GoldBeadFrame_18x42_12x36, _ARQMediciFrame_12x36_Unmounted, _ARQMediciFrame_12x16_Unmounted, _F30x45_Unmounted, _C16x16_1_25_au, _FineArt_36x44, _F36x48_Unmounted, _F12x15_6x9, _C16x20_1_25_au, _ARQMediciFrame_26x31_20x25, _C24x48_nowrap, _ARQMediciFrame_12x24_6x18, _GoldBeadFrame_20x25_Unmounted, _F22x26_16x20, _C12x12_1_25_US, _BoxFrame_16x20_Unmounted, _ARQMediciFrame_20x20_Unmounted, _ARQMediciFrame_22x30_16x24, _GoldBeadFrame_20x20_Unmounted, _BoxFrame_18x30_12x24, _GoldBeadFrame_12x14_6x8, _ARQMediciFrame_18x24_12x18, _BoxFrame_24x32_Unmounted, _ARQMediciFrame_30x38_24x32, _GoldBeadFrame_30x42_24x36, _C11x14_1_25_US, _ARQMediciFrame_18x42_12x36, _F14x22_8x16, _GoldBeadFrame_8x12_Unmounted, _C8x16_nowrap, _C30x30_1_25_US, _ARQMediciFrame_14x48_Unmounted, _ARQMediciFrame_24x36_Unmounted, _ARQMediciFrame_32x40_Unmounted, _GoldBeadFrame_20x20_14x14, _F12x36_Unmounted, _F12x16_Unmounted, _BoxFrame_30x38_24x32, _C16x20_1_25_US, _C36x48_nowrap, _BoxFrame_6x12_Unmounted, _C24x30_1_25_US, _F32x40_Unmounted, _C14x48_nowrap, _GoldBeadFrame_14x48_Unmounted, _F30x42_24x36, _GoldBeadFrame_24x32_Unmounted, _C10x20_1_25_US, _GoldBeadFrame_36x48_Unmounted, _BoxFrame_9x12_Unmounted, _GoldBeadFrame_9x12_Unmounted, _C12x36_nowrap, _F20x25_Unmounted, _C6x8_nowrap, _ARQMediciFrame_26x26_20x20, _C20x30_1_25_US, _C24x32_nowrap, _C36x36_1_25_US, _C16x24_1_25_US, _ARQMediciFrame_22x26_16x20, _ARQMediciFrame_24x32_Unmounted, _C5x7_1_25_US, _BoxFrame_22x26_16x20, _ARQMediciFrame_6x6_Unmounted, _ARQMediciFrame_6x9_Unmounted, _ARQMediciFrame_20x20_14x14, _F14x48_Unmounted, _ARQMediciFrame_36x48_Unmounted, _ARQMediciFrame_6x18_Unmounted, _C32x40_nowrap, _GoldBeadFrame_22x30_16x24, _BoxFrame_30x36_24x30, _C9x12_nowrap, _F16x24_Unmounted, _BoxFrame_32x40_Unmounted, _BoxFrame_6x8_Unmounted, _BoxFrame_18x21_12x15, _BoxFrame_6x6_Unmounted, _ARQMediciFrame_9x12_Unmounted, _F14x18_8x12, _ARQMediciFrame_12x18_Unmounted, _FineArt_20x24, _BoxFrame_6x18_Unmounted, _F20x20_14x14, _ARQMediciFrame_14x18_8x12, _C12x12_1_25_au, _F18x42_12x36, _GoldBeadFrame_14x14_Unmounted, _ARQMediciFrame_18x30_12x24, _F6x9_Unmounted, _F12x14_6x8, _BoxFrame_18x24_12x18, _ARQMediciFrame_14x22_8x16, _BoxFrame_12x14_6x8, _GoldBeadFrame_6x9_Unmounted, _F24x32_Unmounted, _FineArt_16x40, _GoldBeadFrame_14x18_8x12, _C6x6_nowrap, _F8x16_Unmounted, _C12x36_1_25_US, _F26x26_20x20, _F22x30_16x24, _C30x45_nowrap, _F6x18_Unmounted, _F12x12_6x6, _GoldBeadFrame_12x12_6x6, _F16x32_Unmounted, _GoldBeadFrame_12x16_Unmounted, _F12x24_6x18, _GoldBeadFrame_12x36_Unmounted, _FineArt_8x11, _GoldBeadFrame_24x48_Unmounted, _BoxFrame_20x25_Unmounted, _GoldBeadFrame_30x38_24x32, _FineArt_8x14, _BoxFrame_12x24_6x18, _BoxFrame_16x32_Unmounted, _C20x20_1_25_US, _BoxFrame_22x30_16x24, _GoldBeadFrame_12x15_Unmounted, _C12x24_1_25_au, _F18x21_12x15, _C20x30_1_25_au, _BoxFrame_20x20_14x14, _F14x16_8x10, _ARQMediciFrame_30x36_24x30, _BoxFrame_12x18_6x12, _FineArt_40x52, _FineArt_8x20, _C24x24_1_25_au, _ARQMediciFrame_24x30_Unmounted, _ARQMediciFrame_8x16_Unmounted, _F30x38_24x32, _C6x6_1_25_US, _C12x16_1_25_US, _BoxFrame_16x24_Unmounted, _ARQMediciFrame_18x22_12x16, _ARQMediciFrame_8x10_Unmounted, _C18x24_1_25_US, _GoldBeadFrame_26x31_20x25, _F30x36_24x30, _FineArt_20x36, _GoldBeadFrame_24x30_Unmounted, _GoldBeadFrame_12x18_Unmounted, _C30x30_1_25_au, _C24x36_nowrap, _ARQMediciFrame_30x42_24x36, _BoxFrame_6x9_Unmounted, _ARQMediciFrame_12x24_Unmounted, _F6x8_Unmounted, _ARQMediciFrame_18x21_12x15, _C20x25_nowrap, _GoldBeadFrame_8x10_Unmounted, _C32x48_1_25_US, _ARQMediciFrame_6x12_Unmounted, _BoxFrame_18x42_12x36, _GoldBeadFrame_22x26_16x20, _ARQMediciFrame_24x48_Unmounted, _C12x15_nowrap, _C10x10_1_25_US, _C24x24_1_25_US, _F16x20_Unmounted, _F24x30_Unmounted, _GoldBeadFrame_30x36_24x30, _BoxFrame_36x48_Unmounted, _BoxFrame_20x20_Unmounted, _ARQMediciFrame_12x18_6x12, _F12x24_Unmounted, _ARQMediciFrame_8x12_Unmounted, _FineArt_14x26, _FineArt_18x52, _F14x14_Unmounted, _F8x12_Unmounted, _BoxFrame_30x42_24x36, _BoxFrame_12x15_6x9, _GoldBeadFrame_8x16_Unmounted, _ARQMediciFrame_6x8_Unmounted, _C6x9_nowrap, _C12x18_1_25_au, _BoxFrame_18x22_12x16, _FineArt_14x20, _ARQMediciFrame_30x45_Unmounted, _F15x17_9x12, _C8x8_1_25_US, _FineArt_14x17, _FineArt_34x49, _GoldBeadFrame_30x45_Unmounted, _F26x31_20x25, _BoxFrame_14x16_8x10, _F12x18_6x12, _GoldBeadFrame_6x6_Unmounted, _GoldBeadFrame_18x30_12x24, _GoldBeadFrame_12x15_6x9, _BoxFrame_15x17_9x12, _C30x40_1_25_US, _F22x38_16x32, _BoxFrame_30x45_Unmounted, _GoldBeadFrame_6x12_Unmounted, _ARQMediciFrame_16x24_Unmounted, _FineArt_8x8, _F24x48_Unmounted, _BoxFrame_12x12_6x6, _GoldBeadFrame_6x18_Unmounted, _BoxFrame_14x18_8x12, _BoxFrame_26x26_20x20, _ARQMediciFrame_12x15_6x9, _GoldBeadFrame_24x36_Unmounted, _ARQMediciFrame_14x14_Unmounted, _FineArt_A3, _BoxFrame_8x12_Unmounted, _C14x14_nowrap, _BoxFrame_12x15_Unmounted, _ARQMediciFrame_12x15_Unmounted;


        @Override
        public String toString() {
            return super.toString().replaceFirst("_", "");
        }

        public static Type fromName(String name) {
            return valueOf("_" + name.replace(".", "_"));
        }
    }

    public enum Status {
        AwaitingUrlOrData,
        NotYetDownloaded,
        Ok,
        FileNotFoundAtUrl,
        Invalid;
    }

    public enum Sizing {
        Crop,
        ShrinkToFit,
        ShrinkToExactFit;
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
        return "Photo [id="
                + id
                + ", type="
                + type
                + ", url="
                + url
                + ", status="
                + status
                + ", copies="
                + copies
                + ", sizing="
                + sizing
                + ", orderId="
                + orderId
                + "]";
    }
}
