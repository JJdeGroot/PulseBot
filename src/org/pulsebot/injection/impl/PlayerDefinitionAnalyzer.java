package org.pulsebot.injection.impl;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;

/**
 * PlayerDefinition
 * - Identified this in the PlayerAnalyzer
 * 
 * @author JJ
 */
public class PlayerDefinitionAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		return node.name.equals(classnames.get("PlayerDefinition"));
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ PlayerDefinition ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("PlayerDefinition", node.name);
		classnode = node;
		return "PlayerDefinition";
	}

}
