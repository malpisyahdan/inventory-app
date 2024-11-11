package com.app.inventory.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.inventory.common.type.TypeInventory;
import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.model.request.CreateInventoryRequest;
import com.app.inventory.model.request.UpdateInventoryRequest;
import com.app.inventory.model.response.InventoryResponse;
import com.app.inventory.persistence.entity.Inventory;
import com.app.inventory.persistence.repository.InventoryRepository;
import com.app.inventory.service.InventoryService;
import com.app.inventory.service.ItemService;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
public class InventoryServiceImpl implements InventoryService {

	private final InventoryRepository repository;
	private final ItemService itemService;

	public InventoryServiceImpl(InventoryRepository repository, ItemService itemService) {
		super();
		this.repository = repository;
		this.itemService = itemService;
	}

	public void validateBkNotNull(CreateInventoryRequest request) {

		itemService.getEntityById(request.getItemId())
				.orElseThrow(() -> new ValidationException("item id " + ErrorMessageConstant.IS_NOT_EXISTS));
	}

	@Transactional
	@Override
	public void add(CreateInventoryRequest request) {
		validateBkNotNull(request);
		Inventory entity = new Inventory();
		mapToEntity(entity, request);
		repository.save(entity);

	}

	@Transactional
	@Override
	public void edit(UpdateInventoryRequest request) {
		validateBkNotNull(request);
		getEntityById(request.getId()).ifPresentOrElse(entity -> {
			mapToEntity(entity, request);
			repository.save(entity);
		}, () -> {
			throw new RuntimeException("id item " + ErrorMessageConstant.IS_NOT_EXISTS);
		});

	}

	@Transactional
	@Override
	public void delete(Long id) {
		getEntityById(id).ifPresentOrElse(entity -> {
			repository.deleteById(id);
		}, () -> {
			throw new RuntimeException("id item" + ErrorMessageConstant.IS_NOT_EXISTS);
		});

	}

	@Transactional
	@Override
	public Inventory changeTotalStock(Long itemId, Integer qty) {
		List<Inventory> inventories = getEntityByItemId(itemId);

		if (inventories.isEmpty()) {
			throw new RuntimeException("No stock available for item");
		}

		for (Inventory inv : inventories) {
			int currentStock = inv.getQty();

			if (currentStock > 0) {
				if (currentStock >= qty) {
					inv.setQty(currentStock - qty);
					repository.save(inv);
					return inv;
				} else {
					qty -= currentStock;
					inv.setQty(0);
					repository.save(inv);
				}
			}
		}

		throw new RuntimeException("Not enough stock available for item");
	}

	@Override
	public Optional<Inventory> getEntityById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Inventory> getEntityByItemId(Long itemId) {
		return repository.findAllByItemId(itemId);
	}

	@Override
	public InventoryResponse getById(Long id) {
		Inventory entity = getEntityById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item(s) " + ErrorMessageConstant.NOT_FOUND));
		return mapToResponse(entity);
	}

	@Override
	public Page<InventoryResponse> getAllItems(Pageable pageable) {
		return repository.findAll(pageable).map(this::mapToResponse);
	}

	private void mapToEntity(Inventory entity, CreateInventoryRequest request) {
		entity.setItem(itemService.getEntityById(request.getItemId()).orElse(null));
		entity.setQty(request.getQty());
		entity.setType(TypeInventory.valueOf(request.getType()));
	}

	public InventoryResponse mapToResponse(Inventory entity) {
		InventoryResponse response = new InventoryResponse();
		response.setId(entity.getId());
		response.setItemId(entity.getItem().getId());
		response.setItemName(entity.getItem().getName());
		response.setQty(entity.getQty());
		response.setType(entity.getType());
		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());
		

		return response;
	}

}
