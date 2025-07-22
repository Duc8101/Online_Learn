package online_learn.dtos.category_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryListDTO {

    private int categoryId;
    private String categoryName;

    public CategoryListDTO() {
    }

    public CategoryListDTO(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
