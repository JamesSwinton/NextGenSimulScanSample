package com.zebra.jamesswinton.anchorbarcodesample.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.zebra.jamesswinton.anchorbarcodesample.enums.ScanMode;

import java.util.ArrayList;

import static com.zebra.jamesswinton.anchorbarcodesample.enums.ScanMode.Single;
import static com.zebra.jamesswinton.anchorbarcodesample.utils.Constants.SIGNATURE_CHECK_NOT_REQUESTED;
import static com.zebra.jamesswinton.anchorbarcodesample.utils.Constants.SIGNATURE_CHECK_NOT_SUPPORTED;
import static com.zebra.jamesswinton.anchorbarcodesample.utils.Constants.SIGNATURE_NOT_PRESENT;
import static com.zebra.jamesswinton.anchorbarcodesample.utils.Constants.SIGNATURE_PRESENT;

public class DataWedgeUtils {

    /**
     * Creates profile in DW Application - this profile will be associated with all activities in
     * this application
     * @param cx - Context
     * @param illuminate - Enable flash for camera scanner
     * @param scanMode - Single or SimulScan
     * @param selectedTemplateName - Template to use for document capture
     */

    public static void createProfile(Context cx, boolean illuminate, String scannerSelection,
                                     ScanMode scanMode, String selectedTemplateName) {
        // Set Scanning mode (selected by user in dropdown)
        int ng_ss_mode = (scanMode == Single
                ? Constants.DataWedgeConstants.SINGLE_BARCODE_SCANNING_MODE
                : Constants.DataWedgeConstants.NGSS_SCANNING_MODE);

        // App Association Bundle
        Bundle appAssociation = new Bundle();
        appAssociation.putString("PACKAGE_NAME", cx.getPackageName());
        appAssociation.putStringArray("ACTIVITY_LIST", new String[]{ "*" });

        // Main DW Bundle (Profile name, state & update type)
        Bundle bMain = new Bundle();
        bMain.putString("PROFILE_NAME", Constants.DataWedgeConstants.DATA_WEDGE_PROFILE_NAME);
        bMain.putString("PROFILE_ENABLED", "true");
        bMain.putString("CONFIG_MODE", "OVERWRITE");
        bMain.putParcelableArray("APP_LIST", new Bundle[]{ appAssociation });

        // Plugin Bundle (To hold config for plugins, e.g. barcode, intent output etc...)
        ArrayList<Bundle> bundlePluginConfig = new ArrayList<>();

        // Barcode Params Bundle -> This is where template is set
        Bundle bParamsBarcode = new Bundle();
        bParamsBarcode.putString("scanner_selection_by_identifier", scannerSelection);
        bParamsBarcode.putString("scanning_mode", String.valueOf(ng_ss_mode));
        bParamsBarcode.putString("doc_capture_template", selectedTemplateName);
        bParamsBarcode.putString("illumination_mode", illuminate ? "torch" : "off");

        // Add Barcode Params Bundle to Config bundle
        Bundle bConfigBarcode = new Bundle();
        bConfigBarcode.putString("PLUGIN_NAME", "BARCODE");
        bConfigBarcode.putString("RESET_CONFIG", "true");
        bConfigBarcode.putBundle("PARAM_LIST", bParamsBarcode);

        // Intent Params Bundle -> This is where the intent output configuration is set
        Bundle bParamsIntent = new Bundle();
        bParamsIntent.putString("intent_output_enabled", "true");
        bParamsIntent.putString("intent_action", Constants.DataWedgeConstants.DATA_WEDGE_INTENT_OUTPUT_ACTION);
        bParamsIntent.putString("intent_category", "android.intent.category.DEFAULT");
        bParamsIntent.putInt("intent_delivery", 2);
        bParamsIntent.putString("intent_use_content_provider", "true");

        // Add Intent Params Bundle to Config bundle
        Bundle bConfigIntent = new Bundle();
        bConfigIntent.putString("PLUGIN_NAME", "INTENT");
        bConfigIntent.putString("RESET_CONFIG", "true");
        bConfigIntent.putBundle("PARAM_LIST", bParamsIntent);

        // Add Plugins to ArrayList
        bundlePluginConfig.add(bConfigBarcode);
        bundlePluginConfig.add(bConfigIntent);

        // Add plugin configs to Main bundle
        bMain.putParcelableArrayList("PLUGIN_CONFIG", bundlePluginConfig);

        // Create Intent to send configuration DW
        Intent iSetConfig = new Intent();
        iSetConfig.setAction(Constants.DataWedgeConstants.DATA_WEDGE_API_ACTION);
        iSetConfig.putExtra("com.symbol.datawedge.api.SET_CONFIG", bMain);
        iSetConfig.putExtra("SEND_RESULT", "COMPLETE_RESULT");
        iSetConfig.putExtra(Constants.DataWedgeConstants.DATA_WEDGE_COMMAND_IDENTIFIER_KEY,
                Constants.DataWedgeConstants.DATA_WEDGE_COMMAND_IDENTIFIER_CREATE_PROFILE);

        // Broadcast Intent
        cx.sendBroadcast(iSetConfig);
    }

