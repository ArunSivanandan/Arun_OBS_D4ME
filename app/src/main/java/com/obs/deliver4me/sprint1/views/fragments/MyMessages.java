package com.obs.deliver4me.sprint1.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obs.deliver4me.R;
import com.obs.deliver4me.sprint1.views.activity.HomeActivity;

/**
 * Created by Arun.S on 5/2/2017.
 */

public class MyMessages extends Fragment{

    private View view;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_messages_layout, container, false);
        context = view.getContext();

        initViews();
        return view;
    }

    private void initViews() {
        HomeActivity.defaultInstance().setHeader(context.getResources().getString(R.string.my_message_title));
    }
}
