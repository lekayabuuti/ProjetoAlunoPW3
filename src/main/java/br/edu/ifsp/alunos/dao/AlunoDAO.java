package br.edu.ifsp.alunos.dao;

import br.edu.ifsp.alunos.model.Aluno;
import jakarta.persistence.EntityManager;

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

    public boolean excluir(Aluno aluno) {
        Aluno alunoGerenciado = em.find(Aluno.class, aluno.getId());
        if (alunoGerenciado == null) {
            return false;
        }

        em.getTransaction().begin();
        em.remove(alunoGerenciado);
        em.getTransaction().commit();

        return true;
    }

    public void atualizar(Aluno aluno){
        em.getTransaction().begin();
        em.merge(aluno);
        em.getTransaction().commit();
    }

    public List<Aluno> buscarNome(String nome) {
        String jpql = "SELECT a FROM Aluno a WHERE a.nome LIKE :n";

        return em.createQuery(jpql, Aluno.class)
                .setParameter("n", "%" + nome + "%")
                .getResultList();
    }

    public List<Aluno> buscarAlunos() {
        String jpql = "SELECT a FROM Aluno a";

        return em.createQuery(jpql, Aluno.class).getResultList();
    }

    public Aluno buscarPorRa(String ra) {
        String jpql = "SELECT a FROM Aluno a WHERE a.ra LIKE :n";

        return em.createQuery(jpql, Aluno.class)
                .setParameter("n", ra)
                .getSingleResult();
    }
}
