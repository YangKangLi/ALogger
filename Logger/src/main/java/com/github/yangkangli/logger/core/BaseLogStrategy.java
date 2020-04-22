package com.github.yangkangli.logger.core;

public class BaseLogStrategy {

    /**
     * 默认的BaseTag
     */
    private static final String DEFAULT_BASE_TAG = "ALogger";

    /**
     * 默认的BaseTag与SubTag之间的连接字符串
     */
    private static final String DEFAULT_TAG_LINKER = "->";

    /**
     * 默认的边界线长度
     */
    private static final int DEFAULT_BORDER_LENGTH = 120;

    /**
     * 是否显示线程名称
     */
    private boolean showThreadName;

    /**
     * 是否显示调用堆栈
     */
    private boolean showStackTrace;

    /**
     * 显示调用堆栈的方法数
     */
    private int methodCount;

    /**
     * BaseTag
     */
    private String baseTag;

    /**
     * BaseTag 与 SubTAG 之间的连接字符串
     */
    private String linker;

    /**
     * BaseTag 与 SubTAG 之间的连接字符串的长度
     */
    private int linkerLength;

    /**
     * 获得边界线最大长度
     */
    private int borderMaxLength;

    /**
     * 构造方法
     *
     * @param builder
     */
    protected BaseLogStrategy(Builder builder) {
        this.showThreadName = builder.showThreadName;
        this.showStackTrace = builder.showStackTrace;
        this.methodCount = builder.methodCount;
        this.baseTag = builder.baseTag;
        this.linker = builder.linker;
        this.linkerLength = builder.linker.length();
        this.borderMaxLength = builder.borderMaxLength;
    }

    public boolean isShowThreadName() {
        return showThreadName;
    }

    public boolean isShowStackTrace() {
        return showStackTrace;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public String getBaseTag() {
        return baseTag;
    }

    public String getLinker() {
        return linker;
    }

    public int getLinkerLength() {
        return linkerLength;
    }

    public int getBorderMaxLength() {
        return borderMaxLength;
    }


    /**
     * 构造器，用于构造DefaultAdapter实例
     *
     * @author yangkangli
     * @date: 2020/4/22
     */
    public static abstract class Builder<T extends BaseLogStrategy> {
        /**
         * 是否显示线程名称
         */
        private boolean showThreadName = true;

        /**
         * 是否显示调用堆栈
         */
        private boolean showStackTrace = false;

        /**
         * 显示调用堆栈的方法数
         */
        private int methodCount = 5;

        /**
         * BaseTag
         */
        private String baseTag = DEFAULT_BASE_TAG;

        /**
         * BaseTag 与 SubTAG 之间的连接字符串
         */
        private String linker = DEFAULT_TAG_LINKER;

        /**
         * 边界线最大长度
         */
        private int borderMaxLength = DEFAULT_BORDER_LENGTH;

        /**
         * 设置是否显示线程名称
         *
         * @param show
         * @return
         */
        public Builder setShowThreadName(boolean show) {
            this.showThreadName = show;
            return this;
        }

        /**
         * 设置是否显示调用堆栈
         *
         * @param show
         * @return
         */
        public Builder setShowStackTrace(boolean show) {
            this.showStackTrace = show;
            return this;
        }

        /**
         * 设置显示调用堆栈的方法数
         *
         * @param count
         * @return
         */
        public Builder setMethodCount(int count) {
            this.methodCount = count;
            return this;
        }

        /**
         * 设置BaseTag
         *
         * @param baseTag
         * @return
         */
        public Builder setBaseTag(String baseTag) {
            this.baseTag = baseTag;
            return this;
        }

        /**
         * 设置 BaseTag 与 SubTAG 之间的连接字符串
         *
         * @param linker
         * @return
         */
        public Builder setLinker(String linker) {
            this.linker = linker;
            return this;
        }

        /**
         * 设置边界线最大长度
         *
         * @param length
         * @return
         */
        public Builder setBordermaxLength(int length) {
            this.borderMaxLength = length;
            return this;
        }

        /**
         * 构造DefaultAdapter
         *
         * @return
         */
        public abstract T build();
    }
}
