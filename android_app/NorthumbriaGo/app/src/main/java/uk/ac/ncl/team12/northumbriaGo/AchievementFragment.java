package uk.ac.ncl.team12.northumbriaGo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: Samuel Holley

        Description: The script to control the achievements page of the app, managing the display of
        both locked and unlocked achievements

    ----------------------------------------------------------------------------------------------------*/

public class AchievementFragment extends Fragment {

    private List<AchievementItem> achievementList = new ArrayList<AchievementItem>();
    private HomeActivity ha;
    private ShareDialog shareDialog;
    private RelativeLayout fragmentAchievement;
    private ListView fragmentAchievementView;
    private LinearLayout fragmentAchievementMask;
    private LinearLayout fragmentAchievementPop;
    private LinearLayout fragmentAchievementPopUp;
    private TextView fragmentAchievementPopUpViewText;
    private Button fragmentAchievementPopUpViewClose;
    private ImageView fragmentAchievementPopUpBadge;
    private TextView fragmentAchievementPopUpCertificateText;
    private Button fragmentAchievementPopUpFacebookShare;
    private Button fragmentAchievementPopUpShare;
    private LinearLayout fragmentAchievementTutorial;
    private Button fragmentAchievementTutorialCloseButton;
    private TextView fragmentAchievementTutorialContentTitle;
    private TextView fragmentAchievementTutorialContentBody;
    private boolean isInit;
    private View v;


    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: Samuel Holley

        Description: Function runs each time the achievements page is opened. This allows us to refresh the
        list of achievements and unlock achievements which have been unlocked after the app was initially
        opened. On click listeners are added to each list item to give clickable functionality to allow
        the user to press them and load the information associated with them. This also comes with the
        ability to share your achievement using a certificate page inside the website for the app.

