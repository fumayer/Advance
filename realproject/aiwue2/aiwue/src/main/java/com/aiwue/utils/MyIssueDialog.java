package com.aiwue.utils;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aiwue.R;

public class MyIssueDialog extends AlertDialog implements View.OnClickListener{
	private Button share_btn_myissue,cancel_btn_myissue;

	public MyIssueDialog(Context context) {
		super(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_share_myissue);

		share_btn_myissue=(Button) findViewById(R.id.share_btn_myissue);
		cancel_btn_myissue=(Button) findViewById(R.id.cancel_btn_myissue);
		share_btn_myissue.setOnClickListener(this);
		cancel_btn_myissue.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.share_btn_myissue:
			break;
			case R.id.cancel_btn_myissue:
				dismiss();
		}
	}

}
