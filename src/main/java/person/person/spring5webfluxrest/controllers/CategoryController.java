package person.person.spring5webfluxrest.controllers;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import person.person.spring5webfluxrest.domain.Category;
import person.person.spring5webfluxrest.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories";
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/{id}")
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }
}
