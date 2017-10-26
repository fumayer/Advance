package com.quduquxie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.quduquxie.db.table.DraftTable;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.util.QGLog;

import java.util.ArrayList;

/**
 * Created on 16/11/22.
 * Created by crazylei.
 */

public class DraftDao {

    private static final String TAG = DraftDao.class.getSimpleName();

    private static final String DATABASE_NAME = "draft.db";
    private static final int DATABASE_VERSION = 1;

    private static DraftDao draftDao;
    private SQLiteHelper helper = null;

    private static final String SQL_CREATE_DRAFT = "create table IF NOT EXISTS " + DraftTable.TABLE_NAME + "("
            + DraftTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DraftTable.ID + " VARCHAR(100) , "
            + DraftTable.NAME + " VARCHAR(250) , "
            + DraftTable.STATUS + " VARCHAR(40) , "
            + DraftTable.CONTENT + " TEXT , "
            + DraftTable.WORD_COUNT + " long , "
            + DraftTable.UPDATE_TIME + " long , "
            + DraftTable.CHECK_STATUS + " VARCHAR(50) , "
            + DraftTable.CHECK_MESSAGE + " VARCHAR(300) , "
            + DraftTable.SERIAL_NUMBER + " INTEGER , "
            + DraftTable.BOOK_ID + " VARCHAR(100) , "
            + DraftTable.NEED_SYNCHRONIZE + " INTEGER , "
            + DraftTable.LOCAL + " VARCHAR(100)  "
            + ");";


    private DraftDao(Context context) {
        this.helper = SQLiteHelper.getInstance(context);
    }

    public synchronized static DraftDao getInstance(Context context) {
        if (draftDao == null) {
            draftDao = new DraftDao(context);
        }
        return draftDao;
    }

    public boolean isContainsDraft(String id) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            database = helper.getReadableDatabase();

            cursor = database.query(DraftTable.TABLE_NAME, null, DraftTable.ID + " = " + "'" + id + "'", null, null, null, null);

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

    public boolean isContainsLocalDraft(String local) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            database = helper.getReadableDatabase();

            cursor = database.query(DraftTable.TABLE_NAME, null, DraftTable.LOCAL + " = " + "'" + local + "'", null, null, null, null);

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

    public boolean insertDraft(Draft draft) {
        SQLiteDatabase database = null;
        long result = -1;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DraftTable.ID, draft.id);
            contentValues.put(DraftTable.NAME, draft.name);
            contentValues.put(DraftTable.STATUS, draft.status);
            contentValues.put(DraftTable.CONTENT, draft.content);
            contentValues.put(DraftTable.WORD_COUNT, draft.word_count);
            contentValues.put(DraftTable.UPDATE_TIME, draft.update_time);
            contentValues.put(DraftTable.CHECK_STATUS, draft.check_status);
            contentValues.put(DraftTable.CHECK_MESSAGE, draft.check_message);
            contentValues.put(DraftTable.SERIAL_NUMBER, draft.serial_number);
            contentValues.put(DraftTable.BOOK_ID, draft.book_id);
            contentValues.put(DraftTable.NEED_SYNCHRONIZE, draft.need_synchronize);
            contentValues.put(DraftTable.LOCAL, draft.local);

