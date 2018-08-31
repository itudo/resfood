package com.yc.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.yc.bean.JsonModel;

//使用适配器
//抽象类，即不能直接使用，将来写一个Servlet继承自BaseServlet. 
public abstract class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 3929147019319436036L;
	
	protected String op;
	protected int pages=1;
	protected int pagesize=2;
	protected HttpSession session;
	protected JsonModel jm=new JsonModel();
	
	protected int page;
	protected int rows;
	protected String sort;
	protected String order;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost( req, resp);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse arg1) throws ServletException, IOException {
		//req.setCharacterEncoding("utf-8"); // 这样所有的servlet的编码都设置好了.
		session=req.getSession();
		op=req.getParameter("op");
		if (req.getParameter("pages") != null) {
			pages = Integer.parseInt(req.getParameter("pages"));
		}
		if (req.getParameter("pagesize") != null) {
			pagesize = Integer.parseInt(req.getParameter("pagesize"));
		}
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}
		if (req.getParameter("rows") != null) {
			rows = Integer.parseInt(req.getParameter("rows"));
		}
		
		if (req.getParameter("sort") != null) {
			sort = req.getParameter("sort");
		}
		if (req.getParameter("order") != null) {
			order = req.getParameter("order");
		}
		super.service(req, arg1);   // 判断请求方式，如果是get,调用doGet(),..
	}

	// 将一个对象以json字符串的格式输出客户端
	protected void outJsonString(Object obj, HttpServletResponse response) throws IOException {
		// 利用gson将JsonModel对象转为一个json字符串
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);//    json格式:   对象  {"":"","":""}    数组: [值,值....]      混合
		outJsonString(   jsonString, response);
	}

	protected void outJsonString(  String jsonString, HttpServletResponse response) throws IOException {
		// 利用gson将JsonModel对象转为一个json字符串
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(jsonString);
		out.flush();
	}

	public Object parseParameterToT(HttpServletRequest req, Class class1) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		//将  req中的    Map<String,String[]>转成了   Map<String,String>
		Map<String,String[]> map=req.getParameterMap();
		Map<String,String> parameters=new HashMap<String,String>();
		for(   Map.Entry<String, String[]> entry: map.entrySet()) {
			parameters.put(entry.getKey(), entry.getValue()[0]);
		}
		//  parameters    uname     pwd
		List<Method> listmethods=findAllSetMethods(class1   );
		Object obj=class1.newInstance();   //   new Person();
		for(   Method m: listmethods) {   //  setuname    setpwd
			for(  Map.Entry<String, String> entry: parameters.entrySet()  ) {  // uname,    pwd
				String methodName=m.getName();   // setUname
				String pName="set"+entry.getKey();   // setuname
				if(   methodName.equalsIgnoreCase(pName)) {
					//TODO: 求出    m  这个方法中的参数类型, 强制类型转换    entry.getValue()的类型.
					Class parameterType=m.getParameterTypes()[0];
					String parameterTypeName=parameterType.getName();
					if(  "int".equals(parameterTypeName)  || "java.lang.Integer".equals(parameterTypeName)) {
						int v=Integer.parseInt(    entry.getValue());
						m.invoke(obj, v); 
					}else if(  "short".equals(parameterTypeName)  || "java.lang.Short".equals(parameterTypeName)) {
						short v=Short.parseShort(    entry.getValue());
						m.invoke(obj, v); 
					}else if(  "float".equals(parameterTypeName)  || "java.lang.Float".equals(parameterTypeName)) {
						float v=Float.parseFloat(    entry.getValue());
						m.invoke(obj, v); 
					}else if(  "double".equals(parameterTypeName)  || "java.lang.Double".equals(parameterTypeName)) {
						double v=Double.parseDouble(    entry.getValue());
						m.invoke(obj, v); 
					}else {
						//m就是要找的方法
						m.invoke(obj, entry.getValue());       //  person.setUname(   "zy" );
					}
				}
			}
		}
		return obj;
	}
	
	/**
	 * 取出所有的set方法
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Method> findAllSetMethods(  Class clz  ){
		List<Method> list=new ArrayList<Method>();
		Method[] ms=clz.getMethods();
		for(   Method m:ms) {
			if(   m.getName().startsWith("set")) {
				list.add(  m );
			}
		}
		return list;
	}

}
