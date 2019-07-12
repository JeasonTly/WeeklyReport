package com.aorise.weeklyreport.base;

import android.text.TextUtils;

import com.aorise.weeklyreport.adapter.MulityStageRecyclerAdapter;
import com.aorise.weeklyreport.adapter.WorkTypeRecyclerAdapter;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.MulityTypeItem;
import com.aorise.weeklyreport.bean.WeeklyReportBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/1.
 */
public class CommonUtils {
    private static CommonUtils INSTANCE;

    public static CommonUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommonUtils();
        }
        return INSTANCE;
    }

    /**
     * 添加标题
     *
     * @param list
     * @return
     */
    public List<String> resortArrayList(List<String> list) {
        List<String> title = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                title.add(list.get(1).substring(0, 1));
            } else {
                String thisChar = list.get(i).substring(0, 1);
                String LastChar = list.get(i - 1).substring(0, 1);
                if (!thisChar.equals(LastChar)) {
                    title.add(list.get(i).substring(0, 1));
                }
            }
            title.add(list.get(i));
        }
        return title;
    }

    public List<MulityTypeItem> resortWorkTypeMulityTypeList(List<WeeklyReportBean> resortList) {
        List<MulityTypeItem> list = new ArrayList<>();
        String typeTextDescription = "";

        for (int i = 0; i < resortList.size(); i++) {
            LogT.d("重新排列后的工作类型" + resortList.get(i).getWorkType());
            int workType = resortList.get(i).getWorkType();
            switch (workType) {
                case 1:
                    typeTextDescription = "项目工作";
                    break;
                case 2:
                    typeTextDescription = "部门工作";
                    break;
                case 3:
                    typeTextDescription = "临时工作";
                    break;
            }
            if (i == 0) {
                MulityTypeItem mulityTypeItem = new MulityTypeItem();
                mulityTypeItem.setData(typeTextDescription);
                mulityTypeItem.setData_type(WorkTypeRecyclerAdapter.TypeTAG.TYPE_TAG.ordinal());
                mulityTypeItem.setItem_name(typeTextDescription);
                list.add(mulityTypeItem);
            } else {
                int lastWorkType = resortList.get(i - 1).getWorkType();
                if (lastWorkType != workType) {
                    MulityTypeItem mulityTypeItem = new MulityTypeItem();
                    mulityTypeItem.setData(typeTextDescription);
                    mulityTypeItem.setData_type(WorkTypeRecyclerAdapter.TypeTAG.TYPE_TAG.ordinal());
                    mulityTypeItem.setItem_name(typeTextDescription);
                    list.add(mulityTypeItem);
                }
            }
            MulityTypeItem mulityTypeItem = new MulityTypeItem();
            mulityTypeItem.setData(resortList.get(i));
            mulityTypeItem.setData_type(WorkTypeRecyclerAdapter.TypeTAG.TYPE_CONTENT.ordinal());
            mulityTypeItem.setItem_name(typeTextDescription);

            list.add(mulityTypeItem);
        }
        LogT.d("现在复合类型的list数目为" + list.toString());
        return list;
    }

    public static RequestBody getRequestBody(String string) {
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), string);
    }

    public List<MulityTypeItem> resortStage(List<HeaderItemBean.PlanDetailsListBean> listBeans) {
        List<MulityTypeItem> mulityTypeItems = new ArrayList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            HeaderItemBean.PlanDetailsListBean mPlanStage = listBeans.get(i);
            if (i == 0) {
                MulityTypeItem mulityTypeItem = new MulityTypeItem();
                mulityTypeItem.setData(mPlanStage);
                mulityTypeItem.setData_type(MulityStageRecyclerAdapter.TypeTAG.TYPE_TAG.ordinal());
                mulityTypeItem.setItem_name(mPlanStage.getPhase());
                mulityTypeItems.add(mulityTypeItem);

                if (TextUtils.isEmpty(mPlanStage.getSpecificPhase())) {
                    MulityTypeItem mulityTypeItem_one = new MulityTypeItem();
                    mulityTypeItem_one.setData(mPlanStage);
                    mulityTypeItem_one.setData_type(MulityStageRecyclerAdapter.TypeTAG.TYEP_NULL.ordinal());
                    mulityTypeItem_one.setItem_name("无数据");
                    mulityTypeItems.add(mulityTypeItem_one);
                    continue;
                }
            } else {
                if (TextUtils.isEmpty(mPlanStage.getSpecificPhase())) {
                    MulityTypeItem mulityTypeItem = new MulityTypeItem();
                    mulityTypeItem.setData(mPlanStage);
                    mulityTypeItem.setData_type(MulityStageRecyclerAdapter.TypeTAG.TYPE_TAG.ordinal());
                    mulityTypeItem.setItem_name(mPlanStage.getPhase());
                    mulityTypeItems.add(mulityTypeItem);


                    MulityTypeItem mulityTypeItem_one = new MulityTypeItem();
                    mulityTypeItem_one.setData(mPlanStage);
                    mulityTypeItem_one.setData_type(MulityStageRecyclerAdapter.TypeTAG.TYEP_NULL.ordinal());
                    mulityTypeItem_one.setItem_name("无数据");
                    mulityTypeItems.add(mulityTypeItem_one);
                    continue;
                } else {
                    HeaderItemBean.PlanDetailsListBean lastPlanStage = listBeans.get(i - 1);
                    if (!lastPlanStage.getPhase().equals(listBeans.get(i).getPhase())) {
                        MulityTypeItem mulityTypeItem = new MulityTypeItem();
                        mulityTypeItem.setData(mPlanStage);
                        mulityTypeItem.setData_type(MulityStageRecyclerAdapter.TypeTAG.TYPE_TAG.ordinal());
                        mulityTypeItem.setItem_name(mPlanStage.getPhase());
                        mulityTypeItems.add(mulityTypeItem);
                    }
                }

            }
            LogT.d(" 具体的工作阶段名称: " + mPlanStage.toString());

            MulityTypeItem mulityTypeItem = new MulityTypeItem();
            mulityTypeItem.setData(mPlanStage);
            mulityTypeItem.setData_type(MulityStageRecyclerAdapter.TypeTAG.TYPE_CONTENT.ordinal());
            mulityTypeItem.setItem_name(mPlanStage.getSpecificPhase());
            mulityTypeItems.add(mulityTypeItem);


        }
        for (MulityTypeItem mulityTypeItem : mulityTypeItems) {
            LogT.d(" mulityType content is " + mulityTypeItem);
        }
        LogT.d(" size is " + mulityTypeItems.size());
        return mulityTypeItems;
    }
}
