package cn.qxl.aspect;

import cn.qxl.utils.DynamicDataSource;
import cn.qxl.annotation.DataSource;
import cn.qxl.constants.DataSourceNames;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类 处理带有注解的方法类
 */
@Component
@Aspect
public class DataSourceAspect implements Ordered {
 
	protected Logger logger = LoggerFactory.getLogger(getClass());
 
	@Pointcut("@annotation(cn.qxl.annotation.DataSource)")//注意：这里的xxxx代表的是上面public @interface DataSource这个注解DataSource的包名
	public void dataSourcePointCut() {
 
	}
 
	@Around("dataSourcePointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		DataSource ds = method.getAnnotation(DataSource.class);
		if (ds == null) {
			DynamicDataSource.setDataSource(DataSourceNames.FIRST);
			logger.debug("set datasource is " + DataSourceNames.FIRST);
		} else {
			DynamicDataSource.setDataSource(ds.name());
			logger.debug("set datasource is " + ds.name());
		}
		try {
			return point.proceed();
		} finally {
			DynamicDataSource.clearDataSource();
			logger.debug("clean datasource");
		}
	}
 
	@Override
	public int getOrder() {
		return 1;
	}
}