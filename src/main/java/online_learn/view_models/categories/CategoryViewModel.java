package online_learn.view_models.categories;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryViewModel {

    private int categoryId;
    private String categoryName;

    public CategoryViewModel() {
    }

    public CategoryViewModel(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
