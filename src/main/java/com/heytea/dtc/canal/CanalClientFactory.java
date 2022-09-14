package com.heytea.dtc.canal;

import com.heytea.dtc.canal.clients.CanalClient;
import com.heytea.dtc.canal.clients.CanalServerClusterCanalClient;
import com.heytea.dtc.canal.clients.ClusterCanalClient;
import com.heytea.dtc.canal.clients.SimpleCanalClient;
import com.heytea.dtc.constant.enums.ClientModeEnum;
import com.heytea.dtc.util.SpringContextUtil;

/**
 * <p>
 * 获取 Canal 客户端工厂
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 3:50 下午
 */
public class CanalClientFactory {

    public static CanalClient getCanalClient(ClientModeEnum clientModeEnum) {
        CanalClient canalClient = null;

        if (clientModeEnum.equals(ClientModeEnum.SIMPLE)) {
            canalClient = (CanalClient) SpringContextUtil.getBean(SimpleCanalClient.class);
        }

        if (clientModeEnum.equals(ClientModeEnum.CLUSTER)) {
            canalClient = (CanalClient) SpringContextUtil.getBean(ClusterCanalClient.class);
        }

        if (clientModeEnum.equals(ClientModeEnum.CANAL_SERVER_CLUSTER)) {
            canalClient = (CanalClient) SpringContextUtil.getBean(CanalServerClusterCanalClient.class);
        }

        return canalClient;
    }

}
