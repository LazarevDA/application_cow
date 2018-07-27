package com.domain.application.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.domain.application.R;
import com.domain.application.adapter.PassportsAdapter;
import com.domain.application.base.BaseActivity;
import com.domain.application.model.PassportInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.list_passports)
    ListView list_passports;

    List<PassportInfo> listPassportInfo;
    PassportsAdapter passportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        listPassportInfo = PassportInfo.GetItems(db);
        passportsAdapter = new PassportsAdapter(this, listPassportInfo);
        list_passports.setAdapter(passportsAdapter);
    }

    @OnClick(R.id.create_new)
    public void onClickCreateNew() {
        Intent intent = new Intent(this, PassportActivity.class);
        intent.putExtra("IsNewRecord", true);
        startActivity(intent);
    }


}
