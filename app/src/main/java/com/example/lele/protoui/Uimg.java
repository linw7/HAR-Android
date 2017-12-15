package com.example.lele.protoui;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by LeLe on 2017/8/26.
 */
public class Uimg {
    /**
     * 功能：通过view获取bitmap
     * 参考资料：http://stackoverflow.com/questions/2339429/android-view-getdrawingcache-returns-null-only-null?noredirect=1&lq=1
     *
     * @param v
     * @return 以view形式存在的bitmap
     */
    public static Bitmap getBitmap(View v) {
        // 获取bgBitmap
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap bgBitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return bgBitmap;
    }
}
