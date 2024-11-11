package com.app.inventory.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.model.request.CreateOrderRequest;
import com.app.inventory.model.request.UpdateOrderRequest;
import com.app.inventory.model.response.OrderResponse;
import com.app.inventory.persistence.entity.Order;
import com.app.inventory.persistence.repository.OrderRepository;
import com.app.inventory.service.InventoryService;
import com.app.inventory.service.ItemService;
import com.app.inventory.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository repository;
	private final ItemService itemService;
	private final InventoryService inventoryService;

	public OrderServiceImpl(OrderRepository repository, ItemService itemService, InventoryService inventoryService) {
		super();
		this.repository = repository;
		this.itemService = itemService;
		this.inventoryService = inventoryService;
	}

	public void validateBkNotNull(CreateOrderRequest request) {

		itemService.getEntityById(request.getItemId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"item id " + ErrorMessageConstant.IS_NOT_EXISTS));
	}

	@Transactional
	@Override
	public void add(CreateOrderRequest request) {
		Order entity = new Order();
		validateBkNotNull(request);
		mapToEntity(entity, request);
		repository.save(entity);
	}

	private String generateNextOrderNo() {
		Optional<Order> lastOrder = repository.findTopByOrderByOrderNoDesc();

		if (lastOrder.isPresent()) {
			String lastOrderNo = lastOrder.get().getOrderNo();
			int lastOrderNumber = Integer.parseInt(lastOrderNo.substring(1));
			return "Q" + (lastOrderNumber + 1);
		} else {
			return "Q1";
		}
	}

	@Transactional
	@Override
	public void edit(UpdateOrderRequest request) {
		validateBkNotNull(request);
		getEntityById(request.getId()).ifPresentOrElse(entity -> {
			mapToEntity(entity, request);
			repository.save(entity);
		}, () -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id item " + ErrorMessageConstant.IS_NOT_EXISTS);
		});

	}

	@Transactional
	@Override
	public void delete(String id) {
		getEntityById(id).ifPresentOrElse(entity -> {
			repository.deleteById(id);
		}, () -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id item" + ErrorMessageConstant.IS_NOT_EXISTS);
		});

	}

	@Override
	public Optional<Order> getEntityById(String id) {
		return repository.findById(id);
	}

	@Override
	public OrderResponse getById(String id) {
		Order entity = getEntityById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item(s) " + ErrorMessageConstant.NOT_FOUND));
		return mapToResponse(entity);
	}

	@Override
	public Page<OrderResponse> getAllItems(Pageable pageable) {
		return repository.findAll(pageable).map(this::mapToResponse);
	}

	private void mapToEntity(Order entity, CreateOrderRequest request) {
		String noOrder = generateNextOrderNo();

		entity.setOrderNo(noOrder);
		entity.setItem(itemService.getEntityById(request.getItemId()).orElse(null));
		entity.setQty(request.getQty());

		if (request.getItemId() != null) {

			inventoryService.changeTotalStock(request.getItemId(), request.getQty());

			BigDecimal priceItem = itemService.getEntityById(request.getItemId()).get().getPrice();

			BigDecimal totalPrice = priceItem.multiply(BigDecimal.valueOf(request.getQty()));

			entity.setPrice(totalPrice);
		}

	}

	public OrderResponse mapToResponse(Order entity) {
		OrderResponse response = new OrderResponse();
		response.setOrderNo(entity.getOrderNo());
		response.setItemId(entity.getItem().getId());
		response.setItemName(entity.getItem().getName());
		response.setQty(entity.getQty());
		response.setPrice(entity.getPrice());

		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());

		return response;
	}

}
