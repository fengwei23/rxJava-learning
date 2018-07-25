package com.fengwei23.rxjavalearning;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.fengwei23.rxjavalearning.demo1.FirstExampleFragment;
import com.fengwei23.rxjavalearning.navigation.NavigationDrawerCallbacks;
import com.fengwei23.rxjavalearning.navigation.NavigationDrawerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationDrawerCallbacks {
    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar_actionbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    private NavigationDrawerFragment mNavigationDrawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, mDrawerLayout, mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.i(TAG, "onNavigationDrawerItemSelected: " + position);
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction().
                        replace(R.id.container, new FirstExampleFragment())
                        .commit();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
