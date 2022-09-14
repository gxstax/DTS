package com.heytea.dtc.spi.serviceloader;

/**
 * <p>
 * 类加载工具
 * </p>
 *
 * @author Ant
 * @since 2021/12/30 10:22 下午
 */
@Deprecated
public class CanalServiceLoader {

    /**
     * 使用SPI机制加载所有的Class
     */
    public static <S> java.util.ServiceLoader<S> loadAll(final Class<S> clazz) {
        return java.util.ServiceLoader.load(clazz);
    }

}
