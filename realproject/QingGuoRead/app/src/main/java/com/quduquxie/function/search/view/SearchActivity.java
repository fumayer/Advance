package com.quduquxie.function.search.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.search.SearchInterface;
import com.quduquxie.function.search.adapter.SearchDefaultAdapter;
import com.quduquxie.function.search.adapter.SearchResultAdapter;
import com.quduquxie.function.search.adapter.SearchSuggestAdapter;
import com.quduquxie.function.search.component.DaggerSearchComponent;
import com.quduquxie.function.search.listener.SearchListener;
import com.quduquxie.function.search.module.SearchModule;
import com.quduquxie.function.search.presenter.SearchPresenter;
import com.quduquxie.function.search.widget.SearchDefaultSpanSizeLookup;
import com.quduquxie.function.search.widget.SearchResultSpanSizeLookup;
import com.quduquxie.model.Category;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.util.ToastUtils;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class SearchActivity extends BaseActivity implements SearchInterface.View, TextWatcher, TextView.OnEditorActionListener, View.OnFocusChangeListener, SearchListener {

    @BindView(R.id.search_input)
    public EditText search_input;
    @BindView(R.id.search_clear)
    public ImageView search_clear;
    @BindView(R.id.search_cancel)
    public TextView search_cancel;

    @BindView(R.id.search_content)
    public FrameLayout search_content;
    @BindView(R.id.search_default)
    public RecyclerView search_default;
    @BindView(R.id.search_refresh)
    public SwipeRefreshLayout search_refresh;
    @BindView(R.id.search_result)
    public RecyclerView search_result;
    @BindView(R.id.search_suggest)
    public ListView search_suggest;

    @Inject
    SearchPresenter searchPresenter;

    private Handler uiHandler;

    private List<Category> search_categories = new ArrayList<>();
    private SearchDefaultAdapter searchDefaultAdapter;

    private List<Book> search_opuses = new ArrayList<>();
    private SearchResultAdapter searchResultAdapter;

    private LoadingPage loadingPage;

    private BookDaoHelper bookDaoHelper;

    private int page = 1;
    private String keyword;

    private GridLayoutManager resultGridLayoutManager;

    private ArrayList<String> suggestList = new ArrayList<>();
    private SearchSuggestAdapter suggestAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.layout_activity_search);

            initParameter();

        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);

        if (search_result != null && searchResultAdapter != null &&  search_opuses != null && search_opuses.size() > 0) {
            searchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerSearchComponent.builder()
                .applicationComponent(applicationComponent)
                .searchModule(new SearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            page = intent.getIntExtra("page", 1);
            keyword = intent.getStringExtra("keyword");
            if (search_input != null) {
                if (!TextUtils.isEmpty(keyword)) {
                    search_input.setText(keyword);
                    search_input.setSelection(keyword.length());
                }
                hideSoftInput(search_input);
            }
            if (!TextUtils.isEmpty(keyword)) {
                searchPresenter.loadSearchResult(keyword, page);
            }
            if (search_result != null) {
                search_result.scrollToPosition(0);
            }
        }
    }

    @Override
    public void setPresenter(SearchInterface.Presenter searchPresenter) {
        this.searchPresenter = (SearchPresenter) searchPresenter;
    }

    @Override
    public void initParameter() {

        ButterKnife.bind(this);

        page = 1;

        this.bookDaoHelper = BookDaoHelper.getInstance(this);

        search_default.setLayoutFrozen(true);

        if (search_default != null) {
            search_default.setVisibility(View.VISIBLE);
        }
        if (search_refresh != null) {
            search_refresh.setVisibility(View.INVISIBLE);
            if (search_result != null) {
                search_result.setVisibility(View.INVISIBLE);
            }
        }

        if (suggestList != null) {
            suggestList.clear();
        }
        if (suggestAdapter == null) {
            suggestAdapter = new SearchSuggestAdapter(getApplicationContext(), suggestList);
            suggestAdapter.setOnItemClickListener(this);
        }
        search_suggest.setAdapter(suggestAdapter);
        search_suggest.setDivider(null);

        if (search_input != null) {
            search_input.addTextChangedListener(this);
            search_input.setOnFocusChangeListener(this);
            search_input.setOnEditorActionListener(this);
        }

        if (search_refresh != null) {
            search_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    page = 1;
                    if (!TextUtils.isEmpty(keyword)) {
                        searchPresenter.loadSearchResult(keyword, page);
                    }
                }
            });
        }

        if (search_result != null) {
            search_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int visibleItemCount = resultGridLayoutManager.getChildCount();
                    int totalItemCount = resultGridLayoutManager.getItemCount();
                    int firstVisiblePosition = resultGridLayoutManager.findFirstVisibleItemPosition();

                    if (!search_refresh.isRefreshing() && searchPresenter.isCanSearchMore() && (visibleItemCount + firstVisiblePosition) == totalItemCount - 5) {
                        setRefreshViewState(true);
                        page += 1;
                        if (!TextUtils.isEmpty(keyword)) {
                            searchPresenter.loadMoreSearchResult(keyword, page);
                        }
                    }
                }
            });
        }
        searchPresenter.initParameter();
    }

    @Override
    public void initData() {
        searchDefaultAdapter = new SearchDefaultAdapter(getQuApplication(), search_categories, this);
        GridLayoutManager defaultGridLayoutManager = new GridLayoutManager(this, 3);

        SearchDefaultSpanSizeLookup customSpanSizeLookup = new SearchDefaultSpanSizeLookup(searchDefaultAdapter);
        defaultGridLayoutManager.setSpanSizeLookup(customSpanSizeLookup);

        search_default.setLayoutManager(defaultGridLayoutManager);
        search_default.setAdapter(searchDefaultAdapter);

        search_default.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return MotionEvent.ACTION_MOVE == event.getAction();
            }
        });

        searchResultAdapter = new SearchResultAdapter(getQuApplicationContext(), search_opuses, this);

        resultGridLayoutManager = new GridLayoutManager(this, 12);
        SearchResultSpanSizeLookup searchResultSpanSizeLookup = new SearchResultSpanSizeLookup(searchResultAdapter);
        resultGridLayoutManager.setSpanSizeLookup(searchResultSpanSizeLookup);

        search_result.setLayoutManager(resultGridLayoutManager);
        search_result.setAdapter(searchResultAdapter);
    }

    @Override
    public void showSearchView() {
        if (search_default != null) {
            search_default.setVisibility(View.VISIBLE);
            searchPresenter.refreshSearchHistory();
        }
        if (search_refresh != null) {
            search_refresh.setVisibility(View.INVISIBLE);
            if (search_result != null) {
                search_result.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void hideSearchView() {
        if (search_default != null) {
            search_default.setVisibility(View.INVISIBLE);
        }

        if (search_refresh != null) {
            search_refresh.setVisibility(View.VISIBLE);
            if (search_result != null) {
                search_result.smoothScrollToPosition(0);
                search_result.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initSearchView(List<Category> categories) {
        search_categories.clear();
        search_categories.addAll(categories);
        if (searchDefaultAdapter != null) {
            searchDefaultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshSearchView(List<Category> categories) {
        if (search_default != null && search_default.getVisibility() == View.VISIBLE) {
            search_categories.clear();
            search_categories.addAll(categories);

            if (searchDefaultAdapter != null) {
                searchDefaultAdapter.notifyDataSetChanged();
            } else {
                searchDefaultAdapter = new SearchDefaultAdapter(this, search_categories, this);
                search_default.setAdapter(searchDefaultAdapter);
            }
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, search_content);
        }
        searchPresenter.setLoadingPage(loadingPage);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void showLoadingError() {
        setRefreshViewState(false);
        if (loadingPage != null) {
            loadingPage.onError();
        }
    }

    @Override
    public void returnSearchResult(List<Book> books) {
        if (search_opuses == null) {
            search_opuses = new ArrayList<>();
        } else {
            search_opuses.clear();
        }

        for (Book book : books) {
            search_opuses.add(book);
        }
        if (searchResultAdapter == null) {
            searchResultAdapter = new SearchResultAdapter(getQuApplicationContext(), search_opuses, this);
            search_result.setAdapter(searchResultAdapter);
        } else {
            searchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void returnSearchMoreResult(List<Book> books) {
        for (Book book : books) {
            search_opuses.add(book);
        }
        searchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadingMoreError() {
        Book empty = new Book();
        empty.item_type = SearchResultAdapter.TYPE_EMPTY;
        search_opuses.add(empty);

        if (searchResultAdapter == null) {
            searchResultAdapter = new SearchResultAdapter(getQuApplicationContext(), search_opuses, this);
            search_result.setAdapter(searchResultAdapter);
        } else {
            searchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setSearchKeyWord(String keyWord) {
        if (search_input != null) {
            search_input.setText(keyWord);
            if (!TextUtils.isEmpty(keyWord)) {
                hideSoftInput(search_input);
                search_input.setSelection(keyWord.length());
            }
        }
    }

    @Override
    public void setSearchSuggest(ArrayList<String> suggests) {
        suggestList.clear();
        for (String suggest : suggests) {
            suggestList.add(suggest);
        }

        if (suggestAdapter == null) {
            suggestAdapter = new SearchSuggestAdapter(getApplicationContext(), suggestList);
        }
        suggestAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSearchRecommend(ArrayList<String> recommends) {
        if (recommends != null && recommends.size() > 0) {
            if (searchDefaultAdapter != null) {
                searchDefaultAdapter.setSearchRecommend(recommends);
            }
        }
    }

    @Override
    public void showSearchSuggestView() {
        if (search_suggest != null) {
            search_suggest.setVisibility(View.VISIBLE);
        }
        if (search_default != null) {
            search_default.setVisibility(View.INVISIBLE);
        }
        if (search_refresh != null) {
            search_refresh.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hideSearchSuggestView() {
        if (search_suggest != null) {
            search_suggest.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.search_clear, R.id.search_cancel})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.search_clear:
                if (search_input != null) {
                    search_input.setText(null);
                }
                if (search_clear != null) {
                    search_clear.setVisibility(View.INVISIBLE);
                }
                showSearchView();
                break;
            case R.id.search_cancel:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (searchPresenter != null) {
            suggestList.clear();
            if (suggestAdapter != null) {
                suggestAdapter.notifyDataSetChanged();
            }
            searchPresenter.showSuggestList(s.toString());
        }

        if (search_clear == null) {
            return;
        }
        if (!TextUtils.isEmpty(s.toString())) {
            search_clear.setVisibility(View.VISIBLE);
        } else {
            search_clear.setVisibility(View.INVISIBLE);
            s.clear();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            String word = null;
            if (search_input != null) {
                word = search_input.getText().toString();
            }
            if (word != null && word.trim().equals("")) {
                Toast.makeText(this, R.string.search_keyword_error, Toast.LENGTH_SHORT).show();
            } else {
                hideSoftInput(v);
                if (word != null && !word.equals("") && searchPresenter != null) {
                    keyword = word;
                    page = 1;
                    searchPresenter.loadSearchResult(keyword, page);
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (search_input == null)
            return;
        if (hasFocus) {
            showSoftInput(search_input);
        } else {
            hideSoftInput(search_input);
        }
    }

    @Override
    public void feedbackListener() {
        String errorMessage = searchPresenter.openFeedBackActivity();
        if (!TextUtils.isEmpty(errorMessage)) {
            ToastUtils.showToastNoRepeat(errorMessage);
        }
    }

    @Override
    public void recommendMoreListener() {
        Intent intent = new Intent();
        intent.putExtra("position", 1);
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void searchResultListener(Book book) {
        Book localBook = bookDaoHelper.loadBook(book.id, Book.TYPE_ONLINE);
        if (localBook != null && localBook.sequence != -2) {
            Intent intent = new Intent(SearchActivity.this, ReadingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("sequence", localBook.sequence);
            bundle.putInt("offset", localBook.offset);
            bundle.putSerializable("book", localBook);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, CoverActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", book.id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void categoryListener(String word) {
        if (TextUtils.isEmpty(word)) {
            Toast.makeText(this, R.string.search_keyword_error, Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(word) && searchPresenter != null) {
                setSearchKeyWord(word);
                page = 1;
                keyword = word;
                searchPresenter.loadSearchResult(keyword, page);
            }
        }

    }

    @Override
    public void deleteHistory() {
        searchPresenter.clearSearchHistory();
    }

    @Override
    public void suggestItemClicked(String suggest) {
        if (!TextUtils.isEmpty(suggest)) {
            suggest = suggest.replace("<font color=\"#4d91d0\">", "");
            suggest = suggest.replace("</font>", "");
            setSearchKeyWord(suggest);
            keyword = suggest;
            page = 1;
            searchPresenter.loadSearchResult(keyword, page);
        }
    }

    public void showSoftInput(final View view) {
        if (uiHandler == null) {
            uiHandler = new Handler();
        }
        uiHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if (view != null) {
                    inputMethodManager.showSoftInput(view, 0);
                }
            }
        }, 500);

    }

    public void hideSoftInput(final View view) {
        if (view == null || view.getContext() == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public void setRefreshViewState(boolean refreshing) {
        if (search_refresh == null) {
            return;
        }
        if (!refreshing) {
            // 防止刷新消失太快，让子弹飞一会儿. do not use lambda!!
            search_refresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    search_refresh.setRefreshing(false);
                }
            }, 500);
        } else {
            search_refresh.setRefreshing(true);
        }
    }
}
