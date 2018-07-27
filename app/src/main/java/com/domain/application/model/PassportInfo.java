package com.domain.application.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class PassportInfo {
    private long id;
    private String number;
    private long birthday;
    private int fid_breed;
    private int fid_suit;
    private int fid_cow;
    private String breed;
    private String suits;
    private int age = -1;
    private SQLiteDatabase db;
    private List<CowParams> cowParamsList;

    private PassportInfo(SQLiteDatabase db) {
        this.db = db;
    }

    private PassportInfo(int id, String number, long birthday, int fid_breed, int fid_suit,
                         int fid_cow, String breed, String suits) {
        this.id = id;
        this.number = number;
        this.birthday = birthday;
        this.fid_breed = fid_breed;
        this.fid_suit = fid_suit;
        this.fid_cow = fid_cow;
        this.breed = breed;
        this.suits = suits;
    }

    public int getAge() {
        if (age != -1) {
            return age;
        }
        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, year, month, day;

        Date date = new Date(birthday);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        year = Integer.valueOf(formattedDate.substring(0, 4));
        month = Integer.valueOf(formattedDate.substring(5, 7));
        day = Integer.valueOf(formattedDate.substring(8, 10));

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day);
        age = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --age;
        }
        if (age < 0)
            age = 0;
        return age;
    }

    public static PassportInfo GetItemByID(SQLiteDatabase db, long id) {

        Cursor c;
        String sqlQuery = "SELECT c.*, b.name AS breed, s.name AS suit "
                + "FROM cows AS c "
                + "INNER JOIN breeds AS b ON c.fid_breed = b.id "
                + "INNER JOIN suits AS s ON c.fid_suit = s.id "
                + "WHERE c.id = ?";
        c = db.rawQuery(sqlQuery, new String[]{String.valueOf(id)});
        PassportInfo passportInfo = new PassportInfo(db);
        if (c != null) {
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int indexId = c.getColumnIndex("id");
                int indexNumber = c.getColumnIndex("number");
                int indexBirthday = c.getColumnIndex("birthday");
                int indexFidBreed = c.getColumnIndex("fid_breed");
                int indexFidSuit = c.getColumnIndex("fid_suit");
                int indexFidCow = c.getColumnIndex("fid_cow");
                int indexBreed = c.getColumnIndex("breed");
                int indexSuit = c.getColumnIndex("suit");
                passportInfo = new PassportInfo(
                        c.getInt(indexId),
                        c.getString(indexNumber),
                        c.getLong(indexBirthday),
                        c.getInt(indexFidBreed),
                        c.getInt(indexFidSuit),
                        c.getInt(indexFidCow),
                        c.getString(indexBreed),
                        c.getString(indexSuit)
                );
                passportInfo.db = db;
            }
            c.close();
        }

        return passportInfo;
    }

    public static List<PassportInfo> GetItems(SQLiteDatabase db) {
        Cursor c;
        String sqlQuery = "SELECT c.*, b.name AS breed, s.name AS suit "
                + "FROM cows AS c "
                + "INNER JOIN breeds AS b ON c.fid_breed = b.id "
                + "INNER JOIN suits AS s ON c.fid_suit = s.id ";
        c = db.rawQuery(sqlQuery, null);
        List<PassportInfo> listPassportInfo = new ArrayList<>();
        if (c != null) {

            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int indexId = c.getColumnIndex("id");
                int indexNumber = c.getColumnIndex("number");
                int indexBirthday = c.getColumnIndex("birthday");
                int indexFidBreed = c.getColumnIndex("fid_breed");
                int indexFidSuit = c.getColumnIndex("fid_suit");
                int indexFidCow = c.getColumnIndex("fid_cow");
                int indexBreed = c.getColumnIndex("breed");
                int indexSuit = c.getColumnIndex("suit");
                do {
                    PassportInfo passportInfo = new PassportInfo(
                            c.getInt(indexId),
                            c.getString(indexNumber),
                            c.getLong(indexBirthday),
                            c.getInt(indexFidBreed),
                            c.getInt(indexFidSuit),
                            c.getInt(indexFidCow),
                            c.getString(indexBreed),
                            c.getString(indexSuit)
                    );
                    listPassportInfo.add(passportInfo);
                } while (c.moveToNext());
            }
            c.close();
        }
        return listPassportInfo;
    }

    public boolean save() {
        ContentValues cv = new ContentValues();
        if (number == null || number.length() == 0 || (id == 0 && ExistNumber())) return false;
        cv.put("number", number);
        if (birthday == 0) return false;
        cv.put("birthday", birthday);
        if (fid_breed == 0) return false;
        cv.put("fid_breed", fid_breed);
        if (fid_suit == 0) return false;
        cv.put("fid_suit", fid_suit);
        if (fid_cow != 0)
            cv.put("fid_cow", fid_cow);
        if (id == 0) {
            id = db.insert("cows", null, cv);
            return 0 < id;
        } else {
            return 0 < db.update("cows", cv, "id = ?",
                    new String[]{String.valueOf(id)});
        }
    }

    private boolean ExistNumber() {
        Cursor c;
        String selection = "number = ?";
        String[] selectionArgs = new String[]{number};

        c = db.query("cows", null, selection, selectionArgs, null,
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public int getFidBreed() {
        return fid_breed;
    }

    public void setFidBreed(int fid_breed) {
        this.fid_breed = fid_breed;
    }

    public int getFidSuits() {
        return fid_suit;
    }

    public void setFidSuits(int fid_suits) {
        this.fid_suit = fid_suits;
    }

    public int getFidCow() {
        return fid_cow;
    }

    public void setFidCow(int fid_cow) {
        this.fid_cow = fid_cow;
    }

    public String getBreed() {
        return breed;
    }

    public String getSuits() {
        return suits;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public List<CowParams> getCowParamsList() {
        return CowParams.GetItems(db, id);
    }
}
