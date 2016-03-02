package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainDetailFriendsFragment extends Fragment {

    public static final String[] CONTENT = new String[]{"Details", "Albums"};
    public String objectId, titre, description, date, userid;
    public double lon, lat;
    public Bundle bundle;
    ViewPager viewPager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        ActivityContainer.DETECT = "maindetailfriends";
        userid = bundle.getString("userid");
        objectId = bundle.getString("objectId");
        titre = bundle.getString("titre");
        description = bundle.getString("Descritpion");
        date = bundle.getString("date");
        lon = bundle.getDouble("longitude");
        lat = bundle.getDouble("latitude");
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }
    public MainDetailFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_detail_friends, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.plan_details);
        Log.d("DETECT =====>","onActivityCreated"+" "+ActivityContainer.DETECT);
       /* ViewpagerAdapter adapter = new ViewpagerAdapter(getFragmentManager());
        try {
            ViewPager pager = (ViewPager) getView().findViewById(R.id.pagertabprofil);
            pager.setAdapter(adapter);
            TabPageIndicator indicator = (TabPageIndicator) getView().findViewById(R.id.indicatortabprofil);
            indicator.setViewPager(pager);
        }catch (IllegalStateException e){

        }*/
        ViewpagerAdapter adapter = new ViewpagerAdapter
                (getChildFragmentManager());
        viewPager = (ViewPager) getActivity().findViewById(R.id.pagerprofil1);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ListPicFriendsFragment listPicProfilFragment = (ListPicFriendsFragment) getFragmentManager().findFragmentByTag("list_pic_friends");
                PicDetailsFriendsFragment detailPicFragment = (PicDetailsFriendsFragment) getFragmentManager().findFragmentByTag("pic_profil_friends");
                switch (tab.getPosition()) {
                    case 0:
                            ActivityContainer.DETECT = "maindetailfriends";
                        break;
                    case 1:
                        try {
                            if (listPicProfilFragment.isResumed()) {
                                ActivityContainer.DETECT = "maindetailfriends";
                            }
                        } catch (NullPointerException e) {

                        }
                        try {
                            if (detailPicFragment.isResumed()) {
                                ActivityContainer.DETECT = "detailprofilfriends";
                            }
                        } catch (NullPointerException e) {

                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        try {
            MenuItem item = menu.findItem(4);
            item.setVisible(false);
        }catch (NullPointerException e){

        }
        //menu.add(0, 1, 100, R.string.addimage).setIcon(R.drawable.ic_add_picture1).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AppCompatActivity)  activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     return false;
    }

    class ViewpagerAdapter extends FragmentPagerAdapter {

        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            android.support.v4.app.Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new DetailProfilFriends();
                    fragment.setArguments(bundle);
                    break;

                case 1:
                    fragment = new RootPictFriendsFragment();
                    fragment.setArguments(bundle);
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }
    }
}
