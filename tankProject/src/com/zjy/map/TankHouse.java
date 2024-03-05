package com.zjy.map;

import com.zjy.util.Constant;
import com.zjy.util.MapTilePool;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TankHouse {
    public static final int HOUSE_X= (Constant.FRAME_WIDTH-3*MapTile.titleW>>1)+2;
    public static final int HOUSE_Y=Constant.FRAME_HEIGHT-2*MapTile.titleW;

    private List<MapTile> tiles=new ArrayList<>();
    public TankHouse() {
//        tiles.add(new MapTile(HOUSE_X,HOUSE_Y));
//        tiles.add(new MapTile(HOUSE_X,HOUSE_Y+MapTile.titleW));
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW,HOUSE_Y));
//
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW*2,HOUSE_Y));
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW*2,HOUSE_Y+MapTile.titleW));
//        //设置老巢地图块类型
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW,HOUSE_Y+MapTile.titleW));
//        tiles.get(tiles.size()-1).setType(MapTile.TYPE_HOUSE);
    }

    public void createTankHouse(){
//        tiles.add(new MapTile(HOUSE_X,HOUSE_Y));
//        tiles.add(new MapTile(HOUSE_X,HOUSE_Y+MapTile.titleW));
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW,HOUSE_Y));
//
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW*2,HOUSE_Y));
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW*2,HOUSE_Y+MapTile.titleW));
//        //设置老巢地图块类型
//        tiles.add(new MapTile(HOUSE_X+MapTile.titleW,HOUSE_Y+MapTile.titleW));

        MapTile tile1 = MapTilePool.get();
        tile1.setX(HOUSE_X);
        tile1.setY(HOUSE_Y);
        tile1.setVisible(true);
        tile1.setType(MapTile.TYPE_NORMAL);

        tiles.add(tile1);

        MapTile tile2 = MapTilePool.get();
        tile2.setX(HOUSE_X);
        tile2.setY(HOUSE_Y+MapTile.titleW);
        tile2.setVisible(true);
        tile2.setType(MapTile.TYPE_NORMAL);

        tiles.add(tile2);

        MapTile tile3 = MapTilePool.get();
        tile3.setX(HOUSE_X+MapTile.titleW);
        tile3.setY(HOUSE_Y);
        tile3.setVisible(true);
        tile3.setType(MapTile.TYPE_NORMAL);

        tiles.add(tile3);

        MapTile tile4 = MapTilePool.get();
        tile4.setX(HOUSE_X+MapTile.titleW*2);
        tile4.setY(HOUSE_Y);
        tile4.setVisible(true);
        tile4.setType(MapTile.TYPE_NORMAL);

        tiles.add(tile4);

        MapTile tile5 = MapTilePool.get();
        tile5.setX(HOUSE_X+MapTile.titleW*2);
        tile5.setY(HOUSE_Y+MapTile.titleW);
        tile5.setVisible(true);
        tile5.setType(MapTile.TYPE_NORMAL);

        tiles.add(tile5);

        MapTile tile6 = MapTilePool.get();
        tile6.setVisible(true);

        tile6.setX(HOUSE_X+MapTile.titleW);
        tile6.setY(HOUSE_Y+MapTile.titleW);
        tile6.setType(MapTile.TYPE_HOUSE);

        tiles.add(tile6);


//        tiles.get(tiles.size()-1).setType(MapTile.TYPE_HOUSE);
    }
    public void draw(Graphics g){
        for (MapTile tile : tiles) {
            tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }
        public void tilesReturn(){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            MapTilePool.theReturn(tile);
        }
        tiles.clear();
    }
}
