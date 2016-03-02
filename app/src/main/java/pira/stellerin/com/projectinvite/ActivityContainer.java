package pira.stellerin.com.projectinvite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pira.stellerin.com.projectinvite.Fragment.DetailPicFragment;
import pira.stellerin.com.projectinvite.Fragment.DetailPicProfilFragment;
import pira.stellerin.com.projectinvite.Fragment.InvitFriendsFragment;
import pira.stellerin.com.projectinvite.Fragment.ListFriendsFragment;
import pira.stellerin.com.projectinvite.Fragment.ListPicFragment;
import pira.stellerin.com.projectinvite.Fragment.ListPicFriendsFragment;
import pira.stellerin.com.projectinvite.Fragment.ListPicProfilFragment;
import pira.stellerin.com.projectinvite.Fragment.ListeProgramme;
import pira.stellerin.com.projectinvite.Fragment.MainDetailFriendsFragment;
import pira.stellerin.com.projectinvite.Fragment.MainDetailProfilFragment;
import pira.stellerin.com.projectinvite.Fragment.MainDetailProgramme;
import pira.stellerin.com.projectinvite.Fragment.MainFragment;
import pira.stellerin.com.projectinvite.Fragment.NotificationFragment;
import pira.stellerin.com.projectinvite.Fragment.PicDetailsFriendsFragment;
import pira.stellerin.com.projectinvite.Fragment.PostFragment;
import pira.stellerin.com.projectinvite.Fragment.ProfilFragment;
import pira.stellerin.com.projectinvite.Fragment.ProfilFreindsFragment;
import pira.stellerin.com.projectinvite.Fragment.ProgramfriendInvit;
import pira.stellerin.com.projectinvite.Fragment.RootFriendsFragment;
import pira.stellerin.com.projectinvite.Fragment.RootProfilFragment;
import pira.stellerin.com.projectinvite.Fragment.RootProgrammeFragment;
import pira.stellerin.com.projectinvite.Shared.Shared;
import pira.stellerin.com.projectinvite.Utils.InvitemeApplication;

