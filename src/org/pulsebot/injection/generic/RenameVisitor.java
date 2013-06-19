package org.pulsebot.injection.generic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 6/9/13
 * Time: 8:23 AM
 */
public class RenameVisitor extends ClassVisitor {
    private String newName, oldName;


    public RenameVisitor(String newName, String oldName) {
        super(Opcodes.ASM4);
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        if (name.contains(oldName)) {
            System.out.println(newName);
            return cv.visitMethod(access, newName, desc, signature, exceptions);
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);
    }
}
