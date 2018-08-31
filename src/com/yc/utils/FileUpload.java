package com.yc.utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import com.jspsmart.upload.Files;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public class FileUpload {
	private long singleSize = 50 * 1024 * 1024;

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Map<String, String> uploadFiles(PageContext pageContext, HttpServletRequest request)
			throws SmartUploadException, IOException, ServletException, SQLException {
		Map<String, String> map = new HashMap<String, String>();

		SmartUpload su = new SmartUpload();
		su.initialize(pageContext); // 初始化
		su.setCharset("utf-8");
		// 定义允许上传文件类型
		su.setAllowedFilesList("gif,jpg,jpeg,png");
		// 不允许上传文件类型
		su.setDeniedFilesList("jsp,asp,php,aspx,html,htm,exe,bat,sh");
		// 单个文件最大限制
		su.setMaxFileSize(singleSize);
		// 所有上传文件的总容量限制
		su.setTotalMaxFileSize(5 * singleSize);
		su.upload();
		
		// 取参数 Request是smartupload的request -> HttpServletRequest
		Request re = su.getRequest();
		Enumeration<String> enu = re.getParameterNames();
		while (enu.hasMoreElements()) {
			String pn = enu.nextElement();
			map.put(pn, re.getParameter(pn));
		}
		// 取出上传的文件列表
		Files files = su.getFiles();
		if(files!=null&&files.getCount()>0){
			for(int i=0;i<files.getCount();i++){
			// 只取列表中的第一个文件 , 写全路径，防止系统以为是 java.io.File类
			com.jspsmart.upload.File file = files.getFile(i);
			// 取tomcat路径
			Calendar c = Calendar.getInstance();
			String tomcatdir = request.getRealPath("/"); // E:\tomcat\webapps/JavaEE_onlineorder
			File tomcatFile = new File(tomcatdir);
			File webapppath = tomcatFile.getParentFile(); // E:\tomcat\webapps

			// E:\tomcat\webapps/pic/2017/7
			File picpath = new File(webapppath, "pic" + File.separator + c.get(Calendar.YEAR) + File.separator
				+ (c.get(Calendar.MONTH) + 1) + File.separator);
			// 访问路径名
			String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
			String weburl = basePath+"/pic/" + c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/";
			// 判断目录是否存在，不在则创建
			if (picpath.exists() == false) {
				picpath.mkdirs();
			}
			String filePrefixName = genNewFilePrefixName();
			// 取出原文件的后缀名
			String extName = file.getFileExt();
			// 拼接新的文件名
			String fileName = filePrefixName + "." + extName;

			weburl += fileName;
			// 物理路径: xxx/xxx/webapps/pic/2017/7/20170720222222.png
			String destFilePathName = picpath + "/" + fileName;
			// 存
			file.saveAs(destFilePathName, SmartUpload.SAVE_PHYSICAL);
			//取出上传文件的域名
			String fieldName=file.getFieldName();
			map.put("weburl_"+fieldName, weburl);
			map.put("destFilePathName_"+fieldName, destFilePathName);
			}
		}
		return map;
		
	}

	
	private String genNewFilePrefixName() {
		// 生成新的文件名
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String filePrefixName = sdf.format(d); // 文件的前缀名
		return filePrefixName;
	}
}
