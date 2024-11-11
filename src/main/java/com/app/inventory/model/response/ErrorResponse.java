package com.app.inventory.model.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse<T> {

	@JsonProperty("timestamp")
	private LocalDateTime timestamp;

	@JsonProperty("code")
	private Integer code;

	@JsonProperty("error")
	private T error;

	@JsonProperty("path")
	private String path;

	public ErrorResponse(LocalDateTime timestamp, Integer code, T error, String path) {
		this.timestamp = timestamp;
		this.code = code;
		this.error = error;
		this.path = path;
	}

	public static <T> Builder<T> builder() {
		return new Builder<>();
	}

	public static class Builder<T> {
		private LocalDateTime timestamp;
		private Integer code;
		private T error;

		private String path;

		public Builder<T> timestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder<T> code(Integer code) {
			this.code = code;
			return this;
		}

		public Builder<T> error(T error) {
			this.error = error;
			return this;
		}

		public Builder<T> path(String path) {
			this.path = path;
			return this;
		}

		public ErrorResponse<T> build() {
			return new ErrorResponse<>(timestamp, code, error, path);
		}
	}
}
