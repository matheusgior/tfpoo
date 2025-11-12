import javax.swing.*;
import java.awt.*;

public class AplicativoChat {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new JanelaChat().setVisible(true);
        });
    }
}

// janela principal
class JanelaChat extends JFrame {
    private final DefaultListModel<Conversa> modeloConversas = new DefaultListModel<>();
    private final JList<Conversa> listaConversas = new JList<>(modeloConversas);

    private final DefaultListModel<Mensagem> modeloMensagens = new DefaultListModel<>();
    private final JList<Mensagem> listaMensagens = new JList<>(modeloMensagens);

    private final ControladorChat controlador;

    JanelaChat() {
        super("Whatsapp-POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setLocationRelativeTo(null);

        // inicializa o Controlador
        controlador = new ControladorChat(modeloConversas, modeloMensagens);

        controlador.setCallbackRepintarListaConversas(listaConversas::repaint);

        // menu do MenuApp
        MenuApp menuApp = new MenuApp(this, () -> criarConversa(), () -> alternarSilencio());
        setJMenuBar(menuApp.criarMenu());

        // criação dos paineis
        PainelConversas painelEsquerdo = new PainelConversas(listaConversas, controlador);
        PainelMensagens painelDireito = new PainelMensagens(listaMensagens, controlador);

        // divisor principal
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        split.setDividerLocation(320);
        getContentPane().add(split);

        // inicialização de dados e seleção
        controlador.iniciarDados();
        listaConversas.setSelectedIndex(0);
    }

    // metodos de UI
    private void criarConversa() {
        String nome = JOptionPane.showInputDialog(this, "Nome da conversa:");
        if (nome == null || nome.isBlank()) return;
        controlador.criarConversa(nome);
        listaConversas.setSelectedIndex(0);
    }

    private void alternarSilencio() {
        controlador.alternarSilencio();
    }
}
