package com.shopme.admin.paging;

import com.shopme.admin.user.UserConstants;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class PagingAndSortingHelper {

    private ModelAndViewContainer model;
    private String moduleURL;
    private String listName;
    private String sortField;
    private String sortDir;
    private String keyword;

    public PagingAndSortingHelper(ModelAndViewContainer model, String moduleURL, String listName, String sortField, String sortDir, String keyword) {
        this.model = model;
        this.moduleURL = moduleURL;
        this.listName = listName;
        this.sortField = sortField;
        this.sortDir = sortDir;
        this.keyword = keyword;
    }

    public void updateModelAttributes(int pageNum, Page<?> page){
        List<?> listItems = page.getContent();
        int pageSize = page.getSize();

        long startCount = (pageNum - 1) * pageSize + 1;
        long endCount = startCount + pageSize - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute(listName, listItems);
        model.addAttribute("moduleURL", moduleURL);
    }

    public String getSortField() {
        return sortField;
    }

    public String getSortDir() {
        return sortDir;
    }

    public String getKeyword() {
        return keyword;
    }
}
