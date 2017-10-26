package com.quduquxie.base.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.quduquxie.model.Comment;

import java.io.Serializable;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class Book implements Serializable, Comparable<Book> {

    public static final int TYPE_ONLINE = 0;
    public static final int TYPE_LOCAL_TXT = 1;

    public String id;
    public String name;
    public String style;
    public String image;
    public String status;
    public String ending;
    public String channel;
    public String category;
    public String attribute;
    public String authorTalk;
    public String description;
    public String bannerImage;

    public long read_count;
    public long word_count;
    public long click_count;
    public long follow_count;

    public int is_sign;
    public int is_copyright;

    public User author;
    public Chapter chapter;
    public Comment hotComment;
    public User bigGodUser;

    public int read;
    public int offset = -1;
    public int sequence = -2;
    public int flush_count;

    public long insert_time;
    public long update_time;
    public long sequence_time;

    public int update_status = -1;

    //章节是否更新标识：1：增量更新 2：暂无更新 3：全量更新
    public int chapters_update_state;
    public int chapters_update_index;

    //检查更新数量
    public int update_count;
    //是否需要修复书签
    public boolean repairBookmark;

    //书籍类型
    public int book_type;
    //本地书籍文件大小
    public long book_size;
    //本地书籍路径
    public String file_path;


    //临时字段
    public int item_type;


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Book)) {
            return false;
        } else if (TextUtils.isEmpty(((Book)o).id)) {
            return false;
        } else {
            Book book = (Book) o;
            return book.id.equals(this.id);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public int compareTo(@NonNull Book book) {
        return this.sequence_time == book.sequence_time ? 0 : (this.sequence_time < book.sequence_time ? 1 : -1);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", style='" + style + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", ending='" + ending + '\'' +
                ", channel='" + channel + '\'' +
                ", category='" + category + '\'' +
                ", attribute='" + attribute + '\'' +
                ", description='" + description + '\'' +
                ", read_count=" + read_count +
                ", word_count=" + word_count +
                ", click_count=" + click_count +
                ", follow_count=" + follow_count +
                ", is_sign=" + is_sign +
                ", is_copyright=" + is_copyright +
                ", author=" + author +
                ", chapter=" + chapter +
                ", hotComment=" + hotComment +
                ", read=" + read +
                ", offset=" + offset +
                ", sequence=" + sequence +
                ", flush_count=" + flush_count +
                ", insert_time=" + insert_time +
                ", update_time=" + update_time +
                ", sequence_time=" + sequence_time +
                ", update_status=" + update_status +
                ", chapters_update_state=" + chapters_update_state +
                ", chapters_update_index=" + chapters_update_index +
                ", authorTalk='" + authorTalk + '\'' +
                ", update_count=" + update_count +
                ", repairBookmark=" + repairBookmark +
                ", book_type=" + book_type +
                ", book_size=" + book_size +
                ", file_path='" + file_path + '\'' +
                ", item_type=" + item_type +
                '}';
    }
}
