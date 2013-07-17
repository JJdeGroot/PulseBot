package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;

/**
 * NPCDefinition
 * - Identified this in the NPCAnalyzer
 * 
 * @author JJ
 *
 */
public class NPCDefinitionAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		return node.name.equals(classnames.get("NPCDefinition"));
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ NPCDefinition ]\n^ " + node.name + " extends " + node.superName);
		
		// NPC Definition interface
		node.interfaces.add("org/pulsebot/injection/interfaces/INPCDefinition");

		// Field hooks		
		List<FieldNode> fieldNodes = node.fields;
		for(FieldNode fieldNode : fieldNodes){
			if(fieldNode.desc.equals("Ljava/lang/String;")){
				System.out.println("* getName() -> " + fieldNode.desc + " " + node.name + "." + fieldNode.name);
				createNormalMethod(node, fieldNode, "getName", "()Ljava/lang/String;");
			}
		}
		
		classnames.put("NPCDefinition", node.name);
		classnode = node;
		return "NPCDefinition";
	}

}
