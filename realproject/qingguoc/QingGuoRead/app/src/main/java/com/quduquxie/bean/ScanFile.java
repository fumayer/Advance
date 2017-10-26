package com.quduquxie.bean;

import java.io.File;

public class ScanFile {
    private String id;
    private String name;
    private String sortCollection;
    private File file;
    private boolean isChecked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortCollection() {
        return sortCollection;
    }

    public void setSortCollection(String sortCollection) {
        this.sortCollection = sortCollection;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ScanFile)) {
            return false;
        } else {
            ScanFile scanFile = (ScanFile) o;
            return scanFile.file.equals(this.file);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        return result;
    }
}
