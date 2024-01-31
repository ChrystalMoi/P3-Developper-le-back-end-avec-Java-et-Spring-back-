package com.openclassrooms.projet3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// L'annotation @SpringBootTest indique à Spring Boot de créer un contexte
// d'application Spring complet pour le test.

// La classe spécifiée dans l'annotation est utilisée pour déterminer
// comment configurer le contexte
@SpringBootTest(classes = Projet3ApplicationTests.class)
class Projet3ApplicationTests {

	@Test
	void contextLoads() {
	}

}
