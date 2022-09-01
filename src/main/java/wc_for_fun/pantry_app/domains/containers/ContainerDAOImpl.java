package wc_for_fun.pantry_app.domains.containers;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

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

	private static final String[] containerKeySet = { "id", "name" };

	private static final String GET_BY_ID = "from Container c LEFT JOIN FETCH c.contents WHERE c.id=:id";

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
		Transaction tx = null;
		Session addSession = sessionFactory.openSession();

		try {
			tx = addSession.beginTransaction();
			Serializable createdContainer = addSession.save(newContainer);
			if (newContainer.getId() == null) {
				System.out.println("where did the thing go?");
			}
			addSession.flush();
			tx.commit();
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
		Transaction tx = null;
		try {
			tx = updateSession.beginTransaction();
			updateSession.saveOrUpdate(updatedContainer);
			updateSession.flush();
			tx.commit();
		} catch (Exception e) {
			System.out.println("O boy what happened?");
		}
		updateSession.close();
		return Optional.of(updatedContainer);
	}

	@Override
	@Transactional
	public Optional<Container> deleteContainer(Long idOfContainer) {
		Session deleteSession = sessionFactory.openSession();

		if (idOfContainer == null || idOfContainer.equals(Long.valueOf(0)))
			return Optional.empty();
		TypedQuery<Container> query = deleteSession.createQuery(GET_BY_ID);
		query.setParameter("id", idOfContainer);
		Container managedContainer = null;
		try {
			managedContainer = query.getSingleResult();
		} catch (Exception e) {
			System.out.println("ExceptionHandlingHolder");
		}
		Transaction tx = null;
		try {
			tx = deleteSession.getTransaction();
			tx.begin();
			Query deleteQuery = deleteSession.createQuery("DELETE FROM Container WHERE id = :id");
			deleteQuery.setParameter("id", idOfContainer);
			int deleteCheck = deleteQuery.executeUpdate();
			if (deleteCheck == 1) {
				deleteSession.flush();
				tx.commit();
			} else {
				tx.rollback();
			}
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
		TypedQuery<Container> insertSafeId = getIdSession.createQuery(GET_BY_ID);
		insertSafeId.setParameter("id", idOfContainer);
		Container tentativeResult = null;
		try {
			tentativeResult = insertSafeId.getSingleResult();
		} catch (NoResultException e) {
			return Optional.empty();
		}
		getIdSession.close();
		return Optional.of(tentativeResult);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Container> getContainersByName(String name) {
		Session getNameSession = sessionFactory.openSession();
		EntityManagerFactory emf = getNameSession.getEntityManagerFactory();
		CriteriaBuilder cb = emf.getCriteriaBuilder();
		CriteriaQuery<Container> cq = cb.createQuery(Container.class);
		Root<Container> containerRoot = cq.from(Container.class);
		EntityType<Container> Container_ = containerRoot.getModel();

		Predicate likeName = cb.like(cb.lower(containerRoot.get("name")), name.toLowerCase());

		cq.select(containerRoot).where(likeName);

		List<Container> debugHook = getNameSession.createQuery(cq).getResultList();

		return debugHook;
//		TypedQuery<Container> insertSafeId = getIdSession
//				.createQuery("from Container c LEFT JOIN FETCH c.contents WHERE name like :name");
//		insertSafeId.setParameter("name", name);
//
//		return insertSafeId.getResultList();

	}

}
