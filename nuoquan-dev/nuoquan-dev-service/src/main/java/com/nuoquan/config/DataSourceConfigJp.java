package com.nuoquan.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * Jupiter数据源配置
 * @Author: Jerrio
 * @Date: 2020.9.9
 */
@Configuration
@MapperScan(basePackages = "com.jupiter.mapper", sqlSessionFactoryRef = "jpSqlSessionFactory")
public class DataSourceConfigJp {

    // 将这个对象放入Spring容器中
    @Bean(name = "jpDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.jp")
    public DataSource getDateSourceJp()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jpSqlSessionFactory")
    public SqlSessionFactory jpSqlSessionFactory(@Qualifier("jpDataSource") DataSource datasource)
            throws Exception
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/jp/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "jpTransactionManager")
    public DataSourceTransactionManager jpTransactionManager(@Qualifier("jpDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("jpSqlSessionTemplate")
    public SqlSessionTemplate jpSqlSessionTemplate(
            @Qualifier("jpSqlSessionFactory") SqlSessionFactory sessionFactory)
    {
        return new SqlSessionTemplate(sessionFactory);
    }
}