package org.tangerine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.tangerine.Constant.MSGType;

@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RuoteMapping {

	public String value();
	public byte type() default MSGType.MSG_REQUEST;
}
