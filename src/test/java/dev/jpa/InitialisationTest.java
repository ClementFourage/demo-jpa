package dev.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

public class InitialisationTest {
	
	@Test
	public void test_init_jpa() {
		
		//Etape 1 => Créer l'usine à sessions
		// => Créer une instance d'EntityManagerFactory (EMF)
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo-jpa");
		
		//Etape 2 => Demander à l'usine une session
		//=> L'usine fournit une instance D'EntityManager (session de travail)
		EntityManager em = emf.createEntityManager();
		
		Livre livre = em.find(Livre.class, 1);
		//System.out.println(livre);
		
		TypedQuery<Livre> query = em.createQuery("select l from Livre l where l.nom='Germinal'", Livre.class);
		
		Livre livre1 = query.getResultList().get(0);
		System.out.println(livre1);
		
		em.close();
		emf.close();
		
		
	}

}
