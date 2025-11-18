package utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	public static List<Map<String, Object>> lireDonneesDepuisJSON() throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.readValue(
				new File("src/test/resources/data.json"),
				new TypeReference<List<Map<String, Object>>>() {}
			)
		;
	}
	
	public static List<Map<String, Object>> selectionnerDonnees(List<Map<String, Object>> toutesDonnees, String typeTest) {
		
		return toutesDonnees.stream()
				.filter(d -> Boolean.parseBoolean((String) d.getOrDefault("active", "true")))
				.filter(d -> typeTest == null || typeTest.equalsIgnoreCase((String) d.get("typeTest")))
				.collect(Collectors.toList());
	}
	
}
