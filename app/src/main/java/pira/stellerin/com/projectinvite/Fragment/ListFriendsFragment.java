package pira.stellerin.com.projectinvite.Fragment;


import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.novoda.merlin.MerlinsBeard;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.UserAdapter;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Shared.Shared;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFriendsFragment extends Fragment implements AdapterView.OnItemClickListener {

    UserAdapter adapter;
    List<GraphUser> user = new ArrayList<GraphUser>();
    MerlinsBeard merlinsBeard;
    String id;
    ListView list;
    ProgressWheel progressWheel;
    FloatingActionButton btnadd;
    View view1;
    List<ParseUser> parseUsers =new ArrayList<ParseUser>();
    public ListFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public static ListFriendsFragment NewInstance(){
        ListFriendsFragment listPicFragment= new ListFriendsFragment();
        return listPicFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ActivityContainer.title)
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.List_Friends);
        merlinsBeard =  MerlinsBeard.from(getActivity());

                list = (ListView) getView().findViewById(
                        R.id.listView);
                progressWheel = (ProgressWheel) getView().findViewById(R.id.progresscercle);



        list.setOnItemClickListener(this);
        progressWheel.setVisibility(View.VISIBLE);
        view1 = getLayoutInflater(savedInstanceState).inflate(R.layout.empty_layout,list,false);
        if (merlinsBeard.isConnected()) {
                findfriendrequest();

        } else {
            final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
            Snackbar
                    .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                    .show(); // Don’t forget to show!

        }
    }

    private void findfriendrequest() {

        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseRelation parseRelation = parseUser.getRelation("friends");
        ParseQuery<ParseUser> parseQuery = parseRelation.getQuery();
        parseQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                try {
                    if (users.isEmpty()) {
                        list.setAdapter(null);
                        list.addHeaderView(view1);
                        progressWheel.setVisibility(View.GONE);
                    } else {
                        adapter = new UserAdapter(getActivity(),
                                R.layout.item_friends, users);
                        try {
                            list.setAdapter(adapter);

                            progressWheel.setVisibility(View.GONE);
                        } catch (NullPointerException ex) {

                        }

                    }
                } catch (NullPointerException ex) {

                }
            }
        });

    }
    private void InvitRequest() {
        Bundle params = new Bundle();
        params.putString("message", "InviteMe");

        WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(
                getActivity(), ParseFacebookUtils.getSession(), params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error != null) {
                            if (error instanceof FacebookOperationCanceledException) {
                                Toast.makeText(
                                        getActivity(),
                                        "Request cancelled", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(
                                        getActivity(),
                                        "Network Error", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            final String requestId = values
                                    .getString("request");

                            if (requestId != null) {

                                Toast.makeText(
                                        getActivity(),
                                        "Request sent", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(
                                        getActivity(),
                                        "Request cancelled", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }

                }).build();
        requestsDialog.show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 4:
                //InvitRequest();
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.root_friends, new InvitFriendsFragment(), "invitfriends");
                transaction.addToBackStack("invitfriends");
                transaction.commit();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseUser parseUser = (ParseUser)parent.getAdapter().getItem(position);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.root_friends, ProfilFreindsFragment.newInstance(parseUser.getString("fbId")), "profilfriend");
        transaction.commit();
    }



}
