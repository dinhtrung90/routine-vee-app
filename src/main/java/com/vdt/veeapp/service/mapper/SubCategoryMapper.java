package com.vdt.veeapp.service.mapper;


import com.vdt.veeapp.domain.*;
import com.vdt.veeapp.service.dto.SubCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubCategory} and its DTO {@link SubCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface SubCategoryMapper extends EntityMapper<SubCategoryDTO, SubCategory> {

    @Mapping(source = "category.id", target = "categoryId")
    SubCategoryDTO toDto(SubCategory subCategory);

    @Mapping(source = "categoryId", target = "category")
    SubCategory toEntity(SubCategoryDTO subCategoryDTO);

    default SubCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubCategory subCategory = new SubCategory();
        subCategory.setId(id);
        return subCategory;
    }
}
