package com.sev7e0.wow.web.controller;

import com.sev7e0.wow.framework.annotation.WAutowired;
import com.sev7e0.wow.framework.annotation.WController;
import com.sev7e0.wow.framework.annotation.WRequestMapping;
import com.sev7e0.wow.framework.annotation.WRequestParam;
import com.sev7e0.wow.web.service.IWowService;

/**
 * Title:  WController.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-28 23:06
 **/

@WController
@WRequestMapping(value = "/wow/")
public class WowController implements IWowController{

	@WAutowired
	private IWowService wowService;

	@Override
	@WRequestMapping("getWorld")
	public String getWorld(@WRequestParam("anything") String anything) {
		return wowService.getWorld(anything);
	}
}
