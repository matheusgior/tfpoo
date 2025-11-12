import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class PainelMensagens extends JPanel {
    private final JList<Mensagem> listaMensagens;
    private final JTextArea input = new JTextArea(2, 20);
    private final JButton botaoEnviar = new JButton("Enviar");
    private final JButton botaoAnexar = new JButton("📎");
    private final JLabel rotuloTitulo = new JLabel("Selecione uma conversa");

    private final ControladorChat controlador;

    public PainelMensagens(JList<Mensagem> listaMensagens, ControladorChat controlador) {
        super(new BorderLayout());
        this.listaMensagens = listaMensagens;
        this.controlador = controlador;

        // ===== Cabeçalho =====
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBorder(new EmptyBorder(10, 12, 10, 12));
        rotuloTitulo.setFont(rotuloTitulo.getFont().deriveFont(Font.BOLD, 16f));
        cabecalho.add(rotuloTitulo, BorderLayout.CENTER);

        // Botão dos "três pontinhos"
        JButton botaoMais = new JButton("⋮");
        if (!botaoMais.getFont().canDisplay('⋮')) botaoMais.setText("...");
        botaoMais.setFont(botaoMais.getFont().deriveFont(Font.BOLD, 18f));
        botaoMais.setFocusable(false);
        botaoMais.setBorderPainted(false);
        botaoMais.setContentAreaFilled(false);
        botaoMais.setFocusPainted(false);
        botaoMais.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botaoMais.setPreferredSize(new Dimension(28, 24));
        cabecalho.add(botaoMais, BorderLayout.EAST);
        add(cabecalho, BorderLayout.NORTH);

        // 3 pontinhos
        JPopupMenu menuMais = new JPopupMenu();
        JMenuItem itemSilenciar = new JMenuItem();
        itemSilenciar.setFont(itemSilenciar.getFont().deriveFont(Font.PLAIN, 13f));
        menuMais.add(itemSilenciar);

        // silenciar
        itemSilenciar.addActionListener(e -> controlador.alternarSilencio());

        Runnable atualizarEabrirMenu = () -> {
            Conversa atual = controlador.getConversaAtual();
            boolean sil = (atual != null && atual.silenciada);

            String emojiSil = "🔇";
            String emojiAtv = "🔔";
            boolean emojiOk = itemSilenciar.getFont().canDisplayUpTo(emojiSil) == -1
                    && itemSilenciar.getFont().canDisplayUpTo(emojiAtv) == -1;
            if (!emojiOk) {
                emojiSil = "[SIL]";
                emojiAtv = "[SOM]";
            }

            itemSilenciar.setText(sil ? (emojiAtv + "  Ativar som")
                    : (emojiSil + "  Silenciar conversa"));

            menuMais.show(botaoMais, 0, botaoMais.getHeight());
        };

        botaoMais.addActionListener(e -> atualizarEabrirMenu.run());

        //Lista de Mensagens
        listaMensagens.setCellRenderer(new RenderizadorBalao());
        listaMensagens.setFixedCellHeight(-1);
        listaMensagens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMensagens.setBackground(new Color(0xECE5DD));
        JScrollPane scrollMensagens = new JScrollPane(listaMensagens);
        scrollMensagens.getVerticalScrollBar().setUnitIncrement(16);
        scrollMensagens.setBorder(BorderFactory.createEmptyBorder());
        add(scrollMensagens, BorderLayout.CENTER);

        // Área de Input
        JPanel barraInput = new JPanel(new BorderLayout(8, 8));
        barraInput.setBorder(new EmptyBorder(8, 8, 8, 8));
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        JScrollPane scrollInput = new JScrollPane(input);
        scrollInput.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));

        JPanel inputEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        botaoAnexar.setFocusable(false);
        inputEsquerdo.add(botaoAnexar);

        barraInput.add(inputEsquerdo, BorderLayout.WEST);
        barraInput.add(scrollInput, BorderLayout.CENTER);
        barraInput.add(botaoEnviar, BorderLayout.EAST);
        add(barraInput, BorderLayout.SOUTH);

        // ações
        botaoEnviar.addActionListener(e -> enviarMensagem());
        // Enter = enviar
        input.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enviar");
        input.getActionMap().put("enviar", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { enviarMensagem(); }
        });
        input.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                "novaLinha"
        );
        input.getActionMap().put("novaLinha", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { input.append("\n"); }
        });

        botaoAnexar.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Simular anexar arquivo (implementação futura)",
                        "Anexar",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        // callback do título
        controlador.setCallbackAtualizarCabecalho(this::atualizarRotuloTitulo);

        // scroll
        listaMensagens.getModel().addListDataListener(new javax.swing.event.ListDataListener() {
            @Override public void intervalAdded(javax.swing.event.ListDataEvent e) { rolarParaBaixo(listaMensagens); }
            @Override public void intervalRemoved(javax.swing.event.ListDataEvent e) { rolarParaBaixo(listaMensagens); }
            @Override public void contentsChanged(javax.swing.event.ListDataEvent e) { rolarParaBaixo(listaMensagens); }
        });
    }

    private void enviarMensagem() {
        String texto = input.getText();
        if (texto.isBlank()) return;
        controlador.enviarMensagem(texto);
        input.setText("");
    }

    private void atualizarRotuloTitulo(String titulo) {
        rotuloTitulo.setText(titulo);
    }

    private void rolarParaBaixo(JList<?> list) {
        if (list.getModel().getSize() > 0) {
            list.ensureIndexIsVisible(list.getModel().getSize() - 1);
        }
    }
}