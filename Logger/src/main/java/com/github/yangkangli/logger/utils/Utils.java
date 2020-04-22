package com.github.yangkangli.logger.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.yangkangli.logger.ALogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    /**
     * 检查对象是否为null，若为null，则抛出NullPointerException异常
     *
     * @param obj
     * @param <T>
     * @return
     */
    @NonNull
    public static <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * 获得上边线
     *
     * @param subTag
     * @param maxLength
     * @param linkerLength
     * @return
     */
    public static String getTopBorder(String subTag, int maxLength, int linkerLength) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.TOP_CORNER);
        int tagLength = TextUtils.isEmpty(subTag) ? 0 : (subTag.length() + linkerLength);
        while (stringBuilder.length() + tagLength < maxLength) {
            stringBuilder.append(Constant.REAL_LINE_DIVIDER);
        }
        return stringBuilder.toString();
    }

    /**
     * 获得分隔线
     *
     * @param subTag
     * @param maxLength
     * @param linkerLength
     * @return
     */
    public static String getDivider(String subTag, int maxLength, int linkerLength) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.MIDDLE_CORNER);
        int tagLength = TextUtils.isEmpty(subTag) ? 0 : (subTag.length() + linkerLength);
        while (stringBuilder.length() + tagLength < maxLength) {
            stringBuilder.append(Constant.DASH_LINE_DIVIDER);
        }
        return stringBuilder.toString();
    }

    /**
     * 获得下边线
     *
     * @param subTag
     * @param maxLength
     * @param linkerLength
     * @return
     */
    public static String getBottomBorder(String subTag, int maxLength, int linkerLength) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.BOTTOM_CORNER);
        int tagLength = TextUtils.isEmpty(subTag) ? 0 : (subTag.length() + linkerLength);
        while (stringBuilder.length() + tagLength < maxLength) {
            stringBuilder.append(Constant.REAL_LINE_DIVIDER);
        }
        return stringBuilder.toString();
    }


    /**
     * 获得调用堆栈列表
     *
     * @param elements
     * @param methodCount
     * @return
     */
    public static List<StackTraceElement> getTraceList(StackTraceElement[] elements, int methodCount) {
        int count = 0;
        boolean found = false;
        List<StackTraceElement> retList = new ArrayList();
        for (int i = 0; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            String className = element.getClassName();
            if (ALogger.class.getName().equals(className)) {
                found = true;
                continue;
            }
            if (found) {
                retList.add(element);
                count += 1;
            }
            if (count >= methodCount) {
                break;
            }
        }
        return retList;
    }

    /**
     * 获得class名字的简称
     *
     * @param className
     * @return
     */
    public static String getSimpleClassName(@NonNull String className) {
        checkNotNull(className);
        int lastIndex = className.lastIndexOf(".");
        return className.substring(lastIndex + 1);
    }


    /**
     * 将message按换行符分隔成多行
     *
     * @param message
     * @return
     */
    public static String[] splitMessage(@NonNull String message) {
        Utils.checkNotNull(message);
        String[] lines = message.split(System.getProperty("line.separator"));
        return lines;
    }

    /**
     * 获得Log级别字符串
     *
     * @param priority
     * @return
     */
    public static String getLevelName(int priority) {
        switch (priority) {
            case Constant.VERBOSE:
                return "V";
            case Constant.DEBUG:
                return "D";
            case Constant.INFO:
                return "I";
            case Constant.WARN:
                return "W";
            case Constant.ERROR:
                return "E";
            case Constant.ASSERT:
                return "A";
            default:
                return "U";
        }
    }


    /**
     * Object转String
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        if (object != null) {
            if (!object.getClass().isArray()) {
                return object.toString();
            }
            if (object instanceof boolean[]) {
                return Arrays.toString((boolean[]) object);
            } else if (object instanceof byte[]) {
                return Arrays.toString((byte[]) object);
            } else if (object instanceof char[]) {
                return Arrays.toString((char[]) object);
            } else if (object instanceof short[]) {
                return Arrays.toString((short[]) object);
            } else if (object instanceof int[]) {
                return Arrays.toString((int[]) object);
            } else if (object instanceof long[]) {
                return Arrays.toString((long[]) object);
            } else if (object instanceof float[]) {
                return Arrays.toString((float[]) object);
            } else if (object instanceof double[]) {
                return Arrays.toString((double[]) object);
            } else if (object instanceof Object[]) {
                return Arrays.deepToString((Object[]) object);
            }
            return "Couldn't find a correct type for the object";
        }
        return null;
    }
}
