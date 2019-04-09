package net.portlet.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author: 张新征
 * @date: 2019-04-09 23:28
 */
public class CloseUtils {
    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
