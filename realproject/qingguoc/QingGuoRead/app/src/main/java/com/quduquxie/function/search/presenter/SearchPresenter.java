package com.quduquxie.function.search.presenter;

import android.text.TextUtils;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IWxCallback;
import com.orhanobut.logger.Logger;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.function.search.SearchInterface;
import com.quduquxie.function.search.adapter.SearchDefaultAdapter;
import com.quduquxie.function.search.adapter.SearchResultAdapter;
import com.quduquxie.function.search.util.SearchUtil;
import com.quduquxie.function.search.view.SearchActivity;
import com.quduquxie.model.Category;
import com.quduquxie.model.SearchResult;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 16/10/19.
 * Created by WangLei.
 */

public class SearchPresenter implements SearchInterface.Presenter {

    private SearchActivity searchActivity;

    private ArrayList<String> search_category = new ArrayList<>();
    private ArrayList<String> search_history = new ArrayList<>();

    private ArrayList<String> search_recommend = new ArrayList<>();

    private ArrayList<Category> search_categories = new ArrayList<>();

    private ArrayList<Book> search_result = new ArrayList<>();
    private ArrayList<Book> search_more_result = new ArrayList<>();

    private LoadingPage loadingPage;

    private boolean can_search_more = true;

    private ArrayList<String> suggestList = new ArrayList<>();

