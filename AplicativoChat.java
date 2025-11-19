import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class AplicativoChat {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new JanelaChat().setVisible(true);
        });
    }
}

class JanelaChat extends JFrame {

    private final DefaultListModel<Conversa> modeloConversas = new DefaultListModel<>();
    private final JList<Conversa> listaConversas = new JList<>(modeloConversas);

    private final DefaultListModel<Mensagem> modeloMensagens = new DefaultListModel<>();
    private final JList<Mensagem> listaMensagens = new JList<>(modeloMensagens);

    private final ControladorChat controlador;

    public JanelaChat() {
        super("Whatsapp-POO");

        try {
            Image icon = ImageIO.read(new File("whatsapp-logo-png.png"));
            if (icon != null) {
                setIconImage(icon);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone da janela: " + e.getMessage());
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setLocationRelativeTo(null);

        controlador = new ControladorChat(modeloConversas, modeloMensagens);
        controlador.setCallbackRepintarListaConversas(listaConversas::repaint);

        MenuApp menuApp = new MenuApp(
                this,
                e -> criarConversa(),
                e -> alternarSilencio(),
                e -> criarGrupo()
        );
        setJMenuBar(menuApp.criarMenu());

        PainelConversas painelEsquerdo = new PainelConversas(listaConversas, controlador);
        PainelMensagens painelDireito = new PainelMensagens(listaMensagens, controlador);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        split.setDividerLocation(320);
        getContentPane().add(split);

        controlador.iniciarDados();
        listaConversas.setSelectedIndex(0);
    }

    private void criarConversa() {
        String nome = JOptionPane.showInputDialog(this, "Nome da conversa:");
        if (nome == null || nome.isBlank()) return;

        controlador.criarConversa(nome);
        listaConversas.setSelectedIndex(0);
    }

    private void alternarSilencio() {
        controlador.alternarSilencio();
    }

    private void criarGrupo() {
        java.util.List<Conversa> contatos = controlador.getTodasConversas();

        if (contatos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum contato disponível.");
            return;
        }

        String[] nomes = contatos.stream()
                .map(c -> c.titulo)
                .toArray(String[]::new);

        JList<String> lista = new JList<>(nomes);
        lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        int op = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(lista),
                "Selecionar participantes",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                getIconImage() != null ? new ImageIcon(getIconImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)) : null
        );

        if (op != JOptionPane.OK_OPTION) return;

        java.util.List<String> selecionados = lista.getSelectedValuesList();
        if (selecionados.isEmpty()) return;

        String nomeGrupo = JOptionPane.showInputDialog(this, "Nome do grupo:");
        if (nomeGrupo == null || nomeGrupo.isBlank()) return;

        java.util.List<Conversa> membros = new java.util.ArrayList<>();
        for (Conversa c : contatos) {
            if (selecionados.contains(c.titulo)) membros.add(c);
        }

        controlador.criarGrupo(nomeGrupo, membros);
        listaConversas.setSelectedIndex(0);
    }
}
