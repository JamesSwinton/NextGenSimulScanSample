package com.zebra.jamesswinton.anchorbarcodesample.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    // Signature Status
    public static final int SIGNATURE_PRESENT = 1;
    public static final int SIGNATURE_NOT_PRESENT = 0;
    public static final int SIGNATURE_CHECK_NOT_REQUESTED = -1;
    public static final int SIGNATURE_CHECK_NOT_SUPPORTED = -2;

    // Constants
    public static final Map<String, String> TemplateFileNameAndPaths = new HashMap<String, String>() {{
        put("/sdcard/Download/WholePage-MandatoryBarcode.xml", "WholePage-MandatoryBarcode.xml");
        put("/sdcard/Download/WholePage-OptionalBarcode.xml", "WholePage-OptionalBarcode.xml");
    }};

}
