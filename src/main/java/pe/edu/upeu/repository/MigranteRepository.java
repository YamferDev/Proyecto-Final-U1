package pe.edu.upeu.repository;

import pe.edu.upeu.model.Migrante;
import java.util.ArrayList;
import java.util.List;

/**
 * REPOSITORIO (Nuestra Base de Datos Simulada)
 * Esta clase es la encargada de guardar, buscar, modificar y borrar los migrantes.
 */
public class MigranteRepository {

    // --- PATRÓN SINGLETON ---
    // ¿Qué es Singleton? Es un patrón de diseño que asegura que SOLO EXISTA UNA INSTANCIA
    // (una sola copia) de esta clase en todo el programa. 
    // Así evitamos tener múltiples "bases de datos" sueltas y todos los controladores 
    // hablan con la misma lista de datos.
    
    // 1. Creamos la única instancia de forma privada
    private static MigranteRepository instance = new MigranteRepository();

    // 2. Creamos un método público para obtener esa única instancia
    public static MigranteRepository getInstance() {
        if (instance == null) {
            instance = new MigranteRepository();
        }
        return instance;
    }
    // ------------------------

    // Nuestra "tabla" de base de datos en la memoria RAM
    private List<Migrante> migrantes = new ArrayList<>();

    /**
     * C = Create (Crear)
     */
    public void agregarMigrante(Migrante m) {
        migrantes.add(m); 
    }

    /**
     * R = Read (Leer / Listar)
     */
    public List<Migrante> listarMigrantes() {
        return migrantes; 
    }

    /**
     * U = Update (Actualizar)
     */
    public void actualizarMigrante(Migrante m, int index) {
        migrantes.set(index, m); 
    }

    /**
     * D = Delete (Eliminar)
     */
    public void eliminarMigrante(int index) {
        migrantes.remove(index); 
    }

    public void eliminarTodo() {
        migrantes.clear(); 
    }
}
