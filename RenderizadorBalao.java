import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class RenderizadorBalao extends JPanel implements ListCellRenderer<Mensagem> {
    private final JTextArea areaMensagem = new JTextArea();
    private final JLabel rotuloHorario = new JLabel();
    private final JPanel balao = new JPanel(new BorderLayout());

    private static final DateTimeFormatter FORMATADOR_HORARIO = DateTimeFormatter.ofPattern("HH:mm");
    private static final Color COR_EU = new Color(0xDCF8C6);
    private static final Color COR_OUTRO = Color.WHITE;
    private static final Color COR_FUNDO = new Color(0xECE5DD);

    public RenderizadorBalao() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(COR_FUNDO);
        setBorder(new EmptyBorder(4, 8, 4, 8));

        areaMensagem.setLineWrap(true);
        areaMensagem.setWrapStyleWord(true);
        areaMensagem.setEditable(false);
        areaMensagem.setMargin(new Insets(6, 8, 6, 8));
        areaMensagem.setFont(areaMensagem.getFont().deriveFont(14f));

        rotuloHorario.setFont(rotuloHorario.getFont().deriveFont(10f));
        rotuloHorario.setForeground(Color.GRAY);
        rotuloHorario.setBorder(new EmptyBorder(0, 8, 4, 8));

        balao.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        balao.add(areaMensagem, BorderLayout.CENTER);
        balao.add(rotuloHorario, BorderLayout.SOUTH);
        add(balao, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensagem> list, Mensagem valor, int indice, boolean isSelecionado, boolean temFoco) {
        areaMensagem.setText(valor.texto);
        rotuloHorario.setText(valor.horario.format(FORMATADOR_HORARIO));

        int larguraMaximaBalao = (int) (list.getWidth() * 0.6);
        areaMensagem.setSize(larguraMaximaBalao, areaMensagem.getPreferredSize().height);
        areaMensagem.setPreferredSize(new Dimension(areaMensagem.getPreferredSize().width, areaMensagem.getPreferredSize().height));

        balao.setPreferredSize(new Dimension(areaMensagem.getPreferredSize().width + 16, areaMensagem.getPreferredSize().height + rotuloHorario.getPreferredSize().height + 10));

        if (valor.lado == Mensagem.Lado.EU) {
            balao.setBackground(COR_EU);
            areaMensagem.setBackground(COR_EU);
            add(balao, BorderLayout.EAST);
            remove(BorderLayout.WEST);
        } else {
            balao.setBackground(COR_OUTRO);
            areaMensagem.setBackground(COR_OUTRO);
            add(balao, BorderLayout.WEST);
            remove(BorderLayout.EAST);
        }

        setBackground(COR_FUNDO);
        return this;
    }
}