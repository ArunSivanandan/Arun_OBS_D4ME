package com.obs.deliver4me.sprint1.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.obs.deliver4me.R;


/**
 * Created by Arun.S on 04/13/17.
 */
public class NavigationMenuAdapter extends BaseAdapter {

    String[] titles;
    TypedArray icons;
    LayoutInflater inflater;
    Context context;
    ListView menuList;


    public NavigationMenuAdapter(Context context, String[] titles, TypedArray icons, ListView menuList) {
        this.titles = titles;
        this.icons = icons;
        this.context = context;
        this.menuList = menuList;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public String getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_item_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.navTitle.setText(titles[position]);
        mViewHolder.navIcon.setImageDrawable(ContextCompat.getDrawable(context, icons.getResourceId(position, -1)));


        return convertView;
    }

    private class MyViewHolder {
        TextView navTitle, tvUserDAName;
        ImageView navIcon, profileImageHeader;

        public MyViewHolder(View item) {
            navTitle = (TextView) item.findViewById(R.id.tv_nav_title);
            navIcon = (ImageView) item.findViewById(R.id.iv_nav_icon);
            profileImageHeader = (ImageView) item.findViewById(R.id.profileImageHeader);
            tvUserDAName = (TextView) item.findViewById(R.id.tv_userDAName);
        }
    }
}
