package cn.qxl.config;

import cn.qxl.utils.DynamicDataSource;
import cn.qxl.constants.DataSourceNames;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置多数据源 
 */
@Configuration
public class DynamicDataSourceConfig {
 
    @Bean
    @ConfigurationProperties("spring.datasource.druid.first")
    public DataSource firstDataSource(){
        return DruidDataSourceBuilder.create().build();
    }
 
    @Bean
    @ConfigurationProperties("spring.datasource.druid.second")
    public DataSource secondDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.credit")
    public DataSource creditDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
//    @Primary
    public DynamicDataSource dataSource(DataSource firstDataSource, DataSource secondDataSource) {
        Map<String, DataSource> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceNames.FIRST, firstDataSource);
        targetDataSources.put(DataSourceNames.SECOND, secondDataSource);
        targetDataSources.put(DataSourceNames.CREDIT, creditDataSource());
        return new DynamicDataSource(firstDataSource, targetDataSources);
    }
}