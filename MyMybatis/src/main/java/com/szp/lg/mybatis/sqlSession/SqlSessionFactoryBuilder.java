package com.szp.lg.mybatis.sqlSession;


import com.szp.lg.mybatis.pojo.*;
import com.szp.lg.mybatis.sqlSession.impl.DefaultSqlSessionFactoty;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {
//    Logger

    public static SqlSessionFactory build(String path) throws IOException, SAXException {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("configuration", MybatisConfiguration.class);

        // 如果配置文件中有多个bean，add一次即可
        digester.addObjectCreate("configuration/dataSource", DataSource.class);

        // 设置bean的属性<bean name="",id="">中的id和name。默认属性名和类中的属性名一样，不同的要特殊配置
        digester.addSetProperties("configuration/dataSource", "driverClass", "driverClass");
        digester.addSetProperties("configuration/dataSource", "jdbcUrl", "jdbcUrl");
        digester.addSetProperties("configuration/dataSource", "username", "username");
        digester.addSetProperties("configuration/dataSource", "password", "password");

        digester.addObjectCreate("configuration/dataSource/property", Property.class);
        digester.addSetProperties("configuration/dataSource/property", "name", "name");
        digester.addSetProperties("configuration/dataSource/property", "value", "value");

        digester.addObjectCreate("configuration/mappers", Mappers.class);
        digester.addObjectCreate("configuration/mappers/mapper", Mapper.class);

        digester.addSetProperties("configuration/mappers/mapper", "resource", "resource");

        InputStream in = ClassLoader.getSystemResourceAsStream("sqlMapConfig.xml");
        MybatisConfiguration configuration = (MybatisConfiguration) digester.parse(in);
        System.out.println(configuration.toString());
//        DataSource dataSource = configuration.getDataSource();
//
//        for (Bean bean : beanList) {
       return new DefaultSqlSessionFactoty(configuration);
    }
}
