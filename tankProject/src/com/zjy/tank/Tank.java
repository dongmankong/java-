package com.zjy.tank;

import com.zjy.game.Bullet;
import com.zjy.game.Explode;
import com.zjy.game.GameFrame;
import com.zjy.map.MapTile;
import com.zjy.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Tank {
    //四个方向
    public static final int DIR_UP=0;
    public static final int DIR_DOWN=1;
    public static final int DIR_LEFT=2;
    public static final int DIR_RIGHT=3;

    public static final int RADIUS=20;
    //默认速度 每帧
    public static final int DEFAULT_SPEED=6;

    //状态
    public static final int STATE_STAND=0;
    public static final int STATE_MOVE=1;
    public static final int STATE_DIE=2;
    //初始生命值
    public static final int DEFAULT_HP=100;
    public  int maxHp=DEFAULT_HP;


    private int x,y;

    private int hp=DEFAULT_HP;
    private String name;
    private int atk;
    public static final int ATK_MAX=25;
    public static final int ATK_MIN=15;
    private int speed=DEFAULT_SPEED;
    private int dir;
    private int state=STATE_STAND;
    private Color color;
    private boolean isEnemy=false;

    private BloodBar bar=new BloodBar();


    //炮弹
    private List<Bullet> bullets=new ArrayList<>();
    //爆炸效果的容器
    private List<Explode> explodes=new ArrayList<>();

    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initTank();
    }
    public Tank(){
        initTank();
    }
    private void initTank(){
        this.color= MyUtil.getRandomColor();
        name=MyUtil.getRandomName();
        atk=MyUtil.getRandomNumber(ATK_MIN,ATK_MAX);
    }

    public void draw(Graphics g){
        logic();
//        drawTank(g);
        drawImgTank(g);
        drawBullets(g);
        drawName(g);
        bar.draw(g);
    }

    private void drawName(Graphics g){
        g.setColor(color);
        g.setFont(Constant.SMALL_FONT);
        g.drawString(name,x-RADIUS,y-35);
    }

    /**
     * 使用图片绘制
     * @param g
     */
    public abstract void drawImgTank(Graphics g);


    /**
     * 使用系统方式
     * @param g
     */
    private void drawTank(Graphics g){
        g.setColor(color);
        g.fillOval(x-RADIUS,y-RADIUS,RADIUS<<1,RADIUS<<1);
        int endX=x;
        int endY=y;
        switch (dir){
            case DIR_UP:
                endY=y-RADIUS*2;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);

                break;
            case DIR_DOWN:
                endY=y+RADIUS*2;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_LEFT:
                endX=x-RADIUS*2;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
            case DIR_RIGHT:
                endX=x+RADIUS*2;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
        }
        g.drawLine(x,y,endX,endY);
    }

    //坦克的逻辑处理
    private void logic(){
        switch (state){
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }
    //移动
    private int oldX=-1,oldY=-1;
    private void move(){
        oldX=x;
        oldY=y;
        switch (dir){
            case DIR_UP:
                y-=speed;
                if(y<RADIUS+GameFrame.titleBarH){
                    y=RADIUS+GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y+=speed;
                if(y> Constant.FRAME_HEIGHT-RADIUS){
                    y=Constant.FRAME_HEIGHT- RADIUS;
                }
                break;
            case DIR_LEFT:
                x-=speed;
                if(x<RADIUS){
                    x=RADIUS;
                }
                break;
            case DIR_RIGHT:
                x+=speed;
                if(x>Constant.FRAME_WIDTH-RADIUS){
                    x=Constant.FRAME_WIDTH-RADIUS;
                }
                break;
        }
    }


    private long fireTime;
    //子弹攻击间隔
    public static final int FIRE_INTERVAL=200;
    //坦克开火
    public void fire(){
        if(System.currentTimeMillis()-fireTime>FIRE_INTERVAL){
            int bulletX=x;
            int bulletY=y;
            switch (dir){
                case DIR_UP:
                    bulletY-=RADIUS;
                    break;
                case DIR_DOWN:
                    bulletY+=RADIUS;
                    break;
                case DIR_LEFT:
                    bulletX-=RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletX+=RADIUS;
                    break;
            }
            Bullet bullet= BulletsPool.get();
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);

            fireTime=System.currentTimeMillis();
        }

    }

    /**
     * 发射子弹 绘制
     * @param g
     */
    private void drawBullets(Graphics g){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }
        //将不可见的子弹换回子弹池
        for (int i = 0; i < bullets.size(); i++) {
               Bullet bullet=bullets.get(i);
               if(!bullet.isVisible()){
                   Bullet remove=bullets.remove(i);
                   i--;
                   BulletsPool.theReturn(remove);
               }
        }
    }
    //坦克销毁的时候处理子弹
    public void bulletsReturn(){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletsPool.theReturn(bullet);
        }
        bullets.clear();
    }

    //坦克和子弹碰撞的方法
    public void collideBullets(List<Bullet> bullets){
        for (Bullet bullet : bullets) {
            int bulletX=bullet.getX();
            int bulletY=bullet.getY();
            //子弹和坦克碰撞
            if(MyUtil.isCollide(x,y,RADIUS,bulletX,bulletY)){
                bullet.setVisible(false);

                hurt(bullet);
                //添加爆炸效果
                addExplode(x,y+RADIUS);
            }
        }

    }

    private void addExplode(int x,int y){
        Explode explode = ExplodesPool.get();
//                new Explode(x,y+RADIUS)
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);
        explode.setIndex(0);
        explodes.add(explode);
    }

    /**
     * 坦克受到伤害
     * @param bullet
     */
    private void hurt(Bullet bullet){
        int atk = bullet.getAtk();
        hp-=atk;
        if(hp<=0){
            hp=0;
            die();
        }
    }

    public boolean isDie(){
        return hp<=0;
    }


    /**
     * 坦克死亡
     */
    private void die(){
        if(isEnemy){
            GameFrame.killEnemyCount++;
            System.out.println(GameFrame.killEnemyCount);
            EnemyTanksPool.theReturn(this);
            if (GameFrame.isCrossLevel()) {
                if(GameFrame.isLastLevel()){
                    GameFrame.setGameState(Constant.STAGE_WIN);
                }else {
//                    GameFrame.nextLevel();
                    GameFrame.startCrossLevel();
                }
            }
        }else{
            delaySecondsToOver(1000);
        }

    }


    /**
     * 绘制当前坦克的所有的爆炸的效果
     * @param g
     */
    public void drawExplodes(Graphics g){
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode=explodes.get(i);
            if(!explode.isVisible()){
                Explode remove = explodes.remove(i);
                i--;
                ExplodesPool.theReturn(remove);
            }
        }
    }


    class BloodBar{
        public static final int BAR_LENGTH=50;
        public static final int BAR_HEIGHT=3;

        public void draw(Graphics g){
            g.setColor(Color.YELLOW);
            g.fillRect(x-RADIUS,y-RADIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);

            g.setColor(Color.RED);
            g.fillRect(x-RADIUS,y-RADIUS-BAR_HEIGHT*2,hp*BAR_LENGTH/maxHp,BAR_HEIGHT);

            g.setColor(Color.WHITE);
            g.drawRect(x-RADIUS,y-RADIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
        }
    }

    //坦克的子弹和地图块的碰撞
    public void bulletsCollideMapTiles(List<MapTile> tiles){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if(tile.isCollideBullet(bullets)){
                addExplode(tile.getX()+MapTile.radius,tile.getY()+MapTile.titleW);
                //地图水泥块没被击毁
                if(tile.getType()==MapTile.TYPE_HARD){
                    continue;
                }
                //地图块销毁
                tile.setVisible(false);
//                MapTilePool.theReturn(tile);
                //当老巢被击毁之后，1s后切换到结束界面
                if(tile.isHouse()){
                    delaySecondsToOver(1000);
                }
            }
        }
    }

    /**
     * 一个地图块和当前坦克碰撞的方法
     * 从tile中提取8个点来判断
     * 点的顺序为tile的顺时针
     * @param tiles
     * @return
     */
    public boolean isCollideTile(List<MapTile> tiles){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if(tile.isVisible() && tile.getType()==MapTile.TYPE_COVER){
                continue;
            }
            //点1
            int tileX=tile.getX();
            int tileY=tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //2
            tileX+=MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //3
            tileX+=MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //4
            tileY+=MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //5
            tileY+=MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //6
            tileX-=MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //7
            tileX-=MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //8
            tileY-=MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
        }
        return false;
    }

    /**
     * 坦克回退
     */
    public void back(){
        x=oldX;
        y=oldY;
    }

    /**
     * 延迟几毫秒 切换到游戏结束
     * @param millisSecond
     */
    public void delaySecondsToOver(int millisSecond){
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(millisSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STAGE_LOST);
            }
        }.start();
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List bullets) {
        this.bullets = bullets;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
}
