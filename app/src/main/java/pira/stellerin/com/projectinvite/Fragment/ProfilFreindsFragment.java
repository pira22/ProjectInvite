package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.novoda.merlin.MerlinsBeard;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.poliveira.apps.parallaxlistview.ParallaxListView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.FriendProfilProgrammeAdapter;
import pira.stellerin.com.projectinvite.Adapter.TagInprofileFriends;
import pira.stellerin.com.projectinvite.Entity.Programme;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Shared.Shared;
import pira.stellerin.com.projectinvite.Utils.CircleTransform;

/**
 * A simple {@link Fragment} subclass.
 */
public class    ProfilFreindsFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemClickListener {

    ImageView profilimage;
    ParallaxListView list;
    private List<Programme> programmelist = null;
    FriendProfilProgrammeAdapter adapter;
    TagInprofileFriends adaptertag;
    List<ParseObject> ob;
    String objectId, name;
    ImageView imageSettingProg, imageSettingTag;
    TextView fbName;
    MerlinsBeard merlinsBeard;
    ParseUser user;
    Boolean clickprogramme = true;
    public static  Bundle arguments;
    public static ProfilFreindsFragment newInstance(String fbId){
        ProfilFreindsFragment pf = new ProfilFreindsFragment();
        if (fbId != null) {
            arguments = new Bundle();
            arguments.putString("objectId", fbId);
            pf.setArguments(arguments);
        }
        return pf;
    }

