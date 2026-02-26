package br.edu.ifsp.alunos.dao;

import br.edu.ifsp.alunos.model.Aluno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class AlunoDAO {
    private EntityManager em;

    public AlunoDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Aluno aluno){
        em.getTransaction().begin();
        em.persist(aluno);
        em.getTransaction().commit();
    }

    public boolean excluir(String nome){

        String jpql = "SELECT a FROM Aluno a WHERE a.nome = :n";

        List<Aluno> alunos = em.createQuery(jpql, Aluno.class)
                .setParameter("n", nome)
                .getResultList();

        if (alunos.isEmpty()) {
            return false;
        }

        em.getTransaction().begin();
        em.remove(alunos.getFirst());
        em.getTransaction().commit();

        return true;
    }

    public void atualizar(Aluno aluno){
        em.getTransaction().begin();
        em.merge(aluno);
        em.getTransaction().commit();
    }

    public Aluno buscarNome(String nome) {
        try {
            String jpql = "SELECT a FROM Aluno a WHERE a.nome = :n";
            return em.createQuery(jpql, Aluno.class)
                    .setParameter("n", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Aluno> buscarAlunos() {
        String jpql = "SELECT a FROM Aluno a";

        return em.createQuery(jpql, Aluno.class).getResultList();
    }
}
