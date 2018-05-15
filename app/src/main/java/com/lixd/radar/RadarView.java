package com.lixd.radar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * 仿雷达View
 * <!--颜色百分比-开头字母-->
 * <!--100% —FF-->
 * <!--95% — F2-->
 * <!--90% — E6-->
 * <!--85% — D9-->
 * <!--80% — CC-->
 * <!--75% — BF-->
 * <!--70% — B3-->
 * <!--65% — A6-->
 * <!--60% — 99-->      头部
 * <!--55% — 8C-->
 * <!--50% — 80-->
 * <!--45% — 73-->
 * <!--40% — 66-->
 * <!--35% — 59-->
 * <!--30% — 4D-->      尾部
 * <!--25% — 40-->
 * <!--20% — 33-->
 * <!--15% — 26-->
 * <!--10% — 1A-->
 * <!--5% — 0D-->
 * <!--0% — 00-->
 */
public class RadarView extends View {
    private static final String DES_TEXT = "击败好友";
    //默认颜色值
    private static final int COLOR_BG = Color.parseColor("#171E38");                 //背景颜色
    private static final int COLOR_DASHED = Color.parseColor("#C97A2A");             //虚线线条颜色
    private static final int COLOR_BIG_CIRCLE = Color.parseColor("#CC8727");         //大圆线条颜色
    private static final int COLOR_SMALL_CIRCLE = Color.parseColor("#989596");       //小圆线条颜色
    private static final int COLOR_PROGRESS_CIRCLE = Color.parseColor("#99EA7A36");    //进度圆背景颜色
    private static final int COLOR_TEXT = Color.WHITE;                               //文本颜色

    //默认单位值
    private static final int ITEM_MARGIN = 40;          // 每个圆形之间的边距
    private static final int MAX_VALUE = 100;           //进度最大值是100;
    private static final int TEXT_MARGIN_RIGHT = 10;    //文本之间右边距
    private static final int TEXT_MARGIN_TOP = 10;       //文本之间上边距
    private static final int TEXT_SIZE = 40;            //文本大小
    private static final int NUMBER_TEXT_SIZE = 150;    //数字文本大小
    private static final int PERCENT_TEXT_SIZE = 50;    //百分比符号大小

    //宽度
    private int width;
    //高度
    private int height;
    //虚线画笔
    private Paint dashedPaint;
    //大圆画笔
    private Paint bigCirclePaint;
    //小圆画笔
    private Paint smallCirclePaint;
    //背景圆画笔
    private Paint progressCirclePaint;
    //文本画笔
    private Paint textPaint;
    //数字文本画笔
    private Paint numberTextPaint;
    //百分号文本画笔
    private Paint percentTextPaint;

    //最大值
    private int max = MAX_VALUE;
    //当前进度值
    private int progress;
    //进度百分比
    private float percent;

    private int bgColor = COLOR_BG;
    private int dashedColor = COLOR_DASHED;
    private int bigCircleColor = COLOR_BIG_CIRCLE;
    private int smallCircleColor = COLOR_SMALL_CIRCLE;
    private int progressCircleColor = COLOR_PROGRESS_CIRCLE;
    private int textColor = COLOR_TEXT;

    private int itemMargin = ITEM_MARGIN;
    private int textMarginRight = TEXT_MARGIN_RIGHT;
    private int textMarginTop = TEXT_MARGIN_TOP;
    private int textSize = TEXT_SIZE;
    private int numberTextSize = NUMBER_TEXT_SIZE;
    private int percentTextSize = PERCENT_TEXT_SIZE;

