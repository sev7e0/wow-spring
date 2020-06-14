package com.sev7e0.wow.web.controller;

import com.sev7e0.wow.framework.annotation.WAutowired;
import com.sev7e0.wow.framework.annotation.WController;
import com.sev7e0.wow.framework.annotation.WRequestMapping;
import com.sev7e0.wow.framework.annotation.WRequestParam;
import com.sev7e0.wow.framework.webmvc.WModelAndView;
import com.sev7e0.wow.web.service.IWowService;

import java.util.HashMap;

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
public class WowController implements IWowController {

	@WAutowired
	private IWowService wowService;

	@Override
	@WRequestMapping("index.html")
	public WModelAndView getWorld(@WRequestParam("anything") String anything) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("data", wowService.getWorld(anything));
		return new WModelAndView("index.html", map);
	}

	@Override
	@WRequestMapping("getException.html")
	public WModelAndView getException() {
		wowService.getException();
		return new WModelAndView("500.html", null);
	}
}
