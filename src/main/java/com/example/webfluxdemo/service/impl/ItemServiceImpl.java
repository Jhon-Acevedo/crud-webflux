package com.example.webfluxdemo.service.impl;

import com.example.webfluxdemo.models.Item;
import com.example.webfluxdemo.models.exception.ItemNotFoundException;
import com.example.webfluxdemo.repository.IItemRepository;
import com.example.webfluxdemo.service.ItemService;
import com.example.webfluxdemo.service.dto.ItemDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemServiceImpl implements ItemService {

    private final IItemRepository itemRepository;

    public ItemServiceImpl(IItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Mono<Item> getById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Flux<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public Mono<Item> save(ItemDto item) {
        return itemRepository.save(new Item(item.name()));
    }


    @Override
    public Mono<Void> deleteById(Long id) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ItemNotFoundException(id)))
                .flatMap(item -> itemRepository.deleteById(id));
    }

    @Override
    public Mono<Item> update(Item item) {
        return itemRepository.findById(item.getId())
                .switchIfEmpty(Mono.error(new ItemNotFoundException(item.getId())))
                .flatMap(existingItem -> itemRepository.save(item));
    }
}
