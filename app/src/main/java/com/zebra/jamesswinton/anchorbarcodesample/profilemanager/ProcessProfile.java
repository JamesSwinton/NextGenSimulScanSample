package com.zebra.jamesswinton.anchorbarcodesample.profilemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.ProfileManager.PROFILE_FLAG;
import com.zebra.jamesswinton.anchorbarcodesample.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import static com.symbol.emdk.EMDKResults.STATUS_CODE.CHECK_XML;
import static com.symbol.emdk.EMDKResults.STATUS_CODE.FAILURE;
import static com.symbol.emdk.EMDKResults.STATUS_CODE.SUCCESS;

public class ProcessProfile extends AsyncTask<String, Void, EMDKResults> {

  // Debugging
  private static final String TAG = "ProcessProfile";

  // Non-Static Variables
  private String mProfileName;
  private ProfileManager mProfileManager;
  private OnProfileApplied mOnProfileApplied;
  private WeakReference<Context> mContextWeakRef;

  public ProcessProfile(Context context, String profileName, ProfileManager profileManager,
                        OnProfileApplied onProfileApplied) {
    this.mProfileName = profileName;
    this.mProfileManager = profileManager;
    this.mOnProfileApplied = onProfileApplied;
    this.mContextWeakRef = new WeakReference<>(context);
  }

  @Override
  protected EMDKResults doInBackground(String... params) {
    // Write DW Template to file is not already exist
    for (String templateFilePath : Constants.TemplateFileNameAndPaths.keySet()) {
      File template = new File(templateFilePath);
      if (!template.exists()) {
        try {
          OutputStream outputStream = new FileOutputStream(template);
          InputStream inputStream = mContextWeakRef.get().getAssets()
                  .open(Constants.TemplateFileNameAndPaths.get(templateFilePath));

          int n;
          byte[] buf = new byte[8192];
          while ((n = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, n);
          }

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    // Execute Profile
    return mProfileManager.processProfile(mProfileName, PROFILE_FLAG.SET, params);
  }

  @Override
  protected void onPostExecute(EMDKResults results) {
    super.onPostExecute(results);
    // Log Result
    Log.i(TAG, "Profile Manager Result: " + results.statusCode + " | " + results.extendedStatusCode);

    // Notify Class
    if (results.statusCode.equals(CHECK_XML)) {
      Log.i(TAG, "XML: " + results.getStatusString());
      mOnProfileApplied.profileApplied();
    } else if (results.statusCode.equals(FAILURE) || results.statusCode.equals(SUCCESS)) {
      mOnProfileApplied.profileError();
    }
  }

}
