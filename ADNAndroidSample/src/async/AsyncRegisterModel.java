package async;

import java.util.List;

import main.GlobalHelper;
import main.MainActivity;
import services.RestServices;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class AsyncRegisterModel extends  AsyncTask<List<String>, Void, Void>{

	public MainActivity _activity;
	//indicate whether the task completed
    private Boolean _isOK = false;
    
  //ini progress dialog
  	private ProgressDialog progress;
  	  public AsyncRegisterModel(ProgressDialog progress) {
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
					  "Register Model Succeeded!",
					Toast.LENGTH_LONG).show(); 
	    	  
	    	 
	    	 //tell the main activity to refresh
	    	 _activity.setResult(20, null);
	    	 
	    	 
	    	 TextView registerStatus = (TextView)_activity.findViewById(R.id.txtviewregisterstatus);
	    	 registerStatus.setText( "Register Status:" + GlobalHelper._registerStat );
	    }
	    else
	    {
	    	// show msg of failure
	    	 Toast.makeText(
					  _activity.getApplicationContext(),
					  "Failed to Register Model",
					Toast.LENGTH_LONG).show();
	    }
	    
	    // end login activity
	    //return to main activity
	   
	    //_activity.finish();
	  }
	@Override
	protected Void doInBackground(List<String>... params) {
		// TODO Auto-generated method stub
		if(RestServices.srv_register_model(params[0].get(0)) ) 
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
