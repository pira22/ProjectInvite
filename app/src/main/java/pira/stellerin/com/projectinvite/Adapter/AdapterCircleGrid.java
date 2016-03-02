package pira.stellerin.com.projectinvite.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Utils.CircleTransform;

/**
 * Created by pira on 03/11/15.
 */
public class AdapterCircleGrid extends ArrayAdapter<ParseObject> {

    Context context;
    int layoutResourceId;
    List<ParseObject> user = null;
    UserHolder holder = null;
    public AdapterCircleGrid(Context context, int layoutResourceId,
                              List<ParseObject> user) {
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
            holder.imageView = (ImageView) row.findViewById(R.id.imageView4);

            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        if(!find) {
            final ParseObject friend = user.get(position);
            Picasso.with(context).load("https://graph.facebook.com/" + friend.getParseUser("user").get("fbId") + "/picture??width=100&height=100")
                    .placeholder(R.drawable.loading_spinner)
                    .transform(new CircleTransform())
                    .into(holder.imageView);
        }
        return row;
    }

    static class UserHolder {
        //ImageButton addbtn;
        ImageView imageView;
    }

}
