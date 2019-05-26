package uk.ac.ncl.team12.northumbriaGo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;
import java.util.List;

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: Calum Harvey
                     Samuel Holley
                     Jamie Child

        Description: The startup screen for the app, this allows users to log in, or is bypassed if
        someone closed the app without logging out. Also creates new data in the database for when
        a new user logs in.

    ----------------------------------------------------------------------------------------------------*/

public class MainActivity extends AppCompatActivity {

    private LinearLayout main;
    private TextView mainQuote;
    private Button mainOptionSocial;

    private static int RC_SIGN_IN = 100;
    public SharedPreferences mPreferences;
    public SharedPreferences.Editor mEditor;
    public FirebaseDatabase db;
    public DatabaseReference dr;
    private FirebaseAuth mAuth;

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: Samuel Holley
                     Calum Harvey
                     Jamie Child

        Description: Before anything is displayed, the current user is checked so that if one exists
        they are automatically redirected. If no one is logged in, a database connection is established
        so that users can log in with their facebook account, google account, or email account; and
        sign up using this same method. Users are added to the database if they are new, along with
        the data to store their preferences such as theme and mode of transport. Users logging in
        already have this information. If a user logs in, instead of the app auto redirecting, their
        information is then told to be loaded.

    ----------------------------------------------------------------------------------------------------*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mPreferences = getSharedPreferences(getResources().getString(R.string.pref_loc), Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.commit();

        if (mAuth.getCurrentUser() != null) {
            updateUI(mAuth.getCurrentUser());
        }

        db = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_main);
        main = findViewById(R.id.main);
        mainQuote = findViewById(R.id.main_quote);
        mainOptionSocial = findViewById(R.id.main_option_social);
        setColorScheme();

        mainOptionSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.FacebookBuilder().build());
                boolean enableDarkMode = mPreferences.getBoolean(getResources().getString(R.string.pref_dark), true);

                if (enableDarkMode) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.logo)      // Set logo drawable
                                    .setTheme(R.style.fire_dark)   //Dark
                                    .build(),
                            RC_SIGN_IN);
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.logo)      // Set logo drawable
                                    .setTheme(R.style.fire_light)  //Light
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        });
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: When the login button is pressed, and an account successfully logs in, this is
        called. It manages whether or not they are a new or existing user and creates a default
        preference set for the new users. Once this is complete, the user is then redirected to the map

    ----------------------------------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mAuth = FirebaseAuth.getInstance();
                dr = db.getReference();
                final String uid = mAuth.getCurrentUser().getUid();
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean found = false;
                        for(DataSnapshot key: dataSnapshot.getChildren()){
                            Preference curr = new Preference();
                            try{
                                curr.setUid(key.child(uid).getValue(Preference.class).getUid());
                                if(curr.getUid().equals(uid)){
                                    found = true;
                                }
                            } catch (Exception e){
                                Log.e(getString(R.string.ma_login_error), e.getLocalizedMessage());
                            }
                        }
                        if(found){
                            //Existing account, log in
                            updateUILoad(user);
                        } else {
                            //New account create data
                            Preference pref = new Preference(uid, 0, true, true,
                                    getString(R.string.ma_default_achievement));
                            dr.child(getString(R.string.db_child)).child(uid).setValue(pref)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess (Void aVoid) {
                                            updateUILoad(user);
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(getString(R.string.db_err), databaseError.getDetails());
                    }
                });
            }
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Overwrites the preset code for when the back button is pressed, making it close the
        app by default and nothing else.

    ----------------------------------------------------------------------------------------------------*/

    @Override
    public void onBackPressed() {
        finish();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Called when a user has already been logged in since before the app was just opened.
        Redirects to the map screen without reloading data from the database.

    ----------------------------------------------------------------------------------------------------*/

    public void updateUI(FirebaseUser user){
        if (user != null) {
            Intent intent =  new Intent(this, HomeActivity.class);
            intent.putExtra(getString(R.string.ma_log_req), getString(R.string.ma_false));
            startActivity(intent);
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Called when a user has manually signed in. Tells the map screen to load the data
        from the database.

    ----------------------------------------------------------------------------------------------------*/

    public void updateUILoad(FirebaseUser user){
        if (user != null) {
            Intent intent =  new Intent(this, HomeActivity.class);
            intent.putExtra(getString(R.string.ma_log_req), getString(R.string.ma_true));
            startActivity(intent);
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Gets the last used colour scheme from the most recent user and sets the theme of
        this page to that by changing the colours of all visible assets.

    ----------------------------------------------------------------------------------------------------*/

    private void setColorScheme(){
        boolean enableDarkMode = mPreferences.getBoolean(getResources().getString(R.string.pref_dark), true);
        if (enableDarkMode) {
            main.setBackgroundResource(R.drawable.theme_dark_background);
            mainOptionSocial.setBackgroundResource(R.drawable.theme_dark_button_square);
            mainQuote.setTextColor(getColor(R.color.colorDarkAccent));
            mainOptionSocial.setTextColor(getColor(R.color.colorDarkAccent));
        } else {
            main.setBackgroundResource(R.drawable.theme_light_background);
            mainOptionSocial.setBackgroundResource(R.drawable.theme_light_button_square);
            mainQuote.setTextColor(getColor(R.color.colorLightAccent));
            mainOptionSocial.setTextColor(getColor(R.color.colorLightAccent));
        }
    }
}
