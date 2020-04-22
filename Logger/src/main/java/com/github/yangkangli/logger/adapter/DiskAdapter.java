package com.github.yangkangli.logger.adapter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.yangkangli.logger.Constant;
import com.github.yangkangli.logger.core.BaseLogStrategy;
import com.github.yangkangli.logger.core.ILogAdapter;
import com.github.yangkangli.logger.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        String dateTime = simpleDateFormat.format(new Date());

        // 得到完整的Tag
        String tag = getFullTag(strategy, subTag);

        writeHandler.sendMessage(writeHandler.obtainMessage(priority, new LogWrapper(subTag, message, strategy)));
    }

    /**
     * 将日志写入文件
     *
     * @param priority
     * @param wrapper
     */
    private void writeLog(int priority, LogWrapper wrapper) {
        Log.d("Adam", "DiskAdapter -> writeLog(" + priority + ", thread = )" + Thread.currentThread().getName());
        Log.d("Adam", "DiskAdapter -> writeLog -> wrapper:" + wrapper.toString());

        FileWriter fileWriter = null;

        try {
            File logFile = getLogFile();
            fileWriter = new FileWriter(logFile, true);

            fileWriter.append(wrapper.toString());

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
            boolean tag = logFile.createNewFile();
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
                diskAdapter.writeLog(msg.what, (LogWrapper) msg.obj);
            }
        }
    }

    private static class LogWrapper {
        protected String subTag;
        protected String message;
        protected BaseLogStrategy strategy;

        public LogWrapper(String subTag, String message, BaseLogStrategy strategy) {
            this.subTag = subTag;
            this.message = message;
            this.strategy = strategy;
        }

        @Override
        public String toString() {
            return "LogWrapper{" +
                    "subTag='" + subTag + '\'' +
                    ", message='" + message + '\'' +
                    ", strategy=" + strategy +
                    '}';
        }
    }

    /**
     * 构造器，用于构造DefaultAdapter实例
     *
     * @author yangkangli
     * @date: 2020/4/22
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
