package com.github.yangkangli.logger.adapter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.yangkangli.logger.utils.Constant;
import com.github.yangkangli.logger.core.BaseLogStrategy;
import com.github.yangkangli.logger.core.ILogAdapter;
import com.github.yangkangli.logger.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiskAdapter implements ILogAdapter {

    /**
     * 是否开启打印日志
     */
    private boolean loggable;

    /**
     * 日志输出目录
     */
    private String logFilePath;

    /**
     * 时间格式器
     */
    private SimpleDateFormat simpleDateFormat;

    private SimpleDateFormat fileNameFormat;

    private WriteHandler writeHandler;

    /**
     * 构造方法
     *
     * @param builder
     */
    private DiskAdapter(Builder builder) {
        this.loggable = builder.loggable;
        this.logFilePath = builder.logFilePath;
        this.simpleDateFormat = new SimpleDateFormat(builder.formatPattern);
        this.fileNameFormat = new SimpleDateFormat("yyyy-MM-dd");

        HandlerThread thread = new HandlerThread("AndroidFileLogger");
        thread.start();
        this.writeHandler = new WriteHandler(thread.getLooper(), this);
    }


    @Override
    public boolean isLoggable() {
        return loggable;
    }

    @Override
    public void log(int priority, String subTag, String message, BaseLogStrategy strategy) {

        // 时间 级别/Tag:
        String commonInfo = simpleDateFormat.format(new Date()) + " " + Utils.getLevelName(priority) + "/" + getFullTag(strategy, subTag) + ": ";

        List<String> lines = new ArrayList<>();

        // 上边线
        String topBorder = Utils.getTopBorder(subTag, strategy.getBorderMaxLength(), strategy.getLinkerLength());
        lines.add(commonInfo + topBorder);

        // 线程名称
        if (strategy.isShowThreadName()) {
            // 线程名
            String threadName = Constant.HORIZONTAL_LINE + " Thread:" + Thread.currentThread().getName();
            lines.add(commonInfo + threadName);
            // 分隔线
            String divider = Utils.getDivider(subTag, strategy.getBorderMaxLength(), strategy.getLinkerLength());
            lines.add(commonInfo + divider);
        }

        // 调用堆栈
        if (strategy.isShowStackTrace()) {
            String level = "";
            List<StackTraceElement> traceList = Utils.getTraceList(Thread.currentThread().getStackTrace(), strategy.getMethodCount());
            for (StackTraceElement element : traceList) {
                StringBuilder builder = new StringBuilder();
                builder.append(Constant.HORIZONTAL_LINE)
                        .append(' ')
                        .append(level)
                        .append(Utils.getSimpleClassName(element.getClassName()))
                        .append(".")
                        .append(element.getMethodName())
                        .append(" ")
                        .append(" (")
                        .append(element.getFileName())
                        .append(":")
                        .append(element.getLineNumber())
                        .append(")");
                level += "    ";
                lines.add(commonInfo + builder.toString());
            }
            // 分隔线
            String divider = Utils.getDivider(subTag, strategy.getBorderMaxLength(), strategy.getLinkerLength());
            lines.add(commonInfo + divider);
        }

        // 消息内容
        String[] strings = Utils.splitMessage(message);
        for (String msg : strings) {
            lines.add(commonInfo + Constant.HORIZONTAL_LINE + " " + msg);
        }

        // 下边线
        String bottomBorder = Utils.getBottomBorder(subTag, strategy.getBorderMaxLength(), strategy.getLinkerLength());
        lines.add(commonInfo + bottomBorder);

        // 传递给Handler
        writeHandler.sendMessage(writeHandler.obtainMessage(priority, lines));
    }

    /**
     * 将日志写入文件
     *
     * @param lines
     */
    private void writeLog(List<String> lines) {

        FileWriter fileWriter = null;

        try {
            File logFile = getLogFile();
            fileWriter = new FileWriter(logFile, true);
            for (String line : lines) {
                fileWriter.append(line).append("\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e1) { /* fail silently */ }
            }
        }
    }

    /**
     * 获得日志文件
     *
     * @return
     * @throws IOException
     */
    private File getLogFile() throws IOException {
        // 若没有该目录，则创建目录
        File folder = new File(logFilePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File logFile = new File(folder, String.format("%s.log", fileNameFormat.format(new Date())));
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        return logFile;
    }

    /**
     * 获得完整的Tag（BaseTag+SubTag）
     *
     * @param strategy
     * @param subTag
     * @return
     */
    private String getFullTag(BaseLogStrategy strategy, String subTag) {
        if (!TextUtils.isEmpty(subTag)) {
            return strategy.getBaseTag() + strategy.getLinker() + subTag;
        }
        return strategy.getBaseTag();
    }


    /**
     *
     */
    public static class WriteHandler extends Handler {

        /**
         * DiskAdapter弱引用
         */
        private WeakReference<DiskAdapter> adapterReference;

        /**
         * 构造方法
         *
         * @param looper
         * @param adapter
         */
        public WriteHandler(@NonNull Looper looper, DiskAdapter adapter) {
            super(Utils.checkNotNull(looper));
            this.adapterReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            DiskAdapter diskAdapter = adapterReference.get();
            if (diskAdapter != null) {
                diskAdapter.writeLog((List<String>) msg.obj);
            }
        }
    }

    /**
     * 构造器，用于构造DefaultAdapter实例
     */
    public static class Builder {
        /**
         * 是否开启打印日志
         */
        private boolean loggable = true;

        /**
         * 日志文件输出路径
         */
        private String logFilePath;

        /**
         * 日期时间格式化模式
         */
        private String formatPattern = Constant.DEFAULT_FORMAT_PATTERN;

        /**
         * 设置是否开启打印日志
         *
         * @param loggable
         * @return
         */
        public Builder setLoggable(boolean loggable) {
            this.loggable = loggable;
            return this;
        }

        /**
         * 设置日志文件输出路径
         *
         * @param path
         * @return
         */
        public Builder setLogFilePath(String path) {
            this.logFilePath = path;
            return this;
        }

        /**
         * 设置日期时间格式化模式
         *
         * @param pattern
         * @return
         */
        public Builder setFormatPattern(String pattern) {
            this.formatPattern = pattern;
            return this;
        }

        /**
         * 构造DefaultAdapter
         *
         * @return
         */
        public DiskAdapter build() {
            return new DiskAdapter(this);
        }
    }
}
