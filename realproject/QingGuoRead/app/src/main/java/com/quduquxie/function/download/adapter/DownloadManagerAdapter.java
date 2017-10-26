package com.quduquxie.function.download.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.viewholder.ListEmptyFillHolder;
import com.quduquxie.function.download.listener.DownloadListener;
import com.quduquxie.function.download.viewholder.DownloadHolder;
import com.quduquxie.service.download.DownloadService;
import com.quduquxie.service.download.DownloadState;
import com.quduquxie.service.download.DownloadTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

public class DownloadManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Book> books;

    private DownloadService downloadService;

    private DownloadListener downloadListener;

    private LayoutInflater layoutInflater;

    private Typeface typeface;

    public static final int TYPE_EMPTY_FULL_20 = 0x80;

    public DownloadManagerAdapter(Context context, ArrayList<Book> books) {
        this.books = books;
        this.contextReference = new WeakReference<>(context);
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY_FULL_20:
                return new ListEmptyFillHolder(layoutInflater.inflate(R.layout.layout_view_empty_fill_20, parent, false));
            default:
                return new DownloadHolder(layoutInflater.inflate(R.layout.layout_item_download, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Book book = books.get(position);
        if (book == null) {
            return;
        }

        if (viewHolder instanceof DownloadHolder) {
            DownloadTask downloadTask = downloadService.getDownloadTask(book.id);
            if (downloadTask == null) {
                return;
            }

            ((DownloadHolder) viewHolder).download_serial_number.setText(String.valueOf(position + 1));

            ((DownloadHolder) viewHolder).download_book_name.setText(book.name);
            ((DownloadHolder) viewHolder).download_book_name.setTypeface(typeface);

            if (downloadTask.downloadState == DownloadState.FINISH) {
                ((DownloadHolder) viewHolder).download_prompt.setText("下载完成");
                ((DownloadHolder) viewHolder).download_prompt.setTextColor(Color.parseColor("#4D91D0"));
                ((DownloadHolder) viewHolder).download_state.setImageResource(R.drawable.icon_download_book_complete);
            } else if (downloadTask.downloadState == DownloadState.DOWNLOADING) {
                ((DownloadHolder) viewHolder).download_prompt.setText("下载中");
                ((DownloadHolder) viewHolder).download_prompt.setTextColor(Color.parseColor("#4D91D0"));
                ((DownloadHolder) viewHolder).download_state.setImageResource(R.drawable.icon_download_book_stop);
            } else if (downloadTask.downloadState == DownloadState.WAITING) {
                ((DownloadHolder) viewHolder).download_prompt.setText("排队中");
                ((DownloadHolder) viewHolder).download_prompt.setTextColor(Color.parseColor("#191919"));
                ((DownloadHolder) viewHolder).download_state.setImageResource(R.drawable.icon_download_book_stop);
            } else if (downloadTask.downloadState == DownloadState.PAUSED) {
                ((DownloadHolder) viewHolder).download_prompt.setText("已暂停");
                ((DownloadHolder) viewHolder).download_prompt.setTextColor(Color.parseColor("#191919"));
                ((DownloadHolder) viewHolder).download_state.setImageResource(R.drawable.icon_download_book_start);
            } else if (downloadTask.downloadState == DownloadState.NO_START) {
                ((DownloadHolder) viewHolder).download_prompt.setText("未下载");
                ((DownloadHolder) viewHolder).download_prompt.setTextColor(Color.parseColor("#191919"));
                ((DownloadHolder) viewHolder).download_state.setImageResource(R.drawable.icon_download_book_start);
            } else if (downloadTask.downloadState == DownloadState.REFRESH) {
                ((DownloadHolder) viewHolder).download_prompt.setText("下载失败");
                ((DownloadHolder) viewHolder).download_prompt.setTextColor(Color.parseColor("#191919"));
                ((DownloadHolder) viewHolder).download_state.setImageResource(R.drawable.icon_download_book_start);
            }

            int count = downloadTask.end == 0 ? downloadTask.book.chapter.sn : downloadTask.end;

            int progress = 0;
            try {
                progress = downloadTask.start * 100 / count;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            ((DownloadHolder) viewHolder).download_progress.setProgress(progress);

            ((DownloadHolder) viewHolder).download_state.setTag(R.id.click_object, book);
            ((DownloadHolder) viewHolder).download_state.setTag(R.id.click_task, downloadTask);
            ((DownloadHolder) viewHolder).download_state.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Book clickedBook = (Book) view.getTag(R.id.click_object);
                    DownloadTask clickedDownloadTask = (DownloadTask) view.getTag(R.id.click_task);
                    if (clickedBook == null || clickedDownloadTask == null) {
                        return;
                    }

                    DownloadState state = clickedDownloadTask.downloadState;

                    if (state == DownloadState.FINISH) {
                        return;
                    }

                    if (state == null || state == DownloadState.NO_START) {
                        if (downloadListener != null) {
                            downloadListener.downloadBook(clickedBook, true);
                        }
                    } else if (state == DownloadState.DOWNLOADING || state == DownloadState.WAITING) {
                        if (downloadListener != null) {
                            downloadListener.downloadCancelTask(clickedBook);
                        }
                    } else {
                        if (downloadListener != null) {
                            downloadListener.startDownBookTask(book);
                        }
                    }
                    notifyItemChanged(clickedBook);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position > -1 && position < books.size()) {
            return books.get(position).item_type;
        } else {
            return TYPE_EMPTY_FULL_20;
        }
    }

    public void setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void notifyItemChanged(Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {
            int index = books.indexOf(book);
            if (index != -1) {
                notifyItemChanged(index);
            } else {
                notifyDataSetChanged();
            }
        }
    }
}
