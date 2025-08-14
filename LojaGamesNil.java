import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Arrays;
import java.util.List;

public class LojaGamesNil extends JFrame {
    private JList<Game> listaGames;
    private JList<Game> listaComprados;
    private JLabel labelSaldo;
    private Cliente clienteAtual;
    private DefaultListModel<Game> modeloDisponiveis;
    private DefaultListModel<Game> modeloComprados;

    private JTextField campoNomeCliente;
    private JTextField campoSaldoInicial;
    private JTextField campoNomeJogo;
    private JTextField campoPrecoJogo;
    private JTextField campoCategoriaJogo;
    private JTextField campoClassificacaoJogo;

    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(PT_BR);

    public LojaGamesNil() {
        // Janela
        setTitle("Loja de Games - Java OOP");
        setSize(1200, 800); // Aumentei o tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Cliente inicial
        clienteAtual = new Cliente("Nilson", "nilson@teste.com", new BigDecimal("0.00"));

        // Componentes
        inicializarComponentes();
        criarGamesIniciais();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarComponentes() {
        // Painel para inputs do cliente
        JPanel painelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelCliente.add(new JLabel("Nome:"));
        campoNomeCliente = new JTextField(clienteAtual.getNome(), 10);
        painelCliente.add(campoNomeCliente);

        painelCliente.add(new JLabel("Saldo Inicial:"));
        campoSaldoInicial = new JTextField(clienteAtual.getSaldo().toString(), 8);
        painelCliente.add(campoSaldoInicial);

        JButton btnAtualizarCliente = new JButton("Atualizar Dados");
        btnAtualizarCliente.addActionListener(e -> atualizarDadosCliente());
        painelCliente.add(btnAtualizarCliente);

        labelSaldo = new JLabel("Saldo: " + MOEDA.format(clienteAtual.getSaldo()));
        painelCliente.add(labelSaldo);
        add(painelCliente, BorderLayout.NORTH);

        // Painel Esquerda: lista de games disponíveis
        modeloDisponiveis = new DefaultListModel<>();
        listaGames = new JList<>(modeloDisponiveis);
        listaGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaGames);
        scrollLista.setPreferredSize(new Dimension(380, 420));
        JPanel painelEsquerda = new JPanel(new BorderLayout(5, 5));
        painelEsquerda.add(new JLabel("Games disponíveis:"), BorderLayout.NORTH);
        painelEsquerda.add(scrollLista, BorderLayout.CENTER);
        add(painelEsquerda, BorderLayout.WEST);

        // Painel Centro: botões e inputs de novo jogo
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));

        // Botões de compra
        JButton btnComprar = new JButton("Comprar selecionado");
        btnComprar.addActionListener(e -> comprarGameSelecionado());
        JButton btnComprarMax = new JButton("Comprar o máximo");
        btnComprarMax.addActionListener(e -> comprarMaximo());
        
        painelCentro.add(Box.createVerticalStrut(20));
        painelCentro.add(btnComprar);
        painelCentro.add(Box.createVerticalStrut(20));
        painelCentro.add(btnComprarMax);
        painelCentro.add(Box.createVerticalStrut(20));

        // Painel para adicionar novo jogo
        JPanel painelAdicionarJogo = new JPanel(new GridLayout(5, 2, 5, 5));
        painelAdicionarJogo.setBorder(BorderFactory.createTitledBorder("Adicionar Novo Jogo"));
        
        painelAdicionarJogo.add(new JLabel("Nome:"));
        campoNomeJogo = new JTextField();
        painelAdicionarJogo.add(campoNomeJogo);

        painelAdicionarJogo.add(new JLabel("Preço:"));
        campoPrecoJogo = new JTextField();
        painelAdicionarJogo.add(campoPrecoJogo);

        painelAdicionarJogo.add(new JLabel("Categoria:"));
        campoCategoriaJogo = new JTextField();
        painelAdicionarJogo.add(campoCategoriaJogo);

        painelAdicionarJogo.add(new JLabel("Classificação:"));
        campoClassificacaoJogo = new JTextField();
        painelAdicionarJogo.add(campoClassificacaoJogo);
        
        JButton btnAdicionarJogo = new JButton("Adicionar Jogo");
        btnAdicionarJogo.addActionListener(e -> adicionarNovoJogo());
        painelAdicionarJogo.add(new JLabel("")); // Campo vazio para alinhar o botão
        painelAdicionarJogo.add(btnAdicionarJogo);

        painelCentro.add(painelAdicionarJogo);

        add(painelCentro, BorderLayout.CENTER);

        // Painel Direita: lista de games comprados
        modeloComprados = new DefaultListModel<>();
        listaComprados = new JList<>(modeloComprados);
        JScrollPane scrollComprados = new JScrollPane(listaComprados);
        scrollComprados.setPreferredSize(new Dimension(380, 420));
        JPanel painelDireita = new JPanel(new BorderLayout(5, 5));
        painelDireita.add(new JLabel("Games comprados:"), BorderLayout.NORTH);
        painelDireita.add(scrollComprados, BorderLayout.CENTER);
        add(painelDireita, BorderLayout.EAST);

        // Duplo clique para comprar
        listaGames.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) comprarGameSelecionado();
            }
        });
    }

    private void atualizarDadosCliente() {
        try {
            String novoNome = campoNomeCliente.getText();
            BigDecimal novoSaldo = new BigDecimal(campoSaldoInicial.getText());

            // A classe Cliente precisa de métodos setNome e setSaldo ou ser recriada.
            // Assumindo que você adicione setters na classe Cliente:
            clienteAtual = new Cliente(novoNome, clienteAtual.getEmail(), novoSaldo);

            atualizarSaldo();
            JOptionPane.showMessageDialog(this, "Dados do cliente atualizados com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um saldo válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarNovoJogo() {
        try {
            String nome = campoNomeJogo.getText();
            double preco = Double.parseDouble(campoPrecoJogo.getText());
            String categoria = campoCategoriaJogo.getText();
            int classificacao = Integer.parseInt(campoClassificacaoJogo.getText());

            if (nome.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Game novoGame = new Game(nome, preco, categoria, classificacao);
            modeloDisponiveis.addElement(novoGame);
            
            // Limpa os campos após a adição
            campoNomeJogo.setText("");
            campoPrecoJogo.setText("");
            campoCategoriaJogo.setText("");
            campoClassificacaoJogo.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores válidos para preço e classificação.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarGamesIniciais() {
        List<Game> iniciais = Arrays.asList(
                new Game("Minecraft", 89.90, "Aventura", 10),
                new Game("FIFA 2023", 199.90, "Esporte", 0),
                new Game("The Sims 4", 129.90, "Simulação", 12),
                new Game("Grand Theft Auto V", 149.90, "Ação", 18),
                new Game("Fortnite", 0.0, "Battle Royale", 12),
                new Game("Hades", 79.90, "Roguelike", 14),
                new Game("Stardew Valley", 24.99, "Simulação", 0),
                new Game("Celeste", 36.90, "Plataforma", 10),
                new Game("Among Us", 19.99, "Party", 7),
                new Game("Cyberpunk 2077", 149.99, "RPG", 18),
                new Game("Valorant", 0.00, "FPS", 14),
                new Game("Rocket League", 39.99, "Esporte", 3)
        );
        for (Game g : iniciais) {
            modeloDisponiveis.addElement(g);
        }
    }

    private void comprarGameSelecionado() {
        Game gameSelecionado = listaGames.getSelectedValue();
        if (gameSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um game para comprar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean ok = clienteAtual.comprarGame(gameSelecionado);
        if (ok) {
            modeloComprados.addElement(gameSelecionado);
            modeloDisponiveis.removeElement(gameSelecionado);
            atualizarSaldo();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Saldo insuficiente para comprar " + gameSelecionado.getNome() +
                            ". Saldo atual: " + MOEDA.format(clienteAtual.getSaldo()),
                    "Saldo insuficiente", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void comprarMaximo() {
        java.util.List<Game> disponiveis = new java.util.ArrayList<>();
        for (int i = 0; i < modeloDisponiveis.size(); i++) {
            disponiveis.add(modeloDisponiveis.getElementAt(i));
        }

        clienteAtual.comprarMaximo(disponiveis);

        for (Game g : clienteAtual.getGamesComprados()) {
            if (modeloDisponiveis.contains(g) && !modeloComprados.contains(g)) {
                modeloDisponiveis.removeElement(g);
                modeloComprados.addElement(g);
            }
        }
        atualizarSaldo();
    }

    private void atualizarSaldo() {
        labelSaldo.setText("Saldo: " + MOEDA.format(clienteAtual.getSaldo()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LojaGamesNil::new);
    }
}