public class ActivityContainer extends AppCompatActivity {
    private TabBarAdapter tabBarAdapter;
     public static  ViewPager pager;
    private TabPageIndicator indicator;
    private ArrayList<Integer> listIcon;
    int positionindex;
    public static Boolean DISPLAY_POP = false;
    public static String DETECT ;
    public static Boolean title = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_container);
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            makefriendsrequest();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

               // shouldDisplayHomeUp();


            }
        });
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        if (session != null && session.isOpened()) {
            List<String> permissions = Arrays.asList("email", "public_profile",
                    "user_friends", "user_birthday", "user_location");
            makeMeRequest();
        }

        getTabsIcon();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tabBarAdapter = new TabBarAdapter(
                        getSupportFragmentManager());
                pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(tabBarAdapter);
                // pager.setPagingEnabled(false);
                pager.setOffscreenPageLimit(4);
                indicator = (TabPageIndicator) findViewById(R.id.tabs);
                indicator.setTabIconLocation(TabPageIndicator.LOCATION_UP);
                indicator.setViewPager(pager);
                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        MainDetailProgramme mainDetailProgramme = (MainDetailProgramme) getSupportFragmentManager().findFragmentByTag("mainDetailProgramme");
                        MainDetailProfilFragment detailProfilFragment = (MainDetailProfilFragment) getSupportFragmentManager().findFragmentByTag("profildetailmain");
                        ListeProgramme listeProgramme = (ListeProgramme) getSupportFragmentManager().findFragmentByTag("listeProgramme");
                        ProfilFragment profilFragment = (ProfilFragment) getSupportFragmentManager().findFragmentByTag("profilFragment");
                        ListFriendsFragment listFriendsFragment = (ListFriendsFragment) getSupportFragmentManager().findFragmentByTag("listFriends");
                        ProfilFreindsFragment listFriends = (ProfilFreindsFragment) getSupportFragmentManager().findFragmentByTag("profilfriend");
                        DetailPicFragment detailPic = (DetailPicFragment) getSupportFragmentManager().findFragmentByTag("pic_profil_programme");
                        PostFragment postFragment = (PostFragment) getSupportFragmentManager().findFragmentByTag("post");
                        DetailPicProfilFragment detailPicProfilFragment = (DetailPicProfilFragment) getSupportFragmentManager().findFragmentByTag("pic_profil_pic");
                        PicDetailsFriendsFragment detailPicFragment =(PicDetailsFriendsFragment) getSupportFragmentManager().findFragmentByTag("pic_profil_friends");
                        MainDetailFriendsFragment mainDetailProfilFragment = (MainDetailFriendsFragment) getSupportFragmentManager().findFragmentByTag("profilfriendsdetailmain");
                        InvitFriendsFragment invitFriendsFragment =(InvitFriendsFragment) getSupportFragmentManager().findFragmentByTag("invitfriends");
                        NotificationFragment notificationFragment =(NotificationFragment) getSupportFragmentManager().findFragmentByTag("notif");
                        ProgramfriendInvit invitFriendsFragment1 = (ProgramfriendInvit) getSupportFragmentManager().findFragmentByTag("invitprogramm");
                        switch (position) {
                            case 0:
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                getSupportActionBar().setTitle(R.string.Main);
                                break;
                            case 1:
                                title = true;
                                try {
                                    if (mainDetailProgramme.isResumed()) {
                                        DETECT = "maindetail";
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                        getSupportActionBar().setTitle(R.string.plan_details);
                                        Log.d("case tab", "true");
                                    }
                                } catch (NullPointerException e) {

                                }
                                try {
                                    if (detailPic.isResumed()) {
                                        DETECT = "detail";

                                    }
                                } catch (NullPointerException e) {

                                }
                                try {
                                    if (listeProgramme.isResumed()) {
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                        getSupportActionBar().setTitle(R.string.programme);
                                        Log.d("case tab", "false");
                                    }
                                } catch (NullPointerException e) {

                                }
                                try {
                                    if (postFragment.isResumed()) {
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                        ActivityContainer.DETECT = "maindetail";
                                        getSupportActionBar().setTitle(R.string.post);
                                        Log.d("case tab", "false");
                                    }
                                } catch (NullPointerException e) {

                                }
                                try {
                                    if (notificationFragment.isResumed()) {
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                        ActivityContainer.DETECT ="notiffragment";
                                        getSupportActionBar().setTitle(R.string.notif);
                                        Log.d("case tab", "false");
                                    }
                                } catch (NullPointerException e) {

                                }
                                break;
                            case 2:

                                DETECT = "profilfriend";
                                try {
                                    if (listFriends.isResumed()) {

                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                        getSupportActionBar().setTitle(R.string.Profils_user);
                                    }
                                } catch (NullPointerException e) {

                                }
                                try {
                                    if (listFriendsFragment.isResumed()) {
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                        getSupportActionBar().setTitle(R.string.List_Friends);
                                    }
                                } catch (NullPointerException e) {

                                }
                                try{
                                    if (mainDetailProfilFragment.isResumed()) {
                                        DETECT = "maindetailfriends";
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                                    }
                                }catch (NullPointerException e) {

                                }
                                try{
                                    if (detailPicFragment.isResumed()) {
                                        DETECT = "detailprofilfriends";
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                                    }
                                }catch (NullPointerException e) {

                                }
                                try{
                                    if (invitFriendsFragment.isResumed()) {
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                                    }
                                }catch (NullPointerException e) {

                                }

                                Log.d("DETECT ====>",DETECT+"=====>");
                                break;
                            case 3:
                                DETECT ="mainprofil";
                                try {
                                    if (detailProfilFragment.isResumed())
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    getSupportActionBar().setTitle(R.string.plan_details);
                                } catch (NullPointerException e) {

                                }
                                try {
                                    if (profilFragment.isResumed()) {
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                        getSupportActionBar().setTitle(R.string.Profils_user);
                                        Log.d("case tab", "false");
                                    }
                                } catch (NullPointerException e) {

                                }
                                try {
                                    if (detailPic.isResumed()) {
                                        DETECT = "detailprofil";
                                    }
                                } catch (NullPointerException e) {

                                }
                                try {
                                if (invitFriendsFragment1.isResumed()) {
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    getSupportActionBar().setTitle(R.string.addFriends);
                                }
                            } catch (NullPointerException e) {

                            }


                                break;

                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        });

    }

    public void logout(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ActivityContainer.this);

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.logout));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.confirme_logout));

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton(getString(R.string.confirme),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       onLogoutButtonClicked();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case android.R.id.home:
                if(ActivityContainer.DETECT=="maindetailfriends"){
                    ProfilFreindsFragment profilFreindsFragment =new ProfilFreindsFragment();
                    profilFreindsFragment.setArguments(ProfilFreindsFragment.arguments);
                    transaction.replace(R.id.root_friends, profilFreindsFragment, "profilfriend");
                    transaction.commit();
                }else
                if (ActivityContainer.DETECT == "maindetail") {
                    transaction.replace(R.id.root_prog, new ListeProgramme(), "listeProgramme");
                    transaction.commit();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else if (ActivityContainer.DETECT == "profilfriend") {
                    transaction.replace(R.id.root_friends, new ListFriendsFragment(), "listFriends");
                    transaction.commit();

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }else if(ActivityContainer.DETECT =="mainprofil"){
                    transaction.replace(R.id.root_profil, new ProfilFragment(), "profilFragment");
                    transaction.commit();
                }else if (ActivityContainer.DETECT =="detail"){
                    Log.d("fragment", "enter");
                        transaction.replace(R.id.root_pic, ListPicFragment.NewInstance(DetailPicFragment.bundle), "list_pic");
                    transaction.commit();
                    getSupportFragmentManager().executePendingTransactions();

                }else if (ActivityContainer.DETECT =="detailprofil"){
                Log.d("fragment", "itemmenuprofil");
                    transaction.replace(R.id.root_pic_profil, ListPicProfilFragment.NewInstance(DetailPicProfilFragment.bundle), "pic_profil");
                    transaction.commit();
                    getSupportFragmentManager().executePendingTransactions();

            }else if (ActivityContainer.DETECT =="detailprofilfriends"){
                    Log.d("fragment", "itemmenuprofilfriends");
                    transaction.replace(R.id.containerrootpicf, ListPicFriendsFragment.NewInstance(PicDetailsFriendsFragment.bundle), "list_pic_friends");
                    transaction.commit();
                    //getSupportFragmentManager().executePendingTransactions();

                }else if(ActivityContainer.DETECT =="notiffragment"){
                    transaction.replace(R.id.root_prog, new ListeProgramme(), "listeProgramme");
                    transaction.commit();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }

                return false;
            case R.id.logout:
                logout();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    private void makefriendsrequest() {


        Request request = Request.newMyFriendsRequest(
                ParseFacebookUtils.getSession(),
                new Request.GraphUserListCallback() {

                    @Override
                    public void onCompleted(List<GraphUser> users,
                                            Response response) {
                        Shared.listFriends = users;
                    }
                });

        request.executeAsync();

    }
    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {

                        if (user != null) {

                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();

                            try {

                                // Populate the JSON object
                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());
                                Log.d("Image", response.toString());
                                // Log.d("pira",
                                // user.getLocation().getProperty("name").toString());

                                if (user.getProperty("gender") != null) {
                                    userProfile.put("gender",
                                            user.getProperty("gender"));
                                }
                                if (user.getName() != null) {
                                    userProfile.put("username", user.getName());
                                }
                                if (user.getBirthday() != null) {
                                    userProfile.put("birthday",
                                            user.getBirthday());
                                }
                                if (user.getProperty("relationship_status") != null) {
                                    userProfile.put("relationship_status", user
                                            .getProperty("relationship_status"));
                                }
                                if (user.getProperty("email") != null) {
                                    userProfile.put("email",
                                            user.getProperty("email"));

                                }
                                if (user.getProperty("verified") != null) {
                                    userProfile.put("emailVerified",
                                            user.getProperty("verified"));
                                }

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser
                                        .getCurrentUser();
                                Log.d("pira", currentUser.toString());
                                currentUser.put("profile", userProfile);
                                currentUser.put("fbId", user.getId());
                                /*
                                 * currentUser.put("email",userProfile.getString(
								 * "email")); Log.d("user",
								 * userProfile.getString("email"));
								 * currentUser.put
								 * ("username",userProfile.getString
								 * ("username"));
								 * currentUser.put("emailVerified"
								 * ,userProfile.getBoolean("emailVerified"));
								 */
                                currentUser.saveInBackground();


                            } catch (JSONException e) {
                                Log.d(InvitemeApplication.TAG,
                                        "Error parsing returned user data.");
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                                    || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d(InvitemeApplication.TAG,
                                        "The facebook session was invalidated.");
                                onLogoutButtonClicked();
                            } else {
                                Log.d(InvitemeApplication.TAG,
                                        "Some other error: "
                                                + response.getError()
                                                .getErrorMessage());
                            }
                        }
                    }
                });
        request.executeAsync();
    }


    private void onLogoutButtonClicked() {
        // Log the user out
        ParseUser.logOut();

        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(ActivityContainer.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void getTabsIcon() {
        listIcon = new ArrayList<Integer>();

        listIcon.add(R.drawable.home_ic);
        listIcon.add(R.drawable.calendar_ic);
        listIcon.add(R.drawable.users_ic);
        listIcon.add(R.drawable.user_ic);
    }


    class TabBarAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public TabBarAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0: fragment = new MainFragment();
                    break;
                case 1: fragment = new RootProgrammeFragment();
                    break;
                case 2: fragment = new RootFriendsFragment();
                    break;
                case 3 : fragment = new RootProfilFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getIconResId(int index) {
            return listIcon.get(index);
        }

        @Override
        public int getCount() {
            return listIcon.size();
        }
    }
}
