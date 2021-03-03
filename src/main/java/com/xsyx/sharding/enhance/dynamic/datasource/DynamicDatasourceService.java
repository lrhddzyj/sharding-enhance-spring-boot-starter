/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.dynamic.datasource;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.BeansException;

/**
 * 获取分片数据源
 *
 * @author lirh
 * @date 2021/02/25 8:59
 */
public interface DynamicDatasourceService {

    /**
     * 获取分片数据源
     *
     * @return 分片数据源
     * @exception RuntimeException 未找到sharding-jdbc数据源的情况下
     */
    ShardingDataSource getShardingDataSource();

}