    public ProfilFreindsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Shared.listFriendsPic = new ArrayList<String>();
        final Bundle bund = getArguments();
        objectId =  bund.getString("objectId");
        ActivityContainer.DETECT = "profilfriend";
        ParseQuery<ParseUser> userquery = ParseUser.getQuery();
        userquery.whereEqualTo("fbId", bund.getString("objectId"));
        //userquery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
        userquery.getFirstInBackground(new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser userobject, ParseException e) {

                user = userobject;
                Log.d("user", user.getObjectId());
                getUser(user);
                Log.d("Name user Fb", name);


            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil_freinds, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            merlinsBeard = MerlinsBeard.from(getActivity());
                    list = (ParallaxListView) getView().findViewById(R.id.listparalax);
            View view = getLayoutInflater(savedInstanceState).inflate(R.layout.header_list2, list, false);
            list.setParallaxView(view);
        list.setOnItemClickListener(ProfilFreindsFragment.this);
        profilimage = (ImageView) view.findViewById(
                R.id.userProfilePictureprofil);

        fbName = (TextView) view.findViewById(R.id.fbName);
            fbName.setText(name);
            getPicFb(objectId);
            imageSettingProg = (ImageView) view.findViewById(
                    R.id.imageView1);
            imageSettingProg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (merlinsBeard.isConnected()) {
                        new RemoteDataTask("Programme").execute();
                        clickprogramme = true;
                    }
                    else {
                        final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                        Snackbar
                                .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                                .show(); // Don’t forget to show!
                    }

                }
            });
            imageSettingTag = (ImageView) view.findViewById(
                    R.id.imageView2);
            imageSettingTag.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (merlinsBeard.isConnected()) {
                        new RemoteDataTask("Tag").execute();
                        clickprogramme = false;
                    }
                    else {
                        final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                        Snackbar
                                .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                                .show(); // Don’t forget to show!
                    }

                }
            });

            if (merlinsBeard.isConnected())
                new RemoteDataTask("Programme").execute();
            else {
                final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                Snackbar
                        .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                        .show(); // Don’t forget to show!
            }


            super.onActivityCreated(savedInstanceState);
    }

    public void getUser(ParseUser user) {
        JSONObject profile = user.getJSONObject("profile");

        try {

            name = profile.getString("username");

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {
            position -= list.getHeaderViewsCount();
            if (clickprogramme) {
                Bundle bundle = new Bundle();
                bundle.putString("objectId",
                        programmelist.get(position)
                                .getObjectId());
                bundle.putString("titre",
                        programmelist.get(position).getTitre());
                bundle.putString("Descritpion", programmelist
                        .get(position).getDescription());
                bundle.putDouble("longitude", programmelist
                        .get(position).getLongitude());
                bundle.putDouble("latitude",
                        programmelist.get(position)
                                .getLaltitude());
                final DateFormat FORMATTING_PATTERN = new SimpleDateFormat(
                        "yyyy-mm-dd");
                String outputDate = "";
                outputDate = FORMATTING_PATTERN.format(
                        programmelist.get(position).getDate())
                        .toString();
                bundle.putString("date", outputDate);
                bundle.putString("userid", programmelist.get(position)
                        .getUser().getObjectId());

            Log.d("user=====>",programmelist.get(position)
                    .getUser().getObjectId());
           /* MainDetailProgramme mainDetailProgramme = new MainDetailProgramme();
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();programmelist.get(position)
                        .getUser().getObjectId()
            mainDetailProgramme.setArguments(bundle);
            transaction.replace(R.id.root_profil, mainDetailProgramme,"profil");
            transaction.addToBackStack(null);
            transaction.commit();*/
                MainDetailFriendsFragment mainDetailProgramme = new MainDetailFriendsFragment();
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                mainDetailProgramme.setArguments(bundle);
                transaction.replace(R.id.root_friends, mainDetailProgramme, "profilfriendsdetailmain");
                transaction.commit();
            }
        }
    }


    @Override
    public void onClick(View v) {

    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        String classe;

        public RemoteDataTask(String classe) {
            this.classe = classe;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            ParseQuery<ParseUser> users = ParseUser.getQuery();

            users.whereEqualTo("fbId", objectId);
            users.addDescendingOrder("createdAt");
            if (classe.equals("Programme")) {
                programmelist = new ArrayList<Programme>();
                try {


                    ParseQuery<ParseObject> queryy = new ParseQuery<ParseObject>(
                            "Programmes");
                    // Locate the column named "ranknum" in Parse.com and order
                    // list
                    // by ascending
                    queryy.whereMatchesQuery("user", users);
                    queryy.include("user");
                    queryy.setLimit(20);
                    queryy.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    ob = queryy.find();
                    Log.d("doonback", "doInBackground program"+" "+ob.size()+" "+objectId);
                    for (ParseObject programme : ob) {
                        // Locate images in flag column
                        Log.d("test", (String) programme.get("description"));
                        ParseGeoPoint geopoint = (ParseGeoPoint) programme
                                .get("location");
                        Programme prog = new Programme();
                        prog.setObjectId(programme
                                .getObjectId());
                        prog.setDescription((String) programme
                                .get("description"));
                        prog.setDate((Date) programme.get("date"));
                        prog.setTitre((String) programme.get("titre"));
                        prog.setUser(programme.getParseUser("user"));
                        prog.setLaltitude(geopoint.getLatitude());
                        prog.setLongitude(geopoint.getLongitude());
                        programmelist.add(prog);

                    }

                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

            }
            if (classe.equals("Tag")) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("TagIn");
                query.whereMatchesQuery("user", users);
                query.include("user");
                query.addDescendingOrder("createdAt");
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.setLimit(20);

                try {

                    ob = query.find();
                    Log.d("doonback", "doInBackground " + " " + ob.size()+" "+objectId);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (classe.equals("Programme")) {
                if (programmelist.isEmpty()) {
                    list.setAdapter(null);

                } else {

                    adapter = new FriendProfilProgrammeAdapter(getActivity(),
                            R.layout.item_programme, programmelist, false);
                    adapter.notifyDataSetChanged();
                    list.setAdapter(adapter);
                }
            }
            if (classe.equals("Tag")) {
                if (ob.isEmpty()) {
                    list.setAdapter(null);
                } else {
                    adaptertag = new TagInprofileFriends(getActivity(),R.layout.item_tagin, ob);
                    adaptertag.notifyDataSetChanged();
                    list.setAdapter(adaptertag);
                }
            }

        }
    }


    public void getPicFb(String fbId) {
        Picasso.with(getActivity()).load("https://graph.facebook.com/" + fbId + "/picture??width=250&height=250")
                .placeholder(R.drawable.loading_spinner)
                .transform(new CircleTransform())
                .into(profilimage);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        try {
            MenuItem item = menu.findItem(4);
            item.setVisible(false);
        }catch (NullPointerException e){

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)activity).getSupportActionBar().setTitle(R.string.Profils_user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}


