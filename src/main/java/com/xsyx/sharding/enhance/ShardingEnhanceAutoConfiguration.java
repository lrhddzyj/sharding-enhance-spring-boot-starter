/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance;

import com.xsyx.sharding.enhance.dynamic.actualdata.ActualDataNodesService;
import com.xsyx.sharding.enhance.dynamic.datasource.DefaultDynamicDatasourceService;
import com.xsyx.sharding.enhance.dynamic.datasource.DynamicDatasourceService;
import com.xsyx.sharding.enhance.dynamic.table.DynamicCreateTableService;
import com.xsyx.sharding.enhance.properties.ShardingEnhanceProperties;
import com.xsyx.sharding.enhance.task.CreateTableTask;
import com.xsyx.sharding.enhance.task.RefreshTableNodesTask;
import java.util.List;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.CollectionUtils;

/**
 * @author lirh
 * @date 2021/02/25 19:36
 */
@Configuration
@ConditionalOnClass(ShardingDataSource.class)
@ConditionalOnProperty(prefix = "sharding.enhance", name = "enable")
@EnableConfigurationProperties(value = ShardingEnhanceProperties.class)
public class ShardingEnhanceAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ShardingEnhanceAutoConfiguration.class);

    @ConditionalOnMissingBean
    @Bean
    public DynamicDatasourceService dynamicDatasourceService() {
        logger.info("dynamicDatasourceService init");
        return new DefaultDynamicDatasourceService();
    }

    @ConditionalOnProperty(prefix = "sharding.enhance.auto-create", name = {"enable", "corn"})
    @Bean
    public ThreadPoolTaskScheduler createTableScheduler(ShardingEnhanceProperties shardingEnhanceProperties,
                                                        ObjectProvider<List<DynamicCreateTableService>> createTableServiceProvider) {
        final List<DynamicCreateTableService> dynamicCreateTableServices = createTableServiceProvider.getIfAvailable();
        if (CollectionUtils.isEmpty(dynamicCreateTableServices)) {
            throw new RuntimeException("没有找到 DynamicCreateTableService Bean");
        }
        ThreadPoolTaskScheduler createTableScheduler = new ThreadPoolTaskScheduler();
        createTableScheduler.setThreadNamePrefix("sharding-enhance-autoCreate-");
        createTableScheduler.initialize();
        createTableScheduler.schedule(new CreateTableTask(dynamicCreateTableServices) , new CronTrigger(shardingEnhanceProperties.getAutoCreate().getCorn()));
        return createTableScheduler;
    }

    @ConditionalOnProperty(prefix = "sharding.enhance.auto-fresh", name = {"enable", "corn"})
    @Bean
    public ThreadPoolTaskScheduler refreshTableNodesScheduler(ShardingEnhanceProperties shardingEnhanceProperties,
                                                              ObjectProvider<List<ActualDataNodesService>> dataNodesServiceObjectProvider) {
        final List<ActualDataNodesService> actualDataNodesServices = dataNodesServiceObjectProvider.getIfAvailable();
        if (CollectionUtils.isEmpty(actualDataNodesServices)) {
            throw new RuntimeException("没有找到 ActualDataNodesService bean");
        }
        ThreadPoolTaskScheduler refreshTableNodesScheduler = new ThreadPoolTaskScheduler();
        refreshTableNodesScheduler.setThreadNamePrefix("sharding-enhance-refreshNodes-");
        refreshTableNodesScheduler.initialize();
        refreshTableNodesScheduler.schedule(new RefreshTableNodesTask(actualDataNodesServices),
                new CronTrigger(shardingEnhanceProperties.getAutoFresh().getCorn()));
        return refreshTableNodesScheduler;
    }


}
