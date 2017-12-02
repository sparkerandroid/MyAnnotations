package com.example;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

/**
 * Created by xiuli on 2017/12/2.
 */
@SupportedAnnotationTypes("com.example.Bind")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class BindAnnotationProcessor extends AbstractProcessor {

    private Elements elementsUtils;
    private Filer fileUtil;
    private Map<String, AnnotationInfo> annotationInfoMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementsUtils = processingEnv.getElementUtils();
        fileUtil = processingEnv.getFiler();
        annotationInfoMap = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> eleSet = roundEnv.getElementsAnnotatedWith(Bind.class);
        for (Element ele : eleSet) {
            if (!checkIsField(ele)) {
                System.out.println("non filed is error inject ");
                continue;
            }
            if (checkIsPrivate(ele)) {
                System.out.println("filed cant not be private ");
                continue;
            }
            VariableElement variableElement = (VariableElement) ele;
            // full class name
            String className = ((TypeElement) variableElement.getEnclosingElement()).getQualifiedName().toString();
            AnnotationInfo annotationInfo = new AnnotationInfo(elementsUtils.getPackageOf(variableElement), (TypeElement) variableElement.getEnclosingElement());
            int value = variableElement.getAnnotation(Bind.class).value();
            annotationInfo.bindData.put(value, variableElement);

            annotationInfoMap.put(className, annotationInfo);
        }
        for (String key : annotationInfoMap.keySet()) {
            AnnotationInfo annotationInfo = annotationInfoMap.get(key);
            JavaFileObject sourceFile = null;
            try {
                sourceFile = fileUtil.createSourceFile(annotationInfo.classEle.getSimpleName().toString() + "$$Injector");
                Writer writer = sourceFile.openWriter();
                writer.write(generateJavaSourceFile(annotationInfo));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkIsPrivate(Element ele) {
        if (ele.getModifiers().contains(Modifier.PRIVATE))
            return true;
        return false;
    }

    private boolean checkIsField(Element ele) {
        if (ele.getKind().isField())
            return true;
        return false;
    }

    public String generateJavaSourceFile(AnnotationInfo annotationInfo) {

        StringBuilder sb = new StringBuilder();
        sb.append("//auto generate do not modify\n");
        sb.append("package " + annotationInfo.packageEle.getQualifiedName().toString() + ";\n")
                .append("import android.app.Activity;\n")
                .append("import com.example.annotation_api.IInject;\n")
                .append("public class " + annotationInfo.classEle.getSimpleName().toString() + "$$Injector")
                .append(" implements IInject<" + annotationInfo.classEle.getQualifiedName() + "> {")
                .append("@Override\n")
                .append("public void inject(" + annotationInfo.classEle.getQualifiedName() + " activity) {");
        for (int key : annotationInfo.bindData.keySet()) {
            Element ele = annotationInfo.bindData.get(key);
            sb.append("activity." + ele.getSimpleName() + " = (" + ele.asType() + ")activity.findViewById(" + key + ");\n");
        }
        sb.append("}}\n");
        return sb.toString();
    }
}
