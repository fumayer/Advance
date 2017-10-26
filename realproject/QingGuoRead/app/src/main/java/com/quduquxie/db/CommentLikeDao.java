package com.quduquxie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.quduquxie.db.table.CommentLikeTable;

import java.util.ArrayList;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class CommentLikeDao {

    private static final String DATABASE_NAME = "comment_like.db";
    private static final int DATABASE_VERSION = 1;

    private static CommentLikeDao commentLikeDao;
    private SQLiteHelper helper = null;

    private static final String SQL_CREATE_NOVEL = "create table IF NOT EXISTS " + CommentLikeTable.TABLE_NAME + "("
            + CommentLikeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CommentLikeTable.ID + " VARCHAR(100) "
            + ");";

    private CommentLikeDao(Context context) {
        this.helper = SQLiteHelper.getInstance(context);
    }

    public synchronized static CommentLikeDao getInstance(Context context) {
        if (commentLikeDao == null) {
            commentLikeDao = new CommentLikeDao(context);
        }
        return commentLikeDao;
    }

    public boolean isContainsComment(String id) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            database = helper.getReadableDatabase();

            cursor = database.query(CommentLikeTable.TABLE_NAME, null, CommentLikeTable.ID + " = " + "'" + id + "'", null, null, null, null);

            result = cursor.moveToFirst();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (database != null) {
                database.close();
            }
        }
        return result;
    }

    public boolean insertComment(String id_comment) {
        SQLiteDatabase database = null;
        long result = -1;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CommentLikeTable.ID, id_comment);
            result = database.insert(CommentLikeTable.TABLE_NAME, null, contentValues);

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return result != -1;
    }

    public ArrayList<String> getCommentIDList() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        ArrayList<String> commentIDList = new ArrayList<>();

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(CommentLikeTable.TABLE_NAME, null, null, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                commentIDList.add(cursor.getString(CommentLikeTable.ID_INDEX));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (database != null) {
                database.close();
            }
        }
        return commentIDList;
    }

    public void deleteComment(String id) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(CommentLikeTable.TABLE_NAME, CommentLikeTable.ID + " = " + "'" + id + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public void deleteAllComment() {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(CommentLikeTable.TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    private static class SQLiteHelper extends SQLiteOpenHelper {

        private static SQLiteHelper helper;

        private SQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public synchronized static SQLiteHelper getInstance(Context context) {
            if (helper == null) {
                helper = new SQLiteHelper(context);
            }
            return helper;
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(SQL_CREATE_NOVEL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int old_version, int new_version) {

        }
    }
}