            result = database.insert(DraftTable.TABLE_NAME, null, contentValues);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        QGLog.e(TAG, "InsertDraft: " + result);
        return result != -1;
    }

    public boolean updateDraft(Draft draft) {
        SQLiteDatabase database = null;
        long result = 0;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (!TextUtils.isEmpty(draft.id)) {
                contentValues.put(DraftTable.ID, draft.id);
            }
            if (!TextUtils.isEmpty(draft.name)) {
                contentValues.put(DraftTable.NAME, draft.name);
            }
            if (!TextUtils.isEmpty(draft.status)) {
                contentValues.put(DraftTable.STATUS, draft.status);
            }
            if (!TextUtils.isEmpty(draft.content)) {
                contentValues.put(DraftTable.CONTENT, draft.content);
            }
            if (draft.word_count != 0) {
                contentValues.put(DraftTable.WORD_COUNT, draft.word_count);
            }
            if (draft.update_time != 0) {
                contentValues.put(DraftTable.UPDATE_TIME, draft.update_time);
            }
            if (!TextUtils.isEmpty(draft.check_status)) {
                contentValues.put(DraftTable.CHECK_STATUS, draft.check_status);
            }
            if (!TextUtils.isEmpty(draft.check_message)) {
                contentValues.put(DraftTable.CHECK_MESSAGE, draft.check_message);
            }
            if (draft.serial_number != 0) {
                contentValues.put(DraftTable.SERIAL_NUMBER, draft.serial_number);
            }
            if (!TextUtils.isEmpty(draft.book_id)) {
                contentValues.put(DraftTable.BOOK_ID, draft.book_id);
            }

            if (draft.need_synchronize != -1) {
                contentValues.put(DraftTable.NEED_SYNCHRONIZE, draft.need_synchronize);
            }

            if (!TextUtils.isEmpty(draft.local)) {
                contentValues.put(DraftTable.LOCAL, draft.local);
            }

            result = database.update(DraftTable.TABLE_NAME, contentValues, DraftTable.ID + " =? ", new String[]{draft.id});

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return result != 0;
    }

    public boolean updateLocalDraft(Draft draft) {
        SQLiteDatabase database = null;
        long result = 0;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (!TextUtils.isEmpty(draft.id)) {
                contentValues.put(DraftTable.ID, draft.id);
            }
            if (!TextUtils.isEmpty(draft.name)) {
                contentValues.put(DraftTable.NAME, draft.name);
            }
            if (!TextUtils.isEmpty(draft.status)) {
                contentValues.put(DraftTable.STATUS, draft.status);
            }
            if (!TextUtils.isEmpty(draft.content)) {
                contentValues.put(DraftTable.CONTENT, draft.content);
            }
            if (draft.word_count != 0) {
                contentValues.put(DraftTable.WORD_COUNT, draft.word_count);
            }
            if (draft.update_time != 0) {
                contentValues.put(DraftTable.UPDATE_TIME, draft.update_time);
            }
            if (!TextUtils.isEmpty(draft.check_status)) {
                contentValues.put(DraftTable.CHECK_STATUS, draft.check_status);
            }
            if (!TextUtils.isEmpty(draft.check_message)) {
                contentValues.put(DraftTable.CHECK_MESSAGE, draft.check_message);
            }
            if (draft.serial_number != 0) {
                contentValues.put(DraftTable.SERIAL_NUMBER, draft.serial_number);
            }
            if (!TextUtils.isEmpty(draft.book_id)) {
                contentValues.put(DraftTable.BOOK_ID, draft.book_id);
            }

            if (draft.need_synchronize != -1) {
                contentValues.put(DraftTable.NEED_SYNCHRONIZE, draft.need_synchronize);
            }

            if (!TextUtils.isEmpty(draft.local)) {
                contentValues.put(DraftTable.LOCAL, draft.local);
            }

            result = database.update(DraftTable.TABLE_NAME, contentValues, DraftTable.LOCAL + " =? ", new String[]{draft.local});

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return result != 0;
    }

    public ArrayList<Draft> getDraftsByID(String id) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        ArrayList<Draft> drafts = new ArrayList<>();

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(DraftTable.TABLE_NAME, null, DraftTable.BOOK_ID + "=?", new String[]{id}, null, null, DraftTable.SERIAL_NUMBER + " desc");
            Draft draft = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                draft = new Draft();
                loadDraftFromDB(draft, cursor);
                drafts.add(draft);
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
        return drafts;
    }

    public ArrayList<Draft> getDrafts() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        ArrayList<Draft> drafts = new ArrayList<>();

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(DraftTable.TABLE_NAME, null, null, null, null, null, DraftTable.SERIAL_NUMBER + " desc");
            Draft draft = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                draft = new Draft();
                loadDraftFromDB(draft, cursor);
                drafts.add(draft);
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
        return drafts;
    }

    public int getDraftCount(String id) {
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(DraftTable.TABLE_NAME, null, DraftTable.BOOK_ID + " =? ", new String[]{String.valueOf(id)}, null, null, DraftTable.UPDATE_TIME + " desc");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                count++;
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
        return count;
    }

    public Draft getDraft(String id) {
        Draft draft = null;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(DraftTable.TABLE_NAME, null, DraftTable.ID + " =? ", new String[]{String.valueOf(id)}, null, null, DraftTable.SERIAL_NUMBER + " desc");
            if (cursor.moveToFirst()) {
                draft = new Draft();
                loadDraftFromDB(draft, cursor);
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
        return draft;
    }

    private void loadDraftFromDB(Draft draft, Cursor cursor) {
        draft.id = cursor.getString(DraftTable.ID_INDEX);
        draft.name = cursor.getString(DraftTable.NAME_INDEX);
        draft.status = cursor.getString(DraftTable.STATUS_INDEX);
        draft.content = cursor.getString(DraftTable.CONTENT_INDEX);
        draft.word_count = cursor.getLong(DraftTable.WORD_COUNT_INDEX);
        draft.update_time = cursor.getLong(DraftTable.UPDATE_TIME_INDEX);
        draft.check_status = cursor.getString(DraftTable.CHECK_STATUS_INDEX);
        draft.check_message = cursor.getString(DraftTable.CHECK_MESSAGE_INDEX);
        draft.serial_number = cursor.getInt(DraftTable.SERIAL_NUMBER_INDEX);
        draft.book_id = cursor.getString(DraftTable.BOOK_ID_INDEX);
        draft.need_synchronize = cursor.getInt(DraftTable.NEED_SYNCHRONIZE_INDEX);
        draft.local = cursor.getString(DraftTable.LOCAL_INDEX);
    }

    public void deleteDraft(String id) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(DraftTable.TABLE_NAME, DraftTable.ID + " = " + "'" + id + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public void deleteLocalDraft(String id) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(DraftTable.TABLE_NAME, DraftTable.LOCAL + " = " + "'" + id + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public void deleteAllDraft() {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(DraftTable.TABLE_NAME, null, null);
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
            database.execSQL(SQL_CREATE_DRAFT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int old_version, int new_version) {

        }
    }
}
