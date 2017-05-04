package com.arges.sepan.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by arges on 4/28/2017.
 */

public class SquareRelLay extends RelativeLayout {
    public SquareRelLay(Context context) {
        super(context);
    }

    public SquareRelLay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelLay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,widthMeasureSpec);

        int w = getMeasuredWidth(); int h = getMeasuredHeight();
        int size = (w>h ? heightMeasureSpec : widthMeasureSpec);

        super.onMeasure(size,size);
        //this.setWillNotDraw(false);
    }
}
