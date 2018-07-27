package com.domain.application.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.domain.application.R;
import com.domain.application.base.BaseActivity;
import com.domain.application.model.CowParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class CowParamsActivity extends BaseActivity {
    @BindView(R.id.date)
    EditText dateView;
    @BindView(R.id.milk_yield)
    EditText milk_yield;
    @BindView(R.id.weight)
    EditText weight;
    @BindView(R.id.fat)
    EditText fat;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    CowParams cowParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cow_params);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cowParams = new CowParams(db);
        Intent intent = getIntent();
        cowParams.setFid_cow(intent.getLongExtra("id", 0));
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        updateLabel();


        milk_yield.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    cowParams.setMilk_yield(String.valueOf(s));
            }
        });

        weight.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    cowParams.setWeight(String.valueOf(s));
            }
        });

        fat.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    cowParams.setFat(String.valueOf(s));
            }
        });
    }

    @OnClick(R.id.date)
    public void onClickSetDate() {
        new DatePickerDialog(CowParamsActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        long time = myCalendar.getTimeInMillis();
        if (time > System.currentTimeMillis()) {
            Toast.makeText(getBaseContext(), "Не корректная дата", Toast.LENGTH_SHORT).show();
            return;
        }
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(sdf.format(time));
        cowParams.setDate(sdf.format(time));
    }

    @OnClick(R.id.save)
    public void onClickSave() {
        if (cowParams.save()) {
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Поля заполнены некорректно", Toast.LENGTH_SHORT).show();
        }
    }
}
