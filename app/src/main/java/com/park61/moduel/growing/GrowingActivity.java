package com.park61.moduel.growing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseActivity;
import com.park61.MainTabActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.child.BabyBuildActivity;
import com.park61.moduel.child.BabyScheduleActivity;
import com.park61.moduel.child.GuideShowPhotoActivity;
import com.park61.moduel.child.InviteFamily2Activity;
import com.park61.moduel.child.bean.ShowItem;
import com.park61.moduel.childtest.TestHome;
import com.park61.moduel.childtest.bean.QuestionCache;
import com.park61.moduel.growing.adapter.ShowListAdapter;
import com.park61.moduel.me.AddBabyActivity;
import com.park61.moduel.me.BabyInfoActivity;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.openmember.OpenMemberActivity;
import com.park61.net.base.Request;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.pw.GmInputPopWin;
import com.park61.widget.pw.GrowChildPopWin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成长界面
 */
public class GrowingActivity extends BaseActivity implements View.OnClickListener, ShowListAdapter.GmShowItemCallBack {

    private static final int REQ_FOR_RES_ADD_BABY = 1;
    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 5;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private List<BabyItem> babyList = new ArrayList<BabyItem>();
    ;
    private List<ShowItem> showList = new ArrayList<ShowItem>();
    private ShowListAdapter adapter;
    private BabyItem showingBb;// 当前显示的宝宝
    private ImageView img_child_pic, img_right_baby, img_member_type;
    private TextView child_name, tv_age;//tv_date, tv_weight,tv_height;
    private Button btn_member, btn_empty;
    private GrowChildPopWin mGrowChildPopWin;
    private View cover, area_bottom_vline, view_baby_build, view_show_photo, view_ability_level,
            view_invite, schedule_llayout, empty_area, top_bar, view_cover;
    private ListView lv_shows;
    private boolean isEnd;
    private int currIndex = 0;
    private boolean isAddBabyBack;//是否是添加宝宝后返回

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_growing);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            lowSdkLayoutInit();
        }
    }

    @Override
    public void initView() {
        top_bar = findViewById(R.id.top_bar);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        ViewInitTool.initPullToRefresh(mPullRefreshScrollView, mContext);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                PAGE_NUM = 1;
                isEnd = false;
                babyList.clear();
                showList.clear();
                asyncGetUserChilds();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (showingBb == null) {// 如果没有孩子不加载
                    mPullRefreshScrollView.onRefreshComplete();
                    return;
                }
                if (isEnd) {
                    showShortToast("已经是最后一页了");
                    mPullRefreshScrollView.onRefreshComplete();
                    return;
                }
                PAGE_NUM++;
                asyncGetGrowingShowList();
            }
        });
        btn_member = (Button) findViewById(R.id.btn_member);
        img_right_baby = (ImageView) findViewById(R.id.img_right_baby);
        img_child_pic = (ImageView) findViewById(R.id.img_child_pic);
        child_name = (TextView) findViewById(R.id.child_name);
//        tv_date = (TextView) findViewById(R.id.tv_date);
        cover = findViewById(R.id.cover);
        area_bottom_vline = findViewById(R.id.area_bottom_vline);
        tv_age = (TextView) findViewById(R.id.tv_age);
        view_baby_build = findViewById(R.id.view_baby_build);
        view_show_photo = findViewById(R.id.view_show_photo);
        view_invite = findViewById(R.id.view_invite);
        view_ability_level = findViewById(R.id.view_ability_level);
        schedule_llayout = findViewById(R.id.schedule_llayout);
