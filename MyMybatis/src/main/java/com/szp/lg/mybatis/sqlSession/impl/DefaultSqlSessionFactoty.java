package com.szp.lg.mybatis.sqlSession.impl;

import com.szp.lg.mybatis.pojo.MybatisConfiguration;
import com.szp.lg.mybatis.sqlSession.SqlSession;
import com.szp.lg.mybatis.sqlSession.SqlSessionFactory;

public class DefaultSqlSessionFactoty implements SqlSessionFactory {
    private MybatisConfiguration mybatisConfiguration;

    public DefaultSqlSessionFactoty(MybatisConfiguration configuration) {
        this.mybatisConfiguration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return null;
    }
}
