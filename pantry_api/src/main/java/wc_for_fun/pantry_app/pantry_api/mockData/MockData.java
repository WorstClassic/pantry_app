package wc_for_fun.pantry_app.pantry_api.mockData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import wc_for_fun.pantry_app.pantry_api.domains.containers.Container;
import wc_for_fun.pantry_app.pantry_api.domains.containers.ContainerRepo;
import wc_for_fun.pantry_app.pantry_api.domains.items.Item;
import wc_for_fun.pantry_app.pantry_api.domains.items.ItemRepo;
import wc_for_fun.pantry_app.pantry_api.domains.items.UPCWrapper;

/**
 * Mocks up data using a static list.
 * 
 * @author Classic
 *
 */
@Component
public class MockData implements ApplicationListener<ContextRefreshedEvent> {

	
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private ItemRepo itemRepo;
	@Autowired
	private ContainerRepo containerRepo;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// Items should be ingredients to be used against recipe in:
		// https://cooking.nytimes.com/recipes/1893-everyday-pancakes

		if(!containerRepo.findAll().isEmpty()) {
			return;
		}
		ArrayList<Container> containersList = new ArrayList<>();
		Container TopShelf = new Container();
		TopShelf.setName("TopShelf");
		Container MiddleShelf = new Container();
		MiddleShelf.setName("MiddleShelf");
		Container Fridge = new Container();
		Fridge.setName("Fridge");
		containersList.add(TopShelf);
		containersList.add(MiddleShelf);
		containersList.add(Fridge);

//		for (Container entry : containersList) {
//			entry = containerRepo.saveContainer(entry).get();
//		}

		LocalDate now = LocalDate.now();
		LocalDate twoDaysAgo = now.minusDays(2);
		LocalDate twoWeeksAgo = now.minusWeeks(2);
		LocalDate oneWeekFromNow = now.plusWeeks(1);
		LocalDate twoYearsFromNow = now.plusYears(2);

		ArrayList<Item> itemsList = new ArrayList<>();

		Item mortonsNoniodizedSalt = new Item();
		mortonsNoniodizedSalt.setUpc(new UPCWrapper("024600010016"));
		mortonsNoniodizedSalt.setObtainDate(twoWeeksAgo);
		mortonsNoniodizedSalt.setExpiryDate(twoYearsFromNow);
		mortonsNoniodizedSalt.setContainers(Collections.singletonList(TopShelf));
		TopShelf.addAnItem(mortonsNoniodizedSalt);
		mortonsNoniodizedSalt.setNaiiveItemName("Morton Table Salt, Non-Iodized");
		mortonsNoniodizedSalt.setNaiiveItemDescription("Noniodized Salty 26 Ounce Canister");
		mortonsNoniodizedSalt.setNaiiveUnit("Oz");
		mortonsNoniodizedSalt.setNaiiveUnitValue("26");
		itemsList.add(mortonsNoniodizedSalt);

		Item prairieFarmsTwoPercent = new Item();
		prairieFarmsTwoPercent.setUpc(new UPCWrapper("072730221109"));
		prairieFarmsTwoPercent.setObtainDate(twoDaysAgo);
		prairieFarmsTwoPercent.setExpiryDate(oneWeekFromNow);
		prairieFarmsTwoPercent.setContainers(Collections.singletonList(Fridge));
		Fridge.addAnItem(prairieFarmsTwoPercent);
		prairieFarmsTwoPercent.setNaiiveItemName("Prairie Farms 2% Milk ");
		prairieFarmsTwoPercent.setNaiiveItemDescription("IL Local 2% - 1 gal");
		prairieFarmsTwoPercent.setNaiiveUnit("FlOz");
		prairieFarmsTwoPercent.setNaiiveUnitValue("128");
		itemsList.add(prairieFarmsTwoPercent);

		Item ChallengeButter = new Item();
		ChallengeButter.setUpc(new UPCWrapper("047200152603"));
		ChallengeButter.setObtainDate(twoWeeksAgo);
		ChallengeButter.setExpiryDate(oneWeekFromNow);
		ChallengeButter.setContainers(Collections.singletonList(Fridge));
		Fridge.addAnItem(ChallengeButter);
		ChallengeButter.setNaiiveItemName("Challenge Butter");
		ChallengeButter.setNaiiveItemDescription("Real California Milk Butter 16oz");
		ChallengeButter.setNaiiveUnit("Oz");
		ChallengeButter.setNaiiveUnitValue("16");
		itemsList.add(ChallengeButter);

		Item greatValueEggs = new Item();
		greatValueEggs.setUpc(new UPCWrapper("078742127088"));
		greatValueEggs.setObtainDate(twoDaysAgo);
		greatValueEggs.setExpiryDate(oneWeekFromNow);
		greatValueEggs.setContainers(Collections.singletonList(Fridge));
		Fridge.addAnItem(greatValueEggs);
		greatValueEggs.setNaiiveItemName("White Eggs");
		greatValueEggs.setNaiiveItemDescription("Great Value Large Grade A, 18 Eggs");
		greatValueEggs.setNaiiveUnit("Large Egg");
		greatValueEggs.setNaiiveUnitValue("18");
		itemsList.add(greatValueEggs);

		Item allPurposeFlour = new Item();
		allPurposeFlour.setUpc(new UPCWrapper("016000106109"));
		allPurposeFlour.setObtainDate(twoDaysAgo);
		allPurposeFlour.setExpiryDate(twoYearsFromNow);
		allPurposeFlour.setContainers(Collections.singletonList(TopShelf));
		TopShelf.addAnItem(allPurposeFlour);
		allPurposeFlour.setNaiiveItemName("All-Purpose Flour");
		allPurposeFlour.setNaiiveItemDescription("Gold Medal All Purpose Bleahced & Enriched Flour 5 lbs.");
		allPurposeFlour.setNaiiveUnit("lbs");
		allPurposeFlour.setNaiiveUnitValue("5");
		itemsList.add(allPurposeFlour);

		Item bakingPowder = new Item();
		bakingPowder.setUpc(new UPCWrapper("041617007181"));
		bakingPowder.setObtainDate(twoDaysAgo);
		bakingPowder.setExpiryDate(twoYearsFromNow);
		bakingPowder.setContainers(Collections.singletonList(TopShelf));
		TopShelf.addAnItem(bakingPowder);
		bakingPowder.setNaiiveItemName("Baking Powder");
		bakingPowder.setNaiiveItemDescription("Royal Baking Powder, 8.1 Oz");
		bakingPowder.setNaiiveUnit("Oz");
		bakingPowder.setNaiiveUnitValue("8.1");
		itemsList.add(bakingPowder);

//		for (Item entry : itemsList) {
//			entry = itemRepo.saveItem(entry).get();
//		}
//		for (Container entry : containersList) {
//			containerRepo.saveContainer(entry);
//		}
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		for (Container entry : containersList) {
			session.save(entry);
	}
		tx.commit();
		//session.flush();
		session.close();
		//Did this work and the others not because save wasn't opening a session?
		

//		int greatersize = itemsList.size();
//		int lessersize = containersList.size();
//		// Don't forget that you're assuming itemslist is going to always be larger.
//		for(int i=0 ; i< lessersize; i++) {
//			Long index = Long.valueOf(i); // We're not even pretending to be an SQL DB, so why bother with a valid ID?
//			containersList.get(i).setId(index);
//			itemsList.get(i).setId(index);
//		}
//		for(int i=lessersize; i<greatersize;i++) {
//			itemsList.get(i).setId(Long.valueOf(i));
//		}
//		
//		Containers = containersList;
//		Items = itemsList;
	}
}