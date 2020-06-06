package com.sev7e0.wow.framework.aop.support;

/**
 * Title:  WAopConfig.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:13
 **/

public class WAopConfig {

	private String pointCut;
	private String aspectBefore;
	private String aspectAfter;
	private String aspectClass;
	private String aspectThrow;
	private String aspectAfterThrowName;


	public String getPointCut() {
		return pointCut;
	}

	public void setPointCut(String pointCut) {
		this.pointCut = pointCut;
	}

	public String getAspectBefore() {
		return aspectBefore;
	}

	public void setAspectBefore(String aspectBefore) {
		this.aspectBefore = aspectBefore;
	}

	public String getAspectAfter() {
		return aspectAfter;
	}

	public void setAspectAfter(String aspectAfter) {
		this.aspectAfter = aspectAfter;
	}

	public String getAspectClass() {
		return aspectClass;
	}

	public void setAspectClass(String aspectClass) {
		this.aspectClass = aspectClass;
	}

	public String getAspectThrow() {
		return aspectThrow;
	}

	public void setAspectThrow(String aspectThrow) {
		this.aspectThrow = aspectThrow;
	}

	public String getAspectAfterThrowName() {
		return aspectAfterThrowName;
	}

	public void setAspectAfterThrowName(String aspectAfterThrowName) {
		this.aspectAfterThrowName = aspectAfterThrowName;
	}
}
