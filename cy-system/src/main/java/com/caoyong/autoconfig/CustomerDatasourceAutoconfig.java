package com.caoyong.autoconfig;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@ConditionalOnClass({ CustomerDatasourcePropertiesBean.class})
@EnableConfigurationProperties(CustomerDatasourcePropertiesBean.class)
@CustomerMapperScanner(basePackages = {"${autoconfiguration.packagePath}"}, sqlSessionFactoryRef = "businessSqlSessionFactory")
/**
 * @author caoyong
 * 2017年10月24日 下午2:52:47
 */
public class CustomerDatasourceAutoconfig {
	@Primary
    @Bean(name = "businessDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.business")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "businessTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("businessDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "businessSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("businessDataSource") DataSource dataSource,CustomerDatasourcePropertiesBean customerDatasourcePropertiesBean) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage(customerDatasourcePropertiesBean.getAliasesPackage());
        try {
        	factoryBean.setMapperLocations(resolver.getResources(customerDatasourcePropertiesBean.getResourcesPath()));
            return factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Primary
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("businessSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
