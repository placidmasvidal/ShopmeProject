package com.shopme.admin.category.export;

import com.shopme.admin.user.export.UserAbstractExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends CategoryAbstractExporter {

    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Category ID", "Category Name", "Alias", "Enabled"};
        csvWriter.writeHeader(csvHeader);

        String[] fieldMapping = {"id", "name", "alias", "enabled"};

        for (Category category : listCategories) {
            csvWriter.write(category, fieldMapping);
        }

        csvWriter.close();
    }
}
