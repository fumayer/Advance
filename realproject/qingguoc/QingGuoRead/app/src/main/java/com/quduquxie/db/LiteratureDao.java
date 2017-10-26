package com.quduquxie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.quduquxie.db.table.LiteratureTable;
import com.quduquxie.model.creation.Literature;

import java.util.ArrayList;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class LiteratureDao {

    private static final String DATABASE_NAME = "literature.db";
    private static final int DATABASE_VERSION = 1;

    private static LiteratureDao literatureDao;
    private SQLiteHelper helper = null;

    private static final String SQL_CREATE_NOVEL = "create table IF NOT EXISTS " + LiteratureTable.TABLE_NAME + "("
            + LiteratureTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LiteratureTable.ID + " VARCHAR(100) , "
            + LiteratureTable.NAME + " VARCHAR(250) , "
            + LiteratureTable.DESCRIPTION + " VARCHAR(600) , "
            + LiteratureTable.ATTRIBUTE + " VARCHAR(40) , "
            + LiteratureTable.CATEGORY + " VARCHAR(50) , "
            + LiteratureTable.STYLE + " VARCHAR(40) , "
            + LiteratureTable.ENDING + " VARCHAR(40) , "
            + LiteratureTable.FENPIN + " VARCHAR(40) , "
            + LiteratureTable.IMAGE_URL + " VARCHAR(250) , "
            + LiteratureTable.WORD_COUNT + " long , "
            + LiteratureTable.IS_SIGN + " INTEGER default -1 , "
            + LiteratureTable.STATUS + " VARCHAR(40) , "
            + LiteratureTable.SERIAL_NUMBER + " INTEGER default 0 "
            + ");";

    private LiteratureDao(Context context) {
        this.helper = SQLiteHelper.getInstance(context);
    }

    public synchronized static LiteratureDao getInstance(Context context) {
        if (literatureDao == null) {
            literatureDao = new LiteratureDao(context);
        }
        return literatureDao;
    }

    public boolean isContainsLiterature(String id) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            database = helper.getReadableDatabase();

            cursor = database.query(LiteratureTable.TABLE_NAME, null, LiteratureTable.ID + " = " + "'" + id + "'", null, null, null, null);

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

    public boolean insertLiterature(Literature literature) {
        SQLiteDatabase database = null;
        long result = -1;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(LiteratureTable.ID, literature.id);
            contentValues.put(LiteratureTable.NAME, literature.name);
            contentValues.put(LiteratureTable.DESCRIPTION, literature.description);
            contentValues.put(LiteratureTable.ATTRIBUTE, literature.attribute);
            contentValues.put(LiteratureTable.CATEGORY, literature.category);
            contentValues.put(LiteratureTable.STYLE, literature.style);
            contentValues.put(LiteratureTable.ENDING, literature.ending);
            contentValues.put(LiteratureTable.FENPIN, literature.fenpin);
            contentValues.put(LiteratureTable.IMAGE_URL, literature.image_url);
            contentValues.put(LiteratureTable.WORD_COUNT, literature.word_count);
            contentValues.put(LiteratureTable.IS_SIGN, literature.is_sign);
            contentValues.put(LiteratureTable.STATUS, literature.status);
            contentValues.put(LiteratureTable.SERIAL_NUMBER, literature.serial_number);
            result = database.insert(LiteratureTable.TABLE_NAME, null, contentValues);

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return result != -1;
    }

    public boolean updateLiterature(Literature literature) {
        SQLiteDatabase database = null;
        long result = 0;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (!TextUtils.isEmpty(literature.id)) {
                contentValues.put(LiteratureTable.ID, literature.id);
            }
            if (!TextUtils.isEmpty(literature.name)) {
                contentValues.put(LiteratureTable.NAME, literature.name);
            }
            if (!TextUtils.isEmpty(literature.description)) {
                contentValues.put(LiteratureTable.DESCRIPTION, literature.description);
            }
            if (!TextUtils.isEmpty(literature.attribute)) {
                contentValues.put(LiteratureTable.ATTRIBUTE, literature.attribute);
            }
            if (!TextUtils.isEmpty(literature.category)) {
                contentValues.put(LiteratureTable.CATEGORY, literature.category);
            }
            if (!TextUtils.isEmpty(literature.style)) {
                contentValues.put(LiteratureTable.STYLE, literature.style);
            }
            if (!TextUtils.isEmpty(literature.ending)) {
                contentValues.put(LiteratureTable.ENDING, literature.ending);
            }
            if (!TextUtils.isEmpty(literature.fenpin)) {
                contentValues.put(LiteratureTable.FENPIN, literature.fenpin);
            }
            if (!TextUtils.isEmpty(literature.image_url)) {
                contentValues.put(LiteratureTable.IMAGE_URL, literature.image_url);
            }
            if (literature.word_count != 0) {
                contentValues.put(LiteratureTable.WORD_COUNT, literature.word_count);
            }
            if (literature.is_sign != -1) {
                contentValues.put(LiteratureTable.IS_SIGN, literature.is_sign);
            }
            if (!TextUtils.isEmpty(literature.status)) {
                contentValues.put(LiteratureTable.STATUS, literature.status);
            }
            if (literature.serial_number != 0) {
                contentValues.put(LiteratureTable.SERIAL_NUMBER, literature.serial_number);
            }

            result = database.update(LiteratureTable.TABLE_NAME, contentValues, LiteratureTable.ID + " =? ", new String[]{literature.id});

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return result != 0;
    }

    public ArrayList<Literature> getLiteratureList() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        ArrayList<Literature> literatureList = new ArrayList<>();

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(LiteratureTable.TABLE_NAME, null, null, null, null, null, null);
            Literature literature = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                literature = new Literature();
                loadLiteratureFromDB(literature, cursor);
                literatureList.add(literature);
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
        return literatureList;
    }

    public Literature getLiterature(String id) {
        Cursor cursor = null;
        Literature literature = null;
        SQLiteDatabase database = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(LiteratureTable.TABLE_NAME, null, LiteratureTable.ID + " =? ", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                literature = new Literature();
                loadLiteratureFromDB(literature, cursor);
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
        return literature;
    }

    private void loadLiteratureFromDB(Literature literature, Cursor cursor) {
        literature.id = cursor.getString(LiteratureTable.ID_INDEX);
        literature.name = cursor.getString(LiteratureTable.NAME_INDEX);
        literature.description = cursor.getString(LiteratureTable.DESCRIPTION_INDEX);
        literature.attribute = cursor.getString(LiteratureTable.ATTRIBUTE_INDEX);
        literature.category = cursor.getString(LiteratureTable.CATEGORY_INDEX);
        literature.style = cursor.getString(LiteratureTable.STYLE_INDEX);
        literature.ending = cursor.getString(LiteratureTable.ENDING_INDEX);
        literature.fenpin = cursor.getString(LiteratureTable.FENPIN_INDEX);
        literature.image_url = cursor.getString(LiteratureTable.IMAGE_URL_INDEX);
        literature.word_count = cursor.getLong(LiteratureTable.WORD_COUNT_INDEX);
        literature.is_sign = cursor.getInt(LiteratureTable.IS_SIGN_INDEX);
        literature.status = cursor.getString(LiteratureTable.STATUS_INDEX);
        literature.serial_number = cursor.getInt(LiteratureTable.SERIAL_NUMBER_INDEX);
    }

    public void deleteLiterature(String id) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(LiteratureTable.TABLE_NAME, LiteratureTable.ID + " = " + "'" + id + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public void deleteAllLiterature() {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(LiteratureTable.TABLE_NAME, null, null);
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
