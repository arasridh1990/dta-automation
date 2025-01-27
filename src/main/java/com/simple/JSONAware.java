package com.simple;

/**
 * Beans that support customized output of JSON text shall implement this interface.
 * @author Santhosh Karra
 */
public interface JSONAware {
	/**
	 * @return JSON text
	 */
	String toJSONString();
}
