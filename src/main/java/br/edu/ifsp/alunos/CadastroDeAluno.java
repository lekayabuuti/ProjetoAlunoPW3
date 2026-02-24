package br.edu.ifsp.alunos;

import br.edu.ifsp.alunos.model.Aluno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CadastroDeAluno {
    public static void main(String[] args) {
        Aluno aluno1 = new Aluno();
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("aluno");

        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();


        em.persist(aluno1);

        em.getTransaction().commit();

        em.close();
    }
}