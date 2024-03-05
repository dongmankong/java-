package com.zjy.tank;

import com.zjy.game.GameFrame;
import com.zjy.game.LevelInfo;
import com.zjy.util.Constant;
import com.zjy.util.EnemyTanksPool;
import com.zjy.util.MyUtil;

import java.awt.*;

public class EnemyTank extends Tank {
    public static final int TYPE_GREEN=0;
    public static final int TYPE_YELLOW=1;
    private int type=TYPE_GREEN;

    private static Image[] greenImg;
    private static Image[] yellowImg;

    //记录五秒开始的时间
    private long aiTime;

    static {
        greenImg=new Image[4];
        greenImg[0]=MyUtil.createImage("res/ul.png");
        greenImg[1]=MyUtil.createImage("res/dl.png");
        greenImg[2]=MyUtil.createImage("res/ll.png");
        greenImg[3]=MyUtil.createImage("res/rl.png");

        yellowImg=new Image[4];
        yellowImg[0]=MyUtil.createImage("res/u.png");
        yellowImg[1]=MyUtil.createImage("res/d.png");
        yellowImg[2]=MyUtil.createImage("res/l.png");
        yellowImg[3]=MyUtil.createImage("res/r.png");
    }


    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        aiTime=System.currentTimeMillis();
        type=MyUtil.getRandomNumber(0,2);
    }
    public EnemyTank(){
        aiTime=System.currentTimeMillis();
        type=MyUtil.getRandomNumber(0,2);

    }

    @Override
    public void drawImgTank(Graphics g) {
        ai();
        g.drawImage(type == TYPE_GREEN ? greenImg[getDir()] : yellowImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
    }



    //创建敌人坦克
    public static Tank createEnemy(){
        int x= MyUtil.getRandomNumber(0,2)==0 ? RADIUS : Constant.FRAME_WIDTH-RADIUS;
        int y= GameFrame.titleBarH+RADIUS;
        int dir=DIR_DOWN;
//        Tank enemy = new EnemyTank(x, y, dir);
        EnemyTank enemy= (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        //根据游戏难度设置敌人血量
        int maxHp=Tank.DEFAULT_HP*LevelInfo.getInstance().getLevelType();
        enemy.setHp(maxHp);
        enemy.setMaxHp(maxHp);

        enemy.setEnemy(true);
        enemy.setState(STATE_MOVE);

        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);

        return enemy;
    }

    private void ai(){
        if(System.currentTimeMillis()-aiTime > Constant.ENEMY_AI_INTERVAL){
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2)==0 ? STATE_STAND:STATE_MOVE);
            aiTime=System.currentTimeMillis();
        }
        if(Math.random()<Constant.ENEMY_FIRE_PERCENT){
            fire();
        }
    }




    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
