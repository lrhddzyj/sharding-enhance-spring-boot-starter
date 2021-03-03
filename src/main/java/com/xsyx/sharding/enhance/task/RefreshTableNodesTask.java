/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.task;

import com.xsyx.sharding.enhance.dynamic.actualdata.ActualDataNodesService;

import java.util.List;

/**
 * 刷新实例的任务
 *
 * @author lirh
 * @date 2021/02/26 15:13
 */
public class RefreshTableNodesTask implements Runnable{

    private List<ActualDataNodesService> actualDataNodesServices;

    public RefreshTableNodesTask(List<ActualDataNodesService> actualDataNodesServices) {
        this.actualDataNodesServices = actualDataNodesServices;
    }

    @Override
    public void run() {
        actualDataNodesServices.forEach(ActualDataNodesService::refresh);
    }
}
