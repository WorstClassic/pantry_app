package wc_for_fun.pantry_app.domains.items;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ItemDAOImpl implements ItemDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<Item> getItems() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Item");
		List<Item> allItems = query.getResultList();
		session.close();
		return allItems;
	}

}
