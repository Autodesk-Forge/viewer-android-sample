package com.autodesk.forge.forgeviewer_android_sample;

/**
 * Created by xiaodongliang on 1/18/18.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


import com.autodesk.client.ApiException;
import com.autodesk.client.auth.Credentials;
import com.autodesk.client.auth.OAuth2TwoLegged;

import org.w3c.dom.Text;


public class AsyncGetToken extends AsyncTask<List<String>, String, Void>  {



    private MainActivity activity;
    //indicate whether the task completed
    private  String responseStr = "";
    //initialize progress dialog
    private ProgressDialog progress;

    //text view of token
    private TextView tokenView = null;

    //text view of status
    private TextView statusView = null;



    public AsyncGetToken(ProgressDialog p, MainActivity a) {
        this.progress = p;
        this.activity = a;

        statusView = (TextView)activity.findViewById(R.id.textViewStatus);
        tokenView = (TextView)activity.findViewById(R.id.textViewToken);
    }

    public void onPreExecute() {
        progress.show();
        statusView.setText("working for get token.....");

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

            //the JWT token string is too long,
            // but no idea why it does not work with
            // TextView:maxLine =1, TextView:ellipsize="end", TextView:marqueeRepeatLimit="marquee_forever"
            // the text view will be multi lines after the long string is input.
            //so trunct the string manually

            String tokenStr = values[1];

            if(tokenStr.length() > 10){
                tokenStr = tokenStr.substring(0,10);
                tokenStr += ".........";
            }

            tokenView.setText(tokenStr);


        }
    }

    @Override
    protected Void doInBackground(List<String>... params) {

        String CLIENT_ID = Global.CLIENT_ID;
        String CLIENT_SECRET = Global.CLIENT_SECRET;
        List<String> scopes = new ArrayList<String>();

        scopes.add("data:read");
        scopes.add("data:write");
        scopes.add("bucket:create");
        scopes.add("bucket:read");

         try {

             //remove last token if any
             Global.token="";

             Global.oauth2TwoLegged = new OAuth2TwoLegged(CLIENT_ID, CLIENT_SECRET, scopes, true);

             Global.twoLeggedCredentials = Global.oauth2TwoLegged .authenticate();
             String token = Global.twoLeggedCredentials.getAccessToken();

            //update with the new token
             Global.token = token;
             responseStr = "get token Succeeded!";
         }
         catch(ApiException ae){
             responseStr ="Failed to get token" + ae.toString();
         }
         catch(Exception ex){
             responseStr ="Failed to get token" + ex.toString();
         }
         finally{
             String[] values = new String[2];
             values[0]= responseStr;
             values[1]= Global.token;
             publishProgress(values);
          }
        return null;
    }

}
