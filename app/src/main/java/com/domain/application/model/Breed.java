package com.domain.application.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Breed {
    private int id;
    private String name;

    private Breed(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static List<Breed> GetItems(SQLiteDatabase db) {
        Cursor c;
        String sqlQuery = "SELECT * from breeds";
        c = db.rawQuery(sqlQuery, null);
        List<Breed> list = new ArrayList<>();
        Breed breed = new Breed(
                0,
                "-"
        );
        list.add(breed);
        if (c != null) {

            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int indexId = c.getColumnIndex("id");
                int indexName = c.getColumnIndex("name");
                do {
                    breed = new Breed(
                            c.getInt(indexId),
                            c.getString(indexName)
                    );
                    list.add(breed);
                } while (c.moveToNext());
            }
            c.close();
        }
        return list;
    }
}
