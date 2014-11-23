package org.cl.rest.spring.mvc.domain;

import javax.xml.bind.annotation.XmlRootElement;

<<<<<<< HEAD
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

=======
>>>>>>> inclusion de domain et dao : spring:mvc
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@XmlRootElement
@EqualsAndHashCode(of = { "libelle" })
<<<<<<< HEAD
@ApiModel(value = "Une recette de desserts.", description = "Une recette de dessert, avec sa description, le niveau de dificulté et le temps de realisation nécessaire")
public class Recette {

	@ApiModelProperty(value = "id de la recette", notes="Cet id est affecté par l'API lors de la creation de la recette")
	private String id;
	@ApiModelProperty(value = "Le libelle de la recette", required = true)
	private String libelle;
	@ApiModelProperty(value = "Le niveau de difficulté de la recette", required=false, allowableValues="1, 2, 3, 4, 5")
	private int niveau;
	@ApiModelProperty(value = "Le temps nécessaire à la réalisation de la recette en minutes", required=false)
	private int temps;
	@ApiModelProperty(value = "Les étapes necessaires à la réalisation de la recette", required=false)
=======
public class Recette {

	private String id;
	private String libelle;
	private int niveau;
	private int temps;
>>>>>>> inclusion de domain et dao : spring:mvc
	private String recette;

}
