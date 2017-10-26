package com.quduquxie.communal.utils;

import android.text.TextUtils;

import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.bean.User;
import com.quduquxie.model.BookShelf;

/**
 * Created on 16/12/29.
 * Created by crazylei.
 */

public class BeanUtil {

    public static Book bookTransformation(BookShelf bookShelf) {
        Book book = new Book();

        book.book_type = Book.TYPE_ONLINE;

        if (bookShelf.book != null) {
            if (!TextUtils.isEmpty(bookShelf.book.id)) {
                book.id = bookShelf.book.id;
            } else {
                return null;
            }
            if (!TextUtils.isEmpty(bookShelf.book.name)) {
                book.name = bookShelf.book.name;
            }
            if (!TextUtils.isEmpty(bookShelf.book.description)) {
                book.description = bookShelf.book.description;
            }
            if (bookShelf.book.word_count != 0) {
                book.word_count = bookShelf.book.word_count;
            }
            if (!TextUtils.isEmpty(bookShelf.book.attribute)) {
                book.attribute = bookShelf.book.attribute;
            }
            if (!TextUtils.isEmpty(bookShelf.book.category)) {
                book.category = bookShelf.book.category;
            }
            if (!TextUtils.isEmpty(bookShelf.book.style)) {
                book.style = bookShelf.book.style;
            }
            if (!TextUtils.isEmpty(bookShelf.book.ending)) {
                book.ending = bookShelf.book.ending;
            }
            if (!TextUtils.isEmpty(bookShelf.book.image_url)) {
                book.image = bookShelf.book.image_url;
            }
            if (!TextUtils.isEmpty(bookShelf.book.status)) {
                book.status = bookShelf.book.status;
            }
            if (bookShelf.book.user != null) {

                User author = new User();

                if (!TextUtils.isEmpty(bookShelf.book.user.id)) {
                    author.id = bookShelf.book.user.id;
                }
                if (!TextUtils.isEmpty(bookShelf.book.user.avatar_url)) {
                    author.avatar = bookShelf.book.user.avatar_url;
                }
                if (!TextUtils.isEmpty(bookShelf.book.user.penname)) {
                    author.name = bookShelf.book.user.penname;
                }

                book.author = author;
            }
            if (bookShelf.book.chapter != null) {

                Chapter chapter = new Chapter();

                if (!TextUtils.isEmpty(bookShelf.book.chapter.id)) {
                    chapter.id = bookShelf.book.chapter.id;
                }
                if (bookShelf.book.chapter.serial_number != -1) {
                    chapter.sn = bookShelf.book.chapter.serial_number;
                }
                if (!TextUtils.isEmpty(bookShelf.book.chapter.name)) {
                    chapter.name = bookShelf.book.chapter.name;
                }
                if (bookShelf.book.chapter.update_time != 0) {
                    chapter.create_time = bookShelf.book.chapter.update_time;
                }

                book.chapter = chapter;
            }
            if (bookShelf.serial_number > -1) {
                book.sequence = bookShelf.serial_number - 1;
                if (book.sequence > -1) {
                    book.read = 1;
                } else {
                    book.read = 0;
                }
            }
            if (bookShelf.word_offset > -1 ) {
                book.offset = bookShelf.word_offset;
            }
        }
        return book;
    }
}