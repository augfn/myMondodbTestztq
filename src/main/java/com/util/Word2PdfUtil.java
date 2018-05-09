package com.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant; 
import com.jacob.com.ComThread;

/**
* @author :ztq
* @version 创建时间：2018年2月11日 上午10:02:35
* 类说明
*/
public class Word2PdfUtil {
	 /**  
    * log  
    */  
   private static Logger logger = Logger.getLogger(Word2PdfUtil.class);  
   private static final int WDFO_RMATPDF = 17;  
   private static final int XLTYPE_PDF = 0;  
   private static final int PPT_SAVEAS_PDF = 32;  
   public static final int WORD_HTML = 8;  
   public static final int WORD_TXT = 7;  
   public static final int EXCEL_HTML = 44;  
   public static final int PPT_SAVEAS_JPG = 17;
   private static ArrayList<String> fileList = new ArrayList<String>();  
   // private static final int msoTrue = -1;  
   // private static final int msofalse = 0;  
   
   /** 
   * @功能: 
   * @return void    返回类型
   * @author :ZTQ 
   * @date 2018年2月11日 上午10:59:30  
   */
/*   public static void main(String[] args) throws IOException {
	   
	   //文件夹名称，
	   String filepath = "D:\\2009\\fw";
	   getFiles(filepath);
	   
//       String source1 = "e:\\officeConverterToPdf\\整式-ppt课件.ppt";  
//       String target1 = "e:\\officeConverterToPdf\\整式-ppt课件.pdf";  
//       Word2PdfUtil pdf = new Word2PdfUtil();  
//       pdf.officeFileConverterToPdf(source1, target1);  
   }*/
   
   /** 
* @功能: 遍历文件夹获取文件
* @return void    返回类型
* @author :ZTQ 
* @date 2018年3月2日 上午11:23:50  
*/
public static List<String> getFiles(String filepath){
       File root = new File(filepath);
       File[] files = root.listFiles();
       List<String> li = new ArrayList<String>();
       for(File file : files){
           if(file.isDirectory()){//如果file是一个目录，循环递归调用地柜
               //递归调用
               getFiles(file.getAbsolutePath());
               fileList.add(file.getAbsolutePath());
               if (file.isFile() && file.getName().indexOf("doc") > -1) {
            	   System.out.println("显示"+filepath+"下所有子目录及其文件"+file.getAbsolutePath());
               }
           }else{//不是目录的话遍历其地下的文件--need
        	   if (file.isFile() && file.getName().indexOf("doc") > -1) {
        		   Word2PdfUtil pdf = new Word2PdfUtil();
        		   String filePath = file.getAbsolutePath();
//        		   String qqq = filePath.substring(filePath.indexOf("\\",filePath.indexOf("\\")+1 ),filePath.lastIndexOf(".")+1);
        		   String ppp = filePath.substring(filePath.indexOf("\\",filePath.indexOf("\\")+1 ),filePath.lastIndexOf(".")+1);
        		   
        		   String outPath0 = "D:\\2007s"+ppp+"pdf";
        		   
        		   String outPath=outPath0;
        		   File folder=new File(outPath);
        		   if(!folder.exists()){
        		   folder.mkdirs();
        		   pdf.officeFileConverterToPdf(filePath, outPath);
        		   System.out.println("-------------新加----------");
        		   }
        	       li.add(filePath);
//            	   System.out.println("显示"+filepath+"下所有子目录及其文件"+file.getAbsolutePath());
               }
           }
       }
	return li;
   }
   /**  
    * @param argInputFilePath  
    * @param argPdfPath  
    * @return  
    */  
public static boolean officeFileConverterToPdf(String argInputFilePath, String argPdfPath) {  
    if (argInputFilePath.isEmpty() || argPdfPath.isEmpty() || getFileSufix(argInputFilePath).isEmpty()) {  
        logger.debug("输入或输出文件路徑有誤！");
        return false;  
    }  

    String suffix = getFileSufix(argInputFilePath);//取文件名后缀

    File file = new File(argInputFilePath);  
    if (!file.exists()) {  
        logger.debug("文件不存在！");  
        return false;  
    }  
    // PDF如果不存在则创建文件夹  
//    file = new File(getFilePath(argPdfPath));  
//    if (!file.exists()) {  
//        file.mkdir();  
//    }  
    // 如果输入的路径为PDF 则生成失败  
    if (suffix.equals("pdf")) {  
        System.out.println("PDF not need to convert!");  
        return false;  
    }  
    if (suffix.equals("doc") || suffix.equals("docx") || suffix.equals("txt")) {  
        return wordToPDF(argInputFilePath, argPdfPath);  
    } else if (suffix.equals("xls") || suffix.equals("xlsx")) {  
        return excelToPdf(argInputFilePath, argPdfPath);  
    } else if (suffix.equals("ppt") || suffix.equals("pptx")) {  
        return pptToPdf(argInputFilePath, argPdfPath);  
        // return ppt2PDF(argInputFilePath, argPdfPath);  
    }  
    return false;  
}  
 
