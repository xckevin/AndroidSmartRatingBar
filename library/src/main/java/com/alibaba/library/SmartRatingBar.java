package com.alibaba.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kevin on 2017/11/2.
 */

public class SmartRatingBar extends View {

    private int mMaxStarNum = 5;

    private float mRatingNum = 3.3f;

    private float mRatingStepSize = 0.1f;

    private Drawable mRatingDrawable;
    private Drawable mRatingBackgroundDrawable;

    private int mGapSize;

    private int mOrientation = LinearLayout.HORIZONTAL;

    private boolean mIndicator = true;

    private OnRatingBarChangeListener mOnRatingBarChangeListener;

    public SmartRatingBar(Context context) {
        super(context);
        init(context, null);
    }

    public SmartRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setOnRatingBarChangeListener(OnRatingBarChangeListener onRatingBarChangeListener) {
        mOnRatingBarChangeListener = onRatingBarChangeListener;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartRatingBar);
            mRatingNum = typedArray.getFloat(R.styleable.SmartRatingBar_rating, 2.5f);
            mGapSize = typedArray.getDimensionPixelSize(R.styleable.SmartRatingBar_gap, 0);
            mMaxStarNum = typedArray.getInt(R.styleable.SmartRatingBar_maxRating, 5);
            mIndicator = typedArray.getBoolean(R.styleable.SmartRatingBar_indicator, true);
            mOrientation = typedArray.getInt(R.styleable.SmartRatingBar_orientation, LinearLayout.HORIZONTAL);
            mRatingDrawable = typedArray.getDrawable(R.styleable.SmartRatingBar_ratingDrawable);
            mRatingBackgroundDrawable = typedArray.getDrawable(R.styleable.SmartRatingBar_backgroundDrawable);
            typedArray.recycle();
        }
        if (mRatingDrawable == null && mRatingBackgroundDrawable == null) {
            mRatingDrawable = context.getResources().getDrawable(R.drawable.smart_ratingbar_rating);
            mRatingBackgroundDrawable = context.getResources().getDrawable(R.drawable.smart_ratingbar_background);
        }
    }

    public int getMaxStarNum() {
        return mMaxStarNum;
    }

    public void setMaxStarNum(int maxStarNum) {
        mMaxStarNum = maxStarNum;
        postInvalidate();
    }

    public float getRatingNum() {
        return mRatingNum;
    }

    public void setRatingNum(float ratingNum) {
        mRatingNum = ratingNum;
        postInvalidate();
        if (mOnRatingBarChangeListener != null) {
            mOnRatingBarChangeListener.onRatingChanged(this, ratingNum);
        }
    }

    public Drawable getRatingDrawable() {
        return mRatingDrawable;
    }

    public void setRatingDrawable(Drawable ratingDrawable) {
        mRatingDrawable = ratingDrawable;
        postInvalidate();
    }

    public Drawable getRatingBackgroundDrawable() {
        return mRatingBackgroundDrawable;
    }

    public void setRatingBackgroundDrawable(Drawable ratingBackgroundDrawable) {
        mRatingBackgroundDrawable = ratingBackgroundDrawable;
    }

    public int getGapSize() {
        return mGapSize;
    }

    public void setGapSize(int gapSize) {
        mGapSize = gapSize;
        postInvalidate();
    }

    public boolean isIndicator() {
        return mIndicator;
    }

    public void setIndicator(boolean indicator) {
        mIndicator = indicator;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mIndicator;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOrientation == LinearLayout.HORIZONTAL) {
                    final int drawableWidth = mRatingBackgroundDrawable.getBounds().width();
                    float x = event.getX();
                    float coverWidth = x - getPaddingLeft();
                    int coverStarNum = (int) (coverWidth / (drawableWidth + mGapSize));
                    float remainCoverWidth = coverWidth % (drawableWidth + mGapSize);
                    float partStar = remainCoverWidth /drawableWidth;
                    partStar = ((int) (partStar / mRatingStepSize)) / 10.0f;
                    setRatingNum(coverStarNum + partStar);
                } else {
                    final int drawableHeight = mRatingBackgroundDrawable.getBounds().height();
                    float y = event.getY();
                    float coverHeight = y - getPaddingTop();
                    int coverStarNum =
                            (int) (coverHeight / (drawableHeight + mGapSize));
                    float remainCoverHeight = coverHeight % (drawableHeight + mGapSize);
                    float partStar = remainCoverHeight / drawableHeight;
                    partStar = ((int) (partStar / mRatingStepSize)) / 10.0f;
                    setRatingNum(coverStarNum + partStar);
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState ss = new SavedState(parcelable);
        ss.mRating = getRatingNum();
        ss.mStarNum = getMaxStarNum();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setRatingNum(ss.getRating());
        setMaxStarNum(ss.getStarNum());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth;
        int measuredHeight;
        if (mOrientation == LinearLayout.HORIZONTAL) {
            measuredHeight = measureHeight(heightMeasureSpec);
            if (mRatingDrawable.getIntrinsicHeight() > measuredHeight) {
                mRatingDrawable.setBounds(0, 0, measuredHeight, measuredHeight);
                mRatingBackgroundDrawable.setBounds(0, 0, measuredHeight, measuredHeight);
            } else {
                mRatingDrawable.setBounds(0, 0, mRatingDrawable.getIntrinsicWidth(), mRatingDrawable.getIntrinsicHeight());
                mRatingBackgroundDrawable.setBounds(0, 0, mRatingDrawable.getIntrinsicWidth(),
                        mRatingDrawable.getIntrinsicHeight());
            }
            measuredWidth = measureWidth(widthMeasureSpec);

        } else {
            measuredWidth = measureWidth(widthMeasureSpec);
            if (mRatingDrawable.getIntrinsicWidth() > measuredWidth) {
                mRatingDrawable.setBounds(0, 0, measuredWidth, measuredWidth);
                mRatingBackgroundDrawable.setBounds(0, 0, measuredWidth, measuredWidth);
            } else {
                mRatingDrawable.setBounds(0, 0, mRatingDrawable.getIntrinsicWidth(), mRatingDrawable.getIntrinsicHeight());
                mRatingBackgroundDrawable.setBounds(0, 0, mRatingDrawable.getIntrinsicWidth(),
                        mRatingDrawable.getIntrinsicHeight());
            }
            measuredHeight = measureHeight(heightMeasureSpec);

        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Drawable ratingDrawable = mRatingDrawable;
        final Drawable backgroundDrawable = mRatingBackgroundDrawable;
        final float ratingNum = mRatingNum;
        final Rect drawableBounds = ratingDrawable.getBounds();

        final int ratingNumInt = (int) Math.floor(ratingNum);
        canvas.translate(getPaddingLeft(), getPaddingTop());
        int i = 0;
        for (; i < ratingNumInt; i++) {
            ratingDrawable.draw(canvas);
            translateCanvas(canvas, drawableBounds);
        }

        float ratingPart = ratingNum - ratingNumInt;
        int partWidth, partHeight;
        if (mOrientation == LinearLayout.HORIZONTAL) {
            partWidth = (int) (drawableBounds.width() * ratingPart);
            partHeight = drawableBounds.height();
        } else {
            partWidth = drawableBounds.width();
            partHeight = (int) (drawableBounds.height() * ratingPart);
        }
        // draw rating part
        canvas.save();
        canvas.clipRect(0, 0, partWidth, partHeight);
        ratingDrawable.draw(canvas);
        canvas.restore();

        // draw background part
        canvas.save();
        if (mOrientation == LinearLayout.HORIZONTAL) {
            canvas.clipRect(partWidth, 0, drawableBounds.right, drawableBounds.bottom);
        } else {
            canvas.clipRect(0, partHeight, drawableBounds.right, drawableBounds.bottom);
        }
        backgroundDrawable.draw(canvas);
        canvas.restore();

        translateCanvas(canvas, drawableBounds);
        i++; // move to next
        for (; i < mMaxStarNum; i++) {
            backgroundDrawable.draw(canvas);
            translateCanvas(canvas, drawableBounds);
        }
    }

    private void translateCanvas(Canvas canvas, Rect rect) {
        if (mOrientation == LinearLayout.HORIZONTAL) {
            canvas.translate(mGapSize + rect.width(), 0);
        } else {
            canvas.translate(0, mGapSize + rect.height());
        }
    }

    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            return widthSize;
        }
        int drawableWidth = mRatingDrawable.getBounds().width();
        if (drawableWidth == 0) {
            drawableWidth = mRatingDrawable.getIntrinsicWidth();
        }
        int maxSize = getPaddingLeft() + getPaddingRight();
        if (mOrientation == LinearLayout.HORIZONTAL) {
            maxSize += (mMaxStarNum - 1) * mGapSize + mMaxStarNum * drawableWidth;
        } else {
            maxSize += drawableWidth;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            return Math.min(maxSize, widthSize);
        }
        return maxSize;
    }

    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            return heightSize;
        }
        int drawableHeight = mRatingDrawable.getBounds().height();
        if (drawableHeight == 0) {
            drawableHeight = mRatingDrawable.getIntrinsicHeight();
        }
        int maxHeight = getPaddingBottom() + getPaddingTop();
        if (mOrientation == LinearLayout.HORIZONTAL) {
            maxHeight += drawableHeight;
        } else {
            maxHeight += (mMaxStarNum - 1) * mGapSize + mMaxStarNum * drawableHeight;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            return Math.min(heightSize, maxHeight);
        }
        return maxHeight;
    }

    static class SavedState extends BaseSavedState {

        float mRating;

        int mStarNum;

        public float getRating() {
            return mRating;
        }

        public void setRating(float rating) {
            mRating = rating;
        }

        public int getStarNum() {
            return mStarNum;
        }

        public void setStarNum(int starNum) {
            mStarNum = starNum;
        }

        public SavedState(Parcel source) {
            super(source);
            mRating = source.readFloat();
            mStarNum = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(mRating);
            out.writeInt(mStarNum);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnRatingBarChangeListener {

        void onRatingChanged(SmartRatingBar ratingBar, float rating);

    }

}
