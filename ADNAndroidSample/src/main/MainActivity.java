package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.List;

import async.AsyncCreateBucket;
import async.AsyncGetThumbnail;
import async.AsyncRegisterModel;
import async.AsyncToken;
import async.AsyncUpload;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class MainActivity extends Activity {

    private Button btn_get_token;
    private Button btn_create_bucket;
    private Button btn_browser_model;
    private Button btn_upload_model;
    private Button btn_register_model;
    private Button btn_show_thumbnail;
    private Button btn_launch_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //copy test models to the storage of the mobile
        CopyTestModelToStorage();

        btn_get_token = (Button)findViewById(R.id.gettoken);
        btn_create_bucket = (Button)findViewById(R.id.createbucket);
        btn_upload_model =(Button)findViewById(R.id.uploadmodel);
        btn_register_model =(Button)findViewById(R.id.btnpostbubble);
        btn_show_thumbnail = (Button)findViewById(R.id.showthumbnail);
        btn_browser_model =  (Button)findViewById(R.id.btnBrowserModel);
        btn_launch_view = (Button)findViewById(R.id.displaymodel);

        //get token button
        btn_get_token.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<String> forAsync = new ArrayList<String>();
                String dummy1 = "dummy1";
                String dummy2 = "dummy2";
                forAsync.add(dummy1);
                forAsync.add(dummy2);

                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                AsyncToken task_gettoken =  new AsyncToken(progress);
                task_gettoken._activity = MainActivity.this;
                task_gettoken.execute(forAsync );
            }
        });

        //create bucket button
        btn_create_bucket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView bucketName = (TextView)findViewById(R.id.txtViewBucketName);
                List<String> forAsync = new ArrayList<String>();
                String dummy1 = bucketName.getText().toString();
                String dummy2 = "dummy";
                forAsync.add(dummy1);
                forAsync.add(dummy2);

                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                AsyncCreateBucket task_upload =  new AsyncCreateBucket(progress);
                task_upload._activity = MainActivity.this;
                task_upload.execute(forAsync);

            }
        });

        //browser model button
        btn_browser_model.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFileList();
                onCreateDialog(DIALOG_LOAD_FILE).show();
            }
        });

        //upload model button
        btn_upload_model.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mChosenFile==null || mChosenFile=="")
                    return;
                File mPath = new File(Environment.getExternalStorageDirectory() + "/" +
                                      getApplicationContext().getString(R.string.app_name));
                TextView bucketName = (TextView)findViewById(R.id.txtViewBucketName);
                TextView modelName = (TextView)findViewById(R.id.txtViewModelName);


                List<String> forAsync = new ArrayList<String>();
                String dummy1 = bucketName.getText().toString();
                String dummy2 = mPath + "/"+modelName.getText().toString();
                forAsync.add(dummy1);
                forAsync.add(dummy2);

                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                //progress.setMessage(getString(R.string.msg_prog_login_async));
                AsyncUpload task_upload =  new AsyncUpload(progress);
                task_upload._activity = MainActivity.this;
                task_upload.execute(forAsync);

            }
        });

        //register model button
        btn_register_model.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView urntxt = (TextView)findViewById(R.id.textViewUrn);

                List<String> forAsync = new ArrayList<String>();
                String dummy1 = urntxt.getText().toString();
                String dummy2 = "dummy";
                forAsync.add(dummy1);
                forAsync.add(dummy2);

                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                AsyncRegisterModel post_bubble =  new AsyncRegisterModel(progress);
                post_bubble._activity = MainActivity.this;
                post_bubble.execute(forAsync);

            }
        });

        //show thumbnail button
        btn_show_thumbnail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView urntxt = (TextView)findViewById(R.id.textViewUrn);

                List<String> forAsync = new ArrayList<String>();
                String dummy1 = urntxt.getText().toString();
                String dummy2 = "dummy";
                forAsync.add(dummy1);
                forAsync.add(dummy2);

                ProgressDialog progress = new ProgressDialog(MainActivity.this);
                AsyncGetThumbnail task_thumb =  new AsyncGetThumbnail(progress);
                task_thumb._activity = MainActivity.this;
                task_thumb.execute(forAsync);

            }  //onClick
        });

        //launch model in the browser
        btn_launch_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView urntxt = (TextView)findViewById(R.id.textViewUrn);
                TextView tokentxt = (TextView)findViewById(R.id.textViewToken);

                //build the url using helper page provided by DevTech, ADN
                //format:
                //http://viewer.autodesk.io/node/view-helper?urn=someUrn&token=yourGeneratedToken
                String viewUrl = "http://viewer.autodesk.io/node/view-helper?";
                viewUrl = viewUrl + "urn=" + urntxt.getText().toString();
                viewUrl = viewUrl + "&token=" + tokentxt.getText().toString();

                //start the browser activity
                Intent viewModelIntent = new
                Intent("android.intent.action.VIEW",Uri.parse(viewUrl));
                startActivity(viewModelIntent);

                // select the browser that is WebGL compatible.
                // the model should be displayed in the browser.
            }
        });
    }

    // your test models are packaged with apk in \res\raw. They will be copied to
    // Environment.getExternalStorageDirectory().getAbsolutePath() + "/ADNAndroidTestView"
    private void CopyTestModelToStorage() {
        try {
            //get working folder of the app on the mobile
            String fileDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+
                                 getApplicationContext().getString(R.string.app_name) ;
            //create the folder if it does not exist
            File allfiles  = new File(fileDirPath);
            if (!allfiles.exists()) {
                //if the folder does not exist
                if (allfiles.mkdirs()) {
                    //create the folder
                } else {
                    //failed
                    Toast.makeText(
                        getApplicationContext(),
                        "Cannot Create Folder to Store Test Model!",
                        Toast.LENGTH_LONG).show();
                    return;
                }
            }

            //copy the files (test models) in res/raw one by one to the storage of the mobile
            Resources res = getResources();
            java.lang.reflect.Field[] fields=R.raw.class.getFields();
            for (int count=0; count < fields.length; count++) {
                int resourceId=res.getIdentifier(fields[count].getName(),
                                                 "raw",
                                                 getPackageName());
                //get full file name
                TypedValue value = new TypedValue();
                getResources().getValue(resourceId, value, true);
                String filename = value.string.toString();
                filename = filename.split("res/raw/")[1];
                String eachFilePathInStorage = fileDirPath + "/" + filename;
                File eachfile = new File(eachFilePathInStorage);
                //read out the file stream and copy it to the storage
                InputStream ins = res.openRawResource(resourceId);
                FileOutputStream fos = new FileOutputStream(eachfile);
                byte[] buffer = new byte[8192];
                int count1 = 0;
                while ((count1 = ins.read(buffer)) > 0) {
                    fos.write(buffer, 0, count1);
                }
                fos.close();
                ins.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(
                getApplicationContext(),
                "Some Errors When Copying Test Models to Storage. Contact the Author!",
                Toast.LENGTH_LONG).show();
        }
    }

    private String[] mFileList;
    private String mChosenFile;
    private static final int DIALOG_LOAD_FILE = 1000;

    private void loadFileList() {
        File mPath = new File(Environment.getExternalStorageDirectory() + "/" +
                              getApplicationContext().getString(R.string.app_name));
        try {
            mPath.mkdirs();
        } catch (SecurityException e) {
            //Log.e(TAG, "unable to write on the sd card " + e.toString());
        }
        if (mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return true;
                }
            };
            mFileList = mPath.list(filter);
        } else {
            mFileList= new String[0];
        }
    }

    protected Dialog onCreateDialog(int id) {
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
						TextView modelName = (TextView)findViewById(R.id.txtViewModelName);
						modelName.setText(mChosenFile);

					}
				});

				break;
        }
        dialog = builder.show();
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //login activity returned
        if (20==resultCode) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void GetCredentials() {
    }

}
