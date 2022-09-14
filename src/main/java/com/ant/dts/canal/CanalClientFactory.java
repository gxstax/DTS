package com.ant.dts.canal;

import com.ant.dts.canal.clients.CanalClient;
import com.ant.dts.canal.clients.CanalServerClusterCanalClient;
import com.ant.dts.canal.clients.ClusterCanalClient;
import com.ant.dts.canal.clients.SimpleCanalClient;
import com.ant.dts.constant.enums.ClientModeEnum;
import com.ant.dts.util.SpringContextUtil;

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
