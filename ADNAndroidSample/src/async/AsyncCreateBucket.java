package async;

import java.util.List;

import main.MainActivity;
import services.RestServices;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncCreateBucket extends  AsyncTask<List<String>, Void, Void>{

	public MainActivity _activity;
	//indicate whether the task completed
    private Boolean _isOK = false;
    
  //initialize progress dialog
  	private ProgressDialog progress;
  	  public AsyncCreateBucket(ProgressDialog progress) {
  		    this.progress = progress;
  		  }

  		  public void onPreExecute() {
  		    progress.show();
  		  } 
  		  
  		  
    // task completed
	  public void onPostExecute(Void unused) { 
	    progress.dismiss();
	    
	    
	    if(_isOK)
	    {
	    	// show msg of succeess
	    	 Toast.makeText(
					  _activity.getApplicationContext(),
					  "create bucket succeeded!",
					Toast.LENGTH_LONG).show(); 
	    	  
	    	 
	    	 //tell the main activity to refresh
	    	 _activity.setResult(20, null); 
	    	  
	    }
	    else
	    {
	    	// show msg of failure
	    	 Toast.makeText(
					  _activity.getApplicationContext(),
					  "Failed to create bucket",
					Toast.LENGTH_LONG).show();
	    } 
	   
	  }
	@Override
	protected Void doInBackground(List<String>... params) {
		// TODO Auto-generated method stub
		if(RestServices.srv_create_bucket(params[0].get(0)))  
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
