package br.edu.ifsp.alunos.testes;

import br.edu.ifsp.alunos.dao.AlunoDAO;
import br.edu.ifsp.alunos.model.Aluno;
import br.edu.ifsp.alunos.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;

public class CadastroDeAluno {
    public static void main(String[] args) {
        Aluno aluno1 = new Aluno();
        aluno1.setNome("Maria");
        aluno1.setEmail("mariazinha@gmail.com");
        aluno1.setRa("123456");
        aluno1.setNota1(BigDecimal.valueOf(10));
        aluno1.setNota2(BigDecimal.valueOf(8));
        aluno1.setNota3(BigDecimal.valueOf(6));

        EntityManager em = JPAUtil.getEntityManager();

        AlunoDAO dao = new AlunoDAO(em);

        em.getTransaction().begin();

        dao.cadastrar(aluno1);

        em.getTransaction().commit();
        em.close();
    }
}