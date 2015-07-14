package com.codepath.advancedcustomviewslecture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AspectRatioImageView extends ImageView {

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // determine the aspect ratio of the image content
        int width = 0;
        int height = 0;
        float aspectRatio = 1f;

        Drawable d = getDrawable();
        if (d != null) {
            width = d.getIntrinsicWidth();
            aspectRatio = (float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight();
        }

        // set the width based on the measure specs
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                // we can be as big as we want to be, so let's
                // stay the size we are (original size of the bitmap)
                break;
            case MeasureSpec.AT_MOST:
                // we can be as big as we want to be, up to the given spec size
                width = Math.min(width, widthSpecSize);
                break;
            case MeasureSpec.EXACTLY:
                // we must be the spec size
                width = widthSpecSize;
                break;
        }

        // calculate the desired height based on the original aspect ratio
        height = (int) (width / aspectRatio);

        // make sure the height we want is not too large
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.EXACTLY) {
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            // if our measurement exceeds the max height, set height to the max
            // height, and reset the width again based on original aspect ratio
            if (height > heightSpecSize) {
                height = heightSpecSize;
                width = (int) (height * aspectRatio);
            }
        }

        setMeasuredDimension(width, height);
    }
}
