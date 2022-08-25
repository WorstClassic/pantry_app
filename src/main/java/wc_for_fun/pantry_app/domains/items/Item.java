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

import java.io.Serializable;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Entity
public class Item implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long newId) {
		id = newId;
	}

	@Embedded
	private UPCWrapper upc;

	// @DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate obtainDate;
	// @DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate expiryDate;

	@JsonIgnoreProperties(value = "contents")
	@ManyToMany(mappedBy = "contents")
	private List<Container> containers;

	private String naiiveItemName;
	private String naiiveItemDescription;

	// javax.measure's unit api should replace these in business logic at very
	// least.
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

	public Item updatePropertiesFromItem(Item updatedItem) {
		if (updatedItem == null)
			throw new NullPointerException("tried to copy info from non-item");
		if (updatedItem.obtainDate != null)
			this.obtainDate = updatedItem.obtainDate;
		if (updatedItem.expiryDate != null)
			this.expiryDate = updatedItem.expiryDate;
		if (updatedItem.naiiveItemName != null)
			this.naiiveItemName = updatedItem.naiiveItemName;
		if (updatedItem.naiiveItemDescription != null)
			this.naiiveItemDescription = updatedItem.naiiveItemDescription;
		if (updatedItem.upc != null)
			this.upc = updatedItem.upc;
		// We do not support ID updates.

//		HashSet<String> uniqueNamesSet = new HashSet<>();
//		HashSet<Long> uniqueIdSet = new HashSet<>();
//		for(Container c : updatedItem.containers) {
//			if(c.getId()!=null && c.getName()!=null) {
//			if(uniqueNamesSet.add(c.getName())) continue;
//			}
//		}
		if(updatedItem.containers!=null) {
			this.containers.clear();
			this.containers.addAll(updatedItem.containers);
		}
		return this;
	}
}
