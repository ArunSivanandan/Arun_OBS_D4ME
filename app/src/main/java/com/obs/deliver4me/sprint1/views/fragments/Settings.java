package com.obs.deliver4me.sprint1.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.obs.deliver4me.R;
import com.obs.deliver4me.application.AppController;
import com.obs.deliver4me.configs.SessionManager;
import com.obs.deliver4me.sprint1.views.activity.ChangePassword;
import com.obs.deliver4me.sprint1.views.activity.HomeActivity;
import com.obs.deliver4me.sprint1.views.activity.LoginActivity;

import javax.inject.Inject;

/**
 * Created by Arun.S on 6/14/2017.
 */

public class Settings extends Fragment {

    private View view;
    private Context context;
    private RelativeLayout rltRowOne, rltRowTwo, rltRowThree;

    @Inject
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_layout, container, false);
        context = view.getContext();
        AppController.getAppComponent().inject(this);

        initViews();
        return view;
    }

    private void initViews() {
        rltRowOne = (RelativeLayout) view.findViewById(R.id.rlt_row_one);
        rltRowTwo = (RelativeLayout) view.findViewById(R.id.rlt_row_two);
        rltRowThree = (RelativeLayout) view.findViewById(R.id.rlt_row_three);

        HomeActivity.defaultInstance().setHeader(context.getResources().getString(R.string.txt_settings_title));

        if (sessionManager.isFbUser()) {
            rltRowOne.setVisibility(View.GONE);
        } else {
            rltRowOne.setVisibility(View.VISIBLE);
        }

        initEventListener();
    }

    private void initEventListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rlt_row_one:
                        startActivity(new Intent(context, ChangePassword.class));
                        break;
                    case R.id.rlt_row_two:
                        break;
                    case R.id.rlt_row_three:
                        sessionManager.clear();
                        sessionManager.clearAll();
                        startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                }
            }
        };
        rltRowOne.setOnClickListener(onClickListener);
        rltRowThree.setOnClickListener(onClickListener);
    }
}
