package com.zjy.util;

import com.zjy.game.Bullet;
import com.zjy.game.Explode;

import java.util.ArrayList;
import java.util.List;

public class ExplodesPool {

    public static final int DEFAULT_POOL_SIZE=10;

    public static final int POOL_MAX_SIZE=20;

    private static List<Explode> pool=new ArrayList<>();

    static{
        for(int i=0;i<DEFAULT_POOL_SIZE;++i){
            pool.add(new Explode());
        }
    }

    /**
     * 从爆炸池中获得一个爆炸
     * @return
     */
    public static Explode get(){
        Explode explode=null;
        if(pool.size()==0){
            explode=new Explode();
        }else {
            explode=pool.remove(0);
        }
        return explode;
    }

    public static void theReturn(Explode explode){
        if(pool.size()==POOL_MAX_SIZE){
            return;
        }
        pool.add(explode);
    }
}
