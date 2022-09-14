package com.heytea.dtc.canal.clients;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.heytea.dtc.config.ApplicationConfig;
import com.heytea.dtc.config.properties.CanalProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Consumer;

/**
 * <p>
 * 直连ip客户端
 * （不支持server/client的failover机制）
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 3:59 下午
 */
@Component(CanalClient.SIMPLE_CANAL_CLIENT_BEAN_NAME)
public class SimpleCanalClient extends AbstractCanalClient {

    private final CanalProperties canalProperties;

    public SimpleCanalClient(ApplicationConfig applicationConfig) {
        this.canalProperties = applicationConfig.getCanalConfig();
    }

    @Override
    public void newConnector(CanalProperties.Destination destination, Consumer<Message> consumer) {
        // 地址信息
        final CanalProperties.CanalAddress address = canalProperties.getAddress();
        SocketAddress socketAddress = new InetSocketAddress(address.getServerIp(), address.getServerPort());
        // 用户名
        final String username = canalProperties.getUsername();
        // 密码
        final String password = canalProperties.getPassword();

        // 获取链接
        final CanalConnector connector = CanalConnectors.newSingleConnector(socketAddress, destination.getName(), username, password);
        // 初始化客户端属性信息
        initProperties(destination.getName(), destination.getFilterRegex(), connector, consumer);
    }

}
