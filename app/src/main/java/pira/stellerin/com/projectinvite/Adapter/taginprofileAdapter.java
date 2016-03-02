package pira.stellerin.com.projectinvite.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.List;

import pira.stellerin.com.projectinvite.R;

public class taginprofileAdapter extends ArrayAdapter<ParseObject> {
	Context context;
	int layoutResourceId;
	List<ParseObject> tag = null;
	

	public taginprofileAdapter(Context context, int layoutResourceId,
							   List<ParseObject> tags) {
		super(context, layoutResourceId, tags);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.tag = tags;
		

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		UserHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new UserHolder();
			holder.txtName = (TextView) row.findViewById(R.id.textView1tag);
		
			holder.image = (ProfilePictureView) row
					.findViewById(R.id.img_user_smalltag);
			row.setTag(holder);
		} else {
			holder = (UserHolder) row.getTag();
		}

		ParseObject places = tag.get(position);
		ParseGeoPoint geopoint = (ParseGeoPoint) places.get("location");
		
		
		Log.d("pirafacebook", places.get("user").toString());
		ParseUser user = (ParseUser) places.get("user");

		JSONObject jsonobject = user.getJSONObject("profile");
		
		Log.d("pirafacebook", jsonobject.optString("facebookId"));
		holder.image.setProfileId(jsonobject.optString("facebookId"));
		String nom = (String) places.get("locationName");
		holder.txtName.setText("@"+nom);
		
		
		return row;
	}

	static class UserHolder {
		
		TextView txtName;
		ProfilePictureView image;
	}
}
