package com.quduquxie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.quduquxie.application.QuApplication;
import com.quduquxie.model.User;
import com.quduquxie.db.table.UserTable;
import com.quduquxie.util.QGLog;

import java.util.ArrayList;

/**
 * Created on 16/12/4.
 * Created by crazylei.
 */

public class UserDao {

    private static final String TAG = UserDao.class.getSimpleName();

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 3;

    private static UserDao userDao;
    private SQLiteHelper helper = null;

    private static final String SQL_CREATE_USER = "create table IF NOT EXISTS " + UserTable.TABLE_NAME + "("
            + UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UserTable.ID + " VARCHAR(100) , "
            + UserTable.TOKEN + " VARCHAR(500) , "
            + UserTable.PENNAME + " VARCHAR(100) , "
            + UserTable.PLATFORM + " VARCHAR(40) , "
            + UserTable.AVATAR_URL + " VARCHAR(250) , "
            + UserTable.REGISTER_TIME + " long , "
            + UserTable.USER_NAME + " VARCHAR(50) , "
            + UserTable.IS_SIGN + " INTEGER default -1 , "
            + UserTable.STATUS + " VARCHAR(50) , "
            + UserTable.IS_NEW + " INTEGER default -1 , "
            + UserTable.IS_UPLOADED + " INTEGER default -1 , "
            + UserTable.QQ + " VARCHAR(100) "
            + ");";

    private UserDao(Context context) {
        this.helper = SQLiteHelper.getInstance(context);
    }

    public synchronized static UserDao getInstance(Context context) {
        if (userDao == null) {
            userDao = new UserDao(context);
        }
        return userDao;
    }

    public boolean isContainsUser(String token) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            database = helper.getReadableDatabase();

            cursor = database.query(UserTable.TABLE_NAME, null, UserTable.TOKEN + " = " + "'" + token + "'", null, null, null, null);

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

    public boolean insertUser(User user) {
        SQLiteDatabase database = null;
        long result = -1;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserTable.ID, user.id);
            contentValues.put(UserTable.TOKEN, user.token);
            contentValues.put(UserTable.PENNAME, user.penname);
            contentValues.put(UserTable.PLATFORM, user.platform);
            contentValues.put(UserTable.AVATAR_URL, user.avatar_url);
            contentValues.put(UserTable.REGISTER_TIME, user.register_time);
            contentValues.put(UserTable.USER_NAME, user.user_name);
            contentValues.put(UserTable.IS_SIGN, user.is_sign);
            contentValues.put(UserTable.STATUS, user.status);
            contentValues.put(UserTable.IS_NEW, user.is_new);
            contentValues.put(UserTable.IS_UPLOADED, user.is_uploaded);
            contentValues.put(UserTable.QQ, user.qq);

            result = database.insert(UserTable.TABLE_NAME, null, contentValues);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        QGLog.e(TAG, "InsertUser: " + (result != -1));
        return result != -1;
    }

    public boolean updateUser(User user, String token) {
        SQLiteDatabase database = null;
        long result = 0;
        try {
            database = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (!TextUtils.isEmpty(user.id)) {
                contentValues.put(UserTable.ID, user.id);
            }
            if (!TextUtils.isEmpty(user.token)) {
                contentValues.put(UserTable.TOKEN, user.token);
            }
            if (!TextUtils.isEmpty(user.penname)) {
                contentValues.put(UserTable.PENNAME, user.penname);
            }
            if (!TextUtils.isEmpty(user.platform)) {
                contentValues.put(UserTable.PLATFORM, user.platform);
            }
            if (!TextUtils.isEmpty(user.avatar_url)) {
                contentValues.put(UserTable.AVATAR_URL, user.avatar_url);
            }
            if (user.register_time != 0) {
                contentValues.put(UserTable.REGISTER_TIME, user.register_time);
            }
            if (!TextUtils.isEmpty(user.user_name)) {
                contentValues.put(UserTable.USER_NAME, user.user_name);
            }
            if (user.is_sign != -1) {
                contentValues.put(UserTable.IS_SIGN, user.is_sign);
            }
            if (!TextUtils.isEmpty(user.status)) {
                contentValues.put(UserTable.STATUS, user.status);
            }
            if (user.is_new != -1) {
                contentValues.put(UserTable.IS_NEW, user.is_new);
            }
            if (user.is_uploaded != -1) {
                contentValues.put(UserTable.IS_UPLOADED, user.is_uploaded);
            }
            if (!TextUtils.isEmpty(user.qq)) {
                contentValues.put(UserTable.QQ, user.qq);
            }
            result = database.update(UserTable.TABLE_NAME, contentValues, UserTable.TOKEN + " =? ", new String[]{token});

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }

        QGLog.e(TAG, "updateUser: " + (result != 0));

        return result != 0;
    }

    public User getUser() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        User user = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(UserTable.TABLE_NAME, null, null, null, null, null, UserTable.REGISTER_TIME + " desc");
            if (cursor.moveToFirst()) {
                user = new User();
                loadUserFromDB(user, cursor);
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
        return user;
    }

    public ArrayList<User> getUsers() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        User user = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(UserTable.TABLE_NAME, null, null, null, null, null, UserTable.REGISTER_TIME + " desc");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                user = new User();
                loadUserFromDB(user, cursor);
                users.add(user);
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
        return users;
    }

    public void deleteUser() {
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            database.delete(UserTable.TABLE_NAME, null, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    private void loadUserFromDB(User user, Cursor cursor) {
        user.id = cursor.getString(UserTable.ID_INDEX);
        user.token = cursor.getString(UserTable.TOKEN_INDEX);
        user.penname = cursor.getString(UserTable.PENNAME_INDEX);
        user.platform = cursor.getString(UserTable.PLATFORM_INDEX);
        user.avatar_url = cursor.getString(UserTable.AVATAR_URL_INDEX);
        user.register_time = cursor.getLong(UserTable.REGISTER_TIME_INDEX);
        user.user_name = cursor.getString(UserTable.USER_NAME_INDEX);
        user.is_sign = cursor.getInt(UserTable.IS_SIGN_INDEX);
        user.status = cursor.getString(UserTable.STATUS_INDEX);
        user.is_new = cursor.getInt(UserTable.IS_NEW_INDEX);
        user.is_uploaded = cursor.getInt(UserTable.IS_UPLOADED_INDEX);
        user.qq = cursor.getString(UserTable.QQ_INDEX);
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
            database.execSQL(SQL_CREATE_USER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int old_version, int new_version) {
            String new_column = null;
            if (old_version < 2) {
                new_column = "alter table " + UserTable.TABLE_NAME + " add " + UserTable.IS_UPLOADED + " INTEGER default -1 ";
                database.execSQL(new_column);
            }

            if (old_version < 3) {
                new_column = "alter table " + UserTable.TABLE_NAME + " add " + UserTable.QQ + " VARCHAR(50) ";
                database.execSQL(new_column);
            }
        }
    }

    private static String token;

    public static synchronized String getToken(Context context) {
        if (TextUtils.isEmpty(token)) {
            if (userDao == null) {
                userDao = getInstance(context);
                User user = userDao.getUser();
                if (user != null) {
                    token = user.token;
                }
            }
        }
        return token;
    }

    public static synchronized boolean checkUserLogin() {
        return UserDao.getInstance(QuApplication.getInstance()).getUser() != null;
    }

    public static void setToken(String token) {
        UserDao.token = token;
    }
}
