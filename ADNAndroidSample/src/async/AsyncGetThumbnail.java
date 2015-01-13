package async;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import main.MainActivity;
import services.RestServices;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.main.R;

public class AsyncGetThumbnail extends  AsyncTask<List<String>, Void, Void>{

	public MainActivity _activity;
	//indicate whether the task completed
    private Boolean _isOK = false;
    
    private Bitmap _bp = null;
    
  //ini progress dialog
  	private ProgressDialog progress;
  	  public AsyncGetThumbnail(ProgressDialog progress) {
  		    this.progress = progress;
  		  }

  		  public void onPreExecute() {
  		    progress.show();
  		  } 
  		  
  		  
  		public static Bitmap getLoacalBitmap(String url) {
  		     try {
  		          FileInputStream fis = new FileInputStream(url);
  		          return BitmapFactory.decodeStream(fis);
  		     } catch (FileNotFoundException e) {
  		          e.printStackTrace();
  		          return null;
  		     }
  		}
  		
    // task completed
	  public void onPostExecute(Void unused) { 
	    progress.dismiss();
	    
	    
	    if(_isOK)
	    {
	    	// show msg of succeess
	    	 Toast.makeText(
					  _activity.getApplicationContext(),
					  "get thumbnail succeeded!",
					Toast.LENGTH_LONG).show(); 
	    	  
	    	 
	    	 //tell the main activity to refresh
	    	 _activity.setResult(20, null);
	    	 
	    	 
	    	 //create the temp file to store the image of attachment
       	  File folder = new File(Environment.getExternalStorageDirectory(), 
       			  "ADNAndroidTestView");
       	  File toWrite = 
       			  new File(folder, "save.png");
       	  if(!toWrite.exists())
       	  {
       	      if(!folder.mkdirs())
				  {
				       
				  }
       	   } 
	   	 
	   		//produce the file stream for the bitmap
	   		FileOutputStream fos = null;
				try {
					 fos = new FileOutputStream(toWrite);
					 _bp.compress( CompressFormat.PNG, 100, fos );						
					fos.close(); 
					fos.flush();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			
		 	//Intent intent = new Intent();
			//intent.setAction(android.content.Intent.ACTION_VIEW);
			//intent.setDataAndType(Uri.fromFile(new File(toWrite.getPath())), "image/png");
			//_activity.startActivity(intent); 
				
				 ImageView image1 = (ImageView) _activity.findViewById(R.id.imageView1);
				 image1.setImageBitmap(_bp);
			  

	    	  
	    }
	    else
	    {
	    	// show msg of failure
	    	 Toast.makeText(
					  _activity.getApplicationContext(),
					  "Failed to get thumbnail",
					Toast.LENGTH_LONG).show();
	    }
	    
	    // end login activity
	    //return to main activity
	   
	    //_activity.finish();
	  }
	@Override
	protected Void doInBackground(List<String>... params) {
		// TODO Auto-generated method stub
		_bp = RestServices.srv_get_bubble_thumb(params[0].get(0));
		if(_bp != null )   
		 {  			 
			 _isOK = true;
		 }
		 else
		 {
			 _isOK = false;
		 }
		 
		
		return null;
	}

}
