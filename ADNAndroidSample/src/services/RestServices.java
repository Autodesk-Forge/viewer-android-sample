package services;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.GlobalHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.client.CookieStore;

import services.ResponseClass.srv_authenticate_class;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;

public class RestServices {

	//private static String BASEUrl = "https://developer.api.autodesk.com";
    private static String BASEUrl = main.Credentials.BASEUrl;
    private static String authenticate_srv = "/authentication/v1/authenticate";
    private static String upload_srv = "/oss/v2/buckets";
    private static String settoken_srv = "/utility/v1/settoken";
    private static String register_srv = "/viewingservice/v1/register";
    private static String get_urn_thumb_srv = "/viewingservice/v1/thumbnails";

    static  CookieStore globalcookies = null;

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactoryEx sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactoryEx.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    //get token
    public static Boolean srv_authenticate(String _clientid, String _clientsecret) {
        try
        {
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("client_id", main.Credentials.CONSUMER_KEY));
            nvps.add(new BasicNameValuePair("client_secret", main.Credentials.CONSUMER_SECRET));
            nvps.add(new BasicNameValuePair("grant_type", "client_credentials"));

            HttpPost request = new HttpPost(BASEUrl+ authenticate_srv);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

            UrlEncodedFormEntity uf;
            uf = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
            request.setEntity(uf);

            HttpClient httpclient =  getNewHttpClient();
            HttpResponse response = httpclient.execute(request);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                //succeeded
                HttpEntity getResponseEntity = response.getEntity();
                Reader reader = new InputStreamReader(getResponseEntity.getContent());
                Gson gson = new Gson();
                ResponseClass.srv_authenticate_class _login_cls = gson.fromJson(reader, ResponseClass.srv_authenticate_class.class);

                GlobalHelper._currentToken = _login_cls.access_token;

                if (!srv_settoken())
                    return false;
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean srv_create_bucket(String newBucketName) {
        if (globalcookies==null )
            if (!srv_settoken())
                return false;
        try {
            HttpPost request = new HttpPost(BASEUrl+ upload_srv);
            request.addHeader("Content-Type", "application/json");

            //v2 changed the param name from 'policy' to 'policyKey'
            String jsonstr  ="{\"bucketKey\":\""+ newBucketName + "\", \"servicesAllowed\":{}, \"policyKey\":\"temporary\"}";
            HttpEntity jsonent = new StringEntity(jsonstr,HTTP.UTF_8);
            request.setEntity(jsonent);

            HttpClient httpclient =  getNewHttpClient();
            HttpContext localContext = new BasicHttpContext();

            localContext.setAttribute(ClientContext.COOKIE_STORE, globalcookies);

            HttpResponse response = httpclient.execute(request,localContext);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                return true;
            } else if ( statusLine.getStatusCode() == HttpStatus.SC_CONFLICT) {
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean srv_settoken() {
        try {
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("access-token",  GlobalHelper._currentToken ));

            HttpPost request = new HttpPost(BASEUrl+ settoken_srv);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

            UrlEncodedFormEntity uf;
            uf = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
            request.setEntity(uf);

            HttpClient httpclient =  getNewHttpClient();
            HttpResponse response = httpclient.execute(request);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity getResponseEntity = response.getEntity();
                globalcookies =  ((DefaultHttpClient)httpclient).getCookieStore();
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean srv_getbucket_details(String bucketName) {
        try {
            String thisurl =BASEUrl + "/oss/v1/buckets" + "/"+ bucketName + "/details";

            HttpClient httpclient =  getNewHttpClient();
            HttpContext localContext = new BasicHttpContext();

            HttpGet request = new HttpGet(thisurl);
            localContext.setAttribute(ClientContext.COOKIE_STORE, globalcookies);

            HttpResponse response = httpclient.execute(request,localContext);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK || statusLine.getStatusCode() == 409) {
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean srv_upload(String bucketName,String fileName) {
        if ( globalcookies==null ) {
            if ( !srv_settoken() )
                return false;
        }
        try {
            //get file from SD card
            File toWrite = new File(fileName);
            if (!toWrite.exists()) {
                return false;
            }

            String thisurl =BASEUrl + upload_srv + "/" + bucketName+ "/objects/" + toWrite.getName();

            // Required, accepts any content-type except multipart/form-data.
            FileEntity entity = new FileEntity(toWrite,"application/stream");
            HttpPut request = new HttpPut(thisurl);
            request.setEntity(entity);
            //must set although help reference says it could be ignored.
            request.addHeader("Content-Type", "application/stream");

            //request.setHeader("Content-Length",Long.toString(toWrite.length()));

            HttpClient httpclient =  getNewHttpClient();
            HttpContext localContext = new BasicHttpContext();

            localContext.setAttribute(ClientContext.COOKIE_STORE, globalcookies);

            HttpResponse response = httpclient.execute(request,localContext);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK || statusLine.getStatusCode() == 201) {
                //succeeded
                //get response json data
                HttpEntity getResponseEntity = response.getEntity();
                Reader reader = new InputStreamReader(getResponseEntity.getContent());
                Gson gson = new Gson();

                //v2 changed the response json structure

                ResponseClass.srv_bucket_class _bucket_cls = gson.fromJson(reader, ResponseClass.srv_bucket_class.class);
                String _urn = android.util.Base64.encodeToString(_bucket_cls.objectId.getBytes(),Base64.NO_WRAP);;

                GlobalHelper._currentUrn = _urn;

                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean srv_register_model(String thisUrn) {
        try {
            String thisurl = BASEUrl + register_srv;
            HttpPost request = new HttpPost(thisurl);

            String jsonStr  = "{\"urn\":\"" + thisUrn + "\"}";
            HttpEntity jsonEnt = new StringEntity(jsonStr,HTTP.UTF_8);
            request.setEntity(jsonEnt);

            request.setHeader("Content-Type", "application/json");
            request.setHeader("x-ads-force", "true");

            HttpClient httpclient =  getNewHttpClient();
            HttpContext localContext = new BasicHttpContext();

            localContext.setAttribute(ClientContext.COOKIE_STORE, globalcookies);

            HttpResponse response = httpclient.execute(request,localContext);

            StatusLine statusLine = response.getStatusLine();
            GlobalHelper._registerStat =  Integer.toString(statusLine.getStatusCode());
            if (statusLine.getStatusCode() == HttpStatus.SC_OK || statusLine.getStatusCode() == 201 ) {
                //succeeded
                //get response json data
                HttpEntity getResponseEntity = response.getEntity();
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap srv_get_bubble_thumb(String thisUrn) {
        try {
            String thisurl = BASEUrl + get_urn_thumb_srv + "/"+ thisUrn;
            HttpGet request = new HttpGet(thisurl);

            HttpClient httpclient =  getNewHttpClient();
            HttpContext localContext = new BasicHttpContext();

            localContext.setAttribute(ClientContext.COOKIE_STORE, globalcookies);

            HttpResponse response = httpclient.execute(request,localContext);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                //read the binary data and convert to bitmap
                InputStream is = entity.getContent();
                Bitmap returnBM = BitmapFactory.decodeStream(is);
                is.close();
                return returnBM;
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
