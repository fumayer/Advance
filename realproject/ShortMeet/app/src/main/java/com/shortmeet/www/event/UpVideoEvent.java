package com.shortmeet.www.event;

/**
 * Created by Fenglingyue on 2017/11/3.
 */

public class UpVideoEvent {
  private long totalSize;
  private long  currentSize;
  private String message;
  private int code;

    public UpVideoEvent() { }
    public int getCode() {
     return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
