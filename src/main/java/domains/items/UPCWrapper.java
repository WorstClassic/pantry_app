package domains.items;

import javax.persistence.*;

@Entity
public class UPCWrapper {
	private Long manufacturerUPC;
	private Long productUPC;
	private String checkUPC;

	public Long getManufacturerUPC() {
		return manufacturerUPC;
	}

	public void setManufacturerUPC(Long manufacturerUPC) {
		this.manufacturerUPC = manufacturerUPC;
	}

	public Long getProductUPC() {
		return productUPC;
	}

	public void setProductUPC(Long productUPC) {
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

	public UPCWrapper(String wholeUPC) {
		String[] slicedUPC = wholeUPCSlicer(wholeUPC);
		if (slicedUPC != null)
			try {
				this.manufacturerUPC = Long.valueOf(slicedUPC[0]);
				this.productUPC = Long.valueOf(slicedUPC[1]);
				this.checkUPC = slicedUPC[2];
			} catch (NumberFormatException e) {
				//TODO clean up for polish pass/logging portion.
				System.out.println("threw parsing exception; make sure to clean this up during polish pass.");
			}
	}

	private String[] wholeUPCSlicer(String wholeUPC) {
		try {
			String[] slicedUPCA = new String[] { wholeUPC.substring(0, 5), wholeUPC.substring(6, 11),
					wholeUPC.substring(12) };
			return slicedUPCA;
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public String toString() {
		return manufacturerUPC.toString().concat(productUPC.toString()).concat(checkUPC);
	}
	
	public boolean equals(String wholeUPC)
	{
		if(wholeUPC==null) return false;
		if(wholeUPC.length()!=12) return false;
		return this.toString().equalsIgnoreCase(wholeUPC);
	}
}
