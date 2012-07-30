package com.horsefire.tiddly.local;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import junit.framework.TestCase;

import com.beust.jcommander.Parameter;

public class CommandLineArgsTest extends TestCase {

	public void testNoFinalArgs() {
		for (Field field : CommandLineArgs.class.getDeclaredFields()) {
			if (Modifier.isFinal(field.getModifiers())) {
				Parameter param = field.getAnnotation(Parameter.class);
				if (param != null) {
					if (param.names() != null && param.names().length > 0) {
						fail("Cannot have a final parameter field: "
								+ field.getName());
					}
				}
			}
		}
	}
}
