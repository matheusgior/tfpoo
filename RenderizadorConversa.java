import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class RenderizadorConversa extends JPanel implements ListCellRenderer<Conversa> {
    private final JLabel rotuloIcone = new JLabel();
    private final JLabel rotuloTitulo = new JLabel();
    private final JLabel rotuloMensagem = new JLabel();
    private final JLabel rotuloHorario = new JLabel();
    private final JLabel rotuloSilencio = new JLabel("🔇");
    private final JPanel painelCentral;
    private static final ImageIcon ICONE_GRUPO;

    static {
        ICONE_GRUPO = carregarIconeGrupo();
    }

    private static ImageIcon carregarIconeGrupo() {
        try {
            Image icon = ImageIO.read(new File("whatsapp-logo-png.png"));
            if (icon != null) {
                // Redimensionar
                Image scaledIcon = icon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledIcon);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone de grupo: " + e.getMessage());
        }
        return null;
    }

    private static final DateTimeFormatter FORMATADOR_HORARIO = DateTimeFormatter.ofPattern("HH:mm");

    public RenderizadorConversa() {
        setLayout(new BorderLayout(8, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // O tamanho será definido dinamicamente

        JPanel painelTexto = new JPanel(new BorderLayout());
        painelTexto.setOpaque(false);

        rotuloTitulo.setFont(rotuloTitulo.getFont().deriveFont(Font.BOLD, 14f));
        rotuloMensagem.setForeground(Color.GRAY);
        rotuloMensagem.setFont(rotuloMensagem.getFont().deriveFont(12f));

        painelTexto.add(rotuloTitulo, BorderLayout.NORTH);
        painelTexto.add(rotuloMensagem, BorderLayout.SOUTH);

        painelCentral = new JPanel(new BorderLayout(8, 0));
        painelCentral.setOpaque(false);
       
        painelCentral.add(painelTexto, BorderLayout.CENTER);

        JPanel painelDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        painelDireito.setOpaque(false);
        rotuloHorario.setFont(rotuloHorario.getFont().deriveFont(10f));
        rotuloSilencio.setFont(rotuloSilencio.getFont().deriveFont(12f));
        rotuloSilencio.setForeground(Color.GRAY);
        rotuloSilencio.setVisible(false);

        painelDireito.add(rotuloHorario);
        painelDireito.add(rotuloSilencio);

        add(painelCentral, BorderLayout.CENTER);
        add(painelDireito, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Conversa> list, Conversa valor, int indice, boolean isSelecionado, boolean temFoco) {
        rotuloTitulo.setText(valor.titulo);
        rotuloMensagem.setText(valor.ultimaMensagem.isEmpty() ? "Toque para conversar" : valor.ultimaMensagem);
        rotuloHorario.setText(valor.ultimoHorario.format(FORMATADOR_HORARIO));
        rotuloSilencio.setVisible(valor.silenciada);

        //ícone de grupo
        if (valor.isGroup && ICONE_GRUPO != null) {
            rotuloIcone.setIcon(ICONE_GRUPO);
            rotuloIcone.setPreferredSize(new Dimension(32, 32));
            if (painelCentral.getComponent(0) != rotuloIcone) {
                painelCentral.add(rotuloIcone, BorderLayout.WEST);
            }
        } else {
            rotuloIcone.setIcon(null);
            rotuloIcone.setPreferredSize(new Dimension(0, 0));
            painelCentral.remove(rotuloIcone);
        }

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