    private String desText = DES_TEXT;


    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        percent = (progress + 0f) / max;
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
    }

    private void init() {
        dashedPaint = new Paint();
        dashedPaint.setAntiAlias(true);
        dashedPaint.setColor(dashedColor);
        dashedPaint.setStrokeWidth(2);
        dashedPaint.setStyle(Paint.Style.STROKE);

        bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setColor(bigCircleColor);
        bigCirclePaint.setStrokeWidth(20);
        bigCirclePaint.setStyle(Paint.Style.STROKE);

        smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setColor(smallCircleColor);
        smallCirclePaint.setStrokeWidth(2);
        smallCirclePaint.setStyle(Paint.Style.STROKE);

        progressCirclePaint = new Paint();
        progressCirclePaint.setAntiAlias(true);
        //        progressCirclePaint.setColor(progressCircleColor);
        progressCirclePaint.setStyle(Paint.Style.FILL);


        numberTextPaint = new Paint();
        numberTextPaint.setAntiAlias(true);
        numberTextPaint.setColor(textColor);
        numberTextPaint.setTextSize(numberTextSize);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        percentTextPaint = new Paint();
        percentTextPaint.setAntiAlias(true);
        percentTextPaint.setColor(textColor);
        percentTextPaint.setTextSize(percentTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int min = Math.min(width, height);
        int spec = MeasureSpec.makeMeasureSpec(min, MeasureSpec.EXACTLY);
        //保证View是矩形
        setMeasuredDimension(spec, spec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(width / 2, height / 2);
        Path path = new Path();
        path.addCircle(0, 0, width / 2, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawColor(bgColor);
        drawDashedCircle(canvas);
        drawBigCircle(canvas);
        drawSmallCircle(canvas);
        drawBgProgressCircle(canvas);
        drawText(canvas);
    }

    /**
     * 画虚线的圆
     */
    private void drawDashedCircle(Canvas canvas) {
        int dashedSize = width / 2 - itemMargin;
        RectF rectF = new RectF(-dashedSize, -dashedSize, dashedSize, dashedSize);
        for (int i = 0; i < 360; i = i + 4) {
            canvas.drawArc(rectF, i, 2, false, dashedPaint);
        }
    }

    /**
     * 画大圆
     *
     * @param canvas
     */
    private void drawBigCircle(Canvas canvas) {
        int circleSize = width / 2 - (itemMargin * 2);
        RectF rectF = new RectF(-circleSize, -circleSize, circleSize, circleSize);
        canvas.drawArc(rectF, 0, 360, false, bigCirclePaint);
    }

    /**
     * 画两个小圆
     *
     * @param canvas
     */
    private void drawSmallCircle(Canvas canvas) {
        int circleSize1 = width / 2 - (itemMargin * 3);
        int circleSize2 = width / 2 - (itemMargin * 4);
        RectF rectF = new RectF(-circleSize1, -circleSize1, circleSize1, circleSize1);
        canvas.drawArc(rectF, 0, 360, false, smallCirclePaint);
        rectF = new RectF(-circleSize2, -circleSize2, circleSize2, circleSize2);
        canvas.drawArc(rectF, 0, 360, false, smallCirclePaint);
    }

    /**
     * 画背景进度的圆
     *
     * @param canvas
     */
    private void drawBgProgressCircle(Canvas canvas) {
        int circleSize = width / 2;
        RectF rectF = new RectF(-circleSize, -circleSize, circleSize, circleSize);
        //线性渐变颜色值
        Shader shader = new LinearGradient(-rectF.centerX(), rectF.top, rectF.right, rectF.top, progressCircleColor, Color.parseColor("#00EA7A36"),/*Color.parseColor("#ffffff"),*/ Shader.TileMode.CLAMP);
        progressCirclePaint.setShader(shader);
        canvas.drawArc(rectF, 270, -(360 * percent), true, progressCirclePaint);
    }

    /**
     * 绘制文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        String number = String.valueOf((int) (percent * 100));
        String percent = "%";
        String des = desText;

        Rect desRect = getTextRect(des, textSize);
        Rect numberRect = getTextRect(number, numberTextSize);
        Rect percentRect = getTextRect(percent, percentTextSize);

        int width = (numberRect.width() + textMarginRight + percentRect.width()) / 2;
        int height = (desRect.height() + textMarginTop + numberRect.height()) / 2;

        Rect drawTextRect = new Rect(-width, -height, width, height);

        canvas.drawText(des, drawTextRect.centerX(), drawTextRect.top, textPaint);
        canvas.drawText(number, drawTextRect.left, drawTextRect.bottom, numberTextPaint);
        canvas.drawText(percent, drawTextRect.right - percentRect.width() / 2, drawTextRect.bottom, percentTextPaint);
    }


    /**
     * 测量文本的矩形边距
     *
     * @param text     文本
     * @param textSize 文本大小
     * @return
     */
    private Rect getTextRect(String text, int textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }
}
