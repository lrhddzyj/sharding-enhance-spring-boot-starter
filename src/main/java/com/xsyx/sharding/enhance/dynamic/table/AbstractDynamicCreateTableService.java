/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.dynamic.table;

import com.xsyx.sharding.enhance.dynamic.datasource.DynamicDatasourceService;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态创建表的抽象服务
 *
 * @author lirh
 * @date 2021/02/26 17:18
 */
public abstract class AbstractDynamicCreateTableService implements DynamicCreateTableService, ApplicationContextAware {

    protected DynamicCreateTableRepository dynamicCreateTableRepository;

    protected DynamicDatasourceService dynamicDatasourceService;

    protected ApplicationContext applicationContext;

    public AbstractDynamicCreateTableService(DynamicCreateTableRepository dynamicCreateTableRepository) {
        this.dynamicCreateTableRepository = dynamicCreateTableRepository;
    }

    @Override
    public void createActualTable() {
        final String tableSuffix = getTableSuffix();
        final ShardingDataSource shardingDataSource = dynamicDatasourceService.getShardingDataSource();
        final Map<String, DataSource> dataSourceMap = shardingDataSource.getDataSourceMap();
        dataSourceMap.entrySet().parallelStream().forEach(entry -> {
            final String datasource = StringUtils.replace(entry.getKey(),"-", "_");
            dynamicCreateTableRepository.create(datasource,tableSuffix);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        dynamicDatasourceService = applicationContext.getBean(DynamicDatasourceService.class);
    }
}
