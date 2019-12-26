package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {
    private SqlSession sqlSession;

    @Before
    public void before() throws PropertyVetoException, DocumentException {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        sqlSession = sqlSessionFactory.openSession();
    }

    @After
    public void after(){
//        sqlSession.close();
    }

    @Test
    public void test() throws Exception {
        //调用
        User user = new User();
        user.setId(1);
//        user.setUsername("张三");
      /*  User user2 = sqlSession.selectOne("user.selectOne", user);

        System.out.println(user2);*/

       /* List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        List<User> all = userDao.findAll();
        for (User user1 : all) {
            System.out.println(user1);
        }


    }

    @Test
    public void testInsert() throws Exception {
        //调用
        User user = new User();
        user.setId(2);
        user.setUsername("lisi");

        int result = sqlSession.insert("com.lagou.dao.IUserDao.insert", user);

//        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

//        int result = userDao.insert(user);

    }

    @Test
    public void testUpdate() throws Exception {
        //调用
        User user = new User();
        user.setId(2);
        user.setUsername("ergouzi");

        int result = sqlSession.insert("com.lagou.dao.IUserDao.update", user);

//        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

//        int result = userDao.update(user);

    }

    @Test
    public void testDelete() throws Exception {
        //调用
        User user = new User();
        user.setId(2);

        int result = sqlSession.insert("com.lagou.dao.IUserDao.delete", user);

//        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

//        int result = userDao.delete(user);

    }
}
