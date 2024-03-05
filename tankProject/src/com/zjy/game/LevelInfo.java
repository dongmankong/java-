package com.zjy.game;

import com.zjy.util.MyUtil;

//管理当前关卡信息
public class LevelInfo {
    private LevelInfo(){}

    private static LevelInfo instance;

    //懒汉式单例模式
    public static LevelInfo getInstance(){
        if(instance==null){
            instance=new LevelInfo();
        }
        return instance;
    }
    //关卡编号
    private int level;
    private int enemyCount;

    //通关的要求的时长，-1代表不限时
    private int crossTime=-1;

    //敌人类型信息
    private int[] enemyType;

    //关卡难度
    private int levelType;

    //获得敌人类型数组中的随机的一个元素
    //获得一个随机的敌人的类型
    public int getRandomEnemyType(){
        int index=MyUtil.getRandomNumber(0,enemyType.length);
        return enemyType[index];
    }





    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(int crossTime) {
        this.crossTime = crossTime;
    }

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }

    public int getLevelType() {
        return levelType<=0 ? 1 : levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }
}
