package org.example.view;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.repository.GerenteRepository;
import org.example.model.repository.MotoristaRepository;
import org.example.model.repository.PassageiroRepository;
import org.example.model.service.GerenteService;
import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;
import org.example.model.service.VeiculoService;
import org.example.model.entity.Passageiro;

import java.util.Scanner;

public class CLIView {

    private final PassageiroService passageiroManager;
    private final MotoristaService motoristaManager;
    private final GerenteService gerenteManager;
    private final VeiculoService veiculoManager;


    public CLIView() {
        this.passageiroManager = new PassageiroService();
        this.motoristaManager = new MotoristaService();
        this.gerenteManager = new GerenteService();
        this.veiculoManager = new VeiculoService();
        PassageiroRepository passageiroRepository = new PassageiroRepository();
        MotoristaRepository motoristaRepository = new MotoristaRepository();
        GerenteRepository gerenteRepository = new GerenteRepository();
        Scanner scanner = new Scanner(System.in);
    }
    PassageiroRepository passageiro = new PassageiroRepository();
    MotoristaRepository motorista = new MotoristaRepository();
    GerenteRepository gerente = new GerenteRepository();
    public void iniciar() {
        gerenteManager.criar("Lucas", "lucas3@gmail.com", "senha333", "12345678910", "83987380339");
        gerenteManager.criar("Manoel", "Manoel3@gmail.com", "senha333", "12345678911", "83987380320");
        gerenteManager.criar("Mateus", "Mateus3@gmail.com", "senha333", "12345678912", "83987380360");
        gerenteManager.criar("Eric", "Eric3@gmail.com", "senha333", "12345678913", "83987380370");


        passageiroManager.login("lucas1@gmail.com", "senha123");
        passageiroManager.login("Manoel1@gmail.com", "senha123");
        passageiroManager.login("Mateus1@gmail.com", "senha123");
        passageiroManager.login("Eric1@gmail.com", "senha123");


        motoristaManager.login("lucas2@gmail.com", "senha123");
        motoristaManager.login("Manoel2@gmail.com", "senha123");
        motoristaManager.login("Mateus2@gmail.com", "senha123");
        motoristaManager.login("Eric2@gmail.com", "senha123");

        gerenteManager.login("lucas1@gmail.com", "senha123");
        gerenteManager.login("Manoel1@gmail.com", "senha123");
        gerenteManager.login("Mateus1@gmail.com", "senha123");
        gerenteManager.login("Eric1@gmail.com","senha123");

        veiculoManager.criar("ABC-1234", "Onix", "Chevrolet", 2023, CategoriaVeiculo.UBER_X, "Prata");
        veiculoManager.criar("XYZ-3003", "Corolla", "Toyota", 2024, CategoriaVeiculo.UBER_XL, "Preto");
        veiculoManager.criar("JKL-6006", "Duster", "Renault", 2023, CategoriaVeiculo.UBER_BAG, "Marrom");
        veiculoManager.criar("KLZ-7384", "911", "Porsche", 2019, CategoriaVeiculo.UBER_BLACK, "Vermelho");

        Scanner scanner = new Scanner(System.in);
        int opt;

        do {
            exibirMenuPrincipal();
            opt = scanner.nextInt();
            scanner.nextLine();

            switch (opt) {
                case 1:
                    exibirMenuUsuario();
                    int optUsuario = scanner.nextInt();
                    scanner.nextLine();
                    switch (optUsuario) {
                        case 1:

                            System.out.println("Digite o email cadastrado: ");
                            String emailUsuario = scanner.next();
                            System.out.println("e a senha cadastrada");
                            String senhaUsuario = scanner.next();
                            if (passageiro.realizarLogin(emailUsuario, senhaUsuario)) {
                                System.out.println("1.  Solicitar corrida");
                                System.out.println("0.  Voltar");
                                int optUsuarioLogado = scanner.nextInt();
                                scanner.nextLine();
                                switch (optUsuarioLogado) {
                                    case 1:
                                        System.out.println("Selecione a origem (coord X e Y) ex: CoordX: 50 CoordY: 100");
                                        System.out.println("Coordenada X: ");
                                        int origemX = scanner.nextInt();
                                        System.out.println("Coordenada Y: ");
                                        int origemY = scanner.nextInt();
                                        System.out.println("Selecione o destino (coord X e Y) ex: CoordX: 50 CoordY: 100");
                                        System.out.println("Coordenada X: ");
                                        int destinoX = scanner.nextInt();
                                        System.out.println("Coordenada Y: ");
                                        int destinoY = scanner.nextInt();


                                        System.out.println("Selecione a categoria desejada");
                                        System.out.println("1.  UBERX");
                                        System.out.println("2.  Uber Comfort");
                                        System.out.println("3.  UBER Black");
                                        System.out.println("4.  UBER Bag");
                                        System.out.println("5.  UBER XL");

                                        int optCategoria;

                                        optCategoria = scanner.nextInt();
                                        scanner.nextLine();
                                        switch (optCategoria) {
                                            case 1:
                                                //Veiculo.setCategoria(CategoriaVeiculo.UBER_X);//alterar posteriormente (categoriaVeiculo  foi colocado como static por causa desse uso, mudar depois)
                                                break;
                                            case 2:
                                                //Veiculo.setCategoria(CategoriaVeiculo.UBER_COMFORT);//idem
                                                break;
                                            case 3:
                                                //Veiculo.setCategoria(CategoriaVeiculo.UBER_BLACK);//idem
                                                break;
                                            case 4:
                                                //Veiculo.setCategoria(CategoriaVeiculo.UBER_BAG);//idem
                                                break;
                                            case 5:
                                                //Veiculo.setCategoria(CategoriaVeiculo.UBER_XL);//idem
                                                break;
                                            case 0:
                                                break;
                                            default:
                                                break;
                                        }
                                        //Corrida.setStatus(StatusCorrida.SOLICITADA); //alterar posteriormente (setStatus  foi colocado como static por causa desse uso, mudar depois)
                                        System.out.println("Corrida Solicitada");
                                        System.out.println("Procurando motorista...");


                                    case 0:
                                        break;
                                }
                            }

                            break;
                        case 2:
                            System.out.println("Digite o seu nome de usuário: ");
                            String novoNomeUsuario = scanner.next();
                            System.out.println("Digite o email  que deseja cadastrar: ");
                            String novoEmailUsuario = scanner.next();
                            System.out.println("Crie sua senha");
                            String novaSenhaUsuario = scanner.next();
                            System.out.println("Digite o seu cpf");
                            String novoCpfUsuario = scanner.next();
                            System.out.println("Digite o seu número para contato");
                            String novoContatoUsuario = scanner.next();

                            passageiroManager.criar(novoNomeUsuario, novoEmailUsuario,novaSenhaUsuario, novoCpfUsuario, novoContatoUsuario);
                            break;
                        case 0:
                            break;
                    }
                    break;
                case 2:
                    exibirMenuMotorista();
                    int optMotorista = scanner.nextInt();
                    scanner.nextLine();
                    switch (optMotorista) {
                        case 1:
                            System.out.println("Digite o email cadastrado: ");
                            String emailMotorista = scanner.next();
                            System.out.println("e a senha cadastrada");
                            String senhaMotorista = scanner.next();
                            ;
                            if (motorista.realizarLogin(emailMotorista, senhaMotorista)) {
                                System.out.println("1.  Iniciar");
                                System.out.println("2.  Configurar veículo");
                                System.out.println("0.  Voltar");
                                int optMotoristaLogado = scanner.nextInt();
                                scanner.nextLine();
                                switch (optMotoristaLogado) {
                                    case 1:
                                        System.out.println("esperando solicitação de corrida do passageiro");
                                        boolean notificado = false;
                                        if(notificado){
                                            System.out.println("1.  Aceitar");
                                            System.out.println("2.  Recusar");
                                            System.out.println("0.  Voltar");
                                            int optMotoristaSolic = scanner.nextInt();
                                            scanner.nextLine();

                                            switch (optMotoristaSolic) {

                                                case 1:
                                                    //Corrida.setStatus(StatusCorrida.ACEITA);
                                                    int optMotoristaCorrida = scanner.nextInt();
                                                    scanner.nextLine();
                                                    System.out.println("1.  Finalizar Corrida");
                                                    System.out.println("2.  Cancelar");
                                                    switch (optMotoristaCorrida) {
                                                        case 1:
                                                            //Corrida.setStatus(StatusCorrida.FINALIZADA);
                                                            break;
                                                        case 2:
                                                            break;



                                                    }
                                                case 2:
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        break;
                                    case 2:
                                        break;
                                    default:
                                        break;
                                }
                            }

                        case 2:
                            System.out.println("Digite o seu nome de usuário: ");
                            String novoNomeMotorista = scanner.next();
                            System.out.println("Digite o email  que deseja cadastrar: ");
                            String novoEmailMotorista = scanner.next();
                            System.out.println("Crie sua senha");
                            String novaSenhaMotorista = scanner.next();
                            System.out.println("Digite o seu cpf");
                            String novoCpfMotorista = scanner.next();
                            System.out.println("Digite o seu número para contato");
                            String novoContatoMotorista = scanner.next();

                            motoristaManager.criar(novoNomeMotorista, novoEmailMotorista,novaSenhaMotorista, novoCpfMotorista, novoContatoMotorista);
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:
                    exibirMenuGerente();
                    int optGerente = scanner.nextInt();
                    scanner.nextLine();
                    switch (optGerente) {
                        case 1:
                            System.out.println("Digite o email cadastrado: ");
                            String emailGerente = scanner.next();
                            System.out.println("e a senha cadastrada");
                            String senhaGerente = scanner.next();
                            gerente.realizarLogin(emailGerente,senhaGerente);
                            break;
                        case 2:
                            System.out.println("Para cadastrar um novo gerente confirme que voce é um gerente");
                            System.out.println("Digite o email cadastrado: ");
                            String verEmailGerente = scanner.next();
                            System.out.println("e a senha cadastrada");
                            String verSenhaGerente = scanner.next();

                            if (gerente.realizarLogin(verEmailGerente, verSenhaGerente)){
                                System.out.println("Digite o seu nome de usuário: ");
                                String novoNomeGerente = scanner.next();
                                System.out.println("Digite o email  que deseja cadastrar: ");
                                String novoEmailGerente = scanner.next();
                                System.out.println("Crie sua senha");
                                String novaSenhaGerente = scanner.next();
                                System.out.println("Digite o seu cpf");
                                String novoCpfGerente = scanner.next();
                                System.out.println("Digite o seu número para contato");
                                String novoContatoGerente = scanner.next();

                                gerenteManager.criar(novoNomeGerente, novoEmailGerente,novaSenhaGerente, novoCpfGerente, novoContatoGerente);
                            }
                            break;
                        case 0:
                            break;
                    }
                    break;
                case 0:
                    System.out.println("Aplicação encerrada");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opt != 0);

        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("1.  Sou Usuário");
        System.out.println("2.  Sou Motorista");
        System.out.println("3.  Sou Gerente");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void exibirMenuUsuario() {
        System.out.println("          Usuário          ");
        System.out.println("1.  login");
        System.out.println("2.  logon");
        System.out.println("0.  voltar");
        System.out.print("Escolha uma opção: ");
    }


    private static void exibirMenuMotorista() {
        System.out.println("          Motorista          ");
        System.out.println("1.  login");
        System.out.println("2.  logon");
        System.out.println("0.  voltar");
        System.out.print("Escolha uma opção: ");
    }

    private static void exibirMenuGerente() {
        System.out.println("          Gerente          ");
        System.out.println("1.  login");
        System.out.println("2.  logon");
        System.out.println("0.  voltar");
        System.out.print("Escolha uma opção: ");
    }


}