package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String documento;
    private Boolean activo;
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    @Embedded
    private Direccion direccion;

    public Medico(DatosRegistroMedico datosRegistroMedico) {
        this.activo = true;
        this.nombre = datosRegistroMedico.nombre();
        this.documento = datosRegistroMedico.documento();
        this.email = datosRegistroMedico.email();
        this.telefono = datosRegistroMedico.telefono();
        this.especialidad = datosRegistroMedico.especialidad();
        this.direccion = new Direccion(datosRegistroMedico.datosDireccion());
    }

    public void actualizarDatos(UpdateMedico updateMedico) {
        if (updateMedico.nombre() != null) {
            this.nombre = updateMedico.nombre();
        }
        if (updateMedico.documento() != null) {
            this.documento = updateMedico.documento();
        }
        if (updateMedico.datosDireccion() != null) {
            this.direccion = direccion.actualizarDireccion(updateMedico.datosDireccion());
        }
    }

    public void borradoLogico() {
        this.activo = false;
    }
}
