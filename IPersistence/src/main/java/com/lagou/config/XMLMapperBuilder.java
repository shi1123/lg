package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration =configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");

//        List<Element> list = rootElement.selectNodes("//select");
       Iterator<Element> iterator = rootElement.elementIterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String sqlType = element.getName().toUpperCase();
            System.out.println("sqlType: " + sqlType);
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setSqlType(sqlType);
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setSql(sqlText);
            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key,mappedStatement);

        }

    }


}