package wc_for_fun.pantry_app.pantry_api.domains.containers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import wc_for_fun.pantry_app.pantry_api.domains.items.Item;

@Entity
public class Container implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long newId) {
		id = newId;
	}

	private String name;

	@JsonIgnoreProperties(value = "containers")
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Item> contents;// = new ArrayList<Item>();

	public List<Item> getContents() {
		return contents;
	}

	public void setContents(List<Item> contents) {
		this.contents = contents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds an item to the container; mutating it. Currently does not check for
	 * duplicates. Currently does not confirm or update that the item has a
	 * back-reference.
	 * 
	 * @param addMe an item to add to the container.
	 * @return forwards result of add operation.
	 */
	public boolean addAnItem(Item addMe) {
		if (contents == null)
			contents = new ArrayList<Item>();
		return contents.add(addMe);
	}

	public void updateProperties(Container updateData) {
		if (updateData == null)
			return;
		if (updateData.getName() != null)
			if (!updateData.getName().isEmpty())
				this.name = updateData.getName();
	}
}
