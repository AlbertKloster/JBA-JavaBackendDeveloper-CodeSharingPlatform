package platform;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CodeSnippetRepository extends PagingAndSortingRepository<CodeSnippet, String> {
    Page<CodeSnippet> findAllByTimeLessThanAndViewsLessThan(long time, int views, Pageable pageable);

    Iterable<CodeSnippet> findAllByTimeGreaterThan(long time);

}
