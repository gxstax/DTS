package com.ant.dts.constant.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * <p>
 * Canal 客户端模式
 * </p>
 *
 * @author Ant
 * @since 2022/1/1 2:49 下午
 */
@Getter
public enum ClientModeEnum {

    /** 直连ip **/
    SIMPLE("simple"),

    /** 基于zookeeper **/
    CLUSTER("cluster"),

    /** 基于固定canal server的地址 **/
    CANAL_SERVER_CLUSTER("canal_server_cluster")

    ;

    private String desc;

    ClientModeEnum(String desc) {
        this.desc = desc;
    }

    public static ClientModeEnum defaultMode() {
        return ClientModeEnum.SIMPLE;
    }

    public static ClientModeEnum getMode(String mode) {
        return Arrays.stream(ClientModeEnum.values())
                .filter(clientModeEnum -> clientModeEnum.getDesc().equals(mode)).findAny()
                .orElse(ClientModeEnum.SIMPLE);
    }

}
