package com.cts.projects.exception;

/**
 * Custom exception class to handle cases where a requested resource is not
 * found.
 */

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
