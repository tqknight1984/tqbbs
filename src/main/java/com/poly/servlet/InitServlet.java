package com.poly.servlet;

//import java.io.FileNotFoundException;
//import java.io.InputStream;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chetuan.mq.MetaQSend;
import com.poly.util.MyConfig;


//import org.apache.log4j.Logger;

//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;

//import com.sun.corba.se.spi.servicecontext.ServiceContext;

public class InitServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static ServletContext servletContext;

	public static String rootPath;

	public void init(ServletConfig config) {
		servletContext = config.getServletContext();
		rootPath = servletContext.getRealPath("/");
		System.out.println("rootPath====>" + rootPath);
		if (MetaQSend.getInstance().init(MyConfig.zk_host) == false)
    	{
    		return;
    	}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		process(request, response);
	}

	public void process(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("InitServlet.process");
	}
}
