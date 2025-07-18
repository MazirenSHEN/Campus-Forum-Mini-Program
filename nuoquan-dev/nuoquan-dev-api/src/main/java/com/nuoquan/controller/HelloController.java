package com.nuoquan.controller;

import com.jupiter.mapper.LongarticleMapper;
import com.nuoquan.scheduler.MySchedular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.email.EmailTool;
import com.nuoquan.netty.MsgHandler;
import com.nuoquan.pojo.netty.ChatMessage;
import com.nuoquan.service.WeChatService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.JsonUtils;
import com.nuoquan.utils.RestTemplateUtil;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * 测试用例接口
 * @author jerrio
 */
@RestController
public class HelloController extends BasicController{
	
	@Autowired
	private EmailTool emailTool;
//	@Autowired
//	private LongarticleMapper longarticleMapper;
	@Autowired
	private MySchedular mySchedular;

	@GetMapping("/hello")
	public String Hello() {
		return "hello!"+resourceConfig.getName();
	}
	
	@GetMapping("/sendWsMsg")
	public void sendWsMsg() {
		// 测试主动推送消息给所有人
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setMsg("这是一条公告");
		chatMessage.setReceiverId("123");
		chatMessage.setSenderId("123");
//		chatMessage.setMsgId("123");
		
		MsgHandler.clients.writeAndFlush(
				new TextWebSocketFrame(
						JsonUtils.objectToJson(chatMessage)));

	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "toAddr", required = true, dataType = "String", paramType = "form")
		})
	@PostMapping("/sendEmail")
	public void sendEmail(String toAddr) {
		emailTool.simpleSend(toAddr);
	}
	
	@Autowired
    private WeChatService weChatService;
 
    /**
     * 获取微信access_token
     * @return
     */
    @GetMapping("/getWeChatAccessTokenUrl")
    public String getWeChatAccessTokenUrl(){
        return weChatService.getWeChatAccessToken();
    }
    
    
    public static class CheckRes {
    	private Number errcode;
    	private String errmsg;
    	
    	public Number getErrcode() {
			return errcode;
		}
		public void setErrcode(Number errcode) {
			this.errcode = errcode;
		}
		public String getErrmsg() {
			return errmsg;
		}
		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}
    }
    /**
     * 图片安全内容检测接口（微信提供）
     * @return
     */
    @ApiImplicitParams({
		@ApiImplicitParam(name = "content", required = true, dataType = "String", paramType = "form")
	})
    @PostMapping("/msgSecCheck")
    public JSONResult msgSecCheck(String content){

        class Data {
			private String content;
			
        	public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}
        }
        
        // 获取AccessToken
        String accessToken = getWeChatAccessTokenUrl();

        String requestUrl = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+accessToken;
        
        Data data = new Data();
        data.setContent(content);
        String dataString= JsonUtils.objectToJson(data);
        System.out.println(content);
        System.out.println(dataString);
        
        // 获取检测结果
        String jsonRes = RestTemplateUtil.post(requestUrl, dataString);
        CheckRes checkRes = JsonUtils.jsonToPojo(jsonRes, CheckRes.class);
        if (checkRes.getErrcode().equals(0)) {
        	return JSONResult.ok("通过");
		}else {
			return JSONResult.errorMsg("测试不通过, errcode:" + checkRes.getErrcode() + ", " + checkRes.getErrmsg());
		}
    }

	@PostMapping("/test")
	public JSONResult test() throws Exception{
		mySchedular.fetchLongarticleFromJupiter();
    	return JSONResult.ok();
	}
    
}
