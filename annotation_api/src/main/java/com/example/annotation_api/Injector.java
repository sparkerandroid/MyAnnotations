package com.example.annotation_api;

import android.app.Activity;

/**
 * Created by xiuli on 2017/12/2.
 */
public class Injector {
    private static String suffix = "$$Injector";
    public static void bind(Activity activity){
        if (activity==null)
            return;
        StringBuilder sb  = new StringBuilder();
        sb.append(activity.getClass().getCanonicalName());
        sb.append(suffix);
        //查找注解生成的Java文件是否存在
        if(!findAnnotationGenerateJavaFile(sb.toString()))
            return;
        try {
            //通过接口调用Java 类的绑定方法inject，将注解的值绑定到字段
            IInject inject = (IInject) Class.forName(sb.toString()).newInstance();
            inject.inject(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static boolean findAnnotationGenerateJavaFile(String fullName){
        try {
            Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        return false;
        }
        return true;
    }
}
