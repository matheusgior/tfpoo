import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class MenuApp {
    private final JFrame janelaPai;
    private final ActionListener novoChatListener;
    private final ActionListener silenciarListener;
    private final ActionListener criarGrupoListener;

    public MenuApp(JFrame janelaPai,
                   ActionListener novoChatListener,
                   ActionListener silenciarListener,
                   ActionListener criarGrupoListener) {
        this.janelaPai = janelaPai;
        this.novoChatListener = novoChatListener;
        this.silenciarListener = silenciarListener;
        this.criarGrupoListener = criarGrupoListener;
    }

    public JMenuBar criarMenu() {
        JMenuBar mb = new JMenuBar();

        // menu arquivo
        JMenu arquivo = new JMenu("Arquivo");
        JMenuItem novoChat = new JMenuItem("Nova conversa");
        JMenuItem criarGrupo = new JMenuItem("Criar grupo");  
        JMenuItem sair = new JMenuItem("Sair");

        arquivo.add(novoChat);
        arquivo.add(criarGrupo);                             
        arquivo.addSeparator();
        arquivo.add(sair);

        // menu editar
        JMenu editar = new JMenu("Editar");
        JMenuItem silenciar = new JMenuItem("Silenciar conversa");
        editar.add(silenciar);

        // menu ajuda
        JMenu ajuda = new JMenu("Ajuda");
        JMenuItem sobre = new JMenuItem("Sobre");
        try {
            Image icon = ImageIO.read(new File("whatsapp-logo-png.png"));
            if (icon != null) {
                // redimensionar
                Image scaledIcon = icon.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                sobre.setIcon(new ImageIcon(scaledIcon));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone do menu: " + e.getMessage());
        }
        ajuda.add(sobre);

        mb.add(arquivo);
        mb.add(editar);
        mb.add(ajuda);

        // eventos
        novoChat.addActionListener(novoChatListener);
        criarGrupo.addActionListener(criarGrupoListener);  
        sair.addActionListener(e -> janelaPai.dispose());
        silenciar.addActionListener(silenciarListener);
        sobre.addActionListener(e -> JOptionPane.showMessageDialog(
                janelaPai,
                "Criado por Gabriel Borges e Matheus Giordani!\nFeito para um trabalho de Programação Orientada a Objetos!",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE,
                janelaPai.getIconImage() != null ? new ImageIcon(janelaPai.getIconImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)) : null
        ));

        return mb;
    }
}
