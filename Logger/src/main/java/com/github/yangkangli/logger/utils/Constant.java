package com.github.yangkangli.logger.utils;

public class Constant {

    public static final int JSON_INDENT = 2;

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    /**
     * 默认的时间格式模式
     */
    public static final String DEFAULT_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 顶部转角
     */
    public static final String TOP_CORNER = "┌";

    /**
     * 中部转角
     */
    public static final String MIDDLE_CORNER = "|-";

    /**
     * 底部转角
     */
    public static final String BOTTOM_CORNER = "└";

    /**
     * 竖线
     */
    public static final String HORIZONTAL_LINE = "|";

    /**
     * 分隔线（实线）
     */
    public static final String REAL_LINE_DIVIDER = "─";

    /**
     * 分隔线（虚线）
     */
    public static final String DASH_LINE_DIVIDER = "-";
}
