package com.szp.lg.mybatis.plugin;

/*import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;
@Intercepts({
        @Signature(type = Executor.class,
                method = "query",
                args ={MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class})
})*/
public class MyPlugin /*implements Interceptor*/ {
    /*public Object intercept(Invocation invocation) throws Throwable {
        //在这里对方法进行增强
        //do my business
        return invocation.proceed();
    }
    //为该拦截器生成代理并放到拦截器链上
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }*/
}