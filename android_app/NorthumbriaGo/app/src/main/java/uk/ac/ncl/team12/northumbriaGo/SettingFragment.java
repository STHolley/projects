package uk.ac.ncl.team12.northumbriaGo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

    /*----------------------------------------------------------------------------------------------------
        Created By: Calum Harvey

        Modified By: Samuel Holley

        Description: A page for changing the settings of the app, such as the colour theme, your
        default mode of transport, and whether or not you require disability access. These settings
        are saved locally until you logout, where they are then saved to the database. The user can
        also enable permissions here if they accidentally denied them earlier.
        This page also allows users to log out.

    ----------------------------------------------------------------------------------------------------*/

public class SettingFragment extends Fragment {

    private HomeActivity ha;

    private FirebaseAuth mAuth;

    public ScrollView fragmentSettings;
    public TextView fragmentSettingsTitle;
    public LinearLayout fragmentSettingsVisual;
    public TextView fragmentSettingsVisualTitle;
    public Switch fragmentSettingsVisualSwitch;
    public LinearLayout fragmentSettingsDevice;
    public TextView fragmentSettingsDeviceTitle;
    public Button fragmentSettingsDeviceLocation;
    public Button fragmentSettingsDeviceCamera;
    public LinearLayout fragmentSettingsTransport;
    public TextView fragmentSettingsTransportTitle;
    public Switch fragmentSettingsTransportDisability;
    public TextView fragmentSettingsTransportSubtitle;
    public RadioGroup fragmentSettingsTransportRadio;
    public RadioButton fragmentSettingsTransportRadioWalk;
    public RadioButton fragmentSettingsTransportRadioPublic;
    public RadioButton fragmentSettingsTransportRadioCar;
    public LinearLayout fragmentSettingsLogout;
    private TextView fragmentSettingsLogoutPrefix;
    private TextView fragmentSettingsLogoutEmail;
    public Button fragmentSettingsLogoutButton;
    private TextView fragmentSettingsEnabled;
    private LinearLayout fragmentSettingsMask;
    private LinearLayout fragmentSettingsTutorial;
    private Button fragmentSettingsTutorialCloseButton;
    private TextView fragmentSettingsTutorialContentTitle;
    private TextView fragmentSettingsTutorialContentBody;

