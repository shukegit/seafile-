package com.henu.seafile.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.henu.seafile.common.ResponseCode;
import com.henu.seafile.microservice.user.dao.UserDao;
import com.henu.seafile.microservice.user.pojo.User;



@Component
@WebFilter(urlPatterns = "/*", filterName = "tokenAuthorFilter")
public class MyFilterUtil implements Filter{
	
	@Autowired
	private static UserDao userDao;
	
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("\n**************过滤器调用开始****************");
		System.out.printf("过滤器得到url:");
		System.out.println(((HttpServletRequest) request).getRequestURI());
		
		
		
		
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        //设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        rep.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的访问方法
        rep.setHeader("Access-Control-Allow-Methods","POST, GET, PUT, OPTIONS, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        rep.setHeader("Access-Control-Max-Age", "3600");
        rep.setHeader("Access-Control-Allow-Headers","token,Origin, X-Requested-With, Content-Type, Accept");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
//        response.setContentType("multipart/form-data");
        
        //取消对登录登出的过滤
        final List<String> ALLOWED_PATHS = new ArrayList<>();
        ALLOWED_PATHS.add("/page/login");
        ALLOWED_PATHS.add("/user/login");
        ALLOWED_PATHS.add("/js/login.js");
        ALLOWED_PATHS.add("/js/upload.js");
        ALLOWED_PATHS.add("/js/jquery-3.2.1.js");
        ALLOWED_PATHS.add("/page/logout");
//        ALLOWED_PATHS.add("/page/upload");
//        if(req.getSession().getAttribute("token") != null) {
//        	ALLOWED_PATHS.add("/page/upload");
//        }
     
        String path = ((HttpServletRequest) request).getRequestURI();
        boolean allowedPath = ALLOWED_PATHS.contains(path);
        System.out.println(path + " " + allowedPath +"(false过滤，ture通过)");
        //如果是登录登出的接口，则不需要处理
        if(allowedPath) {
        	System.out.println("这里是不需要处理的url进入的方法:" + path);
            chain.doFilter(req, rep);
        } else {
        	//不是登录登出的接口，则进行处理
        	
        	
        	String token = req.getHeader("Authorization");//header方式
        	if(token == null) {
        		token = (String)req.getSession().getAttribute("token");
        	}
        	System.out.println("得到的token:" + token);
            ResultInfo resultInfo = new ResultInfo();
            boolean isFilter = false;

            String method = ((HttpServletRequest) request).getMethod();
            if (method.equals("OPTIONS")) {
                rep.setStatus(HttpServletResponse.SC_OK);
            }else{


                if (null == token || token.isEmpty()) {
                    resultInfo.setCode(ResponseCode.ERROR.getCode());
                    resultInfo.setMessage("用户授权认证没有通过!客户端请求参数中无token信息");
                    rep.sendRedirect("/page/login");
                    System.out.println("用户授权认证没有通过!客户端请求参数中无token信息");
                } else {
//                    if (TokenUtil.volidateToken(token)) {
//                        resultInfo.setCode(ResponseCode.ERROR.getCode());
//                        resultInfo.setMsg("用户授权认证通过!");
//                        isFilter = true;
//                    } else {
//                        resultInfo.setCode(ResponseCode.ERROR.getCode());
//                        resultInfo.setMsg("用户授权认证没有通过!客户端请求参数token信息无效");
//                    }
                	
                	resultInfo = TokenUtil.volidateToken(req, token);
                	if(resultInfo.getCode() == ResponseCode.SUCCESS.getCode()) {
                		isFilter = true;
                	}
                }
                if (resultInfo.getCode() == ResponseCode.ERROR.getCode()) {// 验证失败
                    PrintWriter writer = null;
                    OutputStreamWriter osw = null;
                    try {
                        osw = new OutputStreamWriter(response.getOutputStream(),
                                "UTF-8");
                        writer = new PrintWriter(osw, true);
                        String jsonStr = JSON.toJSONString(resultInfo);
                        writer.write(jsonStr);
                        writer.flush();
                        writer.close();
                        osw.close();
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("过滤器返回信息失败:" + e.getMessage());
                    } catch (IOException e) {
                    	System.out.println("过滤器返回信息失败:" + e.getMessage());
                    } finally {
                        if (null != writer) {
                            writer.close();
                        }
                        if (null != osw) {
                            osw.close();
                        }
                    }
                    return;
                }

                if (isFilter) {
                	
                	req.getSession().setAttribute("token", token);
                	
                	System.out.println("token filter过滤ok!");
                chain.doFilter(request, response);
                }
            }	
        }

		System.out.println("**************过滤器调用结束****************\n");
        
	}
	
	
	
	private class ResultInfo {
		private int code;
		private String message;
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		
	}
	
	private static class TokenUtil {
		
		
		/**
		 * 区别是管理员操作还是用户操作，或者其他非法操作
		 * @param request
		 * @param token
		 * @return
		 */
		@SuppressWarnings("unused")
		protected static ResultInfo volidateToken(HttpServletRequest request, String token) {		
			
			MyFilterUtil myFilterUtil = new MyFilterUtil();
			ResultInfo resultInfo = myFilterUtil.new ResultInfo();
			
			//首先区别是管理员还是用户
			String admin = (String)request.getSession().getAttribute("admin");
			System.out.println("admin:" + admin);
			if(admin != null) {
				//*************
				//***管理员操作***
				//*************
				if(token.equals(request.getSession().getAttribute("token"))) { //检查和登录的时候保存的token一样不一样
					resultInfo.setCode(ResponseCode.SUCCESS.getCode());
					resultInfo.setMessage("用户授权认证通过!");	
				} else {//token是错的
					resultInfo.setCode(ResponseCode.ERROR.getCode());
					resultInfo.setMessage("用户授权认证没有通过!信息不正确");
				}
				System.out.printf("管理员： ");
			} else {
				//*************
				//**非管理员操作***
				//*************
				
				//反射注入dao层
				BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
			    userDao = (UserDao) factory.getBean("userDao");
			    
			    //数据库查询token，找到说明是用户，找不到说明是其它非法请求
			    List<User> users = userDao.selectUserByToken(token);
			    //在数据库中找到了，说明是用户
			    if(users.size() > 0) {
			    	//判断数据表中的字段，看是否被管理员授权了,如果授权了，则可以一下的操作
					if(users.get(0).isUseful()) {
						//检查和登录的时候保存的token一样不一样
						//这边可能的情况是，别人拿到了用户token，然后没有经过登录就调用接口，所以验证它有权限以后要经过用户调用
						if(token.equals(request.getSession().getAttribute("token"))) { 
							resultInfo.setCode(ResponseCode.SUCCESS.getCode());
							resultInfo.setMessage("用户授权认证通过!");	
						} else {//session中没有token,没登录的请求
							resultInfo.setCode(ResponseCode.ERROR.getCode());
							resultInfo.setMessage("用户授权认证没有通过!未登录的请求");
						}				
					} else {
						resultInfo.setCode(ResponseCode.ERROR.getCode());
						resultInfo.setMessage("用户授权认证没有通过!请联系管理员添加授权认证");
					}
				} else {
					//数据表中没找到，不是用户
					resultInfo.setCode(ResponseCode.ERROR.getCode());
					resultInfo.setMessage("用户授权认证没有通过!信息不正确");
				} 
			    System.out.println("users.size(数据库中通过token找用户):" + users.size());
			    System.out.printf("用户： ");
			}
			System.out.println(resultInfo.getMessage());
			return resultInfo;
		}
		
		
		
	}

}
