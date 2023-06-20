package med.voll.api.domain.paciente;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRespuestaPaciente(String nombre, String email, String telefono, String documento,
                                     DatosDireccion datosDireccion) {
    public DatosRespuestaPaciente(Paciente paciente) {
        this(paciente.getNombre(), paciente.getEmail(), paciente.getTelefono(), paciente.getDocumento(), new DatosDireccion(paciente.getDireccion()));
    }
}
