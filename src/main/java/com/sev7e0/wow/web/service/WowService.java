package com.sev7e0.wow.web.service;

import com.sev7e0.wow.framework.annotation.WService;

/**
 * Title:  WowService.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-29 14:52
 **/

@WService
public class WowService implements IWowService {

	@Override
	public String getWorld(String anything) {
		return "今天不学习，明天变辣鸡！" + "\r传参为：" + anything;
	}

	@Override
	public void getException() throws RuntimeException {
		throw new RuntimeException("抛出异常啦！");
	}
}
