package com.github.yangkangli.logger.core;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.github.yangkangli.logger.utils.Constant;
import com.github.yangkangli.logger.strategy.DefaultLogStrategy;
import com.github.yangkangli.logger.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class LoggerCore {

    /**
     * 日志适配器列表
     */
    private final List<ILogAdapter> adapterList = new ArrayList<>();

    /**
     * 日志输出策略
     */
    private BaseLogStrategy logStrategy;

    /**
     * 构造方法
     */
    public LoggerCore() {
        // 构造默认的日志输出策略
        this.logStrategy = new DefaultLogStrategy.Builder().build();
    }

    /**
     * 设置日志输出策略
     *
     * @param strategy
     */
    public void setStrategy(BaseLogStrategy strategy) {
        Utils.checkNotNull(strategy);
        this.logStrategy = strategy;
    }

    /**
     * 网日志适配器列表中加入一个日志适配器
     *
     * @param adapter
     */
    public void addAdapter(ILogAdapter adapter) {
        adapterList.add(Utils.checkNotNull(adapter));
    }

    /**
     * 清除日志适配器列表
     */
    public void clearAdapter() {
        adapterList.clear();
    }


    /**
     * 打印日志
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public void v(String tag, Object message, Throwable throwable) {
        log(Constant.VERBOSE, tag, Utils.toString(message), throwable);
    }


    /**
     * 打印日志
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public void d(String tag, Object message, Throwable throwable) {
        log(Constant.DEBUG, tag, Utils.toString(message), throwable);
    }


    /**
     * 打印日志
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public void i(String tag, Object message, Throwable throwable) {
        log(Constant.INFO, tag, Utils.toString(message), throwable);
    }


    /**
     * 打印日志
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public void w(String tag, Object message, Throwable throwable) {
        log(Constant.WARN, tag, Utils.toString(message), throwable);
    }


    /**
     * 打印日志
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public void e(String tag, Object message, Throwable throwable) {
        log(Constant.ERROR, tag, Utils.toString(message), throwable);
    }


    /**
     * 打印日志（JSON）
     *
     * @param subTag
     * @param title
     * @param json
     */
    public void json(String subTag, String title, String json) {
        try {
            String jsonStr = json.trim();
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String message = jsonObject.toString(Constant.JSON_INDENT);
                if (TextUtils.isEmpty(title)) {
                    d(subTag, message, null);
                } else {
                    d(subTag, title + ":\n" + message, null);
                }
                return;
            } else if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                String message = jsonArray.toString(Constant.JSON_INDENT);
                if (TextUtils.isEmpty(title)) {
                    d(subTag, message, null);
                } else {
                    d(subTag, title + ":\n" + message, null);
                }
                return;
            }
            if (TextUtils.isEmpty(title)) {
                e(subTag, "Invalid Json", null);
            } else {
                e(subTag, title + ":\n" + "Invalid Json", null);
            }
        } catch (JSONException e) {
            if (TextUtils.isEmpty(title)) {
                e(subTag, "Invalid Json", null);
            } else {
                e(subTag, title + ":\n" + "Invalid Json", null);
            }
        }
    }

    /**
     * 打印日志（Xml）
     *
     * @param subTag
     * @param title
     * @param xml
     */
    public void xml(String subTag, String title, String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            if (TextUtils.isEmpty(title)) {
                d(subTag, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"), null);
            } else {
                d(subTag, title + ":\n" + xmlOutput.getWriter().toString().replaceFirst(">", ">\n"), null);
            }
        } catch (TransformerException e) {
            if (TextUtils.isEmpty(title)) {
                e(subTag, "Invalid Xml", null);
            } else {
                e(subTag, title + ":\n" + "Invalid Xml", null);
            }
        }
    }


    /**
     * 打印日志
     *
     * @param priority
     * @param subTag
     * @param message
     * @param throwable
     */
    private void log(int priority, String subTag, String message, @Nullable Throwable throwable) {
        if (throwable != null) {
            if (TextUtils.isEmpty(message)) {
                message = Utils.getStackTraceString(throwable);
            } else {
                message += " : " + Utils.getStackTraceString(throwable);
            }
        }
        if (TextUtils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        for (ILogAdapter adapter : adapterList) {
            if (adapter.isLoggable()) {
                adapter.log(priority, subTag, message, logStrategy);
            }
        }
    }
}
