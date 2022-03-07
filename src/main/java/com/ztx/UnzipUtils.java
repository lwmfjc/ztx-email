package com.ztx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipUtils {

    private static Logger log = LoggerFactory.getLogger(UnzipUtils.class);

    public static void main(String[] args) {
        try {
            unZip("src/main/resources/unzipTest/申报表.zip",
                    "d://a//");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param sourceFilename zip文件路径
     * @param targetDir      解压到的文件夹
     * @throws IOException
     */
    public static void unZip(String sourceFilename, String targetDir) throws IOException {
        unZip(new File(sourceFilename), targetDir);
    }

    /**
     * 将sourceFile解压到targetDir
     *
     * @param sourceFile
     * @param targetDir
     * @throws RuntimeException
     */
    private static void unZip(File sourceFile, String targetDir) throws IOException {
        long start = System.currentTimeMillis();
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("cannot find the file = " + sourceFile.getPath());
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(sourceFile, Charset.forName("GBK"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = targetDir + "/" + entry.getName();
                    createDirIfNotExist(dirPath);
                } else {
                    File targetFile = new File(targetDir + "/" + entry.getName());
                    createFileIfNotExist(targetFile);
                    InputStream is = null;
                    FileOutputStream fos = null;
                    try {
                        is = zipFile.getInputStream(entry);
                        fos = new FileOutputStream(targetFile);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    } finally {
                        try {
                            fos.close();
                        } catch (Exception e) {
                            log.warn("close FileOutputStream exception", e);
                        }
                        try {
                            is.close();
                        } catch (Exception e) {
                            log.warn("close InputStream exception", e);
                        }
                    }
                }
            }
            log.info("解压完成，耗时：" + (System.currentTimeMillis() - start) + " ms");
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    log.warn("close zipFile exception", e);
                }
            }
        }
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public static void createDirIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param file
     * @throws IOException
     */
    public static void createFileIfNotExist(File file) throws IOException {
        //创建文件之前先判断文件夹是否存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //创建文件
        file.createNewFile();
    }
}
