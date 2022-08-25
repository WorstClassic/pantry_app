package wc_for_fun.pantry_app.domains.containers;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import wc_for_fun.pantry_app.domains.items.Item;

@Repository
public class ContainerDAOImpl implements ContainerDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Transactional(readOnly = true)
	@Override
	public List<Container> getContainers() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("SELECT DISTINCT c FROM Container c LEFT JOIN FETCH c.contents");
		List<Container> allContainers = query.getResultList();
		session.close();
		return allContainers;
	}

	@Override
	@Transactional
	public Optional<Container> saveContainer(Container newContainer) {
		// Transaction tx = null;
		Session addSession = sessionFactory.openSession();

		try {
			Serializable createdContainer = addSession.save(newContainer);
			if (newContainer.getId() == null) {
				System.out.println("where did the thing go?");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			addSession.close();
		}

		return Optional.ofNullable(newContainer);
	}

	@Override
	@Transactional
	public Optional<Container> updateContainer(Container updatedContainer) {
		Session updateSession = sessionFactory.openSession();
		updateSession.saveOrUpdate(updatedContainer);
		return Optional.of(updatedContainer);
	}

	@Override
	@Transactional
	public Optional<Container> deleteContainer(Long idOfContainer) {
		Session deleteSession = sessionFactory.openSession();

		if (idOfContainer == null || idOfContainer.equals(Long.valueOf(0)))
			return Optional.empty();
		TypedQuery<Container> query = deleteSession
				.createQuery("SELECT c FROM Container LEFT JOIN FETCH c.contents WHERE i.id=:id");
		query.setParameter("id", idOfContainer);
		Container managedContainer = null;
		try {
			managedContainer = query.getSingleResult();
		} catch (Exception e) {
			System.out.println("ExceptionHandlingHolder");
		}
		try {
			deleteSession.delete(managedContainer);
		} catch (Exception e) {
			System.out.println("ExceptionHandlingHolder");
		} finally {
			deleteSession.close();
		}
		return Optional.of(managedContainer);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Container> getContainerById(Long idOfContainer) {
		Session getIdSession = sessionFactory.openSession();
		TypedQuery<Container> insertSafeId = getIdSession
				.createQuery("from Container c LEFT JOIN FETCH c.contents WHERE id=:id");
		insertSafeId.setParameter("id", idOfContainer);

		return Optional.ofNullable(insertSafeId.getSingleResult());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Container> getContainersByName(String name) {
		Session getIdSession = sessionFactory.openSession();
		TypedQuery<Container> insertSafeId = getIdSession
				.createQuery("from Container c LEFT JOIN FETCH c.contents WHERE name like :name");
		insertSafeId.setParameter("name", name);

		return insertSafeId.getResultList();
	}

}
