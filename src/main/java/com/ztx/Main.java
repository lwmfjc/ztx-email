package com.ztx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        makeFile("D:\\xs\\ztx-email\\a\\b", "hello.txt");
    }

    private static void makeFile(String path, String fileName) throws IOException {
        File filePath = new File(path);
        if (!filePath.exists()) {
            System.out.println("目录不存在,新创建一个");
            boolean mkdirs = filePath.mkdirs();
            //目录创建成功
            if (mkdirs) {
                File file = new File(path, fileName);
                boolean newFile = file.createNewFile();
                if (newFile) {
                    System.out.println("创建成功");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write('h');
                    fileOutputStream.write('k');
                    fileOutputStream.close();
                    System.out.println("写入成功");
                }
            }
        }
    }
}
