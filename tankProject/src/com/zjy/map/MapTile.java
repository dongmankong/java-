package com.zjy.map;

import com.zjy.game.Bullet;
import com.zjy.util.BulletsPool;
import com.zjy.util.Constant;
import com.zjy.util.MyUtil;

import java.awt.*;
import java.util.List;

public class MapTile {
    public static final int TYPE_NORMAL=0;
    public static final int TYPE_HOUSE=1;
    public static final int TYPE_COVER=2;
    public static final int TYPE_HARD=3;


    public static int titleW=40;
    public static int radius=titleW>>1;
    private int type=TYPE_NORMAL;


    private static Image[] tileImg;
    static {
        tileImg=new Image[4];
        tileImg[TYPE_NORMAL]= MyUtil.createImage("res/tile.png");
        tileImg[TYPE_HOUSE]= MyUtil.createImage("res/house.png");
        tileImg[TYPE_COVER]= MyUtil.createImage("res/cover.png");
        tileImg[TYPE_HARD]= MyUtil.createImage("res/hard.png");

        if(titleW<=0){
            titleW=tileImg[TYPE_NORMAL].getWidth(null);
        }
    }
    //图片资源的左上角
    private int x,y;
    private boolean visible=true;

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
        if(titleW<=0){
            titleW=tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    public MapTile(){

    }

    public void draw(Graphics g){
        if(!visible){
            return ;
        }
        if(titleW<=0){
            titleW=tileImg[TYPE_NORMAL].getWidth(null);
        }
        g.drawImage(tileImg[type],x,y,null);


    }

    //地图跟多个子弹的碰撞
    public boolean isCollideBullet(List<Bullet> bullets){
        if(!visible || type==TYPE_COVER){
            return false;
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean collide = MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if(collide){
                bullet.setVisible(false);
                BulletsPool.theReturn(bullet);

                return true;
            }
        }
        return false;
    }


    //判断当前地图块是否是老巢
    public boolean isHouse(){
        return type==TYPE_HOUSE;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
