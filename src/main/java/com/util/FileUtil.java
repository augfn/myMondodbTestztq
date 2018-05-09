package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @功能描述：文件工具类
 */
public class FileUtil {
	private static final int BUFFER_SIZE = 16 * 1024;
	/*
	 * 获取文件的MD5值
	 */
	public static String catchFileMd5(File file){
		try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
           return bigInt.toString(16);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "";
	}
	/**
	 * 将src文件拷贝到dst文件中
	 */
	public static void copyFile(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}

	/**
	 * 获取文件后缀名
	 */
	public static String getExtention(String fileName) {

		final int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	/**
	 * 获取文件名字（不包括后缀名）
	 */
	public static String getFileNameBeforeExtention(String fileName) {

		int index = fileName.lastIndexOf('.');
		if ((index > 0) && (index < (fileName.length() - 1))) {
			return fileName.substring(0,index);
		} else
			return null;
	}
	
	
	 public static byte[] toByteArray(File f) throws IOException{  

	        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());  
	        BufferedInputStream in = null;  
	        try{  
	            in = new BufferedInputStream(new FileInputStream(f));  
	            int buf_size = 1024;  
	            byte[] buffer = new byte[buf_size];  
	            int len = 0;  
	            while(-1 != (len = in.read(buffer,0,buf_size))){  
	                bos.write(buffer,0,len);  
	            }  
	            return bos.toByteArray();  
	        }catch (IOException e) {  
	            e.printStackTrace();  
	            throw e;  
	        }finally{  
	            try{  
	                in.close();  
	            }catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	            bos.close();  
	        }  
	 }
	 /** 
     * the traditional io way  
     * @param filename 
     * @return 
     * @throws IOException 
     */  
    public static byte[] toByteArray(String filename) throws IOException{  
          
        File f = new File(filename);  
        if(!f.exists()){  
            throw new FileNotFoundException(filename);  
        }  
        return toByteArray(f);
    }  
	/**
	 * 删除文件夹
	 */
    public static void deleteFile(File file) {  
	    if (file.exists()) {//判断文件是否存在  
	     if (file.isFile()) {//判断是否是文件  
	      file.delete();//删除文件   
	     } else if (file.isDirectory()) {//否则如果它是一个目录  
	      File[] files = file.listFiles();//声明目录下所有的文件 files[];  
	      for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件  
	       deleteFile(files[i]);//把每个文件用这个方法进行迭代  
	      }  
	      file.delete();//删除文件夹  
	     }  
	    } else {  
	     System.out.println("所删除的文件不存在");  
	    }  
	   }  
}
