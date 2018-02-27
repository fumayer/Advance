package com.aiwue.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwue.R;


public class MyProgressDialog extends MyAlertDialog {

	private TextView mProgressTitle;
	private ProgressBar mProgressBar;
	private TextView progress_item1;
	private TextView progress_item2;
	private int dialogTitle;
	public int total = 0;

	protected MyProgressDialog(Context context, int theme, int titleID) {
		super(context, theme);
	}

	protected MyProgressDialog(Context context, int titleID) {
		super(context);
		this.dialogTitle = titleID;
	}

	public static MyProgressDialog create(Context context, int titleID) {
		
		MyProgressDialog dialog = new MyProgressDialog(context,titleID);
		dialog.setCancelable(false); // 设置不能通过“后退”按钮关闭对话框-------必须，否则整个流程会断
		return dialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Context mContext = getContext();
		LayoutInflater inflater = LayoutInflater.from(mContext);

		View view = inflater.inflate(R.layout.my_progress_dlg, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		mProgressTitle = (TextView) view.findViewById(R.id.progress_title);
		progress_item1 = ((TextView) view.findViewById(R.id.progress_item1));
		progress_item2 = ((TextView) view.findViewById(R.id.progress_item2));
		mProgressBar.setProgress(0);
		progress_item1.setText("0%");
		super.setTitle(mContext.getString(dialogTitle));
		setView(view);

		super.onCreate(savedInstanceState);
	}

	public void setTotalMessage(String message) {
		progress_item2.setText(message);
	}

	public void setTotal(int total) {
		mProgressBar.setMax(total);
		this.total = total;
	}
	
	public void setMax(int total) {
		mProgressBar.setMax(total);
		this.total = total;
	}

	public void setTitle(String title) {
		mProgressTitle.setText(title);
	}

	public void setCurrentPercent(int current) {
		mProgressBar.setProgress(current);
		progress_item1.setText(100 * current / total + "%");
	}
	
	public void setProgress(int current) {
		mProgressBar.setProgress(current);
		progress_item1.setText(100 * current / total + "%");
	}
	
	public void setProgressItem2Visible(boolean visible){
		if(visible)
		{
			progress_item2.setVisibility(View.VISIBLE);
		}else{
			progress_item2.setVisibility(View.INVISIBLE);
		}
	}
}
