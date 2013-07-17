package org.pulsebot.injection.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

/**
 * Npc has
 * - Super class Actor
 * - Not a field type of PlayerDefinition
 * 
 * @author JJ
 *
 */
public class NPCAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Check if the super class is Actor
		if(node.superName.equals(classnames.get("Actor"))){
			//System.out.println(node.name + " has super class Actor");
			
			String defName = "L" + classnames.get("PlayerDefinition") + ";";
			List<FieldNode> fieldNodes = node.fields;
			for(FieldNode fieldNode : fieldNodes){
				if(fieldNode.desc.equals(defName)){ // PlayerDefinition type
					return false;
				}
			}
			return true;
		}

		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ NPC ]\n^ " + node.name + " extends " + node.superName);
		
		// NPC Interface
		node.interfaces.add("org/pulsebot/injection/interfaces/INPC");
		
		// NPC Definition is the only special type
		Pattern defPattern = Pattern.compile("L(\\w+);");
		List<FieldNode> fieldNodes = node.fields;
		for(FieldNode fieldNode : fieldNodes){
			Matcher defMatcher = defPattern.matcher(fieldNode.desc);
			if(defMatcher.find()){
				System.out.println("* getDefinition() -> " + fieldNode.desc + " " + node.name + "." + fieldNode.name);
				createNormalMethod(node, fieldNode, "getDefinition", "()Lorg/pulsebot/injection/interfaces/INPCDefinition;");
				classnames.put("NPCDefinition", defMatcher.group(1));
			}
		}
		
		classnames.put("NPC", node.name);
		classnode = node;
		return "NPC";
	}

}
