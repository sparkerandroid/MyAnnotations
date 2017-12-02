package com.example.xiuli.myannotations;

import android.app.Activity;

import com.example.RuntimeBind;

import java.lang.reflect.Field;

/**
 * Created by xiuli on 2017/12/2.
 */
public class RuntimeAnnotationProcessor {
    public static void bind2(Activity activity){
        if (activity==null) return;
      Field[] fields =   activity.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) return;
        for(int i=0;i<fields.length;i++){
            if (fields[i].isAnnotationPresent(RuntimeBind.class)){
                int resId  = fields[i].getAnnotation(RuntimeBind.class).value();
                fields[i].setAccessible(true);
                try {
                    fields[i].set(activity,activity.findViewById(resId));
                    fields[i].setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    fields[i].setAccessible(false);
                }
            }
        }
    }
}
