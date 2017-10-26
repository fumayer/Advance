package com.quduquxie.base.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.bean.User;
import com.quduquxie.base.database.table.BookTableV2;
import com.quduquxie.base.database.table.BookmarkTable;
import com.quduquxie.db.table.BookTableV1;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentUser;

import java.util.ArrayList;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class BookDao {

    private static final int version = 8;
    private static final String DATABASE_NAME = "novel.db";

    private static BookDao bookDao;

    private SQLiteHelper helper = null;

    private static final String SQL_CREATE_BOOK = "create table IF NOT EXISTS " + BookTableV2.TABLE_NAME + "("
            + BookTableV2._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BookTableV2.ID + " VARCHAR(100) , "
            + BookTableV2.NAME + " VARCHAR(300) , "
            + BookTableV2.STYLE + " VARCHAR(100) , "
            + BookTableV2.IMAGE + " VARCHAR(300) , "
            + BookTableV2.STATUS + " VARCHAR(100) , "
            + BookTableV2.ENDING + " VARCHAR(100) , "
            + BookTableV2.CHANNEL + " VARCHAR(100) , "
            + BookTableV2.CATEGORY + " VARCHAR(100) , "
            + BookTableV2.ATTRIBUTE + " VARCHAR(100) , "
            + BookTableV2.DESCRIPTION + " VARCHAR(2000) , "
            + BookTableV2.AUTHOR_TALK + " VARCHAR(2000) , "
            + BookTableV2.BANNER_IMAGE + " VARCHAR(2000) , "

            + BookTableV2.IS_SIGN + " INTEGER , "
            + BookTableV2.UPDATE_COUNT + " INTEGER , "
            + BookTableV2.IS_COPYRIGHT + " INTEGER default 0 , "

            + BookTableV2.WORD_COUNT + " long , "
            + BookTableV2.READ_COUNT + " long , "
            + BookTableV2.CLICK_COUNT + " long , "
            + BookTableV2.FOLLOW_COUNT + " long , "

            + BookTableV2.READ + " INTEGER default 0 , "
            + BookTableV2.OFFSET + " INTEGER , "
            + BookTableV2.SEQUENCE + " INTEGER default -1 , "
            + BookTableV2.FLUSH_COUNT + " INTEGER default 0 , "

            + BookTableV2.INSERT_TIME + " long , "
            + BookTableV2.UPDATE_TIME + " long , "
            + BookTableV2.SEQUENCE_TIME + " long , "

            + BookTableV2.UPDATE_STATUS + " INTEGER default 0 , "
            + BookTableV2.CHAPTERS_UPDATE_STATE + " INTEGER default 2 , "
            + BookTableV2.CHAPTERS_UPDATE_INDEX + " INTEGER default 0 , "

            + BookTableV2.BOOK_TYPE + " INTEGER default 0 , "
            + BookTableV2.BOOK_SIZE + " long , "
            + BookTableV2.FILE_PATH + " VARCHAR(500) , "

            + BookTableV2.AUTHOR_ID + " VARCHAR(100) , "
            + BookTableV2.AUTHOR_NAME + " VARCHAR(300) , "
            + BookTableV2.AUTHOR_AVATAR + " VARCHAR(300) , "
            + BookTableV2.AUTHOR_IS_SIGN + " INTEGER , "

            + BookTableV2.GOD_AUTHOR_ID + " VARCHAR(100) , "
            + BookTableV2.GOD_AUTHOR_NAME + " VARCHAR(300) , "
            + BookTableV2.GOD_AUTHOR_AVATAR + " VARCHAR(300) , "
            + BookTableV2.GOD_AUTHOR_IS_SIGN + " INTEGER , "

            + BookTableV2.CHAPTER_ID + " VARCHAR(100) , "
            + BookTableV2.CHAPTER_SN + " INTEGER , "
            + BookTableV2.CHAPTER_NAME + " VARCHAR(300) , "
            + BookTableV2.CHAPTER_STATUS + " VARCHAR(100) , "
            + BookTableV2.CHAPTER_CONTENT + " text , "
            + BookTableV2.CHAPTER_WORD_COUNT + " long , "
            + BookTableV2.CHAPTER_CREATE_TIME + " long , "

            + BookTableV2.COMMENT_ID + " VARCHAR(100) , "
            + BookTableV2.COMMENT_SN + " INTEGER , "
            + BookTableV2.COMMENT_STATUS + " INTEGER , "
            + BookTableV2.COMMENT_CONTENT + " VARCHAR(2000) , "
            + BookTableV2.COMMENT_IS_AUTHOR + " INTEGER , "
            + BookTableV2.COMMENT_LIKE_COUNT + " INTEGER default 0 , "
            + BookTableV2.COMMENT_CREATE_TIME + " long , "

            + BookTableV2.COMMENT_SENDER_ID + " VARCHAR(100) , "
            + BookTableV2.COMMENT_SENDER_NAME + " VARCHAR(300) , "
            + BookTableV2.COMMENT_SENDER_IS_SIGN + " INTEGER , "
            + BookTableV2.COMMENT_SENDER_AVATAR_URL + " VARCHAR(300) "
            + ");";

    private static final String SQL_CREATE_BOOK_MARK = "create table if not exists " + BookmarkTable.TABLE_NAME + "( "
            + BookmarkTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BookmarkTable.BOOK_ID + " VARCHAR(40), "
            + BookmarkTable.CHAPTER_NAME + " VARCHAR(250) , "
            + BookmarkTable.CHAPTER_CONTENT + " VARCHAR , "
            + BookmarkTable.SEQUENCE + " INTEGER DEFAULT -1, "
            + BookmarkTable.OFFSET + " INTEGER , "
            + BookmarkTable.INSERT_TIME + " long )";

    private BookDao(Context context) {
        this.helper = BookDao.SQLiteHelper.getInstance(context);
    }

    public synchronized static BookDao getInstance(Context context) {
        if (bookDao == null) {
            bookDao = new BookDao(context);
        }
        return bookDao;
    }

    public ArrayList<Bookmark> loadBookmarks(String id) {

        ArrayList<Bookmark> bookmarks = new ArrayList<>();

        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(BookmarkTable.TABLE_NAME, null, BookmarkTable.BOOK_ID + " =? ", new String[]{String.valueOf(id)}, null, null, BookmarkTable.SEQUENCE + " desc");

            if (cursor.moveToFirst()) {
                do {
                    Bookmark bookmark = new Bookmark();
                    bookmark.id = cursor.getInt(cursor.getColumnIndex(BookmarkTable.ID));
                    bookmark.book_id = id;
                    bookmark.chapter_name = cursor.getString(cursor.getColumnIndex(BookmarkTable.CHAPTER_NAME));
                    bookmark.chapter_content = cursor.getString(cursor.getColumnIndex(BookmarkTable.CHAPTER_CONTENT));
                    bookmark.offset = cursor.getInt(cursor.getColumnIndex(BookmarkTable.OFFSET));
                    bookmark.sequence = cursor.getInt(cursor.getColumnIndex(BookmarkTable.SEQUENCE));
                    bookmark.insert_time = cursor.getLong(cursor.getColumnIndex(BookmarkTable.INSERT_TIME));
                    bookmarks.add(bookmark);

                } while (cursor.moveToNext());
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

        return bookmarks;
    }

    public boolean checkBookmarkExist(String id, int sequence, int offset) {
        boolean exist = false;

        Cursor cursor = null;
        SQLiteDatabase database = null;

        try {
            database = helper.getReadableDatabase();
            cursor = database.query(BookmarkTable.TABLE_NAME, null, BookmarkTable.BOOK_ID + " = " + "'" + id + "'" + " and " + BookmarkTable.SEQUENCE + " = " + sequence + " and " + BookmarkTable.OFFSET + " = " + offset, null, null, null, null);
            exist = cursor.moveToFirst();
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

        return exist;
    }

    public void insertBookmark(Bookmark bookmark) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(BookmarkTable.BOOK_ID, bookmark.book_id);
            contentValues.put(BookmarkTable.CHAPTER_NAME, bookmark.chapter_name);
            contentValues.put(BookmarkTable.CHAPTER_CONTENT, bookmark.chapter_content);
            contentValues.put(BookmarkTable.OFFSET, bookmark.offset);
            contentValues.put(BookmarkTable.SEQUENCE, bookmark.sequence);
            contentValues.put(BookmarkTable.INSERT_TIME, bookmark.insert_time);

            database.insert(BookmarkTable.TABLE_NAME, null, contentValues);

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }


    public void deleteBookmark(String id, int sequence, int offset) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(BookmarkTable.TABLE_NAME, BookmarkTable.BOOK_ID + " = " + "'" + id + "'" + " and " + BookmarkTable.SEQUENCE + " = " + sequence + " and " + BookmarkTable.OFFSET + " = " + offset, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public void deleteBookmarks(String id) {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(BookmarkTable.TABLE_NAME, BookmarkTable.BOOK_ID + " = " + "'" + id + "'", null);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public boolean insertBook(Book book) {
        SQLiteDatabase database = null;
        long result = -1;

        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookTableV2.ID, book.id);
            contentValues.put(BookTableV2.NAME, book.name);
            contentValues.put(BookTableV2.STYLE, book.style);
            contentValues.put(BookTableV2.IMAGE, book.image);
            contentValues.put(BookTableV2.STATUS, book.status);
            contentValues.put(BookTableV2.ENDING, book.ending);
            contentValues.put(BookTableV2.CHANNEL, book.channel);
            contentValues.put(BookTableV2.CATEGORY, book.category);
            contentValues.put(BookTableV2.ATTRIBUTE, book.attribute);
            contentValues.put(BookTableV2.DESCRIPTION, book.description);
            contentValues.put(BookTableV2.AUTHOR_TALK, book.authorTalk);
            contentValues.put(BookTableV2.BANNER_IMAGE, book.bannerImage);

            contentValues.put(BookTableV2.IS_SIGN, book.is_sign);
            contentValues.put(BookTableV2.UPDATE_COUNT, book.update_count);
            contentValues.put(BookTableV2.IS_COPYRIGHT, book.is_copyright);

            contentValues.put(BookTableV2.WORD_COUNT, book.word_count);
            contentValues.put(BookTableV2.READ_COUNT, book.read_count);
            contentValues.put(BookTableV2.CLICK_COUNT, book.click_count);
            contentValues.put(BookTableV2.FOLLOW_COUNT, book.follow_count);

            contentValues.put(BookTableV2.READ, book.read);
            contentValues.put(BookTableV2.OFFSET, book.offset);
            contentValues.put(BookTableV2.SEQUENCE, book.sequence);
            contentValues.put(BookTableV2.FLUSH_COUNT, book.flush_count);

            contentValues.put(BookTableV2.INSERT_TIME, System.currentTimeMillis());
            contentValues.put(BookTableV2.UPDATE_TIME, book.update_time);
            contentValues.put(BookTableV2.SEQUENCE_TIME, book.sequence_time);

            contentValues.put(BookTableV2.UPDATE_STATUS, book.update_status);
            contentValues.put(BookTableV2.CHAPTERS_UPDATE_STATE, book.chapters_update_state);
            contentValues.put(BookTableV2.CHAPTERS_UPDATE_INDEX, book.chapters_update_index);

            contentValues.put(BookTableV2.BOOK_TYPE, book.book_type);
            contentValues.put(BookTableV2.BOOK_SIZE, book.book_size);
            contentValues.put(BookTableV2.FILE_PATH, book.file_path);

            if (book.author != null && !TextUtils.isEmpty(book.author.id)) {
                contentValues.put(BookTableV2.AUTHOR_ID, book.author.id);
                contentValues.put(BookTableV2.AUTHOR_NAME, book.author.name);
                contentValues.put(BookTableV2.AUTHOR_AVATAR, book.author.avatar);
                contentValues.put(BookTableV2.AUTHOR_IS_SIGN, book.author.is_sign);
            }

            if (book.bigGodUser != null && !TextUtils.isEmpty(book.bigGodUser.id)) {
                contentValues.put(BookTableV2.GOD_AUTHOR_ID, book.bigGodUser.id);
                contentValues.put(BookTableV2.GOD_AUTHOR_NAME, book.bigGodUser.name);
                contentValues.put(BookTableV2.GOD_AUTHOR_AVATAR, book.bigGodUser.avatar);
                contentValues.put(BookTableV2.GOD_AUTHOR_IS_SIGN, book.bigGodUser.is_sign);
            }

            if (book.chapter != null && !TextUtils.isEmpty(book.chapter.id)) {
                contentValues.put(BookTableV2.CHAPTER_ID, book.chapter.id);
                contentValues.put(BookTableV2.CHAPTER_SN, book.chapter.sn);
                contentValues.put(BookTableV2.CHAPTER_NAME, book.chapter.name);
                contentValues.put(BookTableV2.CHAPTER_STATUS, book.chapter.status);
                contentValues.put(BookTableV2.CHAPTER_CONTENT, book.chapter.content);
                contentValues.put(BookTableV2.CHAPTER_WORD_COUNT, book.chapter.word_count);
                contentValues.put(BookTableV2.CHAPTER_CREATE_TIME, book.chapter.create_time);
            }

            if (book.hotComment != null && !TextUtils.isEmpty(book.hotComment.id)) {
                contentValues.put(BookTableV2.COMMENT_ID, book.hotComment.id);
                contentValues.put(BookTableV2.COMMENT_SN, book.hotComment.sn);
                contentValues.put(BookTableV2.COMMENT_STATUS, book.hotComment.status);
                contentValues.put(BookTableV2.COMMENT_CONTENT, book.hotComment.content);
                contentValues.put(BookTableV2.COMMENT_IS_AUTHOR, book.hotComment.is_author);
                contentValues.put(BookTableV2.COMMENT_LIKE_COUNT, book.hotComment.like_count);
                contentValues.put(BookTableV2.COMMENT_CREATE_TIME, book.hotComment.create_time);

                if (book.hotComment.sender != null && !TextUtils.isEmpty(book.hotComment.sender.id)) {
                    contentValues.put(BookTableV2.COMMENT_SENDER_ID, book.hotComment.sender.id);
                    contentValues.put(BookTableV2.COMMENT_SENDER_NAME, book.hotComment.sender.name);
                    contentValues.put(BookTableV2.COMMENT_SENDER_IS_SIGN, book.hotComment.sender.is_sign);
                    contentValues.put(BookTableV2.COMMENT_SENDER_AVATAR_URL, book.hotComment.sender.avatar_url);
                }
            }

            result = database.insert(BookTableV2.TABLE_NAME, null, contentValues);

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return result != -1;
    }

    public boolean updateBook(Book book) {
        if (TextUtils.isEmpty(book.id)) {
            return false;
        }

        SQLiteDatabase database = null;
        long result = 0;

        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            if (!TextUtils.isEmpty(book.id)) {
                contentValues.put(BookTableV2.ID, book.id);
            }

            if (!TextUtils.isEmpty(book.name)) {
                contentValues.put(BookTableV2.NAME, book.name);
            }

            if (!TextUtils.isEmpty(book.style)) {
                contentValues.put(BookTableV2.STYLE, book.style);
            }

            if (!TextUtils.isEmpty(book.image)) {
                contentValues.put(BookTableV2.IMAGE, book.image);
            }

            if (!TextUtils.isEmpty(book.status)) {
                contentValues.put(BookTableV2.STATUS, book.status);
            }

            if (!TextUtils.isEmpty(book.ending)) {
                contentValues.put(BookTableV2.ENDING, book.ending);
            }

            if (!TextUtils.isEmpty(book.channel)) {
                contentValues.put(BookTableV2.CHANNEL, book.channel);
            }

            if (!TextUtils.isEmpty(book.category)) {
                contentValues.put(BookTableV2.CATEGORY, book.category);
            }

            if (!TextUtils.isEmpty(book.attribute)) {
                contentValues.put(BookTableV2.ATTRIBUTE, book.attribute);
            }

            if (!TextUtils.isEmpty(book.description)) {
                contentValues.put(BookTableV2.DESCRIPTION, book.description);
            }

            if (!TextUtils.isEmpty(book.authorTalk)) {
                contentValues.put(BookTableV2.AUTHOR_TALK, book.authorTalk);
            }

            if (!TextUtils.isEmpty(book.bannerImage)) {
                contentValues.put(BookTableV2.BANNER_IMAGE, book.bannerImage);
            }




            if (book.is_sign != -1) {
                contentValues.put(BookTableV2.IS_SIGN, book.is_sign);
            }

            if (book.update_count != -1) {
                contentValues.put(BookTableV2.UPDATE_COUNT, book.update_count);
            }

            if (book.is_copyright != -1) {
                contentValues.put(BookTableV2.IS_COPYRIGHT, book.is_copyright);
            }




            if (book.word_count != 0) {
                contentValues.put(BookTableV2.WORD_COUNT, book.word_count);
            }

            if (book.read_count != 0) {
                contentValues.put(BookTableV2.READ_COUNT, book.read_count);
            }

            if (book.click_count != 0) {
                contentValues.put(BookTableV2.CLICK_COUNT, book.click_count);
            }

            if (book.follow_count != 0) {
                contentValues.put(BookTableV2.FOLLOW_COUNT, book.follow_count);
            }




            if (book.read != 0) {
                contentValues.put(BookTableV2.READ, book.read);
            }

            if (book.offset != -1) {
                contentValues.put(BookTableV2.OFFSET, book.offset);
            }

            if (book.sequence >= -1) {
                contentValues.put(BookTableV2.SEQUENCE, book.sequence);
            }

            if (book.flush_count > 0) {
                contentValues.put(BookTableV2.FLUSH_COUNT, book.flush_count);
            }




            if (book.insert_time != 0) {
                contentValues.put(BookTableV2.INSERT_TIME, book.insert_time);
            }

            if (book.update_time != 0) {
                contentValues.put(BookTableV2.UPDATE_TIME, book.update_time);
            }

            if (book.sequence_time != 0) {
                contentValues.put(BookTableV2.SEQUENCE_TIME, book.sequence_time);
            }





            if (book.update_status != -1) {
                contentValues.put(BookTableV2.UPDATE_STATUS, book.update_status);
            }

            if (book.chapters_update_state != 0) {
                contentValues.put(BookTableV2.CHAPTERS_UPDATE_STATE, book.chapters_update_state);
            }

            if (book.chapters_update_index != 0) {
                contentValues.put(BookTableV2.CHAPTERS_UPDATE_INDEX, book.chapters_update_index);
            }




            if (book.book_size != 0) {
                contentValues.put(BookTableV2.BOOK_SIZE, book.book_size);
            }

            if (!TextUtils.isEmpty(book.file_path)) {
                contentValues.put(BookTableV2.FILE_PATH, book.file_path);
            }



            if (book.author != null) {
                if (!TextUtils.isEmpty(book.author.id)) {
                    contentValues.put(BookTableV2.AUTHOR_ID, book.author.id);
                }

                if (!TextUtils.isEmpty(book.author.name)) {
                    contentValues.put(BookTableV2.AUTHOR_NAME, book.author.name);
                }

                if (!TextUtils.isEmpty(book.author.avatar)) {
                    contentValues.put(BookTableV2.AUTHOR_AVATAR, book.author.avatar);
                }

                if (book.author.is_sign != -1) {
                    contentValues.put(BookTableV2.AUTHOR_IS_SIGN, book.author.is_sign);
                }
            }


            if (book.bigGodUser != null) {
                if (!TextUtils.isEmpty(book.bigGodUser.id)) {
                    contentValues.put(BookTableV2.GOD_AUTHOR_ID, book.bigGodUser.id);
                }

                if (!TextUtils.isEmpty(book.bigGodUser.name)) {
                    contentValues.put(BookTableV2.GOD_AUTHOR_NAME, book.bigGodUser.name);
                }

                if (!TextUtils.isEmpty(book.bigGodUser.avatar)) {
                    contentValues.put(BookTableV2.GOD_AUTHOR_AVATAR, book.bigGodUser.avatar);
                }

                if (book.bigGodUser.is_sign != -1) {
                    contentValues.put(BookTableV2.GOD_AUTHOR_IS_SIGN, book.bigGodUser.is_sign);
                }
            }



            if (book.chapter != null) {
                if (!TextUtils.isEmpty(book.chapter.id)) {
                    contentValues.put(BookTableV2.CHAPTER_ID, book.chapter.id);
                }

                if (book.chapter.sn != 0) {
                    contentValues.put(BookTableV2.CHAPTER_SN, book.chapter.sn);
                }

                if (!TextUtils.isEmpty(book.chapter.name)) {
                    contentValues.put(BookTableV2.CHAPTER_NAME, book.chapter.name);
                }

                if (!TextUtils.isEmpty(book.chapter.status)) {
                    contentValues.put(BookTableV2.CHAPTER_STATUS, book.chapter.status);
                }

                if (!TextUtils.isEmpty(book.chapter.content)) {
                    contentValues.put(BookTableV2.CHAPTER_CONTENT, book.chapter.content);
                }

                if (book.chapter.word_count != 0) {
                    contentValues.put(BookTableV2.CHAPTER_WORD_COUNT, book.chapter.word_count);
                }

                if (book.chapter.create_time != 0) {
                    contentValues.put(BookTableV2.CHAPTER_CREATE_TIME, book.chapter.create_time);
                }
            }



            if (book.hotComment != null) {
                if (!TextUtils.isEmpty(book.hotComment.id)) {
                    contentValues.put(BookTableV2.COMMENT_ID, book.hotComment.id);
                }

                if (book.hotComment.sn != 0) {
                    contentValues.put(BookTableV2.COMMENT_SN, book.hotComment.sn);
                }

                if (book.hotComment.status != -1) {
                    contentValues.put(BookTableV2.COMMENT_STATUS, book.hotComment.status);
                }

                if (!TextUtils.isEmpty(book.hotComment.content)) {
                    contentValues.put(BookTableV2.COMMENT_CONTENT, book.hotComment.content);
                }

                if (book.hotComment.is_author != -1) {
                    contentValues.put(BookTableV2.COMMENT_IS_AUTHOR, book.hotComment.is_author);
                }

                if (book.hotComment.like_count != 0) {
                    contentValues.put(BookTableV2.COMMENT_LIKE_COUNT, book.hotComment.like_count);
                }

                if (book.hotComment.create_time != 0) {
                    contentValues.put(BookTableV2.COMMENT_CREATE_TIME, book.hotComment.create_time);
                }

                if (book.hotComment.sender != null) {
                    if (!TextUtils.isEmpty(book.hotComment.sender.id)) {
                        contentValues.put(BookTableV2.COMMENT_SENDER_ID, book.hotComment.sender.id);
                    }

                    if (!TextUtils.isEmpty(book.hotComment.sender.name)) {
                        contentValues.put(BookTableV2.COMMENT_SENDER_NAME, book.hotComment.sender.name);
                    }

                    if (book.hotComment.sender.is_sign != -1) {
                        contentValues.put(BookTableV2.COMMENT_SENDER_IS_SIGN, book.hotComment.sender.is_sign);
                    }

                    if (!TextUtils.isEmpty(book.hotComment.sender.avatar_url)) {
                        contentValues.put(BookTableV2.COMMENT_SENDER_AVATAR_URL, book.hotComment.sender.avatar_url);
                    }
                }
            }

            result = database.update(BookTableV2.TABLE_NAME, contentValues, BookTableV2.ID + " =? ", new String[]{book.id});

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return result != 0;
    }

    public Book loadBook(String id) {
        Cursor cursor = null;
        SQLiteDatabase databased = null;

        try {
            databased = helper.getReadableDatabase();

            cursor = databased.query(BookTableV2.TABLE_NAME, null, BookTableV2.ID + "=?", new String[]{id}, null, null, null);

            if (cursor.moveToNext()) {
                Book book = new Book();
                initBook(book, cursor);
                return book;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
                if (databased != null) {
                    databased.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Book> loadOnlineBooks() {
        Cursor cursor = null;
        SQLiteDatabase database = null;
        ArrayList<Book> books = new ArrayList<>();

        try {
            String where = BookTableV2.BOOK_TYPE + " == 0";

            database = helper.getReadableDatabase();
            cursor = database.query(BookTableV2.TABLE_NAME, null, where, null, null, null, BookTableV2.SEQUENCE_TIME + " desc");
            Book book;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                book = new Book();
                initBook(book, cursor);
                books.add(book);
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
        return books;
    }

    public ArrayList<Book> loadLocalBooks() {
        Cursor cursor = null;
        SQLiteDatabase database = null;
        ArrayList<Book> books = new ArrayList<>();
        try {

            String where = BookTableV2.BOOK_TYPE + " == 1";

            database = helper.getReadableDatabase();
            cursor = database.query(BookTableV2.TABLE_NAME, null, where, null, null, null, BookTableV2.SEQUENCE_TIME + " desc");
            Book book;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                book = new Book();
                initBook(book, cursor);
                books.add(book);
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
        return books;
    }

    public ArrayList<Book> loadReadBooks() {
        Cursor cursor = null;
        SQLiteDatabase database = null;
        ArrayList<Book> list = new ArrayList<>();
        try {

            String where = BookTableV1.READ + " == 1";

            database = helper.getReadableDatabase();
            cursor = database.query(BookTableV2.TABLE_NAME, null, where, null, null, null, BookTableV2.SEQUENCE_TIME + " desc");
            Book book;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                book = new Book();
                initBook(book, cursor);
                list.add(book);
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
        return list;
    }

    public ArrayList<Book> loadAllBooks() {
        Cursor cursor = null;
        SQLiteDatabase database = null;
        ArrayList<Book> list = new ArrayList<>();
        try {

            database = helper.getReadableDatabase();
            cursor = database.query(BookTableV2.TABLE_NAME, null, null, null, null, null, BookTableV2.SEQUENCE_TIME + " desc");
            Book book;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                book = new Book();
                initBook(book, cursor);
                list.add(book);
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
        return list;
    }

    public String[] deleteSubBook(String... ids) {
        String[] deleted_ids = new String[ids.length];
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();

            database.beginTransaction();

            for (int i = 0; i < ids.length; i++) {
                if (database.delete(BookTableV2.TABLE_NAME, BookTableV2.ID + " =? ", new String[]{ids[i]}) > 0) {
                    deleted_ids[i] = ids[i];
                }
            }
            database.setTransactionSuccessful();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
        return deleted_ids;
    }


    private void initBook(Book book, Cursor cursor) {
        book.id = cursor.getString(BookTableV2.ID_INDEX);
        book.name = cursor.getString(BookTableV2.NAME_INDEX);
        book.style = cursor.getString(BookTableV2.STYLE_INDEX);
        book.image = cursor.getString(BookTableV2.IMAGE_INDEX);
        book.status = cursor.getString(BookTableV2.STATUS_INDEX);
        book.ending = cursor.getString(BookTableV2.ENDING_INDEX);
        book.channel = cursor.getString(BookTableV2.CHANNEL_INDEX);
        book.category = cursor.getString(BookTableV2.CATEGORY_INDEX);
        book.attribute = cursor.getString(BookTableV2.ATTRIBUTE_INDEX);
        book.description = cursor.getString(BookTableV2.DESCRIPTION_INDEX);
        book.authorTalk = cursor.getString(BookTableV2.AUTHOR_TALK_INDEX);
        book.bannerImage = cursor.getString(BookTableV2.BANNER_IMAGE_INDEX);


        book.is_sign = cursor.getInt(BookTableV2.IS_SIGN_INDEX);
        book.update_count = cursor.getInt(BookTableV2.UPDATE_COUNT_INDEX);
        book.is_copyright = cursor.getInt(BookTableV2.IS_COPYRIGHT_INDEX);


        book.word_count = cursor.getLong(BookTableV2.WORD_COUNT_INDEX);
        book.read_count = cursor.getLong(BookTableV2.READ_COUNT_INDEX);
        book.click_count = cursor.getLong(BookTableV2.CLICK_COUNT_INDEX);
        book.follow_count = cursor.getLong(BookTableV2.FOLLOW_COUNT_INDEX);


        book.read = cursor.getInt(BookTableV2.READ_INDEX);
        book.offset = cursor.getInt(BookTableV2.OFFSET_INDEX);
        book.sequence = cursor.getInt(BookTableV2.SEQUENCE_INDEX);
        if (book.sequence == -2) {
            book.sequence = -1;
        }
        book.flush_count = cursor.getInt(BookTableV2.FLUSH_COUNT_INDEX);


        book.insert_time = cursor.getLong(BookTableV2.INSERT_TIME_INDEX);
        book.update_time = cursor.getLong(BookTableV2.UPDATE_TIME_INDEX);
        book.sequence_time = cursor.getLong(BookTableV2.SEQUENCE_TIME_INDEX);


        book.update_status = cursor.getInt(BookTableV2.UPDATE_STATUS_INDEX);
        book.chapters_update_state = cursor.getInt(BookTableV2.CHAPTERS_UPDATE_STATE_INDEX);
        book.chapters_update_index = cursor.getInt(BookTableV2.CHAPTERS_UPDATE_INDEX_INDEX);


        book.book_type = cursor.getInt(BookTableV2.BOOK_TYPE_INDEX);
        book.book_size = cursor.getInt(BookTableV2.BOOK_SIZE_INDEX);
        book.file_path = cursor.getString(BookTableV2.FILE_PATH_INDEX);


        String author_id = cursor.getString(BookTableV2.AUTHOR_ID_INDEX);
        if (book.book_type == Book.TYPE_LOCAL_TXT) {
            Logger.e("Book: " + book);
            User user = new User();
            initUser(user, cursor);
            book.author = user;
        } else {
            if (!TextUtils.isEmpty(author_id) && !("null".equals(author_id))) {
                User user = new User();
                initUser(user, cursor);
                book.author = user;
            }
        }

        String god_author_id = cursor.getString(BookTableV2.GOD_AUTHOR_ID_INDEX);
        if (!TextUtils.isEmpty(god_author_id) && !("null".equals(god_author_id))) {
            User user = new User();
            initGodUser(user, cursor);
            book.bigGodUser = user;
        }

        String chapter_id = cursor.getString(BookTableV2.CHAPTER_ID_INDEX);

        if (book.book_type == Book.TYPE_LOCAL_TXT) {
            Logger.e("Book: " + book);
            Chapter chapter = new Chapter();
            initChapter(chapter, cursor);
            Logger.e("Chapter: " + chapter);
            book.chapter = chapter;
        } else {
            if (!TextUtils.isEmpty(chapter_id) && !("null".equals(chapter_id))) {
                Chapter chapter = new Chapter();
                initChapter(chapter, cursor);

                book.chapter = chapter;
            }
        }


        String comment_id = cursor.getString(BookTableV2.COMMENT_ID_INDEX);
        if (!TextUtils.isEmpty(comment_id) && !("null".equals(comment_id))) {
            Comment comment = new Comment();
            initComment(comment, cursor);

            book.hotComment = comment;
        }
    }

    private void initUser(User user, Cursor cursor) {
        user.id = cursor.getString(BookTableV2.AUTHOR_ID_INDEX);
        user.name = cursor.getString(BookTableV2.AUTHOR_NAME_INDEX);
        user.avatar = cursor.getString(BookTableV2.AUTHOR_AVATAR_INDEX);
        user.is_sign = cursor.getInt(BookTableV2.AUTHOR_IS_SIGN_INDEX);
    }

    private void initGodUser(User user, Cursor cursor) {
        user.id = cursor.getString(BookTableV2.GOD_AUTHOR_ID_INDEX);
        user.name = cursor.getString(BookTableV2.GOD_AUTHOR_NAME_INDEX);
        user.avatar = cursor.getString(BookTableV2.GOD_AUTHOR_AVATAR_INDEX);
        user.is_sign = cursor.getInt(BookTableV2.GOD_AUTHOR_IS_SIGN_INDEX);
    }

    private void initChapter(Chapter chapter, Cursor cursor) {
        chapter.id = cursor.getString(BookTableV2.CHAPTER_ID_INDEX);
        chapter.sn = cursor.getInt(BookTableV2.CHAPTER_SN_INDEX);
        chapter.name = cursor.getString(BookTableV2.CHAPTER_NAME_INDEX);
        chapter.status = cursor.getString(BookTableV2.CHAPTER_STATUS_INDEX);
        chapter.content = cursor.getString(BookTableV2.CHAPTER_CONTENT_INDEX);
        chapter.word_count = cursor.getLong(BookTableV2.CHAPTER_WORD_COUNT_INDEX);
        chapter.create_time = cursor.getLong(BookTableV2.CHAPTER_CREATE_TIME_INDEX);
    }

    private void initComment(Comment comment, Cursor cursor) {
        comment.id = cursor.getString(BookTableV2.COMMENT_ID_INDEX);
        comment.sn = cursor.getInt(BookTableV2.COMMENT_SN_INDEX);
        comment.status = cursor.getInt(BookTableV2.COMMENT_STATUS_INDEX);
        comment.content = cursor.getString(BookTableV2.COMMENT_CONTENT_INDEX);
        comment.is_author = cursor.getInt(BookTableV2.COMMENT_IS_AUTHOR_INDEX);
        comment.like_count = cursor.getInt(BookTableV2.COMMENT_LIKE_COUNT_INDEX);
        comment.create_time = cursor.getLong(BookTableV2.COMMENT_CREATE_TIME_INDEX);

        String commentUser_id = cursor.getString(BookTableV2.COMMENT_SENDER_ID_INDEX);

        if (!TextUtils.isEmpty(commentUser_id) && !("null".equals(commentUser_id))) {
            CommentUser commentUser = new CommentUser();
            initCommentUser(commentUser, cursor);

            comment.sender = commentUser;
        }
    }

    private void initCommentUser(CommentUser commentUser, Cursor cursor) {
        commentUser.id = cursor.getString(BookTableV2.COMMENT_SENDER_ID_INDEX);
        commentUser.name = cursor.getString(BookTableV2.COMMENT_SENDER_NAME_INDEX);
        commentUser.is_sign = cursor.getInt(BookTableV2.COMMENT_SENDER_IS_SIGN_INDEX);
        commentUser.avatar_url = cursor.getString(BookTableV2.COMMENT_SENDER_AVATAR_URL_INDEX);
    }


    private static class SQLiteHelper extends SQLiteOpenHelper {

        private static SQLiteHelper helper;

        private SQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, version);
        }

        public synchronized static BookDao.SQLiteHelper getInstance(Context paramContext) {
            if (helper == null) {
                helper = new BookDao.SQLiteHelper(paramContext);
            }
            return helper;
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(SQL_CREATE_BOOK);
            database.execSQL(SQL_CREATE_BOOK_MARK);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

            Logger.e("BookDao update: " + newVersion + " : " + oldVersion);

            String column;
            if (oldVersion < 2) {

                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.BOOK_TYPE + " INTEGER default 0 ";
                database.execSQL(column);

                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.FILE_PATH + " VARCHAR(500) ";
                database.execSQL(column);

                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.BOOK_SIZE + " long ";
                database.execSQL(column);
            }

            if (oldVersion < 3) {
                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.CHAPTERS_UPDATE_INDEX + " INTEGER default 0 ";
                database.execSQL(column);
            }

            if (oldVersion < 4) {
                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.STATUS + " VARCHAR(250) ";
                database.execSQL(column);
            }

            if (oldVersion < 5) {
                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.IS_SIGN + " INTEGER ";
                database.execSQL(column);

                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.TYPE_BOOK + " INTEGER ";
                database.execSQL(column);
            }

            if (oldVersion < 6) {
                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.READ_COUNT + " long ";
                database.execSQL(column);

                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.CLICK_COUNT + " long ";
                database.execSQL(column);

                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.IS_COPYRIGHT + " INTEGER default 0 ";
                database.execSQL(column);

                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.CHANNEL + " VARCHAR(50) ";
                database.execSQL(column);
            }

            if (oldVersion < 7) {
                column = "alter table " + BookTableV1.TABLE_NAME + " add " + BookTableV1.FOLLOW_COUNT + " long ";
                database.execSQL(column);
            }

            if (oldVersion < 8) {
                String TEMP_TABLE_NAME = BookTableV1.TABLE_NAME + "_Temp";

                String copy = "create table if not exists " + TEMP_TABLE_NAME + " as select * from " + BookTableV1.TABLE_NAME;
                database.execSQL(copy);

                String drop = "drop table if exists " + BookTableV1.TABLE_NAME;
                database.execSQL(drop);

                database.execSQL(SQL_CREATE_BOOK);

                String restore = "insert into " + BookTableV2.TABLE_NAME + " select "
                        + BookTableV1._ID + " , " + BookTableV1.ID_BOOK + " , " + BookTableV1.NAME + " , " + BookTableV1.STYLE + " , "
                        + BookTableV1.IMAGE_BOOK + " , " + BookTableV1.STATUS + " , " + BookTableV1.ENDING + " , " + BookTableV1.CHANNEL + " , " + BookTableV1.CATEGORY + " , "
                        + BookTableV1.ATTRIBUTE_BOOK + " , " + BookTableV1.DESCRIPTION + " , " + "''" + " , " + "''" + " , "


                        + BookTableV1.IS_SIGN + " , " + "'0'" + " , " + BookTableV1.IS_COPYRIGHT + " , "


                        + BookTableV1.WORD_COUNT + " , " + BookTableV1.READ_COUNT + " , " + BookTableV1.CLICK_COUNT + " , " + BookTableV1.FOLLOW_COUNT + " , "


                        + BookTableV1.READ + " , " + BookTableV1.OFFSET + " , " + BookTableV1.SEQUENCE + " , " + "'0'" + " , "


                        + BookTableV1.INSERT_TIME + " , " + "'0'" + " , " + BookTableV1.SEQUENCE_TIME + " , "


                        + BookTableV1.UPDATE_STATUS + " , " + BookTableV1.CHAPTERS_NEED_UPDATE + " , " + BookTableV1.CHAPTERS_UPDATE_INDEX + " , "


                        + BookTableV1.BOOK_TYPE + " , " + BookTableV1.BOOK_SIZE + " , " + BookTableV1.FILE_PATH + " , "


                        + BookTableV1.ID_USER + " , " + BookTableV1.PENNAME + " , " + BookTableV1.IMAGE_USER + " , " + "'0'" + " , "


                        + "'null'" + " , " + "'null'" + " , " + "''" + " , " + "'0'" + " , "


                        + BookTableV1.LAST_CHAPTER_ID + " , " + BookTableV1.LAST_CHAPTER_SERIAL_NUMBER + " , " + BookTableV1.ID_LAST_CHAPTER_NAME + " , " + "'null'" + " , " + "'我还在努力码字哦~ 暂无章节更新！'" + " , " + "'0'" + " , " + BookTableV1.ID_LAST_CHAPTER_CREATE_TIME + " , "


                        + "'null'" + " , " + "'0'" + " , " + "'null'" + " , " + "'谢谢支持我的作品，鞠躬~~ 感谢！'" + " , " + "'0'" + " , " + "'0'" + " , " + "'0'" + " , "


                        + "'null'" + " , " + "'青果作家'" + " , " + "'0'" + " , " + "''"


                        + " from " + TEMP_TABLE_NAME;

                database.execSQL(restore);

                String drop_temp = "DROP TABLE IF EXISTS " + TEMP_TABLE_NAME;

                database.execSQL(drop_temp);
            }
        }
    }
}