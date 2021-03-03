/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.exception;

/**
 * 数据节点刷新异常
 *
 * @author lirh
 * @date 2021/02/25 9:17
 */
public class DataNodesRefreshException extends RuntimeException {

    /**
     * 逻辑表名
     */
    private String logicTableName;

    public DataNodesRefreshException(String message, String logicTableName) {
        super(message);
        this.logicTableName = logicTableName;
    }

    public DataNodesRefreshException(String message, Throwable cause, String logicTableName) {
        super(message, cause);
        this.logicTableName = logicTableName;
    }

    public String getLogicTableName() {
        return logicTableName;
    }
}