    /*----------------------------------------------------------------------------------------------------
        Created By: Calum Harvey

        Modified By: Samuel Holley

        Description: Adds click listeners to the options on the page, along with changing the setting
        they are said to be. Also writes data to the local storage for when settings are changed, to
        limit data usage.

    ----------------------------------------------------------------------------------------------------*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ha = ((HomeActivity) Objects.requireNonNull(getActivity()));
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        fragmentSettings = v.findViewById(R.id.fragment_settings);
        fragmentSettingsTitle = v.findViewById(R.id.fragment_settings_title);
        fragmentSettingsVisual = v.findViewById(R.id.fragment_settings_visual);
        fragmentSettingsVisualTitle = v.findViewById(R.id.fragment_settings_visual_title);
        fragmentSettingsVisualSwitch = v.findViewById(R.id.fragment_settings_visual_switch);
        fragmentSettingsDevice = v.findViewById(R.id.fragment_settings_device);
        fragmentSettingsDeviceTitle = v.findViewById(R.id.fragment_settings_device_title);
        fragmentSettingsDeviceCamera = v.findViewById(R.id.fragment_settings_device_camera);
        fragmentSettingsDeviceLocation =  v.findViewById(R.id.fragment_settings_device_location);
        fragmentSettingsTransport = v.findViewById(R.id.fragment_settings_transport);
        fragmentSettingsTransportTitle = v.findViewById(R.id.fragment_settings_transport_title);
        fragmentSettingsTransportSubtitle = v.findViewById(R.id.fragment_settings_transport_subtitle);
        fragmentSettingsTransportRadio = v.findViewById(R.id.fragment_settings_transport_radio);
        fragmentSettingsTransportRadioWalk = v.findViewById(R.id.fragment_settings_transport_radio_walk);
        fragmentSettingsTransportRadioPublic = v.findViewById(R.id.fragment_settings_transport_radio_public);
        fragmentSettingsTransportRadioCar = v.findViewById(R.id.fragment_settings_transport_radio_car);
        fragmentSettingsLogout = v.findViewById(R.id.fragment_settings_logout);
        fragmentSettingsLogoutPrefix = v.findViewById(R.id.fragment_settings_logout_prefix);
        fragmentSettingsLogoutEmail = v.findViewById(R.id.fragment_settings_logout_email);
        fragmentSettingsLogoutButton = v.findViewById(R.id.fragment_settings_logout_button);
        fragmentSettingsEnabled = v.findViewById(R.id.fragment_settings_enabled);
        fragmentSettingsEnabled.setVisibility(View.INVISIBLE);
        fragmentSettingsTransportDisability = v.findViewById(R.id.fragment_settings_transport_disability);
        fragmentSettingsMask = v.findViewById(R.id.fragment_settings_mask);
        fragmentSettingsTutorial = v.findViewById(R.id.fragment_settings_tutorial);
        fragmentSettingsTutorialCloseButton = v.findViewById(R.id.fragment_settings_tutorial_close_button);
        fragmentSettingsTutorialContentTitle = v.findViewById(R.id.fragment_settings_tutorial_content_title);
        fragmentSettingsTutorialContentBody = v.findViewById(R.id.fragment_settings_tutorial_content_body);
        mAuth = FirebaseAuth.getInstance();

        updatePreferences();
        setColorScheme();
        fragmentSettingsDeviceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(ha.mLocationPermissionGranted){
                    decreaseAlpha();
                }else{
                    getLocationPermission();
                }
            }
        });
        fragmentSettingsDeviceCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(ha.mCameraPermissionGranted){
                    decreaseAlpha();
                }else{
                    getCameraPermission();
                }
            }
        });
        fragmentSettingsLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ha.deletePackedRecents();
                ha.logout();
            }
        });
        fragmentSettingsVisualSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ha.enableDarkMode = fragmentSettingsVisualSwitch.isChecked();
                ha.mEditor.putBoolean(getResources().getString(R.string.pref_dark), fragmentSettingsVisualSwitch.isChecked());
                ha.mEditor.commit();
                setColorScheme();
                ha.setColorScheme();
            }
        });
        fragmentSettingsTransportDisability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ha.disabilityAccess = fragmentSettingsTransportDisability.isChecked();
                ha.mEditor.putBoolean(getResources().getString(R.string.pref_dis), fragmentSettingsTransportDisability.isChecked());
                ha.mEditor.commit();
            }
        });
        fragmentSettingsTransportRadioCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ha.travelMode = 0;
                ha.mEditor.putInt(getResources().getString(R.string.pref_travel), 0);
                ha.mEditor.commit();
            }
        });
        fragmentSettingsTransportRadioWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ha.travelMode = 1;
                ha.mEditor.putInt(getResources().getString(R.string.pref_travel), 1);
                ha.mEditor.commit();
            }
        });
        fragmentSettingsTransportRadioPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ha.travelMode = 2;
                ha.mEditor.putInt(getResources().getString(R.string.pref_travel), 2);
                ha.mEditor.commit();
            }
        });
        fragmentSettingsTutorialCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSettingsTutorial.setVisibility(View.GONE);
                fragmentSettingsMask.setVisibility(View.GONE);
            }
        });
        return v;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Checks what the current saved settings are and changes visible settings such as
        slider positions or radio checks for which is necessary.

    ----------------------------------------------------------------------------------------------------*/

