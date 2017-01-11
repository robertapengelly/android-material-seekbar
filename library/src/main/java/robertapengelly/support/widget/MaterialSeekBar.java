package robertapengelly.support.widget;

import  android.annotation.TargetApi;
import  android.content.Context;
import  android.content.res.TypedArray;
import  android.graphics.Canvas;
import  android.graphics.Paint;
import  android.os.Build;
import  android.util.AttributeSet;
import  android.util.TypedValue;
import  android.view.MotionEvent;
import  android.view.View;
import  android.view.ViewParent;
import  android.view.animation.DecelerateInterpolator;

import  robertapengelly.support.animation.ValueAnimator;
import  robertapengelly.support.materialseekbar.R;
import  robertapengelly.support.materialseekbar.util.ThemeUtils;

public class MaterialSeekBar extends View {

    private static float STROKE_WIDTH, THUMB_RADIUS, THUMB_RADIUS_DRAGGED;
    
    private int colorAccent, colorControlNormal;
    
    private DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private ValueAnimator radiusAnimator;
    private Style style;
    
    float min, max, thumbRadius, step, value;
    boolean tick;
    int tickColor, tickStep;
    
    OnValueChangedListener onValueChangedListener;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    
    public MaterialSeekBar(Context context) {
        super(context);
        
        init(context, null, 0, 0);
    
    }
    
