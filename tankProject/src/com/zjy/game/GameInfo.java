package com.zjy.game;

import java.io.*;
import java.util.Properties;

public class GameInfo {
    //关卡数量
    private static int levelCount;
    private static int lastLevel;
    private static Properties prop;
    static {
        prop=new Properties();
        try {
            prop.load(new FileInputStream("level/gameinfo"));
            levelCount=Integer.parseInt(prop.getProperty("levelCount"));
            lastLevel=Integer.parseInt(prop.getProperty("lastLevel"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void changeGameInfo(int level){
//        prop.setProperty("lastLevel",);
//    }


    public static int getLevelCount() {
        return levelCount;
    }

    public static int getLastLevel() {
        return lastLevel;
    }

    public static void setLastLevel(int lastLevel) {
        try {
            prop.setProperty("lastLevel", String.valueOf(lastLevel));
            OutputStream output = new FileOutputStream("level/gameinfo");
            prop.store(output, null);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GameInfo.lastLevel = lastLevel;
    }
}
