package com.quduquxie.service;

import android.app.IntentService;
import android.content.Intent;

import com.quduquxie.util.DeleteBookHelper;
import com.quduquxie.util.QGLog;


public class DeleteBookIntentService extends IntentService {
	
	String TAG = "DeleteBookIntentService";
	public static final String ACTION_DO_DELETE = "com.quduquxie.book.action_delete_book";
	private DeleteBookHelper helper;

	
	public DeleteBookIntentService() {
		super("DeleteBookIntentService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		QGLog.d(TAG, "创建服务");
		helper = new DeleteBookHelper(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		QGLog.d(TAG, "开启服务");

		return super.onStartCommand(intent, flags, startId);
	}


	public void onDestroy() {
		super.onDestroy();
		helper.doRemoveCallbackHelper();
		QGLog.d(TAG, "服务销毁");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		QGLog.d(TAG, "服务开始");
		
		if (intent != null) {
			
			QGLog.d(TAG, "intent.getAction() = " + intent.getAction());
			if (ACTION_DO_DELETE.equals(intent.getAction())) {
				
				helper.doDeleteHelper();
			}
		}
		QGLog.d(TAG, "服务结束");
	}
}
