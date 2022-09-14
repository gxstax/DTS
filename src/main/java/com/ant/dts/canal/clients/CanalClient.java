package com.ant.dts.canal.clients;

import com.alibaba.otter.canal.protocol.Message;
import com.ant.dts.config.properties.CanalProperties;

import java.util.function.Consumer;

/**
 * <p>
 * canal 客户端
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 5:55 下午
 */
public interface CanalClient extends LifeCycle {

    /** 直连ip canal客户端 BeanName **/
    public static final String SIMPLE_CANAL_CLIENT_BEAN_NAME = "simpleCanalClient";

    /** 基于zookeeper获取canal server ip 客户端 BeanName **/
    public static final String CLUSTER_CANAL_CLIENT_BEAN_NAME = "clusterCanalClient";

    /** 基于固定canal server地址客户端 BeanName **/
    public static final String CANAL_SERVER_CLUSTER_CANAL_CLIENT_BEAN_NAME = "canalServerClusterCanalClient";

    /**
     * <p>
     * 新建客户端链接
     * </p>
     *
     * @param destination
     * @return void
     */
    default void newConnector(CanalProperties.Destination destination,
                              Consumer<Message> consumer) {
    }

    /**
     * <p>
     * 获取 client 名称
     * （因为canal 每个 destination 只允许一个客户端消费，
     *   所以客户端名称直接取 destination 名称即可 ）
     * </p>
     *
     * @param
     * @return clientName
     */
    String getClientName();

}
