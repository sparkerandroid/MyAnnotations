package com.example.xiuli.myannotations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.Bind;
import com.example.RuntimeBind;
import com.example.annotation_api.Injector;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_annotationtest)
    TextView tv_annotationtest;
    @RuntimeBind(R.id.tv_runtime)
    TextView tv_runtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Injector.bind(this);
        tv_annotationtest.setText("编译型注解成功");
        RuntimeAnnotationProcessor.bind2(this);
        tv_runtime.setText("运行时注解成功");
    }
}
