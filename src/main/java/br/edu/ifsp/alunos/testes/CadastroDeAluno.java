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

        EntityManager em = JPAUtil.getEntityManager();

        AlunoDAO dao = new AlunoDAO(em);

        Scanner leitorTeclado = new Scanner(System.in);

        while (true) {
            exibirMenu();
            int opcao = leitorTeclado.nextInt();
            leitorTeclado.nextLine();

            switch (opcao) {
                case 1 -> cadastrarAluno(dao, leitorTeclado);
                case 2 -> excluirAluno(dao, leitorTeclado);
                case 3 -> alterarAluno(dao, leitorTeclado);
                case 4 -> buscarAluno(dao, leitorTeclado);
                case 5 -> listarAlunos(dao);
                case 6 -> {
                    System.out.println("Encerrando programa...");
                    em.close();
                    leitorTeclado.close();
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void exibirMenu() {
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
    }

    private static void cadastrarAluno(AlunoDAO dao, Scanner leitor) {
        System.out.println("CADASTRO DE ALUNO:");
        Aluno aluno = coletaDadosAluno(leitor);

        try {
            dao.cadastrar(aluno);
            System.out.println("Aluno cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: RA ou Email já cadastrado.");
        }
    }

    private static void excluirAluno(AlunoDAO dao, Scanner leitor) {
        System.out.println("Digite o nome do aluno a excluir:");
        String nome = leitor.nextLine();

        List<Aluno> alunos = dao.buscarNome(nome);

        if (alunos.size() > 1) {
            System.out.println("Mais de um aluno encontrado:");
            for (Aluno a : alunos) {
                System.out.println("RA: " + a.getRa() + " | Nome: " + a.getNome());
            }

            System.out.println("Digite o RA do aluno que deseja excluir:");
            String ra = leitor.nextLine();
            boolean existeRA = false;
            for (Aluno aluno : alunos) {
                if (aluno.getRa().equalsIgnoreCase(ra)) {
                    existeRA = true;
                    break;
                }
            }
            if (existeRA)
                alunos = List.of(dao.buscarPorRa(ra));
            else {
                System.out.println("RA não está na lista!");
                return;
            }
        }

        if (dao.excluir(alunos.getFirst())) {
            System.out.println("Aluno removido com sucesso!");
        } else {
            System.out.println("Aluno não encontrado!");
        }
    }

    private static void alterarAluno(AlunoDAO dao, Scanner leitor) {
        System.out.println("Digite o nome do aluno:");
        String nome = leitor.nextLine();

        List<Aluno> alunos = dao.buscarNome(nome);

        if (alunos.isEmpty()) {
            System.out.println("Aluno não encontrado!");
            return;
        }

        if (alunos.size() > 1) {
            System.out.println("Mais de um aluno encontrado:");
            for (Aluno a : alunos) {
                System.out.println("RA: " + a.getRa() + " | Nome: " + a.getNome());
            }

            System.out.println("Digite o RA do aluno que deseja alterar:");
            String ra = leitor.nextLine();
            boolean existeRA = false;
            for (Aluno aluno : alunos) {
                if (aluno.getRa().equalsIgnoreCase(ra)) {
                    existeRA = true;
                    break;
                }
            }
            if (existeRA)
                alunos = List.of(dao.buscarPorRa(ra));
            else {
                System.out.println("RA não está na lista!");
                return;
            }
        }

        if (alunos.getFirst() == null) return;

        Aluno aluno = alunos.getFirst();
        imprimirAluno(aluno);

        System.out.println("\nNOVOS DADOS:");
        System.out.println("Digite o novo nome:");
        aluno.setNome(leitor.nextLine());
        System.out.println("Digite o novo RA:");
        aluno.setRa(leitor.nextLine());

        System.out.println("Digite o novo email:");
        aluno.setEmail(leitor.nextLine());

        System.out.println("Digite a nota 1:");
        aluno.setNota1(leitor.nextBigDecimal());

        System.out.println("Digite a nota 2:");
        aluno.setNota2(leitor.nextBigDecimal());

        System.out.println("Digite a nota 3:");
        aluno.setNota3(leitor.nextBigDecimal());
        leitor.nextLine();

        try {
            dao.atualizar(aluno);
            System.out.println("Aluno alterado com sucesso!\n");
        } catch (Exception e) {
            System.out.println("Erro: RA ou Email já cadastrado.");
        }
    }

    private static void buscarAluno(AlunoDAO dao, Scanner leitor) {
        System.out.println("Digite o nome para busca:");
        String nome = leitor.nextLine();

        List<Aluno> alunos = dao.buscarNome(nome);

        if (alunos.isEmpty()) {
            System.out.println("Aluno não encontrado!");
        } else {
            alunos.forEach(CadastroDeAluno::imprimirAluno);
        }
    }

    private static void listarAlunos(AlunoDAO dao) {
        List<Aluno> alunos = dao.buscarAlunos();

        for (Aluno a : alunos) {
            BigDecimal media = calcularMedia(a);
            String situacao = verificarSituacao(media);

            imprimirAluno(a);
            System.out.println("Média: " + media);
            System.out.println("Situação: " + situacao);
            System.out.println("----------------------------");
        }
    }

    private static BigDecimal calcularMedia(Aluno a) {
        return a.getNota1()
                .add(a.getNota2())
                .add(a.getNota3())
                .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
    }

    private static String verificarSituacao(BigDecimal media) {
        if (media.compareTo(BigDecimal.valueOf(4)) < 0)
            return "Reprovado";
        else if (media.compareTo(BigDecimal.valueOf(6)) < 0)
            return "Recuperação";
        else
            return "Aprovado";
    }

    private static Aluno coletaDadosAluno(Scanner leitor) {
        System.out.println("Digite o nome:");
        String nome = leitor.nextLine();

        System.out.println("Digite o RA:");
        String ra = leitor.nextLine();

        System.out.println("Digite o email:");
        String email = leitor.nextLine();

        System.out.println("Digite a nota 1:");
        BigDecimal nota1 = leitor.nextBigDecimal();

        System.out.println("Digite a nota 2:");
        BigDecimal nota2 = leitor.nextBigDecimal();

        System.out.println("Digite a nota 3:");
        BigDecimal nota3 = leitor.nextBigDecimal();
        leitor.nextLine();

        return new Aluno(nome, ra, email, nota1, nota2, nota3);
    }

    private static void imprimirAluno(Aluno aluno) {
        System.out.println("\nDados do aluno:");
        System.out.println("Nome: " + aluno.getNome());
        System.out.println("RA: " + aluno.getRa());
        System.out.println("Email: " + aluno.getEmail());
        System.out.println("Notas: " +
                aluno.getNota1() + " - " +
                aluno.getNota2() + " - " +
                aluno.getNota3());
    }
}