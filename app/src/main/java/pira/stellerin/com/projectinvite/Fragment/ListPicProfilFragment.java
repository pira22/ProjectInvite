package pira.stellerin.com.projectinvite.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.novoda.merlin.MerlinsBeard;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.Adapter.GalerieAdapter;
import pira.stellerin.com.projectinvite.Adapter.PicAdapter;
import pira.stellerin.com.projectinvite.R;
import pira.stellerin.com.projectinvite.Shared.Shared;
import pira.stellerin.com.projectinvite.Utils.HeaderGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPicProfilFragment extends Fragment implements AdapterView.OnItemClickListener {

    HeaderGridView mListBuddies;
    PicAdapter mAdapter;
    private List<String> mImagesLeft = new ArrayList<String>();
    private List<String> mImagesRight = new ArrayList<String>();
    List<ParseObject> ob;
    String objectId, userid;
    ProgressWheel progressWheel;
    MerlinsBeard merlinsBeard;
    SwipeRefreshLayout swipeContainer;
    boolean refresh = false;
    Bundle bundle;
    View view;
    public static ListPicProfilFragment NewInstance(Bundle bundle) {
        ListPicProfilFragment listPicProfilFragment = new ListPicProfilFragment();
        listPicProfilFragment.setArguments(bundle);
        return listPicProfilFragment;
    }

    public ListPicProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        objectId = bundle.getString("objectId");
        userid = bundle.getString("userid");
        ActivityContainer.DETECT = "mainprofil";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_pic_profil, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        merlinsBeard = MerlinsBeard.from(getActivity());
        swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer_profil);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (merlinsBeard.isConnected()) {
                    refresh = true;
                    GetPic getPic = new GetPic();
                    getPic.execute();
                } else {
                    final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                    Snackbar
                            .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                            .show(); // Don’t forget to show!
                }
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mListBuddies = (HeaderGridView) getActivity().findViewById(R.id.listbuddies_profil);
        mListBuddies.setOnItemClickListener(this);
        view = getLayoutInflater(savedInstanceState).inflate(R.layout.empty_layout,mListBuddies,false);
        progressWheel = (ProgressWheel) getActivity().findViewById(R.id.progresscercle_profil);
        progressWheel.setVisibility(View.GONE);

        if (merlinsBeard.isConnected()) {
            if(Shared.listPicProfile.isEmpty()) {
                GetPic getPic = new GetPic();
                getPic.execute();
            }else{
                if(mListBuddies.getHeaderViewCount()>0) {
                    mListBuddies.removeHeaderView(view);
                }
                try {
                    mListBuddies.setOnItemClickListener(ListPicProfilFragment.this);
                    mAdapter = new  PicAdapter(getActivity(), R.layout.item_galerie_profil, Shared.listPicProfile);
                } catch (NullPointerException e) {
                    Log.d("erreur", "Erreur Loading");
                }
                progressWheel.setVisibility(View.GONE);
                mListBuddies.setAdapter(mAdapter);
            }
        } else {
            final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
            Snackbar
                    .make(coordinatorLayoutView, R.string.connection, Snackbar.LENGTH_LONG)
                    .show(); // Don’t forget to show!
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        bundle.putString("url", parent.getAdapter().getItem(position).toString());
        bundle.putString("page","detailprofil");
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.root_pic_profil, DetailPicProfilFragment.newInstance(bundle), "pic_profil_pic");
        transaction.addToBackStack(null);
        transaction.commit();
        //getFragmentManager().executePendingTransactions();
    }


    class GetPic extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (refresh)
                if(!mImagesLeft.isEmpty())
                mAdapter.clear();
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mImagesLeft.isEmpty()) {
                try{
                Log.d("header count ====>", "=====> count" + mListBuddies.getHeaderViewCount());
                if (mListBuddies.getHeaderViewCount() == 0) {

                    mListBuddies.addHeaderView(view);
                    mListBuddies.setAdapter(null);
                    progressWheel.setVisibility(View.GONE);
                } else
                    progressWheel.setVisibility(View.GONE);
                }catch (IllegalStateException e){

                }
            }else{
                if(mListBuddies.getHeaderViewCount()>0) {
                    mListBuddies.removeHeaderView(view);
                }
                try {
                    Shared.listPicProfile = mImagesLeft;
                    mAdapter = new PicAdapter(getActivity(), R.layout.item_galerie_profil, mImagesLeft);
                } catch (NullPointerException e) {
                    Log.d("erreur", "Erreur Loading");
                }

            }
            mListBuddies.setAdapter(mAdapter);
            if (refresh)
                swipeContainer.setRefreshing(false);
            refresh = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseObject programme = ParseObject.createWithoutData("Programmes", objectId);
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Gallery");
                query.whereEqualTo("programme", programme);
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                ob = query.find();
                Log.d("NbreList", ob.size() + "");
                for (int i = 0; i < ob.size(); i++) {
                    Log.d("incremntation", i + "");
                    ParseFile pf = (ParseFile) ob.get(i).get("picture");
                    Log.d("profil size List", ob.size() + " profil");
                    mImagesLeft.add(pf.getUrl());

                }
            } catch (ParseException e1) {
                Log.d("error parse", e1.getMessage());
            }


            return null;
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

