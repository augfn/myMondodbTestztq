 package com.app;
import java.io.File;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.model.Flow;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import com.service.SaveAndqueryService;
import com.util.HttpUtils;
import com.util.PropertyManager;
import com.util.Word2PdfUtil;

@SpringBootApplication
@Configuration
@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.service"})//不能凭空使用
public class APPConfig implements EmbeddedServletContainerCustomizer{
	Word2PdfUtil pdfs = new Word2PdfUtil();
	@Autowired
    private MongoTemplate mongoTemplate;
	@Autowired
	private SaveAndqueryService saveAndqueryService;
	
	 public static void main(String[] args) throws Exception {
	        SpringApplication.run(APPConfig.class, args);
	    }
	 /**
	  * 保存json字符串
	  * @param jsonTxt
	  */
    @RequestMapping(value="/saveJsonTxt",method= RequestMethod.POST)
    public boolean saveJsonTxt(@RequestBody String jsonTxt) {
			try {
				MongoClient mClient = new MongoClient("192.168.1.103",27017);
				boolean result = saveAndqueryService.saveJsonStr(mClient,jsonTxt);
				mClient.close();
				return result;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
    }
    
    
    /**
	  * 保存json字符串
	  * @param jsonTxt
	  */
   @RequestMapping(value="/flowListInfo",method= RequestMethod.GET)
   public ModelAndView flowListInfo() {
	   ModelAndView andView = new ModelAndView("hello");
	   andView.getModel().put("qqq", "fbrtjhnrt");//model本身就是Map的实现类的子类
	   //andView.addObject("www", "bhrtjhrtjjjtj");
		return andView;
   }
    /**
     * 保存json字符串
     * 实体为Flow
     */
    @RequestMapping(value="/insertBean",method = RequestMethod.POST)
    public void insertBeanData(@RequestBody String jsonStr){
    	System.out.println("===============保存  jsonStr=================");
    	saveAndqueryService.insertBeanStr(jsonStr);
    }
    @RequestMapping(value="/deleteMany",method = RequestMethod.GET)
    public void deleteManyData(@RequestBody String str,@RequestBody String condition){
    	mongoTemplate.remove(new Query(Criteria.where(condition).regex(str)),Flow.class);  
    }
    @RequestMapping(value="/updateOrSave",method = RequestMethod.POST)
    public void updateOrSave(@RequestBody String str){
    	saveAndqueryService.updateOrSave(str);
    }
    
    /**
     * 分页查询
     * @param data
     */
    @RequestMapping(value="/queryByCondition",method = RequestMethod.POST)
    public String getDataByCondition(@RequestBody String data){
    	System.out.println("===============aaaa条件查询=================");
    	//String data1 = data.substring(0, data.split(",")[0].length());//第一个是保存的参数json
    	Map<String,Object> map1 = (Map<String,Object>)JSON.parse(data);
    	String result = saveAndqueryService.queryPageBycondition(map1);
		return result;
    }
    /**
     * 查询当前记录的上一条
     * @param data
     */
    @RequestMapping(value="/getPrevious",method = RequestMethod.POST)
    public String getPrevious(@RequestBody String data){
    	System.out.println("===============aaaa条件查询=================");
    	//String data1 = data.substring(0, data.split(",")[0].length());//第一个是保存的参数json
    	Map<String,Object> map1 = (Map<String,Object>)JSON.parse(data);
    	String result = saveAndqueryService.queryPrevious(map1);
		return result;
    }
    /**
     * 查询当前记录的下一条
     * @param data
     */
    @RequestMapping(value="/getNext",method = RequestMethod.POST)
    public String getNext(@RequestBody String data){
    	System.out.println("===============aaaa条件查询=================");
    	//String data1 = data.substring(0, data.split(",")[0].length());//第一个是保存的参数json
    	Map<String,Object> map1 = (Map<String,Object>)JSON.parse(data);
    	String result = saveAndqueryService.queryPageBycondition(map1);
		return result;
    }
    /**
     * 分页查询bean
     * @param data
     */
    @RequestMapping(value="/queryBeanByCondition",method = RequestMethod.POST)
    public List<Flow> queryBeanByCondition(@RequestBody String data){
    	System.out.println("===============条件查询=================");
    	Map<String,Object> map1 = (Map<String,Object>)JSON.parse(data);
    	List<Flow> list = saveAndqueryService.queryBeanInfoBycondition(map1);
		return list;
    }
    /**
     * 集合查询
     * @param data
     */
    @RequestMapping(value="/queryDataList",method = RequestMethod.POST)
    public List<String> queryDataList(@RequestBody String data){
    	System.out.println("===============全部查询=================");
    	List<String>  list = saveAndqueryService.queryAllList(data);
    	return list;
    }
    @RequestMapping(value="/getFjMD5ById",method = RequestMethod.GET)
    public String getFjMD5ById(@PathVariable String id){
    	
    	return saveAndqueryService.checkMD5Info(id);
    }
   // @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(8025);
    }
    
    /**
     * 根据传来的路径转换为文件传回页面
     * @param data
     */
    @RequestMapping(value="/checkAttachView",method = RequestMethod.POST)
    public String getPathReturn(@RequestBody String complete){
    	//之后附件服务器的处理
    	//md5id
    	String md5id = complete.split("=")[0];
    	String filePath = complete.split("=")[1];
		File file = new File(filePath);
		boolean result = true;
		if (file.exists()) {
			String docViewPath = PropertyManager.getProperty("docViewPath",
					new String[] { "application" });
//			String docViewPath = "http://192.168.1.103:58080/word2pdfConvertServer/convert2Html.do";
			System.out.println(docViewPath);
			
			result = HttpUtils.uploadAndReadSaveFromURL(docViewPath, filePath,
					"", "1", md5id);
		} else {
			result = false;
		}
		String html = PropertyManager.getProperty("viewPath",
				new String[] { "application" });
//		String html = "http://192.168.1.103:58080/word2pdfConvertServer/attachmentviewer/";
		JSONObject json = new JSONObject();
		json.put("result", result);
		json.put("url", html + md5id + "/" + md5id + ".html");
		return json.toString();
    }
    
    /** 
    * @功能: 
    * @return void    返回类型
    * @author :ZTQ 
    * @date 2018年4月18日 下午3:50:08  
    */
    @RequestMapping(value="/officeTopdf",method= RequestMethod.GET)
    public void officeTopdf(String filepath) {
        filepath = "D:\\2009\\fw";
        Word2PdfUtil.getFiles(filepath);
    }
    
    
}
 