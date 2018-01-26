package com.autodesk.forge.forgeviewer_android_sample;

import android.os.AsyncTask;

import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;




import com.autodesk.client.ApiException;
import com.autodesk.client.ApiResponse;
import com.autodesk.client.model.Bucket;
import com.autodesk.client.model.PostBucketsPayload;

import java.io.File;
import java.util.List;

/**
 * Created by xiaodongliang on 1/21/18.
 */

public class AsyncThumbnail extends AsyncTask<List<String>, String, Void> {

    private MainActivity activity;
    //indicate whether the task completed
    private  String responseStr = "";
    //initialize progress dialog
    private ProgressDialog progress;
    //text view of status
    private TextView statusView = null;

    //thumbnail bitmap
    private Bitmap thumbnailBP = null;

    public AsyncThumbnail(ProgressDialog p, MainActivity a) {

        this.progress = p;
        this.activity = a;

        statusView = (TextView)activity.findViewById(R.id.textViewStatus);

    }

    public void onPreExecute() {
        progress.show();
        statusView.setText("working for get thumbnail.....");

    }

    // task completed
    public void onPostExecute(Void unused) {

        progress.dismiss();

        if(thumbnailBP != null) {
            ImageView image1 = (ImageView) activity.findViewById(R.id.imgthumbnail);
            image1.setImageBitmap(thumbnailBP);

        }

//        Toast.makeText(
//                activity.getApplicationContext(),
//                responseStr,
//                Toast.LENGTH_LONG).show();
    }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values != null && values.length > 0) {
            statusView.setText(values[0]);

        }
    }

    @Override
    protected Void doInBackground(List<String>... params) {
        try {


            ApiResponse<File> response_thumbnail = Global.derivativesApi.getThumbnail(
                    Global.base64URN,200,200,Global.oauth2TwoLegged,Global.twoLeggedCredentials);

            InputStream is = new FileInputStream(response_thumbnail.getData());
            thumbnailBP = BitmapFactory.decodeStream(is);
            is.close();


            responseStr = "get thumbnail Succeeded!";
        }
        catch(ApiException e){

            responseStr = "Failed to get thumbnail  " + e.getResponseBody();
        }
        catch (Exception ex){

            responseStr = "Failed to get thumbnail " + ex.toString();

        }
        finally{
            publishProgress(responseStr);
        }

        return null;
    }

}
