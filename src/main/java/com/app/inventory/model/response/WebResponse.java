package com.app.inventory.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard Web Response
 */

public class WebResponse<T> {

	public WebResponse(Integer code, String status, T data) {
		super();
		this.code = code;
		this.status = status;
		this.data = data;
	}

	/**
	 * Code , usually same as HTTP Code
	 */
	@JsonProperty("code")
	private Integer code;

	/**
	 * Status, usually same as HTTP status
	 */
	@JsonProperty("status")
	private String status;

	/**
	 * Response data
	 */
	@JsonProperty("data")
	private T data;
	
	 // Manual builder method
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Integer code;
        private String status;
        private T data;

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder<T> status(String status) {
            this.status = status;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public WebResponse<T> build() {
            return new WebResponse<>(code, status, data);
        }
    }

}
