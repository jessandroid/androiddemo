package com.example.dengquan.demo.Choice;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态适配不同的行的item个数
 * 流式的分类显示
 * Created by dengquan on 2018/3/2.
 */

public class MultiSpanLayoutManager extends RecyclerView.LayoutManager {
    private MultiSpanLayoutManager instance = this;

    private int width;
    private int height;

    private int left;
    private int top;
    private int right;

    private int usedMaxWidth;   //最大宽度
    private int verticalScrollOffset = 0;   //竖直方向上的偏移量
    private int totalHeight = 0;     //计算显示的内容的高度

    private Row row = new Row();
    private List<Row> lineRows = new ArrayList<>();
    /**
     * 保存所有的Item的上下左右的偏移量信息
     */
    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    public MultiSpanLayoutManager() {
        /***
         * 设置主动测量规则，适应RecyclerView高度为WRAP_CONTENT
         */
        setAutoMeasureEnabled(true);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /***
     * 该方法主要用来获取每一个ITEM在屏幕的位置
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.e("manager", "getItemCount :" + getItemCount() + "  getChildCount :" + getChildCount() + " state :" + state.isPreLayout());
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            verticalScrollOffset = 0;
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        //在布局之前，先将所有的子View Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);
        if (getChildCount() == 0) {
            width = getWidth();
            height = getHeight();
            left = getPaddingLeft();
            top = getPaddingTop();
            right = getPaddingRight();
            usedMaxWidth = width - left - right;
        }
        totalHeight = 0;
        int cuLineTop = top;
        int cuLineWidth = 0; //当前行使用的宽度
        int itemLeft;
        int itemTop;
        int maxHeightItem = 0;
        row = new Row();
        lineRows.clear();
        allItemFrames.clear();
        removeAllViews();
        for (int i = 0; i < getItemCount(); i++) {
            View childAt = recycler.getViewForPosition(i);
            if (View.GONE == childAt.getVisibility()) {
                continue;
            }
            measureChildWithMargins(childAt, 0, 0);
            int childWidth = getDecoratedMeasuredWidth(childAt);
            int childHeight = getDecoratedMeasuredHeight(childAt);
            Log.e("manager","child width :"+childWidth+"  child height："+childHeight);
            int childUseWidth = childWidth;
            int childUseHeight = childHeight;
            if (childUseWidth + cuLineWidth <= usedMaxWidth) {
                itemLeft = left + cuLineWidth;
                itemTop = cuLineTop;
                Rect frame = allItemFrames.get(i);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
                allItemFrames.put(i, frame);
                cuLineWidth += childUseWidth;
                maxHeightItem = Math.max(maxHeightItem, childUseHeight);
                row.addViews(new Item(childUseHeight, childAt, frame));
                row.setCuTop(cuLineTop);
                row.setMaxHeight(maxHeightItem);
            } else {
                formatAboveRow();
                cuLineTop += maxHeightItem;
                totalHeight += maxHeightItem;
                itemTop = cuLineTop;
                itemLeft = left;
                Rect frame = allItemFrames.get(i);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
                allItemFrames.put(i, frame);
                cuLineWidth = childUseWidth;
                maxHeightItem = childUseHeight;
                row.addViews(new Item(childUseHeight, childAt, frame));
                row.setCuTop(cuLineTop);
                row.setMaxHeight(maxHeightItem);
            }
            if (i == getItemCount() - 1) {
                formatAboveRow();
                totalHeight += maxHeightItem;
            }

        }
        totalHeight = Math.max(totalHeight, getVerticalSpace());
        fillLayout(recycler, state);
    }

    /******
     * 对出现在屏幕上的ITEM进行展示，
     * @param recycler
     * @param state
     */
    private void fillLayout(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {   ///跳过preLayout,preLayout主要用于动画
            return;
        }
        Rect displayFrame = new Rect(getPaddingLeft(), getPaddingTop() + verticalScrollOffset, getWidth() - getPaddingRight(),
                verticalScrollOffset + (getHeight() - getPaddingBottom()));
        for (int j = 0; j < lineRows.size(); j++) {
            Row row = lineRows.get(j);
            float lineTop = row.cuTop;
            float lineBottom = lineTop + row.maxHeight;
            if (lineTop < displayFrame.bottom && displayFrame.top < lineBottom) {
                //如果该行在屏幕内，放置ITEM
                List<Item> views = row.views;
                for (int i = 0; i < views.size(); i++) {
                    View scrap = views.get(i).view;
                    measureChildWithMargins(scrap, 0, 0);
                    addView(scrap);
                    Rect frame = views.get(i).rect;
                    if (frame == null) {
                        frame = new Rect();
                    }
                    layoutDecoratedWithMargins(scrap, frame.left, frame.top - verticalScrollOffset, frame.right, frame.bottom - verticalScrollOffset);
                }
            } else {
                //将不再屏幕的ITEM放入缓存
                List<Item> views = row.views;
                for (int i = 0; i < views.size(); i++) {
                    View scrap = views.get(i).view;
                    removeAndRecycleView(scrap,recycler);
                }
            }
        }
    }

    /****
     *计算每一行没有居中的VIEWGROUP，让其居中
     */
    private void formatAboveRow() {
        List<Item> views = row.views;
        for (int i = 0; i < views.size(); i++) {
            Item item = views.get(i);
            View view = item.view;
            int position = getPosition(view);
            //如果该ITEM的位置不在改行中间位置的话，重新进行放置
            if (allItemFrames.get(position).top < row.cuTop + (row.maxHeight - views.get(i).useHeight) / 2) {
                Rect frame = allItemFrames.get(position);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(allItemFrames.get(position).left, (int) (row.cuTop + (row.maxHeight - views.get(i).useHeight) / 2),
                        allItemFrames.get(position).right, (int) (row.cuTop + (row.maxHeight - views.get(i).useHeight) / 2 + getDecoratedMeasuredHeight(view)));
                allItemFrames.put(position, frame);
                item.setRect(frame);
                views.set(i, item);
            }
        }
        row.views = views;
        lineRows.add(row);
        row = new Row();
    }

    private int getVerticalSpace() {
        return instance.getHeight() + instance.getPaddingTop() - instance.getPaddingBottom();
    }

    private int getHorizontalSpace() {
        return instance.getWidth() + instance.getPaddingLeft() - instance.getPaddingRight();
    }

    /****
     * 监听竖直方向的偏移量
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.e("manager","scroll vertically by dy :"+dy);
        /***
         * 实际要滑动的距离
         */
        int travel = dy;
        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }
        //将竖直方向的偏移量+travel
        verticalScrollOffset += travel;
        //平移容器内的item
        offsetChildrenVertical(-travel);
        fillLayout(recycler, state);
        return travel;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    public class Row {
        private float cuTop;///每一行的头部坐标
        private float maxHeight;  //每一行需要占据的最大高度
        private List<Item> views = new ArrayList<>();   //每一行存储的Item

        public void setCuTop(float cuTop) {
            this.cuTop = cuTop;
        }

        public void setMaxHeight(float maxHeight) {
            this.maxHeight = maxHeight;
        }

        public void addViews(Item view) {
            views.add(view);
        }
    }

    public class Item {
        private int useHeight;
        private View view;
        private Rect rect;

        public void setRect(Rect rect) {
            this.rect = rect;
        }

        public Item(int useHeight, View view, Rect rect) {
            this.useHeight = useHeight;
            this.view = view;
            this.rect = rect;
        }
    }
}
