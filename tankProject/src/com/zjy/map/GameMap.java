package com.zjy.map;

import com.zjy.game.GameFrame;
import com.zjy.game.LevelInfo;
import com.zjy.tank.Tank;
import com.zjy.util.Constant;
import com.zjy.util.MapTilePool;
import com.zjy.util.MyUtil;

import javax.management.MXBean;
import java.awt.*;
import java.io.FileInputStream;
import java.util.*;
import java.util.List;

public class GameMap {
    public static final int MAP_X=Tank.RADIUS*3;
    public static final int MAP_Y=Tank.RADIUS*3+ GameFrame.titleBarH;
    public static final int MAP_WIDTH= Constant.FRAME_WIDTH-Tank.RADIUS*6;
    public static final int MAP_HEIGHT=Constant.FRAME_HEIGHT-Tank.RADIUS*8-GameFrame.titleBarH;

    private List<MapTile> tiles=new ArrayList<>();

    private TankHouse tankHouse;

    public GameMap(){
    }

    public void initMap(int level){
        if(tankHouse==null){
            tankHouse=new TankHouse();
        }
        tankHouse.getTiles().clear();
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        final int COUNT=20;
//        for (int i = 0; i < COUNT; i++) {
//            MapTile mapTile = MapTilePool.get();
//            int x= MyUtil.getRandomNumber(MAP_X, MAP_X+MAP_WIDTH-MapTile.titleW);
//            int y=MyUtil.getRandomNumber(MAP_Y,MAP_Y+MAP_HEIGHT-MapTile.titleW);
//            if(isCollide(tiles,x,y)){
//                i--;
//                continue;
//            }
//            mapTile.setX(x);
//            mapTile.setY(y);
//            tiles.add(mapTile);
//        }
//        addRow(MAP_X,MAP_Y,MAP_X+MAP_WIDTH,MapTile.TYPE_NORMAL,0);
//        addRow(MAP_X,MAP_Y+MapTile.titleW*2,MAP_X+MAP_WIDTH,MapTile.TYPE_COVER,0);
//        addRow(MAP_X,MAP_Y+MapTile.titleW*4,MAP_X+MAP_WIDTH,MapTile.TYPE_HARD,MapTile.titleW+6);

//        tankHouse=new TankHouse();
//        for (int i = 0; i <this.getTiles().size(); i++) {
////            MapTile tile = this.getTiles().get(i);
////            System.out.print(tile.getX());
////            System.out.print("+");
////            System.out.print(tile.getY());
////            System.out.println("-------");
////        }
        tankHouse.createTankHouse();
        addHouse();

//        System.out.println(tiles.size());

    }

    /**
     * 判断某一个地图块是否和tiles中的所有的块有重叠的部分
     * @param tiles
     * @param x
     * @param y
     * @return
     */
    private boolean isCollide(List<MapTile> tiles,int x,int y){
        for (MapTile tile : tiles) {
            int tileX=tile.getX();
            int tileY=tile.getY();
            if(Math.abs(tileX-x)< MapTile.titleW && Math.abs(tileY-y)<MapTile.titleW){
                return true;
            }
        }
        return false;
    }

    /**
     * 只对没有遮挡效果的块进行绘制
     * @param g
     */
    public void drawBk(Graphics g){
        for (MapTile tile : tiles) {
            if(tile.getType()!=MapTile.TYPE_COVER){
                tile.draw(g);
            }
        }
    }
    public void drawTile(Graphics g){
        for (MapTile tile : tiles) {
                tile.draw(g);
        }
    }
    /**
     * 只绘制有遮挡效果的块
     * @param g
     */
    public void drawCover(Graphics g){
        for (MapTile tile : tiles) {
            if(tile.getType()==MapTile.TYPE_COVER){
                tile.draw(g);
            }
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }
    //移除所有不可见的地图块
    public void clearDestroyTile(){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if(!tile.isVisible()){
                tiles.remove(i);
                i--;
            }

        }
    }

    //将老巢的所有的元素块添加到地图的容器中
    private void addHouse(){
        tiles.addAll(tankHouse.getTiles());
    }

