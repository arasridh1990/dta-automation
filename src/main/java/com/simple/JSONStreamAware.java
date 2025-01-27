package com.simple;

import java.io.IOException;
import java.io.Writer;

/**
 * Beans that support customized output of JSON text to a writer shall implement this interface.
 * @author Santhosh Karra
 */
public interface JSONStreamAware {
	/**
	 * write JSON string to out.
	 */
	void writeJSONString(Writer out) throws IOException;
}
