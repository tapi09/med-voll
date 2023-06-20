package med.voll.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.usuario.DatosAutenticacionUsuario;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DatosJWTToken;
import med.voll.api.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AutenticacionController {

        private final AuthenticationManager aut;

        private final TokenService tokenService;

        @PostMapping
        public ResponseEntity login(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario){
                Authentication authToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.usuario(), datosAutenticacionUsuario.clave());
                var usuarioAutenticado= aut.authenticate(authToken);
                var JWTToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
                return ResponseEntity.ok(new DatosJWTToken(JWTToken));
        }
}
