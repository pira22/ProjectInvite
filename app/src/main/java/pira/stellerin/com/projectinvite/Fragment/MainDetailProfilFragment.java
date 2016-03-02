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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.R;


public class MainDetailProfilFragment extends Fragment {

    public static final String[] CONTENT = new String[]{"Details", "Albums"};
    public String objectId, titre, description, date, userid;
    public double lon, lat;
    Bundle bundle;
    ViewPager viewPager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        userid = bundle.getString("userid");
        objectId = bundle.getString("objectId");
        titre = bundle.getString("titre");
        description = bundle.getString("Descritpion");
        date = bundle.getString("date");
        lon = bundle.getDouble("longitude");
        lat = bundle.getDouble("latitude");

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_detail_profil, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.plan_details);
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
        viewPager = (ViewPager) getActivity().findViewById(R.id.pagerprofil);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ListPicProfilFragment listPicProfilFragment = (ListPicProfilFragment) getFragmentManager().findFragmentByTag("pic_profil");
                DetailPicProfilFragment detailPicFragment = (DetailPicProfilFragment) getFragmentManager().findFragmentByTag("pic_profil_pic");
                switch (tab.getPosition()) {
                    case 0:
                        ActivityContainer.DETECT = "mainprofil";
                        break;
                    case 1:
                        try {
                            if (listPicProfilFragment.isResumed()) {
                                ActivityContainer.DETECT = "mainprofil";
                            }
                        } catch (NullPointerException e) {

                        }
                        try {
                            if (detailPicFragment.isResumed()) {
                                ActivityContainer.DETECT = "detailprofil";
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
            menu.findItem(101).setVisible(true);
            menu.findItem(150).setVisible(true);
        }catch (NullPointerException e){

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AppCompatActivity)  activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (item.getItemId()) {

            case 101:
                GetPictureFragment gp = new GetPictureFragment();
                gp.setArguments(bundle);

                transaction.replace(R.id.root_profil, gp, "gp");
                transaction.addToBackStack("gp");
                transaction.commit();
                return true;
            case 150:
                ProgramfriendInvit pfi = new ProgramfriendInvit();
                transaction.replace(R.id.root_profil, pfi, "invitprogramm");
                pfi.setArguments(bundle);
                transaction.addToBackStack("invitprogramm");
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ViewpagerAdapter extends FragmentPagerAdapter {

        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new DetailProfilFragment();
                    fragment.setArguments(bundle);
                    break;

                case 1:
                    fragment = new RootPicProfilFragment();
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

