package com.quduquxie.service.check;

import android.app.NotificationManager;
import android.content.Context;

import java.util.ArrayList;

public class CheckNovelUpdateHelper {

    public static class CheckBook {

        public String id;
        public String name;

        public int count;

        public CheckBook(String name, String id, int count) {
            super();
            this.id = id;
            this.name = name;

            this.count = count;
        }

        @Override
        public boolean equals(Object object) {
            if (object != null && object instanceof CheckBook) {
                CheckBook checkBook = (CheckBook) object;
                return name.equals(checkBook.name) && id.equals(checkBook.id);
            }
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }
    }


    public static void dealLocalNotification(Context context) {
        CheckNovelUpdateService.localCheckBooks = null;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(CheckNovelUpdateService.CHECK_NOVEL_UPDATE_NOTIFICATION_ID);
        }
    }

    public static ArrayList<CheckBook> combain(Context context, ArrayList<CheckBook> checkBooks) {
        ArrayList<CheckBook> cacheBooks = CheckNovelUpdateService.localCheckBooks;
        if (cacheBooks == null)
            return checkBooks;
        if (checkBooks == null)
            return cacheBooks;
        if (cacheBooks.size() == 0 || checkBooks.size() == 0) {
            cacheBooks.addAll(checkBooks);
            return cacheBooks;
        }

        ArrayList<CheckBook> intersection = new ArrayList<>();

        int size = checkBooks.size();
        int cacheSize = cacheBooks.size();
        for (int i = 0; i < cacheSize; i++) {
            CheckBook book = cacheBooks.get(i);
            boolean contain = false;
            label:
            for (int j = 0; j < size; j++) {
                CheckBook checkBook = checkBooks.get(j);
                if (book.equals(checkBook)) {
                    checkBook.count += book.count;
                    contain = true;
                    break label;
                }
            }
            if (!contain) {
                intersection.add(book);
            }
        }
        checkBooks.addAll(intersection);
        return checkBooks;

    }

}
