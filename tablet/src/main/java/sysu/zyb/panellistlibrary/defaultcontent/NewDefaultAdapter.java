package sysu.zyb.panellistlibrary.defaultcontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sysu.zyb.panellistlibrary.R;

/**
 * Created by Tuliyuan.
 * Date: 2019/9/3.
 */
public class NewDefaultAdapter extends ArrayAdapter<List<String>> {
    private static final int MAX_ITEM_SIZE = 10;

    private int contentItemSize;

    private List<Integer> itemWidthList;

    private ListView lv_content;
    private int itemHeight;

    public NewDefaultAdapter(@NonNull Context context, int resource,
                                 @NonNull List<List<String>> objects, List<Integer> itemWidthList,
                                 int itemHeight, ListView lv_content) {
        super(context, resource, objects);
        this.contentItemSize = itemWidthList.size();
        this.itemWidthList = itemWidthList;
        this.lv_content = lv_content;
        this.itemHeight = itemHeight;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        Log.d("ybz", "getCount: "+count);
        return count;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
   ViewHolder viewHolder;
        List<String> itemData = getItem(position);

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defaultcontentitem, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        for (int i = 0; i < contentItemSize; i++) {
            viewHolder.contentTextViewList.get(i).setText(itemData.get(i));
        }

        if (lv_content.isItemChecked(position)){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorSelected));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorDeselected));
        }

        return view;
    }

    class ViewHolder {

        List<TextView> contentTextViewList = new ArrayList<>(10);

        ViewHolder(View view) {
            for (int i = 0; i < contentItemSize; i++) {
                TextView textView = new TextView(getContext());
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                contentTextViewList.add(textView);
            }


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
}
