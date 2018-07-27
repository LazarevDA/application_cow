package com.domain.application.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.domain.application.config.Config;

public class DBHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = "DB_LOG";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, Config.DB_NAME, null, Config.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу пород
        ContentValues values;
        db.execSQL("CREATE TABLE breeds("
                + "id integer primary key autoincrement,"
                + "name text"
                + ");");
        db.execSQL("INSERT INTO breeds (name) VALUES "
                + "('Голштинская'),"
                + "('Черно-пестрая'),"
                + "('Джерсейская'),"
                + "('Симментальская'),"
                + "('Айширская'),"
                + "('Красная степная'),"
                + "('Герефордская'),"
                + "('Ярославская'),"
                + "('Холмогорская'),"
                + "('Голландская')"
                + ";");
        // создаем таблицу мастей
        db.execSQL("CREATE TABLE suits("
                + "id integer primary key autoincrement,"
                + "name text"
                + ");");
        db.execSQL("INSERT INTO suits (name) VALUES "
                + "('Красная'),"
                + "('Черная'),"
                + "('Белая'),"
                + "('Красно-белая'),"
                + "('Бурая'),"
                + "('Светло-рыжая'),"
                + "('Светло-красная'),"
                + "('Светло-бурая'),"
                + "('Серая'),"
                + "('Палевая'),"
                + "('Леопардовая')"
                + ";");
        // создаем таблицу паспортов
        db.execSQL("CREATE TABLE cows("
                + "id integer primary key autoincrement,"
                + "number text,"            // номер бирки
                + "birthday integer,"       // дата рождения в формате Unix Time
                + "fid_breed integer,"      // FK breeds.id
                + "fid_suit integer,"      // FK suits.id
                + "fid_cow integer,"        // FK cows.id мать коровы
                + "FOREIGN KEY (fid_breed) REFERENCES breeds(id),"
                + "FOREIGN KEY (fid_suit) REFERENCES suits(id),"
                + "FOREIGN KEY (fid_cow) REFERENCES cows(id)"
                + ");");
        db.execSQL("INSERT INTO cows (number,birthday,fid_breed,fid_suit) VALUES "
                + "('000001', 1132151777182, 1, 8),"
                + "('000002', 1232152777182, 2, 7),"
                + "('000003', 1332553777182, 3, 6),"
                + "('000004', 1432554777182, 4, 5),"
                + "('000005', 1132655777182, 5, 4),"
                + "('000006', 1232756777182, 6, 3),"
                + "('000007', 1332757777182, 7, 2),"
                + "('000008', 1232758777182, 8, 1)"
                + ";");
        // создаем таблицу статистики
        db.execSQL("CREATE TABLE statistics("
                + "id integer primary key autoincrement,"
                + "date text,"              // дата измерения показателей в yyyy-MM-dd
                + "fid_cow integer,"        // FK cows.id
                + "milk_yield text,"        // надой молока
                + "weight text,"            // вес коровы
                + "fat text,"               // массова доля жира
                + "FOREIGN KEY (fid_cow) REFERENCES cows(id)"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // вывод в лог данных из курсора
    public void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }

}
