package com.example.demo.com.seafile.controllerTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.henu.seafile.microservice.file.controller.ResponseForSomethingController;

/** 
 * 验证controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResponseForSomeThingControllerTest {
	
	/*
	 * 1、mockMvc.perform执行一个请求。
	 * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
	 * 3、ResultActions.param添加请求传值
	 * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
	 * 5、ResultActions.andExpect添加执行完成后的断言。
	 * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情
	 *   比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
	 * 5、ResultActions.andReturn表示执行完成后返回相应的结果。
	 * 
	 * 教程：https://blog.csdn.net/wo541075754/article/details/88983708
	 */

	private MockMvc mockMvc;
	@Before
    public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(new ResponseForSomethingController()).build();
    }
	
	
    @Test
    public void getHello() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders.get("/ping").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
