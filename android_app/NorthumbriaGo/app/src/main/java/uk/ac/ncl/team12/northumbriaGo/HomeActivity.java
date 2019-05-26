package uk.ac.ncl.team12.northumbriaGo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.util.Size;

import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.w3c.dom.Attr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: Michael Putra

        Description: The main functionality of the app. Contains many aspects such as the map,
        controlling directions, handling navigation through pages, the permissions and preferences of the
        app, opening and closing visual elements, achievement unlocking through the camera or otherwise,
        reading the database, and controllingg activity based features of the fragments.

    ----------------------------------------------------------------------------------------------------*/

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Attraction> attractionList = new ArrayList<>();

    public boolean mLocationPermissionGranted;
    public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public boolean mCameraPermissionGranted;
    public final int PERMISSIONS_REQUEST_CAMERA = 2;
    public static boolean firstTutorial;

    Size previewsize;
    CameraDevice cameraDevice;
    CaptureRequest.Builder previewBuilder;
    CameraCaptureSession previewSession;
    private final float CAMERADISTANCE = 0.5f;
    private SensorManager mSensorManager;
    private SensorEventListener eventListener;
    private Sensor sensor;
    public boolean isUpright;
    private boolean isCameraOpen = false;
    public Map<Integer, Integer> locToAchv = new HashMap<>();

    private int tutorialCounter;

    private Fragment nullFrag;
    private Fragment recentFrag;
    private Fragment achvFrag;
    private Fragment settFrag;
    private Fragment helpFrag;

    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //Bounds:
    //NE 55.796495, -1.483156
    //SW 54.830436, -2.676173
    private float neLat = 55.796495f;
    private float neLng = -1.483156f;
    private float swLat = 54.830436f;
    private float swLng = -2.676173f;
    private LatLng ne = new LatLng(neLat, neLng);
    private LatLng sw = new LatLng(swLat, swLng);
    private float minZoom = 9.0f; //Zoom out
    private float maxZoom = 20.0f; //Zoom in
    private LatLngBounds NORTHUMBERLAND = new LatLngBounds(sw, ne);

    private boolean openedWithSearch;
    private boolean firstNearbyShow = true;

    //Map Markers
    private Marker Chev;
    private Marker Hadr;
    private Marker Hayd;
    private Marker AlnC;
    private Marker Holy;
    private Marker Bamb;
    private Marker Duns;
    private Marker Kiel;
    private Marker AlnG;
    private Marker Howc;
    private Marker Wood;
    private Marker Bagp;
    private Marker Yeav;
    private Marker Whit;
    private Marker Newc;
    private DirectionsResult vectorResult;
    private Polyline resultPoly;
    private Marker resultMarker;

    //Preferences
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferencePreference;
    private DataSnapshot currentFiles;
    boolean dataAvailable = false;
    boolean justLogged;
    public Preference pref;
    public FirebaseAuth mAuth;
    public String[] achievementList;
    public SharedPreferences mPreferences;
    public SharedPreferences.Editor mEditor;
    public boolean disabilityAccess = false;
    public int travelMode = 0;
    public boolean enableDarkMode = true;
    public List<Attraction> recentPlaces = new ArrayList<>();

    //Main
    DrawerLayout homeActivityMain;
    RelativeLayout homeLayout;

    //Map Overlay
    LinearLayout map;
    Space mapSpace;
    RelativeLayout mapOverlay;
    SupportMapFragment mapOverlayMap;
    TextView mapOverlayCamerr;
    LinearLayout mapOverlaySearch;
    LinearLayout mapOverlaySearchBar;
    EditText mapOverlaySearchBarText;
    Button mapOverlaySearchBarButton;
    Button mapOverlaySearchNearby;
    LinearLayout mapOverlayDirection;
    LinearLayout mapOverlayDirectionRight;
    LinearLayout mapOverlayDirectionRightAccess;
    Button mapOverlayDirectionRightAccessButton;
    LinearLayout mapOverlayDirectionRightContent;
    LinearLayout mapOverlayDirectionRightContentBorder;
    ScrollView mapOverlayDirectionRightContentScroll;
    LinearLayout mapOverlayDirectionRightContentScrollArea;
    TextView mapOverlayDirectionRightContentScrollAreaText;
    Button mapOverlayDirectionRightContentScrollAreaClear;

    //Nearby
    LinearLayout nearby;
    LinearLayout nearbyArea;
    LinearLayout nearbyAreaHeader;
    ImageView nearbyAreaHeaderImage;
    TextView nearbyAreaHeaderText;
    ListView nearbyAreaView;

    //Results
    LinearLayout results;
    LinearLayout resultsArea;
    LinearLayout resultsAreaHeader;
    ImageView resultsAreaHeaderImage;
    TextView resultsAreaHeaderText;
    LinearLayout resultsAreaHeaderClose;
    LinearLayout resultsAreaHeaderCloseWrap;
    TextView resultsAreaHeaderCloseWrapText;
    Button resultsAreaHeaderCloseWrapButton;
    ListView resultsAreaView;

    //More
    LinearLayout more;
    LinearLayout moreArea;
    LinearLayout moreAreaClose;
    Button moreAreaCloseButton;
    TextView moreAreaName;
    LinearLayout moreAreaDistance;
    TextView moreAreaDistanceText;
    TextView moreAreaDistanceAppend;
    LinearLayout moreAreaDirections;
    Button moreAreaDirectionsButton;
    LinearLayout moreAreaIcon;
    ImageView moreAreaIconToilet;
    ImageView moreAreaIconDisability;
    ImageView moreAreaIconFood;
    ImageView moreAreaIconParking;
    ImageView moreAreaIconPaid;
    ScrollView moreAreaDescription;
    TextView moreAreaDescriptionText;

    //Camera Mask
    LinearLayout camMask;

    //Camera
    LinearLayout camera;
    Space cameraSpacer;
    RelativeLayout cameraOverlay;
    TextureView cameraOverlayMain;
    ImageButton cameraOverlayClose;
    Button cameraOverlaySnapshot;
    LinearLayout cameraOverlayFlash;

    //Tutorial
    LinearLayout camTutorial;
    LinearLayout camTutorialClose;
    Button camTutorialCloseButton;
    LinearLayout camTutorialContent;
    TextView camTutorialContentTitle;
    TextView camTutorialContentBody;

    //Fragment
    LinearLayout fragment;
    Toolbar fragmentToolbar;
    ActionBarDrawerToggle fragmentToolbarToggle;
    Menu toolbarMenu;
    FrameLayout fragmentContainer;

    //Mask
    LinearLayout mask;

    //Tutorial
    LinearLayout tutorial;
    LinearLayout tutorialClose;
    Button tutorialCloseButton;
    LinearLayout tutorialContent;
    TextView tutorialContentTitle;
    TextView tutorialContentBody;

    //Navigation
    NavigationView navigation;
    RelativeLayout navigationTutorial;
    TextView navigationTutorial1;
    TextView navigationTutorial2;
    TextView navigationTutorial3;
    TextView navigationTutorial4;
    TextView navigationTutorial5;
    TextView navigationTutorialNext;

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: When the map first opens, the information about each attraction is generated and
        stored as a list. It then checks if reading the database is necessary and acts accordingly.
        Local preferences are loaded (and overwritten if the database is read). If it is the users
        first time logging in, the tutorial is automatically activated. The map is generated and
        buttons get their on click methods added, showing and hiding content for where is necessary.

    ----------------------------------------------------------------------------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String[] tagsChev = {getString(R.string.ha_tag_hike),
                getString(R.string.ha_tag_nature),
                getString(R.string.ha_tag_outdoors),
                getString(R.string.ha_tag_parking)};
        attractionList.add(new Attraction(1,
                getString(R.string.ha_loc_1_name),
                tagsChev,
                getString(R.string.ha_loc_1_date),
                getString(R.string.ha_loc_1_short),
                getString(R.string.ha_loc_1_long),
                new LatLng(55.480574, -2.108865)));

        String[] tagsHadr = {getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_film),
                getString(R.string.ha_tag_tv),
                getString(R.string.ha_tag_museum),
                getString(R.string.ha_tag_hike),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food)};
        attractionList.add(new Attraction(2,
                getString(R.string.ha_loc_2_name),
                tagsHadr,
                getString(R.string.ha_loc_2_date),
                getString(R.string.ha_loc_2_short),
                getString(R.string.ha_loc_2_long),
                new LatLng(54.9894324,-2.6039602)));

        String[] tagsHayd = {getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_disability),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_free)};
        attractionList.add(new Attraction(3,
                getString(R.string.ha_loc_3_name),
                tagsHayd,
                getString(R.string.ha_loc_3_date),
                getString(R.string.ha_loc_3_short),
                getString(R.string.ha_loc_3_long),
                new LatLng(54.9757309,-2.2640386)));

        String[] tagsAlnC = {getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_tv),
                getString(R.string.ha_tag_film),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_disability),
                getString(R.string.ha_tag_castle)};
        attractionList.add(new Attraction(4,
                getString(R.string.ha_loc_4_name),
                tagsAlnC,
                getString(R.string.ha_loc_4_date),
                getString(R.string.ha_loc_4_short),
                getString(R.string.ha_loc_4_long),
                new LatLng(55.4155858,-1.7081144)));

        String[] tagsHoly = {getString(R.string.ha_tag_nature),
                getString(R.string.ha_tag_outdoors),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food)};
        attractionList.add(new Attraction(5,
                getString(R.string.ha_loc_5_name),
                tagsHoly,
                getString(R.string.ha_loc_5_date),
                getString(R.string.ha_loc_5_short),
                getString(R.string.ha_loc_5_long),
                new LatLng(55.6712373,-1.8195485)));

        String[] tagsBamb = {getString(R.string.ha_tag_castle),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_tv),
                getString(R.string.ha_tag_film),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_parking)};
        attractionList.add(new Attraction(6,
                getString(R.string.ha_loc_6_name),
                tagsBamb,
                getString(R.string.ha_loc_6_date),
                getString(R.string.ha_loc_6_short),
                getString(R.string.ha_loc_6_long),
                new LatLng(55.6089596,-1.7120888)));

        String[] tagsDuns = {getString(R.string.ha_tag_castle),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_free),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_parking)};
        attractionList.add(new Attraction(7,
                getString(R.string.ha_loc_7_name),
                tagsDuns,
                getString(R.string.ha_loc_7_date),
                getString(R.string.ha_loc_7_short),
                getString(R.string.ha_loc_7_long),
                new LatLng(55.4894207,-1.5971871)));

        String[] tagsKield = {getString(R.string.ha_tag_hike),
                getString(R.string.ha_tag_outdoors),
                getString(R.string.ha_tag_nature),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_disability)};
        attractionList.add(new Attraction(8,
                getString(R.string.ha_loc_8_name),
                tagsKield,
                getString(R.string.ha_loc_8_date),
                getString(R.string.ha_loc_8_short),
                getString(R.string.ha_loc_8_long),
                new LatLng(55.2344071,-2.5812507)));

        String[] tagsAlnG = {getString(R.string.ha_tag_garden),
                getString(R.string.ha_tag_outdoors),
                getString(R.string.ha_tag_nature),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_disability),
                getString(R.string.ha_tag_parking)};
        attractionList.add(new Attraction(9,
                getString(R.string.ha_loc_9_name),
                tagsAlnG,
                getString(R.string.ha_loc_9_date),
                getString(R.string.ha_loc_9_short),
                getString(R.string.ha_loc_9_long),
                new LatLng(55.414236, -1.700303)));

        String[] tagsHow = {getString(R.string.ha_tag_garden),
                getString(R.string.ha_tag_outdoors),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_free),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_disability)};
        attractionList.add(new Attraction(10,
                getString(R.string.ha_loc_10_name),
                tagsHow,
                getString(R.string.ha_loc_10_date),
                getString(R.string.ha_loc_10_short),
                getString(R.string.ha_loc_10_long),
                new LatLng(55.450631,-1.6118567)));

        String[] tagsWood = {getString(R.string.ha_tag_museum),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_free),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_disability)};
        attractionList.add(new Attraction(11,
                getString(R.string.ha_loc_11_name),
                tagsWood,
                getString(R.string.ha_loc_11_date),
                getString(R.string.ha_loc_11_short),
                getString(R.string.ha_loc_11_long),
                new LatLng(55.189789,-1.549535)));

        String[] tagsBagp = {getString(R.string.ha_tag_museum),
                getString(R.string.ha_tag_history),
                getString(R.string.ha_tag_heritage),
                getString(R.string.ha_tag_free),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_disability)};
        attractionList.add(new Attraction(12,
                getString(R.string.ha_loc_12_name),
                tagsBagp,
                getString(R.string.ha_loc_12_date),
                getString(R.string.ha_loc_12_short),
                getString(R.string.ha_loc_12_long),
                new LatLng(55.1668643,-1.6889407)));

        String[] tagsYear = {getString(R.string.ha_tag_nature),
                getString(R.string.ha_tag_outdoors),
                getString(R.string.ha_tag_hike),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_free)};
        attractionList.add(new Attraction(13,
                getString(R.string.ha_loc_13_name),
                tagsYear,
                getString(R.string.ha_loc_13_date),
                getString(R.string.ha_loc_13_short),
                getString(R.string.ha_loc_13_long),
                new LatLng(55.5499983,-2.1175095)));

        String[] tagsWhit = {getString(R.string.ha_tag_nature),
                getString(R.string.ha_tag_outdoors),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_disability),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_free)};
        attractionList.add(new Attraction(14,
                getString(R.string.ha_loc_14_name),
                tagsWhit,
                getString(R.string.ha_loc_14_date),
                getString(R.string.ha_loc_14_short),
                getString(R.string.ha_loc_14_long),
                new LatLng( 55.1306841,-1.7042712)));

        String[] tagsNewc = {getString(R.string.ha_tag_food),
                getString(R.string.ha_tag_disability),
                getString(R.string.ha_tag_parking),
                getString(R.string.ha_tag_toilets),
                getString(R.string.ha_tag_museum)};
        attractionList.add(new Attraction(15,
                getString(R.string.ha_loc_15_name),
                tagsNewc,
                getString(R.string.ha_loc_15_date),
                getString(R.string.ha_loc_15_short),
                getString(R.string.ha_loc_15_long),
                new LatLng(54.972902, -1.625872)));

        locToAchv.put(1, 16);
        locToAchv.put(2, 25);
        locToAchv.put(3, 27);
        locToAchv.put(4, 17);
        locToAchv.put(5, 18);
        locToAchv.put(6, 26);
        locToAchv.put(7, 19);
        locToAchv.put(8, 11);
        locToAchv.put(9, 20);
        locToAchv.put(10, 28);
        locToAchv.put(11, 21);
        locToAchv.put(12, 22);
        locToAchv.put(13, 23);
        locToAchv.put(14, 24);
        locToAchv.put(15, 15);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String extra = getIntent().getStringExtra(getString(R.string.ma_log_req));

        if(extra.equals(getString(R.string.ma_true))){
            justLogged = true;
        } else {
            justLogged = false;
        }

        mPreferences = getSharedPreferences(getResources().getString(R.string.pref_loc), Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.commit();

        enableDarkMode = mPreferences.getBoolean(getResources().getString(R.string.pref_dark), enableDarkMode);
        disabilityAccess = mPreferences.getBoolean(getResources().getString(R.string.pref_dis), disabilityAccess);
        travelMode = mPreferences.getInt(getResources().getString(R.string.pref_travel), travelMode);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mReferencePreference = mDatabase.getReference();
        final String uid = mAuth.getCurrentUser().getUid();
        mReferencePreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFiles = dataSnapshot;
                for(DataSnapshot key: currentFiles.getChildren()){
                    hideKeyboard();
                    Preference curr = new Preference();
                    curr.setUid(key.child(uid).getValue(Preference.class).getUid());
                    curr.setAchievementIds(key.child(uid).getValue(Preference.class).getAchievementIds());
                    curr.setDarkTheme(key.child(uid).getValue(Preference.class).getDarkTheme());
                    curr.setFirstUse(key.child(uid).getValue(Preference.class).getFirstUse());
                    curr.setMode(key.child(uid).getValue(Preference.class).getMode());
                    pref = curr;
                    dataAvailable = true;
                    firstTutorial = curr.getFirstUse();
                    //First tutorial
                    if(firstTutorial){
                        startTutorial();
                        navigation.setCheckedItem(R.id.itm_drawer_map);
                        hideKeyboard();
                        Preference userx = curr;
                        curr.setFirstUse(false);

                        updatePreference(userx.getUid(), userx);
                    }
                    loadAchievements();
                    if(justLogged){
                        enableDarkMode = curr.getDarkTheme();
                        travelMode = curr.getMode();
                        setColorScheme();
                        setThemeModeMap();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        unpackRecents();

        //Main
        homeActivityMain = findViewById(R.id.home_screen);
        homeLayout = findViewById(R.id.home_layout);

        //Map Overlay
        map = findViewById(R.id.home_screen_map);
        mapSpace = findViewById(R.id.home_screen_map_spacer);
        mapOverlay = findViewById(R.id.home_screen_map_overlay);
        mapOverlayMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.home_screen_map_overlay_map);
        mapOverlayCamerr = findViewById(R.id.home_screen_map_overlay_camerr);
        mapOverlaySearch = findViewById(R.id.home_screen_map_overlay_search);
        mapOverlaySearchBar = findViewById(R.id.home_screen_map_overlay_search_bar);
        mapOverlaySearchBarText = findViewById(R.id.home_screen_map_overlay_search_bar_text);
        mapOverlaySearchBarButton = findViewById(R.id.home_screen_map_overlay_search_bar_button);
        mapOverlaySearchNearby = findViewById(R.id.home_screen_map_overlay_search_nearby);
        mapOverlayDirection = findViewById(R.id.home_screen_map_overlay_direction);
        mapOverlayDirectionRight = findViewById(R.id.home_screen_map_overlay_direction_right);
        mapOverlayDirectionRightAccess = findViewById(R.id.home_screen_map_overlay_direction_right_access);
        mapOverlayDirectionRightAccessButton = findViewById(R.id.home_screen_map_overlay_direction_right_access_button);
        mapOverlayDirectionRightContent = findViewById(R.id.home_screen_map_overlay_direction_right_content);
        mapOverlayDirectionRightContentBorder = findViewById(R.id.home_screen_map_overlay_direction_right_content_border);
        mapOverlayDirectionRightContentScroll = findViewById(R.id.home_screen_map_overlay_direction_right_content_scroll);
        mapOverlayDirectionRightContentScrollArea = findViewById(R.id.home_screen_map_overlay_direction_right_content_scroll_area);
        mapOverlayDirectionRightContentScrollAreaText = findViewById(R.id.home_screen_map_overlay_direction_right_content_scroll_area_text);
        mapOverlayDirectionRightContentScrollAreaClear = findViewById(R.id.home_screen_map_overlay_direction_right_content_scroll_area_clear);

        //Nearby
        nearby = findViewById(R.id.home_screen_nearby);
        nearbyArea = findViewById(R.id.home_screen_nearby_area);
        nearbyAreaHeader = findViewById(R.id.home_screen_nearby_area_header);
        nearbyAreaHeaderImage = findViewById(R.id.home_screen_nearby_area_header_image);
        nearbyAreaHeaderText = findViewById(R.id.home_screen_nearby_area_header_text);
        nearbyAreaView = findViewById(R.id.home_screen_nearby_area_view);

        //Results
        results = findViewById(R.id.home_screen_results);
        resultsArea = findViewById(R.id.home_screen_results_area);
        resultsAreaHeader = findViewById(R.id.home_screen_results_area_header);
        resultsAreaHeaderImage = findViewById(R.id.home_screen_results_area_header_image);
        resultsAreaHeaderText = findViewById(R.id.home_screen_results_area_header_text);
        resultsAreaHeaderClose = findViewById(R.id.home_screen_results_area_header_close);
        resultsAreaHeaderCloseWrap = findViewById(R.id.home_screen_results_area_header_close_wrap);
        resultsAreaHeaderCloseWrapText = findViewById(R.id.home_screen_results_area_header_close_wrap_text);
        resultsAreaHeaderCloseWrapButton = findViewById(R.id.home_screen_results_area_header_close_wrap_button);
        resultsAreaView = findViewById(R.id.home_screen_results_area_view);

        //More
        more = findViewById(R.id.home_screen_more);
        moreArea = findViewById(R.id.home_screen_more_area);
        moreAreaClose = findViewById(R.id.home_screen_more_area_close);
        moreAreaCloseButton = findViewById(R.id.home_screen_more_area_close_button);
        moreAreaName = findViewById(R.id.home_screen_more_area_name);
        moreAreaDistance = findViewById(R.id.home_screen_more_area_distance);
        moreAreaDistanceText = findViewById(R.id.home_screen_more_area_distance_text);
        moreAreaDistanceAppend = findViewById(R.id.home_screen_more_area_distance_append);
        moreAreaDirections = findViewById(R.id.home_screen_more_area_directions);
        moreAreaDirectionsButton = findViewById(R.id.home_screen_more_area_directions_button);
        moreAreaIcon = findViewById(R.id.home_screen_more_area_icon);
        moreAreaIconToilet = findViewById(R.id.home_screen_more_area_icon_toilet);
        moreAreaIconDisability = findViewById(R.id.home_screen_more_area_icon_disability);
        moreAreaIconFood = findViewById(R.id.home_screen_more_area_icon_food);
        moreAreaIconParking = findViewById(R.id.home_screen_more_area_icon_parking);
        moreAreaIconPaid = findViewById(R.id.home_screen_more_area_icon_paid);
        moreAreaDescription = findViewById(R.id.home_screen_more_area_description);
        moreAreaDescriptionText = findViewById(R.id.home_screen_more_area_description_text);

        //Camera
        camera = findViewById(R.id.home_screen_camera);
        cameraSpacer = findViewById(R.id.home_screen_camera_spacer);
        cameraOverlay = findViewById(R.id.home_screen_camera_overlay);
        cameraOverlayMain = findViewById(R.id.home_screen_camera_overlay_main);
        cameraOverlayClose = findViewById(R.id.home_screen_camera_overlay_close);
        cameraOverlaySnapshot = findViewById(R.id.home_screen_camera_overlay_snapshot);
        cameraOverlayFlash = findViewById(R.id.home_screen_camera_overlay_flash);

        //CameraMask
        camMask = findViewById(R.id.home_camera_mask);

        //CamTutorial
        camTutorial = findViewById(R.id.home_screen_camera_tutorial);
        camTutorialClose = findViewById(R.id.home_screen_camera_tutorial_close);
        camTutorialCloseButton = findViewById(R.id.home_screen_camera_tutorial_close_button);
        camTutorialContent = findViewById(R.id.home_screen_camera_tutorial_content);
        camTutorialContentTitle = findViewById(R.id.home_screen_camera_tutorial_content_title);
        camTutorialContentBody = findViewById(R.id.home_screen_camera_tutorial_content_body);

        //Fragment
        fragment = findViewById(R.id.home_screen_fragment);
        fragmentToolbar = findViewById(R.id.home_screen_fragment_toolbar);
        toolbarMenu = findViewById(R.id.toolbar_menu);
        fragmentContainer = findViewById(R.id.home_screen_fragment_container);

        //Mask
        mask = findViewById(R.id.home_mask);

        //Tutorial
        tutorial = findViewById(R.id.home_screen_tutorial);
        tutorialClose = findViewById(R.id.home_screen_tutorial_close);
        tutorialCloseButton = findViewById(R.id.home_screen_tutorial_close_button);
        tutorialContent = findViewById(R.id.home_screen_tutorial_content);
        tutorialContentTitle = findViewById(R.id.home_screen_tutorial_content_title);
        tutorialContentBody = findViewById(R.id.home_screen_tutorial_content_body);

        //Navigation
        navigation = findViewById(R.id.home_screen_navigation);
        navigationTutorial = findViewById(R.id.home_screen_navigation_tutorial);
        navigationTutorial1 = findViewById(R.id.home_screen_navigation_tutorial_1);
        navigationTutorial2 = findViewById(R.id.home_screen_navigation_tutorial_2);
        navigationTutorial3 = findViewById(R.id.home_screen_navigation_tutorial_3);
        navigationTutorial4 = findViewById(R.id.home_screen_navigation_tutorial_4);
        navigationTutorial5 = findViewById(R.id.home_screen_navigation_tutorial_5);
        navigationTutorialNext = findViewById(R.id.home_screen_navigation_tutorial_next);

        getLocationPermission();

        nullFrag = new NullFragment();
        recentFrag = new RecentPlacesFragment ();
        achvFrag = new AchievementFragment();
        settFrag = new SettingFragment();
        helpFrag = new HelpFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_screen_fragment_container,
                nullFrag, getString(R.string.ha_frag_map)).commit();

        assert mapOverlayMap != null;
        mapOverlayMap.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        cameraOverlayMain.setSurfaceTextureListener(surfaceTextureListener);

        cameraOverlayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCamera();
                enableGestures();
            }
        });

        /*----------------------
            Created By Michael
         ---------------------*/
        setSupportActionBar(fragmentToolbar);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        navigation.setNavigationItemSelectedListener(this);

        //For separate icon colours
        navigation.setItemIconTintList(null);

        //Gets all elements on the page to change for the theme

        //Code below for hamburger icon
        fragmentToolbarToggle = new ActionBarDrawerToggle(this, homeActivityMain, fragmentToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        fragmentToolbarToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        homeActivityMain.addDrawerListener(fragmentToolbarToggle);

        fragmentToolbarToggle.syncState();

        tutorialCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorial.setVisibility(View.GONE);
                mask.setVisibility(View.GONE);
            }
        });

        camTutorialCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camTutorial.setVisibility(View.GONE);
                camMask.setVisibility(View.GONE);
            }
        });

        mapOverlaySearchBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery();
                closeMoreInfo();
            }
        });

        mapOverlaySearchBarText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    searchQuery();
                    return true;
                }
                return false;
            }
        });
        mapOverlaySearchBarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideNearby();
                hideMore();
                hideResultsArea();
                hideSearchResults();
                closeMoreInfo();
            }
        });

        mapOverlayDirectionRightContentScrollAreaClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 13;
                unlockAchievement(id);
                clearRoute();
                hideDirection();
            }
        });
        resultsAreaHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveResultsBox();
            }
        });

        resultsAreaHeaderCloseWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideResultsArea();
                hideSearchResults();
            }
        });

        mapOverlayDirectionRightAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDirectionsBox();
            }
        });

        nearbyArea.setTranslationY(nearbyAreaView.getMeasuredHeight());
        nearbyAreaHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNearbyBox();
                hideKeyboard();
            }
        });
        mapOverlaySearchNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialiseCamera();
                disableGestures();
                hideResultsArea();
                hideSearchResults();
                checkGotAchievement();
            }
        });
        setColorScheme();
        cameraOverlaySnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashCamera();
                unlockLocation();
            }
        });
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Creates the toolbar and navigation menu

    ----------------------------------------------------------------------------------------------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Manages navigation between pages by destroying and creating new views.

    ----------------------------------------------------------------------------------------------------*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Fragment mapFragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.ha_frag_map));
        Fragment recentPlacesFragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.ha_frag_rec));
        Fragment achievementFragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.ha_frag_ach));
        Fragment settingsFragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.ha_frag_set));
        Fragment helpFragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.ha_frag_hel));

        if(mapFragment != null && mapFragment.isVisible()){
            if(isCameraOpen){
                camTutorial.setVisibility(View.VISIBLE);
                camMask.setVisibility(View.VISIBLE);
            }else{
                tutorial.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);
            }
        }
        if(recentPlacesFragment != null && recentPlacesFragment.isVisible()){
            findViewById(R.id.fragment_recent_places_tutorial).setVisibility(View.VISIBLE);
            findViewById(R.id.fragment_recent_places_mask).setVisibility(View.VISIBLE);
        }
        if(achievementFragment != null && achievementFragment.isVisible()){
            findViewById(R.id.fragment_achievement_tutorial).setVisibility(View.VISIBLE);
            findViewById(R.id.fragment_achievement_mask).setVisibility(View.VISIBLE);
        }
        if(settingsFragment != null && settingsFragment.isVisible()){
            findViewById(R.id.fragment_settings_tutorial).setVisibility(View.VISIBLE);
            findViewById(R.id.fragment_settings_mask).setVisibility(View.VISIBLE);
        }
        if(helpFragment != null && helpFragment.isVisible()){
            findViewById(R.id.fragment_help_tutorial).setVisibility(View.VISIBLE);
            findViewById(R.id.fragment_help_mask).setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Runs to check if the user has allowed camera permissions. If not, then they are
        requested. Else, the camera opening starts.

    ----------------------------------------------------------------------------------------------------*/

    private void initialiseCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            getCameraPermission();
        } else {
            showCamera();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        MODIFIED FROM http://coderzpassion.com/android-working-camera2-api/

        THIS ALLOWS THE CAMERA TO OPEN AND CLOSE WHEN NECESSARY

    ----------------------------------------------------------------------------------------------------*/

    public void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String camerId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(camerId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            previewsize = map.getOutputSizes(SurfaceTexture.class)[0];
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(camerId, stateCallback, null);
        }catch (Exception e){
            Log.e(getString(R.string.ha_camera_error), e.getLocalizedMessage());
        }
    }

    /*----------------------------------------------------------------------------------------------------
        MODIFIED FROM http://coderzpassion.com/android-working-camera2-api/

        THIS ALLOWS THE CAMERA TO DRAW WHAT IT SEES ONTO A TEXTURE VIEW

    ----------------------------------------------------------------------------------------------------*/

    private TextureView.SurfaceTextureListener surfaceTextureListener=new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    /*----------------------------------------------------------------------------------------------------
        MODIFIED FROM http://coderzpassion.com/android-working-camera2-api/

        INITIALLY LOADING AND OPENING THE CAMERA USING A CALLBACK TO CHECK IF COMMUNICATION SUCCEEDED

    ----------------------------------------------------------------------------------------------------*/

    private CameraDevice.StateCallback stateCallback=new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice=camera;
            startCamera();
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
        }
        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
        }
    };

    /*----------------------------------------------------------------------------------------------------
        MODIFIED FROM http://coderzpassion.com/android-working-camera2-api/

        INITIALLISES THE CAMERA DEVICE AND THE TEXTURE VIEW WHERE IT WILL DRAW TO

    ----------------------------------------------------------------------------------------------------*/

    void  startCamera(){
        if(cameraDevice==null||!cameraOverlayMain.isAvailable()|| previewsize==null){
            return;
        }
        SurfaceTexture texture=cameraOverlayMain.getSurfaceTexture();
        if(texture==null){
            return;
        }
        texture.setDefaultBufferSize(previewsize.getWidth(),previewsize.getHeight());
        Surface surface=new Surface(texture);
        try{
            previewBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        }catch (Exception e){
            Log.e(getString(R.string.ha_camera_error), e.getLocalizedMessage());
        }
        previewBuilder.addTarget(surface);
        try{
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    previewSession=session;
                    getChangedPreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, null);
        }catch (Exception e){
            Log.e(getString(R.string.ha_camera_error), e.getLocalizedMessage());
        }
    }

    /*----------------------------------------------------------------------------------------------------
        MODIFIED FROM http://coderzpassion.com/android-working-camera2-api/

        ADJUSTS THE IMAGE ON THE TEXTURE VIEW FOR WHEN THE INFORMATION FROM THE CAMERA CHANGES
        BE IT FROM MOVING OR CLOSING.

    ----------------------------------------------------------------------------------------------------*/

    void getChangedPreview(){
        if(cameraDevice==null){
            return;
        }
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread=new HandlerThread(getString(R.string.ha_camera_handler));
        thread.start();
        Handler handler=new Handler(thread.getLooper());
        try{
            previewSession.setRepeatingRequest(previewBuilder.build(), null, handler);
        }catch (Exception e){
            Log.e(getString(R.string.ha_camera_error), e.getLocalizedMessage());
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Causes a fake camera flash for some feedback for the user

    ----------------------------------------------------------------------------------------------------*/
    private void flashCamera(){
        cameraOverlayFlash.setVisibility(View.VISIBLE);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flash);
        cameraOverlayFlash.startAnimation(aniFade);
        cameraOverlayFlash.setVisibility(View.INVISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Hides the camera view.

    ----------------------------------------------------------------------------------------------------*/
    private void hideCamera(){
        cameraOverlay.setVisibility(View.INVISIBLE);
        isCameraOpen = false;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Opens the camera view and starts it.

    ----------------------------------------------------------------------------------------------------*/
    private void showCamera(){
        cameraOverlay.setVisibility(View.VISIBLE);
        startCamera();
        isCameraOpen = true;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Initialises the pre required criteria to allow achievements to be unlocked, by
        reading the motion sensor in the phone to see if it is upright

    ----------------------------------------------------------------------------------------------------*/

    private void checkGotAchievement(){
        boolean gyroExists = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        if(gyroExists){
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            eventListener = new SensorEventListener(){
                @Override
                public void onSensorChanged(SensorEvent sensorEvent){
                    if(sensorEvent.values[1] > 8f){
                        //Device is upright
                        //Allow unlocking of camera based achievements
                        isUpright = true;
                    } else {
                        isUpright = false;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            mSensorManager.registerListener(eventListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            isUpright = true;
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Runs through multiple criteria for different location based achievements
        to see if the current users data matches that of the criteria to make it pass and become
        unlocked.

    ----------------------------------------------------------------------------------------------------*/

    void unlockLocation(){
        if(isUpright){
            int castleCounter = 0;
            for(Attraction loc: attractionList){
                double dist = getDistance(loc.getLocation().latitude, loc.getLocation().longitude, mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())/1000;
                if(dist <= CAMERADISTANCE){
                    loadAchievements();
                    int locId = loc.getId();
                    int id = locToAchv.get(locId);
                    unlockAchievement(id);
                    boolean isMuseum = false;
                    for(String tag: loc.getTags()){
                        if(tag.equals(getString(R.string.ha_tag_museum))){
                            isMuseum = true;
                        }
                    }
                    boolean isCastle = false;
                    for(String tag: loc.getTags()){
                        if(tag.equals(getString(R.string.ha_tag_castle))){
                            isMuseum = true;
                        }
                    }
                    if(isCastle){
                        castleCounter ++;
                        if(castleCounter > 3){
                            id = 12;
                            unlockAchievement(id);
                        }
                    }
                    if(isMuseum){
                        id = 9;
                        unlockAchievement(id);
                    }
                }
            }
            int counter = 0;
            for(Attraction loc: attractionList){
                if(loc.getId() > 15|| loc.getId() == 11){
                    counter ++;
                }
            }
            if(counter >= 10){
                int id = 4;
                unlockAchievement(id);
            }
            if(counter == 15){
                int id = 3;
                unlockAchievement(id);
            }
        }else{
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, getString(R.string.ha_toast_upright), duration);
            toast.show();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Once the google map is ready, it is displayed and information is injected in,
        such as the pin points to allow users to see where locations are on the map.
        Also checks for when the user moves, to update certain parts like nearby location and if
        there is a location close enough to open the camera.

    ----------------------------------------------------------------------------------------------------*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setLatLngBoundsForCameraTarget(NORTHUMBERLAND);
        mMap.setMinZoomPreference(minZoom);
        mMap.setMaxZoomPreference(maxZoom);

        getDeviceLocation();
        updateLocationUI();

        Chev = mMap.addMarker(new MarkerOptions().position(attractionList.get(0).getLocation()).title(attractionList.get(0).getName()).visible(false));
        Hadr = mMap.addMarker(new MarkerOptions().position(attractionList.get(1).getLocation()).title(attractionList.get(1).getName()).visible(false));
        Hayd = mMap.addMarker(new MarkerOptions().position(attractionList.get(2).getLocation()).title(attractionList.get(2).getName()).visible(false));
        AlnC = mMap.addMarker(new MarkerOptions().position(attractionList.get(3).getLocation()).title(attractionList.get(3).getName()).visible(false));
        Holy = mMap.addMarker(new MarkerOptions().position(attractionList.get(4).getLocation()).title(attractionList.get(4).getName()).visible(false));
        Bamb = mMap.addMarker(new MarkerOptions().position(attractionList.get(5).getLocation()).title(attractionList.get(5).getName()).visible(false));
        Duns = mMap.addMarker(new MarkerOptions().position(attractionList.get(6).getLocation()).title(attractionList.get(6).getName()).visible(false));
        Kiel = mMap.addMarker(new MarkerOptions().position(attractionList.get(7).getLocation()).title(attractionList.get(7).getName()).visible(false));
        AlnG = mMap.addMarker(new MarkerOptions().position(attractionList.get(8).getLocation()).title(attractionList.get(8).getName()).visible(false));
        Howc = mMap.addMarker(new MarkerOptions().position(attractionList.get(9).getLocation()).title(attractionList.get(9).getName()).visible(false));
        Wood = mMap.addMarker(new MarkerOptions().position(attractionList.get(10).getLocation()).title(attractionList.get(10).getName()).visible(false));
        Bagp = mMap.addMarker(new MarkerOptions().position(attractionList.get(11).getLocation()).title(attractionList.get(11).getName()).visible(false));
        Yeav = mMap.addMarker(new MarkerOptions().position(attractionList.get(12).getLocation()).title(attractionList.get(12).getName()).visible(false));
        Whit = mMap.addMarker(new MarkerOptions().position(attractionList.get(13).getLocation()).title(attractionList.get(13).getName()).visible(false));
        Newc = mMap.addMarker(new MarkerOptions().position(attractionList.get(14).getLocation()).title(attractionList.get(14).getName()).visible(false));

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                boolean cameraIsReady = false;
                ArrayList<Attraction> nearbyOverall = new ArrayList<>();
                for(int i = 0; i < attractionList.size(); i++){
                    if(mLastKnownLocation != null){
                        if((Math.abs(getDistance(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), attractionList.get(i).getLocation().latitude, attractionList.get(i).getLocation().longitude))/1000) <= CAMERADISTANCE){
                            cameraIsReady = true;
                        }
                    }
                }
                HashMap<Double, Attraction> locationDistance = new HashMap<>();
                for(int i = 0; i < attractionList.size(); i++){
                    if(mLastKnownLocation != null){
                        locationDistance.put(Math.abs(getDistance(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), attractionList.get(i).getLocation().latitude, attractionList.get(i).getLocation().longitude)), attractionList.get(i));
                    }
                }
                List<Double> distances = new ArrayList<>(locationDistance.keySet());
                Collections.sort(distances);
                int nearbyListAmount = 3;
                for(int i = 0; i < nearbyListAmount; i++){
                    nearbyOverall.add(locationDistance.get(distances.get(i)));
                }
                if(cameraIsReady){
                    showLocationNearby();
                } else {
                    hideLocationNearby();
                }
                displayNearby(nearbyOverall);
            }
        });
        hideCamera();
        hideNearby();
        hideMore();
        hideDirection();
        hideResultsArea();
        hideLocationNearby();
        hideKeyboard();
        hideSearchResults();
        closeMoreInfo();
        setThemeModeMap();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Shows the nearby section.

    ----------------------------------------------------------------------------------------------------*/
    private void showLocationNearby(){
        mapOverlaySearchNearby.setVisibility(View.VISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Hides the nearby section.

    ----------------------------------------------------------------------------------------------------*/
    private void hideLocationNearby(){
        mapOverlaySearchNearby.setVisibility(View.INVISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Checks all locations and the distance to them to see which 3 are the closest.
        These 3 are then put into a list which displays at the bottom of the screen to show what is
        nearest.

    ----------------------------------------------------------------------------------------------------*/
    public void displayNearby(ArrayList<Attraction> nearby){
        if(nearby.size() == 0){
            nearby.add(new Attraction(0,
                    getString(R.string.ha_nearby_empty),
                    new String[0],
                    "",
                    "",
                    "",
                    new LatLng(0, 0)));
        }

        final HashMap<String, String> temp = getResultsShort(nearby);
        final List<String> names = new ArrayList<>(temp.keySet());
        final Context context = this;
        final boolean colorTheme = enableDarkMode;

        ListAdapter la = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {
            }

            @Override
            public int getCount() {
                return temp.size();
            }

            @Override
            public Object getItem(int position) {
                return names.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                String title = (String) getItem(position);

                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    if(colorTheme){
                        convertView = layoutInflater.inflate(R.layout.searchresultdark, null);
                    } else {
                        convertView = layoutInflater.inflate(R.layout.searchresultlight, null);
                    }
                }

                TextView txvSearchTitle = convertView.findViewById(R.id.search_info_title);
                TextView txvSearchDesc = convertView.findViewById(R.id.search_info_description);
                Button moreInfoButton = convertView.findViewById(R.id.search_more_button);

                if(title.equals(getString(R.string.ha_nearby_empty))){
                    txvSearchTitle.setText(title);
                    moreInfoButton.setVisibility(View.INVISIBLE);
                    txvSearchDesc.setVisibility(View.INVISIBLE);
                }
                final String name = title;
                moreInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openedWithSearch = false;
                        displayMoreInfo(name);
                    }
                });

                txvSearchTitle.setText(title);
                txvSearchDesc.setText(temp.get(title));
                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return temp.size();
            }

            @Override
            public boolean isEmpty() {
                return getCount() != 0;
            }
        };
        nearbyAreaView.setAdapter(la);
        if(firstNearbyShow){
            firstNearbyShow = false;
            nearbyArea.setTranslationY(nearbyAreaView.getMeasuredHeight());
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Updates the map pin for the user when their location has changed, but only if they
        have enabled loacation services. Displays nearby if location is enabled

    ----------------------------------------------------------------------------------------------------*/
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                if(mapOverlayDirection.getVisibility() == View.INVISIBLE){
                    showNearby();
                }
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e(getString(R.string.ha_map_error), e.getMessage());
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Gets the location of the user and moves the camera to that location when the map
        opens. This only happens when location is enabled.

    ----------------------------------------------------------------------------------------------------*/
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener( this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            assert mLastKnownLocation != null;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), (minZoom + maxZoom) / 2));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((neLat + swLat) / 2, (neLng + swLng) / 2), (minZoom + maxZoom) / 2));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e(getString(R.string.ha_map_error), e.getMessage());
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Disables being able to control the map through certain things like fragment
        backgrounds.

    ----------------------------------------------------------------------------------------------------*/
    private void disableGestures(){
        mMap.getUiSettings().setAllGesturesEnabled(false);
    }

    /*----------------------------------------------------------------------------------------------------
            Created By: Samuel Holley

            Modified By: -----

            Description: Re enables the gesture controls for the map.

        ----------------------------------------------------------------------------------------------------*/
    private void enableGestures(){
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Uses custom XML to style the map to a dark theme when dark mode is enabled.

    ----------------------------------------------------------------------------------------------------*/
    private void setThemeModeMap(){
        if(enableDarkMode){
            boolean successMap = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.darktheme));
            if(!successMap) {
                Log.e(getString(R.string.ha_map_theme_error), getString(R.string.ha_map_theme_response));
            }
        } else {
            boolean successMap = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.normaltheme));
            if(!successMap) {
                Log.e(getString(R.string.ha_map_theme_error), getString(R.string.ha_map_theme_response));
            }
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Changes all visible components of the screen to make the colours match that of the
        current selected colour theme. Also changes the colour of the line drawn for the directions.

    ----------------------------------------------------------------------------------------------------*/

    public void setColorScheme(){

        int translate = mapOverlayDirectionRightContent.getMeasuredWidth();

        if(enableDarkMode){
            mapOverlaySearchBar.setBackgroundResource(R.drawable.theme_dark_button_square);
            mapOverlaySearchBarText.setHintTextColor(getResources().getColor(R.color.colorDarkAccent));
            mapOverlaySearchBarText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            mapOverlaySearchBarText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkAccent)));
            mapOverlayCamerr.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            mapOverlayCamerr.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            resultsAreaHeader.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            resultsAreaHeaderImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            resultsAreaHeaderText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            resultsAreaHeaderCloseWrapText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            moreArea.setBackgroundColor(getResources().getColor(R.color.colorDarkTranslucent));
            moreAreaName.setTextColor(getResources().getColor(R.color.colorDarkSecondary));
            moreAreaDistanceText.setTextColor(getResources().getColor(R.color.colorDarkSecondary));
            moreAreaDistanceAppend.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            moreAreaDirectionsButton.setBackgroundResource(R.drawable.theme_dark_button_square);
            moreAreaDirectionsButton.setTextColor(getResources().getColor(R.color.colorDarkSecondary));
            moreAreaDescriptionText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentToolbar.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            fragmentToolbar.setTitleTextColor(getResources().getColor(R.color.colorDarkSecondary));
            fragmentToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorDarkSecondary));
            mask.setBackgroundColor(getResources().getColor(R.color.colorDarkTranslucent));
            tutorial.setBackground(getDrawable(R.drawable.theme_dark_button_square_v2));
            tutorialContentTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            tutorialContentBody.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            camTutorial.setBackground(getDrawable(R.drawable.theme_dark_button_square_v2));
            camTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            camTutorialContentBody.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            navigationTutorial.setBackgroundColor(getResources().getColor(R.color.colorDarkTranslucent));
            navigationTutorial1.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            navigationTutorial2.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            navigationTutorial3.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            navigationTutorial4.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            navigationTutorial5.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            navigationTutorialNext.setBackgroundResource(R.drawable.theme_dark_button_square);
            navigationTutorialNext.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            navigation.setBackgroundResource(R.drawable.theme_dark_navigation);
            navigation.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkAccent)));
            setTheme(R.style.theme_dark_navigation);
            fragmentToolbarToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorDarkSecondary));
            if(resultPoly != null){
                resultPoly.setColor(getResources().getColor(R.color.colorDarkSecondary));
            }
            if(mapOverlayDirectionRight.getTranslationX() == translate){
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_left_dark);
            } else {
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_dark);
            }
            mapOverlayDirectionRightContent.setBackgroundColor(getResources().getColor(R.color.colorDarkTranslucent));
            mapOverlayDirectionRightContentScrollAreaText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            mapOverlaySearchNearby.setBackgroundResource(R.drawable.theme_dark_button_square);
            mapOverlaySearchNearby.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            nearbyAreaHeader.setBackgroundColor(getResources().getColor(R.color.colorDarkSecondary));
            nearbyAreaHeaderImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkAccent)));
            nearbyAreaHeaderText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            mapOverlayDirectionRightContentBorder.setBackgroundColor(getResources().getColor(R.color.colorDarkSecondary));
            mapOverlayDirectionRightContentScrollAreaClear.setBackgroundResource(R.drawable.theme_dark_button_round);
            mapOverlayDirectionRightContentScrollAreaClear.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            moreAreaIconDisability.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            moreAreaIconFood.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            moreAreaIconToilet.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            moreAreaIconParking.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
            moreAreaIconPaid.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkSecondary)));
        } else {
            mapOverlaySearchBar.setBackgroundResource(R.drawable.theme_light_button_square);
            mapOverlaySearchBarText.setHintTextColor(getResources().getColor(R.color.colorLightAccent));
            mapOverlaySearchBarText.setTextColor(getResources().getColor(R.color.colorLightAccent));
            mapOverlaySearchBarText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightAccent)));
            mapOverlayCamerr.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            mapOverlayCamerr.setTextColor(getResources().getColor(R.color.colorLightAccent));
            resultsAreaHeader.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            resultsAreaHeaderImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightSecondary)));
            resultsAreaHeaderText.setTextColor(getResources().getColor(R.color.colorLightSecondary));
            resultsAreaHeaderCloseWrapText.setTextColor(getResources().getColor(R.color.colorLightAccent));
            moreArea.setBackgroundColor(getResources().getColor(R.color.colorLightTranslucent));
            moreAreaName.setTextColor(getResources().getColor(R.color.colorLightSecondary));
            moreAreaDistanceText.setTextColor(getResources().getColor(R.color.colorLightSecondary));
            moreAreaDistanceAppend.setTextColor(getResources().getColor(R.color.colorLightAccent));
            moreAreaDirectionsButton.setBackgroundResource(R.drawable.theme_light_button_square);
            moreAreaDirectionsButton.setTextColor(getResources().getColor(R.color.colorLightSecondary));
            moreAreaDescriptionText.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentToolbar.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            fragmentToolbar.setTitleTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentToolbar.getContext().setTheme(R.style.theme_light_navigation);
            mask.setBackgroundColor(getResources().getColor(R.color.colorLightTranslucent));
            tutorial.setBackground(getDrawable(R.drawable.theme_light_button_square_v2));
            tutorialContentTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            tutorialContentBody.setTextColor(getResources().getColor(R.color.colorLightAccent));
            camTutorial.setBackground(getDrawable(R.drawable.theme_light_button_square_v2));
            camTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            camTutorialContentBody.setTextColor(getResources().getColor(R.color.colorLightAccent));
            navigationTutorial.setBackgroundColor(getResources().getColor(R.color.colorLightTranslucent));
            navigationTutorial1.setTextColor(getResources().getColor(R.color.colorLightAccent));
            navigationTutorial2.setTextColor(getResources().getColor(R.color.colorLightAccent));
            navigationTutorial3.setTextColor(getResources().getColor(R.color.colorLightAccent));
            navigationTutorial4.setTextColor(getResources().getColor(R.color.colorLightAccent));
            navigationTutorial5.setTextColor(getResources().getColor(R.color.colorLightAccent));
            navigationTutorialNext.setBackgroundResource(R.drawable.theme_light_button_square);
            navigationTutorialNext.setTextColor(getResources().getColor(R.color.colorLightAccent));
            navigation.setBackgroundResource(R.drawable.theme_light_navigation);
            navigation.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorLightAccent)));
            setTheme(R.style.theme_light_navigation);
            fragmentToolbarToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorLightAccent));
            if(resultPoly != null){
                resultPoly.setColor(getResources().getColor(R.color.colorLightSecondary));
            }
            if(mapOverlayDirectionRight.getTranslationX() == translate){
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_left_light);
            } else {
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_light);
            }
            mapOverlayDirectionRightContent.setBackgroundColor(getResources().getColor(R.color.colorLightTranslucent));
            mapOverlayDirectionRightContentScrollAreaText.setTextColor(getResources().getColor(R.color.colorLightAccent));
            mapOverlaySearchNearby.setBackgroundResource(R.drawable.theme_light_button_square);
            mapOverlaySearchNearby.setTextColor(getResources().getColor(R.color.colorLightAccent));
            nearbyAreaHeader.setBackgroundColor(getResources().getColor(R.color.colorLightSecondary));
            nearbyAreaHeaderImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            nearbyAreaHeaderText.setTextColor(getResources().getColor(R.color.colorLightPrimary));
            mapOverlayDirectionRightContentBorder.setBackgroundColor(getResources().getColor(R.color.colorLightSecondary));
            mapOverlayDirectionRightContentScrollAreaClear.setBackgroundResource(R.drawable.theme_light_button_round);
            mapOverlayDirectionRightContentScrollAreaClear.setTextColor(getResources().getColor(R.color.colorLightAccent));
            moreAreaIconDisability.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            moreAreaIconFood.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            moreAreaIconToilet.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            moreAreaIconParking.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
            moreAreaIconPaid.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLightPrimary)));
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Requests location permissions from the user to display your current location and
        other location requiring functions.

    ----------------------------------------------------------------------------------------------------*/
    public void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Requests permissions to use the camera to allow camera requiring functions
    ----------------------------------------------------------------------------------------------------*/

    public void getCameraPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Checks which permission was either granted or rejected and adjusts settings
        accordingly to make this information easier to access.

    ----------------------------------------------------------------------------------------------------*/
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCamera();
                } else {
                    flashCameraError();
                }
            }
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationUI();
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Displays a box saying the camera is not enabled when trying to access the camera
        without allowing camera permissions.

    ----------------------------------------------------------------------------------------------------*/

    private void flashCameraError(){
        mapOverlayCamerr.setVisibility(View.VISIBLE);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        mapOverlayCamerr.startAnimation(aniFade);
        mapOverlayCamerr.setVisibility(View.INVISIBLE);
    }

    //For the NavBar

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: Samuel Holley

        Description: Switches out the fragment overlay for each option on the navigation bar when
        an item is selected. Resets colours for the map when they have been changed within another
        fragment.

    ----------------------------------------------------------------------------------------------------*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        hideCamera();
        disableGestures();
        hideSearchResults();
        hideSearchResults();
        homeActivityMain.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.itm_drawer_map:
                homeLayout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_screen_fragment_container,
                        nullFrag, getString(R.string.ha_frag_map)).commit();
                getDeviceLocation();
                updateLocationUI();
                setThemeModeMap();
                enableGestures();
                break;
            case R.id.itm_drawer_recent_places:
                homeLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_screen_fragment_container,
                        recentFrag, getString(R.string.ha_frag_rec)).commit();
                break;
            case R.id.itm_drawer_achievements:
                homeLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_screen_fragment_container,
                        achvFrag, getString(R.string.ha_frag_ach)).commit();
                break;
            case R.id.itm_drawer_settings:
                homeLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_screen_fragment_container,
                        settFrag, getString(R.string.ha_frag_set)).commit();
                break;
            case R.id.itm_drawer_help:
                homeLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_screen_fragment_container,
                        helpFrag, getString(R.string.ha_frag_hel)).commit();
                break;
            case R.id.tutorial:
                disableGestures();
                startTutorial();
                break;
        }
        return true;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Starts the run through of the tutorial.

    ----------------------------------------------------------------------------------------------------*/

    public void startTutorial() {
        disableMenuItem();
        tutorialCounter = 0;
        homeActivityMain.openDrawer(GravityCompat.START);
        navigationTutorial.setVisibility(View.VISIBLE);
        navigationTutorialNext.setText(getString(R.string.ha_tut_start));
        navigationTutorialNext.setVisibility(View.VISIBLE);
        navigationTutorialNext.setClickable(true);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Iterates through each tutoral section until the end. Uer navigates by pressing
        a button for the next hint.

    ----------------------------------------------------------------------------------------------------*/
    public void drawerTutorial(View view) {
        tutorialCounter ++;
        switch (tutorialCounter) {
            case 1:
                navigationTutorialNext.setText(getString(R.string.ha_tut_mid));
                navigationTutorial1.setVisibility(View.VISIBLE);
                break;
            case 2:
                navigationTutorial1.setVisibility(View.INVISIBLE);
                navigationTutorial2.setVisibility(View.VISIBLE);
                break;
            case 3:
                navigationTutorial2.setVisibility(View.INVISIBLE);
                navigationTutorial3.setVisibility(View.VISIBLE);
                break;
            case 4:
                navigationTutorial3.setVisibility(View.INVISIBLE);
                navigationTutorial4.setVisibility(View.VISIBLE);
                break;
            case 5:
                navigationTutorial4.setVisibility(View.INVISIBLE);
                navigationTutorial5.setVisibility(View.VISIBLE);
                navigationTutorialNext.setText(getString(R.string.ha_tut_end));
                tutorialCounter = 99;
                break;
            case 100:
                navigationTutorial5.setVisibility(View.INVISIBLE);
                navigationTutorialNext.setVisibility(View.INVISIBLE);
                navigationTutorial.setVisibility(View.INVISIBLE);
                homeActivityMain.closeDrawer(GravityCompat.START);
                enableMenuItem();
                if(firstTutorial){
                    tutorial.setVisibility(View.VISIBLE);
                    mask.setVisibility(View.VISIBLE);
                }
                firstTutorial = false;
                break;
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Disables navigation controls when the tutorial is in session

    ----------------------------------------------------------------------------------------------------*/
    public void disableMenuItem(){
        Menu menu = navigation.getMenu();
        menu.findItem(R.id.itm_drawer_map).setEnabled(false);
        menu.findItem(R.id.itm_drawer_recent_places).setEnabled(false);
        menu.findItem(R.id.itm_drawer_settings).setEnabled(false);
        menu.findItem(R.id.itm_drawer_help).setEnabled(false);
        menu.findItem(R.id.itm_drawer_achievements).setEnabled(false);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Re enables navigation control.

    ----------------------------------------------------------------------------------------------------*/
    public void enableMenuItem(){
        Menu menu = navigation.getMenu();
        menu.findItem(R.id.itm_drawer_map).setEnabled(true);
        menu.findItem(R.id.itm_drawer_recent_places).setEnabled(true);
        menu.findItem(R.id.itm_drawer_settings).setEnabled(true);
        menu.findItem(R.id.itm_drawer_help).setEnabled(true);
        menu.findItem(R.id.itm_drawer_achievements).setEnabled(true);
    }


    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Overwrites the default action for when back is pressed to only close the navigation
        menu.

    ----------------------------------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        if (homeActivityMain.isDrawerOpen(GravityCompat.START)) {
            homeActivityMain.closeDrawer(GravityCompat.START);
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Uses a custom search algorithm to use the users input to gather locations to
        display. Using a # for tags and an @ for place names, it gets locations which match all tags,
        and locations that match each individual @. Results are then transferred.

    ----------------------------------------------------------------------------------------------------*/
    private void searchQuery(){
        String query = mapOverlaySearchBarText.getText().toString();
        if(query.length() == 0){
            return;
        }
        if((query.charAt(0) != '@') && (query.charAt(0) != '#')){
            query = getString(R.string.ha_search_place) + query;
        }

        ArrayList<String> tagsAndPlaces = new ArrayList<>();
        if(query.startsWith(getString(R.string.ha_search_place))){
            String firstSplit[] = query.split(getString(R.string.ha_search_place));
            for(int i = 1; i < firstSplit.length; i++){
                firstSplit[i] = getString(R.string.ha_search_place) + firstSplit[i];
                String secondSplit[] = firstSplit[i].split(getString(R.string.ha_search_tag));
                tagsAndPlaces.add(secondSplit[0]);
                for(int j = 1; j < secondSplit.length; j++){
                    secondSplit[j] = getString(R.string.ha_search_tag) + secondSplit[j];
                    tagsAndPlaces.add(secondSplit[j]);
                }
            }
        } else {
            String firstSplit[] = query.split(getString(R.string.ha_search_tag));
            for(int i = 1; i < firstSplit.length; i++){
                firstSplit[i] = getString(R.string.ha_search_tag) + firstSplit[i];
                String secondSplit[] = firstSplit[i].split(getString(R.string.ha_search_place));
                tagsAndPlaces.add(secondSplit[0]);
                for(int j = 1; j < secondSplit.length; j++){
                    secondSplit[j] = getString(R.string.ha_search_place) + secondSplit[j];
                    tagsAndPlaces.add(secondSplit[j]);
                }
            }
        }

        //Sort into tags and places
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> places = new ArrayList<>();
        if(disabilityAccess){
            tags.add(getString(R.string.ha_tag_disability)); //Add it as default to remove non access areas
        }
        for(int i = 0; i < tagsAndPlaces.size(); i++){
            if(tagsAndPlaces.get(i).startsWith(getString(R.string.ha_search_place))){
                places.add(tagsAndPlaces.get(i).replace(getString(R.string.ha_search_place), "").replace(" ", "").toLowerCase());
            }else{
                tags.add(tagsAndPlaces.get(i).replace(getString(R.string.ha_search_tag),"").replace(" ", "").toLowerCase());
            }
        }
        //Stops repeat values by popping off all the places selected already
        ArrayList<Attraction> attractionResults = new ArrayList<>();
        for(int i = 0; i < attractionList.size(); i++){
            boolean queryPass = false;
            String newname = attractionList.get(i).getName().toLowerCase().replace(" ", "");
            for(int j = 0; j < places.size(); j++){
                if(newname.contains(places.get(j))){
                    queryPass = true;
                }
            }
            int tagMatches = 0;
            for(int j = 0; j < tags.size(); j++){
                for(int k = 0; k < attractionList.get(i).getTags().length; k++) {
                    if (attractionList.get(i).getTags()[k].toLowerCase().equals(tags.get(j))) {
                        tagMatches++;
                    }
                }
            }
            if(tagMatches == tags.size() && tags.size() != 0){
                queryPass = true;
            }
            if(queryPass){
                attractionResults.add(attractionList.get(i));
            }
        }
        if(mapOverlaySearchBarText.getText().toString().toLowerCase().contains(getString(R.string.ha_search_all))){
            attractionResults.clear();
            if(!disabilityAccess){
                attractionResults = attractionList;
            } else {
                places.clear();
                tags.clear();
                tags.add(getString(R.string.ha_tag_disability));
                for(int i = 0; i < attractionList.size(); i++){
                    boolean queryPass = false;
                    int tagMatches = 0;
                    for(int j = 0; j < tags.size(); j++){
                        for(int k = 0; k < attractionList.get(i).getTags().length; k++) {
                            if (attractionList.get(i).getTags()[k].toLowerCase().equals(tags.get(j).toLowerCase())) {
                                tagMatches++;
                            }
                        }
                    }
                    if(tagMatches == tags.size() && tags.size() != 0){
                        queryPass = true;
                    }
                    if(queryPass){
                        attractionResults.add(attractionList.get(i));
                    }
                }
            }
        }
        hideSearchResults();
        displaySearchResults(attractionResults);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Hides the markers for each location after a search has been made so that the
        correct ones will display.

    ----------------------------------------------------------------------------------------------------*/
    private void hideSearchResults(){
        Chev.setVisible(false);
        Hadr.setVisible(false);
        Hayd.setVisible(false);
        AlnC.setVisible(false);
        Holy.setVisible(false);
        Bamb.setVisible(false);
        Duns.setVisible(false);
        Kiel.setVisible(false);
        AlnG.setVisible(false);
        Howc.setVisible(false);
        Wood.setVisible(false);
        Bagp.setVisible(false);
        Yeav.setVisible(false);
        Whit.setVisible(false);
        Newc.setVisible(false);
        if(resultMarker != null){
            resultMarker.setVisible(true);
        }
        showNearby();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Checks to see which attractions match the search criteria and stores them.
        Each result's map marker is then shown.

    ----------------------------------------------------------------------------------------------------*/
    private void displaySearchResults(ArrayList<Attraction> attractionResults){
        for(int i = 0; i < attractionResults.size(); i++){
            switch(attractionResults.get(i).getId() - 1){
                case (0):
                    Chev.setVisible(true);
                    break;
                case (1):
                    Hadr.setVisible(true);
                    break;
                case (2):
                    Hayd.setVisible(true);
                    break;
                case (3):
                    AlnC.setVisible(true);
                    break;
                case (4):
                    Holy.setVisible(true);
                    break;
                case (5):
                    Bamb.setVisible(true);
                    break;
                case (6):
                    Duns.setVisible(true);
                    break;
                case (7):
                    Kiel.setVisible(true);
                    break;
                case (8):
                    AlnG.setVisible(true);
                    break;
                case (9):
                    Howc.setVisible(true);
                    break;
                case (10):
                    Wood.setVisible(true);
                    break;
                case (11):
                    Bagp.setVisible(true);
                    break;
                case (12):
                    Yeav.setVisible(true);
                    break;
                case (13):
                    Whit.setVisible(true);
                    break;
                case (14):
                    Newc.setVisible(true);
                    break;
                default:
                    break;
            }
        }
        setContentResults(attractionResults);
        hideNearby();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Gets the short description for a list of attractions and returns it as a map.

    ----------------------------------------------------------------------------------------------------*/
    private HashMap<String, String> getResultsShort(ArrayList<Attraction> attractionResults){
        HashMap<String, String> ret = new HashMap<>();
        for(int i = 0; i < attractionResults.size(); i++){
            ret.put(attractionResults.get(i).getName(), attractionResults.get(i).getShortDesc());
        }
        return ret;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A method for hiding the keyboard as the focus is messed up with the test phone
        and the keyboard won't automatically hide, yet will randomly open.

    ----------------------------------------------------------------------------------------------------*/
    public void hideKeyboard(){
        InputMethodManager IMM = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        IMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Creates a visible list of attractions to display over the map, with a button next
        to each to allow the user to look at more information and get directions.

    ----------------------------------------------------------------------------------------------------*/
    private void setContentResults(ArrayList<Attraction> attractionResults){

        hideKeyboard();

        if(attractionResults.size() == 0){
            attractionResults.add(new Attraction(0,
                    getString(R.string.ha_search_fail),
                    new String[0],
                    "",
                    "",
                    "",
                    new LatLng(0, 0)));
        }

        final HashMap<String, String> temp = getResultsShort(attractionResults);
        final List<String> names = new ArrayList<>(temp.keySet());
        final Context context = this;
        final boolean colorTheme = enableDarkMode;

        ListAdapter la = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return temp.size();
            }

            @Override
            public Object getItem(int position) {
                return names.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                String title = (String) getItem(position);

                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    if(colorTheme){
                        convertView = layoutInflater.inflate(R.layout.searchresultdark, null);
                    } else {
                        convertView = layoutInflater.inflate(R.layout.searchresultlight, null);
                    }
                }
                TextView txvSearchTitle = convertView.findViewById(R.id.search_info_title);
                Button moreInfoButton = convertView.findViewById(R.id.search_more_button);
                TextView txvSearchDesc = convertView.findViewById(R.id.search_info_description);
                if(title.equals(getString(R.string.ha_search_fail))){
                    txvSearchTitle.setText(title);
                    moreInfoButton.setVisibility(View.INVISIBLE);
                    txvSearchDesc.setVisibility(View.INVISIBLE);
                }
                final String name = title;
                moreInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openedWithSearch = true;
                        displayMoreInfo(name);
                    }
                });
                txvSearchTitle.setText(title);
                txvSearchDesc.setText(temp.get(title));
                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return temp.size();
            }

            @Override
            public boolean isEmpty() {
                return getCount() != 0;
            }
        };
        resultsAreaView.setAdapter(la);
        showResultsArea();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A function to close the more information section.

    ----------------------------------------------------------------------------------------------------*/
    public void closeMoreInfo(){
        hideMore();
        showNearby();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Displays the more information using the name of the location to identify it.
        Shows data such as the long description, what features it has (Like toilets, parking etc) and
        how far away it is as the crow flies.

    ----------------------------------------------------------------------------------------------------*/
    public void displayMoreInfo(String name){
        showMore();
        hideNearby();
        hideResultsArea();
        Attraction request = null;
        for(int i = 0; i < attractionList.size(); i++){
            if(attractionList.get(i).getName().equals(name)){
                request = attractionList.get(i);
                i = attractionList.size();
            }
        }
        if(request != null){
            final Attraction destination = request;
            moreAreaName.setText(request.getName());
            String calcDist = "" + ((double) Math.round(Math.abs((float) getDistance(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), request.getLocation().latitude, request.getLocation().longitude))/10)/100);
            moreAreaDistanceText.setText(calcDist);
            moreAreaDescriptionText.setText(request.getLongDesc());
            ArrayList<String> tags = new ArrayList<>(Arrays.asList(request.getTags()));
            if(tags.contains(getString(R.string.ha_tag_disability))){
                moreAreaIconDisability.setVisibility(View.VISIBLE);
            } else {
                moreAreaIconDisability.setVisibility(View.INVISIBLE);
            }
            if(tags.contains(getString(R.string.ha_tag_food))){
                moreAreaIconFood.setVisibility(View.VISIBLE);
            } else {
                moreAreaIconFood.setVisibility(View.INVISIBLE);
            }
            if(tags.contains(getString(R.string.ha_tag_toilets))){
                moreAreaIconToilet.setVisibility(View.VISIBLE);
            } else {
                moreAreaIconToilet.setVisibility(View.INVISIBLE);
            }
            if(tags.contains(getString(R.string.ha_tag_parking))){
                moreAreaIconParking.setVisibility(View.VISIBLE);
            } else {
                moreAreaIconParking.setVisibility(View.INVISIBLE);
            }
            if(!tags.contains(getString(R.string.ha_tag_free))){
                moreAreaIconPaid.setVisibility(View.VISIBLE);
            } else {
                moreAreaIconPaid.setVisibility(View.INVISIBLE);
            }
            moreAreaDirectionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Attraction attr: attractionList){
                        if(attr.getId() == destination.getId()){
                            updateRecentPlaces(attr);
                        }
                    }
                    switch(travelMode){
                        case(0): //Car
                            getCarDirection(destination);
                            break;
                        case(1): //Walking
                            getWalkDirection(destination);
                            break;
                        case(2): //Public Transport
                            getPubDirection(destination);
                            break;
                    }
                    closeMoreInfo();
                    hideNearby();
                }
            });
            moreAreaCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMoreInfo();
                    if(openedWithSearch){
                        moreAreaDescription.smoothScrollTo(0,0);
                        searchQuery();
                    }
                }
            });
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Mathematical formula for calculating distance between 2 lat lng locations as if on
        a globe.

    ----------------------------------------------------------------------------------------------------*/
    private double getDistance(double lat1, double long1, double lat2, double long2){
        double eRad = 6371e3;
        double p1 = Math.toRadians(lat1);
        double p2 = Math.toRadians(lat2);
        double dp = Math.toRadians(lat2 - lat1);
        double dl = Math.toRadians(long2 - long1);

        double a = Math.sin(dp/2) * Math.sin(dp/2) +
                Math.cos(p1) * Math.cos(p2) *
                        Math.sin(dl/2) * Math.sin(dl/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return eRad * c;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: An animation to minimise and open the directions from from the side of the screen.

    ----------------------------------------------------------------------------------------------------*/
    private void moveDirectionsBox(){
        int translate = mapOverlayDirectionRightContent.getMeasuredWidth();
        if(mapOverlayDirectionRight.getTranslationX() == translate){
            ObjectAnimator animation = ObjectAnimator.ofFloat(mapOverlayDirectionRight, getString(R.string.ha_translate_x), 0f);
            animation.setDuration(1000);
            animation.start();
            if(enableDarkMode){
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_dark);
            } else {
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_light);
            }
        } else {
            ObjectAnimator animation = ObjectAnimator.ofFloat(mapOverlayDirectionRight, getString(R.string.ha_translate_x), translate);
            animation.setDuration(1000);
            animation.start();
            if(enableDarkMode){
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_left_dark);
            } else {
                mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_left_light);
            }
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: An animation to minimise and open the nearby locations list from from the bottom
        of the screen.

    ----------------------------------------------------------------------------------------------------*/
    private void moveNearbyBox(){
        int translate = nearbyAreaView.getMeasuredHeight();
        if(nearbyArea.getTranslationY() == translate){
            ObjectAnimator animation = ObjectAnimator.ofFloat(nearbyArea, getString(R.string.ha_translate_y), 0f);
            animation.setDuration(1000);
            animation.start();
        } else {
            ObjectAnimator animation = ObjectAnimator.ofFloat(nearbyArea, getString(R.string.ha_translate_y), translate);
            animation.setDuration(1000);
            animation.start();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: An animation to minimise and open the result locations list from from the bottom
        of the screen.

    ----------------------------------------------------------------------------------------------------*/
    private void moveResultsBox(){
        int translate = resultsAreaView.getMeasuredHeight();
        if(resultsArea.getTranslationY() == translate){
            ObjectAnimator animation = ObjectAnimator.ofFloat(resultsArea, getString(R.string.ha_translate_y), 0f);
            animation.setDuration(1000);
            animation.start();
        } else {
            ObjectAnimator animation = ObjectAnimator.ofFloat(resultsArea, getString(R.string.ha_translate_y), translate);
            animation.setDuration(1000);
            animation.start();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Hides the results area when cancelling a search.

    ----------------------------------------------------------------------------------------------------*/
    private void hideResultsArea(){
        resultsArea.setVisibility(View.INVISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Displays the results and opens the view.

    ----------------------------------------------------------------------------------------------------*/
    private void showResultsArea(){
        resultsArea.setVisibility(View.VISIBLE);
        resultsArea.setTranslationY(0);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Hides the nearby section.

    ----------------------------------------------------------------------------------------------------*/
    private void hideNearby(){
        nearbyArea.setVisibility(View.INVISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Displays the nearby section.

    ----------------------------------------------------------------------------------------------------*/
    private void showNearby(){
        if(mLocationPermissionGranted){
            nearbyArea.setVisibility(View.VISIBLE);
            nearbyArea.setTranslationY(nearbyAreaView.getMeasuredHeight());
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Displays the more information section.

    ----------------------------------------------------------------------------------------------------*/
    private void showMore(){
        moreArea.setVisibility(View.VISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Hides the more information section.

    ----------------------------------------------------------------------------------------------------*/
    private void hideMore(){
        moreArea.setVisibility(View.INVISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Hides the directions section.

    ----------------------------------------------------------------------------------------------------*/
    private void hideDirection(){
        mapOverlayDirection.setVisibility(View.INVISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Shows the directions section.

    ----------------------------------------------------------------------------------------------------*/
    private void showDirection(){
        mapOverlayDirection.setVisibility(View.VISIBLE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Starts a request for getting directions from google maps.

    ----------------------------------------------------------------------------------------------------*/
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, SECONDS)
                .setReadTimeout(1, SECONDS)
                .setWriteTimeout(1, SECONDS);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Gets the directions for road travel only. Dependant on mode of transport selected.

    ----------------------------------------------------------------------------------------------------*/
    private void getCarDirection(Attraction dest){
        try {
            vectorResult = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                    .destination(new com.google.maps.model.LatLng(dest.getLocation().latitude, dest.getLocation().longitude))
                    .await();
            hideSearchResults();
            addPolyline(dest);
        } catch (ApiException e) {
            apiError();
        } catch (InterruptedException e) {
            interruptError();
        } catch (IOException e) {
            noConnectionError();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Gets the directions for all paths of travel. Dependant on mode of transport selected.

    ----------------------------------------------------------------------------------------------------*/
    private void getWalkDirection(Attraction dest){
        try {
            vectorResult = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.WALKING).origin(new com.google.maps.model.LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                    .destination(new com.google.maps.model.LatLng(dest.getLocation().latitude, dest.getLocation().longitude))
                    .await();
            hideSearchResults();
            addPolyline(dest);
        } catch (ApiException e) {
            apiError();
        } catch (InterruptedException e) {
            interruptError();
        } catch (IOException e) {
            noConnectionError();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Gets the directions for public transport only. Dependant on mode of transport
        selected.

    ----------------------------------------------------------------------------------------------------*/
    private void getPubDirection(Attraction dest){
        try {
            vectorResult = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.TRANSIT).origin(new com.google.maps.model.LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                    .destination(new com.google.maps.model.LatLng(dest.getLocation().latitude, dest.getLocation().longitude))
                    .await();
            hideSearchResults();
            addPolyline(dest);
        } catch (ApiException e) {
            apiError();
        } catch (InterruptedException e) {
            interruptError();
        } catch (IOException e) {
            noConnectionError();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Displays a message for when the request times out

    ----------------------------------------------------------------------------------------------------*/
    private void noConnectionError(){
        Toast.makeText(this, getString(R.string.ha_toast_connection), Toast.LENGTH_LONG);
    }

    private void interruptError(){
        Toast.makeText(this, getString(R.string.ha_toast_interrupt), Toast.LENGTH_LONG);
    }

    private void apiError(){
        Toast.makeText(this, getString(R.string.ha_toast_api), Toast.LENGTH_LONG);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Displays the direction information on the map, as well as getting text based
        directions for the user to read. The API generates a collection of lines which get placed on
        the map, along with the final destination marker.

    ----------------------------------------------------------------------------------------------------*/
    private void addPolyline(Attraction dest) {
        clearRoute();
        String directionTextHTML = "";
        for(int i = 0; i < vectorResult.routes.length; i++){
            for(int j = 0; j < vectorResult.routes[i].legs.length; j++){
                for(int k = 0; k < vectorResult.routes[i].legs[j].steps.length; k++){
                    directionTextHTML += vectorResult.routes[i].legs[j].steps[k].htmlInstructions + "\n";
                }
            }
        }
        //Trim HTML tags to extract directions data
        boolean insideTag = false;
        String directionTextPure = getString(R.string.ha_directions_time) +
                vectorResult.routes[0].legs[0].duration.humanReadable +
                getString(R.string.ha_directions_distance) +
                vectorResult.routes[0].legs[0].distance.humanReadable +
                "\n\n";
        for(int i = 0; i < directionTextHTML.length(); i++){
            if(insideTag){
                if(directionTextHTML.charAt(i) == '>'){
                    insideTag = false;
                }
            } else {
                if(directionTextHTML.charAt(i) == '<'){
                    insideTag = true;
                } else {
                    if(directionTextHTML.charAt(i) == '\n'){
                        directionTextPure += '\n';
                    }
                    directionTextPure += directionTextHTML.charAt(i);
                }
            }
        }
        if(directionTextPure.length() >= 2){
            directionTextPure = directionTextPure.substring(0, directionTextPure.length() - 2);
        }
        mapOverlayDirectionRightContentScrollAreaText.setText(directionTextPure);
        showDirection();
        mapOverlayDirectionRight.setTranslationX(mapOverlayDirectionRightContent.getMeasuredWidth());
        if(enableDarkMode){
            mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_left_dark);
        } else {
            mapOverlayDirectionRightAccessButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_left_light);
        }
        List<LatLng> decodedPath = new ArrayList<>();
        for(int i = 0; i < vectorResult.routes[0].overviewPolyline.decodePath().size(); i++){
            decodedPath.add(new LatLng(vectorResult.routes[0].overviewPolyline.decodePath().get(i).lat, vectorResult.routes[0].overviewPolyline.decodePath().get(i).lng));
        }
        if(enableDarkMode){
            resultPoly = mMap.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorDarkSecondary)).addAll(decodedPath));
        } else {
            resultPoly = mMap.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorLightSecondary)).addAll(decodedPath));
        }
        switch(dest.getId() - 1){
            case (0):
                resultMarker = Chev;
                break;
            case (1):
                resultMarker = Hadr;
                break;
            case (2):
                resultMarker = Hayd;
                break;
            case (3):
                resultMarker = AlnC;
                break;
            case (4):
                resultMarker = Holy;
                break;
            case (5):
                resultMarker = Bamb;
                break;
            case (6):
                resultMarker = Duns;
                break;
            case (7):
                resultMarker = Kiel;
                break;
            case (8):
                resultMarker = AlnG;
                break;
            case (9):
                resultMarker = Howc;
                break;
            case (10):
                resultMarker = Wood;
                break;
            case (11):
                resultMarker = Bagp;
                break;
            case (12):
                resultMarker = Yeav;
                break;
            case (13):
                resultMarker = Whit;
                break;
            case (14):
                resultMarker = Newc;
                break;
            default:
                break;
        }
        resultMarker.setVisible(true);
        LatLng centre = new LatLng((dest.getLocation().latitude + mLastKnownLocation.getLatitude())/2, (dest.getLocation().longitude + mLastKnownLocation.getLongitude())/2);
        CameraUpdate zoomToFit = CameraUpdateFactory.newLatLngZoom(centre, 9.0f);
        mMap.animateCamera(zoomToFit);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Cleans the map and pops the direction line.

    ----------------------------------------------------------------------------------------------------*/
    private void clearRoute(){
        if(resultPoly != null){
            resultPoly.remove();
            resultPoly = null;
            resultMarker.setVisible(false);
            resultMarker = null;
            showNearby();
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Adds an attraction to the list of recent places, removing the 10th one if there
        are too many.

    ----------------------------------------------------------------------------------------------------*/
    public void updateRecentPlaces(Attraction update){
        if(recentPlaces.size() == 10){
            recentPlaces.remove(9);
        }
        recentPlaces.add(0,update);
        savePackedRecents();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Turns an array into a sting to save as a saved preference.

    ----------------------------------------------------------------------------------------------------*/
    public String createPackedRecents(){
        String builder = "";
        for(int i = 0; i < recentPlaces.size(); i++){
            builder += recentPlaces.get(i).getId() + ",";
        }
        return builder;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Converts the saved string into an array to be iterated through in the recents
        page.

    ----------------------------------------------------------------------------------------------------*/
    private void unpackRecents(){
        String recentsPack = mPreferences.getString(getString(R.string.recents), "");
        String[] recentsUnpack = recentsPack.split(",");
        if(recentsPack.length() != 0){
            for(int i = 0; i < recentsUnpack.length; i++){
                for(Attraction attr: attractionList){
                    if(attr.getId() == Integer.parseInt(recentsUnpack[i])){
                        updateRecentPlaces(attr);
                    }
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Saves the recent places to memory so that they can load again when the app opens.

    ----------------------------------------------------------------------------------------------------*/
    public void savePackedRecents(){
        String pack = createPackedRecents();
        mEditor.putString(getString(R.string.recents), pack);
        mEditor.commit();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Wipes the saved memory.

    ----------------------------------------------------------------------------------------------------*/
    public void deletePackedRecents(){
        mEditor.putString(getString(R.string.recents), "");
        mEditor.commit();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A shortcut to opening the display more info section from the recent places page.

    ----------------------------------------------------------------------------------------------------*/
    public void openLocationFromRecents(Attraction attr){
        displayMoreInfo(attr.getName());
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Logs the user out from the app, and saves all changed data to the database for when
        they log in again.

    ----------------------------------------------------------------------------------------------------*/
    public void logout(){
        String id = mAuth.getCurrentUser().getUid();
        int mode = mPreferences.getInt(getString(R.string.pref_travel), 0);
        boolean dark = mPreferences.getBoolean(getString(R.string.pref_dark), true);
        String achv = explodeAchievements();
        Preference pref = new Preference(id, mode, dark, false, achv);
        logoutSave(id, pref);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Patches together an array of ids into a comma separated string.

    ----------------------------------------------------------------------------------------------------*/
    public String explodeAchievements(){
        String builder = "";
        for(String s: achievementList){
            builder += s  + ",";
        }
        return builder;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Converts a comma separated string of ids into an array of ids.

    ----------------------------------------------------------------------------------------------------*/
    public void loadAchievements(){
        String achv = pref.getAchievementIds();
        achievementList = achv.split(",");
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Saves the data from logout to the database.

    ----------------------------------------------------------------------------------------------------*/
    public void logoutSave(String uid, Preference pref){
        final Context ctx = this;
        mReferencePreference.child(getString(R.string.db_child)).child(uid).setValue(pref)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess (Void aVoid) {
                        AuthUI.getInstance()
                                .signOut(ctx)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        finish();
                                    }
                                });
                    }
                });
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Usef when updating any preference data about the current user logged in.

    ----------------------------------------------------------------------------------------------------*/
    public void updatePreference(String uid, Preference pref) {
        mReferencePreference.child(getString(R.string.db_child)).child(uid).setValue(pref)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess (Void aVoid) {

                    }
                });
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Checks if an acheivement is unlocked. If it is, nothing happens. If it hasn't been
        and is requested to be unlocked, the achievement is set to unlocked, the database is updated,
        and a message is shown saying you have unlocked an achievement.

    ----------------------------------------------------------------------------------------------------*/
    public void unlockAchievement(int id){
        boolean isAlreadyUnlocked = false;
        for(String s: achievementList){
            int x = Integer.parseInt(s);
            if(x == id){
                isAlreadyUnlocked = true;
            }
        }
        if(!isAlreadyUnlocked){
            String[] newList = ArrayUtils.concat(new String[]{"" + id}, achievementList);
            achievementList = newList;
            pref.setAchievementIds(explodeAchievements());
            updatePreference(mAuth.getCurrentUser().getUid(), pref);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, getString(R.string.ha_achievement_unlock), duration);
            toast.show();
        }else{
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, getString(R.string.ha_achievement_lock), duration);
            toast.show();
        }
    }
}
