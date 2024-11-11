package com.app.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.helper.ResponseHelper;
import com.app.inventory.model.request.CreateItemRequest;
import com.app.inventory.model.request.UpdateItemRequest;
import com.app.inventory.model.response.ItemResponse;
import com.app.inventory.model.response.WebResponse;
import com.app.inventory.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/api" })
public class ItemController {
	
	private final ItemService service;
	private static final String PROP_ITEM = "item ";
	
	public ItemController(ItemService service) {
		super();
		this.service = service;
	}
	

	@PostMapping(value = "/item")
	public ResponseEntity<WebResponse<String>> add(@Valid @RequestBody CreateItemRequest request) {
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_ITEM + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@PutMapping(value = "/item")
	public ResponseEntity<WebResponse<String>> edit(@RequestBody UpdateItemRequest request) {
		service.edit(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_ITEM + ErrorMessageConstant.HAS_BEEN_EDITED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/item/{id}")
	public ResponseEntity<WebResponse<String>> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_ITEM + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/item/{id}")
	public ResponseEntity<WebResponse<ItemResponse>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/item")
	public ResponseEntity<WebResponse<Page<ItemResponse>>> getAllItems(Pageable pageable) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAllItems(pageable)));
	}
	

}
