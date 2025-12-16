import java.io.*;
import java.util.*;

public class Main {

    static class Livro {
        int id;
        String titulo;
        String autor;

        Livro(int id, String titulo, String autor) {
            this.id = id;
            this.titulo = titulo;
            this.autor = autor;
        }

        @Override
        public String toString() {
            return id + ";" + titulo + ";" + autor;
        }

        static Livro fromString(String linha) {
            String[] partes = linha.split(";");
            return new Livro(
                Integer.parseInt(partes[0]),
                partes[1],
                partes[2]
            );
        }
    }

    static class Biblioteca {
        List<Livro> livros = new ArrayList<>();
        int proximoId = 1;
        File arquivo = new File("biblioteca.txt");

        void carregar() {
            if (!arquivo.exists()) return;

            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    Livro l = Livro.fromString(linha);
                    livros.add(l);
                    proximoId = Math.max(proximoId, l.id + 1);
                }
            } catch (IOException e) {
                System.out.println("Erro ao carregar arquivo");
            }
        }

        void salvar() {
            try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                for (Livro l : livros) {
                    pw.println(l.toString());
                }
            } catch (IOException e) {
                System.out.println("Erro ao salvar arquivo");
            }
        }

        void cadastrar(String titulo, String autor) {
            livros.add(new Livro(proximoId++, titulo, autor));
            salvar();
        }

        void listar() {
            for (Livro l : livros) {
                System.out.println(l.id + " - " + l.titulo + " / " + l.autor);
            }
        }

        void buscar(String termo) {
            for (Livro l : livros) {
                if (l.titulo.toLowerCase().contains(termo.toLowerCase())) {
                    System.out.println(l.id + " - " + l.titulo + " / " + l.autor);
                }
            }
        }

        void remover(int id) {
            livros.removeIf(l -> l.id == id);
            salvar();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregar();

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar");
            System.out.println("4 - Remover");
            System.out.println("0 - Sair");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }

            if (opcao == 1) {
                System.out.print("Titulo: ");
                String titulo = scanner.nextLine();
                System.out.print("Autor: ");
                String autor = scanner.nextLine();
                biblioteca.cadastrar(titulo, autor);
            }

            if (opcao == 2) {
                biblioteca.listar();
            }

            if (opcao == 3) {
                System.out.print("Buscar: ");
                biblioteca.buscar(scanner.nextLine());
            }

            if (opcao == 4) {
                System.out.print("ID: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine());
                    biblioteca.remover(id);
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido");
                }
            }
        }

        scanner.close();
    }
}
