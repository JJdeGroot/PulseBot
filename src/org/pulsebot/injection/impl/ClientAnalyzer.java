package org.pulsebot.injection.impl;

import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.tree.*;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.utils.GenericUtils;
import org.pulsebot.injection.utils.InsnFinder;
import org.pulsebot.injection.utils.InsnFinder2;

public class ClientAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		return node.name.equals("client");
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ Client ]\n^ " + node.name + " extends " + node.superName);
		
		// Client interface
		node.interfaces.add("org/pulsebot/injection/interfaces/IClient");
		
		// Hooking fields	
		String playerArrayName = "[L" + classnames.get("Player") + ";";
		String npcArrayName = "[L" + classnames.get("NPC") + ";";
		List<FieldNode> fieldNodes = node.fields;
		for(FieldNode fieldNode : fieldNodes){
			if(fieldNode.desc.equals(playerArrayName)){ // getPlayers()
				System.out.println("* getPlayers() -> " + fieldNode.desc + " " + node.name + "."  + fieldNode.name);
				createStaticMethod(node, fieldNode, "getPlayers", "()[Lorg/pulsebot/injection/interfaces/IPlayer;");
			}else if(fieldNode.desc.equals(npcArrayName)){ // getNPCs()
				System.out.println("* getNPCs() -> " + fieldNode.desc + " " + node.name + "." + fieldNode.name);
				createStaticMethod(node, fieldNode, "getNPCs", "()[Lorg/pulsebot/injection/interfaces/INPC;");
			}else if(fieldNode.desc.equals("I")){ // int
				
			}
		}
		
		// getYaw() regex
		// GETSTATIC{[I,cr,av} GETSTATIC{I,dq,fw} LDC{-1896216087} IMUL IALOAD ISTORE_6 (two times)
		String regex = "(GETSTATIC\\{\\[I,\\w+,\\w+\\} GETSTATIC\\{I,(\\w+),(\\w+)\\} LDC\\{((.*?))\\} IMUL IALOAD ISTORE_\\d+ ){2}";
		Pattern yawPattern = Pattern.compile(regex);
				
		// Looping through all methods
		List<MethodNode> methodNodes = node.methods;
		for(int i =0; i<methodNodes.size();i++){
            MethodNode methodNode = methodNodes.get(i);
			InsnFinder2 insnFinder2 = new InsnFinder2(methodNode);
			String insn = insnFinder2.getInsnString();
			//System.out.println("Insn for method " + methodNode.name + ":\t"/* + insn*/);

			Matcher yawMatcher = yawPattern.matcher(insn);
			while(yawMatcher.find()){
				/*
				System.out.println("\nRegex match with method: " + methodNode.name);
				for(int i = 0; i < m.groupCount(); i++){
					System.out.println("Group #" + i + ": " + m.group(i));
				}
				*/
				System.out.println("* getYaw() -> " + yawMatcher.group(2) + "." + yawMatcher.group(3) + " * " + yawMatcher.group(4));
                ListIterator<AbstractInsnNode> abIt = methodNode.instructions.iterator();
                while(abIt.hasNext()){
                    AbstractInsnNode ab = abIt.next();
                    if(ab instanceof FieldInsnNode){
                        if(((FieldInsnNode) ab).name.equals(yawMatcher.group(3))){
                            GenericUtils.addMethod(node,yawMatcher.group(2),yawMatcher.group(3),((FieldInsnNode) ab).desc,"getCameraYaw","()I",Integer.parseInt(yawMatcher.group(4)),true);
                            break;
                        }
                    }
                }
			}
		}
		
		classnames.put("Client", node.name);
		classnode = node;
		return "Client";
	}

}
