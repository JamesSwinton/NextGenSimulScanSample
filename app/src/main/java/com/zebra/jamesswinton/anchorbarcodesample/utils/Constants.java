package com.zebra.jamesswinton.anchorbarcodesample.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    // Signature Status
    public static final int SIGNATURE_PRESENT = 1;
    public static final int SIGNATURE_NOT_PRESENT = 0;
    public static final int SIGNATURE_CHECK_NOT_REQUESTED = -1;
    public static final int SIGNATURE_CHECK_NOT_SUPPORTED = -2;

    // Template Locations
    public static final Map<String, String> TemplateFileNameAndPaths = new HashMap<String, String>() {{
        put("/sdcard/Download/WholePage-MandatoryBarcode.xml", "WholePage-MandatoryBarcode.xml");
        put("/sdcard/Download/WholePage-OptionalBarcode.xml", "WholePage-OptionalBarcode.xml");
    }};

    public static class DataWedgeConstants {

        // DataWedge Profile Settings
        public static final String DATA_WEDGE_PROFILE_NAME = "AnchorBarcode_Profile";

        // DataWedge Actions
        public static final String DATA_WEDGE_API_ACTION = "com.symbol.datawedge.api.ACTION";
        public static final String DATA_WEDGE_RESULT_ACTION = "com.symbol.datawedge.api.RESULT_ACTION";
        public static final String DATA_WEDGE_NOTIFICATION_ACTION = "com.symbol.datawedge.api.NOTIFICATION_ACTION";
        public static final String DATA_WEDGE_INTENT_OUTPUT_ACTION = "com.symbol.genericdata.INTENT_OUTPUT";

        // DataWedge Notification Types
        public static final String NOTIFICATION_TYPE_SCANNER_STATUS = "SCANNER_STATUS";
        public static final String NOTIFICATION_TYPE_PROFILE_SWITCH = "PROFILE_SWITCH";

        // DataWedge Command Identifiers
        public static final String DATA_WEDGE_COMMAND_IDENTIFIER_KEY = "COMMAND_IDENTIFIER";
        public static final String DATA_WEDGE_COMMAND_IDENTIFIER_CREATE_PROFILE = "CREATE_PROFILE";

        // DataWedge Intent Extras
        public static final String DATA_WEDGE_EXTRA_DATA = "com.symbol.datawedge.data";
        public static final String DATA_WEDGE_EXTRA_DECODE_MODE = "com.symbol.datawedge.decoded_mode";
        public static final String DATA_WEDGE_EXTRA_DATA_STRING = "com.symbol.datawedge.data_string";
        public static final String DATA_WEDGE_EXTRA_LABEL_TYPE = "com.symbol.datawedge.label_type";
        public static final String DATA_WEDGE_EXTRA_DECODE_DATA = "com.symbol.datawedge.decode_data";
        public static final String DATA_WEDGE_EXTRA_FIELD_DATA_URI = "com.symbol.datawedge.field_data_uri";
        public static final String DATA_WEDGE_EXTRA_LABEL_TYPE_SIGNATURE = "LABEL-TYPE-SIGNATURE";

        // DataWedge Scanning Modes
        public static final int SINGLE_BARCODE_SCANNING_MODE = 1;
        public static final int NGSS_SCANNING_MODE = 5;

        // DataWedge Result Codes
        public static final String INTENT_RESULT_CODE_FAILURE = "FAILURE";

        // Decode Mode Types from DW Intent
        public static final String DATA_WEDGE_SINGLE_DECODE_MODE = "single_decode";
        public static final String DATA_WEDGE_MULTIPLE_DECODE_MODE = "multiple_decode";

        // SimulScan Barcode URI Columns
        public static final String CURSOR_COLUMN_SIMULSCAN_FIELD_ID = "field_id";
        public static final String CURSOR_COLUMN_SIMULSCAN_TYPE = "field_type";
        public static final String CURSOR_COLUMN_SIMULSCAN_LABEL_TYPE = "field_label_type";
        public static final String CURSOR_COLUMN_SIMULSCAN_STRING_DATA = "field_string_data";
        public static final String CURSOR_COLUMN_SIMULSCAN_RAW_DATA = "field_raw_data";
        public static final String CURSOR_COLUMN_SIMULSCAN_IMAGE_WIDTH = "field_image_width";
        public static final String CURSOR_COLUMN_SIMULSCAN_IMAGE_HEIGHT = "field_image_height";
        public static final String CURSOR_COLUMN_SIMULSCAN_SIGNATURE_STATUS = "field_signature_status";
        public static final String CURSOR_COLUMN_SIMULSCAN_NEXT_DATA_URI = "next_data_uri";
        public static final String CURSOR_COLUMN_SIMULSCAN_FULL_DATA_SIZE = "full_data_size";
        public static final String CURSOR_COLUMN_SIMULSCAN_DATA_BUFFER_SIZE = "data_buffer_size";

        // Single Barcode URI Columns
        public static final String CURSOR_COLUMN_SINGLE_BARCODE_ID = "id";
        public static final String CURSOR_COLUMN_SINGLE_BARCODE_LABEL_TYPE = "label_type";
        public static final String CURSOR_COLUMN_SINGLE_BARCODE_DATA_STRING = "data_string";
        public static final String CURSOR_COLUMN_SINGLE_BARCODE_DECODE_DATA = "decode_data";
        public static final String CURSOR_COLUMN_SINGLE_BARCODE_NEXT_DATA_URI = "next_data_uri";
        public static final String CURSOR_COLUMN_SINGLE_BARCODE_FULL_DATA_SIZE = "full_data_size";
        public static final String CURSOR_COLUMN_SINGLE_BARCODE_DATA_BUFFER_SIZE = "data_buffer_size";

        // Intent Actions array to simplify registering broadcast receivers
        public static final String[] DW_INTENT_ACTIONS = {
                DATA_WEDGE_NOTIFICATION_ACTION, DATA_WEDGE_INTENT_OUTPUT_ACTION, DATA_WEDGE_RESULT_ACTION
        };

        // Notification Actions array to simplify registering broadcast receivers
        public static final String[] DW_NOTIFICATION_ACTIONS = {
                DataWedgeConstants.NOTIFICATION_TYPE_PROFILE_SWITCH,
                DataWedgeConstants.NOTIFICATION_TYPE_SCANNER_STATUS
        };
    }
}
