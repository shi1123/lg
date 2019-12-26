package com.szp.lg.mybatis.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class MybatisConfiguration {
    private DataSource dataSource;

    private Map<String, Object> mappedStatementMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, Object> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, Object> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }

    @Override
    public String toString() {
        return "MybatisConfiguration{" +
                "dataSource=" + dataSource +
                ", mappedStatementMap=" + mappedStatementMap +
                '}';
    }
}
