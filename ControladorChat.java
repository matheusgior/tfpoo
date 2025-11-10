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
    public void iniciarDados() {
    // resposta automática
    this.plugin = (conv, msg) -> {
        Timer t = new Timer(600, e -> {
            Mensagem r = new Mensagem("Fala Gabriel, recebido!", Mensagem.Lado.OUTRO);

            // pra receber a resposta manter o chat aberto
            if (conversaAtual == conv) {
                modeloMensagens.addElement(r);
            }

            conv.mensagens.add(r);
            conv.ultimaMensagem = r.texto;
            conv.ultimoHorario = LocalDateTime.now();

            if (callbackRepintarListaConversas != null) {
                callbackRepintarListaConversas.run();
            }
        });
        t.setRepeats(false);
        t.start();
    };

    // conversas iniciais
    Conversa prof = new Conversa("Professor Marco");
    prof.mensagens.add(new Mensagem("Olá, Gabriel? Tudo bem com o trabalho final?", Mensagem.Lado.OUTRO));
    prof.ultimaMensagem = "Olá, Gabriel? Tudo bem com o trabalho final?";
    prof.ultimoHorario = LocalDateTime.now().minusMinutes(1);

    Conversa gp = new Conversa("Guilherme Pretto");
    gp.mensagens.add(new Mensagem("Oi Gabriel, já terminou meu layout de folha de advocacia? Abraços!", Mensagem.Lado.OUTRO));
    gp.ultimaMensagem = "Oi Gabriel, já terminou meu layout de folha de advocacia? Abraços!";
    gp.ultimoHorario = LocalDateTime.now().minusMinutes(3);

    Conversa mg = new Conversa("Matheus Giordani");
    mg.mensagens.add(new Mensagem("Fala Gabriel, vamos fazer o TF?", Mensagem.Lado.OUTRO));
    mg.ultimaMensagem = "Fala Gabriel, vamos fazer o TF?";
    mg.ultimoHorario = LocalDateTime.now().minusMinutes(5);

    Conversa mae = new Conversa("Mãe");
    mae.mensagens.add(new Mensagem("Filho, não esquece o casaco e boa aula!", Mensagem.Lado.OUTRO));
    mae.mensagens.add(new Mensagem("Valeu mãe!", Mensagem.Lado.EU));
    mae.ultimaMensagem = "Valeu mãe!";
    mae.ultimoHorario = LocalDateTime.now().minusHours(2);

    Conversa pai = new Conversa("Pai");
    pai.mensagens.add(new Mensagem("Oi filho, recebeu o depósito?", Mensagem.Lado.OUTRO));
    pai.mensagens.add(new Mensagem("Oi pai! Vou confirir assim que chegar em casa.", Mensagem.Lado.EU));
    pai.ultimaMensagem = "Oi pai! Vou confirir assim que chegar em casa.";
    pai.ultimoHorario = LocalDateTime.now().minusHours(4);

    Conversa vo = new Conversa("Vó");
    vo.mensagens.add(new Mensagem("Oi neto querido! Eu fiz um bolo, vem pegar um pedaço.", Mensagem.Lado.OUTRO));
    vo.mensagens.add(new Mensagem("Oi vó! Já tô indo!", Mensagem.Lado.EU));
    vo.ultimaMensagem = "Oi vó! Já tô indo!";
    vo.ultimoHorario = LocalDateTime.now().minusHours(6);

    modeloConversas.addElement(prof);
    modeloConversas.addElement(gp);
    modeloConversas.addElement(mg);
    modeloConversas.addElement(mae);
    modeloConversas.addElement(pai);
    modeloConversas.addElement(vo);
}

}