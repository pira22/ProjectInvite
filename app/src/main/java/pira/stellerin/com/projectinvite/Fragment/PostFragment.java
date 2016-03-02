package pira.stellerin.com.projectinvite.Fragment;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.dd.CircularProgressButton;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import me.drakeet.materialdialog.MaterialDialog;
import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.PlaceAutocompleteAdapter;
import pira.stellerin.com.projectinvite.Adapter.ProgramFriendAdapter;
import pira.stellerin.com.projectinvite.Entity.PlacesAuto;
import pira.stellerin.com.projectinvite.Entity.ProgrammePost;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Utils.ConnectivityOnline;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private EditText postEditText, titre;
    private ParseGeoPoint geoPoint;
    Calendar calender = Calendar.getInstance();
    EditText textdate, texttemp;
    MaterialAutoCompleteTextView  locationtext;
    SuperActivityToast superActivityToast;
    CircularProgressButton circularProgressButton;
    PlaceAutocompleteAdapter adapter;
    private ProgramFriendAdapter adapteruser;
    ListView listView;
    MaterialDialog mMaterialDialog;
    Context context;
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RootProgrammeFragment.menuitem ="zdzd";
        ActivityContainer.DETECT = "maindetail";
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AppCompatActivity)  activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        RootProgrammeFragment.menuitem = "child";
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.post);
        postEditText = (EditText) getActivity().findViewById(R.id.post_edittext);
        titre = (EditText) getActivity().findViewById(R.id.editText1);
        texttemp = (EditText) getActivity().findViewById(R.id.editText3);
        texttemp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                settemp();

            }
        });
        locationtext = (MaterialAutoCompleteTextView) getActivity().findViewById(R.id.location);
        locationtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GetPlaces task = new GetPlaces();
                String check = locationtext.getText().toString();
                if (check.trim().isEmpty()) {

                } else {
                    // now pass the argument in the textview to the task
                    task.execute(check);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        locationtext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlacesAuto place = (PlacesAuto) parent.getAdapter().getItem(position);
                locationtext.setText(place.getDescription());
                Getlanlag lntlag = new Getlanlag();
                lntlag.execute(place.getPlaceId());
            }
        });

        textdate = (EditText) getActivity().findViewById(R.id.editText2);
        textdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setdate();

            }
        });

        circularProgressButton = (CircularProgressButton) getActivity().findViewById(R.id.post_button);
        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (textdate.getText().toString().equals("")
                        || texttemp.getText().toString().equals("")
                        || titre.getText().toString().equals("")
                        || postEditText.getText().toString().equals("")
                        ) {

                    circularProgressButton.setProgress(-1);

                    Crouton.makeText(
                            getActivity(),
                            getActivity().getResources().getString(
                                    R.string.Alert), Style.ALERT).show();
                    circularProgressButton.getErrorText();
                } else {
                    if (geoPoint == null) {
                        Crouton.makeText(
                                getActivity(),
                                getActivity().getResources().getString(
                                        R.string.placesAlert), Style.ALERT).show();
                    } else {
                        if (ConnectivityOnline.isConnectedMobile(getActivity())
                                || ConnectivityOnline
                                .isConnectedWifi(getActivity())) {
                            if (circularProgressButton.getProgress() == 0) {
                                post();
                                simulateSuccessProgress(circularProgressButton);
                                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                getActivity().onBackPressed();
                            } else {
                                circularProgressButton.setProgress(0);
                            }
                        }

                    else{
                        if (circularProgressButton.getProgress() == 0) {
                            simulateErrorProgress(circularProgressButton);
                            superActivityToast = new SuperActivityToast(
                                    getActivity(), SuperToast.Type.STANDARD);
                            superActivityToast
                                    .setText(getString(R.string.connection));
                            superActivityToast
                                    .setAnimations(SuperToast.Animations.FADE);
                            superActivityToast
                                    .setDuration(SuperToast.Duration.LONG);
                            superActivityToast
                                    .setBackground(SuperToast.Background.RED);
                            superActivityToast
                                    .setTextSize(SuperToast.TextSize.MEDIUM);
                            superActivityToast.setIcon(SuperToast.Icon.Dark.INFO,
                                    SuperToast.IconPosition.LEFT);
                            superActivityToast.show();
                        } else {
                            circularProgressButton.setProgress(0);
                        }


                    }
                }

                }

            }
        });



    }


    @Override
    public void onResume() {
        super.onResume();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calender.set(Calendar.HOUR, hourOfDay);
            calender.set(Calendar.HOUR, minute);
            texttemp.setText(hourOfDay + ":" + minute);
        }
    };
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            textdate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        }
    };

    public void settemp() {
        TimePickerDialog td = new TimePickerDialog(getActivity(), t,
                Calendar.HOUR, Calendar.MINUTE, true);
        td.show();

    }

    public void setdate() {
        int mYear = calender.get(Calendar.YEAR);
        int mMonth = calender.get(Calendar.MONTH);
        int mDay = calender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepicker = new DatePickerDialog(getActivity(), d,
                mYear, mMonth, mDay);
        datepicker.show();

    }

    private void post() {
        context = getActivity();
        listView = new ListView(getActivity());
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        mMaterialDialog = new MaterialDialog(getActivity())
                .setTitle("Invit friends")
                .setView(listView)
                .setPositiveButton("SKIP", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();

                    }
                });
        String text = postEditText.getText().toString().trim();
        String titree = titre.getText().toString().trim();
        String d = textdate.getText() + " " + texttemp.getText();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-mm-dd hh:mm");
        Date b = null;
        try {
            b = simpleDateFormat.parse(d);
        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        // Create a post.
        ProgrammePost post = new ProgrammePost();

        post.setLocation(geoPoint);
        post.setDescription(text);
        post.setText(titree);
        post.setDate(b);
        post.setUser(ParseUser.getCurrentUser());
        post.setPlace(locationtext.getText().toString());
        ParseACL acl = new ParseACL();

        // Give public read access
        acl.setPublicReadAccess(true);
        post.setACL(acl);

        // Save the post
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e!=null) {
                    Log.d("erreur",e.getMessage().toString());
                }else{
                  /*  superActivityToast = new SuperActivityToast(getActivity(),
                            SuperToast.Type.STANDARD);
                    superActivityToast.setText("Enregistrer");
                    superActivityToast.setAnimations(SuperToast.Animations.FADE);
                    superActivityToast.setDuration(SuperToast.Duration.LONG);
                    superActivityToast.setBackground(SuperToast.Background.BLUE);
                    superActivityToast.setTextSize(SuperToast.TextSize.MEDIUM);
                    superActivityToast.setIcon(SuperToast.Icon.Dark.SAVE,
                            SuperToast.IconPosition.LEFT);
                    superActivityToast.show();*/
                    findfriendrequest();

                }


            }
        });
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
                        Log.d("users parselist", users.get(0).getObjectId());
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
    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    private void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
                if (value == 99) {
                    button.setProgress(-1);
                }
            }
        });
        widthAnimation.start();
    }


    // get the list of predictions in an asynctask
    class GetPlaces extends AsyncTask<String, Void, List<PlacesAuto>> {

        @Override
        // three dots is java for an array of strings
        protected List<PlacesAuto> doInBackground(String... args) {
            List<PlacesAuto> predictionsArr = new ArrayList<PlacesAuto>();

            try {
                URL googlePlaces = new URL(
                        // URLEncoder.encode(url,"UTF-8");
                        "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                                + URLEncoder.encode(args[0].toString(),
                                "UTF-8")
                                + "&radius=5&language="+ Locale.getDefault().getDisplayLanguage()+"&sensor=true&radius=2000&key=AIzaSyAD-HchmZbL13GYq5uVcqVSt15ON9MKSZE");

                URLConnection tc = googlePlaces.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(tc.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                // take Google's legible JSON and turn it into one big
                // string.
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                // turn that string into a JSON object
                JSONObject predictions = new JSONObject(sb.toString());
                // now get the JSON array that's inside that object
                JSONArray ja = new JSONArray(predictions
                        .getString("predictions"));

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    // add each entry to our array
                    PlacesAuto placesAuto = new PlacesAuto();
                    placesAuto.setDescription(jo.getString("description"));
                    placesAuto.setPlaceId(jo.getString("place_id"));
                    Log.d("places", jo.getString("description"));
                    predictionsArr.add(placesAuto);
                }
            } catch (IOException e) {

            } catch (JSONException e) {

            }
            // return all the predictions based on the typing the in the
            // search
            return predictionsArr;

        }

        // then our post

        @Override
        protected void onPostExecute(List<PlacesAuto> result) {

            // update the adapter
            adapter = new PlaceAutocompleteAdapter(getActivity().getBaseContext(),R.layout.list_item,result);// show the list view as texts
            // attach the adapter to listView
            locationtext.setAdapter(adapter);
            locationtext.showDropDown();
			/*for (String string : result) {
				adapter.add(string);
				adapter.notifyDataSetChanged();

			}*/

        }

    }

    class Getlanlag extends AsyncTask<String, Void, String> {

        @Override
        // three dots is java for an array of strings
        protected String doInBackground(String... args) {


            try {
                URL googlePlaces = new URL(
                        // URLEncoder.encode(url,"UTF-8");
                        "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + args[0]
                                + "&key=AIzaSyAD-HchmZbL13GYq5uVcqVSt15ON9MKSZE");

                URLConnection tc = googlePlaces.openConnection();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(tc.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                // take Google's legible JSON and turn it into one big
                // string.
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                // turn that string into a JSON object
                JSONObject output = new JSONObject(sb.toString());
                JSONObject result = output.getJSONObject("result");
                // now get the JSON array that's inside that object
                JSONObject ja = result.getJSONObject("geometry");
                Log.d("parse geometry",ja.toString());
                JSONObject location = ja.getJSONObject("location");
                Log.d("parse location",location.toString());
                return location.getString("lat") + "-" + location.getString("lng");
            } catch (IOException e) {

            } catch (JSONException e) {

            }
            // return all the predictions based on the typing the in the
            // search

            return null;
        }

        // then our post

        @Override
        protected void onPostExecute(String result) {
            String[] mot=result.split("-");
            geoPoint = new ParseGeoPoint(Float.parseFloat(mot[0]), Float.parseFloat(mot[1]));
            Log.d("geopoint",geoPoint.toString());
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
}
