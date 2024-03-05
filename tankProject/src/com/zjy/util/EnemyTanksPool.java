package com.zjy.util;

import com.zjy.game.Bullet;
import com.zjy.tank.EnemyTank;
import com.zjy.tank.Tank;

import java.util.ArrayList;
import java.util.List;

public class EnemyTanksPool {

    public static final int DEFAULT_POOL_SIZE=20;

    public static final int POOL_MAX_SIZE=20;

    private static List<Tank> pool=new ArrayList<>();

    static{
        for(int i=0;i<DEFAULT_POOL_SIZE;++i){
            pool.add(new EnemyTank());
        }
    }

    /**
     * 从坦克池中获得一个坦克
     * @return
     */
    public static Tank get(){
        Tank tank=null;
        if(pool.size()==0){
            tank=new EnemyTank();
        }else {
            tank=pool.remove(0);
        }
        return tank;
    }

    public static void theReturn(Tank tank){
        if(pool.size()==POOL_MAX_SIZE){
            return;
        }
        pool.add(tank);
    }
}

