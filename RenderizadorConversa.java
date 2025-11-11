import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class RenderizadorConversa extends JPanel implements ListCellRenderer<Conversa> {
    private final JLabel rotuloTitulo = new JLabel();
    private final JLabel rotuloMensagem = new JLabel();
    private final JLabel rotuloHorario = new JLabel();
    private final JLabel rotuloSilencio = new JLabel("🔇");

    private static final DateTimeFormatter FORMATADOR_HORARIO = DateTimeFormatter.ofPattern("HH:mm");

    public RenderizadorConversa() {
        setLayout(new BorderLayout(8, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel painelTexto = new JPanel(new BorderLayout());
        painelTexto.setOpaque(false);

        rotuloTitulo.setFont(rotuloTitulo.getFont().deriveFont(Font.BOLD, 14f));
        rotuloMensagem.setForeground(Color.GRAY);
        rotuloMensagem.setFont(rotuloMensagem.getFont().deriveFont(12f));

        painelTexto.add(rotuloTitulo, BorderLayout.NORTH);
        painelTexto.add(rotuloMensagem, BorderLayout.SOUTH);

        JPanel painelDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        painelDireito.setOpaque(false);
        rotuloHorario.setFont(rotuloHorario.getFont().deriveFont(10f));
        rotuloSilencio.setFont(rotuloSilencio.getFont().deriveFont(12f));
        rotuloSilencio.setForeground(Color.GRAY);
        rotuloSilencio.setVisible(false);

        painelDireito.add(rotuloHorario);
        painelDireito.add(rotuloSilencio);

        add(painelTexto, BorderLayout.CENTER);
        add(painelDireito, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Conversa> list, Conversa valor, int indice, boolean isSelecionado, boolean temFoco) {
        rotuloTitulo.setText(valor.titulo);
        rotuloMensagem.setText(valor.ultimaMensagem.isEmpty() ? "Toque para conversar" : valor.ultimaMensagem);
        rotuloHorario.setText(valor.ultimoHorario.format(FORMATADOR_HORARIO));
        rotuloSilencio.setVisible(valor.silenciada);

        if (isSelecionado) {
            setBackground(list.getSelectionBackground());
            rotuloTitulo.setForeground(list.getSelectionForeground());
            rotuloMensagem.setForeground(list.getSelectionForeground());
            rotuloHorario.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            rotuloTitulo.setForeground(list.getForeground());
            rotuloMensagem.setForeground(Color.GRAY);
            rotuloHorario.setForeground(Color.GRAY);
        }

        return this;
    }
}