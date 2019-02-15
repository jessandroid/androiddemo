package com.qihoo.kidswatch.route_compiler;

import com.google.auto.service.AutoService;
import com.qihoo.kidswatch.route_annotation.Route;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.omg.IOP.ExceptionDetailMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(Route.class);
        try {
            TypeSpec typeSpec = processRouterTable(routeElements);
            if(typeSpec != null){
                JavaFile.builder("com.example.dengquan.demoqw",typeSpec).build().writeTo(mFiler);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    private TypeSpec processRouterTable(Set<? extends Element> elements) {
        if (elements == null || elements.size() == 0) {
            return null;
        }
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(HashMap.class),ClassName.get(String.class),ClassName.get(Class.class));
        ParameterSpec mapParamterSpec = ParameterSpec.builder(mapTypeName,"activityMap").build();
        MethodSpec.Builder routerInitBuilder = MethodSpec.methodBuilder("initActivityMap")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(mapParamterSpec);

        for (Element element : elements){
            Route route = element.getAnnotation(Route.class);
            String[] routerUrls = route.value();
            for (String url : routerUrls){
                routerInitBuilder.addStatement("activityMap.put($S,$T.class)",url,ClassName.get((TypeElement) element));
            }
        }

        return TypeSpec.classBuilder("AutoCreateModuleActivityMap_app")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(routerInitBuilder.build())
                .build();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(Route.class.getCanonicalName());
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
