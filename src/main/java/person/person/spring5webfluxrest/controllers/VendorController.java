package person.person.spring5webfluxrest.controllers;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import person.person.spring5webfluxrest.domain.Vendor;
import person.person.spring5webfluxrest.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Vendor> categoryStream) {
        return vendorRepository.saveAll(categoryStream).then();
    }
}
