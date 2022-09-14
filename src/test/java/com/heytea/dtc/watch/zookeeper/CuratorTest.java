package com.heytea.dtc.watch.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Objects;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author Ant
 * @since 2022/4/14 2:17 下午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CuratorTest {

    private CuratorFramework curatorFramework;

    public void init() {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(2, 10);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("ant_host:2181,ant_host:2182,ant_host:2183")
                .connectionTimeoutMs(10 * 1000)
                .sessionTimeoutMs(15 * 1000)
                .retryPolicy(retry)
                .build();
    }

    public void start() {
        curatorFramework.start();
    }

    public void close() {
        if (null != curatorFramework) {
            curatorFramework.close();
        }
    }

    /**
     * <p>
     * 监听某一节点变化
     * </p>
     *
     * @param
     * @return void
     */
    public void watchNodeChache() throws Exception {
        // 监听一个节点
        NodeCache nodeCache = new NodeCache(curatorFramework, "/otter/canal");
        // 注册监听
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点变化了");
            }
        });

        // 开启监听 param:true 开启监听时加载缓存数据
        nodeCache.start(true);
        while (true);
    }


    public void watchTreeCache() {
        // 1.创建监听器
        TreeCache treeCache = new TreeCache(curatorFramework, "/otter/canal");

//        Stat stat = curatorFramework.checkExists().forPath(CANAL_NODE_WATCH_PATH);
//        if(stat == null) {
//            return;
//        }
        // 2. 注册监听
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                if (Objects.equals(treeCacheEvent.getType(), TreeCacheEvent.Type.NODE_ADDED) ||
                    Objects.equals(treeCacheEvent.getType(), TreeCacheEvent.Type.NODE_UPDATED) ||
                    Objects.equals(treeCacheEvent.getType(), TreeCacheEvent.Type.NODE_REMOVED)) {

                    String nodeName = treeCacheEvent.getData().getPath().replace("/otter/canal" + "/", "");
                    System.out.println("[" + nodeName + "]节点变动，变动事件-->" + treeCacheEvent.getType());
                }
            }
        });

        // 3.开启监听
        try {
            treeCache.start();
            while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void watchNodeChacheRun() throws Exception {
        init();
        start();
        // Node节点监听方式
        watchNodeChache();
        close();
    }

    public void watchTreeCacheRun() {
        init();
        start();
        // Node节点监听方式
        watchTreeCache();
        close();
    }

    public static void main(String[] args) throws Exception {
        CuratorTest curatorTest = new CuratorTest();
        curatorTest.watchTreeCacheRun();
    }

}
