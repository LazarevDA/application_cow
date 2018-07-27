package com.domain.application.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Suit {
    private int id;
    private String name;

    private Suit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static List<Suit> GetItems(SQLiteDatabase db) {
        Cursor c;
        String sqlQuery = "SELECT * from suits";
        c = db.rawQuery(sqlQuery, null);
        List<Suit> list = new ArrayList<>();
        Suit item = new Suit(
                0,
                "-"
        );
        list.add(item);
        if (c != null) {

            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int indexId = c.getColumnIndex("id");
                int indexName = c.getColumnIndex("name");
                do {
                    item = new Suit(
                            c.getInt(indexId),
                            c.getString(indexName)
                    );
                    list.add(item);
                } while (c.moveToNext());
            }
            c.close();
        }
        return list;
    }
}
