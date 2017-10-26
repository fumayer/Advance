package com.quduquxie.model.creation;

import java.io.Serializable;

/**
 * Created on 16/11/22.
 * Created by crazylei.
 */

public class Draft implements Serializable {
    public String id;
    public String name;
    public String local;
    public String status;
    public String book_id;
    public String content;
    public String check_status;
    public String check_message;

    public long word_count;
    public long update_time;

    public int serial_number;
    public int need_synchronize = 1;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Draft)) {
            return false;
        } else {
            Draft draft = (Draft) o;
            return draft.id.equals(this.id) && draft.name.equals(this.name);
        }
    }

    @Override
    public String toString() {
        return "Draft{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", local='" + local + '\'' +
                ", status='" + status + '\'' +
                ", book_id='" + book_id + '\'' +
                ", content='" + content + '\'' +
                ", check_status='" + check_status + '\'' +
                ", check_message='" + check_message + '\'' +
                ", word_count=" + word_count +
                ", update_time=" + update_time +
                ", serial_number=" + serial_number +
                ", need_synchronize=" + need_synchronize +
                '}';
    }
}
