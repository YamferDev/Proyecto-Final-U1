package pe.edu.upeu.service;

import pe.edu.upeu.model.Migrante;
import pe.edu.upeu.repository.MigranteRepository;

import java.util.List;

public class MigranteServiceImp implements MigranteServiceInter {

    private MigranteRepository repo = MigranteRepository.getInstance();

    @Override
    public void agregarMigrante(Migrante m) {
        repo.agregarMigrante(m);
    }

    @Override
    public List<Migrante> listarMigrantes() {
        return repo.listarMigrantes();
    }

    @Override
    public void actualizarMigrante(Migrante m, int index) {
        repo.actualizarMigrante(m, index);
    }

    @Override
    public void eliminarMigrante(int index) {
        repo.eliminarMigrante(index);
    }
}
