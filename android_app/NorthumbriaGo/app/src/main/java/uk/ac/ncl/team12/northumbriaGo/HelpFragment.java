package uk.ac.ncl.team12.northumbriaGo;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

    /*----------------------------------------------------------------------------------------------------
        Created By: Calum Harvey

        Modified By: Samuel Holley
                     Michael Putra

        Description: The java code to accompany the help section of the app. This contains features
        for loading the FAQ strings and their appropriate answers.

    ----------------------------------------------------------------------------------------------------*/

public class HelpFragment extends Fragment {

    private ExpandableListAdapter expandableListAdapter;
    private HomeActivity ha;
    private LinearLayout help;
    private ExpandableListView helpList;
    private int lastExpandedPosition = -1;
    private LinearLayout fragmentHelpMask;
    private LinearLayout fragmentHelpTutorial;
    private Button fragmentHelpTutorialCloseButton;
    private TextView fragmentHelpTutorialContentTitle;
    private TextView fragmentHelpTutorialContentBody;

    /*----------------------------------------------------------------------------------------------------
        Created By: Calum Harvey

        Modified By: Michael Putra
                     Samuel Holley

        Description: The code which runs when the help section is opened. Manages the expanding list
        for displaying the answers to questions

    ----------------------------------------------------------------------------------------------------*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);
        help = v.findViewById(R.id.fragment_help);
        fragmentHelpMask = v.findViewById(R.id.fragment_help_mask);
        fragmentHelpTutorial = v.findViewById(R.id.fragment_help_tutorial);
        fragmentHelpTutorialCloseButton = v.findViewById(R.id.fragment_help_tutorial_close_button);
        fragmentHelpTutorialContentTitle = v.findViewById(R.id.fragment_help_tutorial_content_title);
        fragmentHelpTutorialContentBody = v.findViewById(R.id.fragment_help_tutorial_content_body);
        init(v);
        helpList.setAdapter(expandableListAdapter);
        helpList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                    helpList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        ha = ((HomeActivity) Objects.requireNonNull(getActivity()));
        setColorScheme();
        fragmentHelpTutorialCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentHelpTutorial.setVisibility(View.GONE);
                fragmentHelpMask.setVisibility(View.GONE);
            }
        });
        return v;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Calum Harvey

        Modified By: Michael Putra

        Description: Initialises the expanding list by passing in all necessary information such as the
        map for the text and which colour scheme it should follow, so that it can pick out the
        corresponding layouts to fill the list items.

    ----------------------------------------------------------------------------------------------------*/
    private void init(View v){
        final HashMap<String, String> faqList =  getFaq();
        final List<String> expandableListTitle;
        final View vFinal = v;
        helpList = v.findViewById(R.id.fragment_help_list);
        expandableListTitle = new ArrayList<>(faqList.keySet());
        expandableListAdapter = new ExpandableListAdapter() {
            private Context context = vFinal.getContext();;
            private List<String> listTitle = expandableListTitle;
            private HashMap<String, String> listDescription = faqList;
            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {
            }

            @Override
            public int getGroupCount() {
                return this.listTitle.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 1;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return this.listTitle.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return this.listDescription.get(this.listTitle.get(groupPosition));
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                String title = (String) getGroup(groupPosition);

                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if(ha.enableDarkMode){
                        convertView = layoutInflater.inflate(R.layout.faq_list_dark, null);
                    } else {
                        convertView = layoutInflater.inflate(R.layout.faq_list_light, null);
                    }
                }
                TextView Txv_Faq_Title = convertView.findViewById(R.id.faq_title);
                Txv_Faq_Title.setText(Html.fromHtml(title));
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                String description = (String) getChild(groupPosition, childPosition);
                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if(ha.enableDarkMode){
                        convertView = layoutInflater.inflate(R.layout.faq_description_dark, null);
                    } else {
                        convertView = layoutInflater.inflate(R.layout.faq_description_light, null);
                    }
                }
                TextView Tvx_Faq_Description = convertView.findViewById(R.id.faq_description);
                Tvx_Faq_Description.setText(Html.fromHtml(description));
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
                convertView.startAnimation(animation);
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {
            }

            @Override
            public void onGroupCollapsed(int groupPosition) {
            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return 0;
            }
        };
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Reads the overall colour scheme for the app and changes the colours on the visible
        areas of the app to make it match that theme.

    ----------------------------------------------------------------------------------------------------*/
    private void setColorScheme(){
        if(ha.enableDarkMode){
            help.setBackgroundColor(getResources().getColor(R.color.colorDarkPrimary));
            fragmentHelpTutorial.setBackgroundResource(R.drawable.theme_dark_button_square_v2);
            fragmentHelpTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorDarkAccent));
            fragmentHelpTutorialContentBody.setTextColor(getResources().getColor(R.color.colorDarkAccent));
        } else {
            help.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
            fragmentHelpTutorial.setBackgroundResource(R.drawable.theme_light_button_square_v2);
            fragmentHelpTutorialContentTitle.setTextColor(getResources().getColor(R.color.colorLightAccent));
            fragmentHelpTutorialContentBody.setTextColor(getResources().getColor(R.color.colorLightAccent));
        }
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Calum Harvey

        Modified By: Michael Putra

        Description: Stores all questions and answers into a map, with the questions as the keys.

    ----------------------------------------------------------------------------------------------------*/
    private HashMap<String, String> getFaq(){
        HashMap<String, String> faqList = new HashMap<>();
        faqList.put(getString(R.string.faq_q1), getString(R.string.faq_a1));
        faqList.put(getString(R.string.faq_q2), getString(R.string.faq_a2));
        faqList.put(getString(R.string.faq_q3), getString(R.string.faq_a3));
        faqList.put(getString(R.string.faq_q4), getString(R.string.faq_a4));
        faqList.put(getString(R.string.faq_q5), getString(R.string.faq_a5));

        faqList.put(getString(R.string.faq_q6), getString(R.string.faq_a6));
        faqList.put(getString(R.string.faq_q7), getString(R.string.faq_a7));
        faqList.put(getString(R.string.faq_q8), getString(R.string.faq_a8));
        faqList.put(getString(R.string.faq_q9), getString(R.string.faq_a9));
        faqList.put(getString(R.string.faq_q10), getString(R.string.faq_a10));

        faqList.put(getString(R.string.faq_q11), getString(R.string.faq_a11));
        faqList.put(getString(R.string.faq_q12), getString(R.string.faq_a12));
        faqList.put(getString(R.string.faq_q13), getString(R.string.faq_a13));
        faqList.put(getString(R.string.faq_q14), getString(R.string.faq_a14));
        faqList.put(getString(R.string.faq_q15), getString(R.string.faq_a15));

        faqList.put(getString(R.string.faq_q16), getString(R.string.faq_a16));
        faqList.put(getString(R.string.faq_q17), getString(R.string.faq_a17));
        faqList.put(getString(R.string.faq_q18), getString(R.string.faq_a18));
        faqList.put(getString(R.string.faq_q19), getString(R.string.faq_a19));
        faqList.put(getString(R.string.faq_q20), getString(R.string.faq_a20));

        faqList.put(getString(R.string.faq_q21), getString(R.string.faq_a21));
        faqList.put(getString(R.string.faq_q22), getString(R.string.faq_a22));
        faqList.put(getString(R.string.faq_q23), getString(R.string.faq_a23));
        faqList.put(getString(R.string.faq_q24), getString(R.string.faq_a24));
        return faqList;
    }



}