    private final Object tilesLock = new Object();
    // 拿到同样的地图块？？？
    /**
     *
     * @param startX 添加地图块的起始x坐标
     * @param startY 起始y坐标
     * @param endX x结束坐标
     * @param type 地图块的类型
     * @param DIS 地图块之间的中心点的间隔
     */
    public synchronized void addRow(int startX,int startY,int endX,int type,final int DIS){
            int count  = (endX - startX +DIS )/(MapTile.titleW+DIS);
            for (int i = 0; i < count; ++i) {
                int x=startX+i*(MapTile.titleW+DIS);
                for (int i1 = 0; i1 < tiles.size(); i1++) {
                    if(tiles.get(i1).getX()==x && tiles.get(i1).getY()==startY){
                        System.out.print(startX+i*(MapTile.titleW+DIS));
                        System.out.print("+");
                        System.out.print(startY);
                        System.out.println();
                        System.out.println(i1);
                        System.out.println("-------------------------------------------");
                    }
                }
                MapTile tile = MapTilePool.get();
                tile.setType(type);
                tile.setX(startX+i*(MapTile.titleW+DIS));
                tile.setY(startY);
                tile.setVisible(true);
//                System.out.print(startX+i*(MapTile.titleW+DIS));
//                System.out.print("+");
//                System.out.print(startY);
//                System.out.println("-------");
//                for (int i1 = 0; i1 < tiles.size(); i1++) {
//                    if(tiles.get(i1).getX()==tile.getX() && tiles.get(i1).getY()==tile.getY()){
//                        System.out.print(startX+i*(MapTile.titleW+DIS));
//                        System.out.print("+");
//                        System.out.print(startY);
//                        System.out.println();
//                        System.out.println(i1);
//                        System.out.println("-------------------------------------------");
//                    }
//                }
                tiles.add(tile);
            }
    }


    /**
     * 地图添加一列元素
     * @param startX
     * @param startY
     * @param endY
     * @param type
     * @param DIS
     */
    public void addCol(int startX,int startY,int endY,int type,final int DIS){
        int count=0;
        count=(endY-startY)/(MapTile.titleW+DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX);
            tile.setY(startY+i*(MapTile.titleW+DIS));

            tile.setVisible(true);

            tiles.add(tile);
        }
    }

    /**
     * 对指定的矩形区域添加元素块
     */
    public void addRect(int startX,int startY,int endX,int endY,int type,final int DIS){
        int rows=(endY-startY)/(MapTile.titleW+DIS);
        for (int i = 0; i < rows; i++) {
            addRow(startX, startY+i*(MapTile.titleW+DIS), endX, type, DIS);
        }

//        int cols=(endX-startX)/(MapTile.titleW+DIS);


    }


    private void loadLevel(int level)throws Exception{
        //获得关卡信息唯一的实例类
        LevelInfo levelInfo = LevelInfo.getInstance();

        levelInfo.setLevel(level);

        Properties prop=new Properties();
        prop.load(new FileInputStream("level/lv_"+level));
        int enemyCount=Integer.parseInt(prop.getProperty("enemyCount"));
        levelInfo.setEnemyCount(enemyCount);
        //设置敌人类型
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[] type=new int[enemyType.length];
        for (int i = 0; i < enemyType.length; i++) {
            type[i]=Integer.parseInt(enemyType[i]);
        }
        levelInfo.setEnemyType(type);

        String levelType=prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(levelType==null ? "1": levelType));

        String methodName=prop.getProperty("method");
        int invokeCount=Integer.parseInt(prop.getProperty("invokeCount"));
        String[] params=new String[invokeCount];
        for (int i = 1; i <=invokeCount; i++) {
            params[i-1]=prop.getProperty("param"+i);

        }
        tiles.clear();
        //TODO
        invokeMethod(methodName,params);
//        for (int i = 0; i < this.getTiles().size(); i++) {
//            MapTile tile = this.getTiles().get(i);
//            System.out.print(tile.getX());
//            System.out.print("+");
//            System.out.print(tile.getY());
//            System.out.println("-------");
//        }
    }

    //根据方法的名字和参数调用对应的方法
    private void invokeMethod(String name,String[] params){
        //解析每一行参数
        for (String param : params) {
            String[] split=param.split(",");
            int[] arr=new int[split.length];
            int i;
            for (i = 0; i < split.length-1; i++) {
                arr[i]=Integer.parseInt(split[i]);
            }

            //块间隔为地图块的倍数
            final int DIS=MapTile.titleW;
            int dis=(int)(Double.parseDouble(split[i])*DIS);
            switch (name){
                case "addRow":
                    addRow(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_X+MAP_WIDTH-arr[2]*DIS,arr[3],dis);
                    break;
                case "addCol":
                    addCol(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_Y+MAP_HEIGHT-arr[2]*DIS,arr[3],dis);
                    break;
                case "addRect":
                    addRect(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_X+MAP_WIDTH-arr[2]*DIS,MAP_Y+MAP_HEIGHT-arr[3]*DIS,arr[4],dis);
                    break;
            }
        }
    }


    public void titlesReturn(){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            MapTilePool.theReturn(tile);
        }
        tiles.clear();
    }


    public void setTankHouse(TankHouse tankHouse) {
        this.tankHouse = tankHouse;
    }


    public TankHouse getTankHouse() {
        return tankHouse;
    }

}
