package pira.stellerin.com.projectinvite.Entity;

import com.parse.ParseUser;

import java.util.Date;


/**
 * Data model for a post.
 */
/*@ParseClassName("programme")*/
public class Programme  {
	private String titre;
	private String objectId;
	private String place;

	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getImage() {
		return image;
	}
	public  void setPlace(String place){this.place = place;}
	public String getPlace() {
		return place;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private double laltitude;
	public double getLaltitude() {
		return laltitude;
	}
	public void setLaltitude(double laltitude) {
		this.laltitude = laltitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	private ParseUser user;
	public ParseUser getUser() {
		return user;
	}
	public void setUser(ParseUser user) {
		this.user = user;
	}
	private double longitude;
	private String image;
	private String description;
	
	/*public String getTitre() {
		return getString("titre");
	}
	public void setTitre(String value) {
		 put("titre", value);
	}
	public String getImage() {
		return getString("image");
	}
	public void setImage(String value) {
		 put("image", value);
	}
	public String getDescription() {
		return getString("description");
	}
	public void setDescription(String value) {
		 put("description", value);
	}
	public ParseUser getUser() {
	    return getParseUser("User_id");
	  }

	  public void setUser(ParseUser value) {
	    put("User_id", value);
	  }

	
	public ParseGeoPoint getLocation() {
	    return getParseGeoPoint("location");
	  }

	  public void setLocation(ParseGeoPoint value) {
	    put("location", value);
	  }

	  public static ParseQuery<Programme> getQuery() {
	    return ParseQuery.getQuery(Programme.class);
	  }*/

}
