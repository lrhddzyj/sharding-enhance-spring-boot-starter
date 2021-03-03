/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.dynamic.table;

/**
 * 动态创建表的资源接口
 *
 * @author lirh
 * @date 2021/02/26 17:18
 */
public interface DynamicCreateTableRepository {

    /**
     * 执行动态创建表
     *
     * @param tableSuffix
     */
    void create(String dataSourceName,String tableSuffix);
}
