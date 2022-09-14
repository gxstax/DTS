package com.ant.dts.watch.zookeeper;

import com.ant.dts.base.BaseResult;
import com.ant.dts.monitor.AlarmInfo;
import com.ant.dts.monitor.AlarmReport;
import com.ant.dts.startboot.ApplicationLifeCycle;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * <p>
 * Zookeeper Node 节点监听
 * </p>
 *
 * @author Ant
 * @since 2022/4/14 5:31 下午
 */
@Slf4j
@Component
public class ZookeeperNodeWatch implements ApplicationLifeCycle {

    private String CANAL_NODE_WATCH_PATH = "/otter/canal";
    private String KAFKA_NODE_WATCH_PATH = "/brokers";

    private CuratorFramework curatorFramework;
    private TreeCache canalTreeCache;
    private TreeCache kafkaTreeCache;


    @Value("${zk.servers}")
    private String zkServerUrl;

    private final AlarmReport alarmReport;

    public ZookeeperNodeWatch(AlarmReport alarmReport) {
        this.alarmReport = alarmReport;
    }


    @PostConstruct
    public void init() {
        // 构建监控框架
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(2, 10);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkServerUrl)
                .connectionTimeoutMs(10 * 1000)
                .sessionTimeoutMs(15 * 1000)
                .retryPolicy(retry)
                .build();
        // 创建监听器
        canalTreeCache = new TreeCache(curatorFramework, CANAL_NODE_WATCH_PATH);
        kafkaTreeCache = new TreeCache(curatorFramework, KAFKA_NODE_WATCH_PATH);

        log.info("Zookeeper监听初始化完成！");
    }

    /**
     * <p>
     * Application 启动事件
     * </p>
     *
     * @return void
     */
    @Override
    public void refreshed() {
        // Zookeeper监听框架启动
        curatorFramework.start();

        try {
            canalWatch();
        } catch (Exception e) {
            log.info("[Zookeeper监听]-异常", e);
        }
    }

    public void canalWatch() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(CANAL_NODE_WATCH_PATH);
        if(stat == null) {
            return;
        }
        canalTreeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
                if (Objects.equals(treeCacheEvent.getType(), TreeCacheEvent.Type.NODE_ADDED) ||
                    Objects.equals(treeCacheEvent.getType(), TreeCacheEvent.Type.NODE_UPDATED) ||
                    Objects.equals(treeCacheEvent.getType(), TreeCacheEvent.Type.NODE_REMOVED)) {

                    String nodeName = treeCacheEvent.getData().getPath().replace(CANAL_NODE_WATCH_PATH + "/", "");
                    final TreeCacheEvent.Type type = treeCacheEvent.getType();
                    log.info("[zookeeper监听]-node节点变动：节点[{}], 变动为[{}]状态", nodeName, type);
                    // 监听信息上报
                    AlarmInfo alarmInfo = AlarmInfo.buildReport(nodeName, type);
                    final BaseResult reportRst = alarmReport.report(alarmInfo);
                    log.info("[警告上报]-result[{}]", reportRst);
                }
            }
        });

        canalTreeCache.start();
    }

    /**
     * <p>
     * Application 关闭事件
     * </p>
     *
     * @return void
     */
    @Override
    public void close() {
        if (null != curatorFramework) {
            curatorFramework.close();
            log.info("curatorFramework 关闭！！");
        }
    }
}
