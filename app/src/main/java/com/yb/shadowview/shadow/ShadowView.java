package com.yb.shadowview.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yb.shadowview.R;


/**
 * desc:<br>
 * author : yuanbin<br>
 * tel : 17610999926<br>
 * email : yuanbin@koalareading.com<br>
 * date : 2019/1/8 18:56
 */
public class ShadowView extends FrameLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = {android.R.attr.colorBackground};

    private int shadowColor;
    private int shadowBackground;
    private int shadowLeftWidth;
    private int shadowRightWidth;
    private int shadowTopWidth;
    private int shadowBottomWidth;
    private int shadowRadius;
    private Paint shadowPaint;
    private int shadowWidth;
    private Bitmap shadowBitmap;
    private boolean needRedraw;

    public ShadowView(@NonNull Context context) {
        this(context, null);
    }

    public ShadowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        shadowColor = Color.TRANSPARENT;
        shadowWidth = 0;
        shadowPaint = new Paint();
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(Color.TRANSPARENT);
        shadowPaint.setAntiAlias(true);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShadowView);
            shadowColor = a.getColor(R.styleable.ShadowView_shadowColor, Color.TRANSPARENT);
            if (a.hasValue(R.styleable.ShadowView_shadowBackgroundColor)) {
                shadowBackground = a.getColor(R.styleable.ShadowView_shadowBackgroundColor, Color.TRANSPARENT);
            } else {
                final TypedArray aa = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
                shadowBackground = aa.getColor(0, 0);
                aa.recycle();
            }
            if (shadowBackground == Color.TRANSPARENT) {
                shadowBackground = Color.WHITE;
            }
            shadowWidth = (int) a.getDimension(R.styleable.ShadowView_shadowWidth, 0);
            shadowLeftWidth = (int) a.getDimension(R.styleable.ShadowView_shadowLeftWidth, shadowWidth);
            shadowRightWidth = (int) a.getDimension(R.styleable.ShadowView_shadowRightWidth, shadowWidth);
            shadowTopWidth = (int) a.getDimension(R.styleable.ShadowView_shadowTopWidth, shadowWidth);
            shadowBottomWidth = (int) a.getDimension(R.styleable.ShadowView_shadowBottomWidth, shadowWidth);
            shadowWidth = Math.max(shadowWidth, shadowLeftWidth);
            shadowWidth = Math.max(shadowWidth, shadowRightWidth);
            shadowWidth = Math.max(shadowWidth, shadowTopWidth);
            shadowWidth = Math.max(shadowWidth, shadowBottomWidth);
            shadowRadius = (int) a.getDimension(R.styleable.ShadowView_shadowRadius, 0);
            a.recycle();
        }
        shadowPaint.setColor(shadowBackground);
        shadowPaint.setShadowLayer(shadowWidth, 0, 0, shadowColor);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int originWidth = getMeasuredWidth();
        int originHeight = getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() != originWidth || originHeight != getMeasuredHeight()) {
            needRedraw = true;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (needRedraw) {
            if ((shadowColor != Color.TRANSPARENT && shadowWidth > 0) || shadowBackground != Color.TRANSPARENT) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
                shadowBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas shadowCanvas = new Canvas(shadowBitmap);
                RectF rectF = new RectF(shadowLeftWidth, shadowTopWidth, getWidth() - shadowRightWidth, getHeight() - shadowBottomWidth);
                shadowCanvas.drawRoundRect(rectF, shadowRadius, shadowRadius, shadowPaint);
            }
            needRedraw = false;
        }
        if (shadowBitmap != null) {
            canvas.drawBitmap(shadowBitmap, 0, 0, shadowPaint);
        }
        super.dispatchDraw(canvas);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left + shadowLeftWidth, top + shadowTopWidth, right + shadowRightWidth, bottom + shadowBottomWidth);
    }

    public void setShadowColor(String color) {
        try {
            setShadowColor(Color.parseColor(color));
        } catch (Throwable t) {
            Toast.makeText(getContext(), "色值错误", Toast.LENGTH_SHORT).show();
        }
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        shadowPaint.setShadowLayer(shadowWidth, 0, 0, shadowColor);
        redrawShadow();
    }

    public void setShadowBackground(String color) {
        try {
            setShadowBackground(Color.parseColor(color));
        } catch (Throwable t) {
            Toast.makeText(getContext(), "色值错误", Toast.LENGTH_SHORT).show();
        }
    }

    public void setShadowBackground(int shadowBackground) {
        this.shadowBackground = shadowBackground;
        shadowPaint.setColor(shadowBackground);
        redrawShadow();
    }

    public void setShadowWidthDp(int shadowWidth) {
        setShadowWidthPx(dp2px(shadowWidth));
    }

    public void setShadowWidthPx(int shadowWidth) {
        this.shadowWidth = shadowWidth;
        setShadowWidthPx(shadowWidth, shadowWidth, shadowWidth, shadowWidth);
    }

    public void setShadowWidthDp(int shadowLeftWidth, int shadowRightWidth, int shadowTopWidth, int shadowBottomWidth) {
        setShadowWidthPx(dp2px(shadowLeftWidth), dp2px(shadowRightWidth), dp2px(shadowTopWidth), dp2px(shadowBottomWidth));
    }


    public void setShadowWidthPx(int shadowLeftWidth, int shadowRightWidth, int shadowTopWidth, int shadowBottomWidth) {
        super.setPadding(getPaddingLeft() - this.shadowLeftWidth + shadowLeftWidth, getPaddingTop() - this.shadowTopWidth + shadowTopWidth,
                getPaddingRight() - this.shadowRightWidth + shadowRightWidth, getPaddingBottom() - this.shadowBottomWidth + shadowBottomWidth);
        this.shadowLeftWidth = shadowLeftWidth;
        this.shadowRightWidth = shadowRightWidth;
        this.shadowTopWidth = shadowTopWidth;
        this.shadowBottomWidth = shadowBottomWidth;
        shadowWidth = Math.max(shadowWidth, shadowLeftWidth);
        shadowWidth = Math.max(shadowWidth, shadowRightWidth);
        shadowWidth = Math.max(shadowWidth, shadowTopWidth);
        shadowWidth = Math.max(shadowWidth, shadowBottomWidth);
        shadowPaint.setShadowLayer(shadowWidth, 0, 0, shadowColor);
        redrawShadow();
    }

    public void setShadowRadiusDp(int shadowRadiusDp) {
        setShadowRadiusPx(dp2px(shadowRadiusDp));
    }

    public void setShadowRadiusPx(int shadowRadiusPx) {
        this.shadowRadius = shadowRadiusPx;
        redrawShadow();
    }

    private void redrawShadow() {
        post(new Runnable() {
            @Override
            public void run() {
                needRedraw = true;
                invalidate();
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    private int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        //hsv[1] = hsv[1] + 0.1f;
        hsv[2] = hsv[2] - 0.1f;
        int darkerColor = Color.HSVToColor(hsv);
        Log.e("test", Color.alpha(color) + " " + Color.red(color) + " " + Color.green(color) + " " + Color.blue(color));
        Log.e("test", Color.alpha(darkerColor) + " " + Color.red(darkerColor) + " " + Color.green(darkerColor) + " " + Color.blue(darkerColor));
        return color;
    }
}
