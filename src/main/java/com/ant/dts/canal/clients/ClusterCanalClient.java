package com.ant.dts.canal.clients;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.ant.dts.config.ApplicationConfig;
import com.ant.dts.config.properties.CanalProperties;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * <p>
 * 基于zookeeper获取canal server ip 客户端
 * （支持server/client的failover机制）
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 3:58 下午
 */
@Component(CanalClient.CLUSTER_CANAL_CLIENT_BEAN_NAME)
public class ClusterCanalClient extends AbstractCanalClient {

    private final CanalProperties canalProperties;

    public ClusterCanalClient(ApplicationConfig applicationConfig) {
        this.canalProperties = applicationConfig.getCanalConfig();
    }

    @Override
    public void newConnector(CanalProperties.Destination destination, Consumer<Message> consumer) {
        final String zkServers = canalProperties.getZkServers();
        // 用户名
        final String username = canalProperties.getUsername();
        // 密码
        final String password = canalProperties.getPassword();

        // 获取链接
        final CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, destination.getName(), username, password);
        // 初始化客户端信息
        initProperties(destination.getName(), destination.getFilterRegex(), connector, consumer);
    }

}
