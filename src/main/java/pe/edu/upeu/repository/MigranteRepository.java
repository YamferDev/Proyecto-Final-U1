package pe.edu.upeu.repository;

import pe.edu.upeu.model.Migrante;
import java.util.ArrayList;
import java.util.List;

public class MigranteRepository {

    // Patrón Singleton: Asegura que solo exista una instancia de este repositorio en todo el programa
    private static MigranteRepository instance = new MigranteRepository();

    public static MigranteRepository getInstance() {
        if (instance == null) {
            instance = new MigranteRepository();
        }
        return instance;
    }

    // Nuestra "Base de datos" en memoria
    List<Migrante> migrantes = new ArrayList<>();

    // C = Create (Crear)
    public void agregarMigrante(Migrante m) {
        migrantes.add(m);
    }

    // R = Report/Read (Leer/Listar)
    public List<Migrante> listarMigrantes() {
        return migrantes;
    }

    // U = Update (Actualizar)
    public void actualizarMigrante(Migrante m, int index) {
        migrantes.set(index, m);
    }

    // D = Delete (Eliminar)
    public void eliminarMigrante(int index) {
        migrantes.remove(index);
    }

    public void eliminarTodo() {
        migrantes.clear();
    }
}
