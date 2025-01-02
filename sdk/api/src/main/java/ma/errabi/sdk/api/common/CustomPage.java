package ma.errabi.sdk.api.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomPage<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageNumber = page.getNumber();
    }

    public CustomPage(List<T> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }
}
