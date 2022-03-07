package com.ztx;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;

public class FileTempTest {
    public static void main(String[] args) throws IOException {
        //File file=new File("hello");
        /*File abc = File.createTempFile("abc", ".txt");
        boolean fileCreated = abc.createNewFile();
        if(fileCreated){
            FileOutputStream fos=new FileOutputStream(abc)
        }*/
        //Files.copy(Paths.)
        /*FileUtils.copyDirectoryToDirectory(new File("src\\main\\resources\\unzipTest\\申报表"),
                new File("src\\main\\resources\\unzipTest\\申报表1\\aa"));*/
        //File file=new File("src\\main\\resources\\unzipTest\\申报表.rar");
        //Files.copy(new FileInputStream(file), Paths.get("D:\\Desktop\\临时申请记录\\1646643066000\\申报.rar"));

        FileUtils.deleteDirectory(new File("D:\\xs\\ztx-email\\src\\main\\resources\\unzipTest\\申报表"));
    }
}
