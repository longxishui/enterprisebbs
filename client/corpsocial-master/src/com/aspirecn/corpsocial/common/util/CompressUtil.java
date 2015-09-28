/**
 * @(#) Compress.java Created on 2013-11-28
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The class <code>Compress</code>
 * <p/>
 * 文件压缩解压工具类
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class CompressUtil {
    public static byte[] compress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(bytes);
        gzip.close();
        return out.toByteArray();
    }

    public static byte[] uncompress(byte[] strByte) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(strByte);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
        gunzip.close();
        // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
        return out.toByteArray();
    }

}
