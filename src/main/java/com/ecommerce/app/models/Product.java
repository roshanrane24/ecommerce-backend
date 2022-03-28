package com.ecommerce.app.models;

import java.time.Instant;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data //@Data annotation use to generates getters and setters for all fields.
@Document(collection ="products")  //@Document annotation use to set the collection name that will be used by the model. If the collection doesn’t exist, MongoDB will create it.
public class Product {
	@Id  // Specify the MongoDB document’s primary key _id using the @Id annotation
	private String id;
	@NotBlank  //Validates that the property is not null or whitespace. But, it can be applied only to text values.	 
	@Size(max = 100) //Indicates that the property should have a minimum of two characters and maximum is given by max attribute.
	private String name;
	@NotBlank
	private String description;
	@NotBlank
	private Double price;
	@NotBlank
	private Double rating = 0.0; //initially product rating is 0. 
	@CreatedDate
	private Instant product_added_date;
	@NotBlank
	private Integer stock= 0;   //initially product stock is 0. 
	@NotBlank
	private String subCategoryName;
	@NotBlank
	private Map<String,String> additional_details;
	@NotBlank
	private String image;
}
