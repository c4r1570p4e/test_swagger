package org.cl.rest.spring.resteasy.domain;

import javax.annotation.PostConstruct;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.converter.OverrideConverter;

public class SwaggerModel {
	
	
	private static String RECETTE_JSON = 
		"{" +
		"\"id\":\"Recette\"," +
		"\"description\":\"Une recette de dessert, avec sa description, le niveau de dificulté et le temps de realisation nécessaire\"," +
		"\"required\":[\"libelle\"]," +
		"\"properties\":" +
		"			{" +
		"				\"identifiant\":" +
		"							{" +	
		"								\"type\":\"string\"," +
		"								\"description\":\"id de la recette\"" +
		"							}," +
		"				\"titre\":	" +
		"							{" +
		"								\"type\":\"string\"," +
		"								\"description\":\"Le libelle de la recette\"" +
		"							}," +
		"				\"niveauDeDifficulte\":" +
		"							{" +
		"								\"type\":\"integer\"," +
		"								\"format\":\"int32\"," +
		"								\"description\":\"Le niveau de difficulté de la recette\"," +
		"								\"enum\":[\"1\",\" 2\",\" 3\",\" 4\",\" 5\"]" +
		"							}," +
		"				\"tempsPreparation\":" +
		"							{" +
		"								\"type\":\"integer\"," +
		"								\"format\":\"int32\"," +
		"								\"description\":\"Le temps nécessaire à la réalisation de la recette en minutes\"" +
		"							}," +
		"				\"recette\":" +
		"							{" +
		"								\"type\":\"string\"," +
		"								\"description\":\"Les étapes necessaires à la réalisation de la recette\"" +
		"							}" +
		"			}" +
		"}"	;
	
	@PostConstruct
	public void init() {

		OverrideConverter converter = new OverrideConverter();
		converter.add(Recette.class.getCanonicalName(), RECETTE_JSON);
		ModelConverters.addConverter(converter, true);
	}

}
