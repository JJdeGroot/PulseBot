package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.utils.GenericUtils;

public class CanvasAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		return node.superName.equals("java/awt/Canvas");
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ Canvas ]\n^ " + node.name + " extends " + node.superName);
		
		// Replacing super class
		GenericUtils.setSuper(node, "org/pulsebot/injection/generic/RSCanvas");
		System.out.println("+ replaced Canvas with RSCanvas");

		classnames.put("Canvas", node.name);
		classnode = node;
		return "Canvas";
	}

}
