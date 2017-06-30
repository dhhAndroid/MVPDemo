package com.dhh.mvp.main.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dhh.mvp.R;

/**
 * Created by dhh on 2017/6/12.
 */

public class BitmapView extends View {

    private Bitmap mBitmap;
    private Bitmap canvasBitmap;

    public BitmapView(Context context) {
        super(context);
        init();
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.BLUE);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.del_all);
        canvasBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight() * 5, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(canvasBitmap);
        for (int i = 0; i < 5; i++) {
            canvas.drawBitmap(i == 2 ? bitmap : mBitmap, 0, i * mBitmap.getHeight(), null);
        }
        mBitmap.recycle();
        bitmap.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, null);
    }
}
