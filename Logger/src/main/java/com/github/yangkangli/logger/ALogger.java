package com.github.yangkangli.logger;

import com.github.yangkangli.logger.core.BaseLogStrategy;
import com.github.yangkangli.logger.core.ILogAdapter;
import com.github.yangkangli.logger.core.LoggerCore;

public class ALogger {

    private LoggerCore loggerCore;


    /**
     * 单例对象持有类
     */
    private static class Holder {
        private static final ALogger INSTANCE = new ALogger();
    }


    /**
     * 得到单例对象
     *
     * @return
     */
    private static ALogger getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 私有构造方法
     */
    private ALogger() {
        loggerCore = new LoggerCore();
    }

    /******************************************************************************************************************
     * 对外提供的接口
     ******************************************************************************************************************/

    /**
     * 设置日志输出策略
     *
     * @param strategy
     */
    public static void setLogStrategy(BaseLogStrategy strategy) {
        getInstance().loggerCore.setStrategy(strategy);
    }

    /**
     * 增加日志适配器
     *
     * @param logAdapters
     */
    public static void addLogAdapter(ILogAdapter... logAdapters) {
        if (logAdapters != null && logAdapters.length > 0) {
            for (ILogAdapter adapter : logAdapters) {
                getInstance().loggerCore.addAdapter(adapter);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void v(Object message) {
        getInstance().loggerCore.v(null, message, null);
    }

    public static void v(String subTag, Object message) {
        getInstance().loggerCore.v(subTag, message, null);
    }

    public static void v(Throwable throwable) {
        getInstance().loggerCore.v(null, null, throwable);
    }

    public static void v(String subTag, Throwable throwable) {
        getInstance().loggerCore.v(subTag, null, throwable);
    }

    public static void v(String subTag, Object message, Throwable throwable) {
        getInstance().loggerCore.v(subTag, message, throwable);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void d(Object message) {
        getInstance().loggerCore.d(null, message, null);
    }

    public static void d(String subTag, Object message) {
        getInstance().loggerCore.d(subTag, message, null);
    }

    public static void d(Throwable throwable) {
        getInstance().loggerCore.d(null, null, throwable);
    }

    public static void d(String subTag, Throwable throwable) {
        getInstance().loggerCore.d(subTag, null, throwable);
    }

    public static void d(String subTag, Object message, Throwable throwable) {
        getInstance().loggerCore.d(subTag, message, throwable);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void i(Object message) {
        getInstance().loggerCore.i(null, message, null);
    }

    public static void i(String subTag, Object message) {
        getInstance().loggerCore.i(subTag, message, null);
    }

    public static void i(Throwable throwable) {
        getInstance().loggerCore.i(null, null, throwable);
    }

    public static void i(String subTag, Throwable throwable) {
        getInstance().loggerCore.i(subTag, null, throwable);
    }

    public static void i(String subTag, Object message, Throwable throwable) {
        getInstance().loggerCore.i(subTag, message, throwable);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void w(Object message) {
        getInstance().loggerCore.w(null, message, null);
    }

    public static void w(String subTag, Object message) {
        getInstance().loggerCore.w(subTag, message, null);
    }

    public static void w(Throwable throwable) {
        getInstance().loggerCore.w(null, null, throwable);
    }

    public static void w(String subTag, Throwable throwable) {
        getInstance().loggerCore.w(subTag, null, throwable);
    }

    public static void w(String subTag, Object message, Throwable throwable) {
        getInstance().loggerCore.w(subTag, message, throwable);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void e(Object message) {
        getInstance().loggerCore.e(null, message, null);
    }

    public static void e(String subTag, Object message) {
        getInstance().loggerCore.e(subTag, message, null);
    }

    public static void e(Throwable throwable) {
        getInstance().loggerCore.e(null, null, throwable);
    }

    public static void e(String subTag, Throwable throwable) {
        getInstance().loggerCore.e(subTag, null, throwable);
    }

    public static void e(String subTag, Object message, Throwable throwable) {
        getInstance().loggerCore.e(subTag, message, throwable);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void json(String json) {
        getInstance().loggerCore.json(null, null, json);
    }

    public static void json(String subTag, String json) {
        getInstance().loggerCore.json(subTag, null, json);
    }

    public static void jsonWithTitle(String title, String json) {
        getInstance().loggerCore.json(null, title, json);
    }

    public static void jsonWithTitle(String subTag, String title, String json) {
        getInstance().loggerCore.json(subTag, title, json);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void xml(String xml) {
        getInstance().loggerCore.xml(null, null, xml);
    }

    public static void xml(String subTag, String xml) {
        getInstance().loggerCore.xml(subTag, null, xml);
    }

    public static void xmlWithTitle(String title, String xml) {
        getInstance().loggerCore.xml(null, title, xml);
    }

    public static void xmlWithTitle(String subTag, String title, String xml) {
        getInstance().loggerCore.xml(subTag, title, xml);
    }
}
