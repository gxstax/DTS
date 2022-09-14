package com.ant.dts.base;

/**
 * <p>
 * 基础结果
 * </p>
 *
 * @author Ant
 * @since 2022/9/14 9:22 下午
 */
public class BaseResult {

    private final static Integer SUCC_CODE = 0;
    private final static String SUCC_MESSAGE = "SUCCESS";

    private Integer code;
    private String message;


    public BaseResult() {
        this.code = SUCC_CODE;
        this.message = SUCC_MESSAGE;
    }

    public BaseResult(String messsage) {
        this.code = SUCC_CODE;
        this.message = messsage;
    }

    public BaseResult(Integer code, String messsage) {
        this.code = code;
        this.message = messsage;
    }

    public static BaseResult success(String message) {
        return new BaseResult(message);
    }

    public BaseResult message(String message) {
        this.message = message;
        return this;
    }

    public BaseResult code(Integer code) {
        this.code = code;
        return this;
    }


}
