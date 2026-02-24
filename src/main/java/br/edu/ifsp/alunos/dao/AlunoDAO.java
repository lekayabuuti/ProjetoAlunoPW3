package br.edu.ifsp.alunos.dao;

import br.edu.ifsp.alunos.model.Aluno;
import jakarta.persistence.EntityManager;

public class AlunoDAO {
    private EntityManager em;

    public AlunoDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Aluno aluno) {
        this.em.persist(aluno);
    }
}
