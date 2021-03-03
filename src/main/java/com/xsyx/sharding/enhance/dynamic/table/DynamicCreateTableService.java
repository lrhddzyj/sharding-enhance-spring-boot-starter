/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.dynamic.table;

/**
 * 动态创建表服务
 *
 * @author lirh
 * @date 2021/02/26 17:11
 */
public interface DynamicCreateTableService {

    /**
     * 创建分表实例
     *
     * em：logicTableName = test
     *     tableSuffix = _202001
     *  则创建的表名则为 test_202001
     *
     */
    void createActualTable();

    /**
     * 获取 实际表的后缀
     * @return
     */
    String getTableSuffix();
}
