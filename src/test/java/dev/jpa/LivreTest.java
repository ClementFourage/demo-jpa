package dev.jpa;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LivreTest {

	private EntityManagerFactory emf;
	private EntityManager em;

	@Before
	public void SetUp() {
		this.emf = Persistence.createEntityManagerFactory("demo-jpa-livre");
		this.em = emf.createEntityManager();
	}

	@Test
	public void test_init_jpa() {

		Livre livre = em.find(Livre.class, 1);
		System.out.println(livre);

		TypedQuery<Livre> queryListeLivres = em.createQuery("select l from Livre l where l.titre=:title", Livre.class);
		queryListeLivres.setParameter("title", "Germinal");
		List<Livre> listeLivre = queryListeLivres.getResultList();
		listeLivre.forEach(System.out::println);
	}

	@Test
	public void test_insertion() {
		Livre nouveauLivre = new Livre();

		nouveauLivre.setTitre(RandomStringUtils.randomAlphabetic(10));
		nouveauLivre.setAuteur("Diginamic");

		EntityTransaction tx = this.em.getTransaction();
		tx.begin();
		this.em.persist(nouveauLivre);
		tx.commit();

	}

	@Test
	public void test_recuperer_un_emprunt_par_id() {
		Emprunt emprunt = this.em.find(Emprunt.class, 1);
		System.out.println(emprunt);
	}

	@Test
	public void test_recuperer_un_emprunt_par_client() {

		List<Emprunt> empruntsTrouves = this.em
				.createQuery("select e from Emprunt e where e.client.id=:idClient", Emprunt.class)
				.setParameter("idClient", 2).getResultList();

		empruntsTrouves.forEach(System.out::println);
	}


	@Test
	public void inserer_emprunt_relations_detachees() {
		Emprunt emp = new Emprunt();
		emp.setDateDebut(LocalDateTime.now());
		emp.setDelai(10);

		// livre 1 détaché
		Livre livre1 = new Livre();
		livre1.setId(1);

		// livre 5 détaché
		Livre livre5 = new Livre();
		livre5.setId(5);

		emp.setLivres(Arrays.asList(livre1, livre5));

		// client 1 détaché
		Client client1 = new Client();
		client1.setId(1);

		emp.setClient(client1);

		EntityTransaction tx = this.em.getTransaction();
		tx.begin();
		this.em.persist(emp); // seulement 3 requêtes
		tx.commit();

	}

	@After
	public void tearDown() {

		em.close();
		emf.close();

	}

}