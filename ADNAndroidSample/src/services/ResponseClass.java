package services;

import com.google.gson.annotations.SerializedName;

public class ResponseClass {

	public class srv_authenticate_class {
		
		 @SerializedName("token_type")
		    public String token_type;
		 
		 @SerializedName("expires_in")
		    public String expires_in;
		 
		 @SerializedName("access_token")
		    public String access_token;

	}
	
	public class srv_bucket_class {
		
		 @SerializedName("objects")
		    public srv_bucket_object_class objects[]; 
	}
	
	public class srv_bucket_object_class {
		
		 @SerializedName("id")
		    public String id; 
		 @SerializedName("location")
		    public String location; 
		 

	}
	
	
	
	
}
