package com.github.yangkangli.logger.core;

public interface ILogAdapter {
    /**
     * 用于确定是否应打印日志
     *
     * @return true：打印日志，否则不打印
     */
    boolean isLoggable();

    /**
     * 打印日志
     *
     * @param priority
     * @param subTag
     * @param message
     * @param strategy
     */
    void log(int priority, String subTag, String message, BaseLogStrategy strategy);
}
