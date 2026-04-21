package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Migrante {
    private String nombreCompleto;
    private String paisOrigen;
    private String tipoVisa;
    private LocalDate fechaIngreso;
    private String statusMigratorio;

}
