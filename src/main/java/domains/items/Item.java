package domains.items;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import domains.containers.Container;

import java.time.*;
import java.util.List;


@Entity
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private UPCWrapper upc;

	private LocalDate obtainDate;
	private LocalDate expiryDate;
	
	@JsonIgnoreProperties(value="contents")
	private List<Container> containers;

	private String naiiveItemName;
	private String naiiveItemDescription;
	
	//javax.measure's unit api should replace these in business logic at very least.
	private String naiiveUnit;
	private String naiiveUnitValue;
	public UPCWrapper getUpc() {
		return upc;
	}
	public void setUpc(UPCWrapper upc) {
		this.upc = upc;
	}
	public LocalDate getObtainDate() {
		return obtainDate;
	}
	public void setObtainDate(LocalDate obtainDate) {
		this.obtainDate = obtainDate;
	}
	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	public List<Container> getContainers() {
		return containers;
	}
	public void setContainers(List<Container> locationStrings) {
		this.containers = locationStrings;
	}
	public String getNaiiveItemName() {
		return naiiveItemName;
	}
	public void setNaiiveItemName(String naiiveItemName) {
		this.naiiveItemName = naiiveItemName;
	}
	public String getNaiiveItemDescription() {
		return naiiveItemDescription;
	}
	public void setNaiiveItemDescription(String naiiveItemDescription) {
		this.naiiveItemDescription = naiiveItemDescription;
	}
	public String getNaiiveUnit() {
		return naiiveUnit;
	}
	public void setNaiiveUnit(String naiiveUnit) {
		this.naiiveUnit = naiiveUnit;
	}
	public String getNaiiveUnitValue() {
		return naiiveUnitValue;
	}
	public void setNaiiveUnitValue(String naiiveUnitValue) {
		this.naiiveUnitValue = naiiveUnitValue;
	} 
}
