package com.quduquxie.read.local;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.database.dao.ChapterDao;

public class DecomposeCatalogDialog extends FrameLayout implements DecomposeCatalogMethod.OnDecomposeCatalogListener {
    private Context context;
    private ViewGroup viewGroup;

    private String id_book;
    private TextView local_book_decompose_prompt;

    public OnDecomposeSuccessListener decomposeSuccessListener;

    public DecomposeCatalogMethod decomposeCatalogMethod;

    public DecomposeCatalogDialog(Context context) {
        super(context);
    }

    public DecomposeCatalogDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DecomposeCatalogDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DecomposeCatalogDialog(Context context, String id_book) {
        super(context);
        this.id_book = id_book;
        this.context = context;

        this.viewGroup = ((ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
    }

    public void initCatalog() {
        Logger.e("initCatalog");
        ChapterDao bookChapterDao = new ChapterDao(context, id_book);
        if (bookChapterDao.loadChapterCount() > 0) {
            if (decomposeSuccessListener != null) {
                decomposeSuccessListener.onDecomposeSuccess();
            }
            dismiss();
        } else {
            decomposeCatalogMethod = new DecomposeCatalogMethod(context, id_book);
            decomposeCatalogMethod.addDecomposeCatalogListener(this);
            decomposeCatalogMethod.startDecomposeCatalogTask();
        }
    }

    private void initView() {
        Logger.e("initView");
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_local_book_decompose, viewGroup, false);
        addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        local_book_decompose_prompt = (TextView) findViewById(R.id.local_book_decompose_prompt);
        viewGroup.addView(this, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void refreshMessage(String message) {
        local_book_decompose_prompt.setText(message);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void dismiss() {
        setVisibility(View.GONE);
    }

    @Override
    public void updateProgress(int progress) {
        refreshMessage("正在扫描第" + progress + "章");
    }

    @Override
    public void notifySuccess(int state) {
        decomposeSuccessListener.onDecomposeSuccess();
        if (decomposeCatalogMethod != null) {
            decomposeCatalogMethod.removeDecomposeCatalogListener(this);
        }
        dismiss();
    }

    public void setDecomposeSuccessListener(OnDecomposeSuccessListener decomposeSuccessListener) {
        this.decomposeSuccessListener = decomposeSuccessListener;
    }

    public interface OnDecomposeSuccessListener {
        void onDecomposeSuccess();

    }
}
