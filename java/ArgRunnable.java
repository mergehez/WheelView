package com.arges.sepan.wheelview;

import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.arges.sepan.argPolygonView.ArgPolygonView;
import java.text.MessageFormat;

abstract class ArgRunnable implements Runnable {
    private Handler h = new Handler();
    private String name;
    private ArgPolygonView polygonView;
    private ImageView imgCerx,imgButton;
    private TextView tvRotate, tvSpeed;
    private long msGuherin;

    public ArgRunnable(String name, ArgPolygonView polygonView, ImageView imgCerx, ImageView imgButton, TextView tvRotate, TextView tvSpeed, float speedChange, long msGuherin) {
        this.polygonView = polygonView;
        this.name = name;
        this.imgCerx = imgCerx;
        this.imgButton = imgButton;
        this.tvRotate = tvRotate;
        this.tvSpeed = tvSpeed;
        this.msGuherin = msGuherin;
    }
    abstract boolean canContinue();
    abstract float changeAndGetSpeed();
    abstract float changeAndGetRotate();
    abstract int getColor();
    abstract void onFinish(Handler h);

    @Override
    public void run() {
        float speed = changeAndGetSpeed();
        float rotate = changeAndGetRotate();
        //tvRotate.setText(MessageFormat.format("{0}", speed)); tvSpeed.setText(MessageFormat.format("{0}", speed));
        //Log.d("WheelViewLog",String.format("%s-Speed: %.2f, Rotate: %.2f",name,speed,rotate));
        polygonView.setRotate((int)rotate);
        polygonView.applyChanges();
        imgCerx.setRotation(-rotate);
        if(canContinue()){
            h.postDelayed(this,msGuherin);
        }else {
            //Log.d("WheelViewLog", String.format("finish %s rotate: %.2f",name,rotate));
            //imgButton.setBackgroundColor(getColor());
            onFinish(h);
        }
    }
}
