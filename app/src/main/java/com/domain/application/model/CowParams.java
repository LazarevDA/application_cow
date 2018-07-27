package com.domain.application.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CowParams {
    private Long id;
    private String date;
    private String milk_yield;
    private String weight;
    private String fat;
    private SQLiteDatabase db;
    private Long fid_cow;

    public CowParams(SQLiteDatabase db) {
        this.db = db;
    }

    private CowParams(Long id, String date, String milk_yield, String weight, String fat, Long fid_cow) {
        this.id = id;
        this.date = date;
        this.milk_yield = milk_yield;
        this.weight = weight;
        this.fat = fat;
        this.fid_cow = fid_cow;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMilk_yield() {
        return milk_yield;
    }

    public void setMilk_yield(String milk_yield) {
        this.milk_yield = milk_yield;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setFid_cow(Long fid_cow) {
        this.fid_cow = fid_cow;
    }

    public static List<CowParams> GetItems(SQLiteDatabase db, long id) {

        Cursor c;
        String sqlQuery = "SELECT s.* "
                + "FROM statistics AS s "
                + "WHERE s.fid_cow = ? "
                + "ORDER BY s.date ASC ";
        c = db.rawQuery(sqlQuery, new String[]{String.valueOf(id)});
        List<CowParams> list = new ArrayList<>();
        CowParams item;
        if (c != null) {
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int indexId = c.getColumnIndex("id");
                int indexDate = c.getColumnIndex("date");
                int indexMilkYield = c.getColumnIndex("milk_yield");
                int indexWeight = c.getColumnIndex("weight");
                int indexFat = c.getColumnIndex("fat");
                int indexFidCow = c.getColumnIndex("fid_cow");
                do {
                    item = new CowParams(
                            c.getLong(indexId),
                            c.getString(indexDate),
                            c.getString(indexMilkYield),
                            c.getString(indexWeight),
                            c.getString(indexFat),
                            c.getLong(indexFidCow)
                    );
                    list.add(item);
                } while (c.moveToNext());
            }
            c.close();
        }
        return list;
    }


    public boolean save() {
        ContentValues cv = new ContentValues();
        if (fid_cow == 0) return false;
        cv.put("fid_cow", fid_cow);
        if (date == null || date.length() == 0 || ExistDate()) return false;
        cv.put("date", date);
        if (weight == null || weight.length() == 0) return false;
        cv.put("weight", weight);
        if (milk_yield == null || milk_yield.length() == 0) return false;
        cv.put("milk_yield", milk_yield);
        if (fat == null || fat.length() == 0) return false;
        cv.put("fat", fat);
        return 0 < db.insert("statistics", null, cv);
    }

    private boolean ExistDate() {
        Cursor c;
        String selection = "fid_cow = ? and date = ?";
        String[] selectionArgs = new String[]{String.valueOf(fid_cow), date};

        c = db.query("statistics", null, selection, selectionArgs, null,
                null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                c.close();
                return true;
            }
            c.close();
        }
        return false;
    }
}
