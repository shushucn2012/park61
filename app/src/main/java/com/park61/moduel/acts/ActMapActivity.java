package com.park61.moduel.acts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.FU;
import com.park61.moduel.acts.baidumap.DrivingRouteOverlay;
import com.park61.moduel.acts.baidumap.OverlayManager;
import com.park61.moduel.acts.baidumap.TransitRouteOverlay;
import com.park61.moduel.acts.baidumap.WalkingRouteOverlay;

/**
 * 演示MapView的基本用法
 */
public class ActMapActivity extends BaseActivity implements
        BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {
    @SuppressWarnings("unused")
    private static final String LTAG = ActMapActivity.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCurrentMarker;
    private LocationMode mCurrentMode;
    private MapStatus ms;
    private Marker mMarkerA;
    private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    boolean useDefaultIcon = false;
    private OverlayManager routeOverlay = null;
    private RouteLine route = null;
    private RadioGroup mtab_group;
    private String longitude, latitude;

    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_actmap);
    }

    @Override
    public void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mtab_group = (RadioGroup) findViewById(R.id.mtab_group);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        longitude = intent.getExtras().getString("act_longitude");
        latitude = intent.getExtras().getString("act_latitude");
        logout("longitude=====" + longitude);
        logout("latitude=====" + latitude);
        initPointView(FU.paseDb(latitude), FU.paseDb(longitude));
    }

    @Override
    public void initListener() {
        initTabGroup();
    }

    /**
     * 初始化标签组
     */
    private void initTabGroup() {
        mtab_group.check(R.id.rb_tab_foot);
        mtab_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                requestRoutePlan(Double.parseDouble(latitude),
                        Double.parseDouble(longitude), checkedId);
            }
        });
    }

    private void initPointView(Double latitude, Double longitude) {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        initOverlay(latitude, longitude);
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                return true;
            }
        });
    }

    public void initOverlay(Double latitude, Double longitude) {
        // add marker overlay
        LatLng llA = new LatLng(latitude, longitude);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bd)
                .zIndex(9).draggable(false);
        // 掉下动画
        ooA.animateType(MarkerAnimateType.drop);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llA);
        mBaiduMap.setMapStatus(u);
        requestRoutePlan(latitude, longitude, R.id.rb_tab_foot);
    }

    /**
     * 开始搜索线路
     *
     * @param planType 线路类型 0：驾车；1：公交；2：步行
     */
    private void requestRoutePlan(final Double latitude,
                                  final Double longitude, final int planType) {
        showDialog("正在搜索线路...");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                routePlan(latitude, longitude, planType);
            }
        }, 3000);
    }

    private void routePlan(Double latitude, Double longitude, int planType) {
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withLocation(new LatLng(
                GlobalParam.latitude, GlobalParam.longitude));
        PlanNode enNode = PlanNode
                .withLocation(new LatLng(latitude, longitude));
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        switch (planType) {
            case R.id.rb_tab_drive:
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
                        .to(enNode));
                break;
            case R.id.rb_tab_bus:
                String toCityStr = GlobalParam.chooseCityStr.replace("市", "");// 目的城市
                String fromCityStr = GlobalParam.locationCityStr.replace("市", "");// 起始城市
                mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
                        .city(fromCityStr).to(enNode).city(toCityStr));
                break;
            case R.id.rb_tab_foot:
                mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
                        .to(enNode));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        try {
            mMapView.onDestroy();
            mBaiduMap = null;
            bd.recycle();
            if (mSearch != null)
                mSearch.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        dismissDialog();
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showShortToast("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR && mBaiduMap != null) {
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            changeRoute();
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        dismissDialog();
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showShortToast("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR && mBaiduMap != null) {
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            changeRoute();
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        dismissDialog();
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showShortToast("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR && mBaiduMap != null) {
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            changeRoute();
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    /**
     * 切换路线
     */
    public void changeRoute() {
        if (routeOverlay == null) {
            return;
        }
        routeOverlay.removeFromMap();
    }

    @Override
    public void onMapClick(LatLng arg0) {

    }

    @Override
    public boolean onMapPoiClick(MapPoi arg0) {
        return false;
    }

}
