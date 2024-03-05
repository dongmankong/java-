package com.zjy.game;

import com.zjy.util.MyUtil;

import java.awt.*;

public class Explode {
    public static final int EXPLODE_FRAME_COUNT=12;

    private static Image[] img;
    private static int explodeWidth;
    private static int explodeHeight;

    static {
        img=new Image[EXPLODE_FRAME_COUNT/3];
        for (int i = 0; i < img.length; i++) {
            img[i]= MyUtil.createImage("res/boom_"+i+".png");
        }

    }



    //爆炸效果的属性
    private int x,y;
    //当前播放的帧的下标
    private int index;

    private boolean visible=true;

    public Explode(){
        index=0;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index=0;
    }

    public void draw(Graphics g){
        if(explodeHeight<=0){
            explodeWidth=img[0].getWidth(null)>>1;
            explodeHeight=img[0].getHeight(null);
        }
        if(!visible) return;
        g.drawImage(img[index/3],x-explodeWidth,y-explodeHeight,null);
        index++;
        //播放完最后一帧，设置为不可见
        if(index>=EXPLODE_FRAME_COUNT){
            visible=false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
