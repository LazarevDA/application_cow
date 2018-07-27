package com.domain.application.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.domain.application.R;
import com.domain.application.adapter.BreedAdapter;
import com.domain.application.adapter.CowAdapter;
import com.domain.application.adapter.SuitAdapter;
import com.domain.application.base.BaseActivity;
import com.domain.application.model.Breed;
import com.domain.application.model.Cow;
import com.domain.application.model.CowParams;
import com.domain.application.model.PassportInfo;
import com.domain.application.model.Suit;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class PassportActivity extends BaseActivity {
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.birthday)
    EditText birthday;
    @BindView(R.id.breed)
    Spinner breedSpinner;
    @BindView(R.id.suit)
    Spinner suitSpinner;
    @BindView(R.id.cow_mather)
    Spinner cowSpinner;
    @BindView(R.id.graph)
    GraphView graph;
    @BindView(R.id.linear_graphic)
    LinearLayout linear_graphic;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    List<Breed> breeds;
    List<Suit> suits;
    List<Cow> cows;
    PassportInfo passportInfo;
    LineGraphSeries<DataPoint> fatSeries, weightSeries, milkSeries;
    private boolean showMilksSeries = true, showWeightSeries = true, showFatSeries = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);Intent intent = getIntent();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        passportInfo = PassportInfo.GetItemByID(db, intent.getLongExtra("id", 0));
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
        breeds = Breed.GetItems(db);
        BreedAdapter breedAdapter = new BreedAdapter(
                this,
                R.layout.item_spinner,
                breeds);
        breedSpinner.setAdapter(breedAdapter);
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                passportInfo.setFidBreed(breeds.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        suits = Suit.GetItems(db);
        SuitAdapter suitAdapter = new SuitAdapter(
                this,
                R.layout.item_spinner,
                suits);
        suitSpinner.setAdapter(suitAdapter);
        suitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                passportInfo.setFidSuits(suits.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        cows = Cow.GetItems(db, passportInfo.getId());
        CowAdapter cowAdapter = new CowAdapter(
                this,
                R.layout.item_spinner,
                cows);
        cowSpinner.setAdapter(cowAdapter);
        cowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                passportInfo.setFidCow(suits.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        number.addTextChangedListener(new TextWatcher() {

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
                    passportInfo.setNumber(String.valueOf(s));
            }
        });

        if (!intent.getBooleanExtra("IsNewRecord", true)) {

            passportInfo = PassportInfo.GetItemByID(db, intent.getLongExtra("id", 0));
            number.setText(passportInfo.getNumber());

            Date date = new Date(passportInfo.getBirthday());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthday.setText(sdf.format(date));

            breedSpinner.setSelection(breedAdapter.getIndexByID(passportInfo.getFidBreed()));

            suitSpinner.setSelection(suitAdapter.getIndexByID(passportInfo.getFidSuits()));

            cowSpinner.setSelection(cowAdapter.getIndexByID(passportInfo.getFidCow()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        RenderGraphics();
    }

    private void RenderGraphics() {
        if (passportInfo.getCowParamsList() == null || passportInfo.getCowParamsList().size() <= 1){
            return;
        }

        graph.removeAllSeries();
        Points points = new Points(passportInfo.getCowParamsList().size());
        Date[] arrDate = new Date[passportInfo.getCowParamsList().size()];
        int index = 0;
        double maxY = 0;
        for (CowParams cowparams : passportInfo.getCowParamsList()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = new Date();
            try {
                date = dateFormat.parse(cowparams.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            arrDate[index] = date;
            myCalendar.add(Calendar.DATE, 1);
            double
                    dMilk = Double.parseDouble(cowparams.getMilk_yield()),
                    dWeight = Double.parseDouble(cowparams.getWeight()),
                    dFat = Double.parseDouble(cowparams.getFat());
            if (showMilksSeries && maxY < dMilk) maxY = dMilk;
            if (showWeightSeries && maxY < dWeight) maxY = dWeight;
            if (showFatSeries && maxY < dFat) maxY = dFat;
            points.pointsMilk[index] = new DataPoint(date, dMilk);
            points.pointsWeight[index] = new DataPoint(date, dWeight);
            points.pointsFat[index] = new DataPoint(date, dFat);
            index++;
        }
        milkSeries = new LineGraphSeries<>(points.pointsMilk);
        milkSeries.setColor(Color.BLUE);
        weightSeries = new LineGraphSeries<>(points.pointsWeight);
        weightSeries.setColor(Color.GREEN);
        fatSeries = new LineGraphSeries<>(points.pointsFat);
        fatSeries.setColor(Color.RED);

        if (showMilksSeries) graph.addSeries(milkSeries);
        if (showWeightSeries) graph.addSeries(weightSeries);
        if (showFatSeries) graph.addSeries(fatSeries);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(arrDate.length);

        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxY);
        graph.getViewport().setMinX(arrDate[0].getTime());
        graph.getViewport().setMaxX(arrDate[arrDate.length - 1].getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
    }

    private void updateLabel() {
        long time = myCalendar.getTimeInMillis();
        if (time > System.currentTimeMillis()) {
            Toast.makeText(getBaseContext(), "Не корректная дата", Toast.LENGTH_SHORT).show();
            return;
        }
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthday.setText(sdf.format(time));
        passportInfo.setBirthday(time);
    }

    @OnClick(R.id.birthday)
    public void onClickSetDate() {
        new DatePickerDialog(PassportActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.save)
    public void onClickSave() {
        if (passportInfo.save()) {
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Поля заполнены некорректно", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.create_new)
    public void onClickCreate() {
        if (passportInfo.save()) {
            Intent intent = new Intent(this, CowParamsActivity.class);
            intent.putExtra("id", passportInfo.getId());
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Необходимо корректно заполнить поля, чтобы добавить показания", Toast.LENGTH_SHORT).show();
        }
    }

    @OnCheckedChanged(R.id.checkBoxMilk)
    void onSelectedMilk(CompoundButton button, boolean checked) {
        showMilksSeries = checked;
        RenderGraphics();
    }

    @OnCheckedChanged(R.id.checkBoxWeight)
    void onSelectedWeight(CompoundButton button, boolean checked) {
        showWeightSeries = checked;
        RenderGraphics();
    }

    @OnCheckedChanged(R.id.checkBoxFat)
    void onSelectedFat(CompoundButton button, boolean checked) {
        showFatSeries = checked;
        RenderGraphics();
    }

    private class Points {
        DataPoint[] pointsMilk;
        DataPoint[] pointsWeight;
        DataPoint[] pointsFat;

        Points(int size) {
            pointsMilk = new DataPoint[size];
            pointsWeight = new DataPoint[size];
            pointsFat = new DataPoint[size];
        }
    }
}
