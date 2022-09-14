package com.heytea.dtc.canal.clients;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.Assert;

import java.util.function.Consumer;

/**
 * <p>
 * canal 客户端
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 6:03 下午
 */
@Slf4j
public abstract class AbstractCanalClient implements CanalClient {

    protected volatile boolean                running            = false;
    protected Thread.UncaughtExceptionHandler handler            = (t, e) -> log.error("parse events has an error",
            e);
    protected Thread                          thread             = null;
    protected CanalConnector connector;
    protected String                          destination;
    protected String filterRegex;

    /**
     * 承接 canal 消息的投递方
     */
    protected Consumer<Message> consumer;

    protected void initProperties(String destination, String filterRegex,
                                  CanalConnector connector, Consumer<Message> consumer){
        this.destination = destination;
        this.filterRegex = filterRegex;
        this.connector = connector;
        this.consumer = consumer;
    }

    /**
     * <p>
     * 客户端开启
     * </p>
     *
     * @param
     * @return boolean
     */
    public boolean start() {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(this::process);

        thread.setUncaughtExceptionHandler(handler);
        running = true;
        thread.start();
        log.info("canalClient-[{}]启动...", destination);
        return true;
    }

    /**
     * <p>
     * 客户端处理逻辑
     * </p>
     *
     * @param
     * @return void
     */
    protected void process() {
        int batchSize = 5;
        while (running) {
            try {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe(filterRegex);
                while (running) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    // 判断是否是空数据
                    if (batchId != -1 && size > 0) {
                        try {
                            // 消费数据
                            consumer.accept(message);
                        } catch (Exception e) {
                            connector.rollback();
                            continue;
                        }
                    }

                    connector.ack(batchId); // 提交确认
                }
            } catch (Throwable e) {
                log.error("process error!", e);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e1) {
                    // ignore
                }

                connector.rollback(); // 处理失败, 回滚数据
            } finally {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

    public boolean stop() {
        if (!running) {
            return false;
        }
        running = false;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }

        MDC.remove("destination");
        return true;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * <p>
     * 获取 client 名称
     * （因为canal 每个 destination 只允许一个客户端消费，
     * 所以客户端名称直接取 destination 名称即可 ）
     * </p>
     *
     * @return clientName
     */
    @Override
    public String getClientName() {
        return destination;
    }

}
