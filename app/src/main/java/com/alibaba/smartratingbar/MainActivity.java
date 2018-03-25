package com.alibaba.smartratingbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

import com.alibaba.library.SmartRatingBar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SmartRatingBar smartRatingBar = (SmartRatingBar) findViewById(R.id.smart_rating_bar);
        smartRatingBar.setOnRatingBarChangeListener(new SmartRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SmartRatingBar ratingBar, float rating) {
                toast(rating);
            }
        });
        ValueAnimator valueAnimator = ValueAnimator
                .ofFloat(0.0f, 5.0f, 0f, 5.0f)
                .setDuration(1600L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                smartRatingBar.setRatingNum((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    void toast(float rating) {
//        Toast.makeText(this, "Rating: " + rating, Toast.LENGTH_SHORT).show();
    }
}