    public SearchPresenter(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    @Override
    public void initParameter() {
        searchActivity.initData();

        loadSearchRecommendData();

        FeedbackAPI.initAnnoy(searchActivity.getQuApplication(), BaseConfig.FEEDBACK_APP_KEY);
        Map<String, String> map = new HashMap<>();
        map.put("enableAudio", String.valueOf(0));
        map.put("hideLoginSuccess", "true");
        map.put("sendBtnText", "发消息");
        map.put("color", "#3a3a3a");
        FeedbackAPI.setUICustomInfo(map);

        FeedbackAPI.getFeedbackUnreadCount(searchActivity, null, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i) {

            }
        });
    }

    @Override
    public void loadSearchResult(String keyword, int page) {
        searchActivity.showLoadingPage();
        searchActivity.hideSearchSuggestView();
        searchActivity.hideSearchView();

        saveHistoryWord(keyword);

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.loadSearchResult(keyword, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<SearchResult>>() {
                    @Override
                    public void onNext(CommunalResult<SearchResult> searchResult) {
                        Logger.d("LoadSearchResult onNext");
                        if (searchResult != null) {
                            if (searchResult.getCode() == 0 && searchResult.getModel() != null) {
                                handleSearchResult(searchResult.getModel());
                                searchActivity.returnSearchResult(search_result);
                                searchActivity.hideLoadingPage();
                            } else {
                                can_search_more = false;
                                searchActivity.showLoadingError();
                            }
                        } else {
                            can_search_more = false;
                            searchActivity.showLoadingError();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadSearchResult onError: " + throwable.toString());
                        can_search_more = false;
                        searchActivity.showLoadingError();
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadSearchResult onComplete");
                    }
                });
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    @Override
    public void loadMoreSearchResult(String keyword, int page) {
        search_more_result.clear();

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.loadSearchResult(keyword, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<SearchResult>>() {
                    @Override
                    public void onNext(CommunalResult<SearchResult> searchResult) {
                        Logger.d("LoadSearchResult onNext");
                        searchActivity.setRefreshViewState(false);
                        if (searchResult != null) {
                            if (searchResult.getCode() == 0 && searchResult.getModel() != null) {
                                handleSearchMoreResult(searchResult.getModel());
                                searchActivity.returnSearchMoreResult(search_more_result);
                                searchActivity.hideLoadingPage();
                            } else {
                                can_search_more = false;
                                searchActivity.loadingMoreError();
                            }
                        } else {
                            can_search_more = false;
                            searchActivity.loadingMoreError();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadSearchResult onError: " + throwable.toString());
                        can_search_more = false;
                        searchActivity.setRefreshViewState(false);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadSearchResult onComplete");
                    }
                });
    }

    @Override
    public String openFeedBackActivity() {
        return FeedbackAPI.openFeedbackActivity(searchActivity);
    }

    @Override
    public void clearSearchHistory() {
        search_history.clear();
        SearchUtil.deleteHistoryWord(searchActivity);
        resetCategories();
    }

    @Override
    public void refreshSearchHistory() {
        search_categories.clear();

        Category category_head = new Category();
        category_head.item_type = SearchDefaultAdapter.TYPE_CATEGORY_HEAD;
        search_categories.add(category_head);

        Category category_group = new Category();
        category_group.item_type = SearchDefaultAdapter.TYPE_CATEGORY_GROUP;
        search_categories.add(category_group);

        initSearchHistoryData();

        searchActivity.refreshSearchView(search_categories);
    }

    private void resetCategories() {
        search_categories.clear();

        Category category_head = new Category();
        category_head.item_type = SearchDefaultAdapter.TYPE_CATEGORY_HEAD;
        search_categories.add(category_head);

        Category category_group = new Category();
        category_group.item_type = SearchDefaultAdapter.TYPE_CATEGORY_GROUP;
        search_categories.add(category_group);

        searchActivity.refreshSearchView(search_categories);
    }

    private void saveHistoryWord(String keyword) {
        if (search_history == null) {
            search_history = new ArrayList<>();
        }

        if (keyword == null || TextUtils.isEmpty(keyword)) {
            return;
        }

        if (search_history.contains(keyword)) {
            search_history.remove(keyword);
        }

        if (!search_history.contains(keyword)) {
            int size = search_history.size();
            if (size >= 5) {
                search_history.remove(size - 1);
            }
            search_history.add(0, keyword);
            SearchUtil.saveHistoryWord(searchActivity, search_history);
        }

        initSearchHistoryData();

        searchActivity.refreshSearchView(search_categories);
    }

    public boolean isCanSearchMore() {
        return can_search_more;
    }

    @Override
    public void showSuggestList(String keyword) {
        searchActivity.showSearchSuggestView();

        String word = null;
        try {
            if (!TextUtils.isEmpty(keyword)) {
                word = URLEncoder.encode(keyword, "utf-8");
            }
        } catch (UnsupportedEncodingException exception) {
            word = keyword;
            exception.printStackTrace();
        }
        if (!TextUtils.isEmpty(word)) {
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
            dataRequestService.loadSearchGuess(word, 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<ArrayList<String>>>() {
                        @Override
                        public void onNext(CommunalResult<ArrayList<String>> searchGuessResult) {
                            if (searchGuessResult != null && searchGuessResult.getCode() == 0 && searchGuessResult.getModel() != null) {
                                Logger.d("LoadSearchGuess onNext: " + searchGuessResult.getModel().size());
                                ArrayList<String> result_suggest = searchGuessResult.getModel();
                                suggestList.clear();
                                int index = 0;
                                for (String item : result_suggest) {
                                    if (index > 8) {
                                        break;
                                    }
                                    if (item != null) {
                                        item = item.replace("<@*@>", "<font color=\"#4d91d0\">");
                                        item = item.replace("</@*@>", "</font>");
                                        suggestList.add(item);
                                        index++;
                                    }
                                }
                                searchActivity.setSearchSuggest(suggestList);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadSearchGuess onError: " + throwable.toString());
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadSearchGuess onComplete");
                        }
                    });
        }
    }

    private void loadSearchRecommendData() {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.loadSearchSuggest(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<List<String>>>() {
                    @Override
                    public void onNext(CommunalResult<List<String>> searchSuggestResult) {
                        Logger.d("LoadSearchSuggest onNext");

                        if (searchSuggestResult != null && searchSuggestResult.getCode() == 0 && searchSuggestResult.getModel() != null && searchSuggestResult.getModel().size() > 0) {
                            for (String string : searchSuggestResult.getModel()) {
                                if (!TextUtils.isEmpty(string)) {
                                    search_recommend.add(string);
                                }
                            }
                            Category category_head = new Category();
                            category_head.item_type = SearchDefaultAdapter.TYPE_CATEGORY_HEAD;
                            search_categories.add(category_head);

                            Category category_group = new Category();
                            category_group.item_type = SearchDefaultAdapter.TYPE_CATEGORY_GROUP;
                            search_categories.add(category_group);
                            searchActivity.setSearchRecommend(search_recommend);

                        }
                        initSearchHistoryData();
                        searchActivity.initSearchView(search_categories);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadSearchSuggest onError: " + throwable.toString());
                        initSearchHistoryData();
                        searchActivity.initSearchView(search_categories);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadSearchSuggest onComplete");
                    }
                });
    }

    private void initSearchHistoryData() {
        search_history.clear();
        search_history = SearchUtil.getHistoryWord(searchActivity);

        Category category;

        if (search_history != null && !search_history.isEmpty()) {
            category = new Category();
            category.label = "历史记录";
            category.item_type = SearchDefaultAdapter.TYPE_HISTORY;
            search_categories.add(category);

            for (String label : search_history) {
                category = new Category();
                category.label = label;
                category.item_type = SearchDefaultAdapter.TYPE_HISTORY_ITEM;
                search_categories.add(category);
            }
        }
    }

    private void handleSearchResult(SearchResult searchResult) {
        search_result.clear();
        if (searchResult.result_list != null && !searchResult.result_list.isEmpty()) {
            for (Book book : searchResult.result_list) {
                book.item_type = SearchResultAdapter.TYPE_BOOK;
                search_result.add(book);
            }
            if (searchResult.result_list.size() < 20) {
                Book empty = new Book();
                empty.item_type = SearchResultAdapter.TYPE_EMPTY;
                search_result.add(empty);
                can_search_more = false;
            } else {
                can_search_more = true;
            }
        } else {
            Book empty = new Book();
            empty.item_type = SearchResultAdapter.TYPE_EMPTY;
            search_result.add(empty);

            if (searchResult.recommend_list != null && !searchResult.recommend_list.isEmpty()) {
                Book head = new Book();
                head.item_type = SearchResultAdapter.TYPE_RECOMMEND_HEAD;
                search_result.add(head);

                for (Book book : searchResult.recommend_list) {
                    book.item_type = SearchResultAdapter.TYPE_RECOMMEND_BOOK;
                    search_result.add(book);
                }

                Book more = new Book();
                more.item_type = SearchResultAdapter.TYPE_MORE;
                search_result.add(more);
            }

            Book book;
            for (String category : search_category) {
                book = new Book();
                book.name = category;
                book.item_type = SearchResultAdapter.TYPE_CATEGORY;
                search_result.add(book);
            }
            can_search_more = false;
        }
    }

    private void handleSearchMoreResult(SearchResult searchResult) {
        search_more_result.clear();
        if (searchResult.result_list != null && !searchResult.result_list.isEmpty()) {
            for (Book book : searchResult.result_list) {
                book.item_type = SearchResultAdapter.TYPE_BOOK;
                search_more_result.add(book);
            }
            if (searchResult.result_list.size() < 20) {
                Book empty = new Book();
                empty.item_type = SearchResultAdapter.TYPE_EMPTY;
                search_more_result.add(empty);
                can_search_more = false;
            } else {
                can_search_more = true;
            }
        } else {
            Book empty = new Book();
            empty.item_type = SearchResultAdapter.TYPE_EMPTY;
            search_more_result.add(empty);
            can_search_more = false;
        }
    }
}
