package com.quduquxie.base.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.database.table.ChapterTableV2;
import com.quduquxie.db.table.ChapterTableV1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class ChapterDao {

    private ChapterDao.SQLiteHelper helper = null;

    private String DATABASE_NAME;
    private static final int version = 4;

    private static final String TAB_CHAPTER = "chapter";
    private static final String TAB_CHAPTER_V2 = "chapter_v2";

    private static final String SQL_CREATE_CHAPTER_V2 = "" +
            "create table IF NOT EXISTS " + TAB_CHAPTER_V2 + "( " +
            ChapterTableV2.KEY + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            ChapterTableV2.ID + " VARCHAR(100) UNIQUE , " +
            ChapterTableV2.NAME + " VARCHAR(250) , " +
            ChapterTableV2.STATUS + " VARCHAR(50) , " +
            ChapterTableV2.CONTENT + " TEXT , " +
            ChapterTableV2.SN + " INTEGER , " +
            ChapterTableV2.INDEX_START + " INTEGER default 0 , " +
            ChapterTableV2.SEQUENCE + " INTEGER , " +
            ChapterTableV2.CREATE_TIME + " long , " +
            ChapterTableV2.WORD_COUNT + " long "
            + ")";


    public ChapterDao(Context context, String id_book) {

        DATABASE_NAME = "book_chapter_" + id_book;

        this.helper = new ChapterDao.SQLiteHelper(context);
    }

    /**
     * 获取章节数目
     */
    public int loadChapterCount() {
        Cursor cursor = null;
        SQLiteDatabase database = null;

        int count = 0;

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(TAB_CHAPTER_V2, null, "sn=(select max(sn) from chapter_v2)", null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(ChapterTableV2.SN_INDEX);
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

    public boolean insertBookChapters(ArrayList<Chapter> chapterList) {
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            if (chapterList != null && chapterList.size() != 0) {

                database = helper.getWritableDatabase();

                database.beginTransaction();

                int count = 0;

                cursor = database.rawQuery("select count(_key) from " + TAB_CHAPTER_V2, null);

                boolean state = cursor.moveToNext();

                if (state) {
                    count = cursor.getInt(0);
                }

                ContentValues contentValues;
                for (Chapter chapter : chapterList) {
                    contentValues = new ContentValues();
                    contentValues.put(ChapterTableV2.ID, chapter.id);
                    contentValues.put(ChapterTableV2.NAME, chapter.name);
                    contentValues.put(ChapterTableV2.STATUS, chapter.status);
                    contentValues.put(ChapterTableV2.CONTENT, chapter.content);
                    contentValues.put(ChapterTableV2.SN, chapter.sn);
                    contentValues.put(ChapterTableV2.INDEX_START, chapter.index_start);
                    contentValues.put(ChapterTableV2.SEQUENCE, count);
                    contentValues.put(ChapterTableV2.CREATE_TIME, chapter.create_time);
                    contentValues.put(ChapterTableV2.WORD_COUNT, chapter.word_count);

                    chapter.sequence = count;

                    database.insert(TAB_CHAPTER_V2, null, contentValues);
                    count++;
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

    public boolean loadChapter(String id) {
        Cursor cursor = null;
        SQLiteDatabase database = null;

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(TAB_CHAPTER_V2, null, "id='" + id + "'", null, null, null, null, null);

            if (cursor.moveToFirst()) {
                return true;
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
        return false;
    }

    public ArrayList<Chapter> loadChapters() {
        ArrayList<Chapter> chapters = new ArrayList<>();

        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(TAB_CHAPTER_V2, null, null, null, null, null, null, null);
            Chapter chapter;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                chapter = new Chapter();
                initChapter(chapter, cursor);
                chapters.add(chapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        if (chapters.size() > 0) {
            Collections.sort(chapters, new Comparator<Chapter>() {
                @Override
                public int compare(Chapter firstChapter, Chapter secondChapter) {
                    return firstChapter.sn - secondChapter.sn;
                }
            });
        }
        return chapters;
    }

    private void initChapter(Chapter chapter, Cursor cursor) {
        chapter.id = cursor.getString(ChapterTableV2.ID_INDEX);
        chapter.name = cursor.getString(ChapterTableV2.NAME_INDEX);
        chapter.status = cursor.getString(ChapterTableV2.STATUS_INDEX);
        chapter.content = cursor.getString(ChapterTableV2.CONTENT_INDEX);
        chapter.sn = cursor.getInt(ChapterTableV2.SN_INDEX);
        chapter.index_start = cursor.getInt(ChapterTableV2.INDEX_START_INDEX);
        chapter.sequence = cursor.getInt(ChapterTableV2.SEQUENCE_INDEX);
        chapter.create_time = cursor.getLong(ChapterTableV2.CREATE_TIME_INDEX);
        chapter.word_count = cursor.getLong(ChapterTableV2.WORD_COUNT_INDEX);
    }

    public void deleteChapter(int sequence) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(TAB_CHAPTER_V2, ChapterTableV2.SEQUENCE + ">=" + sequence, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public boolean updateChapter(Chapter chapter) {
        long result = 0;

        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            if (!TextUtils.isEmpty(chapter.id)) {

                contentValues.put(ChapterTableV2.ID, chapter.id);

                if (!TextUtils.isEmpty(chapter.name)) {
                    contentValues.put(ChapterTableV2.NAME, chapter.name);
                }

                if (!TextUtils.isEmpty(chapter.status)) {
                    contentValues.put(ChapterTableV2.STATUS, chapter.status);
                }

                if (!TextUtils.isEmpty(chapter.content)) {
                    contentValues.put(ChapterTableV2.CONTENT, chapter.content);
                }

                if (chapter.sn != 0) {
                    contentValues.put(ChapterTableV2.SN, chapter.sn);
                }

                if (chapter.index_start != -1) {
                    contentValues.put(ChapterTableV2.INDEX_START, chapter.index_start);
                }

                if (chapter.create_time != 0) {
                    contentValues.put(ChapterTableV2.CREATE_TIME, chapter.create_time);
                }

                if (chapter.word_count != 0) {
                    contentValues.put(ChapterTableV2.WORD_COUNT, chapter.word_count);
                }

                result = database.update(TAB_CHAPTER_V2, contentValues, ChapterTableV2.ID + " =? ", new String[]{chapter.id});
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return result != 0;
    }

    private class SQLiteHelper extends SQLiteOpenHelper {

        SQLiteHelper(Context paramContext) {
            super(paramContext, DATABASE_NAME, null, version);
        }

        public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
            paramSQLiteDatabase.execSQL(SQL_CREATE_CHAPTER_V2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String new_column;

            if (oldVersion < 2) {
                new_column = "alter table " + TAB_CHAPTER + " add " + ChapterTableV1.INDEX_START + " INTEGER";
                db.execSQL(new_column);
            }

            if (oldVersion < 3) {
                new_column = "alter table " + TAB_CHAPTER + " add " + ChapterTableV1.URL + " VARCHAR(500)";
                db.execSQL(new_column);

                new_column = "alter table " + TAB_CHAPTER + " add " + ChapterTableV1.HOST + " VARCHAR(200)";
                db.execSQL(new_column);

                new_column = "alter table " + TAB_CHAPTER + " add " + ChapterTableV1.IS_VIP + " INTEGER default 0";
                db.execSQL(new_column);
            }

            if (oldVersion < 4) {

                String TEMP_TABLE_NAME = TAB_CHAPTER + "_Temp";

                String copy = "create table if not exists " + TEMP_TABLE_NAME + " as select * from " + TAB_CHAPTER;
                db.execSQL(copy);

                String drop = "drop table if exists " + TAB_CHAPTER;
                db.execSQL(drop);

                db.execSQL(SQL_CREATE_CHAPTER_V2);

                String restore = "insert into " + TAB_CHAPTER_V2 + " select " + ChapterTableV1.ID + " , " + ChapterTableV1.ID_CHAPTER + " , " + ChapterTableV1.NAME + " , " + ChapterTableV1.STATUS_CHAPTER + " , " + "'null'" + " , " + ChapterTableV1.SERIAL_NUMBER + " , " + ChapterTableV1.INDEX_START + " , " + ChapterTableV1.SEQUENCE + " , " + ChapterTableV1.CREATE_TIME + " , " + ChapterTableV1.WORD_COUNT + " from " + TEMP_TABLE_NAME;

                db.execSQL(restore);

                String drop_temp = "DROP TABLE IF EXISTS " + TEMP_TABLE_NAME;

                db.execSQL(drop_temp);
            }
        }
    }
}