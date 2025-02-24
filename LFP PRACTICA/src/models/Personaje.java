package models;
/**
 *
 * @author jmfuente
 */
public class Personaje {
    private String nombre;
    private int salud;
    private int ataque;
    private int defensa;
    private int vidaActual;
    
    public Personaje(String nombre, int salud, int ataque, int defensa) {
        this.nombre = nombre;
        this.salud = salud;
        this.ataque = ataque;
        this.defensa = defensa;
        this.vidaActual=salud*10;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getSalud() {
        return salud;
    }
    
    public void setSalud(int salud) {
        this.salud = salud;
    }
    
    public int getAtaque() {
        return ataque;
    }
    
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    
    public int getDefensa() {
        return defensa;
    }
    
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    public int getVidaInicial() {
        return this.salud * 10;
    }
    
    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }
    
    //METODSO
    public int calcularDaño(Personaje defensor) {
        int daño = this.ataque - defensor.getDefensa();
        return (daño > 0) ? daño : 0;
    }
    
    public void atacar(Personaje oponente) {
        int daño = calcularDaño(oponente);
        oponente.recibirDaño(daño);
    }

    public void recibirDaño(int daño) {
        if (daño > 0) {
            this.vidaActual -= daño;

            if (this.vidaActual < 0) {
                this.vidaActual = 0;
            }
        }
    }
    
    public boolean estaVivo() {
        return this.vidaActual > 0;
    }


    
}

