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

        // cabeçalho
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBorder(new EmptyBorder(10, 12, 10, 12));
        rotuloTitulo.setFont(rotuloTitulo.getFont().deriveFont(Font.BOLD, 16f));
        cabecalho.add(rotuloTitulo, BorderLayout.CENTER);
        JButton botaoMais = new JButton("⋮");
        cabecalho.add(botaoMais, BorderLayout.EAST);
        add(cabecalho, BorderLayout.NORTH);

        // lista de Mensagens
        listaMensagens.setCellRenderer(new RenderizadorBalao());
        listaMensagens.setFixedCellHeight(-1);
        listaMensagens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMensagens.setBackground(new Color(0xECE5DD));
        JScrollPane scrollMensagens = new JScrollPane(listaMensagens);
        scrollMensagens.getVerticalScrollBar().setUnitIncrement(16);
        scrollMensagens.setBorder(BorderFactory.createEmptyBorder());
        add(scrollMensagens, BorderLayout.CENTER);

        // area de Input
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

        
        botaoEnviar.addActionListener(e -> enviarMensagem());
        input.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enviar");
        input.getActionMap().put("enviar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { enviarMensagem(); }
        });
        input.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "novaLinha");
        input.getActionMap().put("novaLinha", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { input.append("\n"); }
        });

        botaoAnexar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Simular anexar arquivo (implementação futura)", "Anexar", JOptionPane.INFORMATION_MESSAGE));

        // configuração do callback
        controlador.setCallbackAtualizarCabecalho(this::atualizarRotuloTitulo);

        
        listaMensagens.getModel().addListDataListener(new javax.swing.event.ListDataListener() {
            public void intervalAdded(javax.swing.event.ListDataEvent e) { rolarParaBaixo(listaMensagens); }
            public void intervalRemoved(javax.swing.event.ListDataEvent e) { rolarParaBaixo(listaMensagens); }
            public void contentsChanged(javax.swing.event.ListDataEvent e) { rolarParaBaixo(listaMensagens); }
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