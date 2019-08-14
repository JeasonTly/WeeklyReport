package com.aorise.weeklyreport.base;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aorise.weeklyreport.R;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Tuliyuan.
 * Date: 2019/2/21.
 */
public class MenuPopup extends BasePopupWindow {
    private List<String> mList = new ArrayList<>();
    private ListView mListView;
    private MyArrayAdatper mAdapter;
    private int defaultSelected = -1;
    private MenuPopupSelectedListener menuPopupSelectedListener;

    public MenuPopup(Context context, int defaultPosition,MenuPopupSelectedListener menuPopupSelectedListener) {
        super(context);
        setAlignBackground(false);
        setPopupGravity(Gravity.BOTTOM);
        defaultSelected = defaultPosition;
        mListView = (ListView) findViewById(R.id.list_item);
        mList = TimeUtil.getInstance().getHistoryWeeks();
        this.menuPopupSelectedListener =  menuPopupSelectedListener;

        LogT.d("aaarestart.....?");
        mAdapter = new MyArrayAdatper(getContext(), R.layout.listview_item, mList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogT.d("点击了" + mList.get(position));
                mAdapter.changeBg(position);
                dismiss();
            }
        });
    }

    @Override
    protected Animation onCreateShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(getScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation());
        return set;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(getScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation(false));
        return set;
    }

    @Override
    public void showPopupWindow(View v) {
        setOffsetX(v.getWidth() / 2);
      //  mListView.setSelection(mListView.getBottom());
        mListView.smoothScrollToPosition(mList.size()-1);
        super.showPopupWindow(v);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.actionbar_week_to_choose);
    }

    class MyArrayAdatper extends ArrayAdapter<String> {
        private boolean[] isSelected;

        public MyArrayAdatper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            isSelected = new boolean[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                isSelected[i] = false;
            }
            isSelected[defaultSelected] = true;
        }

        public void changeBg(int position) {

            for (int i = 0; i < mList.size(); i++) {
                isSelected[i] = false;
                if(i == position){
                    isSelected[i] = true;
                }
            }
            menuPopupSelectedListener.selectPosistion(position);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(getContext());
                view = mInflater.inflate(R.layout.listview_item, null);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(R.id.list_item_text);
            textView.setText(mList.get(position));
            ImageView imageView = (ImageView) view.findViewById(R.id.list_item_img);
            imageView.setVisibility(isSelected[position] ? View.VISIBLE : View.GONE);

            return view;
        }
    }
    public interface MenuPopupSelectedListener{
        void selectPosistion(int position);
    }
}
