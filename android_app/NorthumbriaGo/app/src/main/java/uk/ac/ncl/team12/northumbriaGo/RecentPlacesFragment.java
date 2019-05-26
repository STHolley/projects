package uk.ac.ncl.team12.northumbriaGo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Takes the top 10 most recently searched for places that the user has gotten
        directions to in their session before logging out. This is wiped for when another user
        logs in, or they log in again. The user can use this to get directions to a place they
        may have accidentally cancelled the directions to, without having to search for it again.

    ----------------------------------------------------------------------------------------------------*/

public class RecentPlacesFragment extends Fragment {

    private HomeActivity ha;
    public List<Attraction> emptyList = new ArrayList<>();
    private RelativeLayout fragmentRecentPlaces;
    private ListView fragmentRecentPlacesView;
    private LinearLayout fragmentRecentPlacesMask;
    private LinearLayout fragmentRecentPlacesTutorial;
    private Button fragmentRecentPlacesTutorialCloseButton;
    private TextView fragmentRecentPlacesTutorialContentTitle;
    private TextView fragmentRecentPlacesTutorialContentBody;

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Creates a list view for all the recent places, along with listeners to allow it to
        be clickable and redirect you to the directions screen for the location again.

    ----------------------------------------------------------------------------------------------------*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent_places, container, false);
        ha = ((HomeActivity) Objects.requireNonNull(getActivity()));
        fragmentRecentPlaces = v.findViewById(R.id.fragment_recent_places);
        fragmentRecentPlacesMask = v.findViewById(R.id.fragment_recent_places_mask);
        fragmentRecentPlacesView = v.findViewById(R.id.fragment_recent_places_view);
        fragmentRecentPlacesTutorial = v.findViewById(R.id.fragment_recent_places_tutorial);
        fragmentRecentPlacesTutorialCloseButton = v.findViewById(R.id.fragment_recent_places_tutorial_close_button);
        fragmentRecentPlacesTutorialContentTitle = v.findViewById(R.id.fragment_recent_places_tutorial_content_title);
        fragmentRecentPlacesTutorialContentBody = v.findViewById(R.id.fragment_recent_places_tutorial_content_body);

        if(emptyList.size() == 0){
            String[] tags = {""};
            emptyList.add(new Attraction(1, getString(R.string.rp_attraction_name),
                    tags,
                    getString(R.string.rp_attraction_date),
                    getString(R.string.rp_attraction_short),
                    "",
                    new LatLng(0, 0)));
        }

        if(ha.recentPlaces.size() != 0){
            CustomAdapter customAdapter = new CustomAdapter(ha.recentPlaces);
            fragmentRecentPlacesView.setAdapter(customAdapter);
            fragmentRecentPlacesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Attraction item = (Attraction) parent.getItemAtPosition(position);
                    Fragment nullFrag = new NullFragment();
                    ha.findViewById(R.id.home_layout).setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_screen_fragment_container,
                            nullFrag, getString(R.string.rp_set_fragment)).commit();
                    ha.openLocationFromRecents(item);
                    ha.navigation.getMenu().getItem(0).setChecked(true);
                }
            });
        } else {
            CustomAdapter customAdapter = new CustomAdapter(emptyList);
            fragmentRecentPlacesView.setAdapter(customAdapter);
        }
        fragmentRecentPlacesTutorialCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentRecentPlacesTutorial.setVisibility(View.GONE);
                fragmentRecentPlacesMask.setVisibility(View.GONE);
            }
        });
        setColorScheme();
        return v;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Gets the colour theme for the app and sets the visible assets to match that.

    ----------------------------------------------------------------------------------------------------*/

    public void setColorScheme() {
        if(ha.enableDarkMode){
            fragmentRecentPlaces.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            fragmentRecentPlacesTutorial.setBackgroundResource(R.drawable.theme_dark_button_square_v2);
            fragmentRecentPlacesTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentRecentPlacesTutorialContentBody.setTextColor(getResources().getColor(R.color.colorDarkAccent));
        } else {
            fragmentRecentPlaces.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            fragmentRecentPlacesTutorial.setBackgroundResource(R.drawable.theme_light_button_square_v2);
            fragmentRecentPlacesTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentRecentPlacesTutorialContentBody.setTextColor(getResources().getColor(R.color.colorLightAccent));
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Generates a list of items with clickable effects, populated by an array.
        Comes with accompanying colour variants for both light and dark mode.

    ----------------------------------------------------------------------------------------------------*/

    class CustomAdapter extends BaseAdapter {
        private List<Attraction> items; //data source of the list adapter

        //Public constructor
        public CustomAdapter(List<Attraction> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Attraction getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (ha.enableDarkMode) {
                view = getLayoutInflater().inflate(R.layout.recent_places_list_dark, null);
            } else {
                view = getLayoutInflater().inflate(R.layout.recent_places_list_light, null);
            }
            // get current item to be displayed
            Attraction item = getItem(i);
            TextView textView_name = view.findViewById(R.id.recent_places_list_title);
            TextView textView_description = view.findViewById(R.id.recent_places_list_description);
            textView_name.setText(item.getName());
            textView_description.setText(item.getShortDesc());
            return view;
        }
    }
}
