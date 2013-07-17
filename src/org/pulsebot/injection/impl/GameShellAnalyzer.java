package org.pulsebot.injection.impl;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

public class GameShellAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		return node.name.equals(RSClient.clientAnalyzer.classnode.superName);
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ GameShell ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("GameShell", node.name);
		classnode = node;
		return "GameShell";
	}

}
