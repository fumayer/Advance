package com.shortmeet.www.entity.home;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fenglingyue on 2017/10/12.
 */

public class KindRecomanVideoEntity {
    // Fly 注：  Type 1 横向 1个         Type 2  横向2个
    private int type;
    private boolean isFull;
    public static final int HORIZANTAL_ONE = 1;
    public static final int VERTICAL_TWO = 2;
    private List<RecomandEntity.DataEntity.VideoEntity> mRecomandEntities=new ArrayList<>();



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public List<RecomandEntity.DataEntity.VideoEntity> getRecomanVideoEntities() {
        return mRecomandEntities;
    }

    /*
     *  Fly 注：向集合 mRecomandEntities里添加数据
     */
    public void  addEntity(RecomandEntity.DataEntity.VideoEntity entity){
        switch (entity.getItem_type()) {
            case 1: //横向 1个
                if(mRecomandEntities.size()==0){
                    mRecomandEntities.add(entity);
                }
                break;
            case 2://横向 2个
                mRecomandEntities.add(entity);
                if(mRecomandEntities.size()==2){
                    isFull=true;
                }
                break;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KindRecomanVideoEntity{");
        sb.append("type=").append(type);
        sb.append(", isFull=").append(isFull);
        sb.append(", mRecomandEntities=").append(mRecomandEntities);
        sb.append('}');
        return sb.toString();
    }
}
