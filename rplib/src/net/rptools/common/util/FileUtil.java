/* The MIT License
 * 
 * Copyright (c) 2005 David Rice, Trevor Croft
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation files 
 * (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, 
 * publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package net.rptools.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 */
public class FileUtil {

    public static byte[] loadFile(File file) throws IOException{
        
        return getBytes(new FileInputStream(file));
    }
    
    public static byte[] loadResource(String resource) throws IOException {
        
        return getBytes(FileUtil.class.getClassLoader().getResourceAsStream(resource));
    }
    
    public static void saveResource(String resource, File destDir) throws IOException {

        int index = resource.lastIndexOf('/');
        String filename = index >= 0 ? resource.substring(index+1) : resource;
        
        saveResource(resource, destDir, filename);
    }

    public static void saveResource(String resource, File destDir, String filename) throws IOException {
        
        File outFilename = new File(destDir + File.separator + filename);
        
        InputStream inStream = FileUtil.class.getClassLoader().getResourceAsStream(resource);
        OutputStream outStream = new BufferedOutputStream(new FileOutputStream(outFilename));
        
        int data = 0;
        while ((data = inStream.read()) != -1) {
            outStream.write(data);
        }
        
        outStream.close();
    }
    
    public static byte[] getBytes(URL url) throws IOException {
    	return getBytes(url.openConnection().getInputStream());
    }
    
    private static byte[] getBytes(InputStream inStream) throws IOException {

        if (inStream == null) {
            throw new IllegalArgumentException ("Input stream cannot be null");
        }
        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        
        int b;
        while ((b = inStream.read()) >= 0) {
            outStream.write(b);
        }
        
        return outStream.toByteArray();
    }
    
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
         destFile.createNewFile();
        }
        
        FileChannel source = null;
        FileChannel destination = null;
        try {
         source = new FileInputStream(sourceFile).getChannel();
         destination = new FileOutputStream(destFile).getChannel();
         destination.transferFrom(source, 0, source.size());
        }
        finally {
         if(source != null) {
          source.close();
         }
         if(destination != null) {
          destination.close();
         }
       }
    }    
    
}
