package com.park61.moduel.firsthead;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.adapter.ClassCommentListAdapter;
import com.park61.moduel.firsthead.bean.ClassComment;
import com.park61.moduel.firsthead.bean.TeachClassNotice;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2017/3/8.
 */
public class ShowClassPhotoActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_num, tv_comt;
    private ImageView praise_img, collect_img;
    private PhotoView img_show;
    private ListView lv_comment;
    private EditText edit_comt;
    private Button btn_send, right_img;
    private View bottom_input_area, bottom_area;
    private ViewPager viewpager_photos;
    private ClassCommentListAdapter commentAdapter;
    private ArrayList<ClassComment> commentList;
    private int PAGE_NUM = 1;// 评论列表页码
    private int comtType = 0;// 评论类型 0 评论；1评论回复；2评论评论
    private Long parentId;//评论回复id

    private String bmpPath;
    private int mposition;
    private ArrayList<String> urlList;
    private String url;
    private ArrayList<View> views = new ArrayList<View>();

    private Long albumId, itemId, classId;
    private int type;//1 点赞 2 收藏
    private ArrayList<TeachClassNotice.ImgListBean> picList;
    private int keepCount;//收藏数
    private int thumbsCount;//点赞数

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_class_show_bigpic);
    }

    @Override
    public void initView() {
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_comt = (TextView) findViewById(R.id.tv_comt);
        img_show = (PhotoView) findViewById(R.id.img_show);
        praise_img = (ImageView) findViewById(R.id.praise_img);
        collect_img = (ImageView) findViewById(R.id.collect_img);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        edit_comt = (EditText) findViewById(R.id.edit_comt);
        btn_send = (Button) findViewById(R.id.btn_send);
        bottom_input_area = findViewById(R.id.bottom_input_area);
        bottom_area = findViewById(R.id.bottom_area);
        viewpager_photos = (ViewPager) findViewById(R.id.viewpager_photos);
        right_img = (Button) findViewById(R.id.right_img);
//        collect_img.setVisibility(View.GONE);
//        findViewById(R.id.tv).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        classId = getIntent().getLongExtra("classId", -1l);
        albumId = getIntent().getLongExtra("albumId", -1l);
        itemId = getIntent().getLongExtra("itemId", -1l);
        picList = (ArrayList) getIntent().getParcelableArrayListExtra("picList");
        mposition = getIntent().getIntExtra("position", -1);
        url = getIntent().getStringExtra("picUrl");
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    img_show.setImageBitmap(resource);
                    scaleImageView(img_show, resource);
                }
            });
        }
        urlList = getIntent().getStringArrayListExtra("urlList");
        if (picList != null && picList.size() > 1) {
            viewpager_photos.setVisibility(View.VISIBLE);
            img_show.setVisibility(View.GONE);
           /* for (int i = 0; i < picList.size(); i++) {
                View viewItem = LayoutInflater.from(mContext).inflate(R.layout.showbigpic_sample_vp_item, null);
                PhotoView img_big_photo = (PhotoView) viewItem.findViewById(R.id.img_big_photo);
                ImageLoader.getInstance().displayImage(urlList.get(i), img_big_photo, options);
                //使图片实现可以放大缩小的功能
                //PhotoViewAttacher mAttacher=new PhotoViewAttacher(img_big_photo);
                views.add(viewItem);
            }*/
            viewpager_photos.setAdapter(new SamplePagerAdapter());
            tv_num.setText((mposition + 1) + "/" + urlList.size());//图片位置
            viewpager_photos.setOnPageChangeListener(mOnPageChangeListener);
            viewpager_photos.setCurrentItem(mposition);
        }
        commentList = new ArrayList<ClassComment>();
        commentAdapter = new ClassCommentListAdapter(mContext, commentList);
        lv_comment.setAdapter(commentAdapter);

        asyncGetCommentList();
        asyncDetailNotice();
    }

    /**
     * 页面切换监听
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mposition = position;
            tv_num.setText((position + 1) + "/" + urlList.size());//图片位置
            viewpager_photos.setCurrentItem(position);
            itemId = picList.get(position).getId();
            if (picList.get(position).isPraiseStatus()) {
                praise_img.setImageResource(R.drawable.dianzanhuangse_yellow);
            } else {
                praise_img.setImageResource(R.drawable.dianzanbaise_white);
            }
            asyncGetCommentList();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        // 覆写该方法实现轮播效果的暂停和恢复
        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动页面时执行此处
                    break;
                case ViewPager.SCROLL_STATE_IDLE:// 未拖动页面时执行此处
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void initListener() {
        tv_comt.setOnClickListener(this);
        praise_img.setOnClickListener(this);
        collect_img.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        lv_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               /* parentId = commentList.get(position).getId();
                showComtArea(1);*/
            }
        });
        right_img.setVisibility(View.GONE);
        right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String shareUrl = "http://m.61park.cn/officePage/albumDetail.do?albumId=" + albumId + "&itemsId=" + itemsId;
                String picUrl = urlList.get(viewpager_photos.getCurrentItem());
                String title = getString(R.string.app_name);
                String description = "店铺相册";
                showShareDialog(shareUrl, picUrl, title, description);*/
            }
        });
    }

    /**
     * 缩放到适应屏幕
     */
    public void scaleImageView(ImageView imgView, Bitmap bmp) {
        Matrix matrix = new Matrix();
        Matrix currentMaritx = new Matrix();
        currentMaritx.set(imgView.getImageMatrix());// 记录ImageView当期的移动位置
        float scale = DevAttr.getScreenWidth(mContext) / bmp.getWidth();// 放大倍数
        matrix.set(currentMaritx);
        matrix.postScale(scale, scale);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_comt:
                showComtArea(0);
                break;
            case R.id.praise_img:
                type = 1;
                if (!picList.get(mposition).isPraiseStatus()) {
                    asyncPraiseColletion();
                }
                break;
            case R.id.collect_img:
                type = 2;
                if (!picList.get(mposition).isCollectStatus()) {
                    asyncAddPhotoFocus();
                }
                break;
            case R.id.btn_send:
                if (TextUtils.isEmpty(edit_comt.getText().toString().trim())) {
                    showShortToast("您未填写评论！");
                    return;
                }
                switch (comtType) {
                    case 0:
                        asyncCommitComment(itemId);
                        break;
                    case 1:
                        asyncCommitComment(itemId);
                        break;
                }
        }
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = bottom_input_area;
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                bottom_area.setVisibility(View.VISIBLE);
                bottom_input_area.setVisibility(View.GONE);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 点击评论按钮，显示评论输入框
     *
     * @param curComtType 当前评论类型
     */
    public void showComtArea(int curComtType) {
        comtType = curComtType;
        // 点击评论按钮，显示评论输入框
        bottom_input_area.setVisibility(View.VISIBLE);
        bottom_area.setVisibility(View.GONE);
        edit_comt.requestFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(edit_comt, 0);
    }

    private void asyncDetailNotice() {
        String wholeUrl = AppUrl.host + AppUrl.detailNotice;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("noticeId", albumId);
        map.put("classId", classId);
        map.put("noticeType", 2);
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
            for (int i = 0; i < itemInfo.getImgList().size(); i++) {
                picList.get(i).setPraiseStatus(itemInfo.getImgList().get(i).isPraiseStatus());
            }
            if (picList.get(mposition).isPraiseStatus()) {
                praise_img.setImageResource(R.drawable.dianzanhuangse_yellow);
            } else {
                praise_img.setImageResource(R.drawable.dianzanbaise_white);
            }
        }
    };

    /**
     * 请求提交评论
     */
    private void asyncCommitComment(Long itemId) {
        String wholeUrl = AppUrl.host + AppUrl.addComment;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bizId", albumId);
        map.put("itemId", itemId);
        map.put("source", 5);
        map.put("content", edit_comt.getText().toString().trim());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtListener);
    }

    BaseRequestListener comtListener = new JsonRequestListener() {
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
            showShortToast("评论成功！");
            // 清空评论并隐藏评论框
            edit_comt.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(edit_comt.getWindowToken(), 0);
            }
            bottom_input_area.setVisibility(View.GONE);
            bottom_area.setVisibility(View.VISIBLE);

            asyncGetCommentList();
        }
    };

    /**
     * 请求点赞
     */
    private void asyncPraiseColletion() {
        String wholeUrl = AppUrl.host + AppUrl.addPraise;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bizId", albumId);
        map.put("itemId", itemId);
        map.put("source", 5);
        map.put("type", type);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, supportListener);
    }

    BaseRequestListener supportListener = new JsonRequestListener() {

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
            if (type == 1) {
                praise_img.setImageResource(R.drawable.dianzanhuangse_yellow);
                ScaleAnimation animation = new ScaleAnimation(1, 1.5f, 1, 1.5f);
                animation.setDuration(2000);
                animation.setRepeatMode(Animation.REVERSE);//反方向执行
                praise_img.setAnimation(animation);
                animation.start();
                picList.get(mposition).setPraiseStatus(true);
            } else if (type == 2) {
                collect_img.setImageResource(R.drawable.shoucangbaise_yellow);
                ScaleAnimation animation = new ScaleAnimation(1, 1.5f, 1, 1.5f);
                animation.setDuration(2000);
                animation.setRepeatMode(Animation.REVERSE);//反方向执行
                collect_img.setAnimation(animation);
                animation.start();
            }
        }
    };

    /**
     * 请求添加收藏
     */
    private void asyncAddPhotoFocus() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.addPhotoFocus;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("photoId", itemId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, afListener);
    }

    BaseRequestListener afListener = new JsonRequestListener() {

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
            collect_img.setImageResource(R.drawable.shoucangbaise_yellow);
            ScaleAnimation animation = new ScaleAnimation(1, 1.5f, 1, 1.5f);
            animation.setDuration(2000);
            animation.setRepeatMode(Animation.REVERSE);//反方向执行
            collect_img.setAnimation(animation);
            animation.start();
            picList.get(mposition).setCollectStatus(true);
        }
    };

    /**
     * 单张图片评论列表
     */
    protected void asyncGetCommentList() {
        String wholeUrl = AppUrl.host + AppUrl.commentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", 5);//评论类型 4：通知 5：图片
        map.put("itemId", itemId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ArrayList<ClassComment> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                tv_comt.setText("0条评论...");
                commentList.clear();
                commentAdapter.notifyDataSetChanged();
            } else {
                // 首次加载清空所有项列表,并重置控件为可下滑
                if (PAGE_NUM == 1) {
                    commentList.clear();
                }
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject actJot = actJay.optJSONObject(i);
                    ClassComment c = gson.fromJson(actJot.toString(), ClassComment.class);
                    currentPageList.add(c);
                }
                commentList.addAll(currentPageList);
                tv_comt.setText(jsonResult.optInt("totalSize") + "条评论...");
                commentAdapter.notifyDataSetChanged();
            }
            if (GlobalParam.userToken != null) {
                asyncCheckPhotoFocus();
            }
        }
    };

    /**
     * 请求收藏状态
     */
    private void asyncCheckPhotoFocus() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.checkPhotoFocus;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("photoId", itemId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, statusListener);
    }

    BaseRequestListener statusListener = new JsonRequestListener() {

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
            if (jsonResult.optString("data").equals("true")) {//已收藏
                collect_img.setImageResource(R.drawable.shoucangbaise_yellow);
                collect_img.setClickable(false);
            } else {
                collect_img.setImageResource(R.drawable.shoucangbaise_img);
                collect_img.setClickable(true);
            }
        }
    };


    class SamplePagerAdapter extends PagerAdapter {

       /* private static final int[] sDrawables = { R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
                R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper };*/

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            //photoView.setImageResource(sDrawables[position]);
            ImageManager.getInstance().displayImg(photoView, urlList.get(position));
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
