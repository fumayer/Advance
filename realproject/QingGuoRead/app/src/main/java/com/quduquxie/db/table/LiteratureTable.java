package com.quduquxie.db.table;

import java.io.Serializable;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class LiteratureTable implements Serializable {

    public static String TABLE_NAME = "literature";

    public static String _ID = "_id";
    public static String ID = "id";
    public static String NAME = "name";
    public static String DESCRIPTION = "description";
    public static String ATTRIBUTE = "attribute";
    public static String CATEGORY = "category";
    public static String STYLE = "style";
    public static String ENDING = "ending";
    public static String FENPIN = "fenpin";
    public static String IMAGE_URL = "image_url";
    public static String WORD_COUNT = "word_count";
    public static String IS_SIGN = "is_sign";
    public static String STATUS = "status";
    public static String SERIAL_NUMBER = "serial_number";


    public static int ID_INDEX = 1;
    public static int NAME_INDEX = 2;
    public static int DESCRIPTION_INDEX = 3;
    public static int ATTRIBUTE_INDEX = 4;
    public static int CATEGORY_INDEX = 5;
    public static int STYLE_INDEX = 6;
    public static int ENDING_INDEX = 7;
    public static int FENPIN_INDEX = 8;
    public static int IMAGE_URL_INDEX = 9;
    public static int WORD_COUNT_INDEX = 10;
    public static int IS_SIGN_INDEX = 11;
    public static int STATUS_INDEX = 12;
    public static int SERIAL_NUMBER_INDEX = 13;

}
