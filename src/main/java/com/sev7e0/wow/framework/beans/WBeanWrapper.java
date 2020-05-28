package com.sev7e0.wow.framework.beans;

/**
 * Title:  WBeanWrapper.java
 * description:
 * {@link WBeanWrapper}用于保存实例化后的对象，这其中包括代理类型和原生类型
 * <p>
 * 不同于{@link com.sev7e0.wow.framework.beans.config}，
 * 其用来保存实例化之前的bean相关信息，实例化之后交由{@link WBeanWrapper}保存。
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:45
 **/

public class WBeanWrapper {

	//被保存的实例对象
	private Object wrappedInstance;

	//被保存的实例类型
	private Class<?> wrappedClass;


	public Object getWrappedInstance() {
		return wrappedInstance;
	}

	public void setWrappedInstance(Object wrappedInstance) {
		this.wrappedInstance = wrappedInstance;
		this.wrappedClass = wrappedInstance.getClass();
	}

	/**
	 * @return 可能是Class，也可能是$Proxy0
	 */
	public Class<?> getWrappedClass() {
		return this.wrappedInstance.getClass();
	}

}
