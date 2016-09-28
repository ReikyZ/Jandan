package com.reikyz.jandan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reikyz.jandan.utils.Utils;

/**
 * Created by reikyZ on 16/9/28.
 */
public class VoteDAOImpl {

    final static String TAG = "===VoteDAOImpl===";

    private static VoteDAOImpl voteDAO;
    DatabaseManager databaseManager;

    public VoteDAOImpl(Context context) {
        databaseManager = DatabaseManager.initInstance(DBHelper.getInstance(context));
    }

    public synchronized static VoteDAOImpl getInstance(Context context) {
        if (voteDAO == null) {
            voteDAO = new VoteDAOImpl(context);
        }
        return voteDAO;
    }


    private SQLiteDatabase openDb() {
        return databaseManager.getInstance().openDatabase();
    }

    private void closeDb() {
        databaseManager.getInstance().closeDatabase();
    }

    public synchronized boolean isExist(String post_id) {
        SQLiteDatabase db = openDb();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                        DBHelper.TAB_VOTE +
                        "WHERE post_id = ?",
                new String[]{post_id});
        boolean exist = cursor.moveToNext();
        cursor.close();
        closeDb();
        return exist;
    }

    public synchronized void setPositive(String post_id, Integer positive) {
        if (isExist(post_id)) {
            updatePositive(post_id, positive);
            return;
        }
        SQLiteDatabase db = openDb();
        db.execSQL("INSERT INTO " +
                        DBHelper.TAB_VOTE +
                        "(post_id," +
                        "positive) " +
                        "VALUES(?,?)",
                new Object[]{post_id, positive});
        closeDb();
    }

    public synchronized void updatePositive(String post_id, Integer positive) {
        SQLiteDatabase db = openDb();
        db.execSQL("UPDATE " +
                        DBHelper.TAB_VOTE +
                        "SET " +
                        "positive = ? " +
                        "WHERE " +
                        "post_id = ?",
                new String[]{String.valueOf(positive), post_id});
        closeDb();
    }

    public synchronized void setNegative(String post_id, Integer negative) {
        if (isExist(post_id)) {
            updateNegative(post_id, negative);
            return;
        }
        SQLiteDatabase db = openDb();
        db.execSQL("INSERT INTO " +
                        DBHelper.TAB_VOTE +
                        "(post_id," +
                        "negative) " +
                        "VALUES(?,?)",
                new Object[]{post_id, negative});
        closeDb();
    }

    public synchronized void updateNegative(String post_id, Integer negative) {
        SQLiteDatabase db = openDb();
        db.execSQL("UPDATE " +
                        DBHelper.TAB_VOTE +
                        "SET " +
                        "negative = ? " +
                        "WHERE " +
                        "post_id = ?",
                new String[]{String.valueOf(negative), post_id});
        closeDb();
    }

    public synchronized int getVoted(String post_id) {
        SQLiteDatabase db = openDb();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                        DBHelper.TAB_VOTE +
                        "WHERE post_id = ?",
                new String[]{post_id});
        int vote = 0;
        while (cursor.moveToNext()) {
            try {
                vote = Integer.parseInt(cursor.getString(cursor.getColumnIndex("vote")));
            } catch (Exception e) {
                vote = 0;
            }
        }
        cursor.close();
        closeDb();
        Utils.log(TAG, "getVoted ===" + post_id + "===status===" + vote);
        return vote;
    }

    public synchronized void setVoted(String post_id, Integer vote) {
        SQLiteDatabase db = openDb();
        db.execSQL("UPDATE " +
                        DBHelper.TAB_VOTE +
                        "SET " +
                        "vote = ? " +
                        "WHERE " +
                        "post_id = ?",
                new String[]{String.valueOf(vote), post_id});
        closeDb();
        Utils.log(TAG, "setVoted ===" + post_id + "===status===" + vote);
    }


}
