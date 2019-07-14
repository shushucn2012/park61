package com.park61.widget.pw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.park61.R;
import com.park61.common.json.NullStringToEmptyAdapterFactory;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.acts.bean.CityBean;
import com.park61.moduel.address.adapter.DeAddrAdapter;
import com.park61.moduel.address.adapter.SelectCityDialogListAdapter;
import com.park61.moduel.address.adapter.SelectProvinceDialogListAdapter;
import com.park61.moduel.address.bean.AddressDetailItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.StringNetRequest;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.CommonProgressDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级导航窗口
 *
 * @author super
 */
public class GoodsDetailsDeAddrPopWin extends PopupWindow {
    private View toolView;
    private ListView lv_deaddr,lv_province,lv_city;
    public CommonProgressDialog commonProgressDialog;
    private OnShopSelectLsner mOnSelectLsner;
    private View mcover,area_deaddr,area_other,left_img,stick0,stick1,img_close_cover;
    private Button btn_other;
    private TextView tv_c ,tv_p;

    private Context mContext;
    private List<AddressDetailItem> deAddrList;
    private List<CityBean> pList,cList;
    private DeAddrAdapter mDeAddrAdapter;
    private SelectProvinceDialogListAdapter pAdapter;
    private SelectCityDialogListAdapter cAdapter;
    private StringNetRequest netRequest;
    private Gson gson;

    public GoodsDetailsDeAddrPopWin(Context context, OnShopSelectLsner lsner, View cover) {
        super(context);
        mContext = context;
        this.mcover = cover;
        this.mOnSelectLsner = lsner;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_gdsdetails_deaddr_layout, null);
        // 初始化视图
        lv_deaddr = (ListView) toolView.findViewById(R.id.lv_deaddr);
        lv_province = (ListView) toolView.findViewById(R.id.lv_province);
        lv_city = (ListView) toolView.findViewById(R.id.lv_city);
        commonProgressDialog = new CommonProgressDialog(mContext);
        mcover.setVisibility(View.VISIBLE);
        btn_other = (Button)toolView.findViewById(R.id.btn_other);
        area_deaddr = toolView.findViewById(R.id.area_deaddr);
        area_other = toolView.findViewById(R.id.area_other);
        left_img = toolView.findViewById(R.id.left_img);
        img_close_cover = toolView.findViewById(R.id.img_close_cover);
        tv_c = (TextView) toolView.findViewById(R.id.tv_c);
        tv_p= (TextView) toolView.findViewById(R.id.tv_p);
        stick0 = toolView.findViewById(R.id.stick0);
        stick1 = toolView.findViewById(R.id.stick1);
        // 初始化数据
        deAddrList = new ArrayList<AddressDetailItem>();
        mDeAddrAdapter = new DeAddrAdapter(mContext, deAddrList);
        lv_deaddr.setAdapter(mDeAddrAdapter);
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

        pList = new ArrayList<CityBean>();
        pAdapter = new SelectProvinceDialogListAdapter(pList, mContext);
        lv_province.setAdapter(pAdapter);

        cList = new ArrayList<CityBean>();
        cAdapter = new SelectCityDialogListAdapter(cList, mContext);
        lv_city.setAdapter(cAdapter);

