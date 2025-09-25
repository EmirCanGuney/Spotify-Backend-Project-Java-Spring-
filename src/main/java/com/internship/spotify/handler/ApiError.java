package com.internship.spotify.handler;

import lombok.Data;

@Data
public class ApiError<E> {

	private Integer status;

	private Exception<E> exception;
}
