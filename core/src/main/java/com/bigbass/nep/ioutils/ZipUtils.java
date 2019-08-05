package com.bigbass.nep.ioutils;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.*;

public class ZipUtils {

    private ZipUtils(){};

    public static FileHandle zipFile(FileHandle handle) throws IOException {
        File f = handle.file();
        File out = new File(f.getParentFile().toString()+File.separatorChar+f.getName()+".zip");
        out.setWritable(true);
        FileInputStream outputStream = new FileInputStream(f);
        ZipEntry e = new ZipEntry(f.getName());
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(out));
        byte[] buffer = new byte[1024];
        int len = 0;
        zipOutputStream.putNextEntry(e);
        while ((len = outputStream.read(buffer)) != -1){
            zipOutputStream.write(buffer,0,len);
        }
        zipOutputStream.closeEntry();
        zipOutputStream.flush();
        zipOutputStream.finish();
        zipOutputStream.close();
        outputStream.close();
        return new FileHandle(out);
    }

    public static FileHandle unzipFile(FileHandle handle) throws IOException {
        File f = handle.file();
        File out = new File(f.getParentFile().toString()+File.separatorChar+f.getName().substring(0,f.getName().length()-4));
        out.setWritable(true);
        ZipInputStream gzipInputStream = new ZipInputStream(new FileInputStream(f));
        FileOutputStream outputStream = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        int len = 0;
        ZipEntry e = gzipInputStream.getNextEntry();
        while ((len = gzipInputStream.read(buffer)) > 0){
            outputStream.write(buffer,0,len);
        }
        gzipInputStream.closeEntry();
        outputStream.flush();
        outputStream.close();
        gzipInputStream.close();
        return new FileHandle(out);
    }

    public static FileHandle gzipFile(FileHandle handle) throws IOException {
        File f = handle.file();
        File out = new File(f.getParentFile().toString()+File.separatorChar+f.getName()+".gzip");
        out.setWritable(true);
        FileInputStream outputStream = new FileInputStream(f);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(out));
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = outputStream.read(buffer)) != -1){
            gzipOutputStream.write(buffer,0,len);
        }
        gzipOutputStream.flush();
        gzipOutputStream.finish();
        gzipOutputStream.close();
        outputStream.close();
        return new FileHandle(out);
    }

    public static FileHandle ungzipFile(FileHandle handle) throws IOException {
        File f = handle.file();
        File out = new File(f.getParentFile().toString()+File.separatorChar+f.getName().substring(0,f.getName().length()-5));
        out.setWritable(true);
        GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(f));
        FileOutputStream outputStream = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = gzipInputStream.read(buffer)) > 0){
            outputStream.write(buffer,0,len);
        }
        outputStream.flush();
        outputStream.close();
        gzipInputStream.close();
        return new FileHandle(out);
    }
}
