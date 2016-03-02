package pira.stellerin.com.projectinvite.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Utils.CircleTransform;

/**
 * Created by pira on 04/11/15.
 */
public class NotifAdapter extends ArrayAdapter<ParseObject> {
    Context context;
    int layoutResourceId;
    List<ParseObject> notifs=null;
    UserHolder holder=null;

    public NotifAdapter(Context context,int layoutResourceId,
                               List<ParseObject>notifs){
        super(context, layoutResourceId, notifs);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.notifs=notifs;


    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View row=convertView;

        if(row==null){
            LayoutInflater inflater=(LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layoutResourceId,parent,false);

            holder=new UserHolder();
            holder.accept = (Button) row.findViewById(R.id.accept);
            holder.refus = (Button) row.findViewById(R.id.refus);
            holder.txtName = (TextView) row.findViewById(R.id.name);
            holder.txtprogram = (TextView) row.findViewById(R.id.program_title);
            holder.image_profil =(ImageView) row.findViewById(R.id.picprofil);
            row.setTag(holder);
        }else{
            holder=(UserHolder)row.getTag();
        }

        final ParseObject notif=notifs.get(position);
        final ParseObject program = notif.getParseObject("program");
        getUser(program.getParseUser("user"));
        holder.txtprogram.setText(program.getString("titre"));
        Picasso.with(context).load("https://graph.facebook.com/" +program.getParseUser("user").get("fbId") + "/picture??width=150&height=150")
                .placeholder(R.drawable.loading_spinner)
                .transform(new CircleTransform())
                .into(holder.image_profil);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseQuery parseQuery = new ParseQuery("Programmes");
                parseQuery.getInBackground(program.getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        ParseRelation parseRelation = parseObject.getRelation("friendaccept");
                        parseRelation.add(notif);
                        parseObject.saveInBackground();
                        ParseQuery parseQuery1 = ParseQuery.getQuery("Notification");
                        parseQuery1.getInBackground(notif.getObjectId(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                parseObject.put("confirm", true);
                                parseObject.saveInBackground();
                            }
                        });
                    }


                });

            }
        });

      holder.refus.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              ParseQuery parseQuery1 = ParseQuery.getQuery("Notification");
              parseQuery1.getInBackground(notif.getObjectId(), new GetCallback<ParseObject>() {
                  @Override
                  public void done(ParseObject parseObject, ParseException e) {
                      if(e!=null)
                          Log.d("erreur", e.getMessage());
                      parseObject.put("refus", true);
                      parseObject.saveInBackground();
                  }
              });
          }
      });
        return row;
    }

    static class UserHolder {

        TextView txtName,txtprogram;
        ImageView image_profil;
        Button accept,refus;
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
