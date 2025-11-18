package test;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.IntStream;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.JsonUtils;

public class RechercheFiltree extends RechercheSimple {
	
	
	private static void addFilters(Map<String, String> filters, Map<String, String> items, String xPathPattern) {
		
		for (Map.Entry<String, String> entry : items.entrySet()) {
			filters.put(entry.getKey(), String.format(xPathPattern, entry.getValue()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void lancerRechercheFiltree(Map<String, Object> data) throws InterruptedException {
		
		
		lancerRechercheSimple(data); 
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		
		driver.findElement(By.xpath("//span[contains(text(), 'Filtres')]/ancestor::button")).click();
		
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		
		Map<String, String> filters = new LinkedHashMap<>();
		
		
		Map<String, Object> askedFilters = (Map<String, Object>) data.get("filters");
		
		String askedAccomodationType = (String) askedFilters.get("accomodationType");
		
		if (askedFilters.containsKey("accomodationType")) 
			
		filters.put(
			"accomodationType",
			"//span[contains(text(), '" + askedAccomodationType + "')]/parent::div"
		);
		
		Map<String, Integer> askedRooms = (Map<String, Integer>) askedFilters.get("rooms");
		
		Map<String, String> rooms = new HashMap<>();
		
		if (askedRooms.containsKey("bedRoom")) 

			rooms.put("addRoom", "Chambres");
		
		if (askedRooms.containsKey("bed")) 

			rooms.put("addBed", "Lits");
		
		if (askedRooms.containsKey("bathRoom")) 

			rooms.put("addBathRoom", "Salles de bain");
		
		
		Map<String, String> equipments = new HashMap<>();		
		
		Map<String, String> equipmentLabels = Map.ofEntries(
				
			Map.entry("heating", "Chauffage"),
			Map.entry("airConditioning", "Climatisation"),
			Map.entry("freeParking", "Parking gratuit"),
			Map.entry("electricStation", "Station de recharge pour véhicule électrique"),
			Map.entry("wifi", "Wifi"),
			Map.entry("tv", "Télévision"),
			Map.entry("chimney", "Cheminée"),
			Map.entry("barbecue", "Barbecue"),
			Map.entry("kitchen", "Cuisine"),
			Map.entry("smokeDetector", "Détecteur de fumée"),
			Map.entry("breakfast", "Petit déjeuner"),
			Map.entry("kingSizeBed", "Lit king size"),
			Map.entry("babyBed", "Lit pour bébé"),
			Map.entry("jacuzzi", "Jacuzzi"),
			Map.entry("washingMachine", "Lave-linge"),
			Map.entry("tumbleDryer", "Sèche-linge"),
			Map.entry("iron", "Fer à repasser"),
			Map.entry("hairDryer", "Sèche-cheveux"),
			Map.entry("workspace", "Espace de travail dédié"),
			Map.entry("gym", "Salle de sport")
		);
		
		Map<String, Integer> askedEquipments = (Map<String, Integer>) askedFilters.get("equipments");
		
		for (String key : equipmentLabels.keySet()) {
			
			equipments.put("moreEquipments", "Afficher plus");
			
			if (askedEquipments.containsKey(key)) {
				
				equipments.put(key, equipmentLabels.get(key));
			}
		}
		
		addFilters(
				filters, 
				rooms, 
				"//div[contains(text(), '%s')]/parent::div/following-sibling::div[1]/div/button[2]"
			);
		
		addFilters(
				filters, 
				equipments, 
				"//span[contains(text(), '%s')]/parent::button"
			);
		
		Map<String, Integer> clicks = Map.of(
				"addRoom", (Integer) askedRooms.get("bedRoom"),
				"addBed", (Integer) askedRooms.get("bed")
			);
		
		for (String key : filters.keySet()) {
			
			
			int times = clicks.getOrDefault(key, 1);
			
			for (int i = 0; i < times; i++) {
				
				WebElement button = driver.findElement(By.xpath(filters.get(key)));
				
				button.click();
				
				if (key.equals("moreEquipment"))
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

			}
			
		}
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		List<WebElement> displayButtons = driver.findElements(By.partialLinkText("Afficher"));

		if (!displayButtons.isEmpty()) {
			
			displayButtons.get(0).click();
			
		} else {
		
			System.out.println("Aucun logement n'est disponible");
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {

		toutesDonnees = JsonUtils.lireDonneesDepuisJSON();
		
		donneesSelectionnees = JsonUtils.selectionnerDonnees(toutesDonnees, "regression");
		
		for (Map<String, Object> data : donneesSelectionnees) lancerRechercheFiltree(data);
		
	}

}
