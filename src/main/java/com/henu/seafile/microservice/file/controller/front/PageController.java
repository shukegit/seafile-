package com.henu.seafile.microservice.file.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/page")
public class PageController {

	@RequestMapping(value = "/login")
	private String login() {
		return "login";
	}
	@RequestMapping(value = "/loginbefore")
	private String loginBefore() {
		return "loginBefore";
	}
	
	@RequestMapping(value = "/relogin")
	private String reLogin() {
		return "relogin";
	}
	
	@RequestMapping(value = "/upload")
	private String upload() {
		return "upload";
	}
	
	@RequestMapping(value = "/main")
	private String main() {
		return "main";
	}
	
	
	@RequestMapping(value = "/login2")
	private String login2() {
		return "login2";
	}
	
	@RequestMapping("/websocket")
    public String webSocket(){
        
        return "mywebsocket";
	}

}
