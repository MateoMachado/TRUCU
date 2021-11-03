package ucu.trucu.util.pagination;

import java.util.List;

/**
 *
 * @author NicoPuig
 * @param <T>
 */
public class Page<T> {

    private final int totalElements;
    private final int totalPages;
    private final int pageNumber;
    private final int pageSize;
    private final List<T> content;

    public Page(int totalElements, int pageNumber, int pageSize, List<T> content) {
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize == 0 ? content.size() : pageSize;
        this.content = content;
        this.totalPages = this.pageSize == 0 ? 0 : (int) Math.ceil((float) this.totalElements / this.pageSize);
    }

    public int getTotalElements() {
        return totalElements;
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
