package com.yb.shadowview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yb.shadowview.shadow.ShadowView;

/**
 * desc:<br>
 * author : yuanbin<br>
 * tel : 17610999926<br>
 * email : yuanbin@koalareading.com<br>
 * date : 2019/1/9 14:44
 */
public class MainActivity extends AppCompatActivity {
    ShadowView shadowView;
    TextView mTvShowStroke;
    View shadowInnerView;
    EditText mEtShadowColor;
    TextView mTvShadowWidth;
    SeekBar mSeekBarShadowWidth;
    TextView mTvShadowRadius;
    SeekBar mSeekBarShadowRadius;
    private boolean isShowStroke;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        isShowStroke = false;
        shadowView = findViewById(R.id.id_layout_activity_shadow_view);
        shadowInnerView = findViewById(R.id.id_layout_activity_shadow_inner_view);
        mEtShadowColor = findViewById(R.id.id_layout_activity_shadow_color);
        mTvShadowWidth = findViewById(R.id.id_layout_activity_shadow_width_tv);
        mSeekBarShadowWidth = findViewById(R.id.id_layout_activity_shadow_width);
        mTvShadowRadius = findViewById(R.id.id_layout_activity_shadow_radius_tv);
        mSeekBarShadowRadius = findViewById(R.id.id_layout_activity_shadow_radius);
        mTvShowStroke = findViewById(R.id.id_layout_activity_show_stroke);
        mSeekBarShadowWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int width = (int) (shadowView.getWidth() * progress / 200f);
                    mTvShadowWidth.setText("shadowWidth:" + width + "px");
                    shadowView.setShadowWidthPx(width);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarShadowRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int radius = (int) (shadowView.getWidth() * progress / 200f);
                    mTvShadowRadius.setText("shadowRadius:" + radius + "px");
                    shadowView.setShadowRadiusPx(radius);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void changeShadowColor(View view) {
        shadowView.setShadowColor(mEtShadowColor.getText().toString());
    }

    public void showStroke(View view) {
        if (isShowStroke) {
            isShowStroke = false;
            shadowView.setBackgroundColor(Color.TRANSPARENT);
            shadowInnerView.setBackgroundColor(Color.TRANSPARENT);
            mTvShowStroke.setText("显示边框");
        } else {
            isShowStroke = true;
            shadowView.setBackgroundResource(R.drawable.bg_stroke);
            shadowInnerView.setBackgroundResource(R.drawable.bg_stroke);
            mTvShowStroke.setText("隐藏边框");
        }
    }
}
