package com.ztx;

import java.io.IOException;

public class DecompressUtils {
    public static void main(String[] args) throws IOException {
        decompress("src/main/resources/unzipTest/申报表4.rar",
                "src/main/resources/unzipTest/aa");
    }

    public static void decompress(String sourceFilename, String targetDir) throws IOException {
        if (sourceFilename.endsWith(".zip")) {
            UnzipUtils.unZip(sourceFilename, targetDir);
        } else if (sourceFilename.endsWith(".rar")) {
            Unrar5Utils.unrar(sourceFilename, targetDir);
        }
    }
}
