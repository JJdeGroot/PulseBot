package org.pulsebot.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

	public String author();
	public String name();
	public String description();
	public String category();
	public String version() default "1.0";

}
