import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Cliente {
    private final String nome;
    private final String email;
    private BigDecimal saldo;
    private final List<Game> gamesComprados;

    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(PT_BR);

    public Cliente(String nome, String email, BigDecimal saldoInicial) {
        this.nome = nome;
        this.email = email;
        this.saldo = saldoInicial;
        this.gamesComprados = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public BigDecimal getSaldo() { return saldo; }
    public List<Game> getGamesComprados() {
        return Collections.unmodifiableList(gamesComprados);
    }

    public boolean comprarGame(Game game) {
        if (game == null) return false;
        if (saldo.compareTo(game.getPreco()) >= 0) {
            gamesComprados.add(game);
            saldo = saldo.subtract(game.getPreco());
            System.out.println(nome + " comprou: " + game.getNome() +
                    " por " + MOEDA.format(game.getPreco()));
            return true;
        } else {
            System.out.println(nome + " não tem saldo suficiente para: " + game.getNome());
            return false;
        }
    }

    /** Compra o MAIOR número possível de games dado o saldo atual (guloso por preço). */
    public void comprarMaximo(List<Game> disponiveis) {
        if (disponiveis == null || disponiveis.isEmpty()) return;
        List<Game> ordenados = new ArrayList<>(disponiveis);
        ordenados.sort(Comparator.comparing(Game::getPreco));

        for (Game g : ordenados) {
            if (saldo.compareTo(g.getPreco()) >= 0) {
                comprarGame(g);
            }
        }
    }
}