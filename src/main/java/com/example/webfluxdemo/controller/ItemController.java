package com.example.webfluxdemo.controller;

import com.example.webfluxdemo.models.Item;
import com.example.webfluxdemo.models.exception.ItemNotFoundException;
import com.example.webfluxdemo.repository.IItemRepository;
import com.example.webfluxdemo.service.ItemService;
import com.example.webfluxdemo.service.dto.ItemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/item")
@Tag(name = "Item", description = "Item API")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by id")
    public Mono<Item> getItemById(@PathVariable Long id) {
        return itemService.getById(id)
                .switchIfEmpty(Mono.error(new ItemNotFoundException(id)));
    }

    @GetMapping
    @Operation(summary = "Get all items")
    public Flux<Item> getAllItems() {
        return itemService.getAll();
    }

    @PostMapping
    @Operation(summary = "Create item")
    public Mono<Item> createItem(@RequestBody ItemDto item) {
        return itemService.save(item);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item by id")
    public Mono<ResponseEntity<Object>> deleteItem(@PathVariable Long id) {
        return itemService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update item by id")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        Item item = new Item(id, itemDto.name());
        return itemService.update(item)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}