        netRequest = StringNetRequest.getInstance(mContext);
        netRequest.setMainContext(mContext);
        asyncGetAllAddress();
        // 初始化监听
        lv_deaddr.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AddressDetailItem ad = deAddrList.get(position);
                /*String addrStr = ad.getGoodReceiverProvinceName()
                        + ad.getGoodReceiverCityName()
                        + ad.getGoodReceiverCountyName()
                        + ad.getGoodReceiverAddress();*/
                mDeAddrAdapter.selectItem(position);
                updateDefaultAddress(ad.getId());
            }
        });
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                left_img.setVisibility(View.VISIBLE);
                area_deaddr.setVisibility(View.GONE);
                area_other.setVisibility(View.VISIBLE);
                asyncGetProvince();
            }
        });
        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_p.setText("请选择");
                left_img.setVisibility(View.GONE);
                area_deaddr.setVisibility(View.VISIBLE);
                area_other.setVisibility(View.GONE);

                tv_c.setVisibility(View.INVISIBLE);
                stick0.setVisibility(View.VISIBLE);
                stick1.setVisibility(View.INVISIBLE);

                lv_province.setVisibility(View.VISIBLE);
                lv_city.setVisibility(View.GONE);
            }
        });
        lv_province.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_p.setText(pList.get(i).getProvinceName());
                tv_c.setVisibility(View.VISIBLE);
                stick0.setVisibility(View.INVISIBLE);
                stick1.setVisibility(View.VISIBLE);

                lv_province.setVisibility(View.GONE);
                lv_city.setVisibility(View.VISIBLE);
                asyncGetCity(pList.get(i).getId());

                pAdapter.selectItem(i);
            }
        });
        lv_city.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mOnSelectLsner.onShopSelect(cList.get(i).getId(), cList.get(i).getCityName());
            }
        });
        tv_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stick0.setVisibility(View.VISIBLE);
                stick1.setVisibility(View.INVISIBLE);

                lv_province.setVisibility(View.VISIBLE);
                lv_city.setVisibility(View.GONE);
            }
        });
        tv_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stick0.setVisibility(View.INVISIBLE);
                stick1.setVisibility(View.VISIBLE);

                lv_province.setVisibility(View.GONE);
                lv_city.setVisibility(View.VISIBLE);
            }
        });
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.getScreenWidth(mContext));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.7));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        this.setAnimationStyle(R.style.AnimBottom);
        img_close_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                mcover.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取用户所有的地址列表(不区分城市-收货地址管理)
     */
    private void asyncGetAllAddress() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ADDR;
        String requestBodyData = "";
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
            if("0000000002".equals(errorCode)) {//不显示登陆失效提示
                return;
            }
            showShortToast(errorMsg);

        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list"); // 获取名为list的Json数组
            deAddrList.clear(); // 清除mList集合，避免重复加载
            for (int i = 0; i < jay.length(); i++) { // 遍历Json数组
                JSONObject jot = jay.optJSONObject(i);
                // 把Json数据解析成对象
                AddressDetailItem b = gson.fromJson(jot.toString(), AddressDetailItem.class);
                deAddrList.add(b);
            }
            mDeAddrAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 获取省份名
     */
    private void asyncGetProvince() {
        String wholeUrl = AppUrl.host + AppUrl.GET_PROVINCE_LIST;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                provinceListener);
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
            pList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CityBean p = gson.fromJson(jot.toString(), CityBean.class);
                pList.add(p);
            }
            pAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 通过省份id获取对应城市名
     */
    private void asyncGetCity(Long goodReceiverProvinceId) {
        String wholeUrl = AppUrl.host + AppUrl.GET_CITY_BY_PROVINCE;
        String requestBodyData = ParamBuild.getPId(goodReceiverProvinceId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                cityListener);
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
            cList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                CityBean city = gson.fromJson(jot.toString(), CityBean.class);
                cList.add(city);
            }
            cAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 更新默认地址
     *
     * @param selectId
     *            默认地址id
     */
    private void updateDefaultAddress(Long selectId) {
        String wholeUrl = AppUrl.host + AppUrl.UPDATE_DEFAULT_ADDR;
        String requestBodyData = ParamBuild.getAddressById(selectId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                defaultListener);
    }

    BaseRequestListener defaultListener = new JsonRequestListener() {

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
            mOnSelectLsner.onShopSelect(null, "");
        }
    };

    public void showDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                return;
            } else {
                commonProgressDialog.showDialog(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.dialogDismiss();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public interface OnShopSelectLsner {
        public void onShopSelect(Long id, String name);
    }
}
