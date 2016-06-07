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

public class AsyncToken extends AsyncTask<List<String>, Void, Void> {

    public MainActivity _activity;
    //indicate whether the task completed
    private Boolean _isOK = false;
    //initialize progress dialog
    private ProgressDialog progress;

    public AsyncToken(ProgressDialog progress) {
        this.progress = progress;
    }

    public void onPreExecute() {
        progress.show();
    }

    // task completed
    public void onPostExecute(Void unused) {
        progress.dismiss();
        if (_isOK) {
            // show msg of succeess
            Toast.makeText(
                _activity.getApplicationContext(),
                "get token Succeeded!",
                Toast.LENGTH_LONG).show();

			//tell the main activity to refresh
            _activity.setResult(20, null);

            TextView tokentxt = (TextView)_activity.findViewById(R.id.textViewToken);
            tokentxt.setText(GlobalHelper._currentToken);
        } else {
            // show msg of failure
            Toast.makeText(
                _activity.getApplicationContext(),
                "Failed to get token",
                Toast.LENGTH_LONG).show();
        }
        // end login activity
        //return to main activity
        //_activity.finish();
    }

    @Override
    protected Void doInBackground(List<String>... params) {
        // TODO Auto-generated method stub

        // call service of login
        // input user name and password

        if (RestServices.srv_authenticate(
                    params[0].get(0),   // user name
                    params[0].get(1) ))  // password
        {
            _isOK = true;
        } else {
            _isOK = false;
        }
        return null;
    }

}
