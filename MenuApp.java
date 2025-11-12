import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuApp {
    private final JFrame janelaPai;
    private final ActionListener novoChatListener;
    private final ActionListener silenciarListener;

    public MenuApp(JFrame janelaPai, ActionListener novoChatListener, ActionListener silenciarListener) {
        this.janelaPai = janelaPai;
        this.novoChatListener = novoChatListener;
        this.silenciarListener = silenciarListener;
    }

    public JMenuBar criarMenu() {
        JMenuBar mb = new JMenuBar();

        // menu Arquivo
        JMenu arquivo = new JMenu("Arquivo");
        JMenuItem novoChat = new JMenuItem("Nova conversa");
        JMenuItem sair = new JMenuItem("Sair");
        arquivo.add(novoChat);
        arquivo.addSeparator();
        arquivo.add(sair);

        // menu Editar
        JMenu editar = new JMenu("Editar");
        JMenuItem silenciar = new JMenuItem("Silenciar conversa");
        editar.add(silenciar);

        // menu Ajuda
        JMenu ajuda = new JMenu("Ajuda");
        JMenuItem sobre = new JMenuItem("Sobre");
        ajuda.add(sobre);

        mb.add(arquivo);
        mb.add(editar);
        mb.add(ajuda);

        // eventos
        novoChat.addActionListener(novoChatListener);
        sair.addActionListener(e -> janelaPai.dispose());
        silenciar.addActionListener(silenciarListener);
        sobre.addActionListener(e -> JOptionPane.showMessageDialog(
                janelaPai,
                "Criado por Gabriel Borges e Matheus Giordani!\nFeito para um trabalho de Programação Orientada a Objetos!",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE
        ));

        return mb;
    }
}