package med.voll.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.medico.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentsBuilder) {
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico);
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();//crear url dinamicamente
        return ResponseEntity.created(url).body(datosRespuestaMedico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size = 10, sort = {"nombre"}, direction = Sort.Direction.ASC) Pageable paginacion) {
        return ResponseEntity.ok(medicoRepository.findAllByActivoTrue(paginacion).map(DatosListadoMedico::new));
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new); para devolver el listado paginado con todos los resultados incluidos
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaMedico> actualizarMedico(@RequestBody UpdateMedico updateMedico) {
        Medico medico = medicoRepository.getReferenceById(updateMedico.id());
        medico.actualizarDatos(updateMedico);
        System.out.println(updateMedico.datosDireccion());
        return ResponseEntity.ok(new DatosRespuestaMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.borradoLogico();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> buscarMedicoPorId(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico);
        return ResponseEntity.ok().body(datosRespuestaMedico);
    }

}
