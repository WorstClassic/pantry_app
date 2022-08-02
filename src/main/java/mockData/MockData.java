package mockData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import domains.containers.Container;
import domains.items.Item;
import domains.items.UPCWrapper;

/**
 * Mocks up data using a static list.
 * 
 * @author Classic
 *
 */
@Service
public class MockData {
	private static ArrayList<Item> Items;
	private static ArrayList<Container> Containers;

	static {
		whyDidDataloaderFailMe();
	}

	public static ArrayList<Item> getItems() {
		return Items;
	}

	public static void setItems(ArrayList<Item> items) {
		Items = items;
	}

	public static ArrayList<Container> getContainers() {
		return Containers;
	}

	public static void setContainers(ArrayList<Container> containers) {
		Containers = containers;
	}

	private static void whyDidDataloaderFailMe() {
		// Items should be ingredients to be used against recipe in:
		// https://cooking.nytimes.com/recipes/1893-everyday-pancakes
		
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
	}
}