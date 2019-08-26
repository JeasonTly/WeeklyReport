package com.aorise.weeklyreport.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.StatisticBean;

import java.util.Date;

import static android.text.Layout.Alignment.ALIGN_CENTER;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 */
public class LinChartView extends View {
    private static final String TAG = "LinChartView";
    private StatisticBean mData;
    private int mTextW, mChartH, mMaxV;

    private Paint arcPaint = null;
    private Date date;

    public LinChartView(Context context) {
        super(context);
        date = new Date();
    }

    public LinChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogT.d("111........111");
        if (mData == null) {
            Log.d(TAG, "数据为空!");
            return;
        }
        // 画文字
        drawText(canvas, "项目阶段: " + mData.getName());

        //画图形
        drawLine(canvas);

        drawBoardLine(canvas);
    }

    /**
     * 绘制图形
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        double chart_length = (getWidth() - mTextW) / (double) mMaxV;
        int start_complete_left = mTextW + 10,
                start_complete_top = 4,
                start_complete_right = start_complete_left + (int) (chart_length * mData.getPercentComplete()),
                start_uncomplete_right = start_complete_left + (int) (chart_length * mMaxV),
                start_complete_bottom = mChartH;

        Log.d(TAG, start_complete_left + "..." + start_complete_top + ",,," + start_complete_right + "ds"
                + start_uncomplete_right);
        if (mChartH > 200) {
            start_complete_top = getHeight() / 5;
            start_complete_bottom = start_complete_top * 4;
        }
        this.arcPaint = new Paint();
        this.arcPaint.setColor(getResources().getColor(R.color.dark_gray));
        this.arcPaint.setAntiAlias(true);// 去除锯齿
        // 绘制未完成的，

        canvas.drawRect(start_complete_left, start_complete_top, start_uncomplete_right, start_complete_bottom, arcPaint);

        // 绘制完成的
        Date endDate = TimeUtil.getInstance().String2Date(mData.getEndDate());
        if(date.before(endDate)){
            LogT.d("今天在计划结束日期之前");
            this.arcPaint.setColor(getResources().getColor(R.color.red));
        }else {
            this.arcPaint.setColor(getResources().getColor(R.color.colorAccent));
        }
        canvas.drawRect(start_complete_left, start_complete_top, start_complete_right, start_complete_bottom, arcPaint);
    }

    /**
     * 绘制文字说明  右对齐
     *
     * @param canvas
     * @param text
     */
    @TargetApi(23)
    private void drawText(Canvas canvas, String text) {
        canvas.save();
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(dip2px(getContext(), 16));
        // 设置文字右对齐
        textPaint.setTextAlign(Paint.Align.LEFT);

        StaticLayout staticLayout;
        if (Build.VERSION.SDK_INT >= 23) {
            staticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, mTextW)
                    .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                    .setEllipsizedWidth(mTextW)
                    .setAlignment(ALIGN_CENTER)
                    .setIncludePad(true)
                    .build();
        } else {
            staticLayout = new StaticLayout(text, textPaint, mTextW, Layout.Alignment.ALIGN_LEFT,
                    1.0F, 0.0F, false);
        }
        staticLayout.draw(canvas);
        Log.d(TAG, " height is " + staticLayout.getHeight());
        canvas.restore();
    }

    private void drawBoardLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawLine(0, 5, mTextW, 5, paint);
        canvas.drawLine(0, getHeight(), mTextW, getHeight(), paint);

        LogT.d("getHeight() " + getHeight());
        int start_complete_top = 4;
        int start_complete_bottom = mChartH;
        if (mChartH > 200) {
            start_complete_top = getHeight() / 5;
            start_complete_bottom = start_complete_top * 4;
        }
        canvas.drawLine(mTextW, 5, mTextW, mChartH, paint);
        canvas.drawLine(mTextW, getHeight() / 2 + 5, mTextW + 10, getHeight() / 2, paint);
    }

    /**
     * @return 返回指定笔和指定字符串的长度
     */
    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }

    /**
     * @return 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }

    public void setData(int textW, int chartW, int max_valur, StatisticBean data) {
        Log.d(TAG, "  textW " + textW + " chartW " + chartW + " max_value " + max_valur);
        this.mTextW = textW;
        this.mChartH = chartW;
        this.mMaxV = max_valur;
        this.mData = data;
        this.postInvalidate();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                StringBuffer mBuffer = new StringBuffer();
                for (StatisticBean.OwnerListBeanX ownerListBeanX : mData.getOwnerList()) {
                    mBuffer.append(ownerListBeanX.getFullName() + "、");
                }
                Toast toast = new Toast(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewDataBinding viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.toast_view,null,false);
                viewDataBinding.setVariable(BR.planName,mData.getName());
                viewDataBinding.setVariable(BR.startTime, TimeUtil.getInstance().date2date(mData.getStartDate()));
                viewDataBinding.setVariable(BR.endTime, TimeUtil.getInstance().date2date(mData.getEndDate()));
                viewDataBinding.setVariable(BR.percentComplete, mData.getPercentComplete() + "%");
                viewDataBinding.setVariable(BR.owner,mBuffer.toString());
                toast.setView(viewDataBinding.getRoot());

                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
        }
        return true;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }
}
