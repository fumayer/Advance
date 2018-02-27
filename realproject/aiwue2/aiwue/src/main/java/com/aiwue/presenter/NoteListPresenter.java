package com.aiwue.presenter;

import com.aiwue.base.BasePresenter;
import com.aiwue.iview.INoteListView;
import com.aiwue.model.Note;
import com.aiwue.model.requestParams.GetNoteListParams;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  主页中的视频列表页面
 * Created by Yibao on 2017年4月19日12:53:31
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class NoteListPresenter extends BasePresenter<INoteListView> {
    public NoteListPresenter(INoteListView mvpView) {
        super(mvpView);
    }

    public void getNoteList(int pIndex, int pSize, int type) {
        GetNoteListParams params = new GetNoteListParams();
        params.setpIndex(pIndex);
        params.setpSize(pSize);
        params.setType(type);
        params.setIsGood(1);
        params.setIsPublic(1);

        AiwueClient.getNoteList(null, null, Schedulers.io(), AndroidSchedulers.mainThread(),params, new SubscriberCallBack<List<Note>>(){
            @Override
            protected void onSuccess(List<Note> response) {
                if(response != null)
                    Logger.i(response.toString());
                mvpView.onGetNoteListSuccess(true, null, response);

            }
            @Override
            protected void onError(String err) {
                Logger.i(err);
                mvpView.onGetNoteListSuccess(false, err, null);
            }

        });
    }
}
