package pe.edu.upeu.repository;

import pe.edu.upeu.model.Migrante;
import java.util.ArrayList;
import java.util.List;

public class MigranteRepository {

    private static MigranteRepository instance = new MigranteRepository();

    public static MigranteRepository getInstance() {
        if (instance == null) {
            instance = new MigranteRepository();
        }
        return instance;
    }

    private List<Migrante> migrantes = new ArrayList<>();

    public void agregarMigrante(Migrante m) {
        migrantes.add(m); 
    }

    public List<Migrante> listarMigrantes() {
        return migrantes; 
    }

    public void actualizarMigrante(Migrante m, int index) {
        migrantes.set(index, m); 
    }

    public void eliminarMigrante(int index) {
        migrantes.remove(index); 
    }

    public void eliminarTodo() {
        migrantes.clear(); 
    }
}
