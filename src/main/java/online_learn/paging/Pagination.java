package online_learn.paging;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Pagination<T> {

    private int currentPage;
    private int pageSize;
    private long totalElement;

    @NonNull
    private String preUrl;

    @NonNull
    private String nextUrl;

    @NonNull
    private String firstUrl;

    @NonNull
    private String lastUrl;

    private List<T> list = new ArrayList<>();

    public long getNumberPage() {
        return (long)Math.ceil((double)this.totalElement/this.pageSize);
    }
}
