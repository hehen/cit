package com.zwl.test_complier;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.util.StringUtils;
import com.zwl.test_annotation.LiveData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author zwl
 * @since 2021/6/21
 */
@AutoService(Processor.class)
public class LiveDataProcessor extends AbstractProcessor {
    private Elements elementUtils; // 操作元素的工具类
    private Filer filer;  // 用来创建文件
    private Messager messager; // 用来输出日志、错误或警告信息
    private Map<String, ClassEntity> classEntityMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
//        指定支持的Java版本
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //指定处理的注解
        return Collections.singleton(LiveData.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        classEntityMap.clear();
        //是处理注解的地方。
        for (Element element : roundEnv.getElementsAnnotatedWith(LiveData.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                handlerField((VariableElement) element); // 表示一个字段
            }
            if (element.getKind() == ElementKind.CLASS) {
                handlerClass((TypeElement) element); // 表示一个类或接口
            }
            // ExecutableElement表示某个类或接口的方法
        }
        for (Map.Entry<String, ClassEntity> item : classEntityMap.entrySet()) {
            try {
//                messager.printMessage(Diagnostic.Kind.WARNING, "ceshi qian");
                brewViewModel(item).writeTo(filer);
//                messager.printMessage(Diagnostic.Kind.WARNING, "ceshi hou");
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), item.getValue().getElement());
            }
        }
        return true;
    }

    private void handlerClass(TypeElement element) {
        ClassEntity classEntity = new ClassEntity(element);
        String className = element.getSimpleName().toString();

        if (classEntityMap.get(className) == null) {
            classEntityMap.put(className, classEntity);
        }
    }

    private void handlerField(VariableElement element) {
        FieldEntity fieldEntity = new FieldEntity(element);
        String className = fieldEntity.getClassSimpleName();
        if (classEntityMap.get(className) == null) {
            classEntityMap.put(className,
                    new ClassEntity((TypeElement) element.getEnclosingElement()));
        }
        ClassEntity classEntity = classEntityMap.get(className);
        classEntity.addFieldEntity(fieldEntity);
    }

    private JavaFile brewViewModel(Map.Entry<String, ClassEntity> item) {
        ClassEntity classEntity = item.getValue();
        LiveData liveData = classEntity.getElement().getAnnotation(LiveData.class);
        /*类名*/
        String className = classEntity.getElement().getSimpleName().toString() + "ViewModel";

        ClassName viewModelClazz = ClassName.get("androidx.lifecycle", "ViewModel");


        TypeSpec.Builder builder = TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(viewModelClazz);

        // 优先执行类LiveData注解
        if (liveData != null) {
            TypeName valueTypeName = ClassName.get(classEntity.getElement());
            brewLiveData(classEntity.getClassSimpleName(), valueTypeName, builder);
        } else {
            Map<String, FieldEntity> fields = classEntity.getFields();

            for (FieldEntity fieldEntity : fields.values()) {
                String fieldName = StringUtils.toUpperCase(fieldEntity.getElement().getSimpleName().toString());
                TypeName valueTypeName = ClassName.get(fieldEntity.getElement().asType());
                brewLiveData(fieldName, valueTypeName, builder);
            }
        }

        TypeSpec typeSpec = builder.build();
        // 指定包名
        return JavaFile.builder("com.zwl.cit.viewmodel", typeSpec).build();
    }

    private void brewLiveData(String fieldName, TypeName valueTypeName, TypeSpec.Builder builder) {

        String liveDataType;
        ClassName liveDataTypeClassName;

        liveDataType = "m$L = new MutableLiveData<>()";
        liveDataTypeClassName = ClassName.get("androidx.lifecycle", "MutableLiveData");

        ParameterizedTypeName typeName = ParameterizedTypeName.get(liveDataTypeClassName, valueTypeName);

        FieldSpec field = FieldSpec.builder(typeName, "m" + fieldName, Modifier.PRIVATE)
                .build();

        MethodSpec getMethod = MethodSpec
                .methodBuilder("get" + fieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(field.type)
                .beginControlFlow("if (m$L == null)", fieldName)
                .addStatement(liveDataType, fieldName)
                .endControlFlow()
                .addStatement("return m$L", fieldName)
                .build();

        MethodSpec getValue = MethodSpec
                .methodBuilder("get" + fieldName + "Value")
                .addModifiers(Modifier.PUBLIC)
                .returns(valueTypeName)
                .addStatement("return this.$N().getValue()", getMethod)
                .build();

        MethodSpec setMethod = MethodSpec
                .methodBuilder("set" + fieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(valueTypeName, "mValue")
                .beginControlFlow("if (this.m$L == null)", fieldName)
                .addStatement("return")
                .endControlFlow()
                .addStatement("this.m$L.setValue(mValue)", fieldName)
                .build();

        MethodSpec postMethod = MethodSpec
                .methodBuilder("post" + fieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(valueTypeName, "mValue")
                .beginControlFlow("if (this.m$L == null)", fieldName)
                .addStatement("return")
                .endControlFlow()
                .addStatement("this.m$L.postValue(mValue)", fieldName)
                .build();

        builder.addField(field)
                .addMethod(getMethod)
                .addMethod(getValue)
                .addMethod(setMethod)
                .addMethod(postMethod);

    }
}