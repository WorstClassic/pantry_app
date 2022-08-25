package wc_for_fun.pantry_app.domains.containers;

import java.util.List;
import java.util.Optional;

public interface ContainerDAO {
	public List<Container> getContainers();
	public Optional<Container> getContainerById(Long idOfContainer);
	public List<Container> getContainersByName(String name);
	public Optional<Container> saveContainer(Container newContainer);
	public Optional<Container> updateContainer(Container updatedContainer);
	public Optional<Container> deleteContainer(Long idOfContainer);
}
