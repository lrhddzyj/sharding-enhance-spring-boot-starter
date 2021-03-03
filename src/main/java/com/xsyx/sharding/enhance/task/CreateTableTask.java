/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.task;

import com.xsyx.sharding.enhance.dynamic.table.DynamicCreateTableService;

import java.util.List;

/**
 * 创建表任务
 *
 * @author lirh
 * @date 2021/02/26 15:13
 */
public class CreateTableTask implements Runnable{

    private List<DynamicCreateTableService> dynamicCreateTableServices;

    public CreateTableTask(List<DynamicCreateTableService> dynamicCreateTableServices) {
        this.dynamicCreateTableServices = dynamicCreateTableServices;
    }

    @Override
    public void run() {
        dynamicCreateTableServices.forEach(DynamicCreateTableService::createActualTable);
    }
}
