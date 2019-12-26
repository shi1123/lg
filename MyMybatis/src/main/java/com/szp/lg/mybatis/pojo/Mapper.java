package com.szp.lg.mybatis.pojo;

public class Mapper {
    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Mapper{" +
                "resource='" + resource + '\'' +
                '}';
    }
}
