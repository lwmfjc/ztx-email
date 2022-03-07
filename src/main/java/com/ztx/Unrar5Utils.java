package com.ztx;

import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.*;

public class Unrar5Utils {

    public static void main(String[] args) throws IOException {
        String rarFile = "src\\main\\resources\\unzipTest\\申报表.rar";
        String unRarDir = "src\\main\\resources\\unzipTest\\";
        unrar(rarFile, unRarDir);

    }

    protected static void unrar(String rarFile, String unRarDir) throws IOException {

        //String rarFile = "src\\main\\resources\\unzipTest\\申报表4.rar";
        //String outDir = "D:\\rar5";
        //String unRarDir="src\\main\\resources\\unzipTest\\";

        IInArchive archive;
        RandomAccessFile randomAccessFile;
        // 第一个参数是需要解压的压缩包路径，第二个参数参考JdkAPI文档的RandomAccessFile
        //r代表以只读的方式打开文本，也就意味着不能用write来操作文件
        randomAccessFile = new RandomAccessFile(rarFile, "r");
        archive = SevenZip.openInArchive(null, // null - autodetect
                new RandomAccessFileInStream(randomAccessFile));
        int[] in = new int[archive.getNumberOfItems()];
        for (int i = 0; i < in.length; i++) {
            in[i] = i;
        }
        archive.extract(in, false, new ExtractCallback(archive, unRarDir + "/"));
        archive.close();
        randomAccessFile.close();
    }
}

class ExtractCallback implements IArchiveExtractCallback {

    private int index;
    private IInArchive inArchive;
    private String ourDir;

    public ExtractCallback(IInArchive inArchive, String ourDir) {
        this.inArchive = inArchive;
        this.ourDir = ourDir;
    }

    @Override
    public void setCompleted(long arg0) throws SevenZipException {
    }

    @Override
    public void setTotal(long arg0) throws SevenZipException {
    }

    @Override
    public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
        this.index = index;
        final String path = (String) inArchive.getProperty(index, PropID.PATH);
        final boolean isFolder = (boolean) inArchive.getProperty(index, PropID.IS_FOLDER);
        return data -> {
            try {
                if (!isFolder) {
                    File file = new File(ourDir + path);
                    save2File(file, data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data.length;
        };
    }

    @Override
    public void prepareOperation(ExtractAskMode arg0) throws SevenZipException {
    }

    @Override
    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {

    }

    private static boolean save2File(File file, byte[] msg) {
        OutputStream fos = null;
        try {
            File parent = file.getParentFile();
            if ((!parent.exists()) && (!parent.mkdirs())) {
                return false;
            }
            fos = new FileOutputStream(file, true);
            fos.write(msg);
            fos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
