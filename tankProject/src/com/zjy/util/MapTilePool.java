package com.zjy.util;

import com.zjy.map.MapTile;
import com.zjy.tank.EnemyTank;
import com.zjy.tank.Tank;

import java.util.ArrayList;
import java.util.List;

public class MapTilePool {

    public static final int DEFAULT_POOL_SIZE=200;

    public static final int POOL_MAX_SIZE=200;

    private static List<MapTile> pool=new ArrayList<>();

    static{
        for(int i=0;i<DEFAULT_POOL_SIZE;++i){
            pool.add(new MapTile());
        }
    }

    /**
     * 从地砖池中获得一个砖块
     * @return
     */
    public static synchronized MapTile get(){
        MapTile mapTile=null;
        if(pool.size()==0){
            mapTile=new MapTile();
        }else {
            mapTile=pool.remove(0);
        }
        return mapTile;
    }

    public static synchronized void theReturn(MapTile mapTile){
        if(pool.size()==POOL_MAX_SIZE){
            return;
        }
        pool.add(mapTile);
    }

}

