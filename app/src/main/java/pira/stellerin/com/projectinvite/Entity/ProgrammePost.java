package pira.stellerin.com.projectinvite.Entity;


import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Data model for a post.
 */
@ParseClassName("Programmes")
public class ProgrammePost extends ParseObject {
	public String getText() {
		return getString("titre");
	}

	public void setText(String value) {
		put("titre", value);
	}

	public String getDescription() {
		return getString("description");
	}
	
	public void setDescription(String value) {
		put("description", value);
	}
	public String getPlace() {
		return getString("place");
	}

	public void setPlace(String value) {
		put("place", value);
	}


	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}

	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}

	public void setDate(Date value) {
		put("date", value);
	}
	public Date getDate() {
		return getDate("date");
	}

	public void setLocation(ParseGeoPoint value) {
		put("location", value);
	}
	public static ParseQuery<ProgrammePost> getQuery() {
		return ParseQuery.getQuery(ProgrammePost.class);
	}
}
