package com.zjy.game;

import com.zjy.map.GameMap;
import com.zjy.map.MapTile;
import com.zjy.tank.EnemyTank;
import com.zjy.tank.MyTank;
import com.zjy.tank.Tank;
import com.zjy.util.Constant;
import com.zjy.util.MyUtil;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
//导入所有静态成员
import static com.zjy.util.Constant.*;
/**
 *游戏主窗口类
 */
public class GameFrame extends Frame implements Runnable{

    private Image overImg=null;

    //定义一张和屏幕大小一样的图片
//    private BufferedImage bufImg=new BufferedImage(FRAME_WIDTH,FRAME_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);

    //游戏状态
    private static int gameState;

    private static int menuIndex;
    //标题栏高度
    public static int titleBarH;

    private static Tank myTank;

    private static List<Tank> enemies=new ArrayList<>();

    private static GameMap gameMap=new GameMap();
    //记录本关卡产生了多少敌人
    private static int bornEnemyCount;
    //消灭的敌人数（本关）
    public static int killEnemyCount;
    private GameMap gameMap1;

    public GameFrame(){
        initEventListener();
        initFrame();
        //启动用于刷新的线程
        new Thread(this).start();

    }
    public void initGame(){
        gameState=STAGE_MENU;
    }

    private void initFrame() {
        setTitle(GAME_TITLE);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
//        setLocation(FRAME_X,FRAME_Y);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        //求标题栏高度
        titleBarH=getInsets().top;
    }


    /**
     * g1 系统提供的画笔（图），系统初始化
     * @param g1
     */
    private Image bufImg;
    public void update(Graphics g1){
        if(bufImg==null){
            bufImg=this.createImage(FRAME_WIDTH,FRAME_HEIGHT);
        }
        //得到自己定义的图
        Graphics g=bufImg.getGraphics();

        //在自己定义的图上画图
        g.setFont(GAME_FONT);
        switch (gameState){
            case STAGE_MENU:
                drawMenu(g);
                break;
            case STAGE_ABOUT:
                drawAbout(g);
                break;
            case STAGE_HELP:
                drawHelp(g);
                break;
            case STAGE_RUN:
                drawRun(g);
                break;
            case STAGE_LOST:
                drawLost(g,"游戏失败");
                break;
            case STAGE_WIN:
                drawWin(g);
                break;
            case STAGE_CROSS:
                drawCross(g);
                break;
        }

        //把自己画的图放到g1上
        g1.drawImage(bufImg,0,0,null);
    }


    //绘制游戏结束
    private void drawLost(Graphics g,String str) {
        if(overImg ==null){
            overImg= MyUtil.createImage("res/over.jpg");
        }
        int imgW=overImg.getWidth(null);
        int imgH=overImg.getHeight(null);

        g.setColor(Color.black);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);


        g.drawImage(overImg,FRAME_WIDTH-imgW>>1,FRAME_HEIGHT-imgH>>1,null);
        //添加按键提示信息
        g.setColor(Color.WHITE);
        g.drawString(OVER_STR0,10,FRAME_HEIGHT-20);
        g.drawString(OVER_STR1,FRAME_WIDTH-255,FRAME_HEIGHT-20);
        g.setColor(Color.WHITE);
        g.drawString(str,FRAME_WIDTH/2-30,80);
    }

    private void drawRun(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);


        //绘制地图的碰撞层
//        gameMap.drawBk(g);
        drawEnemies(g);

        myTank.draw(g);
        gameMap.drawTile(g);

        //绘制地图的遮挡层
