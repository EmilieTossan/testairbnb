package test;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import org.openqa.selenium.*;

import utils.JsonUtils;

public class RechercheFiltree extends RechercheSimple {
	
	
	private static void addFilters(Map<String, String> filters, Map<String, String> items, String xPathPattern) {
		
		for (Map.Entry<String, String> entry : items.entrySet()) {
			filters.put(entry.getKey(), String.format(xPathPattern, entry.getValue()));
		}
	}
	
	private static void setPrice(WebDriver driver, JavascriptExecutor js, String inputId, int price) {

		WebElement input = driver.findElement(By.id(inputId));

		js.executeScript(
			"let el = arguments[0];" +
			"let val = arguments[1];" +
			"let nativeSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
			"nativeSetter.call(el, val);" +
			"el.setAttribute('data-value', val);" +
			"el.dispatchEvent(new Event('input', { bubbles: true }));" +
			"el.dispatchEvent(new Event('change', { bubbles: true }));",
			input, price
		);
	}

	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type", "deprecation", "removal" })
	public static void lancerRechercheFiltree(Map<String, Object> data) throws InterruptedException {
		
		
		lancerRechercheSimple(data); 
		
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		
		if (data.containsKey("filters")) {
			
		
			driver.findElement(By.xpath("//span[contains(text(), 'Filtres')]/ancestor::button")).click();
			
			
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			
			
			Map<String, String> filters = new LinkedHashMap<>();
			
			Map<String, Object> askedFilters = (Map<String, Object>) data.get("filters");
			
			
			if (askedFilters.containsKey("accomodationType")) {
			
				String askedAccomodationType = (String) askedFilters.get("accomodationType");
				
				if (askedFilters.containsKey("accomodationType")) 
					
				filters.put(
					"accomodationType",
					"//span[contains(text(), '" + askedAccomodationType + "')]/parent::div"
				);
			
			}
			
			
			if (askedFilters.containsKey("pricePerNight")) {
				
				Map<String, Integer> pricePerNight = (Map<String, Integer>) askedFilters.get("pricePerNight");
				
				Integer days = new Integer((int) data.get("jours"));
				
				JavascriptExecutor js = (JavascriptExecutor) driver;
				
				if (pricePerNight.containsKey("min")) {
					
					int minPrice = (int) pricePerNight.get("min") * days;
					
					setPrice(driver, js, "price_filter_min", minPrice);

				}
				
				if (pricePerNight.containsKey("max")) {
					
					int maxPrice = (int) pricePerNight.get("max") * days;
					
					setPrice(driver, js, "price_filter_max", maxPrice);

				}
			}
			
			
			Map<String, Integer> clicks = null;
			
			
			if (askedFilters.containsKey("rooms")) {
				
				
				Map<String, Integer> askedRooms = (Map<String, Integer>) askedFilters.get("rooms");
				
				Map<String, String> rooms = new HashMap<>();
				
				if (askedRooms.containsKey("bedRoom"))
		
					rooms.put("addRoom", "Chambres");
				
				if (askedRooms.containsKey("bed")) 
		
					rooms.put("addBed", "Lits");
				
				if (askedRooms.containsKey("bathRoom"))
		
					rooms.put("addBathRoom", "Salles de bain");
				
				addFilters(
					filters, 
					rooms, 
					"//div[contains(text(), '%s')]/parent::div/following-sibling::div[1]/div/button[2]"
				);
				
				clicks = Map.of(
					"addRoom", (Integer) askedRooms.get("bedRoom"),
					"addBed", (Integer) askedRooms.get("bed")
				);
			}
			
			
			if (askedFilters.containsKey("equipments")) {
				
				
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
				
				equipments.put("moreEquipments", "Afficher plus");
				
				for (String key : equipmentLabels.keySet()) {
					
					if (askedEquipments.containsKey(key) && Boolean.TRUE.equals(askedEquipments.get(key)))
						
						equipments.put(key, equipmentLabels.get(key));
					
				}
				
				addFilters(
					filters, 
					equipments, 
					"//span[contains(text(), '%s')]/parent::button"
				);
			
			}
			
			
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
		} else {
			
			System.out.println("Il n'y a pas de filtre dans le jeu de données");
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {

		toutesDonnees = JsonUtils.lireDonneesDepuisJSON();
		
		donneesSelectionnees = JsonUtils.selectionnerDonnees(toutesDonnees, "nonregression");
		
		for (Map<String, Object> data : donneesSelectionnees) lancerRechercheFiltree(data);
		
	}

}
