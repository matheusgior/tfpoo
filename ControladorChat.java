import javax.swing.DefaultListModel;
import javax.swing.Timer;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class ControladorChat {
    private final DefaultListModel<Conversa> modeloConversas;
    private final DefaultListModel<Mensagem> modeloMensagens;
    private Conversa conversaAtual;
    private PluginChat plugin;

    // atualizar UI
    private Consumer<String> callbackAtualizarCabecalho;
    private Runnable callbackRepintarListaConversas;

    public ControladorChat(DefaultListModel<Conversa> modeloConversas, DefaultListModel<Mensagem> modeloMensagens) {
        this.modeloConversas = modeloConversas;
        this.modeloMensagens = modeloMensagens;
    }

    public void setCallbackAtualizarCabecalho(Consumer<String> callbackAtualizarCabecalho) {
        this.callbackAtualizarCabecalho = callbackAtualizarCabecalho;
    }

    public void setCallbackRepintarListaConversas(Runnable callbackRepintarListaConversas) {
        this.callbackRepintarListaConversas = callbackRepintarListaConversas;
    }

    public Conversa getConversaAtual() {
        return conversaAtual;
    }

    public void abrirConversa(Conversa conv) {
        if (conv == null) return;

        conversaAtual = conv;
        modeloMensagens.clear();
        for (Mensagem msg : conv.mensagens) {
            modeloMensagens.addElement(msg);
        }
        if (callbackAtualizarCabecalho != null) {
            callbackAtualizarCabecalho.accept(conv.titulo);
        }
    }

    public void enviarMensagem(String texto) {
        if (conversaAtual == null || texto.isBlank()) return;

        Mensagem msg = new Mensagem(texto.trim(), Mensagem.Lado.EU);
        modeloMensagens.addElement(msg);
        conversaAtual.mensagens.add(msg);
        conversaAtual.ultimaMensagem = msg.texto;
        conversaAtual.ultimoHorario = LocalDateTime.now();

        if (callbackRepintarListaConversas != null) {
            callbackRepintarListaConversas.run();
        }

        if (plugin != null) {
            plugin.aoEnviar(conversaAtual, msg);
        }
    }

    public void criarConversa(String nome) {
        if (nome == null || nome.isBlank()) return;
        Conversa conv = new Conversa(nome.trim());
        modeloConversas.add(0, conv);
    }

    public void alternarSilencio() {
        if (conversaAtual == null) return;
        conversaAtual.silenciada = !conversaAtual.silenciada;
        if (callbackRepintarListaConversas != null) {
            callbackRepintarListaConversas.run();
        }
    }

    public void filtrarConversas(String textoBusca) {
        if (callbackRepintarListaConversas != null) {
            callbackRepintarListaConversas.run();
        }
    }
    
}