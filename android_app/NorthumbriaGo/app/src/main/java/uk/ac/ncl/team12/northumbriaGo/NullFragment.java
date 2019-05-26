package uk.ac.ncl.team12.northumbriaGo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: An empty fragment with no functionality to allow proper transitions between
        fragments with contents and the map screen

    ----------------------------------------------------------------------------------------------------*/

public class NullFragment extends Fragment {

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Empty so that nothing will be shown when it overlays

    ----------------------------------------------------------------------------------------------------*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_null, container, false);
    }
}
