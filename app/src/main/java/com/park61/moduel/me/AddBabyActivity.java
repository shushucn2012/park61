package com.park61.moduel.me;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.picselect.utils.SelectPicPopupWindow;
import com.park61.widget.wheel.DateTimeDialog;
import com.park61.widget.wheel.DateTimeDialog.OnChosenListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class AddBabyActivity extends BaseActivity implements OnClickListener {

    private static final int REQ_GET_PIC = 0;
    private static final int REQ_GET_RELATION = 3;
    private static final int REQ_GET_BIRTHDAY = 4;
    private static final int REQ_GET_SEX = 5;
    private Calendar calendar;
    private String backData = "";
    private String name;// 姓名
    private String birthday;// 生日
    private String relationId;// 关系
    private String sex;// 性别
    private String petName;// 小名
    private String pictureUrl;// 图片

    private View pic_area, birthday_area, sex_area, relation_area, invite_area;
    private EditText tv_nickname_value, tv_name_value;
    private TextView tv_birthday_value, tv_sex_value, tv_relation_value;
    private ImageView img_pic;
    private DateTimeDialog myTimeDialog;
    private Button btn_commit;
    //private boolean isSelectPhoto = false;
    
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_add_baby);
    }

    @Override
    public void initView() {
        pic_area = findViewById(R.id.pic_area);
        birthday_area = findViewById(R.id.birthday_area);
        sex_area = findViewById(R.id.sex_area);
        relation_area = findViewById(R.id.relation_area);
        invite_area = findViewById(R.id.invite_area);

        img_pic = (ImageView) findViewById(R.id.img_pic);
        tv_nickname_value = (EditText) findViewById(R.id.tv_nickname_value);
        tv_name_value = (EditText) findViewById(R.id.tv_name_value);
        tv_birthday_value = (TextView) findViewById(R.id.tv_birthday_value);
        tv_sex_value = (TextView) findViewById(R.id.tv_sex_value);
        tv_relation_value = (TextView) findViewById(R.id.tv_relation_value);

        myTimeDialog = new DateTimeDialog(mContext);
        btn_commit = (Button) findViewById(R.id.btn_commit);
    }

    @Override
    public void initData() {
        calendar = Calendar.getInstance();
    }

    @Override
    public void initListener() {
        pic_area.setOnClickListener(this);
        // nickname_area.setOnClickListener(this);
        // name_area.setOnClickListener(this);
        birthday_area.setOnClickListener(this);
        sex_area.setOnClickListener(this);
        relation_area.setOnClickListener(this);
        invite_area.setOnClickListener(this);
        btn_commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validateBaby())
                    return;
                asyncAddChild();
            }
        });
    }

    protected boolean validateBaby() {
        name = tv_name_value.getText().toString().replace("未填写", "").trim();
        birthday = tv_birthday_value.getText().toString().replace("未填写", "");
        String sexStr = tv_sex_value.getText().toString();
        if (sexStr.equals("男"))
            sex = "0";
        else if (sexStr.equals("女"))
            sex = "1";
        petName = tv_nickname_value.getText().toString().replace("未填写", "");
        if (TextUtils.isEmpty(name)) {
            showShortToast("姓名不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(birthday)) {
            showShortToast("生日不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(relationId)) {
            showShortToast("与宝宝的关系不能为空！");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int reqCode = -1;
        Intent editIt = new Intent(mContext, MeEditActivity.class);
        if (v.getId() == R.id.pic_area) {
            Intent it = new Intent(mContext, SelectPicPopupWindow.class);
            startActivityForResult(it, REQ_GET_PIC);
            return;
        } else if (v.getId() == R.id.birthday_area) {
            String birthday = tv_birthday_value.getText().toString();
            if (!TextUtils.isEmpty(birthday) && !("未填写").equals(birthday))
                calendar.setTime(DateTool.getDateByDay(birthday));
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
                    updateChildView(REQ_GET_BIRTHDAY);
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
        backData = data.getStringExtra("new_data");
        if (requestCode == REQ_GET_PIC) {
            String picPath = data.getStringExtra("path");
            Bitmap bmp = ImageManager.getInstance().readFileBitMap(picPath);
            img_pic.setImageBitmap(bmp);
            new AliyunUploadUtils(mContext).uploadPic(picPath,
                    new OnUploadFinish() {

                        @Override
                        public void onSuccess(String picUrl) {
                            pictureUrl = picUrl;
                        }

                        @Override
                        public void onError() {
                            showShortToast("宝宝头像上传失败请重试！");
                        }
                    });
            return;
        } else if (requestCode == REQ_GET_RELATION) {
            relationId = data.getStringExtra("relationId");
        }
        updateChildView(requestCode);
    }

    DialogInterface.OnClickListener sexListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            backData = mContext.getResources().getStringArray(R.array.sex)[which];
            dialog.dismiss();
            updateChildView(REQ_GET_SEX);
        }
    };

    protected void updateChildView(int requestCode) {
        switch (requestCode) {
            case REQ_GET_BIRTHDAY:
                tv_birthday_value.setText(backData);
                tv_birthday_value.setTextColor(mContext.getResources().getColor(
                        R.color.g666666));
                break;
            case REQ_GET_SEX:
                tv_sex_value.setText(backData);
                tv_sex_value.setTextColor(mContext.getResources().getColor(
                        R.color.g666666));
                break;
            case REQ_GET_RELATION:
                tv_relation_value.setText(backData);
                tv_relation_value.setTextColor(mContext.getResources().getColor(
                        R.color.g666666));
                break;
        }
    }

    /**
     * 添加孩子
     */
    private void asyncAddChild() {
        String wholeUrl = AppUrl.host + AppUrl.ADD_CHILD_END;
        String requestBodyData = ParamBuild.addChild(name, birthday,
                relationId, sex, petName, pictureUrl);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                listener);
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
            showShortToast("宝宝添加成功！");
            GlobalParam.GrowingMainNeedRefresh = true;
            setResult(RESULT_OK);
            finish();
        }
    };

}
