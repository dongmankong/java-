package com.zjy.util;

import java.awt.*;

/**
 * 常量类
 */
public class Constant {
    public static final String GAME_TITLE="坦克大战";

    public static final int FRAME_WIDTH=900;
    public static final int FRAME_HEIGHT=700;

    public static final int SCREEN_W=Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H=Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int FRAME_X=SCREEN_W-FRAME_WIDTH>>1;
    public static final int FRAME_Y=SCREEN_H-FRAME_HEIGHT>>1;

    public static final int STAGE_MENU=0;
    public static final int STAGE_HELP=1;
    public static final int STAGE_ABOUT=2;
    public static final int STAGE_RUN=3;
    public static final int STAGE_LOST=4;
    public static final int STAGE_WIN=5;
    public static final int STAGE_CROSS=6;

    public static final String[] MENUS={
            "开始游戏",
            "继续游戏",
            "游戏帮助",
            "游戏关于",
            "退出游戏",
    };

    public static final String OVER_STR0="ESC键退出游戏";
    public static final String OVER_STR1="ENTER键返回主菜单";

    public static final Font GAME_FONT=new Font("宋体",Font.BOLD,28);

    public static final Font SMALL_FONT=new Font("宋体",Font.BOLD,12);


    public static final int REPAINT_INTERVAL=30;

    public static final int ENEMY_MAX_COUNT=10;

    public static final int ENEMY_BORN_INTERVAL=5000;

    public static final int ENEMY_AI_INTERVAL=3000;

    public static final double ENEMY_FIRE_PERCENT=0.03;
}
