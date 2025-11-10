// Mensagem.java
import java.time.LocalDateTime;

public class Mensagem {

    public enum Lado { EU, OUTRO }

    String texto;
    LocalDateTime horario;
    Lado lado;

    public Mensagem(String texto, Lado lado) {
        this.texto = texto;
        this.lado = lado;
        this.horario = LocalDateTime.now();
    }
}
