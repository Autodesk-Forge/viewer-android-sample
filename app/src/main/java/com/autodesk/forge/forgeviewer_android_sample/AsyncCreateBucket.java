package com.autodesk.forge.forgeviewer_android_sample;

/**
 * Created by xiaodongliang on 1/18/18.
 */

import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


import com.autodesk.client.ApiException;
import com.autodesk.client.ApiResponse;
import com.autodesk.client.auth.Credentials;
import com.autodesk.client.auth.OAuth2TwoLegged;
import com.autodesk.client.model.Bucket;
import com.autodesk.client.model.PostBucketsPayload;

public class AsyncCreateBucket extends AsyncTask<List<String>, String, Void>  {

    private MainActivity activity;
    //indicate whether the task completed
    private  String responseStr = "";
    //initialize progress dialog
    private ProgressDialog progress;
    //text view of status
    private TextView statusView = null;


    public AsyncCreateBucket(ProgressDialog p,MainActivity a) {
            this.progress = p;
            this.activity = a;

            statusView = (TextView)activity.findViewById(R.id.textViewStatus);



    }

        public void onPreExecute() {
            progress.show();
        }

        // task completed
        public void onPostExecute(Void unused) {

            progress.dismiss();


//            Toast.makeText(
//                    activity.getApplicationContext(),
//                    responseStr,
//                    Toast.LENGTH_LONG).show();
//
//            //tell the main activity to refresh
//            activity.setResult(20, null);
        }

    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values != null && values.length > 0) {
            statusView.setText(values[0]);
        }
    }

        @Override
        protected Void doInBackground(List<String>... params){

            try {

                statusView.setText("working for create bucket.....");

                TextView bucketName = (TextView)activity.findViewById(R.id.textViewBucketName);
                String BUCKET_KEY = bucketName.getText().toString();
                //reset the bucket key with user input
                Global.BUCKET_KEY = BUCKET_KEY;

                PostBucketsPayload payload = new PostBucketsPayload();
                payload.setBucketKey(BUCKET_KEY);
                payload.setPolicyKey(PostBucketsPayload.PolicyKeyEnum.PERSISTENT);
                ApiResponse<Bucket> response = Global.bucketsApi.createBucket(payload,
                            "US",
                        Global.oauth2TwoLegged ,Global.twoLeggedCredentials);


                responseStr = "create bucket Succeeded!";
            }
            catch(ApiException e){

                responseStr = "Failed to create bucket  " + e.getResponseBody();
            }
            catch (Exception ex){

                responseStr = "Failed to create bucket  " + ex.toString();

            }
            finally {
                publishProgress(responseStr);
            }
            return null;
        }
}
