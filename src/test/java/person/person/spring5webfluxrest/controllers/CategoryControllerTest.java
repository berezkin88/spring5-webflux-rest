package person.person.spring5webfluxrest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import person.person.spring5webfluxrest.domain.Category;
import person.person.spring5webfluxrest.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    public static final String DESCRIPTION = "Cat1";

    WebTestClient webTestClient;
    private CategoryRepository categoryRepository;
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() throws Exception {
        categoryRepository = mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);

        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        given(categoryRepository.findAll())
            .willReturn(Flux.just(Category.builder().description(DESCRIPTION).build(),
                Category.builder().description("Cat2").build()));

        webTestClient.get()
            .uri(CategoryController.BASE_URL)
            .exchange()
            .expectBodyList(Category.class)
            .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryRepository.findById(anyString()))
            .willReturn(Mono.just(Category.builder().description(DESCRIPTION).build()));

        webTestClient.get()
            .uri(CategoryController.BASE_URL + "/asd")
            .exchange()
            .expectBody(Category.class)
            .value(Category::getDescription, equalTo(DESCRIPTION));
    }

    @Test
    void create() {
        given(categoryRepository.saveAll(any(Publisher.class)))
            .willReturn(Flux.just(Category.builder().description(DESCRIPTION).build()));

        webTestClient.post()
            .uri(CategoryController.BASE_URL)
            .body(Mono.just(Category.builder().build()), Category.class)
            .exchange()
            .expectStatus()
            .isCreated();
    }

    @Test
    void update() {
        given(categoryRepository.save(any(Category.class)))
            .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description("Some cat").build());

        webTestClient.put()
            .uri(CategoryController.BASE_URL + "/asd")
            .body(categoryToSaveMono, Category.class)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    void patch() {
        given(categoryRepository.findById(anyString()))
            .willReturn(Mono.just(Category.builder().description(DESCRIPTION).build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description("Some cat").build());

        webTestClient.patch()
            .uri(CategoryController.BASE_URL + "/asd")
            .body(categoryToSaveMono, Category.class)
            .exchange()
            .expectStatus()
            .isOk();

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void patchNoChanges() {
        given(categoryRepository.findById(anyString()))
            .willReturn(Mono.just(Category.builder().description(DESCRIPTION).build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description(DESCRIPTION).build());

        webTestClient.patch()
            .uri(CategoryController.BASE_URL + "/asd")
            .body(categoryToSaveMono, Category.class)
            .exchange()
            .expectStatus()
            .isOk();

        verify(categoryRepository, never()).save(any(Category.class));
    }
}
