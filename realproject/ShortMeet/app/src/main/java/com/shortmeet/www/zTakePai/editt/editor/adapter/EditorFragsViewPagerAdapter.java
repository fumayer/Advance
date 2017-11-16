package com.shortmeet.www.zTakePai.editt.editor.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shortmeet.www.R;
import com.shortmeet.www.utilsUsed.DimenExchangeUtil;

import java.util.List;

/**
 *
 */

public class EditorFragsViewPagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> sts;
    private List<Fragment> frags;

    public EditorFragsViewPagerAdapter(FragmentManager fragmentManager, Context mContext, List<String> strings, List<Fragment> fragList) {
        super(fragmentManager);
        this.mContext = mContext;
        this.sts = strings;
       this. mLayoutInflater=LayoutInflater.from(mContext);
        this.frags=fragList;
    }


    @Override
    public int getCount() {
        return frags == null ? 0 : frags.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if(convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.item_hometop_indicator_scrolltext, container, false);
        }
        TextView  textView =(TextView) convertView;
        textView.setText(sts.get(position));
        int witdh = getTextWidth(textView);
        int padding = DimenExchangeUtil.dip2px(mContext,20);
        //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
        //1.3f是根据上面字体大小变化的倍数1.3f设置
        textView.setWidth((int) (witdh * 1.3f) + padding);
        return convertView;
    }


    @Override
    public Fragment getFragmentForPage(int position) {
        return frags.get(position);
    }
    @Override
    public int getItemPosition(Object object) {
        //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
        // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
        return PagerAdapter.POSITION_UNCHANGED;
    }

    private int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }
}
