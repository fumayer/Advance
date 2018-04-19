package com.newsdemo.ui.zhihu.activity;

import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.newsdemo.R;
import com.newsdemo.base.SimpleActivity;
import com.newsdemo.component.RxBus;
import com.newsdemo.util.DateUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jianqiang.hu on 2017/5/19.
 */

public class CalendarActivity extends SimpleActivity {

    @BindView(R.id.view_calender)
    MaterialCalendarView mCalender;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    CalendarDay mDate;

    @Override
    protected int getLayout() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar,"选择日期");
        mCalender.state().edit()
                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                .setMinimumDate(CalendarDay.from(2013, 5, 20))
                .setMaximumDate(CalendarDay.from(DateUtil.getCurrentYear(), DateUtil.getCurrentMonth(), DateUtil.getCurrentDay()))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        mCalender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mDate=date;
            }
        });
    }

    @OnClick(R.id.tv_calender_enter)
    void chooseDate(){
        RxBus.getDefault().post(mDate);
        finish();
    }
}
