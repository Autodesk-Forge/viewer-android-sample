package com.autodesk.forge.forgeviewer_android_sample;

import android.*;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.os.Environment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.*;
import android.support.v4.content.*;
import android.support.v4.app.ActivityCompat;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;




import com.autodesk.client.ApiException;
import com.autodesk.client.ApiResponse;
import com.autodesk.client.api.BucketsApi;
import com.autodesk.client.api.DerivativesApi;
import com.autodesk.client.api.ObjectsApi;
import com.autodesk.client.auth.Credentials;
import com.autodesk.client.auth.OAuth2TwoLegged;
import com.autodesk.client.model.*;
import com.autodesk.client.model.Manifest;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//import javax.ws.rs.core.UriBuilder;
//import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private Button btn_get_token;
    private Button btn_create_bucket;
    private Button btn_browser_model;
    private Button btn_upload_model;
    private Button btn_post_job;
    private Button btn_show_thumbnail;
    private Button btn_display_model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_get_token = (Button)findViewById(R.id.btnGetToken);
        btn_get_token.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    ProgressDialog progress = new ProgressDialog(MainActivity.this);
                    AsyncGetToken task_gettoken =  new AsyncGetToken(progress,MainActivity.this);
                    task_gettoken.execute();
                }
                catch(Exception ex){

                    Toast.makeText(
                            getApplicationContext(),
                            ex.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_create_bucket = (Button)findViewById(R.id.btnCreateBucket);
        btn_create_bucket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    ProgressDialog progress = new ProgressDialog(MainActivity.this);
                    AsyncCreateBucket task_createtoken =  new AsyncCreateBucket(progress,MainActivity.this);
                    task_createtoken.execute();
                }
                catch(Exception ex){

                    Toast.makeText(
                            getApplicationContext(),
                            ex.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_browser_model = (Button)findViewById(R.id.btnBrowserModel);
        btn_browser_model.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                loadFileList();
                myFileDialog(DIALOG_LOAD_FILE).show();
            }
        });

        btn_upload_model = (Button)findViewById(R.id.btnUploadModel);
        btn_upload_model.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mChosenFile==null || mChosenFile=="")
                    return;

                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                AsyncUpload task_upload =  new AsyncUpload(progress,MainActivity.this);
                 task_upload.execute();

            }
        });

        btn_post_job = (Button)findViewById(R.id.btnPostJob);
        btn_post_job.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                AsyncPostJob task_post_job =  new AsyncPostJob(progress,MainActivity.this);
                 task_post_job.execute();

            }
        });

        btn_show_thumbnail = (Button)findViewById(R.id.btnShowthumbnail);
        btn_show_thumbnail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                AsyncThumbnail task_thumbnail =  new AsyncThumbnail(progress,MainActivity.this);
                 task_thumbnail.execute();

            }
        });

        btn_display_model = (Button)findViewById(R.id.btndisplaymodel);
        btn_display_model.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String viewUrl = "https://models.autodesk.io/view.html?";

                TextView txtViewToken = (TextView)findViewById(R.id.textViewToken);
                TextView txtViewUrn = (TextView)findViewById(R.id.textViewUrn);


                viewUrl = viewUrl + "token=" + txtViewToken.getText().toString();
                viewUrl = viewUrl + "&urn=" + txtViewUrn.getText().toString();


                //start the browser activity
                Intent viewModelIntent = new
                       Intent("android.intent.action.VIEW",Uri.parse(viewUrl));
                startActivity(viewModelIntent);



            }
        });


    }

    private String[] mFileList;
    private String mChosenFile;
    private static final int DIALOG_LOAD_FILE = 1000;

    private void loadFileList() {


        //String xx = Environment.getExternalStorageDirectory() + "/";
        File mPath = new File(Environment.getExternalStorageDirectory() + "/DCIM/" );//+
                //getApplicationContext().getString(R.string.app_name));

         try {
            mPath.mkdirs();
        } catch (SecurityException e) {
            //Log.e(TAG, "unable to write on the sd card " + e.toString());
        }
        if (mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    //add your filter if needed
                    File sel = new File(dir, filename);
                    return true;
                }
            };
            mFileList = mPath.list(filter);
        } else {
            mFileList= new String[0];
        }
    }

    protected Dialog myFileDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new Builder(this);

        switch (id) {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                if (mFileList == null) {
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChosenFile = mFileList[which];
                        TextView modelName = (TextView)findViewById(R.id.textViewModelName);
                        modelName.setText(mChosenFile);
                    }
                });

                break;
        }
        dialog = builder.show();
        return dialog;
    }


    // In Android 6.0 Marshmallow, application will not be granted any permission at installation time.
    // Instead, application has to ask user for a permission one-by-one at runtime.
    //https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en

    // p: the specific permission
    // e.g. android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    //conserve in case of use.
    private void grantPermission(String p)
    {
         int REQUEST_CODE_ASK_PERMISSIONS = 124;


        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(),
                p);


        //grant this specific permission
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {p},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
