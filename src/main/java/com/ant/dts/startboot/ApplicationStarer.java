package com.ant.dts.startboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author Ant
 * @since 2022/4/14 6:07 下午
 */
@Slf4j
@Component
public class ApplicationStarer implements ApplicationListener {

    private final List<ApplicationLifeCycle> applicationLifeCycleEvents;

    public ApplicationStarer(List<ApplicationLifeCycle> applicationLifeCycles) {
        this.applicationLifeCycleEvents = applicationLifeCycles;
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 容器启动事件
        if ((event instanceof ContextStartedEvent)) {
            applicationLifeCycleEvents.stream().forEach(lifeCycle -> {
                log.info(lifeCycle.getClass() + "启动.....");
                lifeCycle.start();
            });
        }

        // 容器 refreshed 事件
        if ((event instanceof ContextRefreshedEvent)) {
            applicationLifeCycleEvents.stream().forEach(lifeCycle -> {
                lifeCycle.refreshed();
            });
        }

        // 容器关闭事件
        if ((event instanceof ContextClosedEvent)) {
            applicationLifeCycleEvents.stream().forEach(lifeCycle -> {
                lifeCycle.close();
            });
        }

    }

}
