package com.quduquxie.module.read.reading.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.orhanobut.logger.Logger;
import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.R;
import com.quduquxie.application.SharedPreferencesUtil;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.base.module.reading.catalog.view.ReadingCatalogActivity;
import com.quduquxie.base.util.ResourceUtil;
import com.quduquxie.communal.dialog.CustomDialogFragment;
import com.quduquxie.communal.dialog.CustomDialogListener;
import com.quduquxie.communal.utils.FileUtil;
import com.quduquxie.function.read.end.view.ReadEndActivity;
import com.quduquxie.manager.OwnNativeAdManager;
import com.quduquxie.model.CommentList;
import com.quduquxie.module.comment.view.CommentListActivity;
import com.quduquxie.module.read.reading.component.DaggerReadingComponent;
import com.quduquxie.module.read.reading.module.ReadingModule;
import com.quduquxie.module.read.reading.presenter.ReadingPresenter;
import com.quduquxie.read.IReadDataFactory;
import com.quduquxie.read.NovelHelper;
import com.quduquxie.read.ReadDataFactory;
import com.quduquxie.read.ReadSettingView;
import com.quduquxie.read.ReadStatus;
import com.quduquxie.read.local.ReadTXTDataFactory;
import com.quduquxie.read.page.CallBack;
import com.quduquxie.read.page.PageInterface;
import com.quduquxie.read.page.PageView;
import com.quduquxie.read.page.ScrollPageView;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.service.download.DownloadService;
import com.quduquxie.service.download.DownloadServiceUtil;
import com.quduquxie.service.download.DownloadState;
import com.quduquxie.service.download.DownloadTask;
import com.quduquxie.util.BaseAsyncTask;
import com.quduquxie.util.BookHelper;
import com.quduquxie.util.QGLog;
import com.quduquxie.util.StatServiceUtils;
import com.quduquxie.util.StringUtils;
import com.quduquxie.util.TimeUtils;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class ReadingActivity extends BaseActivity<ReadingPresenter> implements View.OnClickListener, CallBack, IReadDataFactory.ReadDataListener, ReadSettingView.ReadSettingListener {
    private static final String TAG = ReadingActivity.class.getSimpleName();

    public static final int MESSAGE_LOAD_JUMP_CHAPTER = 0x50;
    public static final int MESSAGE_LOAD_NEXT_CHAPTER = 0x51;
    public static final int MESSAGE_LOAD_CURRENT_CHAPTER = 0x52;
    public static final int MESSAGE_LOAD_PREVIOUS_CHAPTER = 0x53;

    public static final int MESSAGE_LOAD_CHAPTER_ERROR = 0x54;


    private final static String mFormat = "k:mm";

    private PageInterface read_page;

    private SharedPreferencesUtil sharedPreferencesUtil;

    private Animation menuDownInAnimation;
    private Animation menuUpOutAnimation;

    private Runnable ticker;
    private Calendar calendar;

    private ReadStatus readStatus;
    private NovelHelper novelHelper;
    private NovelDownloader novelDownloader;
    private IReadDataFactory readDataFactory;

    private CharSequence time_text;

    private boolean subscribe;
    private boolean timer_stopped = false;

    private boolean FROM_COVER = true;

    private static final int font_count = 50;

    private Handler handler = new UiHandler(this);
    private OwnNativeAdManager ownNativeAdManager;

    public RelativeLayout reading_content;
    public ImageView read_bookmark;
    public ImageView read_bookmark_arrow;
    public TextView read_bookmark_prompt;
    public ImageView read_bookmark_added;
    public FrameLayout read_base_view;


    public RelativeLayout read_head_options;
    public ImageView read_head_options_back;
    public ImageView read_head_options_bookmark;
    public RelativeLayout read_head_options_comment;
    public ImageView read_head_options_comment_image;
    public TextView read_head_options_comment_count;
    public ImageView read_head_options_download;


    public ReadSettingView read_setting_view;

    int readLength = 0;

    private boolean is_add_bookmark = false;
    private boolean can_consume_event = false;

    private View read_guide_view;
    private ViewStub layout_read_guide;

    private View read_bookmark_guide_view;
    private ViewStub layout_read_bookmark_guide;

    private View read_catalog_guide_view;
    private ViewStub layout_read_catalog_guide;

    private Handler timeHandler = new Handler();

    private Toast toast;

    private CustomDialogFragment customDialogFragment;

    private BookDaoHelper bookDaoHelper;

    private DownloadReceiver downloadReceiver;

    private DownloadService downloadService;

    private boolean register = false;

    @Inject
    ReadingPresenter readingPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.layout_activity_read);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        bookDaoHelper = BookDaoHelper.getInstance(this);

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        BaseConfig.READING_FLIP_MODE = sharedPreferencesUtil.loadInteger(BaseConfig.FLAG_READING_FLIP_MODE, 1);
        BaseConfig.READING_FLIP_UP_DOWN = (BaseConfig.READING_FLIP_MODE == 3);

        Constants.FULL_WINDOW_READ = sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_READING_FULL_WINDOW, true);


        readStatus = new ReadStatus(this);
        ((QuApplication) getApplication()).setReadStatus(readStatus);
        novelHelper = new NovelHelper(getApplicationContext(), this, readStatus);

        initWindow();
        initScreenOrientation();
        initSavedInstanceState(savedInstanceState);

        if (readStatus.book.book_type == Book.TYPE_LOCAL_TXT) {
            readDataFactory = new ReadTXTDataFactory(getApplicationContext(), this, readStatus, novelHelper);
            readDataFactory.setReadDataListener(this);
            Logger.e("本地书籍");
        } else {
            readDataFactory = new ReadDataFactory(getApplicationContext(), this, readStatus, novelHelper);
            readDataFactory.setReadDataListener(this);
            Logger.e("线上书籍");
        }

        if (FROM_COVER && Constants.LANDSCAPE) {
            return;
        }

        initBookState();
        initView();
        initListener();
        loadBookContent();

        registerDownloadReceiver();

        downloadService = applicationUtil.getDownloadService();

        if (downloadService == null) {
            applicationUtil.bindDownloadService();
            downloadService = applicationUtil.getDownloadService();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        try {
            setContentView(R.layout.layout_activity_read);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        BaseConfig.READING_FLIP_MODE = sharedPreferencesUtil.loadInteger(BaseConfig.FLAG_READING_FLIP_MODE, 1);
        BaseConfig.READING_FLIP_UP_DOWN = (BaseConfig.READING_FLIP_MODE == 3);

        Constants.FULL_WINDOW_READ = sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_READING_FULL_WINDOW, true);

        readStatus = new ReadStatus(this);
        ((QuApplication) getApplication()).setReadStatus(readStatus);
        novelHelper = new NovelHelper(getApplicationContext(), this, readStatus);

        initWindow();
        initScreenOrientation();
        initSavedInstanceState(intent.getExtras());

        if (readStatus.book.book_type == Book.TYPE_LOCAL_TXT) {
            readDataFactory = new ReadTXTDataFactory(getApplicationContext(), this, readStatus, novelHelper);
            readDataFactory.setReadDataListener(this);
        } else {
            readDataFactory = new ReadDataFactory(getApplicationContext(), this, readStatus, novelHelper);
            readDataFactory.setReadDataListener(this);
        }

        if (FROM_COVER && Constants.LANDSCAPE) {
            return;
        }

        initBookState();
        initView();
        initListener();
        loadBookContent();

        downloadService = applicationUtil.getDownloadService();

        if (downloadService == null) {
            applicationUtil.bindDownloadService();
            downloadService = applicationUtil.getDownloadService();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (read_page != null) {
            read_page.recyclerData();
        }
        try {
            setContentView(R.layout.layout_activity_read);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        initWindow();

        initBookState();
        initView();
        initListener();
        loadBookContent();

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = this.loadSharedPreferencesUtil();
        }

        downloadService = applicationUtil.getDownloadService();

        if (downloadService == null) {
            applicationUtil.bindDownloadService();
            downloadService = applicationUtil.getDownloadService();
        }

        setReadingMode();

        readStatus.chapter_count = readStatus.book.chapter.sn;

        super.onConfigurationChanged(newConfig);
    }

    /**
     * 初始化窗口基本信息
     **/
    private void initWindow() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        readStatus.screen_width = displayMetrics.widthPixels;
        readStatus.screen_height = displayMetrics.heightPixels;
        readStatus.screen_density = displayMetrics.density;
        readStatus.screen_scaled_density = displayMetrics.scaledDensity;

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        BaseConfig.READING_TEXT_SIZE = sharedPreferencesUtil.loadInteger(BaseConfig.FLAG_READING_TEXT_SIZE, 15);
    }

    /**
     * 初始化屏幕方向
     **/
    private void initScreenOrientation() {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        int screen_orientation = sharedPreferencesUtil.loadInteger(BaseConfig.FLAG_READING_SCREEN_ORIENTATION, 3);

        if (screen_orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Constants.LANDSCAPE = false;
        } else if (screen_orientation == Configuration.ORIENTATION_LANDSCAPE && this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Constants.LANDSCAPE = true;
        }
    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) {
            readStatus.book = (Book) savedInstanceState.getSerializable("book");
            readStatus.offset = savedInstanceState.getInt("offset", 0);
            readStatus.sequence = savedInstanceState.getInt("sequence", 0);
            if (readDataFactory == null) {
                readDataFactory = new ReadDataFactory(this, this, readStatus, novelHelper);
                readDataFactory.setReadDataListener(this);
            }
            readDataFactory.currentChapter = (Chapter) savedInstanceState.getSerializable("currentChapter");
        } else {

            readStatus.sequence = bundle.getInt("sequence", 0);
            readStatus.offset = bundle.getInt("offset", 0);
            readStatus.book = (Book) bundle.getSerializable("book");
            readStatus.book_id = (readStatus.book == null ? "" : readStatus.book.id);

            if (bundle.getBoolean("need_update") && !TextUtils.isEmpty(readStatus.book_id)) {
                if (bookDaoHelper == null) {
                    bookDaoHelper = BookDaoHelper.getInstance(getApplicationContext());
                }

                Book book = bookDaoHelper.loadBook(readStatus.book_id, Book.TYPE_ONLINE);
                if (book != null) {
                    book.chapters_update_state = 1;
                    bookDaoHelper.updateBook(book);
                }
            }
        }
        if (readStatus.sequence == -2) {
            readStatus.sequence = -1;
        }
    }

    /**
     * 处理书籍状态
     */
    private void initBookState() {

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(getApplicationContext());
        }

        readStatus.book_id = readStatus.book.id;

        subscribe = bookDaoHelper.subscribeBook(readStatus.book_id);

        if (subscribe) {
            readStatus.book = bookDaoHelper.loadBook(readStatus.book_id, readStatus.book.book_type);
        }

        if (readStatus.sequence < -1) {
            readStatus.sequence = -1;
        } else if (subscribe && readStatus.sequence + 1 > readStatus.book.chapter.sn) {
            readStatus.sequence = readStatus.book.chapter.sn - 1;
        }
    }


    /**
     * 初始化view
     */
    private void initView() {
        reading_content = (RelativeLayout) findViewById(R.id.reading_content);

        read_bookmark = (ImageView) findViewById(R.id.read_bookmark);
        read_bookmark_arrow = (ImageView) findViewById(R.id.read_bookmark_arrow);
        read_bookmark_prompt = (TextView) findViewById(R.id.read_bookmark_prompt);
        read_bookmark_added = (ImageView) findViewById(R.id.read_bookmark_added);

        if (BaseConfig.READING_FLIP_UP_DOWN) {
            read_bookmark.setVisibility(View.GONE);
        } else {
            read_bookmark.setVisibility(View.VISIBLE);
        }

        read_base_view = (FrameLayout) findViewById(R.id.read_base_view);
        readStatus.novel_basePageView = read_base_view;

        if (BaseConfig.READING_FLIP_UP_DOWN) {
            read_page = new ScrollPageView(getApplicationContext());
        } else {
            read_page = new PageView(getApplicationContext());
        }

        read_base_view.addView((View) read_page, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        read_page.init(this, readStatus, novelHelper);
        read_page.setCallBack(this);
        read_page.setReadFactory(readDataFactory);
        novelHelper.setPageView(read_page);
        readDataFactory.setPageView(read_page);


        read_head_options = (RelativeLayout) findViewById(R.id.read_head_options);
        read_head_options.setVisibility(View.GONE);

        read_head_options_back = (ImageView) findViewById(R.id.read_head_options_back);
        read_head_options_bookmark = (ImageView) findViewById(R.id.read_head_options_bookmark);

        read_head_options_comment = (RelativeLayout) findViewById(R.id.read_head_options_comment);
        read_head_options_comment_image = (ImageView) findViewById(R.id.read_head_options_comment_image);
        read_head_options_comment_count = (TextView) findViewById(R.id.read_head_options_comment_count);
        read_head_options_comment.setVisibility(View.GONE);

        read_head_options_download = (ImageView) findViewById(R.id.read_head_options_download);


        read_setting_view = (ReadSettingView) findViewById(R.id.read_setting_view);
        if (read_setting_view != null) {
            read_setting_view.setReadSettingListener(this);
            read_setting_view.setDataFactory(readDataFactory, readStatus);
            read_setting_view.initializeView(this);
        }

        menuDownInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_push_down_in);
        menuDownInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (read_head_options != null) {
                    read_head_options.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        menuUpOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_push_up_out);
        menuUpOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (read_head_options != null) {
                    read_head_options.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        changeMode(BaseConfig.READING_BACKGROUND_MODE);

        initOwnNativeAD();

        layout_read_guide = (ViewStub) findViewById(R.id.layout_read_guide);
        layout_read_bookmark_guide = (ViewStub) findViewById(R.id.layout_read_bookmark_guide);
        layout_read_catalog_guide = (ViewStub) findViewById(R.id.layout_read_catalog_guide);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        read_head_options_back.setOnClickListener(this);
        read_head_options_bookmark.setOnClickListener(this);
        read_head_options_comment.setOnClickListener(this);
        read_head_options_download.setOnClickListener(this);
    }

    /**
     * 获取书籍内容
     */
    private void loadBookContent() {
        readDataFactory.getChapterByLoading(MESSAGE_LOAD_CURRENT_CHAPTER, readStatus.sequence);
    }

    protected void setReadingMode() {
        if (read_setting_view != null) {
            read_setting_view.resetNightMode();
        }
    }

    private void initOwnNativeAD() {
        if (ownNativeAdManager == null) {
            ownNativeAdManager = OwnNativeAdManager.getInstance(this);
        }
    }

    /**
     * 初始化时间显示
     */
    private void initTime() {
        timer_stopped = false;
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        ticker = new Runnable() {
            public void run() {
                if (timer_stopped || read_page == null) {
                    return;
                }
                calendar.setTimeInMillis(System.currentTimeMillis());
                try {
                    if (read_page != null) {
                        time_text = DateFormat.format(mFormat, calendar);
                        read_page.freshTime(time_text);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long now = SystemClock.uptimeMillis();
                long next = now + (30000 - now % 1000);
                timeHandler.postAtTime(ticker, next);
            }
        };
        ticker.run();
    }

    /**
     * 刷新页面
     */
    private void refreshPage() {
        readStatus.draw_foot_view = (readStatus.sequence != -1);
    }

    /**
     * 预加载 5章
     */
    private void downloadNovel() {

        if (readStatus.book.book_type != Book.TYPE_ONLINE) {
            return;
        }

        if (novelDownloader != null && novelDownloader.getStatus() == BaseAsyncTask.Status.RUNNING) {
            novelDownloader.cancel(true);
        }
        int num = BookHelper.CHAPTER_CACHE_COUNT;
        int max = (readStatus.chapter_count - 1) - readStatus.sequence;
        if (max > 0) {
            if (max < num) {
                num = max;
            }
            novelDownloader = new NovelDownloader();
            novelDownloader.execute2(num);
        }
    }

    /**
     * 打开目录页面
     */
    private void openCategoryPage() {
        if (readStatus.menu_show) {
            showMenu(false);
        }
        if (novelDownloader != null && novelDownloader.getStatus() == BaseAsyncTask.Status.RUNNING) {
            novelDownloader.cancel(true);
        }

        Intent intent = new Intent(ReadingActivity.this, ReadingCatalogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", readStatus.book);
        bundle.putString("book_id", readStatus.book_id);
        bundle.putInt("sequence", readStatus.sequence);
        bundle.putBoolean("fromCover", true);
        intent.putExtras(bundle);

        startActivity(intent);

        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_head_options_back:
                subscribe = bookDaoHelper.subscribeBook(readStatus.book_id);
                if (!subscribe) {
                    showAddShelfDialogFragment();
                    return;
                }
                if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.read_head_options_bookmark:
                if (!bookDaoHelper.subscribeBook(readStatus.book_id)) {
                    if (bookDaoHelper.insertBook(readStatus.book) == 1) {
                        setResult(RESULT_OK);
                    }
                }
                int result = novelHelper.addOptionMark(bookDaoHelper, readDataFactory, read_head_options_bookmark, font_count);
                StatServiceUtils.statReadPageMark(ReadingActivity.this);
                refreshBookMark();
                switch (result) {
                    case 0:
                        showToast("书签添加失败");
                        break;
                    case 1:
                        subscribe = true;
                        showToast("书签添加成功");
                        break;
                    case 2:
                        showToast("书签已删除");
                        break;
                    default:
                        break;
                }

                if (sharedPreferencesUtil == null) {
                    sharedPreferencesUtil = loadSharedPreferencesUtil();
                }

                if (!sharedPreferencesUtil.loadBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_BOOKMARK_GUIDE, false)) {
                    showBookmarkGuide();
                }

                break;
            case R.id.read_head_options_comment:
                Intent intent = new Intent();
                intent.putExtra("book", readStatus.book);
                intent.putExtra("id_book", readStatus.book_id);
                intent.setClass(ReadingActivity.this, CommentListActivity.class);
                startActivity(intent);
                break;
            case R.id.read_head_options_download:
                checkDownloadState();
                break;
            default:
                break;
        }
    }

    private void showBookmarkGuide() {
        if (read_bookmark_guide_view == null) {
            read_bookmark_guide_view = layout_read_bookmark_guide.inflate();
        }

        if (read_bookmark_guide_view != null) {
            read_bookmark_guide_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (read_bookmark_guide_view != null) {
                        read_bookmark_guide_view.setVisibility(View.GONE);

                        if (sharedPreferencesUtil == null) {
                            sharedPreferencesUtil = loadSharedPreferencesUtil();
                        }
                        sharedPreferencesUtil.insertBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_BOOKMARK_GUIDE, true);
                    }
                }
            });
        }
    }

    public void setReaderDisplayBrightness() {
        super.loadBrightnessValue();
    }

    /**
     * 跳章
     */
    public void jumpChapterCallBack() {
        if (readDataFactory == null || readStatus == null || novelHelper == null) {
            return;
        }
        readDataFactory.nextChapter = null;
        readStatus.sequence = readStatus.novel_progress;
        readStatus.offset = 0;
        novelHelper.isShown = false;
        novelHelper.getChapterContent(getApplicationContext(), this, readDataFactory.currentChapter, readStatus.book, false);
        readStatus.current_page = 1;
        refreshPage();
        if (BaseConfig.READING_FLIP_UP_DOWN) {
            read_page.getChapter(false);
        } else {
            read_page.drawCurrentPage();
            read_page.drawNextPage();
        }
        downloadNovel();
    }

    /**
     * 隐藏头部
     **/
    public void hideHeadView() {
        if (menuUpOutAnimation != null && read_head_options != null && read_head_options.isShown()) {
            read_head_options.startAnimation(menuUpOutAnimation);
        }
    }

    /**
     * 显示头部
     **/
    private void showMenu(boolean isShow) {
        if (read_setting_view == null || read_page == null) {
            return;
        }

        if (isShow) {
            read_setting_view.showSettingView(true);

            initializeHeadView();

            checkBookmarkState();

            if (menuUpOutAnimation != null && read_head_options != null) {
                read_head_options.startAnimation(menuDownInAnimation);
            }

            readStatus.menu_show = true;
        } else {
            read_setting_view.showSettingView(false);
            readStatus.menu_show = false;
            if (menuUpOutAnimation != null && read_head_options != null && read_head_options.isShown()) {
                read_head_options.startAnimation(menuUpOutAnimation);
                readStatus.menu_show = false;
            }
        }
    }

    private void checkBookmarkState() {
        if (read_head_options_bookmark != null) {

            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }

            boolean state = BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6;

            if (bookDaoHelper.checkBookmarkExist(readStatus.book_id, readStatus.sequence, readStatus.offset)) {

                if (state) {
                    read_head_options_bookmark.setImageResource(R.drawable.selector_dark_bookmark_remove);
                } else {
                    read_head_options_bookmark.setImageResource(R.drawable.selector_light_bookmark_remove);
                }
            } else {

                if (state) {
                    read_head_options_bookmark.setImageResource(R.drawable.selector_dark_bookmark_insert);
                } else {
                    read_head_options_bookmark.setImageResource(R.drawable.selector_light_bookmark_insert);
                }
            }
        }
    }

    // 全屏切换
    private void full(boolean enable) {
        if (!Constants.FULL_WINDOW_READ) {
            return;
        }
        if (enable) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    /**
     * 切换夜间模式
     */
    private void changeMode(int mode) {

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        if (mode == 6) {
            if ("light".equals(ResourceUtil.mode)) {
                sharedPreferencesUtil.insertString(BaseConfig.FLAG_READING_NIGHT_MODE, "night");
                ResourceUtil.mode = "night";
                setReadingMode();
            }
        } else {
            if ("night".equals(ResourceUtil.mode)) {
                sharedPreferencesUtil.insertString(BaseConfig.FLAG_READING_NIGHT_MODE, "light");
                ResourceUtil.mode = "light";
                setReadingMode();
            }
        }
        if (mode == 1) {
            setTextColor(Color.parseColor("#504841"));
            setPageBackColor(Color.parseColor("#F9F1EA"));
        } else if (mode == 2) {
            setTextColor(Color.parseColor("#3A3A3A"));
            setPageBackColor(Color.parseColor("#F6F6F8"));
        } else if (mode == 3) {
            setTextColor(Color.parseColor("#222224"));
            setPageBackColor(Color.parseColor("#F4EFD9"));
        } else if (mode == 4) {
            setTextColor(Color.parseColor("#333B33"));
            setPageBackColor(Color.parseColor("#CBEDCB"));
        } else if (mode == 5) {
            setTextColor(Color.parseColor("#9499A3"));
            setPageBackColor(Color.parseColor("#2A3449"));
        } else if (mode == 6) {
            setTextColor(Color.parseColor("#808082"));
            setPageBackColor(Color.parseColor("#1D1D1F"));
        }
        setBackground();
    }

    private void setTextColor(int color) {
        read_page.setTextColor(color);
    }

    private void setBackground() {
        read_page.setBackground();
    }

    private void setPageBackColor(int color) {
        read_page.setPageBackColor(color);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 小说音量键翻页
        if (Constants.VOLUME_TURNOVER) {
            if (read_page != null && read_page.setKeyEvent(event)) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (readStatus.menu_show) {
                showMenu(false);
            } else {
                showMenu(true);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // 显示菜单
        if (readStatus.menu_show) {
            showMenu(false);
            return;
        }
        subscribe = bookDaoHelper.subscribeBook(readStatus.book_id);
        if (!subscribe) {
            showAddShelfDialogFragment();
            return;
        }
        if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
        if (!isFinishing()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        StatService.onResume(this);

        if (!Constants.FULL_WINDOW_READ) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (FROM_COVER && Constants.LANDSCAPE) {
            return;
        }

        if (subscribe) {
            readStatus.book = bookDaoHelper.loadBook(readStatus.book_id, 0);
        }

        if (readStatus.book.chapter != null) {
            readStatus.chapter_count = readStatus.book.chapter.sn;
        }

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        if (!sharedPreferencesUtil.loadBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_GUIDE, false)) {
            showGuide();
        }

        if (!BaseConfig.READING_FLIP_UP_DOWN) {
            refreshBookMark();
            can_consume_event = false;
        }
    }

    private void showGuide() {
        if (read_guide_view == null) {
            read_guide_view = layout_read_guide.inflate();
        }

        if (read_guide_view != null) {
            read_guide_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (read_guide_view != null) {
                        read_guide_view.setVisibility(View.GONE);

                        if (sharedPreferencesUtil == null) {
                            sharedPreferencesUtil = loadSharedPreferencesUtil();
                        }

                        sharedPreferencesUtil.insertBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_GUIDE, true);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        StatService.onPause(this);

        FROM_COVER = false;

        if (subscribe) {
            novelHelper.saveBookmark(readDataFactory.chapterList, readStatus.book_id, readStatus.sequence, readStatus.offset, bookDaoHelper);
//            sharedPreferences.edit().putInt("readed_count", Constants.readedCount).apply();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        unRegisterDownloadReceiver();

        readStatus.menu_show = false;
        if (novelDownloader != null && novelDownloader.getStatus() == BaseAsyncTask.Status.RUNNING) {
            novelDownloader.cancel(true);
        }

        timer_stopped = true;

        if (read_page != null) {
            read_page.setCallBack(null);
            read_page.recyclerData();
            read_page = null;
        }

        if (novelHelper != null) {
            novelHelper.clear();
        }


        if (handler != null) {
            handler.removeMessages(MESSAGE_LOAD_CURRENT_CHAPTER);
        }

        try {
            setContentView(R.layout.layout_activity_empty);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


        if (read_base_view != null) {
            read_base_view.removeAllViews();
            read_base_view = null;
        }

        if (read_setting_view != null) {
            read_setting_view.setReadSettingListener(null);
            read_setting_view.recycleResource();
            read_setting_view = null;
        }

        if (reading_content != null) {
            reading_content.removeAllViews();
            reading_content = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 保存书签状态
        outState.putInt("sequence", readStatus.sequence);
        outState.putInt("offset", readStatus.offset);
        outState.putSerializable("book", readStatus.book);
        outState.putSerializable("currentChapter", readDataFactory.currentChapter);
        try {
            super.onSaveInstanceState(outState);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void goToBookOver() {
        if (isFinishing()) {
            return;
        }

        Intent intent = new Intent(ReadingActivity.this, ReadEndActivity.class);
        intent.putExtra("Book", readStatus.book);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.READING_PAGE) {
                if (bookDaoHelper.subscribeBook(readStatus.book_id)) {
                    readStatus.book = bookDaoHelper.loadBook(readStatus.book_id, 0);
                    bookDaoHelper.updateBook(readStatus.book);
                }
            } else if (data != null) {
                if (requestCode == 1) {
                    showMenu(false);
                }
            }
        }
    }

    public void addBookShelf(boolean isAddShelf) {
        if (isAddShelf && bookDaoHelper != null && readStatus != null && readStatus.book != null) {

            Book book = readStatus.book;
            book.offset = readStatus.offset;
            book.sequence = readStatus.sequence;

            int result = bookDaoHelper.insertBook(book);

            Toast.makeText(ReadingActivity.this, result == 1 ? "已添入书架" : "抱歉,添加书架失败,请重试", Toast.LENGTH_SHORT).show();
        }
        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onShowMenu(boolean isShow) {
        QGLog.e(TAG, "onShowMenu : " + isShow);
        showMenu(isShow);
    }

    @Override
    public void onCancelPage() {
        readDataFactory.restore();
        refreshPage();
    }

    @Override
    public void onResize() {
        if (readDataFactory.currentChapter != null && readStatus.book != null) {
            novelHelper.getChapterContent(getApplicationContext(), this, readDataFactory.currentChapter, readStatus.book, true);
            refreshPage();
        }
    }

    @Override
    public void refreshBookMark() {

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        if (bookDaoHelper.checkBookmarkExist(readStatus.book_id, readStatus.sequence, readStatus.offset)) {
            is_add_bookmark = true;
            changeBookMarkSource();
        } else {
            is_add_bookmark = false;
            changeBookMarkSource();
        }
    }

    @Override
    public void refreshHideView(int position) {
        if (is_add_bookmark) {
            if (position < 100) {
                read_bookmark.setImageResource(R.drawable.icon_read_bookmark_added);
                read_bookmark_added.setImageResource(R.drawable.icon_read_bookmark_added);
                read_bookmark_arrow.setImageResource(R.drawable.icon_read_page_down);
                read_bookmark_prompt.setText(R.string.read_delete_bookmark_down);
                can_consume_event = false;
            } else if (position >= 100) {
                read_bookmark.setImageResource(R.drawable.icon_read_bookmark_add);
                read_bookmark_added.setImageResource(R.drawable.icon_read_bookmark_add);
                read_bookmark_arrow.setImageResource(R.drawable.icon_read_page_up);
                read_bookmark_prompt.setText(R.string.read_delete_bookmark_up);
                can_consume_event = true;
            }
        } else {
            if (position < 100) {
                read_bookmark.setImageResource(R.drawable.icon_read_bookmark_add);
                read_bookmark_added.setImageResource(R.drawable.icon_read_bookmark_add);
                read_bookmark_arrow.setImageResource(R.drawable.icon_read_page_down);
                read_bookmark_prompt.setText(R.string.read_add_bookmark_down);
                can_consume_event = false;
            } else if (position >= 100) {
                read_bookmark.setImageResource(R.drawable.icon_read_bookmark_added);
                read_bookmark_added.setImageResource(R.drawable.icon_read_bookmark_added);
                read_bookmark_arrow.setImageResource(R.drawable.icon_read_page_up);
                read_bookmark_prompt.setText(R.string.read_add_bookmark_up);
                can_consume_event = true;
            }
        }
    }

    @Override
    public void changeBookMarkDB() {
        if (can_consume_event) {
            if (is_add_bookmark) {

                if (bookDaoHelper == null) {
                    bookDaoHelper = BookDaoHelper.getInstance(this);
                }

                bookDaoHelper.deleteBookmark(readStatus.book_id, readStatus.sequence, readStatus.offset);
                is_add_bookmark = false;
                changeBookMarkSource();

            } else {
                if (bookDaoHelper == null) {
                    bookDaoHelper = BookDaoHelper.getInstance(this);
                }

                if (!bookDaoHelper.checkBookmarkExist(readStatus.book_id, readStatus.sequence, readStatus.offset)) {

                    if (!bookDaoHelper.subscribeBook(readStatus.book_id)) {
                        if (bookDaoHelper.insertBook(readStatus.book) == 1) {
                            setResult(RESULT_OK);
                        }
                    }

                    Bookmark bookmark = new Bookmark();
                    bookmark.book_id = readStatus.book.id;
                    bookmark.sequence = readStatus.sequence + 1 > readStatus.chapter_count ? readStatus.chapter_count : readStatus.sequence;
                    bookmark.offset = readStatus.offset;
                    bookmark.insert_time = System.currentTimeMillis();
                    bookmark.chapter_name = readDataFactory.currentChapter.name;
                    List<String> content = getPageContent();
                    StringBuilder sb = new StringBuilder();
                    if (readStatus.sequence == -1) {
                        bookmark.chapter_name = "《" + readStatus.book.name + "》书籍封面页";
                    } else if (readStatus.current_page == 1 && content.size() - 3 >= 0) {
                        for (int i = 3; i < content.size(); i++) {
                            sb.append(content.get(i));
                        }
                    } else {
                        for (int i = 0; i < content.size(); i++) {
                            sb.append(content.get(i));
                        }
                    }

                    // 去除第一个字符为标点符号的情况
                    String content_text = sb.toString().trim();
                    content_text = content_text.trim();

                    content_text = StringUtils.deleteTextPoint(content_text);
                    // 控制字数
                    if (content_text.length() > font_count) {
                        content_text = content_text.substring(0, font_count);
                    }
                    bookmark.chapter_content = content_text;

                    StatServiceUtils.statReadPageMark(ReadingActivity.this);

                    bookDaoHelper.insertBookmark(bookmark);

                    is_add_bookmark = true;

                    changeBookMarkSource();
                }
            }
        }

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        if (!sharedPreferencesUtil.loadBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_BOOKMARK_GUIDE, false)) {
            sharedPreferencesUtil.insertBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_BOOKMARK_GUIDE, true);
        }
    }

    public synchronized List<String> getPageContent() {
        if (readStatus.line_list == null) {
            return null;
        }
        if (readStatus.current_page == 0) {
            readStatus.current_page = 1;
        }
        if (readStatus.current_page > readStatus.page_count) {
            readStatus.current_page = readStatus.page_count;
        }
        readStatus.offset = 0;
        ArrayList<String> pageContent;
        if (readStatus.current_page - 1 < readStatus.line_list.size()) {
            pageContent = readStatus.line_list.get(readStatus.current_page - 1);
        } else {
            pageContent = new ArrayList<>();
        }

        for (int i = 0; i < readStatus.current_page - 1 && i < readStatus.line_list.size(); i++) {
            ArrayList<String> pageList = readStatus.line_list.get(i);
            final int size = pageList.size();
            for (int j = 0; j < size; j++) {
                String string = pageList.get(j);
                if (!TextUtils.isEmpty(string) && !string.equals(" ")) {
                    readStatus.offset += string.length();
                }
            }
        }
        readStatus.offset++;

        return pageContent;
    }

    private void changeBookMarkSource() {
        QGLog.e(TAG, "is_add_bookmark: " + is_add_bookmark);
        if (!BaseConfig.READING_FLIP_UP_DOWN) {
            if (is_add_bookmark) {
                if (read_bookmark != null) {
                    read_bookmark.setImageResource(R.drawable.icon_read_bookmark_added);
                }

                if (read_bookmark_added != null) {
                    read_bookmark_added.setVisibility(View.VISIBLE);
                    read_bookmark_added.setImageResource(R.drawable.icon_read_bookmark_added);
                }

                if (read_bookmark_arrow != null) {
                    read_bookmark_arrow.setImageResource(R.drawable.icon_read_page_down);
                }

                if (read_bookmark_prompt != null) {
                    read_bookmark_prompt.setText(R.string.read_delete_bookmark_down);
                }
            } else {

                if (read_bookmark != null) {
                    read_bookmark.setImageResource(R.drawable.icon_read_bookmark_add);
                }

                if (read_bookmark_added != null) {
                    read_bookmark_added.setVisibility(View.INVISIBLE);
                    read_bookmark_added.setImageResource(R.drawable.icon_read_bookmark_add);
                }

                if (read_bookmark_arrow != null) {
                    read_bookmark_arrow.setImageResource(R.drawable.icon_read_page_down);
                }

                if (read_bookmark_prompt != null) {
                    read_bookmark_prompt.setText(R.string.read_add_bookmark_down);
                }
            }
        }
    }

    @Override
    public void openCatalog() {
        openCategoryPage();

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        if (!sharedPreferencesUtil.loadBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_CATEGORY_GUIDE, false)) {
            sharedPreferencesUtil.insertBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_CATEGORY_GUIDE, true);
        }
    }

    @Override
    public void freshPage() {
        refreshPage();
    }

    @Override
    public void gotoOver() {
        goToBookOver();
    }

    @Override
    public void showReadToast(String message) {
        showToast(message);
    }

    @Override
    public void downLoadNovelMore() {
        downloadNovel();
    }

    @Override
    public void initBookStateDeal() {
        if (read_setting_view != null) {
            read_setting_view.initializeBackground();
        }
        refreshPage();

        initTime();

        downloadNovel();

        loadBookCommentCount();

        if (readStatus != null && readStatus.book != null) {
            if (readStatus.book.book_type == 0) {
                read_head_options_comment.setVisibility(View.VISIBLE);
                read_head_options_download.setVisibility(View.VISIBLE);
                loadBookCommentCount();
            } else {
                read_head_options_comment.setVisibility(View.GONE);
                read_head_options_download.setVisibility(View.GONE);
            }
        }
    }

    private void pauseAutoReadHandler() {
    }

    private void resumeAutoReadHandler() {
    }

    @Override
    public void changeChapter() {
        if (read_setting_view != null) {
            read_setting_view.changeChapter();
        }
        checkBookmarkState();
    }


    private void showCatalogGuide() {
        if (read_catalog_guide_view == null) {
            read_catalog_guide_view = layout_read_catalog_guide.inflate();
        }

        if (read_catalog_guide_view != null) {
            read_catalog_guide_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (read_catalog_guide_view != null) {
                        read_catalog_guide_view.setVisibility(View.GONE);
                        openCategoryPage();

                        if (sharedPreferencesUtil == null) {
                            sharedPreferencesUtil = loadSharedPreferencesUtil();
                        }

                        sharedPreferencesUtil.insertBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_CATEGORY_GUIDE, true);
                    }
                }
            });
        }
    }

    public void addTextLength(int length) {
        readLength += length;
    }


    @Override
    public void onBackgroundModeChanged(int mode) {
        changeMode(mode);

        initializeHeadView();
    }

    @Override
    public void onTurnToPreviousChapter() {
        readStatus.current_page = 1;
        readDataFactory.toChapterStart = true;
        readDataFactory.previous();
        if (BaseConfig.READING_FLIP_UP_DOWN) {
            read_page.getChapter(false);
        } else {
            read_page.drawCurrentPage();
            read_page.drawNextPage();
        }
        checkBookmarkState();
    }

    @Override
    public void onTurnToNextChapter() {
        readStatus.current_page = readStatus.page_count;

        readDataFactory.next();

        if (BaseConfig.READING_FLIP_UP_DOWN) {
            read_page.getChapter(false);
        } else {
            read_page.drawCurrentPage();
            read_page.drawNextPage();
        }

        checkBookmarkState();
    }

    @Override
    public void onTurnToChapter() {
        readDataFactory.getChapterByLoading(MESSAGE_LOAD_JUMP_CHAPTER, readStatus.novel_progress);
    }

    @Override
    public void startCatalogActivity() {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = loadSharedPreferencesUtil();
        }

        boolean guide = sharedPreferencesUtil.loadBoolean(QuApplication.getVersionCode() + BaseConfig.FLAG_READING_CATEGORY_GUIDE, false);

        if (!guide) {
            showCatalogGuide();
        } else {
            openCategoryPage();
        }
    }

    @Override
    public void refreshReadingPage() {
        if (read_page instanceof ScrollPageView && ((ScrollPageView) read_page).tempChapter != null) {
            novelHelper.getChapterContent(getApplicationContext(), this, ((ScrollPageView) read_page).tempChapter, readStatus.book, true);
        } else {
            novelHelper.getChapterContent(getApplicationContext(), this, readDataFactory.currentChapter, readStatus.book, true);
        }

        refreshPage();
        read_page.drawCurrentPage();
        read_page.drawNextPage();
        read_page.getChapter(true);
    }

    @Override
    public void onTypefaceChanged(Typeface typeface) {
        read_page.setTypeFace(typeface);
    }

    static class UiHandler extends Handler {
        private WeakReference<ReadingActivity> actReference;

        UiHandler(ReadingActivity act) {
            actReference = new WeakReference<>(act);
        }

        public void handleMessage(android.os.Message msg) {
            ReadingActivity activity = actReference.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    activity.pauseAutoReadHandler();
                    break;
                case 2:
                    activity.resumeAutoReadHandler();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        }
    }

    private class NovelDownloader extends BaseAsyncTask<Integer, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            if (isCancelled() || isFinishing())
                return;
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            if (readDataFactory.chapterList == null) {
                return null;
            }
            int size = readDataFactory.chapterList.size();
            for (int i = readStatus.sequence + 1; i < (readStatus.sequence + params[0] + 1) && i < size; i++) {

                final Chapter chapter = readDataFactory.chapterList.get(i);

                if (chapter != null && !TextUtils.isEmpty(chapter.id)) {
                    String content = FileUtil.loadChapterFromCache(chapter.id, readStatus.book_id);

                    if (!TextUtils.isEmpty(content) && !("null".equals(content))) {
                        if (!TextUtils.equals(chapter.status, BaseConfig.ENABLE)) {
                            chapter.content = BaseConfig.DISABLE_MESSAGE;
                        }
                        if (!TextUtils.isEmpty(content)) {
                            chapter.content = content;
                        }
                        if (TextUtils.isEmpty(content)) {
                            chapter.content = BaseConfig.ERROR;
                        }
                    } else {
                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
                        dataRequestService.loadChapter(chapter.id)
                                .subscribeOn(Schedulers.trampoline())
                                .observeOn(Schedulers.trampoline())
                                .subscribe(new ResourceSubscriber<CommunalResult<Chapter>>() {
                                    @Override
                                    public void onNext(CommunalResult<Chapter> chapterResult) {
                                        Logger.d("LoadChapter onNext");

                                        if (chapterResult != null) {
                                            if (chapterResult.getCode() == 0 && chapterResult.getModel() != null) {

                                                Chapter chapterObject = chapterResult.getModel();

                                                String chapterContent = chapterObject.content;

                                                if (!TextUtils.equals(chapterObject.status, BaseConfig.ENABLE)) {
                                                    chapterObject.content = BaseConfig.DISABLE_MESSAGE;
                                                } else if (TextUtils.isEmpty(chapterContent) || ("null".equals(chapterContent))) {
                                                    chapterObject.content = BaseConfig.ERROR;
                                                } else {
                                                    ChapterDao chapterDao = new ChapterDao(ReadingActivity.this, readStatus.book_id);
                                                    chapterDao.updateChapter(chapterObject);
                                                    FileUtil.saveChapterToCache(chapterContent, chapterObject.id, readStatus.book_id);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("LoadChapter onError: " + throwable.toString());
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("LoadChapter onComplete");
                                    }
                                });
                    }
                }

                if (i == (readStatus.sequence + 1) && chapter != null) {
                    readDataFactory.nextChapter = chapter;
                }
                if (isCancelled()) {
                    break;
                }
            }
            return null;
        }
    }

    private void loadBookCommentCount() {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.loadBookCommentsCount(readStatus.book_id, 1, "count")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<CommentList>>() {
                    @Override
                    public void onNext(CommunalResult<CommentList> countResult) {
                        Logger.d("LoadBookCommentsCount onNext");
                        if (countResult != null && countResult.getCode() == 0 && countResult.getModel() != null) {
                            setCommentCount(countResult.getModel().count);

                        } else {
                            setCommentCount(0);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadBookCommentsCount onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadBookCommentsCount onComplete");
                    }
                });

    }

    private void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!isFinishing() && toast != null) {
            toast.show();
        }
    }

    private void setCommentCount(int count) {
        if (count == 0) {
            read_head_options_comment_count.setVisibility(View.GONE);
        } else {
            if (count > 99) {
                read_head_options_comment_count.setText(MessageFormat.format("{0}+", 99));
                read_head_options_comment_count.setBackgroundResource(R.drawable.icon_comment_count_ellipse);
            } else {
                read_head_options_comment_count.setText(String.valueOf(count));
                read_head_options_comment_count.setBackgroundResource(R.drawable.icon_comment_count_circle);
            }
            read_head_options_comment_count.setVisibility(View.VISIBLE);
        }
    }

    private void showAddShelfDialogFragment() {
        if (isFinishing()) {
            return;
        }
        if (TimeUtils.getBooleanPreferences(this, "exit_hint", false)) {
            addBookShelf(false);
            return;
        }

        if (customDialogFragment == null) {
            customDialogFragment = new CustomDialogFragment();
        }

        customDialogFragment.setPrompt("喜欢就加入书架！");
        customDialogFragment.setFirstOption("加入书架");
        customDialogFragment.setSecondOption("取消");
        customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                hideCustomDialogFragment();
                addBookShelf(true);
            }

            @Override
            public void onSecondOptionClicked() {
                hideCustomDialogFragment();
                addBookShelf(false);
            }
        });

        if (!isFinishing()) {
            if (customDialogFragment.isAdded()) {
                customDialogFragment.setShowsDialog(true);
            } else {
                customDialogFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
            }
        }
    }

    public void hideCustomDialogFragment() {
        if (!isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }

    private void initializeHeadView() {

        boolean state = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

        if (state) {
            read_head_options.setBackgroundColor(Color.parseColor("#252424"));
        } else {
            read_head_options.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if (state) {
            read_head_options_back.setImageResource(R.drawable.selector_dark_back);
        } else {
            read_head_options_back.setImageResource(R.drawable.selector_light_back);
        }

        if (state) {
            read_head_options_comment_image.setImageResource(R.drawable.selector_dark_comment);
        } else {
            read_head_options_comment_image.setImageResource(R.drawable.selector_light_comment);
        }

        if (state) {
            read_head_options_download.setImageResource(R.drawable.selector_dark_download);
        } else {
            read_head_options_download.setImageResource(R.drawable.selector_light_download);
        }

        checkBookmarkState();
    }


    public class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_START)) {
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id)) {
                    onDownloadStart(id);
                }
            }
            if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_FINISH)) {
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id)) {
                    onDownloadFinish(id);
                }
            } else if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_FAILED)) {
                int sequence = intent.getIntExtra("sequence", 0);
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id) && sequence != 0) {
                    onDownloadFiled(id);
                }
            }
        }
    }


    private void registerDownloadReceiver() {
        if (downloadReceiver == null && !register) {
            downloadReceiver = new DownloadReceiver();

            IntentFilter filter = new IntentFilter();
            filter.addAction(DownloadService.DOWNLOAD_SERVICE_START);
            filter.addAction(DownloadService.DOWNLOAD_SERVICE_FINISH);
            filter.addAction(DownloadService.DOWNLOAD_SERVICE_FAILED);

            registerReceiver(downloadReceiver, filter);

            register = true;
        }
    }


    private void unRegisterDownloadReceiver() {
        if (downloadReceiver != null && register) {
            try {
                unregisterReceiver(downloadReceiver);
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
                collectException(exception);
            }
        }
    }

    private void onDownloadStart(String id) {
        if (!TextUtils.isEmpty(id) && readStatus.book != null && id.equals(readStatus.book.id)) {
            DownloadTask downloadTask = downloadService.getDownloadTask(id);
            if (null != downloadTask && downloadTask.downloadState == DownloadState.DOWNLOADING) {
                initDownloadState(DownloadState.DOWNLOADING);
            }
        }
    }

    private void onDownloadFinish(String id) {
        if (!TextUtils.isEmpty(id) && readStatus.book != null && id.equals(readStatus.book.id)) {
            DownloadTask downloadTask = downloadService.getDownloadTask(id);
            if (null != downloadTask && downloadTask.downloadState == DownloadState.FINISH) {
                initDownloadState(DownloadState.FINISH);
                showPromptMessage("《" + readStatus.book.name + "》缓存完成");
            }
        }
    }

    private void onDownloadFiled(String id) {
        if (!TextUtils.isEmpty(id) && readStatus.book != null && id.equals(readStatus.book.id)) {
            DownloadTask downloadTask = downloadService.getDownloadTask(id);
            if (null != downloadTask && downloadTask.downloadState == DownloadState.REFRESH) {
                initDownloadState(DownloadState.REFRESH);
                showPromptMessage("《" + readStatus.book.name + "》缓存失败");
            }
        }
    }


    private void checkDownloadState() {
        if (readStatus.book == null || TextUtils.isEmpty(readStatus.book.id)) {
            return;
        }

        if (downloadService == null) {
            applicationUtil.bindDownloadService();
            downloadService = applicationUtil.getDownloadService();
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        if (!bookDaoHelper.subscribeBook(readStatus.book.id)) {
            bookDaoHelper.insertBook(readStatus.book);
        }

        int downIndex = DownloadServiceUtil.downloadStartIndex(this, readStatus.book.id);

        if (downloadService != null && readStatus.book != null) {

            DownloadTask downloadTask = downloadService.getDownloadTask(readStatus.book.id);

            if (downloadTask == null) {
                DownloadServiceUtil.addDownloadBookTask(this, readStatus.book, true);

                DownloadServiceUtil.startDownloadBookTask(downloadService, this, readStatus.book, downIndex);
                DownloadServiceUtil.writeDownloadIndex(this, readStatus.book.id, downIndex);
                showToast("开始下载！");
            } else {
                if (downloadTask.downloadState == DownloadState.NO_START || downloadTask.downloadState == DownloadState.PAUSED || downloadTask.downloadState == DownloadState.REFRESH) {

                    showReadToast("开始下载！");

                    downloadService.startDownloadTask(readStatus.book.id);
                } else {
                    if (downloadTask.downloadState == DownloadState.FINISH) {
                        showReadToast("下载完成！");
                    } else if (downloadTask.downloadState == DownloadState.DOWNLOADING) {
                        showReadToast("下载中！");
                    } else if (downloadTask.downloadState == DownloadState.WAITING) {
                        showReadToast("排队中！");
                    }
                }

                if (downloadTask.downloadState == DownloadState.NO_START) {
                    downloadService.startDownloadTask(readStatus.book.id);
                    showReadToast("暂未下载！");
                } else if (downloadTask.downloadState == DownloadState.PAUSED) {
                    showReadToast("下载已暂停！");
                } else if (downloadTask.downloadState == DownloadState.REFRESH) {
                    showReadToast("下载失败！");
                }
            }
        } else {
            showToast("缓存服务启动失败，请退出重试！");
        }
    }

    private void initDownloadState(DownloadState downloadState) {
        if (downloadState == DownloadState.NO_START || downloadState == DownloadState.PAUSED || downloadState == DownloadState.REFRESH) {
            if (read_head_options_download != null) {
                read_head_options_download.setSelected(true);
            }
        } else {
            if (read_head_options_download != null) {
                read_head_options_download.setSelected(false);
            }
        }

    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerReadingComponent.builder()
                .applicationComponent(applicationComponent)
                .readingModule(new ReadingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {

    }

    @Override
    public void recycle() {

    }
}