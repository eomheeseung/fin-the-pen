package project.fin_the_pen.model.assets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.assets.category.dto.DetailCategoryMapping;
import project.fin_the_pen.model.assets.category.dto.DetailCategoryRequestDto;
import project.fin_the_pen.model.assets.category.entity.Category;
import project.fin_the_pen.model.assets.category.entity.CategoryDetail;
import project.fin_the_pen.model.assets.category.repository.CategoryDetailRepository;
import project.fin_the_pen.model.assets.category.repository.CategoryRepository;
import project.fin_the_pen.model.report.dto.ExpenditureRequestDTO;
import project.fin_the_pen.model.report.entity.Reports;
import project.fin_the_pen.model.report.repository.ReportRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ReportRepository reportRepository;

    private final CategoryRepository categoryRepository;
    private final CategoryDetailRepository detailRepository;

    public boolean setAmount(ExpenditureRequestDTO dto, HttpServletRequest request) {
        Optional<Reports> findReports = reportRepository.findByUserIdAndDate(dto.getDate(), dto.getUserId());

        if (findReports.isPresent()) {
            Reports reports = findReports.get();

            reports.update(dto.getUserId(), dto.getAmount(), dto.getDate());
            reportRepository.save(reports);
        } else {
            Reports report = Reports.builder()
                    .userId(dto.getUserId())
                    .amount(dto.getAmount())
                    .date(dto.getDate())
                    .build();

            reportRepository.save(report);
        }
        return true;
    }

    public boolean setCategory(DetailCategoryRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String dtoDate = dto.getDate();
        DetailCategoryMapping details = dto.getDetails();
        String categoryName = details.getCategoryName();
        Map<String, String> detailsMap = details.getDetails();
        List<CategoryDetail> list = new ArrayList<>();


        Optional<Category> optionalCategory = categoryRepository.findByUserIdAnAndCategoryName(userId, categoryName);


        /*
        저장 logic
         */
        Category category = Category.builder()
                .userId(userId)
                .categoryName(categoryName)
                .date(dtoDate)
                .build();

        //TODO cascade를 사용 test
//        categoryRepository.save(category);

        for (String key : detailsMap.keySet()) {
            CategoryDetail categoryDetail = CategoryDetail.builder()
                    .detailName(key)
                    .build();
            category.addCategoryDetail(categoryDetail);
//            detailRepository.save(categoryDetail);
        }

        categoryRepository.save(category);




        // 수정
       /* if (optionalCategory.isPresent()) {
            Category findCategory = optionalCategory.get();


            if (detailsMap != null) {
                Set<String> keySet = detailsMap.keySet();

                for (String key : keySet) {
                    CategoryDetail categoryDetail = new CategoryDetail();
                    categoryDetail.update(detailsMap.get(key), dtoDate);
                    categoryDetail.getCategory().update(categoryName);
                    list.add(categoryDetail);
                }
                findCategory.update(categoryName);

                categoryRepository.save(findCategory);

            }

            List<CategoryDetail> categoryDetails = findCategory.getCategoryDetails();

            for (CategoryDetail categoryDetail : categoryDetails) {
                categoryDetails.remove(categoryDetail);
            }

            findCategory.update(categoryName, list);

            categoryRepository.save(findCategory);

        } else {
            if (detailsMap != null) {
                Set<String> keySet = detailsMap.keySet();

                for (String key : keySet) {

                }
            }

            Category category = Category.builder()
                    .categoryDetails(list)
                    .categoryName(categoryName)
                    .userId(userId)
                    .build();

            categoryRepository.save(category);
        }*/

        return true;
    }
}
