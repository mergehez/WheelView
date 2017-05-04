package com.arges.sepan.wheelview;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arges.sepan.argPolygonView.ArgPolygonView;

import java.text.MessageFormat;
import java.util.Random;


public class WheelView extends SquareRelLay {
    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    ImageView imgCerx, imgButton;
    ArgPolygonView polygonView;
    TextView tvRotate;
    TextView tvSpeed;
    Handler h;
    Runnable r1, r2, r3;
    float rotate = 360;
    //float rotateStart = 330;
    float rotateMiddle;
    float speed = 0.0f;
    float maxSpeed = 30.0f;
    float speedChange = 0.5f;
    long msGuherin = 20;
    float gav2RemainMs = (1000/msGuherin) * 5;
    void init(final Context context){
        inflate(context,R.layout.wheel_layout,this);
        h = new Handler();
    }
    int btnH=-1, btnW=-1;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(isInEditMode()) return;
        polygonView = (ArgPolygonView) findViewById(R.id.wheel_view);
        imgCerx = ( ImageView) findViewById(R.id.imgWheelCerx);
        imgButton = ( ImageView) findViewById(R.id.imgWheelBtn);
        tvRotate = (TextView) findViewById(R.id.tvRotate);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        if(btnH==-1){
            btnH = imgButton.getMeasuredHeight();
            btnW = imgButton.getMeasuredWidth();
        }
        final LayoutParams p = (RelativeLayout.LayoutParams) imgButton.getLayoutParams();
        imgButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        p.height = (int) (btnH*1.2f);
                        p.width = (int) (btnW*1.2f);
                        imgButton.setLayoutParams(p);
                        break;
                    case MotionEvent.ACTION_MOVE: break;
                    case MotionEvent.ACTION_UP:
                        p.height = btnH;
                        p.width = btnW;
                        imgButton.setLayoutParams(p);
                        break;
                }
                return false;
            }
        });
        imgButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //rotateStart = (rotateStart+60)%360;
                rotate = 360;
                //Log.d("WheelLog","ROTATE START: "+rotate);
                rotateMiddle = getRndMiddleAngle();
                speed = 0;
                gav2RemainMs = (1000/msGuherin) * 5;
                h.postDelayed(r1,msGuherin);
                //cdtStopToFast.start();
            }
        });
        if(!rInited) initRunnables();
    }
    boolean rInited = false;
    void initRunnables(){
        rotateMiddle = getRndMiddleAngle();
        Log.d("WheelLog","ROTATE MIDDLE: "+rotateMiddle);
        r1 =  new ArgRunnable("Gav1",polygonView,imgCerx,imgButton,tvRotate,tvSpeed,speedChange,msGuherin) {
            @Override boolean canContinue() { return speed<maxSpeed; }
            @Override float changeAndGetSpeed() { return (speed += speedChange); }
            @Override float changeAndGetRotate() { return rotate = (rotate - speed + 360) % 360; }
            @Override int getColor() { return Color.BLACK; }
            @Override void onFinish(Handler h) { h.postDelayed(r2,msGuherin); }
        };
        r2 =  new ArgRunnable("Gav2",polygonView,imgCerx,imgButton,tvRotate,tvSpeed,speedChange,msGuherin) {
            @Override boolean canContinue() { return (gav2RemainMs -= msGuherin)>0 || rotate!=rotateMiddle; }
            @Override float changeAndGetSpeed() { return speed; }
            @Override float changeAndGetRotate() { return rotate = (rotate - speed + 360) % 360; }
            @Override int getColor() { return Color.RED; }
            @Override void onFinish(Handler h) { h.postDelayed(r3,msGuherin); gav2RemainMs = (1000/msGuherin) * 5; }
        };
        r3 =  new ArgRunnable("Gav3",polygonView,imgCerx,imgButton,tvRotate,tvSpeed,speedChange,msGuherin) {
            @Override boolean canContinue() { return speed>0.0f; }
            @Override float changeAndGetSpeed() { return (speed -= speedChange); }
            @Override float changeAndGetRotate() { return rotate = (rotate - speed + 360) % 360; }
            @Override int getColor() { return Color.GREEN; }
            @Override void onFinish(Handler h) { rotate = 360; }
        };
        rInited = true;
    }
    float getRndMiddleAngle(){
        int[] angles = {0, 60, 120, 180, 240, 300};
        //int[] relativeAngles = {30,90,150,210,270,330};
        int rndIndex = new Random().nextInt(angles.length);
        return (angles[rndIndex] + 165) % 360;
    }
}
