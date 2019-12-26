package com.szp.lg.mybatis.pojo;

import java.util.ArrayList;
import java.util.List;

public class Mappers {
    private List<Mapper> mapperList = new ArrayList<>();

    public List<Mapper> getMapperList() {
        return mapperList;
    }

    public void setMapperList(List<Mapper> mapperList) {
        this.mapperList = mapperList;
    }

    @Override
    public String toString() {
        return "Mappers{" +
                "mapperList=" + mapperList +
                '}';
    }
}
