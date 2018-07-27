package com.domain.application.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.domain.application.db.DBHelper;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected DBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        dbHelper = new DBHelper(this);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void finish() {
        super.finish();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
