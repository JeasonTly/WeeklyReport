package com.aorise.weeklyreport.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.ProjectStatisticsActivity;
import com.aorise.weeklyreport.databinding.FragmentProjectStatisticsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProjectStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectStatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentProjectStatisticsBinding mViewDataBinding;

    // TODO: Rename and change types of parameters
    private int projectId;
    private String mParam2;

    public ProjectStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ProjectStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectStatisticsFragment newInstance(int param1) {
        ProjectStatisticsFragment fragment = new ProjectStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_project_statistics, container, false);
        mViewDataBinding.intentStatisticActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId",projectId);
                mIntent.setClass(getContext(), ProjectStatisticsActivity.class);
                startActivity(mIntent);
            }
        });
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        getActivity().setRequestedOrientation(//通过程序改变屏幕显示的方向
//                hidden ? ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
//                        : ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
