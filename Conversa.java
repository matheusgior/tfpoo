import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conversa {
    String titulo;
    String ultimaMensagem;
    LocalDateTime ultimoHorario;
    List<Mensagem> mensagens = new ArrayList<>();
    boolean silenciada;

    public Conversa(String titulo) {
        this.titulo = titulo;
        this.ultimaMensagem = "";
        this.ultimoHorario = LocalDateTime.now();
    }
}