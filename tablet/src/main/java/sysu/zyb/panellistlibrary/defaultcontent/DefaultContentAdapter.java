package sysu.zyb.panellistlibrary.defaultcontent;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sysu.zyb.panellistlibrary.R;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2018/4/15 下午1:25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DefaultContentAdapter extends ArrayAdapter<List<String>> {

    private static final int MAX_ITEM_SIZE = 13;

    private int contentItemSize;

    private List<Integer> itemWidthList;

    private ListView lv_content;
    private int itemHeight;
    private List<String> mPlanList;

    public DefaultContentAdapter(@NonNull Context context, int resource,
                                 @NonNull List<List<String>> objects, List<Integer> itemWidthList, List<String> planWorkTimeList,
                                 int itemHeight, ListView lv_content) {
        super(context, resource, objects);
        this.contentItemSize = itemWidthList.size();
        this.itemWidthList = itemWidthList;
        this.lv_content = lv_content;
        this.itemHeight = itemHeight;
        this.mPlanList = planWorkTimeList;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        Log.d("ybz", "getCount: " + count);
        return count;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        List<String> itemData = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.defaultcontentitem, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);


        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        for (int i = 0; i < contentItemSize; i++) {
            viewHolder.getContentTextViewList().get(i).setText(itemData.get(i));
            if (mPlanList != null && mPlanList.size() != 0) {
                if (Float.valueOf(itemData.get(i)) < Float.valueOf(mPlanList.get(i))) {
                    Log.d("tuliyuan", " 超出计划日期  计划日期为" + mPlanList.get(i) + " 实际工时为" + itemData.get(i));
                    viewHolder.getContentTextViewList().get(i).setTextColor(Color.RED);
                }else{
                    viewHolder.getContentTextViewList().get(i).setTextColor(Color.GRAY);
                }
            }
        }

        return convertView;
    }

    class ViewHolder {
        public List<TextView> getContentTextViewList() {
            return contentTextViewList;
        }

        List<TextView> contentTextViewList = new ArrayList<>();

        ViewHolder(View view) {
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content1));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content2));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content3));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content4));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content5));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content6));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content7));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content8));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content9));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content10));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content11));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content12));
            contentTextViewList.add((TextView) view.findViewById(R.id.id_tv_content13));

            for (int i = 0; i < MAX_ITEM_SIZE; i++) {
                try {
                    contentTextViewList.get(i).setWidth(itemWidthList.get(i));
                } catch (Exception e) {
                    contentTextViewList.get(i).setWidth(0);
                }
                contentTextViewList.get(i).setHeight(itemHeight);
            }
        }
    }
//
//    @Override
//    public void notifyDataSetChanged() {
//
//        super.notifyDataSetChanged();
//    }
}
