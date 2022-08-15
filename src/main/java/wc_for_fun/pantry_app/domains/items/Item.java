package wc_for_fun.pantry_app.domains.items;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import javafx.util.converter.LocalDateStringConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import wc_for_fun.pantry_app.domains.containers.Container;

import java.time.*;
import java.util.List;


@Entity
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long newId) {
		id=newId;
	}
	
	private UPCWrapper upc;

	// @DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate obtainDate;
	// @DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate expiryDate;
	
	@JsonIgnoreProperties(value="contents")
	private List<Container> containers;

	private String naiiveItemName;
	private String naiiveItemDescription;
	
	//javax.measure's unit api should replace these in business logic at very least.
	@JsonProperty("unit")
	private String naiiveUnit;
	@JsonProperty("unit_amount")
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
