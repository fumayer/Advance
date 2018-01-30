package com.aiwue.model;

import java.io.Serializable;

/**
 * Created by liaixiong on 2016/09/24.
 */
public class UpdatedApkInfo implements Serializable {
    private String version;
    private int versionCode;
    private String description;
    private String url;
    private String updateDate;
    private int size;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "UpdatedApkInfo{" +
                "version=" + version +
                ", versionCode=" + versionCode +
//                ", description=" + description +
                ", url=" + url +
                ", updateDate=" + updateDate +
                ", size=" + size +
                '}';
    }
}
