package com.quduquxie.base.module.main.fragment.mine.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.application.QuApplication;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.module.main.fragment.mine.MineInterface;
import com.quduquxie.base.module.main.fragment.mine.component.DaggerMineComponent;
import com.quduquxie.base.module.main.fragment.mine.module.MineModule;
import com.quduquxie.base.module.main.fragment.mine.presenter.MinePresenter;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.communal.widget.CustomProgressDialog;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.db.UserDao;
import com.quduquxie.function.creation.literature.view.LiteratureListActivity;
import com.quduquxie.local.view.LocalFilesActivity;
import com.quduquxie.model.User;
import com.quduquxie.module.comment.view.ReceivedCommentsActivity;
import com.quduquxie.module.comment.view.ReceivedRepliesActivity;
import com.quduquxie.module.comment.view.UserDynamicActivity;
import com.quduquxie.module.setting.view.SettingActivity;
import com.quduquxie.revise.view.ReviseUserActivity;
import com.quduquxie.util.StatServiceUtils;
import com.quduquxie.wifi.view.WiFiTransportActivity;
import com.quduquxie.wxapi.WXEntryActivity;

import java.text.MessageFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class MineFragment extends BaseFragment<MinePresenter> implements MineInterface.View {

    @BindView(R.id.mine_head_title)
    public TextView mine_head_title;
    @BindView(R.id.mine_head_setting)
    public ImageView mine_head_setting;
    @BindView(R.id.mine_user_view)
    public RelativeLayout mine_user_view;
    @BindView(R.id.mine_user_avatar)
    public ImageView mine_user_avatar;
    @BindView(R.id.mine_user_sign_mark)
    public ImageView mine_user_sign_mark;
    @BindView(R.id.mine_user_name)
    public TextView mine_user_name;
    @BindView(R.id.mine_user_prompt)
    public TextView mine_user_prompt;
    @BindView(R.id.mine_user_literature)
    public TextView mine_user_literature;
    @BindView(R.id.mine_user_dynamic)
    public TextView mine_user_dynamic;
    @BindView(R.id.mine_received_book_review_view)
    public RelativeLayout mine_received_book_review_view;
    @BindView(R.id.mine_received_book_review)
    public TextView mine_received_book_review;
    @BindView(R.id.mine_received_book_review_number)
    public TextView mine_received_book_review_number;
    @BindView(R.id.mine_received_comment_view)
    public RelativeLayout mine_received_comment_view;
    @BindView(R.id.mine_received_comment)
    public TextView mine_received_comment;
    @BindView(R.id.mine_received_comment_number)
    public TextView mine_received_comment_number;
    @BindView(R.id.mine_wifi_transmission)
    public TextView mine_wifi_transmission;
    @BindView(R.id.mine_import_local)
    public TextView mine_import_local;

    @Inject
    MinePresenter minePresenter;

    private CustomProgressDialog customProgressDialog;

    private Toast toast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        initializeParameter();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(getContext());

        if (!isHidden()) {
            minePresenter.refreshUserInformation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            minePresenter.refreshUserInformation();
        }
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerMineComponent.builder()
                .applicationComponent(applicationComponent)
                .mineModule(new MineModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        if (mine_head_title != null) {
            mine_head_title.setText(R.string.mine);
            mine_head_title.setTypeface(typeface_song_depict);
        }

        initCommentCountView(QuApplication.getInstance().loadCommentCount(), QuApplication.getInstance().loadReplyCount());
    }

    @Override
    public void recycle() {

    }

    @Override
    public void initUserInformation(User user) {
        if (user != null) {
            if (mine_user_name != null) {
                mine_user_name.setText(TextUtils.isEmpty(user.penname) ? "青果阅读" : user.penname);
            }

            if (mine_user_prompt != null) {
                mine_user_prompt.setText("编辑个人资料");
                mine_user_prompt.setTextColor(Color.parseColor("#191919"));
            }

            if (mine_user_avatar != null && this.getContext() != null) {
                if (!TextUtils.isEmpty(user.avatar_url)) {
                    Glide.with(getContext())
                            .load(user.avatar_url)
                            .transform(new GlideCircleTransform(getContext()))
                            .signature(new StringSignature(user.avatar_url))
                            .error(R.drawable.icon_avatar_default)
                            .placeholder(R.drawable.icon_avatar_default)
                            .into(mine_user_avatar);
                } else {
                    mine_user_avatar.setImageResource(R.drawable.icon_avatar_default);
                }
            }

            if (mine_user_sign_mark != null) {
                if (user.is_sign == 1) {
                    mine_user_sign_mark.setVisibility(View.VISIBLE);
                } else {
                    mine_user_sign_mark.setVisibility(View.GONE);
                }
            }
        } else {
            if (mine_user_name != null) {
                mine_user_name.setText("未登陆");
            }

            if (mine_user_prompt != null) {
                mine_user_prompt.setText("点击登陆");
                mine_user_prompt.setTextColor(Color.parseColor("#4D91D0"));
            }

            if (mine_user_avatar != null) {
                mine_user_avatar.setImageResource(R.drawable.icon_avatar_default);
            }

            if (mine_user_sign_mark != null) {
                mine_user_sign_mark.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initCommentCountView(int comment_count, int reply_count) {
        if (comment_count == 0) {
            mine_received_book_review_number.setVisibility(View.GONE);
        } else {
            mine_received_book_review_number.setVisibility(View.VISIBLE);
            if (comment_count > 99) {
                mine_received_book_review_number.setText(MessageFormat.format("{0}+", 99));
                mine_received_book_review_number.setBackgroundResource(R.drawable.icon_comment_count_ellipse);
            } else {
                mine_received_book_review_number.setText(String.valueOf(comment_count));
                mine_received_book_review_number.setBackgroundResource(R.drawable.icon_comment_count_circle);
            }
        }

        if (reply_count == 0) {
            mine_received_comment_number.setVisibility(View.GONE);
        } else {
            mine_received_comment_number.setVisibility(View.VISIBLE);
            if (reply_count > 99) {
                mine_received_comment_number.setText(MessageFormat.format("{0}+", 99));
                mine_received_comment_number.setBackgroundResource(R.drawable.icon_comment_count_ellipse);
            } else {
                mine_received_comment_number.setText(String.valueOf(reply_count));
                mine_received_comment_number.setBackgroundResource(R.drawable.icon_comment_count_circle);
            }
        }

    }

    @Override
    public void showToastMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!getActivity().isFinishing() && toast != null) {
            toast.show();
        }
    }

    @Override
    public void showProgressDialog() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(getContext());
            TextView progress_prompt = (TextView) customProgressDialog.findViewById(R.id.progress_prompt);
            progress_prompt.setText("正在处理用户信息...");
        }
        if (!getActivity().isFinishing() && customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (!getActivity().isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getContext());
        Intent intent = new Intent();
        intent.setClass(getContext(), WXEntryActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.mine_head_setting, R.id.mine_user_view, R.id.mine_user_literature, R.id.mine_user_dynamic, R.id.mine_received_book_review_view, R.id.mine_received_comment_view, R.id.mine_wifi_transmission, R.id.mine_import_local})
    public void OnClickListener(View view) {
        switch (view.getId()) {
            case R.id.mine_head_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.mine_user_view:
                if (UserDao.checkUserLogin()) {
                    startActivity(new Intent(getContext(), ReviseUserActivity.class));
                } else {
                    startActivity(new Intent(getContext(), WXEntryActivity.class));
                }
                break;
            case R.id.mine_user_literature:
                if (UserDao.checkUserLogin()) {
                    startActivity(new Intent(getContext(), LiteratureListActivity.class));
                } else {
                    startActivity(new Intent(getContext(), WXEntryActivity.class));
                }
                break;
            case R.id.mine_user_dynamic:
                if (UserDao.checkUserLogin()) {
                    startActivity(new Intent(getContext(), UserDynamicActivity.class));
                } else {
                    startActivity(new Intent(getContext(), WXEntryActivity.class));
                }
                break;
            case R.id.mine_received_book_review_view:
                if (UserDao.checkUserLogin()) {
                    if (mine_received_book_review_number != null) {
                        mine_received_book_review_number.setVisibility(View.GONE);
                    }
                    startActivity(new Intent(getContext(), ReceivedCommentsActivity.class));
                } else {
                    startActivity(new Intent(getContext(), WXEntryActivity.class));
                }
                break;
            case R.id.mine_received_comment_view:
                if (UserDao.checkUserLogin()) {
                    if (mine_received_comment_number != null) {
                        mine_received_comment_number.setVisibility(View.GONE);
                    }
                    startActivity(new Intent(getContext(), ReceivedRepliesActivity.class));
                } else {
                    startActivity(new Intent(getContext(), WXEntryActivity.class));
                }
                break;
            case R.id.mine_wifi_transmission:
                StatServiceUtils.statBookShelfWIFI(getContext());
                startActivity(new Intent(getActivity(), WiFiTransportActivity.class));
                break;
            case R.id.mine_import_local:
                StatServiceUtils.statBookShelfImportBook(getContext());
                startActivity(new Intent(getActivity(), LocalFilesActivity.class));
                break;
        }
    }
}
