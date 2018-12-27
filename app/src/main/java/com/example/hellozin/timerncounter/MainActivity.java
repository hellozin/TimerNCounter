package com.example.hellozin.timerncounter;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, DoubleClickListener {
    TextView counterArea;
    TextView mElapse;
    int count;
    long mBaseTime;
    long mPauseTime;
    final static int IDLE = 0;
    final static int RUNNING = 1;
    final static int PAUSE = 2;
    int mState = IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterArea = findViewById(R.id.counter_area);
        counterArea.setText(String.valueOf(count));
        counterArea.setOnClickListener(new DoubleClick(this, 300));
        counterArea.setOnLongClickListener(this);

        mElapse = findViewById(R.id.stopwatch_area);
        mElapse.setText("00:00");
        mElapse.setOnClickListener(new DoubleClick(this, 300));
        mElapse.setOnLongClickListener(this);
    }

    final Handler mTimer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mElapse.setText(getElapse());
            mTimer.sendEmptyMessage(0);
        }
    };

    @Override
    protected void onDestroy() {
        mTimer.removeMessages(0);
        super.onDestroy();
    }

    @Override
    public void onSingleClick(View view) {
        switch(view.getId()) {
            case R.id.counter_area:
                count++;
                counterArea.setText(String.valueOf(count));
                break;
            case R.id.stopwatch_area:
                switch(mState) {
                    case IDLE:
                        mBaseTime = SystemClock.elapsedRealtime();
                        mTimer.sendEmptyMessage(0);
                        mState = RUNNING;
                        break;
                    case RUNNING:
                        mTimer.removeMessages(0);
                        mPauseTime = SystemClock.elapsedRealtime();
                        mState = PAUSE;
                        break;
                    case PAUSE:
                        long now = SystemClock.elapsedRealtime();
                        mBaseTime += (now - mPauseTime);
                        mTimer.sendEmptyMessage(0);
                        mState = RUNNING;
                        break;
                }
                break;
        }
    }

    @Override
    public void onDoubleClick(View view) {
        switch(view.getId()) {
            case R.id.counter_area:
                count--;
                counterArea.setText(String.valueOf(count));
                break;
            case R.id.stopwatch_area:
                mTimer.removeMessages(0);
                mElapse.setText("00:00");
                mState = IDLE;
                break;

        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch(view.getId()) {
            case R.id.counter_area:
                count = 0;
                counterArea.setText(String.valueOf(count));
                break;
            case R.id.stopwatch_area:
                mTimer.removeMessages(0);
                mElapse.setText("00:00");
                mState = IDLE;
                break;
        }
        return true;
    }

    String getElapse() {
        long now = SystemClock.elapsedRealtime();
        long el = now - mBaseTime;

        String sEl = String.format("%02d:%02d", el/1000/60, (el/1000)%60);
        return sEl;
    }
}
