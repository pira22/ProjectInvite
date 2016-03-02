package pira.stellerin.com.projectinvite.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.viewpagerindicator.TabPageIndicator;

import pira.stellerin.com.projectinvite.ActivityContainer;
import pira.stellerin.com.projectinvite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainDetailProgramme extends Fragment {
    private static final String[] CONTENT = new String[]{"Details", "Albums"};
    public String objectId, titre, description, date,userid;
    public double lon, lat;
    Bundle bundle;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityContainer.DETECT = "maindetail";
        RootProgrammeFragment.menuitem = "dz";
        bundle = getArguments();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AppCompatActivity)  activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_detail_programme, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pagertab);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.plan_details);
        TabPageIndicator indicator = (TabPageIndicator) getActivity().findViewById(R.id.indicatortab);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ListPicFragment listPicFragment = (ListPicFragment) getFragmentManager().findFragmentByTag("list_pic");
                DetailPicFragment detailPicFragment = (DetailPicFragment) getFragmentManager().findFragmentByTag("pic_profil_programme");
                switch (position){
                    case 0:
                        ActivityContainer.DETECT = "maindetail";
                        break;
                    case 1:
                        try{
                            if(listPicFragment.isResumed()){
                                ActivityContainer.DETECT = "maindetail";
                            }
                        } catch (NullPointerException e) {

                        }
                        try{
                            if (detailPicFragment.isResumed()){
                                ActivityContainer.DETECT = "detail";
                            }
                        }catch (NullPointerException e){

                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        RootProgrammeFragment.menuitem = "child";
    }

    @Override
    public void onResume() {
        super.onResume();
        RootProgrammeFragment.ID_USER = userid;
        RootProgrammeFragment.ID_USER_PARTAGE = ParseUser.getCurrentUser().getObjectId();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {





        //menu.add(0, 1, 100, R.string.addimage).setIcon(R.drawable.ic_add_picture1).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
            case 3:
                GetPictureFragment gp = new GetPictureFragment();
                gp.setArguments(bundle);
                transaction.replace(R.id.root_prog, gp, "getpic");
                transaction.addToBackStack("getpic");
                transaction.commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new DetailFragment();
                    fragment.setArguments(bundle);
                    break;

                case 1:
                    fragment = new Root_Pic_Fragment();
                    fragment.setArguments(bundle);
                    break;

            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
}

