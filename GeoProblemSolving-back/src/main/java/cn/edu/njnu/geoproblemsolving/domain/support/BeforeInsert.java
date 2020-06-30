package cn.edu.njnu.geoproblemsolving.domain.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AnnotationName BeforeInsert
 * @Description todo
 * @Author sun_liber
 * @Date 2019/7/10
 * @Version 1.0.0
 */
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface BeforeInsert {
}
