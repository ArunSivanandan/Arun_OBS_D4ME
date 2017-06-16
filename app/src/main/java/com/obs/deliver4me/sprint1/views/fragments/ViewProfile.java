package com.obs.deliver4me.sprint1.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obs.deliver4me.R;

/**
 * Created by Arun.S on 6/16/2017.
 */

public class ViewProfile extends Fragment {

    private View view;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_profile_layout, container, false);
        context = view.getContext();

        return view;
    }
}
