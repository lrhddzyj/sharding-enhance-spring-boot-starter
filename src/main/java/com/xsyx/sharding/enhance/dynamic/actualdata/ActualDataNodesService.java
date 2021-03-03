/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.dynamic.actualdata;

/**
 * 节点刷新接口
 *
 * @author lirh
 * @date 2021/02/25 9:14
 */
@FunctionalInterface
public interface ActualDataNodesService {

    /**
     * 刷新逻辑表的动态实例
     *
     */
    void refresh();
}
