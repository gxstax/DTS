package com.heytea.dtc.monitor;

import com.heytea.starter.core.result.BaseResult;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author Ant
 * @since 2022/4/15 11:39 上午
 */
public interface AlarmReport {

    <F> BaseResult report(F t);

}
