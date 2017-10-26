package com.quduquxie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.quduquxie.db.table.SectionTable;
import com.quduquxie.model.creation.Section;

import java.util.ArrayList;

public class SectionDao {

    private SQLiteHelper helper = null;

    private String DATABASE_NAME;

    private static final int version = 1;
    private static final String TABLE_SECTION = "section";

    private static final String SQL_CREATE_SECTION = "create table IF NOT EXISTS " + TABLE_SECTION + "( " +
            SectionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            SectionTable.ID + " VARCHAR(100) , " +
            SectionTable.NAME + " VARCHAR(250) , " +
            SectionTable.STATUS + " VARCHAR(40) , " +
            SectionTable.CONTENT + " TEXT , " +
            SectionTable.WORD_COUNT + " long , " +
            SectionTable.UPDATE_TIME + " long , " +
            SectionTable.CHECK_STATUS + " VARCHAR(40) , " +
            SectionTable.CHECK_MESSAGE + " VARCHAR(300) , " +
            SectionTable.SERIAL_NUMBER + " INTEGER "
            + ")";

    public SectionDao(Context context, String _literature_id) {
        DATABASE_NAME = "section" + _literature_id;
        this.helper = new SQLiteHelper(context);
    }

    public boolean insertSection(Section section) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            if (section != null) {
                database = helper.getWritableDatabase();
                database.beginTransaction();
                cursor = database.rawQuery("select count(_id) from " + TABLE_SECTION, null);

                ContentValues contentValues = new ContentValues();
                contentValues.put(SectionTable.ID, section.id);
                contentValues.put(SectionTable.NAME, section.name);
                contentValues.put(SectionTable.STATUS, section.status);
                contentValues.put(SectionTable.CONTENT, section.content);
                contentValues.put(SectionTable.WORD_COUNT, section.word_count);
                contentValues.put(SectionTable.UPDATE_TIME, section.update_time);
                contentValues.put(SectionTable.CHECK_STATUS, section.check_status);
                contentValues.put(SectionTable.CHECK_MESSAGE, section.check_message);
                contentValues.put(SectionTable.SERIAL_NUMBER, section.serial_number);

                database.insert(TABLE_SECTION, null, contentValues);

                database.setTransactionSuccessful();
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean insertSections(ArrayList<Section> sections) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            if (sections != null && sections.size() != 0) {
                database = helper.getWritableDatabase();
                database.beginTransaction();
                cursor = database.rawQuery("select count(_id) from " + TABLE_SECTION, null);
                boolean flag = cursor.moveToNext();
                for (Section section : sections) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(SectionTable.ID, section.id);
                    contentValues.put(SectionTable.NAME, section.name);
                    contentValues.put(SectionTable.STATUS, section.status);
                    contentValues.put(SectionTable.CONTENT, section.content);
                    contentValues.put(SectionTable.WORD_COUNT, section.word_count);
                    contentValues.put(SectionTable.UPDATE_TIME, section.update_time);
                    contentValues.put(SectionTable.CHECK_STATUS, section.check_status);
                    contentValues.put(SectionTable.CHECK_MESSAGE, section.check_message);
                    contentValues.put(SectionTable.SERIAL_NUMBER, section.serial_number);

                    database.insert(TABLE_SECTION, null, contentValues);
                }
                database.setTransactionSuccessful();
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean isContainsSection(String id) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(TABLE_SECTION, null, SectionTable.ID + " = " + "'" + id + "'", null, null, null, null);
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

    public boolean updateSection(Section section) {
        SQLiteDatabase database = null;
        long result = 0;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (!TextUtils.isEmpty(section.id)) {
                contentValues.put(SectionTable.ID, section.id);
            }
            if (!TextUtils.isEmpty(section.name)) {
                contentValues.put(SectionTable.NAME, section.name);
            }
            if (!TextUtils.isEmpty(section.status)) {
                contentValues.put(SectionTable.STATUS, section.status);
            }
            if (!TextUtils.isEmpty(section.content)) {
                contentValues.put(SectionTable.CONTENT, section.content);
            }
            if (section.word_count != 0) {
                contentValues.put(SectionTable.WORD_COUNT, section.word_count);
            }
            if (section.update_time != 0) {
                contentValues.put(SectionTable.UPDATE_TIME, section.update_time);
            }
            if (!TextUtils.isEmpty(section.check_status)) {
                contentValues.put(SectionTable.CHECK_STATUS, section.check_status);
            }
            if (!TextUtils.isEmpty(section.check_message)) {
                contentValues.put(SectionTable.CHECK_MESSAGE, section.check_message);
            }
            if (section.serial_number != 0) {
                contentValues.put(SectionTable.SERIAL_NUMBER, section.serial_number);
            }

            result = database.update(TABLE_SECTION, contentValues, SectionTable.ID + " =? ", new String[]{section.id});

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return result != 0;
    }

    public void deleteSections(int serial_number) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            int i = database.delete(TABLE_SECTION, SectionTable.SERIAL_NUMBER + ">=" + serial_number, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
    }

    public void deleteAllSection() {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            int i = database.delete(TABLE_SECTION, SectionTable.SERIAL_NUMBER + ">=" + 1, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
    }

    public ArrayList<Section> getSectionList() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        ArrayList<Section> sectionList = new ArrayList<>();

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(TABLE_SECTION, null, null, null, null, null, null);
            Section section = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                section = new Section();
                loadSectionFromDB(section, cursor);
                sectionList.add(section);
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
        return sectionList;
    }

    private void loadSectionFromDB(Section section, Cursor cursor) {
        section.id = cursor.getString(SectionTable.ID_INDEX);
        section.name = cursor.getString(SectionTable.NAME_INDEX);
        section.status = cursor.getString(SectionTable.STATUS_INDEX);
        section.content = cursor.getString(SectionTable.CONTENT_INDEX);
        section.word_count = cursor.getLong(SectionTable.WORD_COUNT_INDEX);
        section.update_time = cursor.getLong(SectionTable.UPDATE_TIME_INDEX);
        section.check_status = cursor.getString(SectionTable.CHECK_STATUS_INDEX);
        section.check_message = cursor.getString(SectionTable.CHECK_MESSAGE_INDEX);
        section.serial_number = cursor.getInt(SectionTable.SERIAL_NUMBER_INDEX);
    }



    private class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context paramContext) {
            super(paramContext, DATABASE_NAME, null, version);
        }

        public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
            paramSQLiteDatabase.execSQL(SQL_CREATE_SECTION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int old_version, int new_version) {

        }
    }
}