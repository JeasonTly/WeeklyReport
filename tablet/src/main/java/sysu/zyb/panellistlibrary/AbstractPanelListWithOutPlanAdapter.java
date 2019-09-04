package sysu.zyb.panellistlibrary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sysu.zyb.panellistlibrary.defaultcontent.DefaultContentAdapter;
import sysu.zyb.panellistlibrary.defaultcontent.DefaultNoPlanContentAdapter;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/05/23
 *     desc   : 总的adapterController
 *     version: 1.0
 * </pre>
 *
 * @author zyb
 */

public abstract class AbstractPanelListWithOutPlanAdapter extends AbstractPanelListAdapter{

    private static final String TAG = "ybz";

    private Context context;

    /**
     * 三个横向滑动layout
     */
    private MyHorizontalScrollView mhsv_row;
    private MyHorizontalScrollView mhsv_content;

    /**
     * 整个页面的所有布局
     */
    private PanelListLayout pl_root;//外层的根布局
    private TextView tv_title;//左上角的title
    private LinearLayout ll_row;//上方的表头
    private ListView lv_column;//左边的表头
    private ListView lv_content;//中间的内容部分
    private LinearLayout ll_contentItem;//中间的内容部分的子布局
    private SwipeRefreshLayout swipeRefreshLayout;//中间ListView外层的下拉刷新布局

    private WorkTimePlanClickListener workTimePlanClickListener;
    /**
     * 标题的宽和高,同时也是列表头的宽和列表头的高
     */
    private int titleWidth = 180;
    private int titleHeight = 100;
    private int columnItemHeight = 100;

    private String title = "";
    private int titleBackgroundResource;
    private List<String> columnDataList;
    private List<String> rowDataList;
    private List<String> planDataList;

    private String columnColor = "#607D8B";//default color of column
    private String titleColor = "#3DD078";//default color of title
    private String rowColor = "#3DD078";//default color of title

    private Drawable rowDivider;
    private Drawable columnDivider;

    /**
     * 默认关闭下拉刷新
     */
    private boolean swipeRefreshEnable = false;
    /**
     * 标志位，是否使用了默认的column实现
     */
    private boolean defaultColumn = true;

    private int initPosition = 0;//列表显示的初始值，默认第一条数据显示在最上面

    private BaseAdapter columnAdapter;
    private BaseAdapter contentAdapter;


    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new DefaultRefreshListener();

    /**
     * 两个监听器，分别控制水平和垂直方向上的同步滑动
     */
    private HorizontalScrollListener horizontalScrollListener = new HorizontalScrollListener();
    private VerticalScrollListener verticalScrollListener = new VerticalScrollListener();

    /**
     * 如果想使用默认的 contentAdapter，那么只需要传入要显示的数据，和每一行的每一个数据的宽度就可以了
     */
    private List<List<String>> contentDataList;
    private List<Integer> itemWidthList;
    private int itemHeight = 150;// 默认150px 的高度

    /**
     * constructor
     *
     * @param context
     * @param pl_root
     * @param lv_content                内容的ListView
     */
    public AbstractPanelListWithOutPlanAdapter(Context context, PanelListLayout pl_root, ListView lv_content) {
        super(context, pl_root, lv_content, null);
        this.context = context;
        this.pl_root = pl_root;
        this.lv_content = lv_content;
    }


    //region APIs

    public void setItemHeight(int dp) {
        itemHeight = dp2px(dp);
    }

    public void setContentDataList(List<List<String>> contentDataList) {
        this.contentDataList = contentDataList;
    }

    public void setItemWidthList(List<Integer> itemWidthList) {
        this.itemWidthList = parseDpList2PxList(itemWidthList);
    }

    /**
     * 设置表的标题
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 设置表标题的背景
     *
     * @param resourceId a drawable resource id
     */
    public void setTitleBackgroundResource(int resourceId) {
        this.titleBackgroundResource = resourceId;
    }

    /**
     * 设置表头的宽度
     *
     * @param titleWidth title width
     */
    public void setTitleWidth(int titleWidth) {
        this.titleWidth = dp2px(titleWidth);
    }

    /**
     * 设置表头的高度
     *
     * @param titleHeight title height
     */
    public void setTitleHeight(int titleHeight) {
        this.titleHeight = dp2px(titleHeight);
    }

    /**
     * 设置横向表头的标题（！！必须调用！！）
     *
     * @param rowDataList data list of row layout, must be a List<String>
     */
    public void setRowDataList(List<String> rowDataList) {
        this.rowDataList = rowDataList;
    }

