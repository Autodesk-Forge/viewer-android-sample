package com.autodesk.forge.forgeviewer_android_sample;

import com.autodesk.client.api.BucketsApi;
import com.autodesk.client.api.DerivativesApi;
import com.autodesk.client.api.ObjectsApi;
import com.autodesk.client.auth.Credentials;
import com.autodesk.client.auth.OAuth2TwoLegged;

/**
 * Created by xiaodongliang on 1/18/18.
 */

public class Global {

    public static final String CLIENT_ID = "<Your API key of Forge>";
    public static final String CLIENT_SECRET = "<Your API Secret of Forge>";

    public static String BUCKET_KEY = "";

    public static String FILE_NAME = "";

    public static String token = "";

    public static String URN = "";
    public static String base64URN = "";

    public static OAuth2TwoLegged oauth2TwoLegged = null;
    public static Credentials twoLeggedCredentials = null;

    public static final BucketsApi bucketsApi = new BucketsApi();
    public static final ObjectsApi objectsApi = new ObjectsApi();
    public static final DerivativesApi derivativesApi = new DerivativesApi();

}
