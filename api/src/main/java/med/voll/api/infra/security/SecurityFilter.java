package med.voll.api.infra.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //obtener el token
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
           var token = authHeader.replace("Bearer ", "");
           var nombreDeUsuario = tokenService.getSubject(token);
           if (nombreDeUsuario != null){
               //token valido
               var usuario =  usuarioRepository.findByUsuario(nombreDeUsuario);//buscamos el usuario
               var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                       usuario.getAuthorities());//forzamos el inicio de sesion
               SecurityContextHolder.getContext().setAuthentication(authentication);
           }
        }
        filterChain.doFilter(request, response);
    }
}
