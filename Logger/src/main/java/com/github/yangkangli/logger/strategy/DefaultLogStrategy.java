package com.github.yangkangli.logger.strategy;

import com.github.yangkangli.logger.core.BaseLogStrategy;

/**
 * 默认日志输出策略
 */
public class DefaultLogStrategy extends BaseLogStrategy {


    /**
     * 构造方法
     *
     * @param builder
     */
    protected DefaultLogStrategy(Builder builder) {
        super(builder);
    }

    /**
     * 默认日志输出策略的构造器
     */
    public static class Builder extends BaseLogStrategy.Builder<DefaultLogStrategy> {


        @Override
        public DefaultLogStrategy build() {
            return new DefaultLogStrategy(this);
        }
    }
}