//        gameMap.drawCover(g);
        drawExplodes(g);

        bulletCollideTank();
        //子弹和地图块的碰撞
        bulletAndTanksCollideMapTile();


    }
    //绘制敌人坦克，如果敌人已经死亡，从容器中移除
    private void drawEnemies(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy=enemies.get(i);
            if(enemy.isDie()){
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
    }

    private Image helpImg;
    private void drawHelp(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(helpImg==null){
            helpImg=MyUtil.createImage("res/help.png");
        }
        int width=helpImg.getWidth(null);
        int height=helpImg.getHeight(null);
        int x=FRAME_WIDTH-width>>1;
        int y=FRAME_HEIGHT-height>>1;
        g.drawImage(helpImg,x,y,null);
        g.setColor(Color.WHITE);
        g.drawString("任意键继续",10,FRAME_HEIGHT-10);
    }

    private void drawAbout(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString("游戏关于",FRAME_WIDTH/2-80,FRAME_HEIGHT/2-150);
        g.drawString("制作人：dongmankong",FRAME_WIDTH/2-115,FRAME_HEIGHT/2-65);

        g.drawString("任意键继续",10,FRAME_HEIGHT-10);
    }


    private void drawMenu(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        final int STR_WIDTH=110;
        final int DIS=50;
        int x=FRAME_WIDTH-STR_WIDTH>>1;
        int y=FRAME_HEIGHT/3;

        g.setColor(Color.WHITE);
        for(int i = 0; i < MENUS.length; i++){
            if(i==menuIndex){
                g.setColor(Color.RED);
            }else{
                g.setColor(Color.WHITE);
            }
            g.drawString(MENUS[i],x,y+DIS*i);
        }

    }

    private Image winImg;
    private void drawWin(Graphics g){
//        drawLost(g,"游戏通关");
        if(winImg ==null){
//            winImg= MyUtil.createImage("res/victory.jpg");
            winImg=Toolkit.getDefaultToolkit().getImage("res/win.jpg");
        }

        int imgW=winImg.getWidth(null);
        int imgH=winImg.getHeight(null);
        g.setColor(Color.black);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
//        g.drawImage(winImg,FRAME_WIDTH-imgW>>1,FRAME_HEIGHT-imgH>>1,null);
        g.drawImage(winImg,0,0,FRAME_WIDTH,FRAME_HEIGHT,null);

        //添加按键提示信息
        g.setColor(Color.WHITE);
        g.drawString(OVER_STR0,10,FRAME_HEIGHT-20);
        g.drawString(OVER_STR1,FRAME_WIDTH-255,FRAME_HEIGHT-20);
    }


    private void initEventListener(){
        //注册窗口监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //注册按键监听
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (gameState){
                    case STAGE_MENU:
                        keyPressedEventMenu(keyCode);
                        break;
                    case STAGE_ABOUT:
                        keyPressedEventAbout(keyCode);
                        break;
                    case STAGE_HELP:
                        keyPressedEventHelp(keyCode);
                        break;
                    case STAGE_RUN:
                        keyPressedEventRun(keyCode);
                        break;
                    case STAGE_LOST:
                        keyPressedEventLost(keyCode);
                        break;
                    case STAGE_WIN:
                        keyPressedEventWin(keyCode);
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if(gameState==STAGE_RUN){
                    keyReleasedEventRun(keyCode);
                }
            }

        });
    }
    //游戏通关按键处理
    private void keyPressedEventWin(int keyCode){
        keyPressedEventLost(keyCode);
    }

    private void keyReleasedEventRun(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setState(Tank.STATE_STAND);
                break;
        }
    }

    //游戏结束的按键的处理
    private void keyPressedEventLost(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        else if(keyCode == KeyEvent.VK_ENTER){
            setGameState(STAGE_MENU);
            //重置许多参数
            resetGame();

        }
    }
    //重置游戏
    private void resetGame(){
        killEnemyCount=0;
        menuIndex=0;
        //处理自己的子弹
        myTank.bulletsReturn();
        myTank=null;

        //处理敌人的子弹
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
        }
        enemies.clear();

//        gameMap.setTankHouse(null);

        gameMap.titlesReturn();

