package project.fin_the_pen.model.assets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.assets.category.dto.CategoryGoalRequestDto;
import project.fin_the_pen.model.assets.category.dto.CategoryGoalResponseDto;
import project.fin_the_pen.model.assets.category.dto.SmallCategoryResponseDto;
import project.fin_the_pen.model.assets.category.entity.Category;
import project.fin_the_pen.model.assets.category.entity.SmallCategory;
import project.fin_the_pen.model.assets.category.repository.CategoryRepository;
import project.fin_the_pen.model.assets.spend.entity.SpendAmount;
import project.fin_the_pen.model.assets.spend.entity.SpendAmountRegular;
import project.fin_the_pen.model.assets.spend.repository.SpendAmountRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final SpendAmountRepository spendAmountRepository;
    private final CategoryRepository categoryRepository;

    public Map<Object, Object> viewAmount(String userId, String date, HttpServletRequest request) {
        List<SpendAmount> spendAmountList = spendAmountRepository.findByUserIdAndStartDateToList(userId, date);

        Map<Object, Object> responseMap = new HashMap<>();
        ArrayList<CategoryGoalResponseDto> responseList = new ArrayList<>();

        Optional<SpendAmount> offSpendAmount = spendAmountList.stream()
                .filter(spendAmount -> spendAmount.getRegular().equals(SpendAmountRegular.OFF))
                .findAny();

        // OFF 값이 없는 경우 ON 값으로 필터링
        Optional<SpendAmount> onSpendAmount = offSpendAmount.or(() ->
                spendAmountList.stream()
                        .filter(spendAmount -> spendAmount.getRegular().equals(SpendAmountRegular.ON))
                        .findAny()
        );

        if (offSpendAmount.isPresent()) {
            SpendAmount spendAmount = offSpendAmount.get();
            responseMap.put("spend_goal_amount", spendAmount.getSpendGoalAmount());
            responseMap.put("date", spendAmount.getStartDate());

            List<Category> categoryList = categoryRepository.findByUserIdAndDate(userId, date);

            int monthTotalValue = 0;
            monthTotalValue = categoryList.stream().mapToInt(category -> Integer.parseInt(category.getMediumValue())).sum();


            for (Category category : categoryList) {
                CategoryGoalResponseDto responseDto = new CategoryGoalResponseDto();

                List<SmallCategory> smallCategoryList = category.getCategoryList();

                for (SmallCategory smallCategory : smallCategoryList) {
                    SmallCategoryResponseDto smallResponseDto = new SmallCategoryResponseDto();
                    smallResponseDto.setName(smallCategory.getSmallName());
                    smallResponseDto.setValue(smallCategory.getSmallValue());

                    responseDto.addList(smallResponseDto);
                }

                responseDto.setCategoryTotal(category.getMediumValue());
                responseDto.setCategoryName(category.getMediumName());

                responseList.add(responseDto);
            }

            responseMap.put("category_list", responseList);
            int spendGoalAmount = Integer.parseInt(spendAmount.getSpendGoalAmount());
            int ratio = (monthTotalValue * 100) / spendGoalAmount;
            log.info("비율:{}", ratio);
            responseMap.put("ratio", ratio + "%");
            responseMap.put("category_total", String.valueOf(monthTotalValue));

            log.info("카테고리 합산 값:{}", monthTotalValue);


        } else if (onSpendAmount.isPresent()) {
            SpendAmount spendAmount = onSpendAmount.get();
            responseMap.put("spend_goal_amount", spendAmount.getSpendGoalAmount());
            responseMap.put("date", spendAmount.getStartDate());

            List<Category> categoryList = categoryRepository.findByUserIdAndDate(userId, date);

            int monthTotalValue = 0;
            monthTotalValue = categoryList.stream().mapToInt(category -> Integer.parseInt(category.getMediumValue())).sum();

            for (Category category : categoryList) {
                CategoryGoalResponseDto responseDto = new CategoryGoalResponseDto();

                List<SmallCategory> smallCategoryList = category.getCategoryList();

                for (SmallCategory smallCategory : smallCategoryList) {
                    SmallCategoryResponseDto smallResponseDto = new SmallCategoryResponseDto();
                    smallResponseDto.setName(smallCategory.getSmallName());
                    smallResponseDto.setValue(smallCategory.getSmallValue());
                    responseDto.addList(smallResponseDto);
                }

                responseDto.setCategoryTotal(category.getMediumValue());
                responseDto.setCategoryName(category.getMediumName());

                responseList.add(responseDto);
            }

            responseMap.put("category_list", responseList);
            int spendGoalAmount = Integer.parseInt(spendAmount.getSpendGoalAmount());
            int ratio = (monthTotalValue * 100) / spendGoalAmount;

            log.info("비율:{}", ratio);
            responseMap.put("ratio", ratio + "%");
            log.info("카테고리 합산 값:{}", monthTotalValue);
            responseMap.put("category_total", String.valueOf(monthTotalValue));
        } else {
            responseMap.put("data", "no data");
        }

        return responseMap;
    }

    public boolean setAmount(CategoryGoalRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String date = dto.getDate();
        String mediumValue = dto.getMediumValue();
        String mediumName = dto.getMediumName();
        Map<String, String> smallMap = dto.getSmallMap();

        Optional<Category> optionalCategory =
                categoryRepository.findByUserIdAndDateAndMediumName(userId, date, mediumName);

        try {
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                category.deleteList();

                Set<String> keySet = smallMap.keySet();

                for (String key : keySet) {
                    SmallCategory smallCategory = SmallCategory
                            .builder()
                            .smallName(key)
                            .smallValue(smallMap.get(key))
                            .build();

                    category.addSmallCategory(smallCategory);
                }

                category.update(userId, mediumName, mediumValue);

                categoryRepository.save(category);
                return true;
            }

            Set<String> keySet = smallMap.keySet();

            Category category = Category.builder()
                    .mediumName(mediumName)
                    .userId(userId)
                    .date(date)
                    .mediumValue(mediumValue)
                    .build();

            for (String key : keySet) {
                SmallCategory smallCategory = SmallCategory
                        .builder()
                        .smallName(key)
                        .smallValue(smallMap.get(key))
                        .build();

                category.addSmallCategory(smallCategory);
            }

            categoryRepository.save(category);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean deleteAmount(String userId, String date, HttpServletRequest request) {
        List<Category> categoryList = categoryRepository.findByUserIdAndDate(userId, date);

        try {
            if (categoryList.isEmpty()) {
                return true;
            }

            categoryRepository.deleteAll(categoryList);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
