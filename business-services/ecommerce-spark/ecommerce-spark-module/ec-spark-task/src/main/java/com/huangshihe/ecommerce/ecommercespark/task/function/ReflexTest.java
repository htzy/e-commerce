package com.huangshihe.ecommerce.ecommercespark.task.function;

import com.huangshihe.ecommerce.common.kits.ClassKit;
import com.huangshihe.ecommerce.ecommercespark.pipeline.MyStage;
import org.apache.spark.ml.PipelineModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * demo
 * <p>
 * Create Date: 2018-06-07 20:50
 *
 * @author huangshihe
 */
public class ReflexTest {

    private static void simpleWay() {
        Class<PipelineModel> clazz = PipelineModel.class;
        Constructor<PipelineModel> c0;

        try {
            c0 = clazz.getConstructor(String.class, List.class);

            System.out.println(Arrays.toString(clazz.getConstructors()));

            c0.setAccessible(true);

            PipelineModel model = c0.newInstance("123", Collections.singletonList(new MyStage()));
            System.out.println("uid:" + model.uid());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PipelineModel model = ClassKit.newInstance(PipelineModel.class, "345", Collections.singletonList(new MyStage()));
        System.out.println("uid:" + model.uid());
    }

}