    public void setPlanDataList(List<String> planDataList) {
        this.planDataList = planDataList;
    }

    /**
     * 设置纵向表头的内容
     *
     * @param columnDataList data list of column layout, must be a List<String>. if you don`t call
     *                       this method, the default column list will be used
     */
    public void setColumnDataList(List<String> columnDataList) {
        this.columnDataList = columnDataList;
        //getColumnAdapter();

    }

    /**
     * 横向表头的分割线
     */
    public void setRowDivider(Drawable rowDivider) {
        this.rowDivider = rowDivider;
    }

    /**
     * 纵向表头的分割线
     */
    public void setColumnDivider(Drawable columnDivider) {
        this.columnDivider = columnDivider;
    }

    /**
     * 设置纵向表头的背景色
     *
     * @param columnColor background color of column
     */
    public void setColumnColor(String columnColor) {
        this.columnColor = columnColor;
    }

    /**
     * 设置标题的背景色
     *
     * @param titleColor background color of title
     */
    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * 设置横向表头的背景色
     *
     * @param rowColor background color of row
     */
    public void setRowColor(String rowColor) {
        this.rowColor = rowColor;
    }

    /**
     * 设置纵向表头的适配器
     *
     * @param columnAdapter adapter of column ListView
     */
    public void setColumnAdapter(BaseAdapter columnAdapter) {
        this.columnAdapter = columnAdapter;
    }

    /**
     * 设置content的初始position
     * <p>
     * 比如你想进入这个Activity的时候让第300条数据显示在屏幕上（前提是该数据存在）
     * 那么在这里传入299即可
     *
     * @param initPosition position
     */
    public void setInitPosition(int initPosition) {
        this.initPosition = initPosition;
    }

    /**
     * 返回中间内容部分的ListView
     *
     * @return listView of content
     */
    public ListView getContentListView() {
        return lv_content;
    }

    /**
     * 返回左边表头的ListView
     *
     * @return listView of column(left)
     */
    public ListView getColumnListView() {
        return lv_column;
    }

    /**
     * 返回上访表头的最外层布局
     *
     * @return a CheckableLinearLayout
     */
    public LinearLayout getRowLayout() {
        return ll_row;
    }

    /**
     * 设置是否开启下拉刷新（默认关闭）
     *
     * @param bool pass true to enable pullToRefresh
     */
    public void setSwipeRefreshEnabled(boolean bool) {
        swipeRefreshEnable = bool;
    }

    /**
     * 这里有点蛋疼，因为控件是在initAdapter中赋值的，但是这里要用
     * 所以如果开发者在setAdapter之前调用了该方法，则必须对控件进行赋值
     * 但如果赋值了，还得判断开发者是否设置了初始位置，因为控件默认开启，如果初始位置不为0，则控件启用
     * 这样会造成在中间阶段下拉会触发监听，因此对initPosition再进行一次判断
     * 当用户发生了滑动操作，控件的状态会被随即改变
     *
     * @param listener
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        this.onRefreshListener = listener;
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = new SwipeRefreshLayout(context);
            if (initPosition != 0) {
                swipeRefreshLayout.setEnabled(false);
            }
        }
        swipeRefreshLayout.setOnRefreshListener(listener);
        Log.d(TAG, "setOnRefreshListener: " + onRefreshListener.toString());
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    //endregion

    /**
     * 在该方法中返回contentList的adapter
     *
     * @return content部分的adapter
     */
    protected abstract BaseAdapter getContentAdapter();

    /**
     * 初始化总Adapter，加载数据到视图
     */
    void initAdapter() {
        Log.d(TAG, " initAdapter.....");
        contentAdapter = getContentAdapter();
        columnAdapter = getColumnAdapter();
        if (contentAdapter == null) {
            contentAdapter = new DefaultNoPlanContentAdapter(context, R.layout.defaultcontentitem,
                    contentDataList, itemWidthList, itemHeight, lv_content);
        }

        reorganizeViewGroup();

        mhsv_row.setOnHorizontalScrollListener(horizontalScrollListener);
        mhsv_content.setOnHorizontalScrollListener(horizontalScrollListener);

        lv_content.setOnScrollListener(verticalScrollListener);
        lv_column.setOnScrollListener(verticalScrollListener);
    }

