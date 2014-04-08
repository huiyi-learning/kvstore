/**
 * @author HC
 * @version 0.0.1
 */
package cn.edu.sjtu.se.kvstore.common;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream; 
import java.io.IOException;
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream; 

/**
 * Compress data
 */
public class ZipUtil {
  
    private static String format = "ISO8859-1";
    private static int bytesize = 256;
    
    public static String compress(String str) throws IOException {
	    if (str == null || str.length() == 0) {
	    	return str;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    GZIPOutputStream gzip = new GZIPOutputStream(out);
	    gzip.write(str.getBytes());
	    gzip.close();
	    return out.toString(format);
    }

    public static String decompress(String str) throws IOException {
	    if (str == null || str.length() == 0) {
	    	return str;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(format));
	    GZIPInputStream gunzip = new GZIPInputStream(in);
	    byte[] buffer = new byte[bytesize];
	    int n;
	    while ((n = gunzip.read(buffer, 0, bytesize)) > 0) {
	    	out.write(buffer, 0, n);
	    }
	    return out.toString();
    }
}

