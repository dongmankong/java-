package com.zjy.util;

import com.zjy.game.Bullet;

import java.util.ArrayList;
import java.util.List;

public class BulletsPool {

    public static final int DEFAULT_POOL_SIZE=200;

    public static final int POOL_MAX_SIZE=300;

    private static List<Bullet> pool=new ArrayList<>();

    static{
        for(int i=0;i<DEFAULT_POOL_SIZE;++i){
            pool.add(new Bullet());
        }
    }

    /**
     * 从子弹池中获得一个子弹
     * @return
     */
    public static Bullet get(){
        Bullet bullet=null;
        if(pool.size()==0){
            bullet=new Bullet();
        }else {
            bullet=pool.remove(0);
        }
        return bullet;
    }

    public static void theReturn(Bullet bullet){
        if(pool.size()==POOL_MAX_SIZE){
            return;
        }
        pool.add(bullet);
    }
}
