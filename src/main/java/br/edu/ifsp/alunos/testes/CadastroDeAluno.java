package br.edu.ifsp.alunos.testes;

import br.edu.ifsp.alunos.dao.AlunoDAO;
import br.edu.ifsp.alunos.model.Aluno;
import br.edu.ifsp.alunos.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class CadastroDeAluno {
    public static void main(String[] args) {
        //Aluno aluno1 = new Aluno("Maria", "123456", "mariazinha@gmail.com", BigDecimal.valueOf(10), BigDecimal.valueOf(8), BigDecimal.valueOf(6));

        EntityManager em = JPAUtil.getEntityManager();

        AlunoDAO dao = new AlunoDAO(em);

        Scanner leitorTeclado = new Scanner(System.in);

        while(true){
            System.out.println("""
                 ** CADASTRO DE ALUNOS **
                 
                 1 - Cadastrar aluno
                 2 - Excluir aluno
                 3 - Alterar aluno
                 4 - Buscar aluno pelo nome
                 5 - Listar alunos (com status aprovação)
                 6 - FIM   
                 
                 Digite a opção desejada: 
            """);

            int option = leitorTeclado.nextInt();
            leitorTeclado.nextLine();

            switch (option) {
                case 1:
                    System.out.println("CADASTRO DE ALUNO: \nDigite o nome: ");
                    String nome = leitorTeclado.nextLine();
                    System.out.println("Digite o RA: ");
                    String ra = leitorTeclado.nextLine();
                    System.out.println("Digite o email: ");
                    String email = leitorTeclado.nextLine();
                    System.out.println("Digite a nota 1: ");
                    BigDecimal nota1 = leitorTeclado.nextBigDecimal();
                    System.out.println("Digite a nota 2: ");
                    BigDecimal nota2 = leitorTeclado.nextBigDecimal();
                    System.out.println("Digite a nota 3: ");
                    BigDecimal nota3 = leitorTeclado.nextBigDecimal();
                    leitorTeclado.nextLine();
                    Aluno aluno = new Aluno(nome, ra, email, nota1, nota2, nota3);
                        dao.cadastrar(aluno);
                        System.out.println("Aluno cadastrado com sucesso!");
                    break;

                case 2:
                    System.out.println("EXCLUIR ALUNO: \nDigite o nome: ");
                    String nomeExcluir = leitorTeclado.nextLine();
                    if (dao.excluir(nomeExcluir)){
                        System.out.println("Aluno removido com sucesso!");
                    } else {
                        System.out.println("Aluno não encontrado!");
                    }
                    break;

                case 3:
                    System.out.println("ALTERAR ALUNO: \nDigite o nome: ");
                    String nomeAlterar = leitorTeclado.nextLine();
                    Aluno alunoAlterar = dao.buscarNome(nomeAlterar);
                        if (alunoAlterar != null){
                            System.out.println("Dados do aluno:");
                            System.out.println("Nome: " + alunoAlterar.getNome());
                            System.out.println("Email: " + alunoAlterar.getEmail());
                            System.out.println("RA: " + alunoAlterar.getRa());
                            System.out.println("Notas: " + alunoAlterar.getNota1() + " - " + alunoAlterar.getNota2() + " - " + alunoAlterar.getNota3());

                        System.out.println("NOVOS DADOS:");

                        System.out.println("Digite o nome: ");
                        alunoAlterar.setNome(leitorTeclado.nextLine());

                        System.out.println("Digite o RA: ");
                        alunoAlterar.setRa(leitorTeclado.nextLine());

                        System.out.println("Digite o email: ");
                        alunoAlterar.setEmail(leitorTeclado.nextLine());

                        System.out.println("Digite a nota 1: ");
                        alunoAlterar.setNota1(leitorTeclado.nextBigDecimal());

                        System.out.println("Digite a nota 2: ");
                        alunoAlterar.setNota2(leitorTeclado.nextBigDecimal());

                        System.out.println("Digite a nota 3: ");
                        alunoAlterar.setNota3(leitorTeclado.nextBigDecimal());
                        leitorTeclado.nextLine();

                        dao.atualizar(alunoAlterar);
                        System.out.println("Aluno alterado com sucesso!");

                    } else {
                        System.out.println("Aluno não encontrado!");
                    }
                    break;

                case 4:
                    System.out.println("CONSULTAR ALUNO: \nDigite o nome: ");
                    String nomeConsultar = leitorTeclado.nextLine();
                    Aluno alunoConsultar = dao.buscarNome(nomeConsultar);
                    if (alunoConsultar != null) {
                        System.out.println("Dados do aluno:");
                        System.out.println("Nome: " + alunoConsultar.getNome());
                        System.out.println("Email: " + alunoConsultar.getEmail());
                        System.out.println("RA: " + alunoConsultar.getRa());
                        System.out.println("Notas: " + alunoConsultar.getNota1() + " - " + alunoConsultar.getNota2() + " - " + alunoConsultar.getNota3());
                    } else {
                        System.out.println("Aluno não encontrado!");
                    }
                    break;

                case 5:
                    System.out.println("EXIBINDO TODOS OS ALUNOS: \n");
                    List<Aluno> alunos = dao.buscarAlunos();
                    alunos.forEach(a -> {
                        BigDecimal media = a.getNota1().add(a.getNota2()).add(a.getNota3()).divide(BigDecimal.valueOf(3), RoundingMode.CEILING);
                        String resultado;

                        if (media.compareTo(BigDecimal.valueOf(4)) < 0) {
                            resultado = "Reprovado";
                        } else if(media.compareTo(BigDecimal.valueOf(6)) < 0) {
                            resultado = "Recuperação";
                        }else{
                            resultado = "Aprovado";
                        }
                        System.out.println("Nome: " + a.getNome());
                        System.out.println("Email: " + a.getEmail());
                        System.out.println("RA: " + a.getRa());
                        System.out.println("Notas: " + a.getNota1() + " - " + a.getNota2() + " - " + a.getNota3());
                        System.out.println("Media: " + media);
                        System.out.println("Situação: " + resultado);
                    });
                    break;

                case 6:
                    System.out.println("Encerrando programa!");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        }
    }
}