package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class TxtToJson
{
  private static Logger logger = Logger.getLogger(TxtToJson.class);
  public static final int WORD_HTML = 8;
  public static final int WORD_TXT = 7;
  public static final int EXCEL_HTML = 44;
  public static final int PPT_SAVEAS_JPG = 17;
  
  public static List<String> getFiles(String filepath)
  {
    File root = new File(filepath);
    File[] files = root.listFiles();
    List<String> li = new ArrayList();
    int i = 0;
    for (File file : files) {
      if ((file.isFile()) && (file.getName().indexOf("txt") > -1))
      {
        i++;
        TxtToJson pdf = new TxtToJson();
        String filePath = file.getAbsolutePath();
        
        li.add(filePath);
      }
    }
    System.out.println("Number" + i);
    System.out.println("Size" + li.size());
    return li;
  }
  
  public static String officeFileConverterToPdf(String argInputFilePath)
  {
    String txt = null;
    if ((argInputFilePath.isEmpty()) || (getFileSufix(argInputFilePath).isEmpty()))
    {
      logger.debug("输入或输出文件路徑有誤！");
      return "输入或输出文件路徑有誤！";
    }
    File file = new File(argInputFilePath);
    if (!file.exists())
    {
      logger.debug("文件不存在！");
      return "文件不存在！";
    }
    txt = convert(argInputFilePath);
    return txt;
  }
  
  public static String convert(String wordPath)
  {
    File file = new File(wordPath);
    BufferedReader reader = null;
    String jsonStr = null;
    String file2 = file.getAbsolutePath();
    
    String fileFolder = file2.substring(0, file2.indexOf("."));
    File mulu = new File(fileFolder);
    String attachments = "";
    String attachmentss = "";
    if (mulu.isDirectory())
    {
      String[] filelist = mulu.list();
      for (String string : filelist) {
        attachments = attachments + string + ",";
      }
    }
    String cut0 = file2.substring(file2.indexOf("\\", file2.indexOf("\\") + 1), file2.length());
    String sfw = cut0.substring(1, 3);
    String year = file2.substring(file2.indexOf("\\", file2.indexOf("\\")), file2.indexOf("\\", file2.indexOf("\\") + 1));
    String cut = "D:" + year + "txtError" + cut0;
    String nameV = file.getName();
    String nameK = nameV.substring(0, nameV.indexOf("."));
    try
    {
      FileInputStream in = new FileInputStream(file);
      reader = new BufferedReader(new InputStreamReader(in, "gbk"));
      String tempString = null;
      JSONObject jsObject = new JSONObject();
      System.out.println(file);
      String starttmp = null;
      String str1 = null;
      String str0 = null;
      StringBuffer permission0 = new StringBuffer();
      jsObject.put("_id", nameK);
      jsObject.put("sfw", sfw);
      jsObject.put("year", year);
      if (!"".equals(attachments))
      {
        attachmentss = attachments.substring(0, attachments.length() - 1);
        jsObject.put("attachments", attachmentss);
      }
      else
      {
        jsObject.put("attachments", attachments);
      }
      while ((tempString = reader.readLine()) != null) {
        try
        {
          JSONObject json = new JSONObject(jsObject);
          if ((tempString.length() != 0) && (!tempString.contains("\",\"")))
          {
            String value = json.getString(starttmp);
            if (permission0.length() == 0) {
              permission0.append(value);
            }
            permission0.append(tempString);
            str1 = starttmp;
            str0 = permission0.toString().replace("\"", "");
            jsObject.put(str1, str0);
          }
          else if (tempString.length() != 0)
          {
            str1 = tempString.substring(0, tempString.indexOf(",", tempString.indexOf(","))).replace("\"", "");
            str0 = tempString.substring(tempString.indexOf(",", tempString.indexOf(",")) + 1).replace("\"", "");
            jsObject.put(str1, str0);
          }
          starttmp = str1;
        }
        catch (Exception e)
        {
          e.printStackTrace();
          
          File folder = new File(cut);
          if (!folder.exists()) {
            folder.mkdirs();
          }
        }
      }
      jsonStr = JSON.toJSONString(jsObject);
      reader.close();
      if (reader != null) {
        try
        {
          reader.close();
        }
        catch (IOException el) {}
      }
      logger.debug(wordPath + ",转换完成..");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (reader != null) {
        try
        {
          reader.close();
        }
        catch (IOException el) {}
      }
    }
    return jsonStr;
  }
  
  public static String getFileSufix(String argFilePath)
  {
    int splitIndex = argFilePath.lastIndexOf(".");
    return argFilePath.substring(splitIndex + 1);
  }
  
  public static String getFilePath(String argFilePath)
  {
    int pathIndex = argFilePath.lastIndexOf(".");
    return argFilePath.substring(0, pathIndex);
  }
  
  private static void deletePdf(String pdfPath)
  {
    File pdfFile = new File(pdfPath);
    if (pdfFile.exists()) {
      pdfFile.delete();
    }
  }
}