    /**
     * 更新ContentList数据后需要调用此方法来刷新列表
     * <p>
     * 该方法会判断是否使用了默认的纵向表头，如果是，则自动更新表头
     * 如果不是，则不更新纵向表头，交给开发者自己去更新
     * 开发者可以调用{@link #getColumnAdapter()}以获得columnAdapter
     */
    public void notifyDataSetChanged() {
        // 先刷新lv_content的数据，然后根据判断决定是否要刷新表头的数据
        Log.d(TAG, " notifyDataSetChanged columnDataList size " + (columnDataList.size()) + " contentdata size " + contentDataList.size());
        contentAdapter.notifyDataSetChanged();
        ll_contentItem = (LinearLayout) lv_content.getChildAt(0);//获得content的第一个可见item
        initColumnLayout();
        initRowLayout();
        // 当ListView绘制完成后设置初始位置，否则ll_contentItem会报空指针
        lv_content.setSelection(initPosition);
        lv_column.setSelection(initPosition);


    }



    /**
     * 核心代码：
     * 整理重组整个表的布局
     * <p>
     * 主要包含4个部分
     * 1. title
     * 2. row
     * 3. column
     * 4. content
     */
    private void reorganizeViewGroup() {
        Log.d(TAG, " reorganizeViewGroup.....");
        lv_content.setAdapter(contentAdapter);
        lv_content.setVerticalScrollBarEnabled(true);
//        lv_content.setFastScrollEnabled(false);

        // clear root viewGroup
        pl_root.removeView(lv_content);

        // 1. title (TextView --> PanelListLayout)
        tv_title = new TextView(context);
        tv_title.setText(title);
        if (titleBackgroundResource != 0) {
            tv_title.setBackgroundResource(titleBackgroundResource);
        }
        tv_title.getPaint().setFakeBoldText(true);
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setBackgroundColor(Color.parseColor(titleColor));
        tv_title.setTextColor(Color.WHITE);
        tv_title.setId(View.generateViewId());//设置一个随机id，这样可以保证不冲突
        RelativeLayout.LayoutParams lp_tv_title = new RelativeLayout.LayoutParams(titleWidth, titleHeight);
        pl_root.addView(tv_title, lp_tv_title);

        // 2. row（LinearLayout --> MyHorizontalScrollView --> PanelListLayout）
        ll_row = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll_row.setLayoutParams(lp);
        mhsv_row = new MyHorizontalScrollView(context);
        mhsv_row.setHorizontalScrollBarEnabled(false);
        mhsv_row.setOverScrollMode(View.OVER_SCROLL_NEVER);//去除滑动到边缘时出现的阴影
        mhsv_row.addView(ll_row);// 暂时先不给ll_row添加子view，等布局画出来了再添加
        mhsv_row.setId(View.generateViewId());
        RelativeLayout.LayoutParams lp_mhsv_row = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight);
        lp_mhsv_row.addRule(RelativeLayout.END_OF, tv_title.getId());
        lp_mhsv_row.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        pl_root.addView(mhsv_row, lp_mhsv_row);

        // 4. column （ListView --> PanelListLayout）
        lv_column = new ListView(context);
        lv_column.setId(View.generateViewId());
        lv_column.setVerticalScrollBarEnabled(false);//去掉滚动条
        RelativeLayout.LayoutParams lp_lv_column = new RelativeLayout.LayoutParams(titleWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        lp_lv_column.addRule(RelativeLayout.BELOW, mhsv_row.getId());
        pl_root.addView(lv_column, lp_lv_column);

        // 5. content (ListView --> MyHorizontalScrollView --> SwipeRefreshLayout --> PanelListLayout)
        mhsv_content = new MyHorizontalScrollView(context);
        mhsv_content.addView(lv_content);//因为 lv_content 在 xml 文件中已经设置了 layout 为 match_parent，所以这里add时不需要再加 LayoutParameter 对象
        mhsv_content.setOverScrollMode(View.OVER_SCROLL_NEVER);//去除滑动到边缘时出现的阴影
        RelativeLayout.LayoutParams lp_mhsv_content = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = new SwipeRefreshLayout(context);
        }
        swipeRefreshLayout.addView(mhsv_content, lp_mhsv_content);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setEnabled(false);
        Log.d(TAG, " reorganizeViewGroup: " + onRefreshListener.toString());
        RelativeLayout.LayoutParams lp_srl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp_srl.addRule(RelativeLayout.RIGHT_OF, lv_column.getId());
        lp_srl.addRule(RelativeLayout.BELOW, tv_title.getId());
        pl_root.addView(swipeRefreshLayout, lp_srl);
        if (initPosition == 0) {
            swipeRefreshLayout.setEnabled(swipeRefreshEnable);
        }

