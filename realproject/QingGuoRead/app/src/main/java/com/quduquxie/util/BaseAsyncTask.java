package com.quduquxie.util;

import android.os.AsyncTask;
import android.os.Build;

/**
 * 
* BaseAsyncTask
* TODO(屏蔽掉系统版本10之后异步任务串行执行。注意：执行该异步任务时请调用 execute2 方法，不要调用 父类execute 方法)
* 
* @param <Params>
* @param <Progress>
* @param <Result>
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{
    public final AsyncTask<Params, Progress, Result> execute2(Params... params) {
    	if(Build.VERSION.SDK_INT>10){
    		return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    	} else {
    		return super.execute(params);
    	}
    }
}
