package wc_for_fun.pantry_app.domains.items;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import wc_for_fun.pantry_app.domains.containers.Container;

@Repository
public class ItemDAOImpl implements ItemDAO {

	@Autowired
	SessionFactory sessionFactory;

//	@PersistenceContext
//	private EntityManager entityManager;

	private static final String[] itemKeySet = { "id", "upc.typeCode", "upc.checkUPC", "upc.manufacturerUPC",
			"upc.productUPC", "obtainDate", "expiryDate", "naiiveItemName", "naiiveItemDescription", "unit",
			"unit_amount" };

	@Transactional(readOnly = true)
	@Override
	public List<Item> getItems() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("SELECT DISTINCT i FROM Item as i LEFT JOIN FETCH i.containers");
		List<Item> allItems = query.getResultList();
		session.close();
		return allItems;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Item> findById(Long idOfItem) {
		Session findByIdSession = sessionFactory.openSession();
		TypedQuery<Item> query = findByIdSession
				.createQuery("SELECT i FROM Item as i LEFT JOIN FETCH i.containers WHERE i.id=:id");
		query.setParameter("id", idOfItem);
		Item tentativeReturn=null;
		try {
			tentativeReturn = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("ErrorLogginCase");
		}
		findByIdSession.close();
		return Optional.of(tentativeReturn);
	}

	@Transactional
	@Override
	public Optional<Item> saveItem(Item newItem) {
		Transaction tx = null;
		Session addSession = sessionFactory.openSession();

		try {
			tx = addSession.beginTransaction();
			addSession.save(newItem);
			if (newItem.getId() == null) {
				System.out.println("where did the thing go?");
			}
			addSession.flush();
			tx.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// addSession.flush();
			addSession.close();
		}

		return Optional.ofNullable(newItem);
	}

	@Override
	@Transactional
	public Optional<Item> updateItem(Item updatedItem) {
		Session updateSession = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = updateSession.beginTransaction();
			updateSession.saveOrUpdate(updatedItem);
			updateSession.flush();
			tx.commit();
		} catch (Exception e) {
			System.out.println("O boy, what happened?");
		}
		updateSession.close();
		return Optional.of(updatedItem);
	}

	@Override
	@Transactional
	public Optional<Item> deleteItem(Long idOfItem) {
		Session deleteSession = sessionFactory.openSession();

		if (idOfItem == null || idOfItem.equals(Long.valueOf(0)))
			return Optional.empty();
		TypedQuery<Item> query = deleteSession
				.createQuery("SELECT i FROM Item as i LEFT JOIN FETCH i.containers WHERE i.id=:id");
		query.setParameter("id", idOfItem);
		Item managedItem = null;
		try {
			managedItem = query.getSingleResult();
			if(managedItem==null) {
				deleteSession.close();
				return Optional.empty();
			}
		} catch (Exception e) {
			System.out.println("ExceptionHandlingHolder");
		}
		Transaction tx = null;
		try {
			tx = deleteSession.getTransaction();
			tx.begin();
			if(!managedItem.getContainers().isEmpty()) {
//				Query pivotCleanup = deleteSession.createQuery("DELETE FROM Container_Item where contents_id = :itemId ");
//				pivotCleanup.setParameter("itemId", idOfItem);
//				pivotCleanup.executeUpdate();
				for(Container holdingContainer : managedItem.getContainers()) {
					holdingContainer.getContents().remove(managedItem);
					deleteSession.saveOrUpdate(holdingContainer);
				}
				managedItem.getContainers().clear();
				deleteSession.saveOrUpdate(managedItem);
			}
			deleteSession.delete(managedItem);
//			Query deleteQuery = deleteSession.createQuery("DELETE FROM Item WHERE id = :id");
//			deleteQuery.setParameter("id", idOfItem);
//			int deleteCheck = deleteQuery.executeUpdate();
//			if (deleteCheck == 1) {
//				deleteSession.flush();
//				tx.commit();
//			} else {
//				tx.rollback();
//			}
			deleteSession.flush();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("ExceptionHandlingHolder");
		}
		deleteSession.close();
		return Optional.of(managedItem);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Item> findAllByUpc(String upc) {
		Session upcFindSession = sessionFactory.getCurrentSession();

		String queryString = "FROM Item as i LEFT JOIN FETCH i.containers WHERE ";

		UPCWrapper queryUpc = new UPCWrapper(upc);
		queryString += ("i.upc.typeCode= :typeCode AND ");
		queryString += ("i.upc.checkUPC= :checkUPC AND ");
		queryString += ("i.upc.manufacturerUPC= :manufacturerUPC AND ");
		queryString += ("i.upc.productUPC= :productUPC");

		Query query = upcFindSession.createQuery(queryString);
		query.setParameter("typeCode", queryUpc.getTypeCode());
		query.setParameter("checkUPC", queryUpc.getCheckUPC());
		query.setParameter("manufacturerUPC", queryUpc.getManufacturerUPC());
		query.setParameter("productUPC", queryUpc.getProductUPC());
		List<Item> upcList = query.getResultList();

		return upcList;

//		EntityManagerFactory emf = upcFindSession.getEntityManagerFactory();
//		CriteriaBuilder criteriaBuilder = emf.getCriteriaBuilder();
//		CriteriaQuery<Item> criteriaQuery = criteriaBuilder.createQuery(Item.class);
//		Root <Item> itemRoot = criteriaQuery.from(Item.class);
//		EntityType<Item> Item_ = itemRoot.getModel();
//		//ListJoin<Item,Container> itemsComplete = itemRoot.join(Item_.getList("containers",Container.class));
//		itemRoot.fetch(Item_.getList("containers",Container.class));
//
//		Predicate d=criteriaBuilder.equal(itemRoot.get("upc").get("typeCode"), queryUpc.getTypeCode());
//		Predicate a = criteriaBuilder.equal(itemRoot.get("upc").get("checkUPC"), queryUpc.getCheckUPC());
//		Predicate b =criteriaBuilder.equal(itemRoot.get("upc").get("manufacturerUPC"), queryUpc.getManufacturerUPC());
//		Predicate c=criteriaBuilder.equal(itemRoot.get("upc").get("productUPC"), queryUpc.getProductUPC());
//		
//		criteriaQuery.select(itemRoot).where(criteriaBuilder.and(a,b,c,d));
//		
//		TypedQuery<Item> query = upcFindSession.createQuery(criteriaQuery);
//		List<Item> results = query.getResultList();
//		Set<Parameter<?>> parameters = query.getParameters();
//		String queryString = query.unwrap(org.hibernate.Query.class).getQueryString();
//		
//		return results;
	}

//	private Predicate upcMatchingPredicate(String incomingUpc, CriteriaBuilder cb) {
//		Predicate predicate = cb.conjunction();
//		ParameterExpression
//	}

}
