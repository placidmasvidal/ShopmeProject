package com.shopme.admin.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer model,
                                  NativeWebRequest request,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        String keyword = request.getParameter("keyword");
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        PagingAndSortingParam annotation = methodParameter.getParameterAnnotation(PagingAndSortingParam.class);

    return new PagingAndSortingHelper(model, annotation.moduleURL(), annotation.listName(), sortField, sortDir, keyword);
    }
}
