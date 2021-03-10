package com.zebra.jamesswinton.anchorbarcodesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.zebra.jamesswinton.anchorbarcodesample.profilemanager.OnProfileApplied;
import com.zebra.jamesswinton.anchorbarcodesample.profilemanager.ProcessProfile;
import com.zebra.jamesswinton.anchorbarcodesample.utils.Constants;
import com.zebra.jamesswinton.anchorbarcodesample.utils.PermissionsHelper;

import java.io.File;

import static com.zebra.jamesswinton.anchorbarcodesample.App.DW_BATCH_PROFILE_NAME;
import static com.zebra.jamesswinton.anchorbarcodesample.App.DW_BATCH_PROFILE_XML;

public class SplashScreenActivity extends AppCompatActivity implements EMDKManager.EMDKListener,
        PermissionsHelper.OnPermissionsResultListener {

    // Debugging
    private static final String TAG = "MainActivity";

    // Static Variables
    private EMDKManager mEmdkManager = null;
    private ProfileManager mProfileManager = null;

    // Variables
    private PermissionsHelper mPermissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Notify Start
        Toast.makeText(this, "Installing templates, please wait..." , Toast.LENGTH_LONG).show();

        // Permissions
        mPermissionsManager = new PermissionsHelper(this, this);
        mPermissionsManager.forcePermissionsUntilGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsHelper.PERMISSIONS_REQUEST_CODE) {
            mPermissionsManager.onRequestPermissionsResult();
        }
    }

    @Override
    public void onPermissionsGranted() {
        // Check if templates exist on device (lazy assume already applied)
        boolean templateInstalled = false;
        for (String templateFilePath : Constants.TemplateFileNameAndPaths.keySet()) {
            File template = new File(templateFilePath);
            if (template.exists()) {
                templateInstalled = true;
                break;
            }
        }

        if (templateInstalled) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        } else {
            // Init EMDK
            EMDKResults emdkManagerResults = EMDKManager.getEMDKManager(this, this);

            // Verify EMDK Manager
            if (emdkManagerResults == null || emdkManagerResults.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
                // Log Error
                Log.e(TAG, "onCreate: Failed to get EMDK Manager -> " +
                        (emdkManagerResults == null ? "No Results Returned" : emdkManagerResults.statusCode));
                Toast.makeText(this, "Failed to get EMDK Manager!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release EMDK Manager Instance
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        // Assign EMDK Reference
        mEmdkManager = emdkManager;

        // Get Profile & Version Manager Instances
        mProfileManager = (ProfileManager) mEmdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);

        // Apply Profile
        if (mProfileManager != null) {
            applyCustomXml();
        } else {
            Log.e(TAG, "Error Obtaining ProfileManager!");
            Toast.makeText(this, "Error Obtaining ProfileManager!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onClosed() {
        // Release EMDK Manager Instance
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    private void applyCustomXml() {
        String[] params = new String[] { DW_BATCH_PROFILE_XML };
        new ProcessProfile(this, DW_BATCH_PROFILE_NAME, mProfileManager, new OnProfileApplied() {
            @Override
            public void profileApplied() {
                // Notify User
                Toast.makeText(SplashScreenActivity.this, "Templates installed",
                        Toast.LENGTH_SHORT).show();

                // Start Main Activity
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                // Exit
                finish();
            }

            @Override
            public void profileError() {
                // Notify User
                Log.e(TAG, "Error Processing Profile!");
                Toast.makeText(SplashScreenActivity.this, "Could not install templates!",
                        Toast.LENGTH_SHORT).show();

                // Exit App
                finish();
            }
        }).execute(params);
    }
}