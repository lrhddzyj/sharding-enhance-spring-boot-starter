/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.dynamic.actualdata;

import java.util.List;

/**
 * 分表实例资源接口
 *
 * @author lirh
 * @date 2021/02/25 9:06
 */
public interface ActualDataNodesRepository {

    /**
     * 获取数据库中的某个表的实例
     *
     * @return
     */
    List<String> getActualTables(String dataSourceName);

}
