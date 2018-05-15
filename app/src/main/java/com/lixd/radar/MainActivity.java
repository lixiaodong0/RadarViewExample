package com.lixd.radar;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.radar_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RadarView view = (RadarView) v;
                        ValueAnimator animator = ValueAnimator.ofInt(0,99);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                               int progress = (int) animation.getAnimatedValue();
                                view.setProgress(progress);
                            }
                        });
                        animator.setDuration(3000);
                        animator.setInterpolator(new LinearInterpolator());
                        animator.start();

                    }
                });
    }
}