//        tv_weight = (TextView) findViewById(R.id.tv_weight);
//        tv_height = (TextView) findViewById(R.id.tv_height);
        lv_shows = (ListView) findViewById(R.id.lv_shows);
        lv_shows.setFocusable(false);
        empty_area = findViewById(R.id.empty_area);
        btn_empty = (Button) findViewById(R.id.btn_empty);
        img_member_type = (ImageView) findViewById(R.id.img_member_type);
        view_cover = findViewById(R.id.view_cover);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        img_right_baby.setOnClickListener(this);
        view_baby_build.setOnClickListener(this);
        view_show_photo.setOnClickListener(this);
        view_invite.setOnClickListener(this);
        view_ability_level.setOnClickListener(this);
        schedule_llayout.setOnClickListener(this);
        btn_member.setOnClickListener(this);
        btn_empty.setOnClickListener(this);
        img_child_pic.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalParam.GrowingMainNeedRefresh) {
            // 获取当前孩子
            if (GlobalParam.CUR_CHILD_ID != 0) {
                showingBb = new BabyItem();
                showingBb.setChildId(GlobalParam.CUR_CHILD_ID);
            }
            PAGE_NUM = 1;
            isEnd = false;
            babyList.clear();
            showList.clear();
            adapter = new ShowListAdapter(mContext, showList);
            lv_shows.setAdapter(adapter);
            asyncGetUserChilds();
            GlobalParam.GrowingMainNeedRefresh = false;
        }
    }

    /**
     * 获取我的宝宝列表
     */
    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String requestBodyData = ParamBuild.getUserChilds();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getBabysLsner);
    }

    BaseRequestListener getBabysLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
            GlobalParam.GrowingMainNeedRefresh = true;
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            JSONArray jay = jsonResult.optJSONArray("list");
            if (jay == null || jay.length() <= 0) {
                showShortToast("您还没有添加宝宝哦！");
                Intent it = new Intent(mContext, AddBabyActivity.class);
                startActivityForResult(it, REQ_FOR_RES_ADD_BABY);
                Intent changeIt = new Intent();
                changeIt.setAction("ACTION_TAB_CHANGE");
                changeIt.putExtra("TAB_NAME", "tab_me");
                mContext.sendBroadcast(changeIt);
                GlobalParam.GrowingMainNeedRefresh = true;
                return;
            }
            babyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
                babyList.add(b);
            }
            if (showingBb == null || GlobalParam.CUR_CHILD_ID == 0) {
                showingBb = babyList.get(0);// 默认显示第一个宝宝
                GlobalParam.CUR_CHILD_ID = showingBb.getId();
            } else if (isAddBabyBack) {
                currIndex = babyList.size() - 1;
                showingBb = babyList.get(currIndex);// 默认显示第一个宝宝
                GlobalParam.CUR_CHILD_ID = showingBb.getId();
                isAddBabyBack = false;
            } else {
                for (int j = 0; j < babyList.size(); j++) {
                    if (babyList.get(j).getId().equals(showingBb.getId())) {
                        currIndex = j;
                        break;
                    }
                }
                showingBb = babyList.get(currIndex);
            }
            ImageManager.getInstance().displayImg(img_child_pic, showingBb.getPictureUrl(), R.drawable.headimg_default_img);
            child_name.setText(showingBb.getPetName());
            tv_age.setText(showingBb.getAge());
            if (babyList.size() < 1) {
                child_name.setText("您还没有添加宝宝哦");
                tv_age.setText("年龄:--");
            }
            GlobalParam.GrowingMainNeedRefresh = false;
            asyncGetGrowingShowList();
        }
    };

    /**
     * 请求获取成长萌照列表
     */
    private void asyncGetGrowingShowList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GROWING_SHOW_LIST;
        String requestBodyData = ParamBuild.getGrowingShowLis(PAGE_NUM, PAGE_SIZE, showingBb.getId());
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<ShowItem> currentPageList = new ArrayList<ShowItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                showShortToast("没有萌照数据哦！");
                empty_area.setVisibility(View.VISIBLE);
                area_bottom_vline.setVisibility(View.GONE);
                showList.clear();
                adapter.notifyDataSetChanged();
                isEnd = true;
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                showList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                isEnd = true;
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ShowItem s = gson.fromJson(actJot.toString(), ShowItem.class);
                currentPageList.add(s);
            }
            showList.addAll(currentPageList);
            if (showList.size() < 1) {
                empty_area.setVisibility(View.VISIBLE);
            } else {
                empty_area.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
            area_bottom_vline.setVisibility(View.VISIBLE);
            if (PAGE_NUM == 1 && showList.get(0).getVoteApplyId() != null) {
                // 判断是否是第一次进入APP
                SharedPreferences isFirstRunSp = getSharedPreferences("IsFirstGrowingFlag", Activity.MODE_PRIVATE);
                boolean isFirstFlag = isFirstRunSp.getBoolean("IsFirstGrowingRun", true);
                if (isFirstFlag) {
                    MainTabActivity.grow_view_cover.setVisibility(View.VISIBLE);
                    MainTabActivity.grow_view_cover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainTabActivity.grow_view_cover.setVisibility(View.GONE);
                        }
                    });
                    isFirstRunSp.edit().putBoolean("IsFirstGrowingRun", false).commit();
                }
            }
        }
    };

    protected void listStopLoadView() {
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_right_baby:
                mGrowChildPopWin = new GrowChildPopWin(mContext, cover, babyList, currIndex, new GrowChildPopWin.OnBabySelectLsner() {
                    @Override
                    public void onSelect(int pos) {
                        showingBb = babyList.get(pos);
                        GlobalParam.CUR_CHILD_ID = showingBb.getId();
                        refreshShowList();
                        mGrowChildPopWin.dismiss();
                    }

                    @Override
                    public void onAdd() {
                        Intent it = new Intent(mContext, AddBabyActivity.class);
                        startActivityForResult(it, REQ_FOR_RES_ADD_BABY);
                    }
                });
                mGrowChildPopWin.showAsDropDown(findViewById(R.id.top_bar), 0, 1);
                break;
            case R.id.view_baby_build:
                Intent buildIt = new Intent(mContext, BabyBuildActivity.class);
                buildIt.putExtra("showingBb", showingBb);
                startActivity(buildIt);
                break;
            case R.id.view_invite:
                Intent inviteIt = new Intent(mContext, InviteFamily2Activity.class);
                inviteIt.putExtra("childId", showingBb.getId());
                inviteIt.putExtra("childPic", showingBb.getPictureUrl());
                inviteIt.putExtra("childName", showingBb.getName());
                startActivity(inviteIt);
                break;
            case R.id.view_show_photo:
                Intent showPhotoIt = new Intent(mContext, GuideShowPhotoActivity.class);
                showPhotoIt.putExtra("showingBbId", showingBb.getId());
                startActivityForResult(showPhotoIt, 0);
                break;
            case R.id.view_ability_level:
//                Intent abilityIt = new Intent(mContext, AbilityLevelActivity.class);
//                Intent abilityIt = new Intent(mContext, ChildAbilityLevelActivity.class);
//                Intent abilityIt = new Intent(mContext, DAPFirstV2Activity.class);
                Intent abilityIt = new Intent(mContext, TestHome.class);
                QuestionCache.chosenChildId = showingBb.getId();
                //abilityIt.putExtra("showingBb", showingBb);
                startActivity(abilityIt);
                GlobalParam.GrowingMainNeedRefresh = true;
                break;
            case R.id.schedule_llayout:
                Intent scheduleIntent = new Intent(mContext, BabyScheduleActivity.class);
                scheduleIntent.putExtra("childId", showingBb.getId());
                startActivity(scheduleIntent);
                break;
            case R.id.btn_member:
                Intent it = new Intent(this, OpenMemberActivity.class);
                it.putExtra("child", showingBb);
                startActivity(it);
                break;
            case R.id.btn_empty:
                Intent showPhoto = new Intent(mContext, GuideShowPhotoActivity.class);
                showPhoto.putExtra("showingBbId", showingBb.getId());
                startActivityForResult(showPhoto, 0);
                break;
            case R.id.img_child_pic:
                Intent infoIt = new Intent(this, BabyInfoActivity.class);
                infoIt.putExtra("baby_info", showingBb);
                startActivity(infoIt);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            refreshShowList();
        } else if (requestCode == REQ_FOR_RES_ADD_BABY) {//添加完孩子后直接测评
            if (resultCode == RESULT_OK) {//onActivityResult() -> onStart() -> onResume()
                //Intent abilityIt = new Intent(mContext, DAPFirstV2Activity.class);
                //startActivity(abilityIt);
                Intent changeIt = new Intent();
                changeIt.setAction("ACTION_TAB_CHANGE");
                changeIt.putExtra("TAB_NAME", "tab_grow");
                mContext.sendBroadcast(changeIt);
                isAddBabyBack = true;
            }
        }
    }

    /**
     * 刷新列表记录
     */
    public void refreshShowList() {
        PAGE_NUM = 1;
        isEnd = false;
        asyncGetUserChilds();
    }

    public void showInput(int pos, Long replyId) {
        Intent it = new Intent(mContext, GmInputPopWin.class);
        it.putExtra("growItem", showList.get(pos));
        if (replyId != null)
            it.putExtra("replyId", replyId);
        startActivity(it);
    }

    /**
     * 评论
     */
    @Override
    public void onComtClicked(int pos) {
        showInput(pos, null);
    }

    /**
     * 回复评论
     */
    @Override
    public void onReplyClicked(int pos, Long replyId) {
        showInput(pos, replyId);
    }

    @Override
    public void onShareClicked(int pos) {
        ShowItem si = showList.get(pos);
        String shareUrl = String.format(AppUrl.GROW_SHARE_URL, si.getId() + "",
                si.getType() + "");
        String picUrl = showingBb.getPictureUrl();
        String title = getString(R.string.app_name);
        String ageStr = si.getAgeYear() + "岁" + si.getAgeMonth() + "月"
                + si.getAgeDay() + "天";
        String description = "";
        if (si.getType() == 0) {
            String str = "";
            if (!TextUtils.isEmpty(si.getComment()))
                str = si.getComment();
            description = showingBb.getName() + " " + ageStr + "\n" + str + "\n成长记录";
        } else {
            String desc = "";
            if (si.getHeight() != null && si.getHeight() != 0)
                desc = "身高：" + FU.strDbFmt(si.getHeight()) + "cm " + si.getState();
            else
                desc = "体重：" + si.getWeight() + "kg " + si.getState();
            description = showingBb.getName() + " " + ageStr + "\n" + desc + "\n成长记录";
        }
        showShareDialog(shareUrl, picUrl, title, description);
    }

    @Override
    public void onDeleteClicked(final int pos) {
        dDialog.showDialog("提示", "您确定删除该记录吗？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowItem si = showList.get(pos);
                // 如果是萌照就用萌照删除接口，如果是体测记录就用记录删除接口
                if (si.getType() == 0)// 0:萌照 1:成长记录
                    asyncDelShowItem(si.getId());
                else
                    asyncDelRecItem(si.getId());
                dDialog.dismissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.dismissDialog();
            }
        });
    }

    @Override
    public void onShareVoteClicked(int pos) {
        final ShowItem si = showList.get(pos);
        asyncVoteShare(si.getVoteApply().getVoteId(), si.getVoteApplyId());
        String url = "http://m.61park.cn/vote/toVoteDetail.do?applyId=" + si.getVoteApplyId() + "&voteId=" + si.getVoteApply().getVoteId();
        showShareDialog(url, si.getVoteApply().getImgUrl(), si.getVoteApply().getTitle(), si.getVoteApply().getDescription());
    }

    /**
     * 请求删除萌照记录
     */
    private void asyncVoteShare(Long voteId, Long voteApplyId) {
        String wholeUrl = AppUrl.host + AppUrl.VOTESHARE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", 1);//1拉票，2详情；
        map.put("voteId", voteId);
        map.put("voteApplyId", voteApplyId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    /**
     * 请求删除萌照记录
     */
    private void asyncDelShowItem(Long id) {
        String wholeUrl = AppUrl.host + AppUrl.DELETE_SHOW_PHOTOS;
        String requestBodyData = ParamBuild.deleteShowPhoto(id);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, deleteChildLsner);
    }

    /**
     * 请求删除体测记录
     */
    private void asyncDelRecItem(Long id) {
        String wholeUrl = AppUrl.host + AppUrl.DELETE_GROWING_REC;
        String requestBodyData = ParamBuild.deleteShowPhoto(id);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, deleteChildLsner);
    }

    BaseRequestListener deleteChildLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("删除成功！");
            refreshShowList();
        }
    };
}
