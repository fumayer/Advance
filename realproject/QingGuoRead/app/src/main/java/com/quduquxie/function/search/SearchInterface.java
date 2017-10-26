package com.quduquxie.function.search;

import com.quduquxie.base.bean.Book;
import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;
import com.quduquxie.model.Category;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 17/3/30.
 * Created by crazylei.
 */

public interface SearchInterface {
    interface Presenter extends BasePresenter {

        void loadSearchResult(String keyword, int page);

        void setLoadingPage(LoadingPage loadingPage);

        void loadMoreSearchResult(String keyword, int page);

        String openFeedBackActivity();

        void clearSearchHistory();

        void refreshSearchHistory();

        boolean isCanSearchMore();

        void showSuggestList(String keyword);
    }

    interface View extends BaseView<Presenter> {

        void initData();

        void showSearchView();

        void hideSearchView();

        void initSearchView(List<Category> categories);

        void refreshSearchView(List<Category> categories);

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void returnSearchResult(List<Book> books);

        void returnSearchMoreResult(List<Book> books);

        void loadingMoreError();

        void setSearchKeyWord(String keyWord);

        void setSearchSuggest(ArrayList<String> suggests);

        void setSearchRecommend(ArrayList<String> recommends);

        void showSearchSuggestView();

        void hideSearchSuggestView();
    }
}