   /**  
    * converter word to pdf  
    *   
    * @param wordPath  
    * @param pdfPath  
    * @return  
    */  
   public static boolean wordToPDF(String wordPath, String pdfFile) {
	   ComThread.InitSTA();
       ActiveXComponent msWordApp = new ActiveXComponent("Word.Application");  
       try {
       msWordApp.setProperty("Visible", new Variant(false));  
 
       Dispatch docs = Dispatch.get(msWordApp, "Documents").toDispatch();  
       // long pdfStart = System.currentTimeMillis();  
       Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[] { wordPath, new Variant(false), new Variant(true) }, new int[1]).toDispatch();  
 
       deletePdf(pdfFile);  
 
       Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] { pdfFile, new Variant(WDFO_RMATPDF) }, new int[1]);  
       // long pdfEnd = System.currentTimeMillis();  
       logger.debug(wordPath + ",pdf转换完成..");  
       if (null != doc){
           Dispatch.call(doc, "Close", false);  
       }
       System.out.println(pdfFile);
       } catch (Exception var9) {
           System.out.println("========Error:Operation fail:" + var9.getMessage());
       } finally {
           if (msWordApp != null) {
               msWordApp.invoke("Quit", new Variant[0]);
           }

       }
       ComThread.Release();
       return true;
   }  
 
   /**  
    * excel to pdf  
    *   
    * @param inputFile  
    * @param pdfFile  
    * @return  
    */  
   public static boolean excelToPdf(String inputFile, String pdfFile) {  
       ActiveXComponent activeXComponent = new ActiveXComponent("Excel.Application");  
       activeXComponent.setProperty("Visible", false);  
 
       deletePdf(pdfFile);  
 
       Dispatch excels = activeXComponent.getProperty("Workbooks").toDispatch();  
       Dispatch excel = Dispatch.call(excels, "Open", inputFile, false, true).toDispatch();  
       Dispatch.call(excel, "ExportAsFixedFormat", XLTYPE_PDF, pdfFile);  
       Dispatch.call(excel, "Close", false);  
       activeXComponent.invoke("Quit");  
       System.out.println(pdfFile);
       return true;
   }  
 
   /**  
    * ppt to pdf  
    *   
    * @param inputFile  
    * @param pdfFile  
    * @return  
    */  
   public static boolean pptToPdf(String inputFile, String pdfFile) {  
//     ComThread.InitSTA();  
       ActiveXComponent activeXComponent = new ActiveXComponent("PowerPoint.Application");  
//     activeXComponent.setProperty("Visible", new Variant(false));  
       Dispatch ppts = activeXComponent.getProperty("Presentations").toDispatch();  
 
       deletePdf(pdfFile);  
 
//       Dispatch ppt = Dispatch.call(ppts, "Open", inputFile, false, // ReadOnly  
//               true, // Untitled指定文件是否有标题  
//               true// WithWindow指定文件是否可见  
//       ).toDispatch();  
 
     Dispatch ppt = Dispatch.invoke(ppts, "Open", Dispatch.Method, new Object[] { inputFile, new Variant(false), new Variant(true) }, new int[1]).toDispatch();  
 
//       Dispatch.call(ppt, "SaveAs", pdfFile, PPT_SAVEAS_PDF);  
       Dispatch.call(ppt, "SaveAs", pdfFile, new Variant(PPT_SAVEAS_PDF));  
//     Dispatch.call(ppt, "SaveAs", pdfFile, new Variant(PPT_SAVEAS_PDF));  
//     Dispatch.invoke(ppt, "SaveAs", Dispatch.Method, new Object[] { pdfFile, PPT_SAVEAS_PDF }, new int[1]);  
//     Dispatch.invoke(ppt, "SaveAs", Dispatch.Method, new Object[] { new Variant(PPT_SAVEAS_PDF) }, new int[1]);  
//       Dispatch.callN(ppt, "SaveAs",  new Variant(pdfFile));  
         
       Dispatch.call(ppt, "Close");  
 
       activeXComponent.invoke("Quit");  
       System.out.println(pdfFile);
//     ComThread.Release();  
       return true;  
   }  
 
   /**  
    * ppt to img  
    *   
    * @param inputFile  
    * @param imgFile  
    * @return  
    */  
   public static boolean pptToImg(String inputFile, String imgFile) {  
       // 打开word应用程序  
       ActiveXComponent app = new ActiveXComponent("PowerPoint.Application");  
       // 设置word不可见，office可能有限制  
       // app.setProperty("Visible", false);  
       // 获取word中国所打开的文档，返回Documents对象  
       Dispatch files = app.getProperty("Presentations").toDispatch();  
       // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document  
       Dispatch file = Dispatch.call(files, "open", inputFile, true, true, false).toDispatch();  
       // 调用Document对象的SaveAs方法，将文档保存为pdf格式  
       // Dispatch.call(doc, "ExportAsFixedFormat", outputFile,  
       // PPT_TO_PDF);  
       Dispatch.call(file, "SaveAs", imgFile, PPT_SAVEAS_JPG);  
       // 关闭文档  
       // Dispatch.call(file, "Close", false);  
       Dispatch.call(file, "Close");  
       // 关闭word应用程序  
       // app.invoke("Quit", 0);  
       app.invoke("Quit");  
       return true;  
   }  
 
   /**  
    * get file extension  
    *   
    * @param argFilePath  
    * @return  
    */  
   public static String getFileSufix(String argFilePath) {  
       int splitIndex = argFilePath.lastIndexOf(".");  
       return argFilePath.substring(splitIndex + 1);  
   }  
 
   /**  
    * subString file path  
    *   
    * @param argFilePath  
    *            file path  
    * @return filePaths  
    */  
   public static String getFilePath(String argFilePath) {  
       int pathIndex = argFilePath.lastIndexOf(".");  
       return argFilePath.substring(0, pathIndex);  
   }  
 
   /**  
    * 如果PDF存在则删除PDF  
    *   
    * @param pdfPath  
    */  
   private static void deletePdf(String pdfPath) {  
       File pdfFile = new File(pdfPath);  
       if (pdfFile.exists()) {  
           pdfFile.delete();  
       }  
   }

   
}