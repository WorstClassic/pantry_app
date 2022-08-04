package wc_for_fun.pantry_app.domains.items;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Entity
public class UPCWrapper {
	private Integer manufacturerUPC;
	private Integer productUPC;
	private String checkUPC;

	public Integer getManufacturerUPC() {
		return manufacturerUPC;
	}

	public void setManufacturerUPC(Integer manufacturerUPC) {
		this.manufacturerUPC = manufacturerUPC;
	}

	public Integer getProductUPC() {
		return productUPC;
	}

	public void setProductUPC(Integer productUPC) {
		this.productUPC = productUPC;
	}

	public String getCheckUPC() {
		return checkUPC;
	}

	public void setCheckUPC(String checkUPC) {
		this.checkUPC = checkUPC;
	}

	public UPCWrapper() {

	}

	@JsonCreator
	public UPCWrapper(String wholeUPC) {
		String[] slicedUPC = wholeUPCSlicer(wholeUPC);
		if (slicedUPC != null)
			try {
				this.manufacturerUPC = Integer.valueOf(slicedUPC[0]);
				this.productUPC = Integer.valueOf(slicedUPC[1]);
				this.checkUPC = slicedUPC[2];
			} catch (NumberFormatException e) {
				//TODO clean up for polish pass/logging portion.
				System.out.println("threw parsing exception; make sure to clean this up during polish pass.");
			}
	}

	private String[] wholeUPCSlicer(String wholeUPC) {
		try {
			String[] slicedUPCA = new String[] { wholeUPC.substring(0, 6), wholeUPC.substring(6, 11),
					wholeUPC.substring(11) };
			return slicedUPCA;
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private String zeroPrepend(Integer incomingNumber) {
		final String[] numberOfZeroes = {"","0","00","000","0000","00000"};
		String prependMe = incomingNumber.toString();
		return numberOfZeroes[6 - prependMe.length()].concat(prependMe);
	}
	
	@JsonValue
	public String toString() {
		return zeroPrepend(manufacturerUPC).concat(zeroPrepend(productUPC)).concat(checkUPC);
	}
	
	public boolean equals(String wholeUPC)
	{
		if(wholeUPC==null) return false;
		if(wholeUPC.length()!=12) return false;
		return this.toString().equalsIgnoreCase(wholeUPC);
	}
}
