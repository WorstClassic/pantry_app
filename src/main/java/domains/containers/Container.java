package domains.containers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import domains.items.Item;


@Entity
public class Container {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private List<Item> contents = new ArrayList<Item>();

	private String name;

	@JsonIgnoreProperties(value="containers")
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
	 * Adds an item to the container; mutating it.
	 * Currently does not check for duplicates.
	 * Currently does not confirm or update that the item has a back-reference.
	 * @param addMe an item to add to the container. 
	 * @return forwards result of add operation.
	 */
	public boolean addAnItem(Item addMe) {
		return contents.add(addMe);
	}
}
