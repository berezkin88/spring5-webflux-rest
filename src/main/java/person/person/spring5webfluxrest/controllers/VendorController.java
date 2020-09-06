package person.person.spring5webfluxrest.controllers;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import person.person.spring5webfluxrest.domain.Category;
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
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor foundVendor = vendorRepository.findById(id).block();

        if (!foundVendor.equals(vendor)) {
            if (!foundVendor.getFirstName().equals(vendor.getFirstName())) {
                foundVendor.setFirstName(vendor.getFirstName());
            }

            if (!foundVendor.getLastName().equals(vendor.getLastName())) {
                foundVendor.setLastName(vendor.getLastName());
            }

            return vendorRepository.save(foundVendor);
        }

        return Mono.just(foundVendor);
    }
}
