package com.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyManager
/*     */ {
/*  24 */   private static final Map<String, PropertyManager> map = new HashMap();
/*  25 */   private static Object managerLock = new Object();
/*  26 */   private static String propPath = "/";
/*  27 */   private static String defaultName = "application";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   public static String getProperty(String name, String... propertyFileName) { return getPropertyObject(propertyFileName).getProp(name); }
/*     */   
/*     */   private static PropertyManager getPropertyObject(String... propertyName) {
/*     */     String name;
/*  42 */     if ((propertyName == null) || (propertyName.length == 0) || 
/*  43 */       (propertyName[0] == null) || (propertyName[0].trim().equals(""))) {
/*  44 */       name = defaultName;
/*     */     } else
/*  46 */       name = propertyName[0];
/*  47 */     PropertyManager manager = (PropertyManager)map.get(name);
/*  48 */     if (manager == null) {
/*  49 */       String fullPath = propPath + name + ".properties";
/*  50 */       synchronized (managerLock)
/*     */       {
/*  52 */         manager = new PropertyManager(fullPath);
/*  53 */         map.put(name, manager);
/*     */       }
/*     */     }
/*     */     
/*  57 */     return manager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setProperty(String name, String value, String... propertyName)
/*     */   {
/*  71 */     getPropertyObject(propertyName).setProp(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void deleteProperty(String name, String... propertyName)
/*     */   {
/*  82 */     getPropertyObject(propertyName).deleteProp(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Enumeration propertyNames(String... propertyName)
/*     */   {
/*  91 */     return getPropertyObject(propertyName).propNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean propertyFileIsReadable(String... propertyName)
/*     */   {
/*  99 */     return getPropertyObject(propertyName).propFileIsReadable();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean propertyFileIsWritable(String... propertyName)
/*     */   {
/* 107 */     return getPropertyObject(propertyName).propFileIsWritable();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean propertyFileExists(String... propertyName)
/*     */   {
/* 115 */     return getPropertyObject(propertyName).propFileExists();
/*     */   }
/*     */   
/* 118 */   private Properties properties = null;
/* 119 */   private Object propertiesLock = new Object();
/*     */   
/*     */   private String resourceURI;
/*     */   
/*     */ 
/*     */   private PropertyManager(String resourceURI)
/*     */   {
/* 126 */     this.resourceURI = resourceURI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getProp(String name)
/*     */   {
/* 142 */     if (this.properties == null) {
/* 143 */       synchronized (this.propertiesLock)
/*     */       {
/* 145 */         if (this.properties == null) {
/* 146 */           loadProps();
/*     */         }
/*     */       }
/*     */     }
/* 150 */     String property = this.properties.getProperty(name);
/* 151 */     if (property == null) {
/* 152 */       return null;
/*     */     }
/* 154 */     return property.trim();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setProp(String name, String value)
/*     */   {
/* 164 */     synchronized (this.propertiesLock)
/*     */     {
/* 166 */       if (this.properties == null) {
/* 167 */         loadProps();
/*     */       }
/* 169 */       this.properties.setProperty(name, value);
/* 170 */       saveProps();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void deleteProp(String name)
/*     */   {
/* 176 */     synchronized (this.propertiesLock)
/*     */     {
/* 178 */       if (this.properties == null) {
/* 179 */         loadProps();
/*     */       }
/* 181 */       this.properties.remove(name);
/* 182 */       saveProps();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Enumeration propNames()
/*     */   {
/* 189 */     if (this.properties == null) {
/* 190 */       synchronized (this.propertiesLock)
/*     */       {
/* 192 */         if (this.properties == null) {
/* 193 */           loadProps();
/*     */         }
/*     */       }
/*     */     }
/* 197 */     return this.properties.propertyNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void loadProps()
/*     */   {
/* 204 */     this.properties = new Properties();
/* 205 */     InputStream in = null;
/*     */     try {
/* 207 */       in = getClass().getResourceAsStream(this.resourceURI);
/* 208 */       this.properties.load(in);
/*     */     }
/*     */     catch (Exception e) {
/* 211 */       System.err.println("Error reading Application properties in PropertyManager.loadProps() " + 
/* 212 */         e);
/* 213 */       e.printStackTrace();
/*     */       try
/*     */       {
/* 216 */         in.close(); } catch (Exception localException1) {} } finally { try { in.close();
/*     */       }
/*     */       catch (Exception localException2) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void saveProps()
/*     */   {
/* 229 */     String path = "";
/* 230 */     OutputStream out = null;
/*     */     try {
/* 232 */       path = this.properties.getProperty("path").trim();
/* 233 */       out = new FileOutputStream(path);
/* 234 */       this.properties.store(out, "application.properties -- " + 
/* 235 */         new Date());
/*     */     }
/*     */     catch (Exception ioe) {
/* 238 */       System.err.println("There was an error writing application.properties to " + 
/* 239 */         path + 
/* 240 */         ". " + 
/* 241 */         "Ensure that the path exists and that the Application process has permission " + 
/* 242 */         "to write to it -- " + ioe);
/* 243 */       ioe.printStackTrace();
/*     */       try
/*     */       {
/* 246 */         out.close(); } catch (Exception localException1) {} } finally { try { out.close();
/*     */       }
/*     */       catch (Exception localException2) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean propFileIsReadable()
/*     */   {
/*     */     try
/*     */     {
/* 258 */       InputStream in = getClass().getResourceAsStream(this.resourceURI);
/* 259 */       return true;
/*     */     } catch (Exception e) {}
/* 261 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean propFileExists()
/*     */   {
/* 270 */     String path = getProp("path");
/* 271 */     if (path == null) {
/* 272 */       return false;
/*     */     }
/* 274 */     File file = new File(path);
/* 275 */     if (file.isFile()) {
/* 276 */       return true;
/*     */     }
/* 278 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean propFileIsWritable()
/*     */   {
/* 287 */     String path = getProp("path");
/* 288 */     File file = new File(path);
/* 289 */     if (file.isFile())
/*     */     {
/* 291 */       if (file.canWrite()) {
/* 292 */         return true;
/*     */       }
/* 294 */       return false;
/*     */     }
/*     */     
/* 297 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 304 */   private static String sAppServer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int MAJOR_VERSION = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int MINOR_VERSION = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int REVISION_VERSION = 4;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getAppVersion()
/*     */   {
/* 326 */     return "1.2.4";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getAppVersionMajor()
/*     */   {
/* 333 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getAppVersionMinor()
/*     */   {
/* 340 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 347 */   public static int getAppVersionRevision() { return 4; }
/*     */   
/*        public static void main(String[] args) {
 350      System.out.println(getProperty("JDBC_TYPE", new String[] { "" }));
 351      System.out.println(getProperty("db.driver", new String[0]));
 352      System.out.println(getProperty("1000", new String[] { "errorConfig" }));
        }*/
/*     */ }

/* Location:           D:\workspace\newoa\WebContent\WEB-INF\lib\easyweb-core-3.1.1-SNAPSHOT.jar
 * Qualified Name:     com.zrar.easyweb.core.util.PropertyManager
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */