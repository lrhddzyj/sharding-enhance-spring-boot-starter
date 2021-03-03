/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.dynamic.datasource;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

/**
 * 动态分片数据源的获取
 *
 * @author lirh
 * @date 2021/02/26 14:07
 */
public class DefaultDynamicDatasourceService implements DynamicDatasourceService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public ShardingDataSource getShardingDataSource(){
        try {
            final DataSource primaryDs = applicationContext.getBean(DataSource.class);
            if (primaryDs instanceof ShardingDataSource) {
                return (ShardingDataSource) primaryDs;
            }
        } catch (NoUniqueBeanDefinitionException e) {
            final Map<String, DataSource> allDs = applicationContext.getBeansOfType(DataSource.class);
            final Optional<Map.Entry<String, DataSource>> firstSds =
                    allDs.entrySet().stream().filter(entry -> (entry.getValue() instanceof ShardingDataSource)).findFirst();
            if (firstSds.isPresent()) {
                final DataSource sds = firstSds.get().getValue();
                return (ShardingDataSource)sds;
            }
            throw new RuntimeException("未找到ShardingDataSource Bean");
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
