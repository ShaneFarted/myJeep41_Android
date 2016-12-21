package cn.jeeper41.jeeper.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jeeper41.jeeper.R;

/**
 * Created by joker on 2016/11/15.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态

    private View mHeaderView;
    private TextView tvTitle;
    private TextView tvTime;
    private ProgressBar pbProgress;
    private ImageView ivArrow;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private int startY = -1;// 滑动起点的y坐标
    private int endY;
    private int measuredHeight;
    private int mFooterViewHeight;
    private View footerView;

    /** 是否启用动画，默认treu*/
    private Boolean enableAnim = true;
    private Boolean enableLoadMore = true;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refeeash_header, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);


        mHeaderView.measure(0, 0);//先测量再拿到它的高度
        measuredHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -measuredHeight, 0, 0);

        initArrowAnim();
        tvTime.setText(tvTime.getText()+ getCurrentTime());
    }

    /**
     * 初始化脚布局
     */
    public void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.refreash_listview_footer, null);
        this.addFooterView(footerView);
        footerView.measure(0, 0);
        mFooterViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -mFooterViewHeight, 0, 0);//默认隐藏脚布局

        this.setOnScrollListener(this);

    }

    private boolean isLoadingMOre;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(enableLoadMore) {
            if (scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_IDLE) {
                if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMOre) {//滑倒最后
                    footerView.setPadding(0, 0, 0, 0);
                    setSelection(getCount() - 1);//改变ListView的显示位置
                    isLoadingMOre = true;
                    if (mListener != null) {
                        mListener.onLoadMore();
                    }
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(enableAnim) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = (int) ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (startY == -1) {//有时候不会响应 MotionEvent.ACTION_DOWN 事件,这是要重新获取startY坐标
                        startY = (int) ev.getRawY();
                    }
                    //当正在刷新的时候,跳出循环,不再执行下面逻辑
                    if (mCurrrentState == STATE_RELEASE_REFRESH) {
                        break;

                    }

                    endY = (int) ev.getRawY();
                    int dy = endY - startY;//计算手指滑动距离
                    if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
                        int padding = dy - measuredHeight;//计算padding
                        mHeaderView.setPadding(0, padding, 0, 0);//设置当前padding

                        if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {
                            mCurrrentState = STATE_RELEASE_REFRESH;
                            refreshState();

                        } else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
                            mCurrrentState = STATE_PULL_REFRESH;
                            refreshState();
                        }
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    startY = -1;//手指抬起重置
                    //当状态是松开刷新时抬起了手指,正在刷新
                    if (mCurrrentState == STATE_RELEASE_REFRESH) {
                        mCurrrentState = STATE_REFRESHING;// 正在刷新
                        mHeaderView.setPadding(0, 0, 0, 0);// 显示
                        refreshState();
                    } else if (mCurrrentState == STATE_PULL_REFRESH) {
                        mHeaderView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = format.format(new Date());
        return currentTime;
    }

    private void initArrowAnim() {
        //初始化箭头动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(500);
        animUp.setFillAfter(true);//保持状态

        //箭头向下动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(500);
        animDown.setFillAfter(true);

    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText(R.string.STATE_PULL_REFRESH);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText(R.string.STATE_RELEASE_REFRESH);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText(R.string.STATE_REFRESHING);
                ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
                ivArrow.setVisibility(View.GONE);
                pbProgress.setVisibility(View.VISIBLE);
                if (mListener != null) {
                    mListener.onRefreash();
                }
                break;

            default:
                break;
        }
    }

    OnRefreashListener mListener;

    public void setOnRefreashListener(OnRefreashListener listener) {
        mListener = listener;

    }

    public Boolean getEnableAnim() {
        return enableAnim;
    }

    public void setEnableAnim(Boolean enableAnim) {
        this.enableAnim = enableAnim;
    }

    public Boolean getEnableLoadMore() {
        return enableLoadMore;
    }

    public void setEnableLoadMore(Boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
    }


    public interface OnRefreashListener {
        void onRefreash();
        void onLoadMore();
    }

    /**
     * 收起下拉刷新的控件
     */
    public void onRefreashComplete() {

        if (isLoadingMOre) {
            footerView.setPadding(0, -mFooterViewHeight, 0, 0);//隐藏脚布局
            isLoadingMOre = false;

        } else {

            mCurrrentState = STATE_PULL_REFRESH;
            tvTitle.setText(R.string.STATE_PULL_REFRESH);
            ivArrow.setVisibility(View.VISIBLE);
            pbProgress.setVisibility(View.GONE);

            mHeaderView.setPadding(0, -measuredHeight, 0, 0);//隐藏
        }

    }

    public void noFooterView() {
        footerView.setPadding(0, mFooterViewHeight, 0, 0);//隐藏脚布局
    }
    public void refresh(){
        mHeaderView.setPadding(0, 0, 0, 0);// 显示
        mCurrrentState = STATE_REFRESHING;
        refreshState();
    }

}
