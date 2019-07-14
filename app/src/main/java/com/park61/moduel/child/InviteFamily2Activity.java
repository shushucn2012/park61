package com.park61.moduel.child;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.me.bean.RelationItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.circleimageview.GlideCircleTransform;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请亲界面
 */
public class InviteFamily2Activity extends BaseActivity implements View.OnClickListener {
    private View invite_root;
    private ImageView img_user, img0, img1, img2, img3, img4;
    private TextView tv_user_name, tv_user_relation, tv_relation0, tv_relation1, tv_relation2, tv_relation3, tv_relation4;
    private Button btn_relation0, btn_relation1, btn_relation2, btn_relation3, btn_relation4, btn_relation_other;

    private String relationName;// 关系名称
    private String id;// 关系id
    private Long curChildId;
    private String childName;
    private String picUrl;
    private List<RelationItem> relationList;
    private String curRelationId;
    private int selectedPos = -1;
    private int curFinalPos = -1;
    private int position;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_invite_family2);
    }

    @Override
    public void initView() {
        invite_root = findViewById(R.id.invite_root);
        img_user = (ImageView) findViewById(R.id.img_user);
        img0 = (ImageView) findViewById(R.id.img0);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_relation = (TextView) findViewById(R.id.tv_user_relation);
        tv_relation0 = (TextView) findViewById(R.id.tv_relation0);
        tv_relation1 = (TextView) findViewById(R.id.tv_relation1);
        tv_relation2 = (TextView) findViewById(R.id.tv_relation2);
        tv_relation3 = (TextView) findViewById(R.id.tv_relation3);
        tv_relation4 = (TextView) findViewById(R.id.tv_relation4);

        btn_relation0 = (Button) findViewById(R.id.btn_relation0);
        btn_relation1 = (Button) findViewById(R.id.btn_relation1);
        btn_relation2 = (Button) findViewById(R.id.btn_relation2);
        btn_relation3 = (Button) findViewById(R.id.btn_relation3);
        btn_relation4 = (Button) findViewById(R.id.btn_relation4);
        btn_relation_other = (Button) findViewById(R.id.btn_relation_other);
    }

    @Override
    public void initData() {
        curChildId = getIntent().getLongExtra("childId", -1l);
        childName = getIntent().getStringExtra("childName");
        picUrl = getIntent().getStringExtra("childPic");
        //ImageManager.getInstance().displayImg(img_baby, picUrl);
        relationList = new ArrayList<RelationItem>();
        asyncGetRelation();
    }

    @Override
    public void initListener() {
        btn_relation0.setOnClickListener(this);
        btn_relation1.setOnClickListener(this);
        btn_relation2.setOnClickListener(this);
        btn_relation3.setOnClickListener(this);
        btn_relation4.setOnClickListener(this);
        btn_relation_other.setOnClickListener(this);
    }

    /**
     * 获取用户孩子关系
     */
    private void asyncGetRelation() {
        String wholeUrl = AppUrl.host + AppUrl.GET_RELATION_END;
        String requestBodyData = ParamBuild.getRelation(curChildId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, rlistener);
    }

    BaseRequestListener rlistener = new JsonRequestListener() {

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
            curRelationId = jsonResult.optString("id");
            RelationItem item = gson.fromJson(jsonResult.toString(), RelationItem.class);
            relationName = item.getRelationName();
            id = item.getId();
            asyncGetRelationList();
        }
    };

    /**
     * 获取用户孩子关系列表
     */
    private void asyncGetRelationList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_RELATION_LIST_END;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, rlist_listener);
    }

    BaseRequestListener rlist_listener = new JsonRequestListener() {

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
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                String id = jay.optJSONObject(i).optString("id");
                String relationName = jay.optJSONObject(i).optString("relationName");
                relationList.add(new RelationItem(id, relationName));
                if (id.equals(curRelationId)) {// 已有关系显示为选中
                    curFinalPos = i;
                }
            }
            setData();
        }
    };

    private void setData() {
        ImageManager.getInstance().displayImg(img_user, GlobalParam.currentUser.getPictureUrl());
        tv_user_name.setText(GlobalParam.currentUser.getPetName());
        tv_user_relation.setText("hi~我是" + relationName);
        int index = 0;
        for (int i = 0; i < relationList.size(); i++) {
            if (curRelationId.equals(relationList.get(i).getId())) {
                index = i;
                break;
            }
        }
        relationList.remove(index);

        tv_relation0.setText(relationList.get(0).getRelationName());
        setImgByRelation(relationList.get(0).getRelationName(), img0);

        tv_relation1.setText(relationList.get(1).getRelationName());
        setImgByRelation(relationList.get(1).getRelationName(), img1);

        tv_relation2.setText(relationList.get(2).getRelationName());
        setImgByRelation(relationList.get(2).getRelationName(), img2);

        tv_relation3.setText(relationList.get(3).getRelationName());
        setImgByRelation(relationList.get(3).getRelationName(), img3);

        tv_relation4.setText(relationList.get(4).getRelationName());
        setImgByRelation(relationList.get(4).getRelationName(), img4);
    }

    @Override
    public void onClick(View view) {
        Intent intent1 = new Intent(mContext, InviteMethodActivity.class);
        intent1.putExtra("childId", curChildId);
        intent1.putExtra("childPic", picUrl);
        intent1.putExtra("childName", childName);
        switch (view.getId()) {
            case R.id.btn_relation0:
                position = 0;
                break;
            case R.id.btn_relation1:
                position = 1;
                break;
            case R.id.btn_relation2:
                position = 2;
                break;
            case R.id.btn_relation3:
                position = 3;
                break;
            case R.id.btn_relation4:
                position = 4;
                break;
            case R.id.btn_relation_other:
                position = 5;
                break;
        }
        intent1.putExtra("id", relationList.get(position).getId());
        intent1.putExtra("relationName", relationName);
        startActivity(intent1);
    }

    public void setImgByRelation(String relationName, ImageView imgRelation) {
        int resId = 0;
        if (relationName.equals("妈妈")) {
            resId = R.drawable.mama;
        } else if (relationName.equals("爸爸")) {
            resId = R.drawable.baba;
        } else if (relationName.equals("爷爷")) {
            resId = R.drawable.yeye;
        } else if (relationName.equals("奶奶")) {
            resId = R.drawable.nainai;
        } else if (relationName.equals("外公")) {
            resId = R.drawable.waigong;
        } else if (relationName.equals("外婆")) {
            resId = R.drawable.waipo;
        }
        Glide.with(this).load(resId).transform(new GlideCircleTransform(this)).into(imgRelation);
    }

}
