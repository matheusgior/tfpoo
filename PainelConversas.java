import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class PainelConversas extends JPanel {
    private final JList<Conversa> listaConversas;
    private final JTextField campoBusca = new JTextField();
    private final ControladorChat controlador;

    public PainelConversas(JList<Conversa> listaConversas, ControladorChat controlador) {
        super(new BorderLayout());
        this.listaConversas = listaConversas;
        this.controlador = controlador;
        this.setPreferredSize(new Dimension(350, 700));

        // painel superior (busca)
        JPanel topoEsquerdo = new JPanel(new BorderLayout(8, 8));
        topoEsquerdo.setBorder(new EmptyBorder(8, 8, 8, 8));
        campoBusca.putClientProperty("JTextField.placeholderText", "Procurar conversas");
        topoEsquerdo.add(campoBusca, BorderLayout.CENTER);
        add(topoEsquerdo, BorderLayout.NORTH);

        //lista de Conversas
        listaConversas.setCellRenderer(new RenderizadorConversa());
        listaConversas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaConversas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Conversa selecionada = listaConversas.getSelectedValue();
                controlador.abrirConversa(selecionada);
            }
        });

        JScrollPane scrollConversas = new JScrollPane(listaConversas);
        scrollConversas.setBorder(BorderFactory.createEmptyBorder());
        add(scrollConversas, BorderLayout.CENTER);

        campoBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarConversas();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarConversas();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarConversas();
            }
        });
    }

    private void filtrarConversas() {
        controlador.filtrarConversas(campoBusca.getText());
    }
}
