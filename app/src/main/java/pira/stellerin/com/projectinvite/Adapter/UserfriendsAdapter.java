package pira.stellerin.com.projectinvite.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pira.stellerin.com.projectinvite.R;

/**
 * Created by pira on 20/10/15.
 */
public class UserfriendsAdapter extends ArrayAdapter<ParseUser> {

    Context context;
    int layoutResourceId;
    List<ParseUser> user = null;
    UserHolder holder = null;
    public UserfriendsAdapter(Context context, int layoutResourceId,
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
            holder.txtName = (TextView) row.findViewById(R.id.name_user_friend);
            holder.image = (ProfilePictureView) row
                    .findViewById(R.id.img_user_small_friend);
            holder.button =(Button) row.findViewById(R.id.buttonsend);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        if(!find){
            final ParseUser friend = user.get(position);
            getUser(friend);
            holder.image.setProfileId(friend.getString("fbId"));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("====>clickB", "=====>clickbutton");
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.getInBackground(friend.getObjectId(),new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            Log.d("====>clickB","=====>clickbutton"+user.getObjectId());
                            ParseUser usercu = ParseUser.getCurrentUser();
                            ParseRelation relation = usercu.getRelation("friends");
                            relation.add(user);
                            usercu.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e!=null)
                                        Log.d("====>clickBerror","=====>clickbutton"+e);
                                }
                            });
                        }
                    });

                }


            });
        }

        return row;
    }

    static class UserHolder {
        //ImageButton addbtn;
        TextView txtName;
        ProfilePictureView image;
        Button button;
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
