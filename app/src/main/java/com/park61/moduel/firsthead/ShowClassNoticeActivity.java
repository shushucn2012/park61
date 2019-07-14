package com.park61.moduel.firsthead;

import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.adapter.ClassCommentListAdapter;
import com.park61.moduel.firsthead.bean.ClassComment;
import com.park61.moduel.firsthead.bean.TeachClassNotice;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/11/22.
 */

public class ShowClassNoticeActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 6;

    private PullToRefreshListView mPullRefreshListView;

    private ClassCommentListAdapter commentAdapter;
    private List<ClassComment> commentList;
    private long noticeId;
    private TeachClassNotice mTeachClassNotice;
    TextView tv_notice_title, tv_time_and_readnum, tv_teacher_name, tv_class_name, tv_content;
    ImageView img_teacher;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_show_class_notice);
    }

    @Override
    public void initView() {
        setPagTitle("通知详情");
        tv_notice_title = (TextView) findViewById(R.id.tv_notice_title);
        tv_time_and_readnum = (TextView) findViewById(R.id.tv_time_and_readnum);
        img_teacher = (ImageView) findViewById(R.id.img_teacher);
        tv_teacher_name = (TextView) findViewById(R.id.tv_teacher_name);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);
        tv_content = (TextView) findViewById(R.id.tv_content);
        /*mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                //asyncGetList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                //asyncGetList();
            }
        });
        View headerView = View.inflate(getApplicationContext(), R.layout.show_notice_header, null);
        mPullRefreshListView.getRefreshableView().addHeaderView(headerView);*/
    }

    @Override
    public void initData() {
       /* commentList = new ArrayList<>();
        commentList.add(new ClassComment());
        commentList.add(new ClassComment());
        commentList.add(new ClassComment());
        commentList.add(new ClassComment());
        commentList.add(new ClassComment());
        commentList.add(new ClassComment());
        commentAdapter = new ClassCommentListAdapter(mContext, commentList);
        mPullRefreshListView.setAdapter(commentAdapter);*/
        mTeachClassNotice = (TeachClassNotice) getIntent().getSerializableExtra("TeachClassNotice");
        //asyncDetailNotice();
        tv_notice_title.setText(mTeachClassNotice.getTitle());
        ImageManager.getInstance().displayImg(img_teacher, mTeachClassNotice.getHeadPic());
        tv_teacher_name.setText(mTeachClassNotice.getUserName());
        tv_class_name.setText(mTeachClassNotice.getClassName());
        tv_content.setText(mTeachClassNotice.getContent());

        asyncDetailNotice();
    }

    @Override
    public void initListener() {

    }

    private void asyncDetailNotice() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.noticeDetails;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("noticeId", mTeachClassNotice.getNoticeId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dListener);
    }

    BaseRequestListener dListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            TeachClassNotice itemInfo = gson.fromJson(jsonResult.toString(), TeachClassNotice.class);
            tv_time_and_readnum.setText(itemInfo.getCreateDate() + " " + itemInfo.getViewNum() + "次浏览");
        }
    };
}