    /**
     * Registers Broadcast Receiver to receive intents from DW
     * @param actionsArray - actions to listen to from DW
     * @param broadcastReceiver - Receiver to deliver results to
     */

    public static void registerDataWedgeBroadcastReceiver(Context cx, String[] actionsArray,
                                                    BroadcastReceiver broadcastReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addCategory("android.intent.category.DEFAULT");
        for (String action : actionsArray) {
            filter.addAction(action);
        }
        cx.registerReceiver(broadcastReceiver, filter);
    }

    /**
     * Registers application to receive notifications from DW
     * @param notificationTypes - notifications to listen to from DW
     */

    public static void registerForNotificationsFromDataWedge(Context cx, String[] notificationTypes) {
        for (String notificationType : notificationTypes) {
            Bundle b = new Bundle();
            b.putString("com.symbol.datawedge.api.APPLICATION_NAME", cx.getPackageName());
            b.putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", notificationType);
            Intent i = new Intent();
            i.setAction(Constants.DataWedgeConstants.DATA_WEDGE_API_ACTION);
            i.putExtra("com.symbol.datawedge.api.REGISTER_FOR_NOTIFICATION", b);
            cx.sendBroadcast(i);
        }
    }

    /**
     * Unregisters application from receiving notifications from DW
     * @param notificationTypes - notifications to unregister from DW
     */

    public static void unregisterForNotifications(Context cx, String[] notificationTypes) {
        for (String notificationType : notificationTypes) {
            Bundle b = new Bundle();
            b.putString("com.symbol.datawedge.api.APPLICATION_NAME", cx.getPackageName());
            b.putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", notificationType);
            Intent i = new Intent();
            i.setAction(Constants.DataWedgeConstants.DATA_WEDGE_API_ACTION);
            i.putExtra("com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION", b);
            cx.sendBroadcast(i);
        }
    }

    /**
     * Parses intent returned from Datawedge to check for errors.
     * If error is found, Error String will be built & returned to calling class
     * @param intent - intent from DW
     * @return - either null for success or error string for failure
     */

    public static String handleResultAction(Intent intent) {
        // Init Holders
        boolean actionSuccessful = true;
        StringBuilder results = new StringBuilder();

        // Get Extras from Intent
        Bundle intentExtras = intent.getExtras();

        // Query extras for Command Identifier
        if (intentExtras.containsKey(Constants.DataWedgeConstants.DATA_WEDGE_COMMAND_IDENTIFIER_KEY)) {
            // Get Identifier from Extras
            String commandIdentifier = intentExtras.getString(Constants.DataWedgeConstants.DATA_WEDGE_COMMAND_IDENTIFIER_KEY);

            // Verify command identifier is ours
            if (commandIdentifier.equalsIgnoreCase(Constants.DataWedgeConstants.DATA_WEDGE_COMMAND_IDENTIFIER_CREATE_PROFILE)) {
                // Get Results
                ArrayList<Bundle> resultsList = (ArrayList<Bundle>) intentExtras.get("RESULT_LIST");

                // Verify existence of results
                if (!resultsList.isEmpty()) {

                    // Loop results & Build Results String
                    for (Bundle result : resultsList) {
                        if (result.getString("RESULT")
                                .equalsIgnoreCase(Constants.DataWedgeConstants.INTENT_RESULT_CODE_FAILURE)) {
                            // update Holder
                            actionSuccessful = false;

                            // Get Module
                            results.append("Module: ");
                            results.append(result.getString("MODULE"));
                            results.append("\n");

                            // get Result
                            results.append("Result: ");
                            results.append(result.getString("RESULT"));
                            results.append("\n");

                            // Get Result code
                            results.append("Result code: ");
                            results.append(result.getString("RESULT_CODE"));
                            results.append("\n");

                            // et sub-result code if exists
                            if (result.containsKey("SUB_RESULT_CODE"))
                                results.append("\t");
                                results.append("Sub Result code: ");
                                results.append(result.getString("SUB_RESULT_CODE"));
                                results.append("\n");;
                        }
                    }

                }
            }
        }

        // If result contains a failure, notify our calling class to display the error
        return actionSuccessful ? null : results.toString();
    }

    public static String convertSignatureStatusToString(int sigStatus) {
        switch (sigStatus) {
            case SIGNATURE_PRESENT:
                return "Signature Present";
            case SIGNATURE_NOT_PRESENT:
                return "Signature Not Present";
            case SIGNATURE_CHECK_NOT_REQUESTED:
                return "Signature Not Requested";
            case SIGNATURE_CHECK_NOT_SUPPORTED:
                return "Signature Check Not Supported";
            default:
                return "Signature Status Unknown";
        }
    }

}
