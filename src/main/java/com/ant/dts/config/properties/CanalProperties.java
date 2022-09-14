package com.ant.dts.config.properties;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * Canal 配置信息
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 2:37 下午
 */
@Data
public class CanalProperties {

    /**
     * 消息投递方（kafka/rabbitmq/roketmq）
     */
    private String deliver;

    /**
     * 客户端运行模式（simple：直连ip, cluster:基于zookeeper, canal_server_cluster:基于固定canal server的地址）
     */
    private String mode;

    /**
     * mode=simple 模式下的地址信息
     */
    private CanalAddress address;

    /**
     * mode=canal_server_cluster 模式下的地址信息
     */
    private List<CanalAddress> addresses;

    /**
     * mode=cluster 模式下的zk服务地址信息
     */
    private String zkServers;

    /**
     * canal 实例列表
     */
    private List<Destination> destinations;

    /**
     * binlog dump 的数据库链接信息-用户名
     */
    private String username;

    /**
     * binlog dump 的数据库链接信息-密码
     */
    private String password;

    @Data
    public static class CanalAddress {
        private String serverIp;
        private Integer serverPort;
    }

    @Data
    public static class Destination {
        /**
         * 实例名称
         */
        private String name;

        /**
         * 服务承接方
         */
        private String deliver;

        /**
         * 实例过滤正则表达式
         */
        private String filterRegex;
    }

    public CanalProperties() {}

}
