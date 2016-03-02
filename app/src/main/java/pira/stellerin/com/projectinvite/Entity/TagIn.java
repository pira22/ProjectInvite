package pira.stellerin.com.projectinvite.Entity;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("TagIn")
public class TagIn extends ParseObject {

	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}

	public void setLocation(ParseGeoPoint value) {
		put("location", value);
	}

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}

	public void setLocationName(String value) {
		put("locationName", value);
	}

	public String getLocationName() {
		return getString("locationName");
	}

	public static ParseQuery<TagIn> getQuery() {
		return ParseQuery.getQuery(TagIn.class);
	}
	@Override
	public String toString() {
		
		return getLocationName()+""+getLocationName();
	}
}
