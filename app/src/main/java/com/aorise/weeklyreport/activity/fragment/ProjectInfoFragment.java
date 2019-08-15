package com.aorise.weeklyreport.activity.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.MyWebViewClient;
import com.aorise.weeklyreport.bean.ProjectBaseInfo;
import com.aorise.weeklyreport.databinding.FragmentProjectInfoBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProjectInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private FragmentProjectInfoBinding mViewDataBinding ;
    // TODO: Rename and change types of parameters
    private int projectId;


    public ProjectInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectId projectId.
     * @return A new instance of fragment ProjectInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectInfoFragment newInstance(int projectId) {
        ProjectInfoFragment fragment = new ProjectInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, projectId);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_project_info, container, false);
        mViewDataBinding.projectInfoTxt.setBackgroundColor(0); // 设置背景色
        mViewDataBinding.projectInfoTxt.getSettings().setJavaScriptEnabled(true);
        mViewDataBinding.projectInfoTxt.getSettings().setAppCacheEnabled(true);
        mViewDataBinding.projectInfoTxt.getSettings().setDatabaseEnabled(true);
        mViewDataBinding.projectInfoTxt.getSettings().setDomStorageEnabled(true);
        mViewDataBinding.projectInfoTxt.getSettings().setLoadWithOverviewMode(true);
       // mViewDataBinding.projectInfoTxt.addJavascriptInterface(new MJavascriptInterface(this, null), "imagelistener");
        mViewDataBinding.projectInfoTxt.setWebViewClient(new MyWebViewClient());
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initProjectInfo();
    }
    public void updateProjectId(int projectId){
        this.projectId = projectId;
    }
    private void initProjectInfo(){
        ApiService.Utils.getInstance(getContext()).getProjectInfoById(projectId)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<ProjectBaseInfo>>(getActivity()) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<ProjectBaseInfo> data) {
                        super.onNext(data);
                        LogT.d("获取到的项目详细信息为"+data.toString());
                        if(data.isRet()){
                            mViewDataBinding.projectHeaderName.setText(data.getData().getLeaderName());
                            mViewDataBinding.setCodeName(data.getData().getCode());
                            mViewDataBinding.setProjectName(data.getData().getName());
                            String niContent = data.getData().getIntor();
                            niContent = "<html> \n" +
                                    "<head> \n" +
                                    "<style type=\"text/css\"> \n" +
                                    "body {font-size:18px}\n" +
                                    "img{max-width:100% !important;}\n" +
                                    "</style> \n" +
                                    "</head> \n" +
                                    "<body width=100% style=word-wrap:break-word;>" +
                                    niContent +
                                    "</body>" +
                                    "</html>";
                            LogT.d("news Content "+ niContent );
                            mViewDataBinding.projectInfoTxt.loadDataWithBaseURL("", niContent, "text/html", "utf-8", "");
                        }
                    }
                });
    }
}
