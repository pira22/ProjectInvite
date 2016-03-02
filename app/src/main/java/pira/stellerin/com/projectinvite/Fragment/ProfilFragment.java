package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.novoda.merlin.MerlinsBeard;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;
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
import pira.stellerin.com.projectinvite.Adapter.ProgrammeAdapter;
import pira.stellerin.com.projectinvite.Adapter.taginprofileAdapter;
import pira.stellerin.com.projectinvite.Entity.Programme;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Shared.Shared;
import pira.stellerin.com.projectinvite.Utils.CircleTransform;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment implements
        View.OnClickListener, OnItemClickListener {
        ImageView profilimage;
        ParallaxListView  list;
        private List<Programme> programmelist = null;
        ProgrammeAdapter adapter;
        taginprofileAdapter adaptertag;
        List<ParseObject> ob;
        ParseUser user;
        String objectId, name;
        ImageView imageSettingProg, imageSettingTag;
        TextView fbName;
        private ProgressWheel progressWheel;
        MerlinsBeard merlinsBeard;
        Boolean clickprogramme = true;
        Context context ;
        View empty;

        public static ProfilFragment newInstance() {
            ProfilFragment pf = new ProfilFragment();
            return pf;
        }

        public ProfilFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_profil, container, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            merlinsBeard = MerlinsBeard.from(getActivity());
            ((AppCompatActivity)  getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if(ActivityContainer.title)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.Profils_user);

            list = (ParallaxListView) getView().findViewById(R.id.listparalax);

            list.setOnItemClickListener(ProfilFragment.this);

            View view = getLayoutInflater(savedInstanceState).inflate(R.layout.header_list, list, false);
            list.setParallaxView(view);
            profilimage = (ImageView) view.findViewById(
                    R.id.userProfilePictureprofil);
            fbName = (TextView) view.findViewById(R.id.fbName);
            getUser(ParseUser.getCurrentUser());
            fbName.setText(name);
            imageSettingProg = (ImageView) view.findViewById(
                    R.id.imageView1);
            imageSettingProg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (merlinsBeard.isConnected()) {
                        clickprogramme = true;
                        new RemoteDataTask("Programme").execute();
                    } else {
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
                        clickprogramme = false;
                        new RemoteDataTask("Tag").execute();
                    } else {
                        final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                        Snackbar
                                .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                                .show(); // Don’t forget to show!
                    }

                }
            });
           /* progressWheel = (ProgressWheel) getActivity().findViewById(R.id.progress_wheel);
            progressWheel.setVisibility(View.GONE);*/
            if (merlinsBeard.isConnected()) {
                new RemoteDataTask("Programme").execute();
            } else {
                final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                Snackbar
                        .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                        .show(); // Don’t forget to show!
            }


            super.onActivityCreated(savedInstanceState);
        }

        public void getUser(ParseUser user) {
            objectId = user.getObjectId();
            JSONObject profile = user.getJSONObject("profile");
            String facebookId = null;
            try {
                facebookId = profile.getString("facebookId");
                name = profile.getString("username");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            getPicFb(facebookId);

        }

        @Override
        public void onClick(View v) {

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


               /* MainDetailProgramme mainDetailProgramme = new MainDetailProgramme();
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                mainDetailProgramme.setArguments(bundle);
                transaction.replace(R.id.root_profil, mainDetailProgramme,"profil");
                transaction.addToBackStack(null);
                transaction.commit();*/
                    MainDetailProfilFragment mainDetailProgramme = new MainDetailProfilFragment();
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    mainDetailProgramme.setArguments(bundle);
                    transaction.replace(R.id.root_profil, mainDetailProgramme, "profildetailmain");
                    transaction.commit();
                }
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            context = getActivity();
            Shared.listPicProfile = new ArrayList<String>();
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            try {
                menu.findItem(101).setVisible(false);
                menu.findItem(150).setVisible(false);
            }catch (NullPointerException e){

            }
        }

        private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
            String classe;

            public RemoteDataTask(String classe) {
                this.classe = classe;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressWheel.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                programmelist = new ArrayList<Programme>();
                if (classe.equals("Programme")) {

                    try {
                        // Locate the class table named "Country" in Parse.com
                        ParseQuery<ParseUser> users = ParseUser.getQuery();

                        users.whereEqualTo("objectId", objectId);
                        ParseQuery<ParseObject> queryy = new ParseQuery<ParseObject>(
                                "Programmes");
                            // Locate the column named "ranknum" in Parse.com and order
                            // list
                            // by ascending
                            queryy.whereMatchesQuery("user", users);
                            queryy.include("user");
                            queryy.setLimit(20);
                            queryy.addDescendingOrder("createdAt");
                        ob = queryy.find();

                        for (ParseObject programme : ob) {
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
                    ParseQuery<ParseUser> users = ParseUser.getQuery();

                    users.whereEqualTo("objectId", objectId);
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("TagIn");
                    query.whereMatchesQuery("user", users);
                    query.include("user");
                    query.addDescendingOrder("createdAt");
                    query.setLimit(20);
                    //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    try {

                        ob = query.find();

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
                        //  progressWheel.setVisibility(View.GONE);
                        list.setAdapter(null);

                    } else {
                        adapter = new ProgrammeAdapter(context,
                                R.layout.item_programme, programmelist, false);
                        list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //  progressWheel.setVisibility(View.GONE);

                    }
                }
                if (classe.equals("Tag")) {
                    if (ob.isEmpty()) {
                        //  progressWheel.setVisibility(View.GONE);
                      list.setAdapter(null);
                    } else {
                        adaptertag = new taginprofileAdapter(context,
                                R.layout.item_tagin, ob);
                        adaptertag.notifyDataSetChanged();
                        list.setAdapter(adaptertag);
                        //   progressWheel.setVisibility(View.GONE);

                    }
                }
                // Close the progressdialog

            }
        }


        public void getPicFb(String fbId) {
            Picasso.with(context).load("https://graph.facebook.com/" + fbId + "/picture??width=250&height=250")
                    .placeholder(R.drawable.loading_spinner)
                    .transform(new CircleTransform())
                    .into(profilimage);
        }

}

