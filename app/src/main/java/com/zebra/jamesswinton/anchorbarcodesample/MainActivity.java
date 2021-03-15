package com.zebra.jamesswinton.anchorbarcodesample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zebra.jamesswinton.anchorbarcodesample.data.Field;
import com.zebra.jamesswinton.anchorbarcodesample.databinding.ActivityMainMaterialBinding;
import com.zebra.jamesswinton.anchorbarcodesample.enums.ScanMode;
import com.zebra.jamesswinton.anchorbarcodesample.utils.DataWedgeUtils;
import com.zebra.jamesswinton.anchorbarcodesample.utils.IntentKeys;
import com.zebra.jamesswinton.anchorbarcodesample.utils.ProcessDataWedgeOutputAsync;

import java.util.ArrayList;

import static com.zebra.jamesswinton.anchorbarcodesample.utils.DataWedgeUtils.convertSignatureStatusToString;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        ProcessDataWedgeOutputAsync.OnDataWedgeDataProcessedListener {

    // UI DataBinding (allows access to views without multiple findViewById calls)
    private ActivityMainMaterialBinding mDataBinding;

    // Config Holders
    private String mScanner;
    private String mTemplate;
    private ScanMode mScanMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_material);

        // Init UI (Click listeners, Spinner Change Listeners etc...)
        initUIComponents();

        // Set Defaults
        mScanner = getResources().getStringArray(R.array.scanner_identifier)[0];
        mTemplate = getResources().getStringArray(R.array.templates)[0];
        mScanMode = getResources().getStringArray(R.array.scanning_modes)[0]
                .equals("Single") ? ScanMode.Single : ScanMode.SimulScan;

        // Create DW Profile
        DataWedgeUtils.createProfile(this, mScanner, mScanMode, mTemplate,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register Broadcast Receivers
        DataWedgeUtils.registerDataWedgeBroadcastReceiver(this, IntentKeys.DW_INTENT_ACTIONS,
                DataWedgeBroadcastReceiver);

        // Register for Notifications from DataWedge
        DataWedgeUtils.registerForNotificationsFromDataWedge(this,
                IntentKeys.DW_NOTIFICATION_ACTIONS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister Broadcast Receivers
        unregisterReceiver(DataWedgeBroadcastReceiver);

        // Unregister from Notifications
        DataWedgeUtils.unregisterForNotifications(this, IntentKeys.DW_NOTIFICATION_ACTIONS);
    }

    /**
     * DW Broadcast Receiver
     */

    private BroadcastReceiver DataWedgeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case IntentKeys.NOTIFICATION_ACTION:
                    // TODO
                    break;
                case IntentKeys.INTENT_OUTPUT_ACTION:
                    Bundle data = intent.getExtras();
                    if (data != null) {
                        new ProcessDataWedgeOutputAsync(MainActivity.this, data,
                                MainActivity.this).execute();
                    }
                    break;
                case IntentKeys.RESULT_ACTION:
                    // TODO
                    break;
            }
        }
    };

    /**
     * DW Intent Output Results
     */

    @Override
    public void onCursorNull(String labelType, String barcodeData, String decodeMode) {
        // Clear Display
        clearDisplay();

        // Process Decode field
        TextView textView = new TextView(this);
        textView.setText(barcodeData);
        mDataBinding.dataLayout.scanDataLayout.addView(textView);
    }

    @Override
    public void onSingleBarcodeProcessed(String id, String labelType, String dataString,
                                         byte[] decodeData, String nextDataUri, int fullDataSize,
                                         int dataBufferSize) {
        // Clear Display
        clearDisplay();

        // Process Decode field
        TextView textView = new TextView(this);
        textView.setText(dataString);
        mDataBinding.dataLayout.scanDataLayout.addView(textView);
    }

    @Override
    public void onMultiBarcodeProcessed(ArrayList<Field> decodedFields) {
        // Clear Display
        clearDisplay();

        // Loop decoded fields & display
        for (Field decodedField : decodedFields) {
            if (decodedField.getLabelType().equals(IntentKeys.LABEL_TYPE_SIGNATURE)) {
                // Display Signature Status
                TextView textView = new TextView(MainActivity.this);
                textView.setText("Signature Status: " +
                        convertSignatureStatusToString(decodedField.getSignatureStatus())
                        + "(" + decodedField.getSignatureStatus() + ")");
                mDataBinding.dataLayout.scanDataLayout.addView(textView);

                // Process Signature Field
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageBitmap(decodedField.getBitmap());
                mDataBinding.dataLayout.docLayout.addView(imageView);
            } else {
                // Process Decode field
                TextView textView = new TextView(MainActivity.this);
                textView.setText(decodedField.getStringData());
                mDataBinding.dataLayout.scanDataLayout.addView(textView);
            }
        }
    }

    @Override
    public void onError(Exception e) {
        Log.i(this.getClass().getName(), "Err: " + e.getMessage());
    }

    private void clearDisplay() {
       mDataBinding.dataLayout.docLayout.removeAllViews();
       mDataBinding.dataLayout.scanDataLayout.removeAllViews();
    }

    /**
     * UI
     */

    private void initUIComponents() {
        mDataBinding.settingsLayout.scanModeSpinner.setOnItemSelectedListener(this);
        mDataBinding.settingsLayout.templateSpinner.setOnItemSelectedListener(this);

        // disable Scanner selection - TODO: Remove when feature is released
        // mDataBinding.settingsLayout.scannerSpinner.setEnabled(false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.scanner_spinner: {
                // Update Template Variable
                mScanner = parent.getItemAtPosition(position).toString();

                // Update Profile to reflect change
                DataWedgeUtils.createProfile(this, mScanner, mScanMode, mTemplate, true);
                break;
            }
            case R.id.scan_mode_spinner: {
                // Update Scan Mode Variable
                mScanMode = parent.getItemAtPosition(position).toString()
                        .equals(ScanMode.Single.name()) ? ScanMode.Single : ScanMode.SimulScan;

                // Update Profile to reflect change
                DataWedgeUtils.createProfile(this, mScanner, mScanMode, mTemplate, true);

                // Enable / Disable template selection
                mDataBinding.settingsLayout.templateSpinner.setEnabled(
                        mScanMode == ScanMode.SimulScan);
                break;
            }
            case R.id.template_spinner: {
                // Update Template Variable
                mTemplate = parent.getItemAtPosition(position).toString();

                // Update Profile to reflect change
                DataWedgeUtils.createProfile(this, mScanner, mScanMode, mTemplate, true);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Unsupported
    }
}