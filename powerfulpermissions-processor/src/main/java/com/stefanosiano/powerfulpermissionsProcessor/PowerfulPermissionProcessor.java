package com.stefanosiano.powerfulpermissionsProcessor;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.stefanosiano.powerfulpermissions.Perms")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class PowerfulPermissionProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add("com.stefanosiano.powerfulpermissions.Perms");
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        StringBuilder builder = new StringBuilder()
                .append("package com.stefanosiano.powerfulpermissions;\n\n")
                .append("import java.util.Map;\n\n")
                .append("public class Permissions$PowerfulPermission {\n\n") // open class
                .append("\tpublic static void init(Map map) {\n") // open method
                .append("\t\tmap.clear();\n\n");



                        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Perms.class)) {

            if(element.getKind() != ElementKind.METHOD) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Only methods can be annotated with Perms");
                return true;
            }


            String objectType = element.getSimpleName().toString();


            builder.append("String[] permissions = " + ";\n");
            builder.append("map.put(\"" + "\", new ContextPermMapping(permissions, " + "methodName" + ", " + atomicInteger.getAndIncrement() + "));\n\n");

                    // this is appending to the return statement
            builder.append(objectType).append(" says hello!\n");
        }


        builder.append("\t\treturn;\n\n") // end return
                .append("\t}\n\n"); // close method

        //add ContextPermMapping class
        builder.append(
                "\tpublic static class ContextPermMapping {\n" +
                "\t\tString[] permissions;\n" +
                "\t\tString methodName;\n" +
                "\t\tint methodId;\n" +
                "\n" +
                "\t\tpublic ContextPermMapping(String[] permissions, String methodName, int methodId) {\n" +
                "\t\t\tthis.permissions = permissions;\n" +
                "\t\t\tthis.methodName = methodName;\n" +
                "\t\t\tthis.methodId = methodId;\n" +
                "\t\t}\n" +
                "\t}"); // close inner class

        builder.append("}\n"); // close class



        try { // write the file
            JavaFileObject source = filer.createSourceFile("com.stefanosiano.powerfulpermissions.Permissions$PowerfulPermission");


            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }


        return true;
    }
}
