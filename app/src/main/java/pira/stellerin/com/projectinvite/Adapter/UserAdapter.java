package pira.stellerin.com.projectinvite.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import pira.stellerin.com.projectinvite.R;

public class UserAdapter extends ArrayAdapter<ParseUser> {

	Context context;
	int layoutResourceId;
	List<ParseUser> user = null;
	UserHolder holder = null;
	public UserAdapter(Context context, int layoutResourceId,
					   List<ParseUser> user) {
		super(context, layoutResourceId, user);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.user = user;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		final int p = position;
		Boolean find = false;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new UserHolder();
			holder.txtName = (TextView) row.findViewById(R.id.name_user);
			holder.image = (ProfilePictureView) row
					.findViewById(R.id.img_user_small);
			row.setTag(holder);
		} else {
			holder = (UserHolder) row.getTag();
		}


		

				

	
		
		if(!find){
			final ParseUser friend = user.get(position);
			getUser(friend);
			holder.image.setProfileId(friend.getString("fbId"));

		}

		return row;
	}

	static class UserHolder {
		//ImageButton addbtn;
		TextView txtName;
		ProfilePictureView image;
	}

	public void getUser(ParseUser user) {
		JSONObject profile = user.getJSONObject("profile");

		try {

			holder.txtName.setText(profile.getString("username"));

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
