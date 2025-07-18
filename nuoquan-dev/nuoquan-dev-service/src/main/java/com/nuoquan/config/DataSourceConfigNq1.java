package com.nuoquan.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan; //使用了通用mapper，不要用import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 主数据源配置
 * @Author: Jerrio
 * @Date: 2020.9.9
 */
@Configuration
@MapperScan(basePackages = "com.nuoquan.mapper.nq1", sqlSessionFactoryRef = "nq1SqlSessionFactory")
public class DataSourceConfigNq1 {

    // 将这个对象放入Spring容器中
    @Bean(name = "nq1DataSource")
    // 表示这个数据源是默认数据源
    @Primary
    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.nq1")
    public DataSource getDateSourceNq1()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "nq1SqlSessionFactory")
    @Primary
    // @Qualifier表示查找Spring容器中名字为ds1DataSource的对象
    public SqlSessionFactory nq1SqlSessionFactory(@Qualifier("nq1DataSource") DataSource datasource)
            throws Exception
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/nq1/*.xml"));
        return bean.getObject();
    }


    @Bean(name = "nq1TransactionManager")
    @Primary
    public DataSourceTransactionManager nq1TransactionManager(@Qualifier("nq1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("nq1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate nq1SqlSessionTemplate(
            @Qualifier("nq1SqlSessionFactory") SqlSessionFactory sessionFactory)
    {
        return new SqlSessionTemplate(sessionFactory);
    }
}