//        gameMap=null;
    }

    private void keyPressedEventRun(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.DIR_UP);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);

                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);

                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);

                break;
            case KeyEvent.VK_SPACE:
                myTank.fire();
                break;
        }
    }

    private void keyPressedEventHelp(int keyCode) {
        setGameState(STAGE_MENU);
    }

    private void keyPressedEventAbout(int keyCode) {
        setGameState(STAGE_MENU);
    }

    //菜单状态下的按下
    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if(--menuIndex<0){
                    menuIndex=MENUS.length-1;
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if(++menuIndex>MENUS.length-1){
                    menuIndex=0;
                }
                break;
            case KeyEvent.VK_ENTER:
                switch (menuIndex){
                    case 0:
                        startGame(1);
                        break;
                    case 1:
                        startGame(GameInfo.getLastLevel());
                        break;
                    case 2:
                        setGameState(STAGE_HELP);
                        break;
                    case 3:
                        setGameState(STAGE_ABOUT);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                }
                break;
        }

    }

    /**
     * 开始新游戏
     */
    private static Thread thread;
    private static void startGame(int level) {
        GameInfo.setLastLevel(level);
        enemies.clear();
        if(gameMap==null){
            gameMap=new GameMap();
        }
        gameMap.initMap(level);
        bornEnemyCount=0;
        killEnemyCount=0;
        gameState=STAGE_RUN;
        System.out.println();
//        for (int i = 0; i < gameMap.getTiles().size(); i++) {
//            MapTile tile = gameMap.getTiles().get(i);
//            System.out.print(tile.getX());
//            System.out.print("+");
//            System.out.print(tile.getY());
//            System.out.println("-------");
//        }
        myTank=new MyTank(FRAME_WIDTH/3,FRAME_HEIGHT-Tank.RADIUS,Tank.DIR_UP);
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        thread=null;
        //单独线程 创建坦克
//        if(thread==null){
            thread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()){
                        //只有在游戏run状态 才创建敌人
                        if(gameState != STAGE_RUN){
                            break;
                        }
                        if(LevelInfo.getInstance().getEnemyCount()>bornEnemyCount &&
                                enemies.size()<ENEMY_MAX_COUNT){
                            Tank enemy= EnemyTank.createEnemy();
                            enemies.add(enemy);
                            bornEnemyCount++;
                        }
                        try {
                            Thread.sleep(ENEMY_BORN_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println("Thread sleep interrupted");
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            });
            thread.start();
//        }

//        new Thread(() -> {
//            while (true){
//                if(LevelInfo.getInstance().getEnemyCount()>bornEnemyCount &&
//                        enemies.size()<ENEMY_MAX_COUNT){
//                    Tank enemy= EnemyTank.createEnemy();
//                    enemies.add(enemy);
//                    bornEnemyCount++;
//                }
//                try {
//                    Thread.sleep(ENEMY_BORN_INTERVAL);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                //只有在游戏run状态 才创建敌人
//                if(gameState != STAGE_RUN){
//                    break;
//                }
//            }
//        }).start();
    }


    @Override
    public void run() {
        while(true){
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //敌人的坦克的子弹与我的坦克的碰撞
    //我的坦克的子弹与所有的敌人的碰撞
    private void bulletCollideTank(){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.collideBullets(myTank.getBullets());

        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy=enemies.get(i);
            myTank.collideBullets(enemy.getBullets());

        }
//        for (Tank enemy : enemies) {
//            //我的坦克的子弹与所有的敌人的碰撞
//        enemy.collideBullets(myTank.getBullets());
//        }
//
//        for (Tank enemy : enemies) {
//        myTank.collideBullets(enemy.getBullets());
//        }
    }

    //所有子弹和地图块的碰撞
    private void bulletAndTanksCollideMapTile(){
        //坦克的子弹与地图的碰撞
        myTank.bulletsCollideMapTiles(gameMap.getTiles());
        //坦克的子弹与地图的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy=enemies.get(i);
            enemy.bulletsCollideMapTiles(gameMap.getTiles());

        }

//        for (Tank enemy : enemies) {
//            enemy.bulletsCollideMapTiles(gameMap.getTiles());
//        }
        //坦克与地图的碰撞
        if(myTank.isCollideTile(gameMap.getTiles())){
            myTank.back();
        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isCollideTile(gameMap.getTiles())){
                enemy.back();
            }
        }
//        //坦克与地图的碰撞
//        for (Tank enemy : enemies) {
//            if(enemy.isCollideTile(gameMap.getTiles())){
//                enemy.back();
//            }
//        }
        //清理所有的被销毁的地图块
        gameMap.clearDestroyTile();
    }

    //所有的坦克的爆炸效果
    private void drawExplodes(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.drawExplodes(g);
        }
//        for (Tank enemy : enemies) {
//            enemy.drawExplodes(g);
//        }
        myTank.drawExplodes(g);
    }

    //是否是最后一关
    public static boolean isLastLevel(){
        //关卡数
        int curLevel=LevelInfo.getInstance().getLevel();
        int levelCount=GameInfo.getLevelCount();
        return curLevel==levelCount;
    }




    /**
     * 判断是否过关
     * @return
     */
    public static boolean isCrossLevel(){
        return killEnemyCount==LevelInfo.getInstance().getEnemyCount();
    }

    public static void nextLevel(){
        startGame(LevelInfo.getInstance().getLevel()+1);
    }
    //开始过关动画
    public static int flashTime;
    public static final int RECT_WIDTH=40;
    public static final int RECT_COUNT=FRAME_WIDTH/RECT_WIDTH+1;
    public static boolean isOpen=false;
    public static void startCrossLevel(){
        gameState=STAGE_CROSS;
        flashTime=0;
        isOpen=false;
    }
    //绘制过关动画
    public void drawCross(Graphics g){
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);
        g.setColor(Color.BLACK);
        if(!isOpen){
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i*RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            if(flashTime++-RECT_WIDTH>5){
                isOpen=true;
//                gameMap.initMap(LevelInfo.getInstance().getLevel()+1);
            }
        }else {
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i*RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            if(flashTime--==0){
//                GameInfo.setLastLevel(LevelInfo.getInstance().getLevel()+1);
                startGame(LevelInfo.getInstance().getLevel()+1);
            }
        }

    }


    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }

    public static int getGameState() {
        return gameState;
    }
}
