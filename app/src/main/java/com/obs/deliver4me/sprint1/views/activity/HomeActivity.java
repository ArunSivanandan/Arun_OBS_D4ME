package com.obs.deliver4me.sprint1.views.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.obs.deliver4me.R;
import com.obs.deliver4me.application.AppController;
import com.obs.deliver4me.sprint1.adapters.NavigationMenuAdapter;
import com.obs.deliver4me.sprint1.views.fragments.MapsActivity;
import com.obs.deliver4me.sprint1.views.fragments.MyMessages;
import com.obs.deliver4me.sprint1.views.fragments.Settings;
import com.obs.deliver4me.sprint1.views.fragments.ViewProfile;
import com.obs.deliver4me.utils.CTypeface;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FrameLayout containerView;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ListView menuList;
    private CircleImageView profileImageHeader;
    private ImageView ivBack, ivMenu;
    private TextView tvUserDA, tvTitle;
    private String[] navTitles;
    private TypedArray navIcons;
    private boolean isUser;
    private NavigationMenuAdapter menuAdapter;
    private static HomeActivity instance = null;

    public static HomeActivity defaultInstance() {
        return instance;
    }

    @Inject
    CTypeface cTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_slider_layout);
        AppController.getAppComponent().inject(this);
        instance = this;
        getIntentValues();
        initviews();
    }

    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

        }
    }

    public void setHeader(String title) {
        tvTitle.setText(" " + title);
    }

    private void initviews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        containerView = (FrameLayout) findViewById(R.id.container_view);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        menuList = (ListView) findViewById(R.id.menu_list);
        profileImageHeader = (CircleImageView) findViewById(R.id.profileImageHeader);
        tvUserDA = (TextView) findViewById(R.id.tv_userDAName);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);

        ivBack.setImageResource(R.drawable.menu_icon);
        //Initiating SIDE MENU Items
        initNavigationDrawer();
        initEventListener();

        //Disable Scrollbars
        disableNavigationViewScrollbars(navigationView);

        setMenuAdapter();
        setDefaultPage();
    }

    private void initEventListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_back:
                        if (drawerLayout.isDrawerOpen(navigationView)) {
                            drawerLayout.closeDrawer(navigationView);
                        } else {
                            drawerLayout.openDrawer(navigationView);
                        }
                        break;
                    case R.id.iv_menu:
                        startActivity(new Intent(HomeActivity.this, CreateProfile.class));
                        break;
                }
            }
        };
        ivBack.setOnClickListener(onClickListener);
        ivMenu.setOnClickListener(onClickListener);
    }
    public void initNavigationDrawer() {

        int width = (getResources().getDisplayMetrics().widthPixels * 65) / 100;
        DrawerLayout.LayoutParams menuParams = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        menuParams.width = width;
        navigationView.setLayoutParams(menuParams);


        //Navigation Drawer Icon
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                //Re-Sizing Left Side Navigation Drawer width
//                float xPositionOpenDrawer = navigationView.getWidth();
//                float xPositionWindowContent = (slideOffset * xPositionOpenDrawer);
//                containerView.setX(xPositionWindowContent);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    //Setting Adapter
    private void setMenuAdapter() {
        navTitles = getResources().getStringArray(R.array.navDrawerItemsUser);
        navIcons = getResources().obtainTypedArray(R.array.navDrawerIconsUser);

        menuAdapter = new NavigationMenuAdapter(HomeActivity.this, navTitles, navIcons, menuList);
        menuList.setAdapter(menuAdapter);
        menuList.setItemChecked(0, true);
        menuList.setOnItemClickListener(onMenuItemClick);
    }

    private void setDefaultPage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_view, new MyMessages()).commit();
        tvTitle.setText(navTitles[2]);
    }

    //Disable Scrollbars
    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
                navigationMenuView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }
        }
    }

    //OnListItemClick
    AdapterView.OnItemClickListener onMenuItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
            menuList.setItemChecked(position, true);
            Fragment fragment = null;
                switch (position) {
                    case 0:
                        ivMenu.setVisibility(View.GONE);
                        break;
                    case 1:
                        ivMenu.setVisibility(View.GONE);
                        break;
                    case 2:
                        tvTitle.setText(navTitles[position]);
                        fragment = new MapsActivity();
                        ivMenu.setVisibility(View.GONE);
                        break;
                    case 3:
                        ivMenu.setVisibility(View.GONE);
                        break;
                    case 4:
                        tvTitle.setText(navTitles[position]);
                        fragment = new ViewProfile();
                        ivMenu.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        tvTitle.setText(navTitles[position]);
                        fragment = new Settings();
                        ivMenu.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            if (fragment != null) {
                final Fragment finalFragment = fragment;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_view, finalFragment).commit();
                    }
                }, 500);
            }
            drawerLayout.closeDrawers();
        }
    };
}