    ----------------------------------------------------------------------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_achievement, container, false);
        ha = ((HomeActivity) Objects.requireNonNull(getActivity()));
        fragmentAchievement = v.findViewById(R.id.fragment_achievement);
        fragmentAchievementView = v.findViewById(R.id.fragment_achievement_view);
        fragmentAchievementMask = v.findViewById(R.id.fragment_achievement_mask);
        fragmentAchievementPop = v.findViewById(R.id.fragment_achievement_pop);
        fragmentAchievementPopUp = v.findViewById(R.id.fragment_achievement_pop_up);
        fragmentAchievementPopUpViewText = v.findViewById(R.id.fragment_achievement_pop_up_view_text);
        fragmentAchievementPopUpViewClose = v.findViewById(R.id.fragment_achievement_pop_up_view_close);
        fragmentAchievementPopUpBadge = v.findViewById(R.id.fragment_achievement_pop_up_badge);
        fragmentAchievementPopUpCertificateText = v.findViewById(R.id.fragment_achievement_pop_up_certificate_text);
        fragmentAchievementPopUpFacebookShare = v.findViewById(R.id.fragment_achievement_pop_up_facebook_share);
        fragmentAchievementPopUpShare = v.findViewById(R.id.fragment_achievement_pop_up_share);
        fragmentAchievementTutorial = v.findViewById(R.id.fragment_achievement_tutorial);
        fragmentAchievementTutorialCloseButton = v.findViewById(R.id.fragment_achievement_tutorial_close_button);
        fragmentAchievementTutorialContentTitle = v.findViewById(R.id.fragment_achievement_tutorial_content_title);
        fragmentAchievementTutorialContentBody = v.findViewById(R.id.fragment_achievement_tutorial_content_body);
        setColorScheme();

        shareDialog = new ShareDialog(this);

        if (!isInit) {
            init();
        }
        ha.loadAchievements();

        for(String id: ha.achievementList){
            for(AchievementItem achv: achievementList){
                if(Integer.parseInt(id) == achv.getItemID()){
                    achv.setStatus(true);
                }
            }
        }

        CustomAdapter customAdapter = new CustomAdapter(getContext(), achievementList);
        fragmentAchievementView.setAdapter(customAdapter);

        achievementList.get(0).setStatus(true);
        fragmentAchievementView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AchievementItem item = (AchievementItem) parent.getItemAtPosition(position);
                fragmentAchievementPopUpViewText.setText(item.getItemCertificate());
                fragmentAchievementPopUpBadge.setImageResource(item.getLckItemLogo());
                fragmentAchievementPop.setVisibility(View.VISIBLE);
                fragmentAchievementMask.setVisibility(View.VISIBLE);

                if (!item.getStatus()) {
                    fragmentAchievementPopUpFacebookShare.setClickable(false);
                    fragmentAchievementPopUpShare.setClickable(false);
                    fragmentAchievementPopUpFacebookShare.setVisibility(View.GONE);
                    fragmentAchievementPopUpShare.setVisibility(View.GONE);
                    fragmentAchievementPopUpBadge.setImageResource(item.getLckItemLogo());
                    fragmentAchievementPopUpViewText.setText(getString(R.string.achievement_locked));
                } else {
                    fragmentAchievementPopUpFacebookShare.setClickable(true);
                    fragmentAchievementPopUpShare.setClickable(true);
                    fragmentAchievementPopUpFacebookShare.setVisibility(View.VISIBLE);
                    fragmentAchievementPopUpShare.setVisibility(View.VISIBLE);
                    fragmentAchievementPopUpBadge.setImageResource(item.getUnlckItemLogo());
                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.default_date), Locale.UK);
                    String viewText = item.getItemCertificate() + formatter.format(item.getDateUnlocked().getTime());
                    fragmentAchievementPopUpViewText.setText(viewText);
                }

                fragmentAchievementPop.setVisibility(View.VISIBLE);
                Calendar date = item.getDateUnlocked();

                int pos = position+1;
                int d = date.get(Calendar.DATE);
                int m = date.get(Calendar.MONTH)+1;
                int y = date.get(Calendar.YEAR);
                final String certificateUrl = getString(R.string.a_certificate_url) + pos
                        + getString(R.string.a_certificate_append_day) + d
                        + getString(R.string.a_certificate_append_month) + m
                        + getString(R.string.a_certificate_append_year) + y;
                final String url = getString(R.string.a_certificate_quote) + certificateUrl;

                //FACEBOOK SHARE BUTTON
                fragmentAchievementPopUpFacebookShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setQuote(getString(R.string.a_certificate_quote))
                                .setContentUrl(Uri.parse(certificateUrl))
                                .build();
                        shareDialog.show(content);
                    }
                });

                //SHARE BUTTON
                fragmentAchievementPopUpShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType(getString(R.string.a_popup_style));
                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.a_popup_subject));
                        intent.putExtra(Intent.EXTRA_TEXT, url);
                        startActivity(Intent.createChooser(intent, getString(R.string.a_popup_action)));
                    }
                });
            }
        });

        fragmentAchievementTutorialCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentAchievementTutorial.setVisibility(View.GONE);
                fragmentAchievementMask.setVisibility(View.GONE);
            }
        });

        fragmentAchievementPopUpViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentAchievementMask.setVisibility(View.GONE);
                hideCert();
            }
        });

        return v;
    }

    /*----------------------------------------------------------------------------------------------------
         Created By: Michael Putra

         Modified By: -----

         Description: This function allows a quicker way to hide the achievement certificate popup, as
         a way of closing said popup.

     ----------------------------------------------------------------------------------------------------*/
    public void hideCert() {
        fragmentAchievementPop.setVisibility(View.GONE);
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: Michael Putra

        Description: After looking at the colour scheme currently selected by the user, it changed all
        visible components of the layout screen to match that colour scheme selected.

    ----------------------------------------------------------------------------------------------------*/
    public void setColorScheme() {
        if (ha.enableDarkMode) {
            fragmentAchievement.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            fragmentAchievementPopUp.setBackgroundResource(R.drawable.theme_dark_button_square);
            fragmentAchievementPop.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            fragmentAchievementPopUpViewText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentAchievementPopUpCertificateText.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentAchievementTutorial.setBackgroundResource(R.drawable.theme_dark_button_square_v2);
            fragmentAchievementTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentAchievementTutorialContentBody.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentAchievementPopUpShare.getBackground().setTint(getResources().getColor(R.color.colorDarkAccent));
        } else {
            fragmentAchievement.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            fragmentAchievementPopUp.setBackgroundResource(R.drawable.theme_light_button_square);
            fragmentAchievementPop.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            fragmentAchievementPopUpViewText.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentAchievementPopUpCertificateText.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentAchievementTutorial.setBackgroundResource(R.drawable.theme_light_button_square_v2);
            fragmentAchievementTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentAchievementTutorialContentBody.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentAchievementPopUpShare.getBackground().setTint(getResources().getColor(R.color.colorLightAccent));
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: A custom adapter for the list view, allowing an orgamised way to select what will
        be shown in the list view when it generates, using a custom set of data and layouts, which also
        follow the colour scheme.

    ----------------------------------------------------------------------------------------------------*/
    class CustomAdapter extends BaseAdapter {
        private List<AchievementItem> items; //data source of the list adapter

        //Public constructor
        public CustomAdapter(Context context, List<AchievementItem> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public AchievementItem getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (ha.enableDarkMode) {
                view = getLayoutInflater().inflate(R.layout.achievement_list_dark, null);
            } else {
                view = getLayoutInflater().inflate(R.layout.achievement_list_light, null);
            }

            // get current item to be displayed
            AchievementItem item = getItem(i);

            ImageView imageView = view.findViewById(R.id.achievement_list_icon);
            TextView textView_name = view.findViewById(R.id.achievement_list_info_title);
            TextView textView_description = view.findViewById(R.id.achievement_list_info_description);

            if (!item.getStatus()) {
                imageView.setImageResource(item.getLckItemLogo());
            } else {
                imageView.setImageResource(item.getUnlckItemLogo());
            }
            textView_name.setText(item.getItemName());
            textView_description.setText(item.getItemDescription());
            return view;
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Creates a list of achievements using stored strings and images, along with
        utilising the class made specifically for the achievement items to make accessing the data within
        them easier. This only happens once, when the app is loaded.

    ----------------------------------------------------------------------------------------------------*/
    private void init() {
        achievementList.add(new AchievementItem(1,getString(R.string.ach_t_1),
                getString(R.string.ach_d_1),
                R.drawable.u_1,
                R.drawable.l_1,
                getString(R.string.ach_c_1)));

        achievementList.add(new AchievementItem(2,getString(R.string.ach_t_2),
                getString(R.string.ach_d_2),
                R.drawable.u_2,
                R.drawable.l_2,
                getString(R.string.ach_c_2)));

        achievementList.add(new AchievementItem(3,getString(R.string.ach_t_3),
                getString(R.string.ach_d_3),
                R.drawable.u_3,
                R.drawable.l_3,
                getString(R.string.ach_c_3)));

        achievementList.add(new AchievementItem(4,getString(R.string.ach_t_4),
                getString(R.string.ach_d_4),
                R.drawable.u_4,
                R.drawable.l_4,
                getString(R.string.ach_c_4)));

        achievementList.add(new AchievementItem(5,getString(R.string.ach_t_5),
                getString(R.string.ach_d_5),
                R.drawable.u_5,
                R.drawable.l_5,
                getString(R.string.ach_c_5)));

        achievementList.add(new AchievementItem(6,getString(R.string.ach_t_6),
                getString(R.string.ach_d_6),
                R.drawable.u_6,
                R.drawable.l_6,
                getString(R.string.ach_c_6)));

        achievementList.add(new AchievementItem(7,getString(R.string.ach_t_7),
                getString(R.string.ach_d_7),
                R.drawable.u_7,
                R.drawable.l_7,
                getString(R.string.ach_c_7)));

        achievementList.add(new AchievementItem(8,getString(R.string.ach_t_8),
                getString(R.string.ach_d_8),
                R.drawable.u_8,
                R.drawable.l_8,
                getString(R.string.ach_c_8)));

        achievementList.add(new AchievementItem(9,getString(R.string.ach_t_9),
                getString(R.string.ach_d_9),
                R.drawable.u_9,
                R.drawable.l_9,
                getString(R.string.ach_c_9)));

        achievementList.add(new AchievementItem(10,getString(R.string.ach_t_10),
                getString(R.string.ach_d_10),
                R.drawable.u_10,
                R.drawable.l_10,
                getString(R.string.ach_c_10)));

        achievementList.add(new AchievementItem(11,getString(R.string.ach_t_11),
                getString(R.string.ach_d_11),
                R.drawable.u_11,
                R.drawable.l_11,
                getString(R.string.ach_c_11)));

        achievementList.add(new AchievementItem(12,getString(R.string.ach_t_12),
                getString(R.string.ach_d_12),
                R.drawable.u_12,
                R.drawable.l_12,
                getString(R.string.ach_c_12)));

        achievementList.add(new AchievementItem(13,getString(R.string.ach_t_13),
                getString(R.string.ach_d_13),
                R.drawable.u_13,
                R.drawable.l_13,
                getString(R.string.ach_c_13)));

        achievementList.add(new AchievementItem(14,getString(R.string.ach_t_14),
                getString(R.string.ach_d_14),
                R.drawable.u_14,
                R.drawable.l_14,
                getString(R.string.ach_c_14)));

        achievementList.add(new AchievementItem(15,getString(R.string.ach_t_15),
                getString(R.string.ach_d_15),
                R.drawable.u_15,
                R.drawable.l_15,
                getString(R.string.ach_c_15)));

        achievementList.add(new AchievementItem(16,getString(R.string.ach_t_16),
                getString(R.string.ach_d_16),
                R.drawable.u_16,
                R.drawable.l_16,
                getString(R.string.ach_c_16)));

        achievementList.add(new AchievementItem(17,getString(R.string.ach_t_17),
                getString(R.string.ach_d_17),
                R.drawable.u_17,
                R.drawable.l_17,
                getString(R.string.ach_c_17)));

        achievementList.add(new AchievementItem(18,getString(R.string.ach_t_18),
                getString(R.string.ach_d_18),
                R.drawable.u_18,
                R.drawable.l_18,
                getString(R.string.ach_c_18)));

        achievementList.add(new AchievementItem(19,getString(R.string.ach_t_19),
                getString(R.string.ach_d_19),
                R.drawable.u_19,
                R.drawable.l_19,
                getString(R.string.ach_c_19)));

        achievementList.add(new AchievementItem(20,getString(R.string.ach_t_20),
                getString(R.string.ach_d_20),
                R.drawable.u_20,
                R.drawable.l_20,
                getString(R.string.ach_c_20)));

        achievementList.add(new AchievementItem(21,getString(R.string.ach_t_21),
                getString(R.string.ach_d_21),
                R.drawable.u_21,
                R.drawable.l_21,
                getString(R.string.ach_c_21)));

        achievementList.add(new AchievementItem(22,getString(R.string.ach_t_22),
                getString(R.string.ach_d_22),
                R.drawable.u_22,
                R.drawable.l_22,
                getString(R.string.ach_c_22)));

        achievementList.add(new AchievementItem(23,getString(R.string.ach_t_23),
                getString(R.string.ach_d_23),
                R.drawable.u_23,
                R.drawable.l_23,
                getString(R.string.ach_c_23)));

        achievementList.add(new AchievementItem(24,getString(R.string.ach_t_24),
                getString(R.string.ach_d_24),
                R.drawable.u_24,
                R.drawable.l_24,
                getString(R.string.ach_c_24)));

        achievementList.add(new AchievementItem(25,getString(R.string.ach_t_25),
                getString(R.string.ach_d_25),
                R.drawable.u_25,
                R.drawable.l_25,
                getString(R.string.ach_c_25)));

        achievementList.add(new AchievementItem(26,getString(R.string.ach_t_26),
                getString(R.string.ach_d_26),
                R.drawable.u_26,
                R.drawable.l_26,
                getString(R.string.ach_c_26)));

        achievementList.add(new AchievementItem(27,getString(R.string.ach_t_27),
                getString(R.string.ach_d_27),
                R.drawable.u_27,
                R.drawable.l_27,
                getString(R.string.ach_c_27)));

        achievementList.add(new AchievementItem(28,getString(R.string.ach_t_28),
                getString(R.string.ach_d_28),
                R.drawable.u_28,
                R.drawable.l_28,
                getString(R.string.ach_c_28)));
        isInit = true;
    }
}
