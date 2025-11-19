import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conversa {
    String titulo;
    String ultimaMensagem;
    LocalDateTime ultimoHorario;
    List<Mensagem> mensagens = new ArrayList<>();
    boolean silenciada;
    boolean isGroup;

    public Conversa(String titulo) {
        this(titulo, false);
    }

    public Conversa(String titulo, boolean isGroup) {
        this.titulo = titulo;
        this.ultimaMensagem = "";
        this.ultimoHorario = LocalDateTime.now();
        this.isGroup = isGroup;
    }
}