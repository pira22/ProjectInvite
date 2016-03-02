package pira.stellerin.com.projectinvite.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

import pira.stellerin.com.projectinvite.Entity.ProgrammePost;
import pira.stellerin.com.projectinvite.Entity.TagIn;
import pira.stellerin.com.projectinvite.R;


public class InvitemeApplication extends MultiDexApplication {

    public static final String TAG = "InviteMe";
    public static final String INTENT_EXTRA_LOCATION = "location";
    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE = "searchDistance";
    private static SharedPreferences preferences;

    public InvitemeApplication() {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();
        //	PushService.setDefaultPushCallback(this, UserDetailsActivity.class);

        ParseObject.registerSubclass(ProgrammePost.class);
        ParseObject.registerSubclass(TagIn.class);
        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Vn2plvxcL0WEJWYCHe3jmHjGijh4OtoTYIA3Pkoy",
                "i0mgiAK9KbkfaBagH42GsMbsOaOcqBjFGaGcmweB");

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(getString(R.string.app_id));
        preferences = getSharedPreferences("com.sim.invite_me", Context.MODE_PRIVATE);
        // Set your Facebook App Id in strings.xml

    }


    public static void setSearchDistance(float value) {
        preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
    }

}
