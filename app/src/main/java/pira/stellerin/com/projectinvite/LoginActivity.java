package pira.stellerin.com.projectinvite;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import pira.stellerin.com.projectinvite.Utils.ConnectivityOnline;
import pira.stellerin.com.projectinvite.Utils.InvitemeApplication;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    SuperActivityToast superActivityToast;
    ProgressWheel progress;
    TextView textView3,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        DebugKeyHashFb();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress = (ProgressWheel) findViewById(R.id.progresscercle);
                progress.setVisibility(View.GONE);
                loginButton = (Button) findViewById(R.id.loginButton);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ConnectivityOnline.isConnectedMobile(getBaseContext())
                                || ConnectivityOnline.isConnectedWifi(getBaseContext())) {
                            onLoginButtonClicked();
                        } else {
                            superActivityToast = new SuperActivityToast(LoginActivity.this,
                                    SuperToast.Type.STANDARD);
                            superActivityToast.setText(getString(R.string.connection));
                            superActivityToast.setAnimations(SuperToast.Animations.FADE);
                            superActivityToast.setDuration(SuperToast.Duration.LONG);
                            superActivityToast.setBackground(SuperToast.Background.RED);
                            superActivityToast.setTextSize(SuperToast.TextSize.MEDIUM);
                            superActivityToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
                            superActivityToast.show();
                        }


                    }
                });
            }
        });


        // Check if there is a currently logged in user
        // and they are linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            showUserDetailsActivity();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private void onLoginButtonClicked() {
        progress.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        List<String> permissions = Arrays.asList("email", "public_profile", "user_friends", "user_birthday", "user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                if (user == null) {
                    Log.d(InvitemeApplication.TAG,
                            "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(InvitemeApplication.TAG,
                            "User signed up and logged in through Facebook!");

                    makeMeRequest();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showUserDetailsActivity();
                } else {

                    Log.d(InvitemeApplication.TAG,
                            "User logged in through Facebook!" + user.getObjectId());
                    user.fetchInBackground();
                    makeMeRequest();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showUserDetailsActivity();
                }
            }
        });
    }

    private void showUserDetailsActivity() {
        Intent intent = new Intent(this, ActivityContainer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                                currentUser.put("fbId",user.getId());
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

    public void DebugKeyHashFb(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("key-sign",sign);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("nope", "nope");
        } catch (NoSuchAlgorithmException e) {
        }
    }
}
