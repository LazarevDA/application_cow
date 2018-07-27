package com.domain.application.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cow {
    private int id;
    private String number;

    private Cow(int id, String number) {
        this.id = id;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public static List<Cow> GetItems(SQLiteDatabase db, long fid_cow) {
        Cursor c;
        String sqlQuery = "SELECT * from cows where id <> ?";
        c = db.rawQuery(sqlQuery, new String[]{String.valueOf(fid_cow)});
        List<Cow> list = new ArrayList<>();
        Cow item = new Cow(
                0,
                "-"
        );
        list.add(item);
        if (c != null) {

            if (c.moveToFirst()) {
                int indexId = c.getColumnIndex("id");
                int indexNumber = c.getColumnIndex("number");
                do {
                    item = new Cow(
                            c.getInt(indexId),
                            c.getString(indexNumber)
                    );
                    list.add(item);
                } while (c.moveToNext());
            }
            c.close();
        }
        return list;
    }
}