        // 发一个消息出去。当布局渲染完成之后会执行消息内容，此时
        pl_root.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "post--lv_content = " + lv_content.toString());
//                ll_contentItem = (LinearLayout) lv_content.getChildAt(lv_content.getFirstVisiblePosition());//获得content的第一个可见item
                Log.d(TAG, "lv_content ...." + lv_content.getChildCount());
                ll_contentItem = (LinearLayout) lv_content.getChildAt(0);//获得content的第一个可见item
                initColumnLayout();
                initRowLayout();
                // 当ListView绘制完成后设置初始位置，否则ll_contentItem会报空指针
                lv_content.setSelection(initPosition);
                lv_column.setSelection(initPosition);
            }
        });
    }

    private void initColumnLayout() {
        Log.d(TAG, " initColumnLayout  " + (ll_contentItem == null));

        if (ll_contentItem != null) {
            columnItemHeight = ll_contentItem.getHeight();
            lv_column.setAdapter(getColumnAdapter());
            if (columnDivider != null) {
                lv_column.setDivider(columnDivider);
            }
        } else {
            columnItemHeight = itemHeight;
            lv_column.setAdapter(getColumnAdapter());
        }
    }


    /**
     * 初始化横向表头的布局，必须在所有的布局都载入完之后才能调用
     * <p>
     * must be called in pl_root.post();
     */
    private void initRowLayout() {

        Integer[] widthArray = new Integer[getRowDataList().size()];

        if (ll_contentItem == null) {
            if (itemWidthList != null) {
                for (int i = 0; i < widthArray.length; i++) {
                    widthArray[i] = itemWidthList.get(i);
                }
            } else {
                try {
                    throw new Exception("how can I set the row width if you don`t give me any reference");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < widthArray.length; i++) {
                widthArray[i] = ll_contentItem.getChildAt(i).getWidth();
            }
        }

        List<String> rowDataList1 = getRowDataList();
        int rowCount = rowDataList1.size();

        ll_row.setBackgroundColor(Color.parseColor(rowColor));
        //分隔线的设置，如果content的item设置了分割线，那row使用相同的分割线，除非单独给row设置了分割线
        if (rowDivider == null) {
            if (ll_contentItem != null) {
                ll_row.setDividerDrawable(ll_contentItem.getDividerDrawable());
                ll_row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            }
        } else {
            ll_row.setDividerDrawable(rowDivider);
        }

        // 横向表头每一个 item 的宽度都取决于 content 的 item 的宽度
        for (int i = 0; i < rowCount; i++) {
            TextView rowItem = new TextView(context);
            rowItem.setText(rowDataList1.get(i));//设置文字
            rowItem.getPaint().setFakeBoldText(true);
            rowItem.setWidth(widthArray[i]);//设置宽度
            rowItem.setHeight(titleHeight);//设置高度
            rowItem.setTextColor(Color.WHITE);
            rowItem.setGravity(Gravity.CENTER);
            final int position = i;
            rowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workTimePlanClickListener.monthClick(position);
                }
            });
            ll_row.addView(rowItem);
        }

    }

    /**
     * 返回横向表头的内容列表
     * <p>
     * 如果设置了自定义的表头内容，则直接返回引用
     * 如果用户没设置，则根据传进来的count数生成一个默认表头
     */
    private List<String> getRowDataList() {
        if (rowDataList == null) {
            try {
                throw new Exception("you must set your column data list by calling setColumnDataList(List<String>)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rowDataList;

    }

    private List<String> getPlanDataList() {
        if (planDataList == null) {
            try {
                throw new Exception("you must set your column data list by calling setColumnDataList(List<String>)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return planDataList;

    }

    /**
     * 返回纵向表头的数据列表
     * 如果开发者没有自定义纵向表头，则生成默认的表头，其内容为1~n，并且将标志位置true
     * 方便{@link #notifyDataSetChanged()}方法作出判断
     * 如果开发者自定义了纵向表头，则直接返回其自定义的内容
     *
     * @return data list of column ListView
     */
    private List<String> getColumnDataList() {
        Log.d(TAG, " getColumnDataList  ");
        if (columnDataList == null) {
            defaultColumn = true;
            columnDataList = new ArrayList<>();
            int contentDataCount = contentAdapter.getCount();
            for (int i = 1; i <= contentDataCount; i++) {
                Log.d(TAG, " getColumnDataList  " + i);
                columnDataList.add(String.valueOf(i));
            }
        }
        return columnDataList;
    }

    private int dp2px(int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private List<Integer> parseDpList2PxList(List<Integer> itemWidthList) {
        List<Integer> itemWidthListInPx = new ArrayList<>();
        for (int i = 0; i < itemWidthList.size(); i++) {
            itemWidthListInPx.add(dp2px(itemWidthList.get(i)));
            Log.d(TAG, " parseDpList2PxList " + dp2px(itemWidthList.get(i)));
        }
        // todo 限制 itemWidthListInPx 最大只能有10个
        return itemWidthListInPx;
    }

    /**
     * 返回纵向表头的适配器
     *
     * @return adapter of column ListView
     */
    public BaseAdapter getColumnAdapter() {
        if (columnAdapter == null) {
            columnAdapter = new ColumnAdapter(context, android.R.layout.simple_list_item_1, getColumnDataList());
        }
        return columnAdapter;
    }

    /**
     * HorizontalScrollView的滑动监听（水平方向同步控制）
     */
    private class HorizontalScrollListener implements MyHorizontalScrollView.OnHorizontalScrollListener {
        @Override
        public void onHorizontalScrolled(MyHorizontalScrollView view, int l, int t, int oldl, int oldt) {
            if (view == mhsv_content) {
                mhsv_row.scrollTo(l, t);
            } else {
                mhsv_content.scrollTo(l, t);
            }
        }
    }

    /**
     * 两个ListView的滑动监听（垂直方向同步控制）
     */
    private class VerticalScrollListener implements AbsListView.OnScrollListener {

        int scrollState;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            this.scrollState = scrollState;
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                View subView = view.getChildAt(0);
                if (subView != null && view == lv_content) {
                    int top = subView.getTop();
                    int position = view.getFirstVisiblePosition();
                    lv_column.setSelectionFromTop(position, top);
                } else if (subView != null && view == lv_column) {
                    int top = subView.getTop();
                    int position = view.getFirstVisiblePosition();
                    lv_content.setSelectionFromTop(position, top);
                }
            }

            // 滑动事件冲突的解决：如果ListView的首条item的position != 0，即此时不再顶上，则将下拉刷新禁用
            if (swipeRefreshEnable) {

                if (view.getFirstVisiblePosition() != 0 && swipeRefreshLayout.isEnabled()) {
                    swipeRefreshLayout.setEnabled(false);
                }

                if (view.getFirstVisiblePosition() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                }
                swipeRefreshLayout.setEnabled(false);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //判断滑动是否终止，以停止自动对齐，否则该方法会一直被调用，影响性能
            if (scrollState == SCROLL_STATE_IDLE) {
                return;
            }
            View subView = view.getChildAt(0);
            if (subView != null && view == lv_content) {
                int top = subView.getTop();
                lv_column.setSelectionFromTop(firstVisibleItem, top);
            } else if (subView != null && view == lv_column) {
                int top = subView.getTop();
                lv_content.setSelectionFromTop(firstVisibleItem, top);
            }
        }
    }

    /**
     * 默认的columnAdapter
     * <p>
     * 之所以重写是为了根据content的item之高度动态设置column的item之高度
     */
    private class ColumnAdapter extends ArrayAdapter<String> {

        private int resourceId;
        private List<String> columnDataList;

        ColumnAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            resourceId = resource;
            columnDataList = objects;
        }

        @Override
        public int getCount() {
            Log.d(TAG, " columnAdapter size is " + columnDataList.size());
            return super.getCount();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = new TextView(context);
                ((TextView) view).setHeight(columnItemHeight);
                //如果以上设置高度的代码无法生效，则使用下面的方式设置
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(titleWidth,columnItemHeight);
//                view.setLayoutParams(lp);
//                view.getLayoutParams().height = columnItemHeight;
            } else {
                view = convertView;
            }
            Log.d("tuliyuan1111", "设置列" + columnDataList.get(position));
            ((TextView) view).setText(columnDataList.get(position));
            ((TextView) view).setTextSize(15);
            view.setPadding(0, 0, 0, 0);
            ((TextView) view).setGravity(Gravity.CENTER);

            return view;
        }
    }

    private class DefaultRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            Toast.makeText(context, "请调用PanelListAdapter的setOnRefreshListener()并传入你的Listener", Toast.LENGTH_SHORT).show();
            if (swipeRefreshLayout.isRefreshing()) {

                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}