package com.zebra.jamesswinton.anchorbarcodesample.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.zebra.jamesswinton.anchorbarcodesample.data.Field;
import com.zebra.jamesswinton.anchorbarcodesample.utils.IntentKeys;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ProcessDataWedgeOutputAsync extends AsyncTask<Void, Void, Void> {

    // Variables
    private final WeakReference<Context> mCx;
    private final Bundle mData;
    private final OnDataWedgeDataProcessedListener mOnDataWedgeDataProcessedListener;

    // Main Thread Handler
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public ProcessDataWedgeOutputAsync(Context cx, Bundle data, OnDataWedgeDataProcessedListener
            onDataWedgeDataProcessedListener) {
        super();
        this.mCx = new WeakReference<>(cx);
        this.mData = data;
        this.mOnDataWedgeDataProcessedListener = onDataWedgeDataProcessedListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Extract Data from Bundle
        String decodedMode = mData.getString(IntentKeys.DATA_WEDGE_EXTRA_DECODE_MODE);

        // Handle Decode Modes
        switch(decodedMode) {
            case IntentKeys.SINGLE_DECODE_MODE:
                handleSingleDecode();
                break;
            case IntentKeys.MULTIPLE_DECODE_MODE:
                handleMultipleDecode();
                break;
        }

        return null;
    }

    /**
     * Utils
     */

    private void handleSingleDecode() {
        // Get Decode Data URI
        String decodeDataUri = mData.getString(IntentKeys.DECODE_DATA_EXTRA);

        // Check if URI exists
        if(decodeDataUri != null) {
            // Init Cursor to Query URI
            Cursor cursor = mCx.get().getContentResolver().query(Uri.parse(decodeDataUri), null,
                            null, null);

            // Validate Cursor
            if (cursor != null) {
                // Move Cursor to first item
                cursor.moveToFirst();

                // Extract Data
                String id = cursor.getString(
                        cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SINGLE_BARCODE_ID));
                String labelType = cursor.getString(
                        cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SINGLE_BARCODE_LABEL_TYPE));
                String dataString = cursor.getString(
                        cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SINGLE_BARCODE_DATA_STRING));
                byte[] decodeData = cursor.getBlob(
                        cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SINGLE_BARCODE_DECODE_DATA));
                String nextDataUri = cursor.getString(
                        cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SINGLE_BARCODE_NEXT_DATA_URI));
                int fullDataSize = cursor.getInt(
                        cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SINGLE_BARCODE_FULL_DATA_SIZE));
                int dataBufferSize = cursor.getInt(
                        cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SINGLE_BARCODE_DATA_BUFFER_SIZE));

                // Return String
                mHandler.post(() -> mOnDataWedgeDataProcessedListener.onSingleBarcodeProcessed(id,
                        labelType, dataString, decodeData, nextDataUri, fullDataSize,
                        dataBufferSize));

                // Close cursor
                cursor.close();
            } else {
                // Return Cursor error
                mHandler.post(() -> mOnDataWedgeDataProcessedListener
                        .onError(new Exception("Could not init cursor")));
            }
        } else {
            handleEmptyCursor(IntentKeys.SINGLE_DECODE_MODE);
        }
    }

    private void handleMultipleDecode() {
        // Init Holder Array
        ArrayList<Field> decodedFields = new ArrayList<>();

        // Get Barcodes Bundle from Intent
        ArrayList<Bundle> barcodesBundles = mData.getParcelableArrayList(IntentKeys.DATA_TAG);

        // Loop Barcodes
        for (Bundle barcodeBundle : barcodesBundles) {
            // Grab URI from bundle
            String decodeDataUri = barcodeBundle.getString(IntentKeys.FIELD_DATA_URI);

            // Check if URI exists
            if (decodeDataUri != null) {
                // Create & verify Cursor
                Cursor cursor = mCx.get().getContentResolver().query(Uri.parse(decodeDataUri),
                        null, null, null);
                if (cursor != null) {
                    // Grab first item
                    cursor.moveToFirst();

                    // Extract Data
                    String fieldId = cursor.getString(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_FIELD_ID));
                    String fieldType = cursor.getString(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_TYPE));
                    String fieldLabelType = cursor.getString(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_LABEL_TYPE));
                    String fieldStringData = cursor.getString(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_STRING_DATA));
                    byte[] fieldRawData = cursor.getBlob(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_RAW_DATA));
                    int fieldImageWidth = cursor.getInt(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_IMAGE_WIDTH));
                    int fieldImageHeight = cursor.getInt(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_IMAGE_HEIGHT));
                    int fieldSignatureStatus = cursor.getInt(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_SIGNATURE_STATUS));
                    int fullDataSize = cursor.getInt(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_FULL_DATA_SIZE));
                    int dataBufferSize = cursor.getInt(
                            cursor.getColumnIndex(IntentKeys.CURSOR_COLUMN_SIMULSCAN_DATA_BUFFER_SIZE));

                    Field field;
                    if (fieldLabelType.equals(IntentKeys.LABEL_TYPE_SIGNATURE)) {
                        // Convert Raw Data to Bitmap
                        Bitmap fieldBitmap = convertBinaryDataToBitmap(fieldRawData,
                                fieldImageWidth, fieldImageHeight);

                        // Create Field
                        field = new Field(fieldId, fieldType, fieldLabelType, fieldStringData,
                                fieldImageWidth, fieldImageHeight, fieldSignatureStatus, fullDataSize,
                                dataBufferSize, fieldRawData, fieldBitmap);
                    } else {
                        // Create Field
                        field = new Field(fieldId, fieldType, fieldLabelType, fieldStringData,
                                fieldImageWidth, fieldImageHeight, fieldSignatureStatus, fullDataSize,
                                dataBufferSize, fieldRawData, null);
                    }

                    // Add Field to Array
                    decodedFields.add(field);

                    // Close cursor
                    cursor.close();
                } else {
                    // Return Cursor error
                    mHandler.post(() -> mOnDataWedgeDataProcessedListener
                            .onError(new Exception("Could not init cursor")));
                }
            } else {
                handleEmptyCursor(IntentKeys.MULTIPLE_DECODE_MODE);
            }
        }

        // return Field array
        mHandler.post(() -> mOnDataWedgeDataProcessedListener.onMultiBarcodeProcessed(decodedFields));
    }

    private void handleEmptyCursor(String decodeMode) {
        // Extract Data
        String dataString = mData.getString(IntentKeys.DATA_WEDGE_EXTRA_DATA_STRING);
        String labelType = mData.getString(IntentKeys.DATA_WEDGE_EXTRA_LABEL_TYPE);

        // Return String
        mHandler.post(() -> mOnDataWedgeDataProcessedListener.onCursorNull(
                labelType, dataString, decodeMode));
    }

    private Bitmap convertBinaryDataToBitmap(@NonNull byte[] binaryData, int imgWidth,
                                             int imgHeight) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(binaryData, ImageFormat.NV21, imgWidth, imgHeight,
                null);
        yuvImage.compressToJpeg(new Rect(0, 0, imgWidth, imgHeight), 50, out);
        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    /**
     * Callback Interface
     */

    public interface OnDataWedgeDataProcessedListener {
        // Use data in intent, cursor URI is null
        void onCursorNull(String labelType, String barcodeData, String decodeMode);

        // Cursor available, but only Single Barcode mode
        void onSingleBarcodeProcessed(String id, String labelType, String dataString,
                                      byte[] decodeData, String nextDataUri, int fullDataSize,
                                      int dataBufferSize);

        // Cursor available, barcode decoded
        void onMultiBarcodeProcessed(ArrayList<Field> decodedFields);

        // An error occurred
        void onError(Exception e);
    }
}
