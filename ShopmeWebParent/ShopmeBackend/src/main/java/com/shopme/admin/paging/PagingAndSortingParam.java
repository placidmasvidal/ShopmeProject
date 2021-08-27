package com.shopme.admin.paging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PagingAndSortingParam {

    String moduleURL();

    String listName();
}
