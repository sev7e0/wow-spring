package com.sev7e0.wow.web.controller;

import com.sev7e0.wow.framework.webmvc.WModelAndView;

/**
 * Title:  IWowController.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-29 15:05
 **/

public interface IWowController {

	WModelAndView getWorld(String anything);

	WModelAndView getException();

}
