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
     * @param scanMode - Single or SimulScan
     * @param selectedTemplateName - Template to use for document capture
     * @param update - Whether to update an existing profile
     */

    public static void createProfile(Context cx, String scannerSelection, ScanMode scanMode,
                                     String selectedTemplateName, boolean update) {
        // Set Scanning mode (selected by user in dropdown)
        int ng_ss_mode = (scanMode == Single
                ? IntentKeys.SINGLEBARCODE_SCANNING_MODE
                : IntentKeys.NG_SS_SCANNING_MODE);

        // App Association Bundle
        Bundle appAssociation = new Bundle();
        appAssociation.putString("PACKAGE_NAME", cx.getPackageName());
        appAssociation.putStringArray("ACTIVITY_LIST", new String[]{ "*" });

        // Main DW Bundle (Profile name, state & update type)
        Bundle bMain = new Bundle();
        bMain.putString("PROFILE_NAME", IntentKeys.PROFILE_NAME);
        bMain.putString("PROFILE_ENABLED", "true");
        bMain.putString("CONFIG_MODE", update ? "UPDATE" : "CREATE_IF_NOT_EXIST");
        bMain.putParcelableArray("APP_LIST", new Bundle[]{ appAssociation });

        // Plugin Bundle (To hold config for plugins, e.g. barcode, intent output etc...)
        ArrayList<Bundle> bundlePluginConfig = new ArrayList<>();

        // Barcode Params Bundle -> This is where template is set
        Bundle bParamsBarcode = new Bundle();
        bParamsBarcode.putString("scanner_selection_by_identifier", scannerSelection);
        bParamsBarcode.putString("scanning_mode", String.valueOf(ng_ss_mode));
        bParamsBarcode.putString("doc_capture_template", selectedTemplateName);

        // Add Barcode Params Bundle to Config bundle
        Bundle bConfigBarcode = new Bundle();
        bConfigBarcode.putString("PLUGIN_NAME", "BARCODE");
        bConfigBarcode.putString("RESET_CONFIG", "true");
        bConfigBarcode.putBundle("PARAM_LIST", bParamsBarcode);

        // Barcode Params Bundle -> This is where the intent output configuration is set
        Bundle bParamsIntent = new Bundle();
        bParamsIntent.putString("intent_output_enabled", "true");
        bParamsIntent.putString("intent_action", IntentKeys.INTENT_OUTPUT_ACTION);
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
        iSetConfig.setAction(IntentKeys.DATAWEDGE_API_ACTION);
        iSetConfig.putExtra("com.symbol.datawedge.api.SET_CONFIG", bMain);
        iSetConfig.putExtra("SEND_RESULT", "COMPLETE_RESULT");
        iSetConfig.putExtra(IntentKeys.COMMAND_IDENTIFIER_EXTRA,
                IntentKeys.COMMAND_IDENTIFIER_CREATE_PROFILE);

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
            i.setAction(IntentKeys.DATAWEDGE_API_ACTION);
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
            i.setAction(IntentKeys.DATAWEDGE_API_ACTION);
            i.putExtra("com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION", b);
            cx.sendBroadcast(i);
        }
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
