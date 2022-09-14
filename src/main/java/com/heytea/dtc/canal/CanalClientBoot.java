package com.heytea.dtc.canal;

import com.heytea.dtc.canal.clients.CanalClient;
import com.heytea.dtc.config.ApplicationConfig;
import com.heytea.dtc.config.properties.CanalProperties;
import com.heytea.dtc.constant.enums.ClientModeEnum;
import com.heytea.dtc.deliver.Deliver;
import com.heytea.dtc.deliver.DeliverFactory;
import com.heytea.dtc.startboot.ApplicationLifeCycle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Canal 客户端启动
 * </p>
 *
 * @author Ant
 * @since 2022/1/1 5:17 下午
 */
@Slf4j
@Component
public class CanalClientBoot implements ApplicationLifeCycle {

    private final List<CanalClient> clients = new ArrayList<>();

    private final CanalProperties canalProperties;

    public CanalClientBoot(ApplicationConfig applicationConfig) {
        this.canalProperties = applicationConfig.getCanalConfig();
    }

    /**
     * <p>
     * 容器启动
     * </p>
     *
     * @return void
     */
    @Override
    public void refreshed() {
        // 启动canal客户端
        startCanalClients();
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
        // 销毁canal客户端
        destroyCanalClient();
    }


    /*
     * <p>
     * Canal 客户端启动
     * </p>
     *
     * @param
     * @return void
     */
    private void startCanalClients() {
        log.info("启动canal客户端.....");
        // 获取canal客户端链接模式
        ClientModeEnum mode = ClientModeEnum.getMode(canalProperties.getMode());
        if (StringUtils.isEmpty(mode)) {
            mode = ClientModeEnum.defaultMode();
        }

        // 创建客户端
        for (CanalProperties.Destination destination : canalProperties.getDestinations()) {
            final CanalClient canalClient = CanalClientFactory.getCanalClient(mode);

            // 获取消息投递实现方
            Deliver deliver = DeliverFactory.getDeliver(destination.getDeliver());

            if (Objects.nonNull(canalClient)) {
                // 获取客户端链接
                canalClient.newConnector(destination, msg -> deliver.deliver(msg));
                // 启动客户端
                final boolean started = canalClient.start();
                if (started) {
                    clients.add(canalClient);
                }
            }
        }
        log.info("canal客户端启动完成！！！");
    }

    /**
     * <p>
     * 销毁客户端
     * </p>
     *
     * @param
     * @return void
     */
    private void destroyCanalClient() {
        log.info("开始销毁Canal客户端.....");
        clients.stream().forEach(canalClient -> {
            final boolean stop = canalClient.stop();
            log.info("销毁-client[{}], 结果-{}", canalClient.getClientName(), stop);
        });
    }

}
