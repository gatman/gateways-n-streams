package org.streamy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SampleDetail {
	
    private String requestId;
    
	private String name;

	private String UUID;

	private String application;

	private String uiVersion;
	
	private String status;

	private String createdDate;
	
	private String createdBy;

	private String updatedDate;

	private String updatedBy;

	private boolean booleanOne;
	
	private Integer intOne;
	
	private Double dblOne;
	
}
