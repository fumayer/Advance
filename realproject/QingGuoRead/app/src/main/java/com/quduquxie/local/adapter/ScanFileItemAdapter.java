package com.quduquxie.local.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.bean.ScanFile;
import com.quduquxie.util.CustomUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class ScanFileItemAdapter extends RecyclerView.Adapter<ScanFileItemAdapter.ViewHolder>{

    private List<ScanFile> scannedFileList;
    private WeakReference<Context> contextReference;
    private LocalFileItemClickListener localFileItemClickListener;
    private BookDaoHelper bookDaoHelper;

    public ScanFileItemAdapter(List<ScanFile> scannedFileList, Context context) {
        this.scannedFileList = scannedFileList;
        this.contextReference = new WeakReference<>(context);
        this.bookDaoHelper = BookDaoHelper.getInstance(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(contextReference.get()).inflate(R.layout.item_local_file_book, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final ScanFile scanFile = scannedFileList.get(position);

        if (scanFile == null) {
            return;
        }

        viewHolder.local_file_item_content.setTag(R.id.refresh_object, scanFile);
        viewHolder.local_file_item_content.setTag(R.id.refresh_position, position);
        viewHolder.local_file_item_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanFile scannedFile = (ScanFile) view.getTag(R.id.refresh_object);
                if (!bookDaoHelper.subscribeBook(scannedFile.getId())) {
                    int index = (int) view.getTag(R.id.refresh_position);
                    if (localFileItemClickListener != null) {
                        localFileItemClickListener.localFileItemClick(scannedFile, index);
                    }
                }
            }
        });

        viewHolder.local_file_item_name.setText(TextUtils.isEmpty(scanFile.getName()) ? "未知" : scanFile.getName());
        viewHolder.local_file_item_size.setText(CustomUtils.convertStorage(scanFile.getFile().length()));
        viewHolder.local_file_item_time.setText(CustomUtils.transformationTime(scanFile.getFile().lastModified()));

        if (bookDaoHelper.subscribeBook(scanFile.getId())) {
            viewHolder.local_file_item_import.setVisibility(View.VISIBLE);
            viewHolder.local_file_item_check.setVisibility(View.GONE);
        } else {

            viewHolder.local_file_item_import.setVisibility(View.GONE);
            viewHolder.local_file_item_check.setVisibility(View.VISIBLE);

            if (scanFile.isChecked()) {
                viewHolder.local_file_item_check.setImageResource(R.drawable.icon_local_file_checked);
            } else {
                viewHolder.local_file_item_check.setImageResource(R.drawable.icon_local_file_check);
            }
        }
    }

    @Override
    public int getItemCount() {
        return scannedFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout local_file_item_content;
        public TextView local_file_item_name;
        public TextView local_file_item_size;
        public TextView local_file_item_time;
        public ImageView local_file_item_check;
        public TextView local_file_item_import;

        public ViewHolder(View itemView) {
            super(itemView);
            local_file_item_content = (RelativeLayout) itemView.findViewById(R.id.local_file_item_content);
            local_file_item_name = (TextView) itemView.findViewById(R.id.local_file_item_name);
            local_file_item_size = (TextView) itemView.findViewById(R.id.local_file_item_size);
            local_file_item_time = (TextView) itemView.findViewById(R.id.local_file_item_time);
            local_file_item_check = (ImageView) itemView.findViewById(R.id.local_file_item_check);
            local_file_item_import = (TextView) itemView.findViewById(R.id.local_file_item_import);
        }
    }

    public void setLocalFileItemClickListener(LocalFileItemClickListener localFileItemClickListener) {
        this.localFileItemClickListener = localFileItemClickListener;
    }

    public interface LocalFileItemClickListener {
        void localFileItemClick(ScanFile scanFile, int position);
    }
}
