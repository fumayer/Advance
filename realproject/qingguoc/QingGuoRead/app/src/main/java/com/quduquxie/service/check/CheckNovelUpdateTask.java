package com.quduquxie.service.check;

import com.quduquxie.base.bean.Book;

import java.util.ArrayList;


public class CheckNovelUpdateTask {

    public enum CheckNovelUpdateTaskFrom {

        FROM_SELF(0), FROM_BOOK_SHELF(1);

        private int from;

        public int from() {
            return from;
        }

        CheckNovelUpdateTaskFrom(int from) {
            this.from = from;
        }
    }


    public CheckNovelUpdateTaskFrom from = null;

    public CheckNovelUpdateCallBack checkNovelUpdateCallBack;

    public ArrayList<Book> checkUpdateBooks;

    private int hashCode;

    @Override
    public boolean equals(Object object) {
        if (object instanceof CheckNovelUpdateTask) {
            CheckNovelUpdateTask checkNovelUpdateTask = (CheckNovelUpdateTask) object;
            return from == checkNovelUpdateTask.from;
        }
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        int hash = hashCode;
        if (hash == 0) {
            hash = (from == null) ? 0 : from.from() << 30;
            hashCode = hash;
        }
        return hash;
    }
}
