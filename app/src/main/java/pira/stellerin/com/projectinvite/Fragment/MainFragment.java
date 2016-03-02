package pira.stellerin.com.projectinvite.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.novoda.merlin.MerlinsBeard;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardGridView;
import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.CheckActivity;
import pira.stellerin.com.projectinvite.R;


public class MainFragment extends Fragment implements
        View.OnClickListener {
    private LocationManager locationManager;
    private Location loc;
    private FloatingActionButton checkin;
    CardGridView listView;
    MerlinsBeard merlinsBeard;
    ArrayList<Card> cards;

    public static MainFragment NewInstance() {
        MainFragment mf = new MainFragment();
        return mf;
    }

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.Main);
        merlinsBeard = MerlinsBeard.from(getActivity());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkin = (FloatingActionButton) view.findViewById(R.id.buttonFloat);
                checkin.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CheckActivity.class);
                        startActivity(intent);
                    }
                });
                listView = (CardGridView) view.findViewById(R.id.carddemo_grid_base1);
            }
        });
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cards.get(i).
            }
        });*/
        if (merlinsBeard.isConnected()) {

            /***********Get Location and if is enabled get to setting ******/
            GetLocation();
            /**************CardsGridView & adapter &  queryfromparse ********/
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            Date d1 = c.getTime();

            ParseGeoPoint geopoint = null;
            if (loc != null) {
                geopoint = new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
            }


        } else {
            // Disconnected, do something!
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void GetLocation() {
        Location location = null;

            locationManager = (LocationManager) getActivity().getSystemService(
                    Context.LOCATION_SERVICE);
       // try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }


            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location == null
                    && locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, listener);

            if (location == null
                    && locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0, listener);
       /* }catch (IllegalArgumentException e){

        }*/
        loc = location;

       /* if (loc == null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    getActivity());

            // Setting Dialog Title
            alertDialog.setTitle(getString(R.string.settings));

            // Setting Dialog Message
            alertDialog.setMessage(getString(R.string.settingsGps));

            // Setting Icon to Dialog
            // alertDialog.setIcon(R.drawable.delete);

            // On pressing Settings button
            alertDialog.setPositiveButton(getString(R.string.settings),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            getActivity().startActivity(intent);
                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton(getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        }else {*/
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GetCheckIn();
                }
            });
            final Handler handler = new Handler();

            Runnable refresh = new Runnable() {
                @Override
                public void run() {
                    Log.d("handler_check","msg msg");
                    if (merlinsBeard.isConnected()) {
                        GetCheckIn();
                        handler.postDelayed(this, 5000);
                    }
                }
            };

            handler.postDelayed(refresh, 3*60*1000);
       // }
    }

    public void GetCheckIn() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseRelation<ParseUser> parseRelation = parseUser.getRelation("friends");
        ParseQuery<ParseUser> query = parseRelation.getQuery();
        query.whereNotEqualTo("locationName", "");
        //query.whereNotEqualTo("objectId", ParseUser.getCurrentUser()
        // .getObjectId());
        // query.whereWithinKilometers("location", geopoint, 2);
        query.whereNotEqualTo("locationName", null);
        // query.whereGreaterThan("updatedAt", d1);
         query.addDescendingOrder("updatedAt");
         query.setLimit(20);
        //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.d("List", "Nbre: " + objects.size());
                    cards = new ArrayList<Card>();
                    Log.d("size List", objects.size() + " ");
                    for (int i = 0; i < objects.size(); i++) {

                        GplayGridCard card = new GplayGridCard(getActivity());
                        ParseGeoPoint geopoint = (ParseGeoPoint) objects.get(i).get("location");
                        JSONObject jsonobject = objects.get(i).getJSONObject("profile");
                        Log.d("Pira", jsonobject.optString("facebookId") + "test");
                        String nom = jsonobject.optString("name").split(" ")[0];
                        card.headerTitle = "@" + objects.get(i).getString("locationName");
                        //int s = (int)getDistanceBetweenTwoPoints(new PointF((float) geopoint.getLatitude(), (float) geopoint.getLongitude()), new PointF((float) loc.getLatitude(), (float) loc.getLongitude()));
                        //card.secondaryTitle = s + " " + "m" ;
                        card.rating = nom;
                        card.facebooka = jsonobject.optString("facebookId");
                        card.objectId = objects.get(i).getObjectId();

                        card.init();
                        cards.add(card);
                    }
                    if(ParseUser.getCurrentUser().get("locationName")!=null) {
                        GplayGridCard card = new GplayGridCard(getActivity());
                        ParseGeoPoint geopoint = (ParseGeoPoint) ParseUser.getCurrentUser().get("location");
                        JSONObject jsonobject = ParseUser.getCurrentUser().getJSONObject("profile");
                        String nom = jsonobject.optString("name").split(" ")[0];
                        card.headerTitle = "@" + ParseUser.getCurrentUser().getString("locationName");
                        //int s = (int)getDistanceBetweenTwoPoints(new PointF((float) geopoint.getLatitude(), (float) geopoint.getLongitude()), new PointF((float) loc.getLatitude(), (float) loc.getLongitude()));
                        //card.secondaryTitle = s + " " + "m" ;
                        card.rating = nom;
                        card.facebooka = jsonobject.optString("facebookId");
                        card.objectId = ParseUser.getCurrentUser().getObjectId();

                        card.init();
                        cards.add(card);
                        CardGridArrayAdapter mCardArrayAdapter;
                        try {
                            mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cards);
                            if (listView != null) {
                                listView.setAdapter(mCardArrayAdapter);
                            }
                        } catch (NullPointerException e1) {

                        }
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e("test", "location update : " + location);
            loc = location;

            Log.d("location", loc.toString());
            locationManager.removeUpdates(listener);
        }
    };

    public class GplayGridCard extends Card {

        protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected String facebooka;
        protected String objectId;
        protected int count;

        protected String headerTitle;
        protected String secondaryTitle;
        protected String rating;

        public GplayGridCard(Context context) {
            super(context, R.layout.carddemo_gplay_inner_content);
        }

        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {
            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);
            addCardHeader(header);
            GplayGridThumb thumbnail = new GplayGridThumb(getContext());
            thumbnail.facebookId = facebooka;
            thumbnail.setExternalUsage(true);
            //  thumbnail.setExternalUsage(true);
            addCardThumbnail(thumbnail);
            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Log.d("===>click", facebooka);
                    ActivityContainer.pager.setCurrentItem(2);
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.root_friends, ProfilFreindsFragment.newInstance(facebooka), "profilfriend");
                    transaction.commit();
                }
            });
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
            title.setText(rating);

            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
            subtitle.setText(secondaryTitle);

        }



        class GplayGridThumb extends CardThumbnail {
            protected String facebookId;

            public GplayGridThumb(Context context) {
                super(context);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View viewImage) {
                Picasso.with(getContext())
                        .load("https://graph.facebook.com/" + facebooka + "/picture??width=250&height=250")
                        .placeholder(R.drawable.loading_spinner)
                        .into((ImageView) viewImage);
                //viewImage.getLayoutParams().width = 196;
                //viewImage.getLayoutParams().height = 196;

            }
        }

    }

    public static double getDistanceBetweenTwoPoints(PointF p1, PointF p2) {
        double R = 6371000; // Earth radius
        double dLat = Math.toRadians(p2.x - p1.x);
        double dLon = Math.toRadians(p2.y - p1.y);
        double lat1 = Math.toRadians(p1.x);
        double lat2 = Math.toRadians(p2.x);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
                * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }
}