    private void updatePreferences(){
        int travelMode = ha.travelMode;
        boolean themeMode = ha.enableDarkMode;
        boolean disabilityMode = ha.mPreferences.getBoolean(getResources().getString(R.string.pref_dis), false);
        String email = mAuth.getCurrentUser().getDisplayName();
        fragmentSettingsVisualSwitch.setChecked(themeMode);
        fragmentSettingsTransportDisability.setChecked(disabilityMode);
        switch (travelMode){
            case(0):
                fragmentSettingsTransportRadioCar.setChecked(true);
                fragmentSettingsTransportRadioWalk.setChecked(false);
                fragmentSettingsTransportRadioPublic.setChecked(false);
                break;
            case(1):
                fragmentSettingsTransportRadioCar.setChecked(false);
                fragmentSettingsTransportRadioWalk.setChecked(true);
                fragmentSettingsTransportRadioPublic.setChecked(false);
                break;
            case(2):
                fragmentSettingsTransportRadioCar.setChecked(false);
                fragmentSettingsTransportRadioWalk.setChecked(false);
                fragmentSettingsTransportRadioPublic.setChecked(true);
                break;
        }
        fragmentSettingsLogoutEmail.setText(email);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A custom animation for making an object fade away after a few seconds.

    ----------------------------------------------------------------------------------------------------*/

    private void decreaseAlpha() {
        fragmentSettingsEnabled.setVisibility(View.VISIBLE);
        Animation aniFade = AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getApplicationContext(), R.anim.fadeout);
        fragmentSettingsEnabled.startAnimation(aniFade);
        fragmentSettingsEnabled.setVisibility(View.INVISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A request for getting location permissions for use with the map.

    ----------------------------------------------------------------------------------------------------*/

    public void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ha.mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ha.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A request for getting camera permissions for use with achievement unlocking.

    ----------------------------------------------------------------------------------------------------*/

    public void getCameraPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext(),
                android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            ha.mCameraPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CAMERA},
                    ha.PERMISSIONS_REQUEST_CAMERA);
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Sets the colour theme of the page to that of what the current theme of the app is.
        Does this by manually changing the required sections of each visible component on the screen.

    ----------------------------------------------------------------------------------------------------*/
    public void setColorScheme(){
        if(ha.enableDarkMode){
            fragmentSettings.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            fragmentSettingsTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsVisualTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsVisualSwitch.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsVisualSwitch.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            fragmentSettingsDeviceTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsDeviceCamera.setBackgroundResource(R.drawable.theme_dark_button_square);
            fragmentSettingsDeviceCamera.setTextColor(getResources().getColor(R.color.colorDarkSecondary));
            fragmentSettingsDeviceLocation.setBackgroundResource(R.drawable.theme_dark_button_square);
            fragmentSettingsDeviceLocation.setTextColor(getResources().getColor(R.color.colorDarkSecondary));
            fragmentSettingsTransportTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsTransportSubtitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsTransportDisability.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsTransportDisability.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            fragmentSettingsTransportRadioWalk.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsTransportRadioPublic.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsTransportRadioCar.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsTransportRadioWalk.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            fragmentSettingsTransportRadioPublic.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            fragmentSettingsTransportRadioCar.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            fragmentSettingsLogoutPrefix.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsLogoutEmail.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsLogoutButton.setBackgroundResource(R.drawable.theme_dark_button_square);
            fragmentSettingsLogoutButton.setTextColor(getResources().getColor(R.color.colorDarkSecondary));
            fragmentSettingsEnabled.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsEnabled.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            fragmentSettingsTutorial.setBackgroundResource(R.drawable.theme_dark_button_square_v2);
            fragmentSettingsTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentSettingsTutorialContentBody.setTextColor(getResources().getColor(R.color.colorDarkAccent));
        } else {
            fragmentSettings.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            fragmentSettingsTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsVisualTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsVisualSwitch.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsVisualSwitch.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightSecondary)));
            fragmentSettingsDeviceTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsDeviceCamera.setBackgroundResource(R.drawable.theme_light_button_square);
            fragmentSettingsDeviceCamera.setTextColor(getResources().getColor(R.color.colorLightSecondary));
            fragmentSettingsDeviceLocation.setBackgroundResource(R.drawable.theme_light_button_square);
            fragmentSettingsDeviceLocation.setTextColor(getResources().getColor(R.color.colorLightSecondary));
            fragmentSettingsTransportTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsTransportSubtitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsTransportDisability.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsTransportDisability.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightSecondary)));
            fragmentSettingsTransportRadioWalk.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsTransportRadioPublic.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsTransportRadioCar.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsTransportRadioWalk.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightSecondary)));
            fragmentSettingsTransportRadioPublic.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightSecondary)));
            fragmentSettingsTransportRadioCar.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightSecondary)));
            fragmentSettingsLogoutPrefix.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsLogoutEmail.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsLogoutButton.setBackgroundResource(R.drawable.theme_light_button_square);
            fragmentSettingsLogoutButton.setTextColor(getResources().getColor(R.color.colorLightSecondary));
            fragmentSettingsEnabled.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsEnabled.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            fragmentSettingsTutorial.setBackgroundResource(R.drawable.theme_light_button_square_v2);
            fragmentSettingsTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentSettingsTutorialContentBody.setTextColor(getResources().getColor(R.color.colorLightAccent));
        }
    }
}
