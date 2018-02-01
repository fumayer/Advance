package com.example.sj.imitateproject.base;

import android.os.Bundle;

/**
 * Created by sunjie on 2018/1/30.
 */

public interface Ipresenter<T> {
    /**
     * setContent 之前的相关操作
     *
     * @param saveInstance
     */
    void create(Bundle saveInstance);

    /**
     * setContent 之后的操作
     *
     * @param savaInstance
     */
    void created(Bundle savaInstance);

    /**
     * 获取View类型
     *
     * @return
     */
    Class<T> getViewClass();
}
