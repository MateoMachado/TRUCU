package ucu.trucu.util.pagination;

import java.util.List;

/**
 *
 * @author NicoPuig
 * @param <T>
 */
public class Page<T> {

    private final int totalPages;
    private final int pageNumber;
    private final int pageSize;
    private final List<T> content;

    public Page(int totalPages, int pageNumber, int pageSize, List<T> content) {
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize == 0 ? content.size() : pageSize;
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getContent() {
        return content;
    }
}
