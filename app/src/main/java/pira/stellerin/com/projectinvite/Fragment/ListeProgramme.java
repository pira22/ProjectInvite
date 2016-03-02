package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.novoda.merlin.MerlinsBeard;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.ProgramFriendAdapter;
import pira.stellerin.com.projectinvite.Adapter.ProgrammeAdapter;
import pira.stellerin.com.projectinvite.Adapter.UserAdapter;
import pira.stellerin.com.projectinvite.Entity.Programme;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Shared.Shared;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListeProgramme extends Fragment implements AdapterView.OnItemClickListener {
    public static  Boolean PROGRAM_EXIST = false;
    List<ParseObject> ob;
    ProgrammeAdapter adapter;
    public  List<Programme> programmelist = null;
    ListView list;
    ProgressWheel progressWheel;
    MerlinsBeard merlinsBeard;
    SwipeRefreshLayout layout;
    Boolean refresh = false;
    View view;
    ListView listView;
    MaterialDialog mMaterialDialog;
    private ProgramFriendAdapter adapteruser;
    Context context;
    public static ListeProgramme NewInstance() {
        ListeProgramme lp = new ListeProgramme();
        return lp;
    }


    public ListeProgramme() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity();
        Shared.listPic = new ArrayList<String>();
        Log.d("life cycle fragment", "oncreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("life cycle fragment","oncreateview");
        return inflater.inflate(R.layout.fragment_liste_programme, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ActivityContainer.title)
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.programme);
        merlinsBeard =  MerlinsBeard.from(getActivity());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list = (ListView) getView().findViewById(
                        android.R.id.list);
                progressWheel = (ProgressWheel) getView().findViewById(R.id.progress_wheel);
                layout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
                layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (merlinsBeard.isConnected()) {
                            refresh = true;
                            new RemoteDataTask().execute();
                        } else {
                            final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                            Snackbar
                                    .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                                    .show(); // Don’t forget to show!
                        }

                    }
                });


            }
        });
        view = getLayoutInflater(savedInstanceState).inflate(R.layout.empty_layout, list, false);
        // Configure the refreshing colors
        layout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        progressWheel.setVisibility(View.GONE);
        if (merlinsBeard.isConnected()) {

            if(Shared.listprog.isEmpty())
           new RemoteDataTask().execute();
            else {
                list.setOnItemClickListener(ListeProgramme.this);
                adapter = new ProgrammeAdapter(context,
                        R.layout.item_programme, Shared.listprog, true);
                list.setAdapter(adapter);

            }
        } else {
            final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
            Snackbar
                    .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                    .show(); // Don’t forget to show!
        }
        Log.d("life cycle fragment","onviewcreated");

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("life cycle fragment", "onactivitycreated");


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
                if(Shared.listprog.isEmpty()) {
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
                    bundle.putString("userid", programmelist.get(position)
                            .getUser().getObjectId());
                    final DateFormat FORMATTING_PATTERN = new SimpleDateFormat(
                            "yyyy-mm-dd");
                    String outputDate = "";
                    outputDate = FORMATTING_PATTERN.format(
                            programmelist.get(position).getDate())
                            .toString();
                    bundle.putString("date", outputDate);
                    bundle.putBoolean("exist", true);
                }else{
                    bundle.putString("objectId",
                            Shared.listprog.get(position)
                                    .getObjectId());
                    bundle.putString("titre",
                            Shared.listprog.get(position).getTitre());
                    bundle.putString("Descritpion", Shared.listprog
                            .get(position).getDescription());
                    bundle.putDouble("longitude", Shared.listprog
                            .get(position).getLongitude());
                    bundle.putDouble("latitude",
                            Shared.listprog.get(position)
                                    .getLaltitude());
                    bundle.putString("userid", Shared.listprog.get(position)
                            .getUser().getObjectId());
                    final DateFormat FORMATTING_PATTERN = new SimpleDateFormat(
                            "yyyy-mm-dd");
                    String outputDate = "";
                    outputDate = FORMATTING_PATTERN.format(
                            Shared.listprog.get(position).getDate())
                            .toString();
                    bundle.putString("date", outputDate);
                    bundle.putBoolean("exist", true);
                }
        MainDetailProgramme mainDetailProgramme = new MainDetailProgramme();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        mainDetailProgramme.setArguments(bundle);
        transaction.replace(R.id.root_prog, mainDetailProgramme, "mainDetailProgramme");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!refresh)
            progressWheel.setVisibility(View.VISIBLE);
            else {
                try {
                    if (refresh)
                        adapter.clear();
                } catch (NullPointerException e) {

                }
            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                programmelist = new ArrayList<Programme>();
                /*Date today = new Date();
                Calendar aujourdhui = Calendar.getInstance();
                aujourdhui.add(Calendar.DATE, 7);
                Date d = aujourdhui.getTime();*/
                ParseUser parseUser = ParseUser.getCurrentUser();
                ParseRelation<ParseUser> parseRelation = parseUser.getRelation("friends");
                ParseQuery<ParseUser> parseQuery = parseRelation.getQuery();
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Programmes");
                query.whereMatchesQuery("user", parseQuery);
                query.addDescendingOrder("createdAt");
                ////query.whereGreaterThan("date", today);
                //query.whereLessThan("date", d);
               // if(!refresh)
                //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
                query.include("user");
                boolean isInCache = query.hasCachedResult();
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                ob = query.find();
                for (ParseObject programme : ob) {
                    ParseGeoPoint geopoint = (ParseGeoPoint) programme
                            .get("location");
                    Programme prog = new Programme();
                    prog.setLaltitude(geopoint.getLatitude());
                    prog.setLongitude(geopoint.getLongitude());
                    prog.setDescription((String) programme.get("description"));
                    prog.setTitre((String) programme.get("titre"));
                    prog.setObjectId(programme.getObjectId());
                    prog.setDate(programme.getDate("date"));
                    prog.setUser(programme.getParseUser("user"));
                    prog.setPlace(programme.getString("place"));
                    programmelist.add(prog);
                    Log.d("programme", programmelist.toString());
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);


            if (programmelist.isEmpty()) {
               // list.addHeaderView(view);
              //  list.setAdapter(null);
                progressWheel.setVisibility(View.GONE);
            } else {
                Shared.listprog =programmelist;
                list.setOnItemClickListener(ListeProgramme.this);
                adapter = new ProgrammeAdapter(context,
                        R.layout.item_programme, programmelist, true);
                list.setAdapter(adapter);
            }
            progressWheel.setVisibility(View.GONE);
            if(refresh)
                layout.setRefreshing(false);
            refresh = false;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("life cycle fragment", "onpause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("life cycle fragment", "onResume");
        ActivityContainer.DISPLAY_POP = false;

        if(PROGRAM_EXIST) {
            RelativeLayout relativeLayout = new RelativeLayout(getActivity());
            listView = new ListView(getActivity());
            listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8 * scale + 0.5f);
            listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
            listView.setDividerHeight(0);
            relativeLayout.addView(listView);
            mMaterialDialog = new MaterialDialog(getActivity())
                    .setTitle("Invit friends")
                    .setView(relativeLayout)
                    .setPositiveButton("SKIP", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();

                        }
                    });


            findfriendrequest();
        }
    }

    private void findfriendrequest() {

        ParseQuery programme = ParseQuery.getQuery("Programmes");
        programme.whereEqualTo("user", ParseUser.getCurrentUser());
        programme.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                Log.d("users parse",parseObject.getObjectId() );
                ParseQuery parsenotification = ParseQuery.getQuery("Notification");
                parsenotification.include("user");
                parsenotification.whereEqualTo("program", parseObject);
                parsenotification.whereEqualTo("confirm", false);
                parsenotification.whereEqualTo("refus", false);
                ParseUser parseUser = ParseUser.getCurrentUser();
                ParseRelation<ParseUser> parseRelation = parseUser.getRelation("friends");
                ParseQuery<ParseUser> parseQuery = parseRelation.getQuery();
                parseQuery.whereDoesNotMatchKeyInQuery("objectId", "iduser", parsenotification);
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> users, ParseException e) {

                        adapteruser = new ProgramFriendAdapter(context,
                                R.layout.item_proram_friends, users, parseObject.getObjectId());
                        try {
                            listView.setAdapter(adapteruser);
                            mMaterialDialog.show();
                        } catch (NullPointerException ex) {

                        }
                    }
                });
            }


        });


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("life cycle fragment", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("life cycle fragment", "onDetach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("life cycle fragment", "onStart");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("life cycle fragment", "onHiddenChanged");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("life cycle fragment", "onStop");
    }

}
