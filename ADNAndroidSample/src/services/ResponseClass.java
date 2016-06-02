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
	
	//v2 format
	//https://developer.autodesk.com/api/view-and-data-api/#api-features
	public class srv_bucket_class {
		
		 @SerializedName("objectId")
		 	public String objectId; 
		 @SerializedName("bucketKey")
		 	public String bucketKey; 
		 @SerializedName("location")
		 	public String location; 
	}
	
	//v1 format; retired
	//public class srv_bucket_class {
		
	//	 @SerializedName("objects")
	//	    public srv_bucket_object_class objects[]; 
	//}
	
	//v1 format; retired
	//public class srv_bucket_object_class {
		
	//	 @SerializedName("id")
	//	    public String id; 
	//	 @SerializedName("location")
	//	    public String location; 
		 

	//}
	
	
	
	
}
