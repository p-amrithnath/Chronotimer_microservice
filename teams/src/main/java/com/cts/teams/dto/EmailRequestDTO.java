package com.cts.teams.dto;

import lombok.Data;

@Data
public class EmailRequestDTO {

	private String to;
	private String subject;
	private String text;

}