    public MaterialSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init(context, attrs, 0, 0);
    
    }
    
    public MaterialSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init(context, attrs, defStyleAttr, 0);
    
    }
    
    @TargetApi(21)
    public MaterialSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        
        init(context, attrs, defStyleAttr, defStyleRes);
    
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        float v = ((value - min) / (max - min));
        
        int thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
        int thumbY = (getHeight() / 2);
        
        paint.setColor(colorAccent);
        paint.setStrokeWidth(STROKE_WIDTH);
        
        if (getPaddingLeft() < (thumbX - thumbRadius))
            canvas.drawLine(getPaddingLeft(), thumbY, (thumbX - thumbRadius), thumbY, paint);
        
        paint.setColor(colorControlNormal);
        
        if ((thumbX + thumbRadius) < (getWidth() - getPaddingLeft()))
            canvas.drawLine((thumbX + thumbRadius), thumbY, (getWidth() - getPaddingLeft()), thumbY, paint);
        
        if ((style == Style.Discrete) && tick) {
        
            paint.setColor(tickColor);
            
            float range = ((max - min) / step);
            
            for (int i = 0; i < range; i += tickStep)
                canvas.drawCircle((i / range * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft()),
                    (getHeight() / 2), (STROKE_WIDTH / 2), paint);
            
            canvas.drawCircle((getWidth() - getPaddingRight()), (getHeight() / 2), (STROKE_WIDTH / 2), paint);
        
        }
        
        paint.setColor(colorAccent);
        canvas.drawCircle(thumbX, thumbY, thumbRadius, paint);
    
    }
    
    public float getMax() {
        return max;
    }
    
    public float getMin() {
        return min;
    }
    
    public float getStepSize() {
        return step;
    }
    
    public Style getStyle() {
        return style;
    }
    
    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) Math.ceil(THUMB_RADIUS_DRAGGED * 2), super.getSuggestedMinimumHeight());
    }
    
    @Override
    protected int getSuggestedMinimumWidth() {
        return Math.max((int) Math.ceil(THUMB_RADIUS_DRAGGED * 2), super.getSuggestedMinimumWidth());
    }
    
    public int getTickColor() {
        return tickColor;
    }
    
    public int getTickStep() {
        return tickStep;
    }
    
    public float getValue() {
    
        if (style == Style.Discrete)
            return stepValue(value);
        
        return value;
    
    }
    
    public boolean hashTick() {
        return tick;
    }
    
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    
        colorAccent = ThemeUtils.getColorFromAttrRes(R.attr.colorAccent, context);
        
        if ((colorAccent == 0) && Build.VERSION.SDK_INT >= 21)
            colorAccent = ThemeUtils.getColorFromAttrRes(android.R.attr.colorAccent, context);
        
        colorControlNormal = ThemeUtils.getColorFromAttrRes(R.attr.colorControlNormal, context);
        
        if ((colorControlNormal == 0) && Build.VERSION.SDK_INT >= 21)
            colorControlNormal = ThemeUtils.getColorFromAttrRes(android.R.attr.colorControlNormal, context);
        
        STROKE_WIDTH = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
            getResources().getDisplayMetrics()) * 2);
        
        THUMB_RADIUS = thumbRadius = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
            getResources().getDisplayMetrics()) * 8);
        
        THUMB_RADIUS_DRAGGED = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
            getResources().getDisplayMetrics()) * 10);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialSeekBar, defStyleAttr, defStyleRes);
        
        setMax(a.getFloat(R.styleable.MaterialSeekBar_msb_max, 100));
        setMin(a.getFloat(R.styleable.MaterialSeekBar_msb_min, 0));
        setPadding((int) THUMB_RADIUS_DRAGGED, 0, (int) THUMB_RADIUS_DRAGGED, 0);
        setStepSize(a.getFloat(R.styleable.MaterialSeekBar_msb_stepSize, 1));
        setStyle(Style.values()[a.getInt(R.styleable.MaterialSeekBar_msb_barStyle, 0)]);
        setTick(a.getBoolean(R.styleable.MaterialSeekBar_msb_tick, true));
        setTickColor(a.getColor(R.styleable.MaterialSeekBar_msb_tickColor, colorControlNormal));
        setTickStep(a.getInt(R.styleable.MaterialSeekBar_msb_tickStep, 1));
        setValue(a.getFloat(R.styleable.MaterialSeekBar_msb_value, 0));
        
        a.recycle();
    
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    
        int desiredHeight = getSuggestedMinimumHeight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int height;
        
        if (heightMode == MeasureSpec.EXACTLY)
            height = heightSize;
        else if (heightMode == MeasureSpec.AT_MOST)
            height = Math.min(desiredHeight, heightSize);
        else
            height = desiredHeight;
        
        int desiredWidth = getSuggestedMinimumWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        
        int width;
        
        if (widthMode == MeasureSpec.EXACTLY)
            width = widthSize;
        else if (widthMode == MeasureSpec.AT_MOST)
            width = Math.min(desiredWidth, widthSize);
        else
            width = desiredWidth;
        
        setMeasuredDimension(width, height);
    
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    
        ViewParent parent = getParent();
        
        switch (event.getAction()) {
        
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (radiusAnimator != null)
                    radiusAnimator.end();
                
                radiusAnimator = ValueAnimator.ofFloat(thumbRadius, THUMB_RADIUS);
                radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                    
                        thumbRadius = (float) animation.getAnimatedValue();
                        postInvalidate();
                    
                    }
                
                });
                radiusAnimator.setDuration(200);
                radiusAnimator.setInterpolator(interpolator);
                radiusAnimator.start();
                
                if (parent != null)
                    parent.requestDisallowInterceptTouchEvent(false);
                
                break;
            case MotionEvent.ACTION_DOWN:
                if (radiusAnimator != null)
                    radiusAnimator.end();
                
                radiusAnimator = ValueAnimator.ofFloat(thumbRadius, THUMB_RADIUS_DRAGGED);
                radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                    
                        thumbRadius = (float) animation.getAnimatedValue();
                        postInvalidate();
                    
                    }
                
                });
                radiusAnimator.setDuration(200);
                radiusAnimator.setInterpolator(interpolator);
                radiusAnimator.start();
                
                if (parent != null)
                    parent.requestDisallowInterceptTouchEvent(true);
                
                break;
            default:
                break;
        
        }
        
        float v = (event.getX() - getPaddingLeft()) / (getWidth() - getPaddingLeft() - getPaddingRight());
        v = Math.max(0, Math.min(v, 1));
        
        float newValue = (v * (max - min) + min);
        
        postInvalidate();
        
        if ((newValue != value) && (onValueChangedListener != null)) {
        
            if (style == Style.Discrete) {
            
                int sv = stepValue(newValue);
                
                if (stepValue(value) != sv)
                    onValueChangedListener.onValueChanged(this, sv);
            
            } else
                onValueChangedListener.onValueChanged(this, newValue);
        
        }
        
        value = newValue;
        
        super.onTouchEvent(event);
        return true;
    
    }
    
    public void setMax(float max) {
    
        if (max > min)
            this.max = max;
        else
            this.max = (min + step);
        
        this.value = Math.max(min, Math.min(value, max));
    
    }
    
    public void setMin(float min) {
    
        if (min < max)
            this.min = min;
        else
            this.min = (max - step);
        
        this.value = Math.max(min, Math.min(value, max));
    
    }
    
    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }
    
    public void setStepSize(float step) {
    
        if (step > 0)
            this.step = step;
        else
            this.step = 1;
    
    }
    
    public void setStyle(Style style) {
        this.style = style;
    }
    
    public void setTick(boolean tick) {
        this.tick = tick;
    }
    
    public void setTickColor(int tickColor) {
        this.tickColor = tickColor;
    }
    
    public void setTickStep(int tickStep) {
        this.tickStep = tickStep;
    }
    
    public void setValue(float value) {
    
        if (style == Style.Discrete)
            this.value = stepValue(Math.max(min, Math.min(value, max)));
        else
            this.value = Math.max(min, Math.min(value, max));
    
    }
    
    private int stepValue(float v) {
        return (int) (Math.floor((v - min + step / 2) / step) * step + min);
    }
    
    public enum Style {
        Continuous, Discrete
    }
    
    public interface OnValueChangedListener {
        void onValueChanged(MaterialSeekBar seekbar, float value);
    }

}