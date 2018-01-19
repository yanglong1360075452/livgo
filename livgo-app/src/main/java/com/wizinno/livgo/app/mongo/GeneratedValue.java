package com.wizinno.livgo.app.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LiuMei on 2017-05-08.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface GeneratedValue {
}
