package com.autodesk.forge.forgeviewer_android_sample;

import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.autodesk.client.ApiException;
import com.autodesk.client.ApiResponse;
import com.autodesk.client.auth.OAuth2TwoLegged;
import com.autodesk.client.model.ObjectDetails;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaodongliang on 1/18/18.
 */

public class AsyncUpload extends AsyncTask<List<String>, String, Void>  {

    private MainActivity activity;
    //indicate whether the task completed
    private  String responseStr = "";
    //initialize progress dialog
    private ProgressDialog progress;
    //text view of urn
    private TextView urnView = null;
    //text view of status
    private TextView statusView = null;

    public AsyncUpload(ProgressDialog p, MainActivity a) {

        this.progress = p;
        this.activity = a;
        statusView = (TextView)activity.findViewById(R.id.textViewStatus);
        urnView = (TextView)activity.findViewById(R.id.textViewUrn);


    }

    public void onPreExecute() {
        progress.show();
        statusView.setText("working for upload file.....");

    }

    // task completed
    public void onPostExecute(Void unused) {

        progress.dismiss();

//        Toast.makeText(
//                activity.getApplicationContext(),
//                responseStr,
//                Toast.LENGTH_LONG).show();
    }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values != null && values.length > 0) {
            statusView.setText(values[0]);
            urnView.setText(values[0]);

        }
    }

    @Override
    protected Void doInBackground(List<String>... params) {

        try {


            TextView modelName = (TextView)activity.findViewById(R.id.textViewModelName);
            String FILE_NAME = modelName.getText().toString();
            String FILE_PATH = Environment.getExternalStorageDirectory() + "/DCIM/" +  FILE_NAME;

            File fileToUpload = new File(FILE_PATH);

            ApiResponse<ObjectDetails> response = Global.objectsApi.uploadObject(Global.BUCKET_KEY,
                    FILE_NAME, (int)fileToUpload.length(),
                    fileToUpload, null, null,
                    Global.oauth2TwoLegged, Global.twoLeggedCredentials);

            ObjectDetails objectDetails = response.getData();

            //get urn from the response
            Global.URN = objectDetails.getObjectId();

            //convert to base64 urn
            byte[] urnBase64 = Base64.encodeBase64(Global.URN.getBytes());
            String base64Str = new String(urnBase64);
            Global.base64URN = base64Str;


            responseStr = "Upload File Succeeded!";
        }
        catch(ApiException ae){
            responseStr ="Failed to upload file " + ae.toString();
        }
        catch(Exception ex){
            responseStr ="Failed to upload file " + ex.toString();
        }
        finally{
            String[] values = new String[2];
            values[0]= responseStr;
            values[1]= Global.base64URN;
            publishProgress(values);
        }

        return null;
    }
}
