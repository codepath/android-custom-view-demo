package com.codepath.advancedcustomviewslecture;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyLinearLayout extends ViewGroup {

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int width = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            // trigger a measure on the child
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

            // create the width measure spec for this child
            int widthUsedThusFar = 0;
            widthUsedThusFar += getPaddingLeft();
            widthUsedThusFar += getPaddingRight();
            widthUsedThusFar += layoutParams.leftMargin;
            widthUsedThusFar += layoutParams.rightMargin;
            int childWidthMeasureSpec =
                    createMeasureSpec(widthMeasureSpec, widthUsedThusFar, layoutParams.width);

            // create the height measure spec for this child
            int heightUsedThusFar = height;
            heightUsedThusFar += getPaddingTop();
            heightUsedThusFar += getPaddingBottom();
            heightUsedThusFar += layoutParams.topMargin;
            heightUsedThusFar += layoutParams.bottomMargin;
            int childHeightMeasureSpec =
                    createMeasureSpec(heightMeasureSpec, heightUsedThusFar, layoutParams.height);

            // tell this child to measure itself
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            // reset our width if this child is wider than all others thus far
            width = Math.max(width, child.getMeasuredWidth());

            // account for margins of this child, and add child's height to our height
            MarginLayoutParams childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            height += childLayoutParams.topMargin + childLayoutParams.bottomMargin;
            height += child.getMeasuredHeight();
        }

        // make sure our width adheres to the constraints defined by our parent
        width = resolveSizeAndState(width, widthMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    private int createMeasureSpec(int parentMeasureSpec, int sizeUsedThusFar,
            int childDesiredDimension) {
        if (childDesiredDimension != LayoutParams.MATCH_PARENT &&
                childDesiredDimension != LayoutParams.WRAP_CONTENT) {
            // child wants a specific size... so be it
            return MeasureSpec.makeMeasureSpec(childDesiredDimension, MeasureSpec.EXACTLY);
        }

        int totalParentSize = MeasureSpec.getSize(parentMeasureSpec);
        int availableSize = Math.max(0, totalParentSize - sizeUsedThusFar);

        int resultSize = 0;
        int resultMode = 0;

        switch (MeasureSpec.getMode(parentMeasureSpec)) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                resultSize = availableSize;
                resultMode = childDesiredDimension == LayoutParams.MATCH_PARENT ?
                        MeasureSpec.EXACTLY : MeasureSpec.AT_MOST;
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                resultSize = availableSize;
                resultMode = MeasureSpec.AT_MOST;
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                resultSize = 0;
                resultMode = MeasureSpec.UNSPECIFIED;
                break;
        }

        //noinspection ResourceType
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = getPaddingTop();
        int childLeft;
        int childRight;
        int childBottom;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            final int childHeight = child.getMeasuredHeight();
            final int childWidth = child.getMeasuredWidth();

            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

            childLeft = getPaddingLeft() + layoutParams.leftMargin;

            childTop += layoutParams.topMargin;

            childRight = childLeft + childWidth;
            childBottom = childTop + childHeight;

            // set the child's frame
            child.layout(childLeft, childTop, childRight, childBottom);

            childTop += childHeight;
            childTop += layoutParams.bottomMargin;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
