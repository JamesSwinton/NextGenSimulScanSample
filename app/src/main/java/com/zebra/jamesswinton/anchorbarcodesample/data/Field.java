package com.zebra.jamesswinton.anchorbarcodesample.data;

import android.graphics.Bitmap;

public class Field {

    private String id, type, labelType, stringData;
    private int imageWidth, imageHeight, signatureStatus, fullDataSize, dataBufferSize;
    private byte[] rawData;
    private Bitmap bitmap;

    public Field(String id, String type, String labelType, String stringData, int imageWidth,
                 int imageHeight, int signatureStatus, int fullDataSize, int dataBufferSize,
                 byte[] rawData, Bitmap bitmap) {
        this.id = id;
        this.type = type;
        this.labelType = labelType;
        this.stringData = stringData;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.signatureStatus = signatureStatus;
        this.fullDataSize = fullDataSize;
        this.dataBufferSize = dataBufferSize;
        this.rawData = rawData;
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getSignatureStatus() {
        return signatureStatus;
    }

    public void setSignatureStatus(int signatureStatus) {
        this.signatureStatus = signatureStatus;
    }

    public int getFullDataSize() {
        return fullDataSize;
    }

    public void setFullDataSize(int fullDataSize) {
        this.fullDataSize = fullDataSize;
    }

    public int getDataBufferSize() {
        return dataBufferSize;
    }

    public void setDataBufferSize(int dataBufferSize) {
        this.dataBufferSize = dataBufferSize;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}