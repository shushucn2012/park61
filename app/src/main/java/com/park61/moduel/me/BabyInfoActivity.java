package com.park61.moduel.me;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.AliyunUploadUtils.OnUploadFinish;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.picselect.utils.SelectPicPopupWindow;
import com.park61.widget.wheel.DateTimeDialog;
import com.park61.widget.wheel.DateTimeDialog.OnChosenListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class BabyInfoActivity extends BaseActivity implements OnClickListener {

    private static final int ASYNCREQ_GET_RELATION = 0;
    private static final int REQ_GET_PIC = 0;
    private static final int REQ_GET_NICKNAME = 1;
    private static final int REQ_GET_NAME = 2;
    private static final int REQ_GET_RELATION = 3;
    private static final int REQ_GET_BIRTHDAY = 4;
    private static final int REQ_GET_SEX = 5;
    private Calendar calendar;
    private BabyItem mBabyItem;
    private String backData = "";
    private boolean canChangeBirthDay = true;// 是否可以更改生日

    private View pic_area, nickname_area, name_area, birthday_area, sex_area,
            relation_area, invite_area;
    private TextView tv_nickname_value, tv_name_value, tv_birthday_value,
            tv_sex_value, tv_relation_value;
    private ImageView img_pic;
    private Button btn_delete, left_img_babyinfo;
    private DateTimeDialog myTimeDialog;
    private boolean hasDoneModify;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_babyinfo);
    }

    @Override
    public void initView() {
        pic_area = findViewById(R.id.pic_area);
        nickname_area = findViewById(R.id.nickname_area);
        name_area = findViewById(R.id.name_area);
        birthday_area = findViewById(R.id.birthday_area);
        sex_area = findViewById(R.id.sex_area);
        relation_area = findViewById(R.id.relation_area);
        invite_area = findViewById(R.id.invite_area);

        img_pic = (ImageView) findViewById(R.id.img_pic);
        tv_nickname_value = (TextView) findViewById(R.id.tv_nickname_value);
        tv_name_value = (TextView) findViewById(R.id.tv_name_value);
        tv_birthday_value = (TextView) findViewById(R.id.tv_birthday_value);
        tv_sex_value = (TextView) findViewById(R.id.tv_sex_value);
        tv_relation_value = (TextView) findViewById(R.id.tv_relation_value);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        left_img_babyinfo = (Button) findViewById(R.id.left_img_babyinfo);
        myTimeDialog = new DateTimeDialog(mContext);
    }
    @Override
    public void initData() {
        calendar = Calendar.getInstance();
        mBabyItem = (BabyItem) getIntent().getSerializableExtra("baby_info");
        if (mBabyItem != null) {
            ImageManager.getInstance().displayImg(img_pic, mBabyItem.getPictureUrl());
            if (!TextUtils.isEmpty(mBabyItem.getPetName()))
                tv_nickname_value.setText(mBabyItem.getPetName());
            if (!TextUtils.isEmpty(mBabyItem.getName()))
                tv_name_value.setText(mBabyItem.getName());
            if (!TextUtils.isEmpty(mBabyItem.getBirthday()))
                tv_birthday_value.setText(DateTool.L2SEndDay(mBabyItem
                        .getBirthday()));
            if (!TextUtils.isEmpty(mBabyItem.getSex()))
                tv_sex_value
                        .setText(mBabyItem.getSex().equals("0") ? "男" : "女");
        }
        asyncGetRelation();
        asyncGetGrowingShowList();
    }

    @Override
    public void initListener() {
        pic_area.setOnClickListener(this);
        nickname_area.setOnClickListener(this);
        name_area.setOnClickListener(this);
        birthday_area.setOnClickListener(this);
        sex_area.setOnClickListener(this);
        relation_area.setOnClickListener(this);
        invite_area.setOnClickListener(this);
        btn_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dDialog.showDialog("提醒", "确认删除该宝宝吗？", "确认", "取消",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                asyncDelChild();
                                dDialog.dismissDialog();
                            }
                        }, null);
            }
        });
        left_img_babyinfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasDoneModify) {
                    setResult(RESULT_OK);
                }
                finish();
            }
        });
    }

    /**
     * 请求获取成长萌照列表
     */
    private void asyncGetGrowingShowList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GROWING_SHOW_LIST;
        String requestBodyData = ParamBuild.getGrowingShowLis(1, 10,
                mBabyItem.getId());
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay.length() > 0)
                canChangeBirthDay = false;
        }
    };

    @Override
    public void onClick(View v) {
        hasDoneModify = true;
        int reqCode = -1;
        Intent editIt = new Intent(mContext, MeEditActivity.class);
        if (v.getId() == R.id.pic_area) {
            Intent it = new Intent(mContext, SelectPicPopupWindow.class);
            startActivityForResult(it, REQ_GET_PIC);
            return;
        } else if (v.getId() == R.id.birthday_area) {
            if (!canChangeBirthDay) {
                showShortToast("该宝宝已有成长记录，不能更改生日了！");
                return;
            }
            String birthday = tv_birthday_value.getText().toString();
            if (!TextUtils.isEmpty(birthday))
                calendar.setTime(DateTool.getDateByDay(birthday));
            myTimeDialog.setTime(calendar);
            myTimeDialog.setOnChosenListener(new OnChosenListener() {

                @Override
                public void onFinish(String year, String month, String day,
                                     String hour, String min) {
                    backData = year + "-" + month + "-" + day;
                    if (DateTool.getDateByDay(backData)
                            .after(new Date(new Date().getTime() - 24 * 60 * 60
                                    * 1000))) {
                        showShortToast("您选择的出生日期不能大于今天！");
                        return;
                    }
                    String requestBodyData = ParamBuild.updateChild(
                            mBabyItem.getId(), null, null, null, backData,
                            null, null);
                    asyncUpdateChild(requestBodyData, REQ_GET_BIRTHDAY);
                }
            });
            myTimeDialog.show();
            return;
        } else if (v.getId() == R.id.sex_area) {
            // 创建我们的单选对话框
            String sexStr = tv_sex_value.getText().toString();
            int sexCode = 0;
            if (sexStr.equals("男"))
                sexCode = 0;
            else if (sexStr.equals("女"))
                sexCode = 1;
            Dialog dialog = new AlertDialog.Builder(mContext)
                    .setSingleChoiceItems(R.array.sex, sexCode, sexListener)
                    .create();
            dialog.show();
            return;
        } else if (v.getId() == R.id.nickname_area) {
            reqCode = REQ_GET_NICKNAME;
            editIt.putExtra("current_data", tv_nickname_value.getText());
        } else if (v.getId() == R.id.name_area) {
            reqCode = REQ_GET_NAME;
            editIt.putExtra("current_data", tv_name_value.getText());
        } else if (v.getId() == R.id.relation_area) {
            reqCode = REQ_GET_RELATION;
            editIt.putExtra("current_data", tv_relation_value.getText());
            editIt.putExtra("relation", true);
        } else if (v.getId() == R.id.invite_area) {
            return;
        }
        startActivityForResult(editIt, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;
        String requestBodyData = "";
        backData = data.getStringExtra("new_data");
        if (requestCode == REQ_GET_PIC) {
            String picPath = data.getStringExtra("path");
            logout("picPath======" + picPath);
            Bitmap bmp = ImageManager.getInstance().readFileBitMap(picPath);
            img_pic.setImageBitmap(bmp);
            new AliyunUploadUtils(mContext).uploadPic(picPath,
                    new OnUploadFinish() {

                        @Override
                        public void onSuccess(String picUrl) {
                            String requestBodyData = ParamBuild.updateChild(
                                    mBabyItem.getId(), picUrl, null, null,
                                    null, null, null);
                            asyncUpdateChild(requestBodyData, 0);
                        }

                        @Override
                        public void onError() {
                            showShortToast("宝宝头像上传失败请重试！");
                        }
                    });
            return;
        } else if (requestCode == REQ_GET_NICKNAME) {
            requestBodyData = ParamBuild.updateChild(mBabyItem.getId(), null,
                    backData, null, null, null, null);
        } else if (requestCode == REQ_GET_NAME) {
            requestBodyData = ParamBuild.updateChild(mBabyItem.getId(), null,
                    null, backData, null, null, null);
        } else if (requestCode == REQ_GET_RELATION) {
            requestBodyData = ParamBuild.updateChild(mBabyItem.getId(), null,
                    null, null, null, null, data.getStringExtra("relationId"));
        }
        asyncUpdateChild(requestBodyData, requestCode);
    }

    DialogInterface.OnClickListener sexListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            backData = mContext.getResources().getStringArray(R.array.sex)[which];
            dialog.dismiss();
            String requestBodyData = ParamBuild.updateChild(mBabyItem.getId(),
                    null, null, null, null, which + "", null);
            asyncUpdateChild(requestBodyData, REQ_GET_SEX);
        }
    };

    /**
     * 获取用户孩子关系
     */
    private void asyncGetRelation() {
        String wholeUrl = AppUrl.host + AppUrl.GET_RELATION_END;
        String requestBodyData = ParamBuild.getRelation(mBabyItem.getId());
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData,
                ASYNCREQ_GET_RELATION, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
            String relationId = jsonResult.optString("id");
            String relationName = jsonResult.optString("relationName");
            tv_relation_value.setText(relationName);
        }
    };

    /**
     * 请求更新孩子数据
     */
    private void asyncUpdateChild(String requestBodyData, int requestCode) {
        String wholeUrl = AppUrl.host + AppUrl.UPDATE_CHILD_INFO_END;
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData,
                requestCode, updateChildLsner);
    }

    BaseRequestListener updateChildLsner = new JsonRequestListener() {

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
            showShortToast("修改宝宝信息成功！");
            switch (requestId) {
                case REQ_GET_NICKNAME:
                    tv_nickname_value.setText(backData);
                    break;
                case REQ_GET_NAME:
                    tv_name_value.setText(backData);
                    break;
                case REQ_GET_BIRTHDAY:
                    tv_birthday_value.setText(backData);
                    break;
                case REQ_GET_SEX:
                    tv_sex_value.setText(backData);
                    break;
                case REQ_GET_RELATION:
                    tv_relation_value.setText(backData);
                    break;
            }
            GlobalParam.GrowingMainNeedRefresh = true;
        }
    };

    /**
     * 请求删除孩子
     */
    private void asyncDelChild() {
        String wholeUrl = AppUrl.host + AppUrl.DEL_CHILD_END;
        String requestBodyData = ParamBuild.deleteChild(mBabyItem.getId());
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                deleteChildLsner);
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
            showShortToast("删除宝宝成功！");
            GlobalParam.GrowingMainNeedRefresh = true;
            finish();
        }
    };

}
