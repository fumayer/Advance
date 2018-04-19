package com.newsdemo.model.event;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class SearchEvent {
    private int type;
    private String query;

    public SearchEvent(String query,int type){
        this.query=query;
        this.type=type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
