package com.ant.dts.canal.clients;

/**
 * @author lijunjun
 * @date 2021-10-31 15:52
 */
public interface LifeCycle {

    boolean start();

    boolean stop();

    boolean isRunning();

}
