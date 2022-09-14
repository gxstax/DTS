package com.ant.dts.startboot;

/**
 * <p>
 * SpringApplication 生命周期感知事件
 * </p>
 *
 * @author Ant
 * @since 2022/4/14 8:21 下午
 */
public interface ApplicationLifeCycle {

    /**
     * <p>
     * Application 启动事件
     * </p>
     *
     * @param
     * @return void
     */
    default void start() {}

    /**
     * <p>
     * Application Refreshed 事件
     * </p>
     *
     * @param
     * @return void
     */
    default void refreshed() {}

    /**
     * <p>
     * Application 关闭事件
     * </p>
     *
     * @param
     * @return void
     */
    default void close() {}
}
