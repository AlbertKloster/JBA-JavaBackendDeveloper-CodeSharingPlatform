package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CodeSnippetService {

    private final CodeSnippetRepository repository;

    @Autowired
    public CodeSnippetService(CodeSnippetRepository repository) {
        this.repository = repository;
    }

    public CodeSnippet findCodeSnippetById(String uuid) {
        CodeSnippet codeSnippet = repository.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        deleteIfViewLimit(codeSnippet);
        return codeSnippet;
    }

    public Object save(CodeSnippet codeSnippet) {
        setExp(codeSnippet);
        return Map.of("id", repository.save(codeSnippet).getUuid());
    }

    private void setExp(CodeSnippet codeSnippet) {
        LocalDateTime date = LocalDateTime.now();
        codeSnippet.setDate(date);
        codeSnippet.setExp(date.plusSeconds(codeSnippet.getTime()));
    }

    public List<CodeSnippet> findLatest() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("date").descending());
        return repository.findAllByTimeLessThanAndViewsLessThan(1L, 1, paging).getContent();
    }

    public void deleteIfViewLimit(CodeSnippet codeSnippet) {
        int views = codeSnippet.getViews();
        if (views == 0)
            return;
        codeSnippet.setViews(--views);
        if (views == 0)
            repository.delete(codeSnippet);
        else
            repository.save(codeSnippet);
        codeSnippet.setViews(++views); // return initial views for code.html DOM rendering logic
    }

    @Scheduled(fixedDelay = 1000)
    public void deleteIfExpired() {
        repository.findAllByTimeGreaterThan(0L).forEach(codeEntity -> {
            long newTime = Duration.between(LocalDateTime.now(), codeEntity.getExp()).getSeconds();
            if (newTime <= 0) {
                repository.delete(codeEntity);
            } else {
                codeEntity.setTime(newTime);
                repository.save(codeEntity);
            }
        });
    }

}
