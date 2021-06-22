package com.zwl.javapoetdemo;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Date;

import javax.lang.model.element.Modifier;

public class MyClass {
    public static void main(String[] args) {
//    创建字段（属性）
// private final String android = "Android";
        FieldSpec.builder(String.class, "android")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", "Android")
                .build();

//    创建方法
/* public void test(String str) {
 System.out.println(str);
 }
 */
        MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "str")
                .addStatement("System.out.println(str)")
                .returns(void.class)
                .build();

//    创建类、接口或枚举
        // class Test {}
        TypeSpec.classBuilder("Test").build();

//interface Test {}
        TypeSpec.interfaceBuilder("Test").build();
/*
enum Test {
    ONE
}*/
        TypeSpec.enumBuilder("Test").addEnumConstant("ONE").build();

//            输出文件
        /*package com.zwl.javapoetdemo;

import java.lang.String;

class Test {
  private final String android = "Android";

  public void test(String str) {
    System.out.println(str);
  }
}*/
        FieldSpec fieldSpec = FieldSpec.builder(String.class, "android")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", "Android")
                .build();

        MethodSpec methodSpec = MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "str")
                .returns(void.class)
                .addStatement("System.out.println(str)")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("Test")
                .addField(fieldSpec)
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder("com.zwl.javapoetdemo", typeSpec).build();

        System.out.println(javaFile.toString());


//        实现一个for循环：
        MethodSpec.methodBuilder("main")
                .addCode(""
                        + "int total = 0;\n"
                        + "for (int i = 0; i < 10; i++) {\n"
                        + "  total += i;\n"
                        + "}\n")
                .build();

        MethodSpec.methodBuilder("main")
                .addStatement("int total = 0")
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement("total += i")
                .endControlFlow()
                .build();

        // do... while
        MethodSpec.methodBuilder("main")
                .beginControlFlow("do")
                .endControlFlow("while (true)");

// if... else if... else...
        MethodSpec.methodBuilder("main")
                .beginControlFlow("if (true)")
                .nextControlFlow("else if (false)")
                .nextControlFlow("else")
                .endControlFlow();

// try... catch... finally
        MethodSpec.methodBuilder("main")
                .beginControlFlow("try")
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("e.printStackTrace()")
                .nextControlFlow("finally")
                .endControlFlow();
    }

    /*$S （String）
当代码中包含字符串的时候, 可以使用 $S 表示。*/
    private static MethodSpec whatsMyName(String name) {
        return MethodSpec.methodBuilder(name)
                .returns(String.class)
                .addStatement("return $S", name)
                .build();
    }

    /*$L （Literal）
$L 是字面量替换，它与$S相似，但是它并不需要转义，也就是不包含字符串的引号。*/
    private MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 0")
                .beginControlFlow("for (int i = $L; i < $L; i++)", from, to)
                .addStatement("result = result $L i", op)
                .endControlFlow()
                .addStatement("return result")
                .build();
    }

    /*$T （Type）
     上面例子为了简单，都使用的是一些基础类型，为的是不需要导包。实际中我们需要使用大量对象，如果只是在字符串中写死，代码虽没有问题，但是没有导包还是会保错。这是可以考虑使用$T，它的作用是替换类型。*/
    MethodSpec today = MethodSpec.methodBuilder("today")
            .returns(Date.class)
            .addStatement("return new $T()", Date.class)
            .build();

    /*$N （ Name）
$N是名称替换。例如我们定义了一个getXXX的方法，我们调用它时可以使用addStatement("get$L()", "XXX")
这种写法实现，但是每次拼接"get"未免太麻烦了，一个不留心说不定还忘记了。那么使用addStatement("$N()", methodSpec)就更加方便了。*/
//    MethodSpec methodSpec = MethodSpec.methodBuilder("get" + name)
//            .returns(String.class)
//            .addStatement("return $S", name)
//            .build();
//
//MethodSpec.methodBuilder("getValue")
//        .returns(String.class)
//    .addStatement("return $N()", methodSpec)
//    .build();


//    继承与接口
//TypeSpec.classBuilder("Test").
//    superclass(String .class)
//    .
//
//    addSuperinterface(Serializable .class)
//    .
//
//    build();
//
////class Test extends String implements Serializable {}
//
//    泛型
//FieldSpec.builder(TypeVariableName.get("T"),"mT",Modifier.PRIVATE).
//
//    build();
//// private T mT;
//
//    TypeVariableName mTypeVariable = TypeVariableName.get("T");
//    ParameterizedTypeName mListTypeName = ParameterizedTypeName.get(ClassName.get(List.class), mTypeVariable);
//    FieldSpec fieldSpec = FieldSpec.builder(mListTypeName, "mList", Modifier.PRIVATE).build();
//
////private List<T> mList;
//
////    方法和类中使用addTypeVariable添加泛型。
////
////    初始化块
//TypeSpec.classBuilder("Test")
//        .addStaticBlock(CodeBlock.builder().
//    build())
//            .
//
//    addInitializerBlock(CodeBlock.builder().
//
//    build())
//            .
//
//    build();

/*
class Test {
    static {
    }

    {
    }
}*/

//    构造方法
    MethodSpec methodSpec = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .build();

    TypeSpec typeSpec = TypeSpec.classBuilder("Test")
            .addMethod(methodSpec)
            .build();

/*
class Test {
    public Test() {
    }
}*/


//    Annotations
//    添加注解的方法可以直接使用addAnnotation。

    MethodSpec toString = MethodSpec.methodBuilder("toString")
            .addAnnotation(Override.class)
            .returns(String.class)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return $S", "Hoverboard")
            .build();

//    如果需要给注解设置属性，那么需要使用AnnotationSpec :

//            AnnotationSpec.Builder(Headers.class).addMember("accept", "$S", "application/json; charset=utf-8")
//    .addMember("userAgent", "$S", "Square Cash")
//    .build();

/*@Headers(
    accept = "application/json; charset=utf-8",
    userAgent = "Square Cash"
)*/

//    匿名内部类
//TypeSpec.anonymousClassBuilder("") // <- 也可添加参数
//        .superclass(Runnable.class)
//    .addMethod(MethodSpec.methodBuilder("run")
//    			   .addModifiers(Modifier.PUBLIC)
//                   .addAnnotation(Override.class)
//                   .returns(TypeName.VOID)
//                   .build())
//            .build();

/*
new Runnable() {
    @Override
    public void run() {
    }
}*/
}