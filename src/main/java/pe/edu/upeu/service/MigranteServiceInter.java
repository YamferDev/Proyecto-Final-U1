package pe.edu.upeu.service;

import pe.edu.upeu.model.Migrante;
import java.util.List;

public interface MigranteServiceInter {
    void agregarMigrante(Migrante m);
    List<Migrante> listarMigrantes();
    void actualizarMigrante(Migrante m, int index);
    void eliminarMigrante(int index);
}
