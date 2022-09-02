package wc_for_fun.pantry_app.pantry_api.domains.items;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@EnableTransactionManagement
@Component
public class ItemDaoImpl implements ItemDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly = true)
	public List<Item> findByUpcString(String upc) {
		Session upcFindSession = sessionFactory.getCurrentSession();

		String queryString = "FROM Item as i LEFT JOIN FETCH i.containers WHERE ";

		UPCWrapper queryUpc = new UPCWrapper(upc);
		queryString += ("i.upc.typeCode= :typeCode AND ");
		queryString += ("i.upc.checkUPC= :checkUPC AND ");
		queryString += ("i.upc.manufacturerUPC= :manufacturerUPC AND ");
		queryString += ("i.upc.productUPC= :productUPC");
		try {
			Query<Item> query = upcFindSession.createQuery(queryString);
			query.setParameter("typeCode", queryUpc.getTypeCode());
			query.setParameter("checkUPC", queryUpc.getCheckUPC());
			query.setParameter("manufacturerUPC", queryUpc.getManufacturerUPC());
			query.setParameter("productUPC", queryUpc.getProductUPC());
			List<Item> upcList = query.getResultList();

			return upcList;
		} catch (Exception e) {
			System.out.println("Either we had a call fail or wedid a bad type conversion,");
			return null;
		}
	}

}
