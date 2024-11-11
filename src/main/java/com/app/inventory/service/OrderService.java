package com.app.inventory.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.inventory.model.request.CreateOrderRequest;
import com.app.inventory.model.request.UpdateOrderRequest;
import com.app.inventory.model.response.OrderResponse;
import com.app.inventory.persistence.entity.Order;

public interface OrderService {

	void add(CreateOrderRequest request);

	void edit(UpdateOrderRequest request);

	void delete(String id);

	Optional<Order> getEntityById(String id);

	OrderResponse getById(String id);

	Page<OrderResponse> getAllItems(Pageable pageable);

}
