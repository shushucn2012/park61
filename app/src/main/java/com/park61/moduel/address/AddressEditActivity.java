package com.park61.moduel.address;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.RegexValidator;
import com.park61.moduel.acts.bean.CityBean;
import com.park61.moduel.address.bean.AddressDetailItem;
import com.park61.moduel.address.bean.TownBean;
import com.park61.moduel.address.dialog.SelecCityDialog;
import com.park61.moduel.address.dialog.SelecCityDialog.OnCitySelect;
import com.park61.moduel.address.dialog.SelecCountyDialog;
import com.park61.moduel.address.dialog.SelecCountyDialog.OnCountySelect;
import com.park61.moduel.address.dialog.SelecProvinceDialog;
import com.park61.moduel.address.dialog.SelecProvinceDialog.OnProvinceSelect;
import com.park61.moduel.address.dialog.SelecTownDialog;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑地址
 *
 * @author Lucia
 */
public class AddressEditActivity extends BaseActivity implements
        OnClickListener, OnProvinceSelect, OnCitySelect, OnCountySelect, SelecTownDialog.OnTownSelect {
    private String goodReceiverName;// 收货人姓名
    private String goodReceiverAddress;// 收货人地址
    private String goodReceiverMobile;// 收货人手机号
    private String goodReceiverProvinceName;// 省份
    private String goodReceiverCityName;// 城市
    private String goodReceiverCountyName;// 区域
    private String goodReceiverTownName;//乡镇名
    private Long goodReceiverTownId;//乡镇id
    private Long goodReceiverProvinceId; // 省份id
    private Long goodReceiverCityId; // 城市id
    private Long goodReceiverCountyId; // 区域id
    private Long id; // 选中条目的id

    public static final Integer GOODRECEIVERTYPE = 0;// 快递类型 传0
    public static final Long GOODRECEIVERCOUNTRYID = 1L; // 国家id
    private Integer ISDEFAULT = 1;// 是否默认收货地址 0-否 1-是

    private EditText et_name, et_number, et_address;
    private TextView tv_province, tv_city, tv_county, tv_town, tv_province_details, tv_del;
    private View area_rl;
    private Button btn_commit;
    private List<CityBean> mList = new ArrayList<>();
    private List<TownBean> townList = new ArrayList<>();
    private int hasChild; //子节点个数，0代表没有子节点

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_address_edit);
    }

    @Override
    public void initView() {
        et_name = (EditText) findViewById(R.id.name_value);
        et_number = (EditText) findViewById(R.id.number_value);
        et_address = (EditText) findViewById(R.id.address_detail_value);
        tv_province = (TextView) findViewById(R.id.tv_province);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_county = (TextView) findViewById(R.id.tv_county);
        tv_town = (TextView) findViewById(R.id.tv_town);
        tv_province_details = (TextView) findViewById(R.id.tv_province_details);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        area_rl = findViewById(R.id.area_rl);
        tv_del = (TextView) findViewById(R.id.tv_del);
    }

    @Override
    public void initData() {
        id = getIntent().getLongExtra("id", -1L);
        asyncDetailAddress();
    }

    @Override
    public void initListener() {
        tv_province.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        tv_county.setOnClickListener(this);
        tv_town.setOnClickListener(this);
        area_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncGetProvince();
            }
        });
        tv_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "您确定要删除吗？", "取消", "确定", null, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        delAddress(id);
                    }
                });
            }
        });
        btn_commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取编辑页面编辑后各参数的值
                goodReceiverName = et_name.getText().toString();
                goodReceiverAddress = et_address.getText().toString();
                goodReceiverMobile = et_number.getText().toString();
                goodReceiverProvinceName = tv_province.getText().toString();
                goodReceiverCityName = tv_city.getText().toString();
                goodReceiverCountyName = tv_county.getText().toString();
                goodReceiverTownName = tv_town.getText().toString();
                if (!validateAddress())
                    return;
                updateAddress();
            }
        });
    }

    protected boolean validateAddress() {
        if (TextUtils.isEmpty(goodReceiverName)) {
            showShortToast("请输入收货人姓名");
            return false;
        }
        if (goodReceiverName.length() < 2) {
            showShortToast("姓名不能小于2个字符!");
            return false;
        }
        if (TextUtils.isEmpty(goodReceiverMobile)) {
            showShortToast("请输入手机号码");
            return false;
        }
        if (!RegexValidator.isMobile(goodReceiverMobile)) {
            showShortToast("手机号输入有误！");
            return false;
        }
        if (TextUtils.isEmpty(goodReceiverCityName)) {
            showShortToast("请选择省份对应的城市");
            return false;
        }
        if (TextUtils.isEmpty(goodReceiverCountyName)) {
            showShortToast("请选择城市对应的区域");
            return false;
        }
        if (hasChild != 0 && TextUtils.isEmpty(goodReceiverTownName)) {
            showShortToast("请选择区域对应的乡镇");
            return false;
        }
        if (TextUtils.isEmpty(goodReceiverAddress)) {
            showShortToast("请输入详细地址");
            return false;
        }
        return true;
    }

    /**
     * 更新地址
     */
    private void updateAddress() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.updateAddressByUserId;
        String requestBodyData = ParamBuild.updateDetailAddress(
                goodReceiverName, goodReceiverAddress, goodReceiverMobile,
                goodReceiverProvinceName, goodReceiverCityName,
                goodReceiverCountyName, goodReceiverTownName, GOODRECEIVERTYPE,
                GOODRECEIVERCOUNTRYID, goodReceiverProvinceId,
                goodReceiverCityId, goodReceiverCountyId, goodReceiverTownId, ISDEFAULT, id);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, updateAddressListener);
    }

    BaseRequestListener updateAddressListener = new JsonRequestListener() {

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
            setResult(RESULT_OK);
            finish();
        }
    };

    /**
     * 根据选中条目id获取详细地址
     */
    private void asyncDetailAddress() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getAddressById;
        String requestBodyData = ParamBuild.getAddressById(id);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
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
            AddressDetailItem b = gson.fromJson(jsonResult.toString(), AddressDetailItem.class);
            et_name.setText(b.getGoodReceiverName());
            et_number.setText(b.getGoodReceiverMobile());
            et_address.setText(b.getGoodReceiverAddress());
            tv_province.setText(b.getGoodReceiverProvinceName());
            tv_city.setText(b.getGoodReceiverCityName());
            tv_county.setText(b.getGoodReceiverCountyName());
            if (!TextUtils.isEmpty(b.getGoodReceiverTownName())) {
                tv_town.setText(b.getGoodReceiverTownName());
            } else {
                tv_town.setVisibility(View.GONE);
            }
            goodReceiverProvinceId = b.getGoodReceiverProvinceId();
            goodReceiverCityId = b.getGoodReceiverCityId();
            goodReceiverCountyId = b.getGoodReceiverCountyId();
            goodReceiverTownId = b.getGoodReceiverTownId();
            ISDEFAULT = b.getIsDefault();
            tv_province_details.setText(tv_province.getText().toString()
                    + tv_city.getText().toString()
                    + tv_county.getText().toString()
                    + tv_town.getText().toString());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_province:
                asyncGetProvince();

                break;
            case R.id.tv_city:
                asyncGetCity(goodReceiverProvinceId);

                break;
            case R.id.tv_county:
                asyncGetCounty(goodReceiverCityId);

                break;
            case R.id.tv_town:
                asyncGetTown(goodReceiverCountyId);

                break;
        }
    }

    /**
     * 获取省份
     */
    private void asyncGetProvince() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getProvinces;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, provinceListener);
    }

    BaseRequestListener provinceListener = new JsonRequestListener() {

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
            mList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CityBean p = gson.fromJson(jot.toString(), CityBean.class);
                mList.add(p);
            }
            SelecProvinceDialog dialog = new SelecProvinceDialog(mContext, mList);
            dialog.showDialog();
            dialog.setOnProvinceSelectLsner(AddressEditActivity.this);

        }
    };

    /**
     * 根据省份id获取城市
     *
     * @param goodReceiverProvinceId 省份id
     */
    private void asyncGetCity(Long goodReceiverProvinceId) {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getCitiesByProvinceId;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodReceiverProvinceId", goodReceiverProvinceId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, cityListener);
    }

    BaseRequestListener cityListener = new JsonRequestListener() {

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
            mList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CityBean city = gson.fromJson(jot.toString(), CityBean.class);
                mList.add(city);
            }
            SelecCityDialog dialog = new SelecCityDialog(mContext, mList);
            dialog.showDialog();
            dialog.setOnCitySelectLsner(AddressEditActivity.this);
        }
    };

    /**
     * 根据城市id获取区域
     *
     * @param goodReceiverCityId 城市id
     */
    private void asyncGetCounty(Long goodReceiverCityId) {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getCountysByCityId;
        String requestBodyData = ParamBuild.getGoodReceiverCityId(goodReceiverCityId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, countyListener);
    }

    BaseRequestListener countyListener = new JsonRequestListener() {

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
            JSONArray jay = jsonResult.optJSONArray("counties");
            mList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CityBean county = gson.fromJson(jot.toString(), CityBean.class);
                mList.add(county);
            }
            SelecCountyDialog dialog = new SelecCountyDialog(mContext, mList);
            dialog.showDialog();
            dialog.setOnCountySelectLsner(AddressEditActivity.this);
        }
    };

    /**
     * 通过区域id获取对应乡镇名
     */
    private void asyncGetTown(Long goodReceiverCountyId) {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getTownsByCountyId;
        String requestBodyData = ParamBuild.goodReceiverCountyId(goodReceiverCountyId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, townListener);
    }

    BaseRequestListener townListener = new JsonRequestListener() {

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
            JSONArray jay = jsonResult.optJSONArray("towns");
            townList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                TownBean town = gson.fromJson(jot.toString(), TownBean.class);
                townList.add(town);
            }
            SelecTownDialog dialog = new SelecTownDialog(mContext, townList);
            dialog.showDialog();
            dialog.setOnTownSelectLsner(AddressEditActivity.this);// 选择乡镇点击事件
        }
    };

    // 选择省份名
    @Override
    public void onSelectProvince(int position) {
        tv_province.setText(mList.get(position).getName());
        goodReceiverProvinceId = mList.get(position).getId();// 获取省份id
        tv_city.setText("");
        tv_county.setText("");
        tv_town.setText("");
        et_address.setText("");
        asyncGetCity(goodReceiverProvinceId);
    }

    // 选择城市名
    @Override
    public void onSelectCity(int position) {
        tv_city.setText(mList.get(position).getName());
        goodReceiverCityId = mList.get(position).getId();// 获取城市id
        tv_county.setText("");
        tv_town.setText("");
        et_address.setText("");
        asyncGetCounty(goodReceiverCityId);
    }

    // 选择区域名
    @Override
    public void onSelectCounty(int position) {
        tv_county.setText(mList.get(position).getName());
        goodReceiverCountyId = mList.get(position).getId();// 获取区域id
        hasChild = mList.get(position).getHasChild();
        logout("======hasChild=======" + hasChild + "");
        tv_town.setText("");
        et_address.setText("");
        if (hasChild != 0) {
            asyncGetTown(goodReceiverCountyId);
            tv_town.setVisibility(View.VISIBLE);
        } else {
            tv_town.setVisibility(View.GONE);
            goodReceiverTownId = 0l;
            tv_province_details.setText(tv_province.getText().toString()
                    + tv_city.getText().toString()
                    + tv_county.getText().toString());
        }
    }

    @Override
    public void onTownSelect(int position) {
        tv_town.setText(townList.get(position).getName());
        goodReceiverTownId = townList.get(position).getId();
        et_address.setText("");
        tv_province_details.setText(tv_province.getText().toString()
                + tv_city.getText().toString()
                + tv_county.getText().toString()
                + tv_town.getText().toString());
    }

    /**
     * 删除地址信息
     *
     * @param itemId 选择删除项的id
     */
    private void delAddress(Long itemId) {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.delAddressById;
        String requestBodyData = ParamBuild.getAddressById(itemId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, delListener);
    }

    BaseRequestListener delListener = new JsonRequestListener() {

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
            showShortToast("删除成功");
            finish();
        }
    };
}
