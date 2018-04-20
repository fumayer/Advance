package com.newsdemo.model.http.response;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class GoldHttpResponse<T> {
    private T results;
    public T getResults(){
        return results;
    }

    public void setResults(T results){
        this.results=results;
    }
}
