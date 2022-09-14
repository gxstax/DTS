package com.heytea.dtc.exception;

/**
 * <p>
 * 异常
 * </p>
 *
 * @author Ant
 * @since 2022/1/6 10:51 下午
 */
public class DTCException extends RuntimeException{

    public DTCException(String message) {
        super(message);
    }

    public DTCException(String message, Throwable cause) {
        super(message, cause);
    }

    public DTCException(Throwable cause) {
        super(cause);
    }
}
