## 1、Mybatis动态sql是做什么的？都有哪些动态sql？简述一下动态sql的执行原理？

1. 动态sql主要是用于在处理复杂业务逻辑时，sql语句会根据输入参数的不同也就是业务的不同会进行动态变化。
2. 动态sql主要有：
* if
* choose(when.otherwise)
* foreach
* trim(where, set)
3. Executor在执行真正的sql前会进行sql语言的拼接
```
/**
 * 调用查询
 */
public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        //此处拼装生成BoundSql
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        CacheKey key = this.createCacheKey(ms, parameterObject, rowBounds, boundSql);
        //执行查询
        return this.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
}
```
```
public BoundSql getBoundSql(Object parameterObject) {
        //会根据不同的sqlSource类型执行不同的解析，
       //此处会调用DynamicSqlSource的解析方法，并返回解析好的BoundSql
        BoundSql boundSql = this.sqlSource.getBoundSql(parameterObject);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if(parameterMappings == null || parameterMappings.size() <= 0) {
            boundSql = new BoundSql(this.configuration, boundSql.getSql(), this.parameterMap.getParameterMappings(), parameterObject);
        }
        Iterator i$ = boundSql.getParameterMappings().iterator();

        while(i$.hasNext()) {
            ParameterMapping pm = (ParameterMapping)i$.next();
            String rmId = pm.getResultMapId();
            if(rmId != null) {
                ResultMap rm = this.configuration.getResultMap(rmId);
                if(rm != null) {
                    this.hasNestedResultMaps |= rm.hasNestedResultMaps();
                }
            }
        }

        return boundSql;
    }
```

```
        
/**
     * 解析SQL
     */
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(this.configuration, parameterObject);
        //会根据 rootSqlNode 中每个节点的内容，会调用 MixedSqlNode 的 apply 方法，解析合并SQL
        this.rootSqlNode.apply(context);
        //用 SqlSourceBuilder 将SQL中的 #{} 换成 ？
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
        Class<?> parameterType = parameterObject == null?Object.class:parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        Iterator i$ = context.getBindings().entrySet().iterator();

        while(i$.hasNext()) {
            Entry<String, Object> entry = (Entry)i$.next();
            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
        }
        return boundSql;
    }
```

```

public boolean apply(DynamicContext context) {
    Iterator i$ = this.contents.iterator();

    while(i$.hasNext()) {
       SqlNode sqlNode = (SqlNode)i$.next();
       //进行解析并拼装，此处调用下文的 动态sqlNode的Apply 方法
       sqlNode.apply(context);
    }

    return true;
}
```

## 2、Mybatis是否支持延迟加载？如果支持，它的实现原理是什么？
支持：可以通过标签`fetchType="lazy"`来开启单个mapper的支持或者通过在配置文件中增加以下代码来开启
```
<!-- 打开延迟加载的开关 -->  
<setting name="lazyLoadingEnabled" value="true" />  
<!-- 将积极加载改为消息加载即按需加载 -->  
<setting name="aggressiveLazyLoading" value="false"/> 
```
Mybatis的延迟加载就是分多次执行SQL语句，这样就实现了延迟加载的机制，并且第一次执行的结果值可能是接下来执行的SQL语句的参数值，Mybatis实现执行接下来的SQL的原理机制是通过代理类来实现的，就是第一次执行的结果对象其实已经是一个代理对象，当执行接下来相关的对象时会执行其他SQL语句，这样就实现了延迟加载的机制。


## 3、Mybatis都有哪些Executor执行器？它们之间的区别是什么？
主要有：

* BaseExecutor
* BatchExecutor 执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。
* CachingExecutor
* ClosedExecutor in ResultLoaderMap
* ReuseExecutor 执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map内，供下一次使用。简言之，就是重复使用Statement对象。
* SimpleExecutor 每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。

Mybatis配置文件中，可以指定默认的ExecutorType执行器类型，也可以手动给DefaultSqlSessionFactory的创建SqlSession的方法传递ExecutorType类型参数。


## 4、简述下Mybatis的一级、二级缓存（分别从存储结构、范围、失效场景。三个方面来作答）？

1. 一级缓存：
存储结构：HashMap（），作用范围sqlsession，失效场景：任何的 UPDATE, INSERT, DELETE 语句都会清空缓存，或者sqlsession主动调用了clearCache方法，在mapper.xml中的select标签中可以添加flushCache=“true”来强制刷新缓存。

2. 二级缓存
存储结构：默认实现的PerpetualCache中缓存的存储结构为HashMap，缓存的实体要实现序列化接口；作用范围：SqlSessionFactory的生命周期，所有由该SqlSessionFactory创建的SqlSession都共用一个缓存；失效场景：任意sqlSession进行了删除或更新操作。

## 5、简述Mybatis的插件运行原理，以及如何编写一个插件？
### 插件运行原理：
 MyBatis所允许拦截的方法如下:
·执行器Executor(update、 query、 commit、 rollback等方法);
SQL语法构StatementHandler建器(prepare、 parameterize、 batch、 Tpdate、 query等方
法);
·参数处理器ParameterHandler(getParameterObje、 setParameters方法);
·结果集处理器ResultSetHandler(handleResultSet、 handleOutputParameters等方法);
3. Mybatis插件原理
在四大对象创建的时候
1、每个创建出来的对象不是直接返回的,而是 interceptorChain. pluginAll(parameterHandler)
2、获取到所有的 Interceptor(拦截器(插件需要实现的接口);调用
 interceptor. plugin(target)返回 target包装后的对象
3、插件机制,我们可以使用插件为目标对象创建一个代理对象;AOP(面向切面)我们的插件可
以为四大对象创建出代理对象,代理对象就可以拦截到四大对象的每一个执行;
拦截
插件具体是如何拦截并附加额外的功能的呢?以 ParameterHandler来说
 
```
public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object object, BoundSql sql, InterceptorChain interceptorChain){ 
    ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement,object,sql); 
    parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler); 
    return parameterHandler; 
 } 
 public Object pluginAll(Object target) { 
    for (Interceptor interceptor : interceptors) {         target = interceptor.plugin(target); 
    } 
    return target; 
 }
```
interceptorChain保存了所有拦截器(interceptors)，是mybatis初始化的时候创建的。调用拦截器链中的拦截器一次对目标进行拦截或增强。interceptor.plugin(target)中的target可以理解为mybatis中的四大对象。返回的target是被重重代理后的对象。
### 实现自己的插件

1. 继承`org.apache.ibatis.plugin.Interceptor`，指明需要拦截的方法，方法的参数
```
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
                })
public class MyPlugin implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        //在这里对方法进行增强
        //do my business
        return invocation.proceed();
    }
    //为该拦截器生成代理并放到拦截器链上
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }
}
```
将我们自己的插件配置到sqlMapoConfig中。
```
<plugins>
    <plugin interceptor="com.szp.lg.mybatis.plugin.MyPlugin"/>
</plugins>
```

## 请完善自定义持久层框架IPersistence，在现有代码基础上添加、修改及删除功能。