package com.reikyz.jandan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.reikyz.jandan.utils.Utils;

/**
 * Created by reikyZ on 16/9/28.
 */
public class DBHelper extends SQLiteOpenHelper {

    final static String TAG = "===DBHelper===";

    final static String DB_NAME = "jandan.db";
    Context mContext;
    static DBHelper sHelper = null;
    final static int VERSION = 1;

    final static String TAB_VOTE = " vote ";

    private static final String SQL_CREATE_VOTE_TABLE = "create table " +
            TAB_VOTE +
            "(_id integer primary key autoincrement, " +
            "post_id text, " +
            "positive integer, " +
            "negative integer, " +
            "vote text)";

    private static final String SQL_DROP_VOTE_TABLE = "drop table if exists " + TAB_VOTE;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mContext = context;
    }

    public static DBHelper getInstance(Context context) {
        if (sHelper == null) {
            sHelper = new DBHelper(context);
        }

        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_VOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (!tableIsExist(TAB_VOTE))
            db.execSQL(SQL_DROP_VOTE_TABLE);
    }

    public boolean tableIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) from " + DB_NAME + " where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        Log.e(TAG, tableName + "==" + result + Utils.getLineNumber(new Exception()));
        return result;
    }

}
