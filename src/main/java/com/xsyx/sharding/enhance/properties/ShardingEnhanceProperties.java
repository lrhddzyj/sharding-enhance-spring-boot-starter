/*
 * xsyx Inc. 湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2021. All Rights Reserved.
 */
package com.xsyx.sharding.enhance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * sharding-jdbc增加设置
 *
 * @author lirh
 * @date 2021/02/26 13:50
 */
@ConfigurationProperties(prefix = "sharding.enhance")
public class ShardingEnhanceProperties {

    /**
     * 是否开启增强
     */
    private boolean enable;

    @NestedConfigurationProperty
    private AutoCreate autoCreate;


    @NestedConfigurationProperty
    private AutoFresh autoFresh;


    public AutoCreate getAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(AutoCreate autoCreate) {
        this.autoCreate = autoCreate;
    }

    public AutoFresh getAutoFresh() {
        return autoFresh;
    }

    public void setAutoFresh(AutoFresh autoFresh) {
        this.autoFresh = autoFresh;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 自动创建
     */
    public static class AutoCreate{

        /**
         * 是否开启
         */
        private boolean enable = false;

        /**
         *  cron expression,
         */
        private String corn;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getCorn() {
            return corn;
        }

        public void setCorn(String corn) {
            this.corn = corn;
        }
    }

    /**
     * 自动刷新
     */
    public static class AutoFresh{

        /**
         * 是否开启
         */
        private boolean enable = false;

        /**
         *  cron expression,
         */
        private String corn;


        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getCorn() {
            return corn;
        }

        public void setCorn(String corn) {
            this.corn = corn;
        }
    }

}
