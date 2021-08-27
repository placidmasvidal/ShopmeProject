package com.shopme.admin.util.export;

import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;

public class ExporterFactory {

    public static AbstractExporter createExporter(ExporterTypeEnum type) {
        switch(type) {
            case USER_CSV:
                return new UserCsvExporter();
            case USER_EXCEL:
                return new UserExcelExporter();
            case USER_PDF:
                return new UserPdfExporter();
            case CAT_CSV:
                return new CategoryCsvExporter();
            default:
                throw new IllegalArgumentException("Exporter type is unknown");
        }
    }

}
