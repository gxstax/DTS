package com.heytea.dtc.canal.clients;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.heytea.dtc.config.ApplicationConfig;
import com.heytea.dtc.config.properties.CanalProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 基于固定canal server地址客户端
 * （支持固定的server ip的failover机制，不支持client的failover机制）
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 5:59 下午
 */
@Component(CanalClient.CANAL_SERVER_CLUSTER_CANAL_CLIENT_BEAN_NAME)
public class CanalServerClusterCanalClient extends AbstractCanalClient {

    private final CanalProperties canalProperties;

    public CanalServerClusterCanalClient(ApplicationConfig applicationConfig) {
        this.canalProperties = applicationConfig.getCanalConfig();
    }

    @Override
    public void newConnector(CanalProperties.Destination destination, Consumer<Message> consumer) {
        List<SocketAddress> addresses = new ArrayList<>();
        // 地址信息
        final List<CanalProperties.CanalAddress> canalAddresses = canalProperties.getAddresses();
        canalAddresses.stream().forEach(canalAddress -> {
            SocketAddress socketAddress =
                    new InetSocketAddress(canalAddress.getServerIp(), canalAddress.getServerPort());
            addresses.add(socketAddress);
        });
        // 用户名
        final String username = canalProperties.getUsername();
        // 密码
        final String password = canalProperties.getPassword();

        // 获取链接
        final CanalConnector connector = CanalConnectors.newClusterConnector(addresses, destination.getName(), username, password);
        // 初始化客户端链接信息
        initProperties(destination.getName(), destination.getFilterRegex(), connector, consumer);
    }

}
