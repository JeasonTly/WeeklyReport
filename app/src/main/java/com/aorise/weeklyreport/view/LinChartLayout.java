package com.aorise.weeklyreport.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.StatisticBean;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 * 暂时未使用
 */
@Deprecated
public class LinChartLayout extends LinearLayout {

    private static final String TAG = "LinChartLayout";
    /**
     * 列表的数据源
     */
    private List<StatisticBean> mData;

    /**
     * 屏幕的宽
     */
    private int scrW;

    public LinChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // setView();
    }

    public LinChartLayout(Context context) {
        super(context);
        this.setOrientation(VERTICAL);
        // setView();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        scrW = measureWidth(widthMeasureSpec);
//        Log.d(TAG, " onMeasure " + measureWidth(widthMeasureSpec));
//        setView();
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setView() {

        if (mData != null && !mData.isEmpty()) {
            int text_max_length = 0;
            int value_max = 0;
            StatisticBean maxLengthData = new StatisticBean();
            for (StatisticBean data : mData) {
                // 获取最长文字的个数
                if (text_max_length <= data.getName().length()) {
                    text_max_length = data.getName().length();
                    maxLengthData = data;
                }
                // 获取数据值的大小
                int total = 100;

                // 获取数据的值
                if (value_max <= total) {
                    value_max = total;
                }
                Log.d(TAG, " maxlength is " + text_max_length);
            }
            int[] wh = getTextWH();
            // 文字区域的宽
            //int textAreW = text_max_length * wh[0] + dip2px(getContext(), 10);
            int textAreW = 600;

            // 图形区域的宽
            int chartAreW = scrW - textAreW - 10;


            LayoutParams layoutParams = new LayoutParams(scrW - dip2px(getContext(), 10), Math.max(100, caculatMaxItemHeight(maxLengthData, textAreW)));
            LogT.d(" srcW is "+ scrW);
            LogT.d("Params width is "+ (scrW - dip2px(getContext(), 10)));
            LogT.d("Params Height is "+ Math.max(120, caculatMaxItemHeight(maxLengthData, textAreW)));
            // 设置居中
            layoutParams.gravity = Gravity.CENTER;
            // 设置Margin
            layoutParams.topMargin = dip2px(getContext(), 4);
            layoutParams.bottomMargin = dip2px(getContext(), 4);

            LogT.d("Params topMargin is "+ dip2px(getContext(), 4));
            LogT.d("Params bottomMargin is "+ dip2px(getContext(), 4));
            // 遍历添加LinChartView
            for (StatisticBean data : mData) {
                LinChartView chartView = new LinChartView(getContext());
                chartView.setData(textAreW, chartAreW, value_max, data);
                LogT.d("addData " + data.toString());
                this.addView(chartView, layoutParams);
            }
        }

    }

    @TargetApi(23)
    private int caculatMaxItemHeight(StatisticBean data, int textAreW) {
        int height = 0;
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(dip2px(getContext(), 16));
        // 设置文字右对齐
        textPaint.setTextAlign(Paint.Align.LEFT);
        StaticLayout staticLayout;
        if(Build.VERSION.SDK_INT >= 23) {
            staticLayout = StaticLayout.Builder.obtain(data.getName(), 0, data.getName().length(), textPaint, textAreW)
                    .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                    .setEllipsizedWidth(textAreW)
                    .setIncludePad(true)
                    .build();
        }else{
            staticLayout = new StaticLayout(data.getName(), textPaint, textAreW, Layout.Alignment.ALIGN_NORMAL,
                    1.0F, 0.0F, false);
        }
        LogT.d(" itemHeigh is " + staticLayout.getHeight());
        height = staticLayout.getHeight();

        return height;
    }

    /**
     * 获取单个字符的高和宽
     */
    private int[] getTextWH() {
        int[] wh = new int[2];
        // 一个矩形
        Rect rect = new Rect();
        String text = "我";
        Paint paint = new Paint();
        // 设置文字大小
        paint.setTextSize(dip2px(getContext(), 16));
        paint.getTextBounds(text, 0, text.length(), rect);
        //wh[0] = rect.width();
        wh[0] = rect.width();
        wh[1] = rect.height();
        Log.d(TAG, "getTextWH " + rect.width());
        return wh;
    }


    public void setData(List<StatisticBean> d,int scrw) {
        this.mData = d;
        this.scrW = scrw;
        setView();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e(TAG, "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = getPaddingLeft() + getPaddingRight();
                Log.e(TAG, "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.e(TAG, "---speMode = EXACTLY");
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e(TAG, "---speMode = UNSPECIFIED");
                result = Math.max(result, specSize);
        }

        Log.e(TAG, "---result = " + result);
        return result;
    }
}
