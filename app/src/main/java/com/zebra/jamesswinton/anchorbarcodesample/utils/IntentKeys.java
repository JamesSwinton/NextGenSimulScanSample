package com.zebra.jamesswinton.anchorbarcodesample.utils;

public class IntentKeys {

    // DataWedge Intent Extras
    public static final String DATA_WEDGE_EXTRA_DECODE_MODE = "com.symbol.datawedge.decoded_mode";
    public static final String DATA_WEDGE_EXTRA_DATA_STRING = "com.symbol.datawedge.data_string";
    public static final String DATA_WEDGE_EXTRA_LABEL_TYPE = "com.symbol.datawedge.label_type";
    public static final String DATA_WEDGE_EXTRA_RAW_DECODE_DATA = "com.symbol.datawedge.decode_data";

    // Decode Mode Types from DW Intent
    public static final String SINGLE_DECODE_MODE = "single_decode";
    public static final String MULTIPLE_DECODE_MODE = "multiple_decode";

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



    public static final String DATA_NEXT_URI = "next_data_uri";
    public static final String FULL_DATA_SIZE = "full_data_size";
    public static final String RAW_DATA_SIZE = "data_buffer_size";
    public static final String RAW_DATA_INDEX = "data_index";
    public static final String PROFILE_NAME = "AnchorBarcode_Profile";
    public static final String DATAWEDGE_API_ACTION = "com.symbol.datawedge.api.ACTION";
    public static final String DECODE_DATA_EXTRA = "com.symbol.datawedge.decode_data";
    public static final String RESULT_ACTION = "com.symbol.datawedge.api.RESULT_ACTION";
    public static final String NOTIFICATION_ACTION = "com.symbol.datawedge.api.NOTIFICATION_ACTION";
    public static final String INTENT_OUTPUT_ACTION = "com.symbol.genericdata.INTENT_OUTPUT";
    public static final String NOTIFICATION_TYPE_SCANNER_STATUS = "SCANNER_STATUS";
    public static final String NOTIFICATION_TYPE_PROFILE_SWITCH = "PROFILE_SWITCH";
    public static final String NOTIFICATION_TYPE_CONFIGURATION_UPDATE = "CONFIGURATION_UPDATE";
    public static final String COMMAND_IDENTIFIER_CREATE_PROFILE = "CREATE_PROFILE";
    public static final String COMMAND_IDENTIFIER_EXTRA = "COMMAND_IDENTIFIER";
    public static final String LABEL_TYPE_SIGNATURE = "LABEL-TYPE-SIGNATURE";
    public static final String SOURCE = "com.symbol.datawedge.source";
    public static final String DATA_DISPATCH_TIME = "com.symbol.datawedge.data_dispatch_time";
    public static final String FIELD_DATA_URI = "com.symbol.datawedge.field_data_uri";
    public static final String DATA_TAG = "com.symbol.datawedge.data";
    public static final String LABEL_TYPE = "label_type";
    public static final String FIELD_LABEL_TYPE = "field_label_type";
    public static final String DATA_STRING = "field_string_data";
    public static final String DECODE_DATA = "field_raw_data";
    public static final String IMAGE_WIDTH_TAG = "field_image_width";
    public static final String IMAGE_HEIGHT_TAG = "field_image_height";
    public static final String SWITCH_SCANNER_PARAMS_KEY = "com.symbol.datawedge.api.SWITCH_SCANNER_PARAMS";
    public static final int SINGLEBARCODE_SCANNING_MODE = 1;
    public static final int NG_SS_SCANNING_MODE = 5;

    public static final String STRING_DATA_KEY_SINGLE_BARCODE = "data_string";
    public static final String INTENT_RESULT_CODE_FAILURE = "FAILURE";

    // Intent Actions array to simplify registering broadcast receivers
    public static final String[] DW_INTENT_ACTIONS = {
            NOTIFICATION_ACTION, INTENT_OUTPUT_ACTION, RESULT_ACTION
    };

    // Notification Actions array to simplify registering broadcast receivers
    public static final String[] DW_NOTIFICATION_ACTIONS = {
            IntentKeys.NOTIFICATION_TYPE_PROFILE_SWITCH,
            IntentKeys.NOTIFICATION_TYPE_SCANNER_STATUS
    };
}
