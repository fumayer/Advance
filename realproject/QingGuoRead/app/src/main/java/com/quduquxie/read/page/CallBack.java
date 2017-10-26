package com.quduquxie.read.page;

public interface CallBack {
	void onShowMenu(boolean show);

	void onCancelPage();

	void onResize();

	void refreshBookMark();

	void refreshHideView(int position);

	void changeBookMarkDB();

	void openCatalog();
}
