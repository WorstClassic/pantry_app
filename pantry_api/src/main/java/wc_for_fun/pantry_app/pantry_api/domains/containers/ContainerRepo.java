package wc_for_fun.pantry_app.pantry_api.domains.containers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContainerRepo extends JpaRepository<Container, Long> {
	public List<Container> findAll();

	public Optional<Container> getContainerById(Long idOfContainer);

	public List<Container> findAllByNameIgnoreCase(String name);

}
