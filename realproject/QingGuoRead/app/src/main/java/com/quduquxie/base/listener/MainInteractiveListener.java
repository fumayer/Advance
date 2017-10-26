package com.quduquxie.base.listener;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public interface MainInteractiveListener {

    void changeTitle(String title, String subtitle);

    void showEmptyPrompt();

    void hideEmptyPrompt();

    void cancelUpdateStatus(String id);

    void resetDeleteSelected(int count);

    void showFragment(int position);

    void showBottomView();

    void hideBottomView(int count);

    void checkShelfGuide();

    void showShelfSlideMore();

    void hideShelfSlideMore();

    void storeContentScrollDistance(int distance);
}