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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zebra.jamesswinton.anchorbarcodesample.data.Field;
import com.zebra.jamesswinton.anchorbarcodesample.databinding.ActivityMainMaterialBinding;
import com.zebra.jamesswinton.anchorbarcodesample.enums.ScanMode;
import com.zebra.jamesswinton.anchorbarcodesample.utils.Constants;
import com.zebra.jamesswinton.anchorbarcodesample.utils.CustomDialog;
import com.zebra.jamesswinton.anchorbarcodesample.utils.DataWedgeUtils;
import com.zebra.jamesswinton.anchorbarcodesample.utils.ProcessDataWedgeOutputAsync;

import java.util.ArrayList;

import static com.zebra.jamesswinton.anchorbarcodesample.utils.DataWedgeUtils.convertSignatureStatusToString;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        ProcessDataWedgeOutputAsync.OnDataWedgeDataProcessedListener,
        CompoundButton.OnCheckedChangeListener {

    // UI DataBinding (allows access to views without multiple findViewById calls)
    private ActivityMainMaterialBinding mDataBinding;

    // Config Holders
    private boolean mIlluminate;
    private String mScanner;
    private String mTemplate;
    private ScanMode mScanMode;

    // Spinner Listener holder (When setting the listener on the spinner it triggers the callback,
    // so we need a flag to indicate first-run)
    private int mTotalTimesCallbackTriggered = 0;
    private final int mTotalSpinnerListeners = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_material);

        // Init UI (Click listeners, Spinner Change Listeners etc...)
        initUIComponents();

        // Set Defaults (First item in String Array Resource is considered default)
        mIlluminate = false;
        mScanner = getResources().getStringArray(R.array.scanner_identifier)[0];
        mTemplate = getResources().getStringArray(R.array.templates)[0];
        mScanMode = getResources().getStringArray(R.array.scanning_modes)[0]
                .equals("Single") ? ScanMode.Single : ScanMode.SimulScan;

        // Create DW Profile
        DataWedgeUtils.createProfile(this);
        DataWedgeUtils.updateProfile(this, mIlluminate, mScanner, mScanMode, mTemplate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register Broadcast Receivers
        DataWedgeUtils.registerDataWedgeBroadcastReceiver(this, Constants.DataWedgeConstants.DW_INTENT_ACTIONS,
                DataWedgeBroadcastReceiver);

        // Register for Notifications from DataWedge
        DataWedgeUtils.registerForNotificationsFromDataWedge(this,
                Constants.DataWedgeConstants.DW_NOTIFICATION_ACTIONS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister Broadcast Receivers
        unregisterReceiver(DataWedgeBroadcastReceiver);

        // Unregister from Notifications
        DataWedgeUtils.unregisterForNotifications(this, Constants.DataWedgeConstants.DW_NOTIFICATION_ACTIONS);
    }

    /**
     * DW Broadcast Receiver
     */

    private final BroadcastReceiver DataWedgeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.DataWedgeConstants.DATA_WEDGE_INTENT_OUTPUT_ACTION:
                    Bundle data = intent.getExtras();
                    if (data != null) {
                        new ProcessDataWedgeOutputAsync(MainActivity.this, data,
                                MainActivity.this).execute();
                    }
                    break;
                case Constants.DataWedgeConstants.DATA_WEDGE_RESULT_ACTION:
                    // Parse result from DW
                    String result = DataWedgeUtils.handleResultAction(intent);

                    // If result exists, there was an error. Display error in dialog.
                    if (result != null) {
                        CustomDialog.showCustomDialog(MainActivity.this,
                                CustomDialog.DialogType.ERROR, "Error!", result);
                    }
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
            if (decodedField.getLabelType().equals(Constants.DataWedgeConstants.DATA_WEDGE_EXTRA_LABEL_TYPE_SIGNATURE)) {
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

    /**
     * UI
     */

    private void clearDisplay() {
        mDataBinding.dataLayout.docLayout.removeAllViews();
        mDataBinding.dataLayout.scanDataLayout.removeAllViews();
    }

    private void initUIComponents() {
        mDataBinding.settingsLayout.flashToggle.setOnCheckedChangeListener(this);
        mDataBinding.settingsLayout.scanModeSpinner.setOnItemSelectedListener(this);
        mDataBinding.settingsLayout.templateSpinner.setOnItemSelectedListener(this);
        mDataBinding.settingsLayout.scannerSpinner.setOnItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // This callback is triggered as soon as the interface is set on the View, so lets ignore
        // the first run of each spinner
        if (mTotalSpinnerListeners > mTotalTimesCallbackTriggered++) {
            return;
        }

        // Handle Event
        switch (parent.getId()) {
            case R.id.scanner_spinner: {
                // Update Template Variable
                mScanner = parent.getItemAtPosition(position).toString();

                // Update Profile to reflect change
                DataWedgeUtils.updateProfile(this, mIlluminate, mScanner, mScanMode, mTemplate);
                break;
            }
            case R.id.scan_mode_spinner: {
                // Update Scan Mode Variable
                mScanMode = parent.getItemAtPosition(position).toString()
                        .equals(ScanMode.Single.name()) ? ScanMode.Single : ScanMode.SimulScan;

                // Update Profile to reflect change
                DataWedgeUtils.updateProfile(this, mIlluminate, mScanner, mScanMode, mTemplate);

                // Enable / Disable template selection
                mDataBinding.settingsLayout.templateLayout.setVisibility(
                        mScanMode == ScanMode.SimulScan ? View.VISIBLE : View.GONE);
                break;
            }
            case R.id.template_spinner: {
                // Update Template Variable
                mTemplate = parent.getItemAtPosition(position).toString();

                // Update Profile to reflect change
                DataWedgeUtils.updateProfile(this, mIlluminate, mScanner, mScanMode, mTemplate);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Unsupported
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.flash_toggle: {
                // Update Template Variable
                mIlluminate = isChecked;

                // Update Profile to reflect change
                DataWedgeUtils.updateProfile(this, mIlluminate, mScanner, mScanMode, mTemplate);
                break;
            }
        }
    }
}