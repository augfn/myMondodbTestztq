package com.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;



/**
 * Http 访问工具类
 * 
 */
public class HttpUtils {

	private final static Logger logger = Logger.getLogger(HttpUtils.class);
	/**
	 * 证书信任管理器（用于https请求）
	 * 
	 */
	public static class MyX509TrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * GET请求 默认是 utf-8 编码
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, String params) {
		return get(url, params, Charset.forName("utf-8"));
	}

	private static void https(URLConnection conn) throws Exception {
		if (conn instanceof HttpsURLConnection) {
			// 创建SSLContext对象，并使用 指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			HttpsURLConnection https = (HttpsURLConnection) conn;
			https.setSSLSocketFactory(ssf);
		}
	}

	/**
	 * GET请求
	 * 
	 * @param url
	 *            请求URL
	 * @return
	 */
	public static String get(String url, String params, Charset charset) {
		String result = "";
		InputStream in = null;
		if (null != params && !params.equals("")) {
			if (url.contains("?")) {// 包含?,后面加&直接填加
				url += "&" + params;
			} else {
				url += "?" + params;
			}
		}

		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			https(conn);

			conn.connect();
			in = conn.getInputStream();
			result = readInputStream(charset, in);
		} catch (Exception e) { 
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		logger.info("HttpUtils:url--"+url+"--param:"+params);
		logger.info("result=----"+result);
		return result;
	}

	/**
	 * POST请求 默认是 utf-8 编码
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, String params) {
		return post(url, params, Charset.forName("utf-8"));
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 *            请求URL
	 * @param param
	 *            请求参数，请求参数格式 name1=value1&name2=value2
	 * @return
	 */
	public static String post(String url, String param, Charset charset) {
		PrintWriter out = null;
		InputStream in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			https(conn);

			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();

			in = conn.getInputStream();
			result = readInputStream(charset, in);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				try {
					if (out != null) {
						out.close();
					}
				} finally {
					if (in != null) {
						in.close();
					}
				}
			} catch (Exception ex) {
			}
		}
		logger.info("HttpUtils:url："+url+"--param:"+param+"--"+"--result"+result);
		return result;
	}

	public static String post(String url, List<PostParam> params, String charset) {

		String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
		HttpURLConnection conn;
		OutputStream out;
		try {
			URL URI = new URL(url);
			conn = (HttpURLConnection) URI.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Charsert", charset);
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			https(conn);
			out = new DataOutputStream(conn.getOutputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线

		int leng = params.size();
		for (int i = 0; i < leng; i++) {
			PostParam param = params.get(i);

			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");

			sb.append("Content-Disposition: form-data;name=\""
					+ param.getName() + "\"");
			if (param.getIn() != null) {
				// 上传文件
				sb.append(";filename=\"" + param.getFileName() + "\"\r\n");
				sb.append("Content-Type:" + param.getContentType() + "\r\n\r\n");
				byte[] data = sb.toString().getBytes();

				InputStream in = param.getIn();
				int bytes = 0;
				byte[] bufferOut = new byte[1024];

				try {
					out.write(data);
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					out.write("\r\n".getBytes());
				} catch (IOException e) {
					throw new RuntimeException(e);
				} finally {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			} else {
				// 表单
				sb.append("\r\n");
				sb.append("Content-Type:" + param.getContentType() + "\r\n\r\n");

				sb.append(param.getValue() + "\r\n");
				byte[] data = sb.toString().getBytes();
				try {
					out.write(data);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		try {
			out.write(end_data);
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}

		try {
			InputStream in = conn.getInputStream();
			return readInputStream(Charset.forName(charset), in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String readInputStream(Charset charset, InputStream in)
			throws IOException {
		byte[] bytes = new byte[1024];
		int length = -1;
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		while ((length = in.read(bytes)) != -1) {
			byteOutput.write(bytes, 0, length);
		}
		return new String(byteOutput.toByteArray(), charset);
	}

	/**
	 * https 请求用这个！
	 * @param url
	 * @param postData
	 * @param charset
	 * @return
	 */
	public static String postHttps(String url, String postData, String charset) {
		if(charset==null){
			charset = "utf-8";
		}
		String data = null;
		try {
			URL dataUrl = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) dataUrl
					.openConnection();
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestMethod("POST");
			// con.setRequestProperty("Proxy-Connection", "Keep-Alive");
//			con.setRequestProperty("Content-Type", "text/html");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			con.setDoInput(true);
			/*OutputStream os = con.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.write(postData.getBytes());
			dos.flush();
			dos.close();*/
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), charset); 
			out.write(postData);
			out.flush(); 
			out.close();
			InputStream is = con.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			byte d[] = new byte[dis.available()];
			dis.read(d);
			data = new String(d, charset);
			con.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("HttpUtils:url："+url+"--param:"+postData+"----result"+data);
		return data == null ? null : data.trim();
		
	}
	/**
     * 文件上传到微信服务器
     * @param fileType 文件类型
     * @param filePath 文件路径
     * @return JSONObject
     * @throws Exception
     */
    public static String send(String fileType, String filePath,String url)  {  
        String result = "";  
    	try {
            File file = new File(filePath);   
            /** 
            * 第一部分 
            */  
            URL urlObj = new URL(url);  
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();  
            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式  
            con.setDoInput(true);  
            con.setDoOutput(true);  
            con.setUseCaches(false); // post方式不能使用缓存  
            // 设置请求头信息  
            con.setRequestProperty("Connection", "Keep-Alive");  
            con.setRequestProperty("Charset", "UTF-8");  
            // 设置边界  
            String BOUNDARY = "----------" + System.currentTimeMillis();  
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);  
            // 请求正文信息  
            // 第一部分：  
            StringBuilder sb = new StringBuilder();  
            sb.append("--"); // 必须多两道线  
            sb.append(BOUNDARY);  
            sb.append("\r\n");  
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");  
            sb.append("Content-Type:application/octet-stream\r\n\r\n");  
            byte[] head = sb.toString().getBytes("utf-8");  
            // 获得输出流  
            OutputStream out = new DataOutputStream(con.getOutputStream());  
            // 输出表头  
            out.write(head);  
            // 文件正文部分  
            // 把文件已流文件的方式 推入到url中  
            DataInputStream in = new DataInputStream(new FileInputStream(file));  
            int bytes = 0;  
            byte[] bufferOut = new byte[1024];  
            while ((bytes = in.read(bufferOut)) != -1) {  
            out.write(bufferOut, 0, bytes);  
            }  
            in.close();  
            // 结尾部分  
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线  
            out.write(foot);  
            out.flush();  
            out.close();  
            StringBuffer buffer = new StringBuffer();  
            BufferedReader reader = null;  
            try {  
            // 定义BufferedReader输入流来读取URL的响应  
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
            //System.out.println(line);  
            buffer.append(line);  
            }  
            if(StringUtil.isNull(result)){   
            result = buffer.toString();  
            logger.debug("---------------上传多媒体返回的消息beg");
            logger.debug(result);
            logger.debug("---------------上传多媒体返回的消息end");
            }  
            } catch (IOException e) {  
            System.out.println("发送POST请求出现异常！" + e);  
            e.printStackTrace();  
            throw new IOException("数据读取异常");  
            } finally {  
            if(reader!=null){  
            reader.close();  
            }  
            }  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return result;   
    }
    public static String post(String url, Map<String,Object> param){
    	StringBuffer sb = new StringBuffer();
    	for(String key:param.keySet()){
    		sb.append(key).append("=").append(param.get(key));
    		sb.append("&");
    	}
    	String p = sb.substring(0, sb.length()-1);
    	return post(url, p);
    }
    /*
     * 向HTTP服务器上传文件
     */
    public static boolean uploadFile(File file,String url){
    	try {
    		int byteread = 0;
    		  /** 
	            * 第一部分 
	            */  
	            URL urlObj = new URL(url);  
	            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();  
	            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式  
	            con.setDoInput(true);  
	            con.setDoOutput(true);  
	            con.setUseCaches(false); // post方式不能使用缓存  
	            // 设置请求头信息  
	            con.setRequestProperty("Connection", "Keep-Alive");  
	            con.setRequestProperty("Charset", "UTF-8");  
	            // 设置边界  
	            String BOUNDARY = "----------" + System.currentTimeMillis();  
	            con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);  
	            // 请求正文信息  
	            // 第一部分：  
	            StringBuilder sb = new StringBuilder();  
	            sb.append("--"); // 必须多两道线  
	            sb.append(BOUNDARY);  
	            sb.append("\r\n");  
	            sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");  
	            sb.append("Content-Type:application/octet-stream\r\n\r\n");  
	            byte[] head = sb.toString().getBytes("utf-8");  
	            OutputStream out = new DataOutputStream(con.getOutputStream());  
	            out.write(head);  
	            DataInputStream in = new DataInputStream(new FileInputStream(file));  
	            int bytes = 0;  
	            byte[] bufferOut = new byte[1024];  
	            while ((bytes = in.read(bufferOut)) != -1) {  
	            out.write(bufferOut, 0, bytes);  
	            }  
	            in.close();  
	            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线  
	            out.write(foot);  
	            out.flush();  
	            out.close();  
	            InputStream inStream = con.getInputStream();
	 	         byte[] buffer = new byte[1204];
	 	         while ((byteread = inStream.read(buffer)) != -1) {
	 	        	 String a=new String(buffer);
	 	        	 if(a.contains("失败")) return false;
	 	         }
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
    	return true;
    }
    /*
     * 向HTTP服务器上传文件跟一个url作为参数，并且读取返回的文件存入本地
     */
    public static boolean uploadAndReadSaveFromURL(String url ,String uploadFilePath,String downFilePath,String picurl,String filemd5Id){

    	 try {
 			 int byteread = 0;
 			 File file = new File(uploadFilePath);   
 	            /** 
 	            * 第一部分 
 	            */  
 	            URL urlObj = new URL(url);  
 	            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();  
 	            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式  
 	            con.setDoInput(true);  
 	            con.setDoOutput(true);  
 	            con.setUseCaches(false); // post方式不能使用缓存  
 	            // 设置请求头信息  
 	            con.setRequestProperty("Connection", "Keep-Alive");  
 	            con.setRequestProperty("Charset", "UTF-8");  
 	            // 设置边界  
 	            String BOUNDARY = "----------" + System.currentTimeMillis();  
 	            con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);  
 	            // 请求正文信息  
 	            // 第一部分：  
 	            StringBuilder sb = new StringBuilder();  
 	            sb.append("--"); // 必须多两道线  
 	            sb.append(BOUNDARY);  
 	            sb.append("\r\n");  
 	            if(StringUtil.isNotNull(filemd5Id)){
 	            	 sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ filemd5Id+FileUtil.getExtention(file.getName()) + "\"\r\n"); 
 	            }else{ 
 	            	 sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n"); 
 	            }
 	            sb.append("Content-Type:application/octet-stream\r\n\r\n");  
 	            // 第二部分：  
 	            StringBuilder parsb = new StringBuilder();  
 	           parsb.append("--"); // 必须多两道线  
 	           parsb.append(BOUNDARY);  
 	           parsb.append("\r\n");  
 	          parsb.append("Content-Disposition: form-data;name=\"picUrl\"");   
 	           parsb.append("\r\n\r\n");
 	           parsb.append(picurl);
 	            byte[] head = sb.toString().getBytes("utf-8");  
 	            System.out.println(sb.toString());
 	            OutputStream out = new DataOutputStream(con.getOutputStream());  
 	            out.write(head);  
 	            DataInputStream in = new DataInputStream(new FileInputStream(file));  
 	            int bytes = 0;  
 	            byte[] bufferOut = new byte[1024];  
 	            while ((bytes = in.read(bufferOut)) != -1) {  
 	            	//System.out.println(bytesToHexString(bufferOut));
 	            out.write(bufferOut, 0, bytes);   
 	            }  
 	           out.write("\r\n".getBytes("utf-8")); 
 	           //System.out.println("123456\r\n");
 	           byte[] parttwo =parsb.toString().getBytes("utf-8");  
 	      /*    System.out.println(sb); 
 	           System.out.println(parttwo.toString()); */
 	           out.write(parttwo);  
 	            in.close();  
 	            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线  
 	            out.write(foot);  
 	            out.flush();  
 	            out.close(); 
 	            
 	            
 	            
 	         InputStream inStream = con.getInputStream();
 	         byte[] buffer = new byte[1204];
 	         FileOutputStream fs=null;
 	         int i=0;
 	         while ((byteread = inStream.read(buffer)) != -1) {
 	        	 String a=new String(buffer);
 	        	 if(a.contains("失败") ) break;
 	        	 if(a.contains("成功")) return true;
 	        	 if(i==0 ){
 	        		 //源文件重命名 
 	        		 fs= new FileOutputStream(downFilePath);
 	        	 } 
 	             fs.write(buffer, 0, byteread);
 	             i++;
 	         }
 	         if(i==0){
 	        	 return false;
 	         }
 		} catch (Exception e) { 
 			e.printStackTrace();
 			return false;
 		}
    	
    	return true;
    }
    
    /*
     * 向HTTP服务器上传文件，并且读取返回的文件存入本地
     */
    public static boolean uploadAndReadSaveFromURL(String url ,String uploadFilePath,String downFilePath){

    	 try {
 			 int byteread = 0;
 			 File file = new File(uploadFilePath);   
 	            /** 
 	            * 第一部分 
 	            */  
 	            URL urlObj = new URL(url);  
 	            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();  
 	            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式  
 	            con.setDoInput(true);  
 	            con.setDoOutput(true);  
 	            con.setUseCaches(false); // post方式不能使用缓存  
 	            // 设置请求头信息  
 	            con.setRequestProperty("Connection", "Keep-Alive");  
 	            con.setRequestProperty("Charset", "UTF-8");  
 	            // 设置边界  
 	            String BOUNDARY = "----------" + System.currentTimeMillis();  
 	            con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);  
 	            // 请求正文信息  
 	            // 第一部分：  
 	            StringBuilder sb = new StringBuilder();  
 	            sb.append("--"); // 必须多两道线  
 	            sb.append(BOUNDARY);  
 	            sb.append("\r\n");  
 	            sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");  
 	            sb.append("Content-Type:application/octet-stream\r\n\r\n");  
 	            byte[] head = sb.toString().getBytes("utf-8");  
 	            OutputStream out = new DataOutputStream(con.getOutputStream());  
 	            out.write(head);  
 	            DataInputStream in = new DataInputStream(new FileInputStream(file));  
 	            int bytes = 0;  
 	            byte[] bufferOut = new byte[1024];  
 	            while ((bytes = in.read(bufferOut)) != -1) {  
 	            out.write(bufferOut, 0, bytes);  
 	            }  
 	            in.close();  
 	            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线  
 	            out.write(foot);  
 	            out.flush();  
 	            out.close();  
 	         InputStream inStream = con.getInputStream();
 	         byte[] buffer = new byte[1204];
 	         FileOutputStream fs=null;
 	         int i=0;
 	         while ((byteread = inStream.read(buffer)) != -1) {
 	        	 String a=new String(buffer);
 	        	 if(a.contains("失败")) break;
 	        	 if(i==0){
 	        		 fs= new FileOutputStream(downFilePath);
 	        	 } 
 	             fs.write(buffer, 0, byteread);
 	             i++;
 	         }
 	         if(i==0){
 	        	 return false;
 	         }
 		} catch (Exception e) { 
 			return false;
 		}
    	
    	return true;
    }
    
    public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
/*    public static void main(String[] args) throws FileNotFoundException {
    	String tokenString="Rq90zueC42fabWv0tJ15MH6oDj0M6eqApU2-hK0_Rp0hZaqnTD3engPxYn1J3tMzuLZ1VxqvVcSsmInMcetuQQmB_tW2M6Wiicv_aITe4IEElLr659KczXFaOh_Fn3KbDKLhAIAQXY";
    	List<PostParam>  params = new ArrayList<PostParam>();
    	File  file = new File("D:\\checkin.jpg");
    	InputStream inputStream = new FileInputStream(file);
    	PostParam  param = new PostParam("media", "", "checkin", inputStream);
    	params.add(param);
    	String result=HttpUtils.post("https://api.weixin.qq.com/shakearound/material/add?access_token="+tokenString+"&type=icon", params, "utf-8");
    	System.out.println(result);
    }*/
}
