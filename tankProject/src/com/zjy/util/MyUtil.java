package com.zjy.util;

import java.awt.*;
import java.lang.reflect.Modifier;
import java.util.Map;

public class MyUtil {
    private MyUtil(){}
    //得到随机数
    public static final int getRandomNumber(int min,int max){
        return (int)(Math.random()*(max-min)+min);
    }

    /**
     * 得到随机颜色
     * @return
     */
    public static final Color getRandomColor(){
        int red=getRandomNumber(0,256);
        int blue=getRandomNumber(0,256);
        int green=getRandomNumber(0,256);
        return new Color(red,green,blue);
    }

    /**
     * 判断一个点是否在正方形的内部
     * @param rectX 正方形的中心点的x
     * @param rectY 正方形的中心点的y
     * @param radius 正方形的边长的一半
     * @param pointX 点的x
     * @param pointY 点的y
     * @return 在内部返回true
     */
    public static final boolean isCollide(int rectX,int rectY,int radius,int pointX,int pointY ){

        int disX=Math.abs(rectX-pointX);
        int disY=Math.abs(rectY-pointY);
        if(disX<radius && disY<radius){
            return true;
        }
        return false;

    }

    /**
     * 根据图片路径加载图片对象
     * @param path 图片路径
     * @return
     */
    public  static final Image createImage(String path){
        return Toolkit.getDefaultToolkit().createImage(path);
    }

    private static final String[] NAMES = {
            "行人","乐园","花草","人才","左手","目的","课文","优点","年代","灰尘",
            "沙子","小说","儿女","难题","明星","本子","彩色","水珠","路灯","把握",
            "房屋","心愿","左边","新闻","早点",
            "市场","雨点","细雨","书房","毛巾","画家","元旦","绿豆","本领","起点",
            "青菜","土豆","总结","礼貌","右边",
            "老虎","老鼠","猴子","树懒"," 斑马","小狗","狐狸","狗熊","黑熊",
            "大象","豹子"," 麝牛","狮子","熊猫","疣猪","羚羊","驯鹿","考拉",
            "犀牛","猞猁","猩猩","海牛","水獭","海豚","海象","刺猬","袋鼠",
            "犰狳","河马","海豹","海狮","蝙蝠","白虎","狸猫","水牛","山羊",
            "绵羊","牦牛","猿猴","松鼠","野猪","豪猪","麋鹿","花豹","野狼",
            "灰狼","蜂猴","熊猴","叶猴","紫貂","貂熊","熊狸","云豹","雪豹",
            "黑麂","野马","鼷鹿","坡鹿","豚鹿","野牛","藏羚","河狸","驼鹿",
            "黄羊","鬣羚","斑羚","岩羊","盘羊","雪兔"
    };

    private static final String[] MODIFIY = {
            "可爱","傻傻","萌萌","羞羞","笨笨","呆呆","美丽","聪明","伶俐","狡猾",
            "胖乎乎","粉嫩嫩","白胖胖","漂亮","可爱","聪明","懂事","乖巧","淘气",
            "淘气","顽劣","调皮","顽皮","天真","可爱","无邪","单纯","纯洁","无暇",
            "纯真","稚气","温润","好奇"
    };

    public static final String getRandomName(){
        return MODIFIY[getRandomNumber(0,MODIFIY.length)]+"的"+NAMES[getRandomNumber(0,NAMES.length)];
    }
}
