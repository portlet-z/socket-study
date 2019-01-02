package net.portlet.utils;

import java.io.Closeable;

/**
 * @author zhangxinzheng
 * @date 2019-01-02 22:39
 */
public class CloseUtils {
    public static void close(Closeable...closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
