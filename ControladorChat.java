import javax.swing.DefaultListModel;
import javax.swing.Timer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ControladorChat {

    private final DefaultListModel<Conversa> modeloConversas;
    private final DefaultListModel<Mensagem> modeloMensagens;
    private Conversa conversaAtual;
    private PluginChat plugin;

    // callbacks de UI
    private Consumer<String> callbackAtualizarCabecalho;
    private Runnable callbackRepintarListaConversas;

    // lista com todas as conversas
    private final List<Conversa> todasConversas = new ArrayList<>();

    public ControladorChat(DefaultListModel<Conversa> modeloConversas,
                           DefaultListModel<Mensagem> modeloMensagens) {
        this.modeloConversas = modeloConversas;
        this.modeloMensagens = modeloMensagens;
    }

    // usado pelo JanelaChat pra acessar contatos
    public List<Conversa> getTodasConversas() {
        return todasConversas;
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

    // abrir uma conversa na direita
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

    // enviar mensagem
    public void enviarMensagem(String texto) {
        if (conversaAtual == null || texto.isBlank()) return;

        Mensagem msg = new Mensagem(texto.trim(), Mensagem.Lado.EU);
        modeloMensagens.addElement(msg);
        conversaAtual.mensagens.add(msg);
        conversaAtual.ultimaMensagem = msg.texto;
        conversaAtual.ultimoHorario = LocalDateTime.now();

        ordenarConversasPorHorario();

        if (callbackRepintarListaConversas != null) {
            callbackRepintarListaConversas.run();
        }

        if (plugin != null) {
            plugin.aoEnviar(conversaAtual, msg);
        }
    }

    // criar contato/conversa
    public void criarConversa(String nome) {
        if (nome == null || nome.isBlank()) return;
        Conversa conv = new Conversa(nome.trim());
        adicionarSeed(conv);
    }

    // silenciar / dessilenciar conversa atual
    public void alternarSilencio() {
        if (conversaAtual == null) return;
        conversaAtual.silenciada = !conversaAtual.silenciada;

        if (callbackRepintarListaConversas != null) {
            callbackRepintarListaConversas.run();
        }
    }

    // filtro da lista da esquerda
    public void filtrarConversas(String textoBusca) {
        String q = (textoBusca == null) ? "" : textoBusca.trim().toLowerCase();
        modeloConversas.clear();

        if (q.isEmpty()) {
            for (Conversa c : todasConversas) {
                modeloConversas.addElement(c);
            }
        } else {
            for (Conversa c : todasConversas) {
                boolean matchTitulo = c.titulo != null && c.titulo.toLowerCase().contains(q);
                boolean matchUltMsg = c.ultimaMensagem != null && c.ultimaMensagem.toLowerCase().contains(q);
                if (matchTitulo || matchUltMsg) {
                    modeloConversas.addElement(c);
                }
            }
        }

        if (callbackRepintarListaConversas != null) {
            callbackRepintarListaConversas.run();
        }
    }

    public void criarGrupo(String nomeGrupo, List<Conversa> membros) {
        if (nomeGrupo == null || nomeGrupo.isBlank()) return;
        if (membros == null || membros.isEmpty()) return;

        Conversa grupo = new Conversa(nomeGrupo + " (Grupo)", true);
        adicionarSeed(grupo);
    }

    public void iniciarDados() {
        // resposta automática pra simulação
        this.plugin = (conv, msg) -> {
            Timer t = new Timer(600, e -> {
                Mensagem r = new Mensagem("Fala Gabriel, recebido!", Mensagem.Lado.OUTRO);

                // mostra a resposta se a conversa ainda estiver aberta
                if (conversaAtual == conv) {
                    modeloMensagens.addElement(r);
                }

                conv.mensagens.add(r);
                conv.ultimaMensagem = r.texto;
                conv.ultimoHorario = LocalDateTime.now();

                ordenarConversasPorHorario();

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
        gp.mensagens.add(new Mensagem("Oi Gabriel, precisamos conversar.", Mensagem.Lado.OUTRO));
        gp.ultimaMensagem = "Oi Gabriel, precisamos conversar.";
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
        pai.mensagens.add(new Mensagem("Oi pai! Vou conferir assim que chegar em casa.", Mensagem.Lado.EU));
        pai.ultimaMensagem = "Oi pai! Vou confirir assim que chegar em casa.";
        pai.ultimoHorario = LocalDateTime.now().minusHours(4);

        Conversa vo = new Conversa("Vó");
        vo.mensagens.add(new Mensagem("Oi neto querido! Preciso da sua ajuda.", Mensagem.Lado.OUTRO));
        vo.mensagens.add(new Mensagem("Oi vó! Já tô indo!", Mensagem.Lado.EU));
        vo.ultimaMensagem = "Oi vó! Já tô indo!";
        vo.ultimoHorario = LocalDateTime.now().minusHours(6);

        adicionarSeed(prof);
        adicionarSeed(gp);
        adicionarSeed(mg);
        adicionarSeed(mae);
        adicionarSeed(pai);
        adicionarSeed(vo);
    }

    //mantem lista ordenada pelo último horário
    private void ordenarConversasPorHorario() {
        todasConversas.sort((a, b) -> {
            if (a.ultimoHorario == null && b.ultimoHorario == null) return 0;
            if (a.ultimoHorario == null) return 1;
            if (b.ultimoHorario == null) return -1;
            return b.ultimoHorario.compareTo(a.ultimoHorario);
        });

        // recarrega o modelo com essa ordem
        modeloConversas.clear();
        for (Conversa c : todasConversas) {
            modeloConversas.addElement(c);
        }
    }

    private void adicionarSeed(Conversa c) {
        todasConversas.add(c);
        ordenarConversasPorHorario();
    }
}
