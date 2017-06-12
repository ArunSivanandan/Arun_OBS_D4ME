package com.obs.deliver4me.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obs.deliver4me.R;


public class BaseDialogFragment extends DialogFragment {
    private int layoutId;
    protected Activity mActivity;

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public BaseDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.share_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layoutId, container, false);
        initViews(v);
        return v;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public void initViews(View v) {
        getDialog().setCanceledOnTouchOutside(false);
    }
}
