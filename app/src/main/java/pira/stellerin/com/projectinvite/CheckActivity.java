package pira.stellerin.com.projectinvite;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.novoda.merlin.MerlinsBeard;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;
import pira.stellerin.com.projectinvite.Adapter.PlacesAdapter;
import pira.stellerin.com.projectinvite.Entity.Place;
import pira.stellerin.com.projectinvite.Entity.PlacesService;
import pira.stellerin.com.projectinvite.Entity.TagIn;

public class CheckActivity extends AppCompatActivity implements OnItemClickListener,
       OnClickListener {
    private LocationManager locationManager;
    private Location loc;
    ListView places;
    ArrayList<Place> findPlaces;
    PlacesAdapter adapter;
    private ParseGeoPoint geoPoint;
    FloatingActionButton btnadd;
    SuperActivityToast superActivityToast;
    ProgressWheel progress;
    MerlinsBeard merlinsBeard;
    EditText placeadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ActivityContainer.title = false;
        places = (ListView) findViewById(R.id.listplace);
        btnadd = (FloatingActionButton) findViewById(R.id.btnaddplace);
        progress = (ProgressWheel) findViewById(R.id.progresscercle);
        progress.setVisibility(View.GONE);
        places.setOnItemClickListener(this);
        merlinsBeard =  MerlinsBeard.from(this.getBaseContext());
        if (merlinsBeard.isConnected()) {
            currentLocation();
            btnadd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    final View view = LayoutInflater.from(CheckActivity.this).inflate(R.layout.addplacedialog, null);
                    final MaterialDialog mMaterialDialog = new MaterialDialog(CheckActivity.this);
                    mMaterialDialog.setMessage("Add Place")
                            .setContentView(view)
                            .setNegativeButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            })
                            .setPositiveButton(
                                    getResources().getString(R.string.add_place), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            placeadd = (EditText) view.findViewById(R.id.addplacedialog);
                                            if(placeadd.getText().equals("")){
                                                superActivityToast = new SuperActivityToast(CheckActivity.this,
                                                        SuperToast.Type.STANDARD);
                                                superActivityToast.setText("texte vide");
                                                superActivityToast.setAnimations(SuperToast.Animations.FADE);
                                                superActivityToast.setDuration(SuperToast.Duration.LONG);
                                                superActivityToast.setBackground(SuperToast.Background.RED);
                                                superActivityToast.setTextSize(SuperToast.TextSize.MEDIUM);
                                                superActivityToast.setIcon(SuperToast.Icon.Dark.UNDO,
                                                        SuperToast.IconPosition.LEFT);
                                                superActivityToast.show();
                                            }else {
                                                mMaterialDialog.dismiss();
                                                Log.d("placeTag", loc.toString() + " " + placeadd.getText() + "null");
                                                new checkin(CheckActivity.this, loc
                                                        .getLongitude(), loc.getLatitude(),
                                                        placeadd.getText() + "").execute();
                                            }

                                        }
                                    })

                            .show();
                }
            });

        }else{
            progress.setVisibility(View.VISIBLE);
            btnadd.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

    }


    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {


        private Context context;

        public GetPlaces(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);
            adapter = new PlacesAdapter(getBaseContext(), R.layout.item_places,
                    result);

            places.setAdapter(adapter);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Place> doInBackground(Void... arg0) {
            PlacesService service = new PlacesService(
                    "AIzaSyAD-HchmZbL13GYq5uVcqVSt15ON9MKSZE");
            findPlaces = service.findPlaces(loc.getLatitude(), // 28.632808
                    loc.getLongitude()); // 77.218276

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                Log.e("test", "places : " + placeDetail.getName());
            }
            return findPlaces;
        }

    }

    private void currentLocation() {
        Location location = null;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

        loc = location;
        if(loc != null) {
            new GetPlaces(CheckActivity.this).execute();
        }else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    CheckActivity.this);

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
                            startActivity(intent);
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
        }
        Log.e("test", "location : " + location);

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
            locationManager.removeUpdates(listener);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Place placeDetail = findPlaces.get(position);
        new checkin(CheckActivity.this, placeDetail.getLongitude(),
                placeDetail.getLatitude(), placeDetail.getName()).execute();
        superActivityToast = new SuperActivityToast(
                this, SuperToast.Type.STANDARD);
        superActivityToast
                .setText("Place Checked");
        superActivityToast
                .setAnimations(SuperToast.Animations.FADE);
        superActivityToast
                .setDuration(SuperToast.Duration.LONG);
        superActivityToast
                .setBackground(SuperToast.Background.GREEN);
        superActivityToast
                .setTextSize(SuperToast.TextSize.MEDIUM);
        superActivityToast.setIcon(SuperToast.Icon.Dark.SHARE,
                SuperToast.IconPosition.LEFT);
        superActivityToast.show();

    }

    private class checkin extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private Context context;
        private Double lan, alt;
        private String namelocation;

        public checkin(Context context, Double lan, Double alt,
                       String namelocation) {
            this.context = context;
            this.lan = lan;
            this.alt = alt;
            this.namelocation = namelocation;
        }



        @Override
        protected Void doInBackground(Void... params) {
            Log.e("pirachecktest", namelocation);
            TagIn tag = new TagIn();
            ParseUser user = ParseUser.getCurrentUser();
            tag.setUser(user);
            Log.e("pirachecktest", user.toString());
            geoPoint = new ParseGeoPoint(alt, lan);
            tag.setLocation(geoPoint);
            tag.setLocationName(namelocation);
            ParseACL acl = new ParseACL();
            acl.setPublicReadAccess(true);
            tag.setACL(acl);
            tag.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d("ParseSave", e.getMessage());

                    }

                }
            });
            user.put("locationName", namelocation);
            user.put("location", geoPoint);
            user.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d("ParseSave", e.getMessage());

                    }

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            superActivityToast = new SuperActivityToast(CheckActivity.this,
                    SuperToast.Type.STANDARD);
            superActivityToast.setText("Enregistrer");
            superActivityToast.setAnimations(SuperToast.Animations.FADE);
            superActivityToast.setDuration(SuperToast.Duration.LONG);
            superActivityToast.setBackground(SuperToast.Background.BLUE);
            superActivityToast.setTextSize(SuperToast.TextSize.MEDIUM);
            superActivityToast.setIcon(SuperToast.Icon.Dark.SAVE,
                    SuperToast.IconPosition.LEFT);
            superActivityToast.show();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

    }


}
