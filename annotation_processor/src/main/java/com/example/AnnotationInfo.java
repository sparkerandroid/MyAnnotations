package com.example;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by xiuli on 2017/12/2.
 */
public class AnnotationInfo {

    public Map<Integer,Element> bindData;
    public PackageElement packageEle;
    public TypeElement classEle;

    public AnnotationInfo(PackageElement packageName,TypeElement className){
        this.packageEle = packageName;
        this.classEle = className;
        bindData = new HashMap<>();
    }
}
