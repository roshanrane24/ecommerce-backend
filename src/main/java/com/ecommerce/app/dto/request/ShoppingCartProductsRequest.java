package com.ecommerce.app.dto.request;

import java.util.Objects;

import lombok.Data;

@Data
public class ShoppingCartProductsRequest{

	    String _id, name, image;
	    Double price;
	    Integer quantity=1;
	    Double subTotal;
		
	    public ShoppingCartProductsRequest(String _id, String name, String image, Double price) {
			this._id = _id;
			this.name = name;
			this.image = image;
			this.price = price;
			this.subTotal=price;
	    }
	    
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ShoppingCartProductsRequest other = (ShoppingCartProductsRequest) obj;
			return Objects.equals(_id, other._id) && Objects.equals(image, other.image)
					&& Objects.equals(name, other.name) && Objects.equals(price, other.price);
		}
		@Override
		public int hashCode() {
			return Objects.hash(_id, image, name, price);
		}
}
