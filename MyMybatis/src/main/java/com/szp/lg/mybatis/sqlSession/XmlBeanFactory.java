package com.szp.lg.mybatis.sqlSession;

import com.szp.lg.mybatis.pojo.*;
import org.apache.commons.digester3.Digester;

import java.io.InputStream;
import java.util.List;

public class XmlBeanFactory {

	public XmlBeanFactory(){
	}

	public void load() throws Exception {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("beans", Beans.class);

		// 如果配置文件中有多个bean，add一次即可
		digester.addObjectCreate("beans/bean", Bean.class);

		// 设置bean的属性<bean name="",id="">中的id和name。默认属性名和类中的属性名一样，不同的要特殊配置
		digester.addSetProperties("beans/bean", "class", "className");
		digester.addSetProperties("beans/bean", "init-method", "initMethodName");
		digester.addSetProperties("beans/bean", "destroy-method", "destroyMethodName");
		digester.addSetProperties("beans/bean");

		digester.addObjectCreate("beans/bean/property", Property.class);
		digester.addSetProperties("beans/bean/property");

		digester.addObjectCreate("beans/scan", Scan.class);
		digester.addSetProperties("beans/scan", "package", "packageName");

		digester.addObjectCreate("beans/aspect", Aspect.class);
		digester.addSetProperties("beans/aspect");

		digester.addObjectCreate("beans/aspect/before", Before.class);
		digester.addSetProperties("beans/aspect/before");

		// 设置对象间的关系
		digester.addSetNext("beans/bean/property", "addProperty");
		digester.addSetNext("beans/aspect/before", "addAdvice");
		digester.addSetNext("beans/bean", "addBean");
		digester.addSetNext("beans/scan", "setScan");
		digester.addSetNext("beans/aspect", "addAspect");
		

		InputStream in = ClassLoader.getSystemResourceAsStream("beans.xml");
		Beans beans = (Beans) digester.parse(in);
		List<Bean> beanList = beans.getBeans();
		for (Bean bean : beanList) {
			System.out.println("bean =================================================>");
			System.out.println("    id ==> " + bean.getId());
			List<Property> props = bean.getProps();
			for (Property prop : props) {
				System.out.println("    property =================================================>");
				System.out.println("        name ==> " + prop.getName());
				System.out.println("        ref ==> " + prop.getRef());
				System.out.println("        value ==> " + prop.getValue());
			}

			Class clazz = Class.forName(bean.getClassName());
		}

		Scan scan = beans.getScan();
		if (null != scan) {
			System.out.println("scan =================================================>");
			System.out.println("    package name ==> " + scan.getPackageName());
		}
		
		List<Aspect> aspects = beans.getAspects();
		for(Aspect aspect : aspects){
			System.out.println("aspect =================================================>");
			System.out.println("    aspect ref ==> " + aspect.getRef());
			for(Advice advice : aspect.getAdvices()){
				System.out.println("        advice method ==> " + advice.getMethod());
			}
		}
	}
	

}
