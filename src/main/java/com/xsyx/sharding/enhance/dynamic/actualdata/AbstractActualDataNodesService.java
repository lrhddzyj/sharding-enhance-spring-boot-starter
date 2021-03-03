package com.xsyx.sharding.enhance.dynamic.actualdata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xsyx.sharding.enhance.dynamic.datasource.DynamicDatasourceService;
import com.xsyx.sharding.enhance.exception.DataNodesRefreshException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.core.rule.DataNode;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.core.rule.TableRule;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.context.ShardingRuntimeContext;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态实例刷新节点的抽象实现
 *
 * @author lirh
 * @date 2021/02/25 9:06
 */
public abstract class AbstractActualDataNodesService implements ActualDataNodesService, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(AbstractActualDataNodesService.class);

    protected DynamicDatasourceService dynamicDatasourceService;

    protected String logicTableName;

    protected ActualDataNodesRepository actualDataNodesRepository;

    protected ApplicationContext applicationContext;

    public AbstractActualDataNodesService(String logicTableName,
            ActualDataNodesRepository actualDataNodesRepository) {
        this.logicTableName = logicTableName;
        this.actualDataNodesRepository = actualDataNodesRepository;
    }

    @Override
    public void refresh() {
        final ShardingDataSource shardingDataSource = dynamicDatasourceService.getShardingDataSource();
        ShardingRuntimeContext runtimeContext = shardingDataSource.getRuntimeContext();
        ShardingRule shardingRule = runtimeContext.getRule();
        TableRule tableRule = shardingRule.getTableRule(logicTableName);
        final List<DataNode> actualDataNodes = tableRule.getActualDataNodes();
        logger.info("更新前的actualDataNodes大小{}", actualDataNodes.size());
        final Map<String, DataSource> dataSourceMap = shardingDataSource.getDataSourceMap();
        Map<String, List<DataNode>> datasourceToTablesMap = Maps.newHashMapWithExpectedSize(dataSourceMap.size());
        dataSourceMap.entrySet().parallelStream().forEach(entry -> {
            final String dataSourceName = StringUtils.replace(entry.getKey(),"-", "_");
            Set<DataNode> dataNodes = this.getAllNodes(dataSourceName);
            if (CollectionUtils.isEmpty(dataNodes)) {
                logger.warn(String.format("datasource:[%s]未找到逻辑表 %s 的表实例数据", dataSourceName, logicTableName));
            }
            datasourceToTablesMap.put(dataSourceName, Lists.newArrayList(dataNodes));
        });
        try {
            this.doRefresh(tableRule,datasourceToTablesMap);
        } catch (Exception e) {
            throw new DataNodesRefreshException("刷新实例异常", logicTableName);
        }

    }

    /**
     * 执行动态刷新
     */
    protected void doRefresh(TableRule tableRule, Map<String, List<DataNode>> datasourceToTablesMap) {
        Set<String> actualTables = Sets.newHashSet();
        List<DataNode> allDataNodes = Lists.newArrayList();
        datasourceToTablesMap.forEach((k,v) ->{
            allDataNodes.addAll(v);
        });

        Map<DataNode, Integer> dataNodeIndexMap = Maps.newHashMap();
        AtomicInteger index = new AtomicInteger(0);
        allDataNodes.forEach(dataNode -> {
            actualTables.add(dataNode.getTableName());
            if (index.intValue() == 0) {
                dataNodeIndexMap.put(dataNode, 0);
            } else {
                dataNodeIndexMap.put(dataNode, index.intValue());
            }
            index.incrementAndGet();
        });

        Field actualDataNodes = ReflectionUtils.findField(TableRule.class, "actualDataNodes");
        ReflectionUtils.makeAccessible(actualDataNodes);
        ReflectionUtils.setField(actualDataNodes, tableRule, allDataNodes);

        Field actualTablesField = ReflectionUtils.findField(TableRule.class, "actualTables");
        ReflectionUtils.makeAccessible(actualTablesField);
        ReflectionUtils.setField(actualTablesField, tableRule, actualTables);

        Field dataNodeIndexMapField = ReflectionUtils.findField(TableRule.class, "dataNodeIndexMap");
        ReflectionUtils.makeAccessible(dataNodeIndexMapField);
        ReflectionUtils.setField(dataNodeIndexMapField, tableRule, dataNodeIndexMap);

        Field datasourceToTablesMapField = ReflectionUtils.findField(TableRule.class, "datasourceToTablesMap");
        ReflectionUtils.makeAccessible(datasourceToTablesMapField);
        ReflectionUtils.setField(datasourceToTablesMapField, tableRule, datasourceToTablesMap);
    }


    /**
     * 动态获取节点数据源
     *
     * @param dataSourceName
     * @return
     */
    protected Set<DataNode> getAllNodes(final String dataSourceName) {
        Set<DataNode> dataNodes = Sets.newHashSet();
        List<String> tableNames = actualDataNodesRepository.getActualTables(dataSourceName);

        for (String tableName : tableNames) {
            final String dataNodeInfo = new StringBuilder().append(dataSourceName).append(".").append(tableName).toString();
            DataNode dataNode = new DataNode(dataNodeInfo);
            dataNodes.add(dataNode);
        }
        return dataNodes;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        this.applicationContext = applicationContext;
        dynamicDatasourceService = applicationContext.getBean(DynamicDatasourceService.class);
    }


}
