package bth.pa2555.helpers;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import bth.pa2555.agilegardeningapp.R;

public class AppCompatActivityBase extends AppCompatActivity {

    private Toolbar mToolbar;

    protected AppCompatActivityBase getCurrentContext() {
        return this;
    }

    protected final Toolbar setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return mToolbar;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
        mToolbar.setTitleTextColor(Color.WHITE);
    }
}
