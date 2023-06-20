package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("voll-med")
                    .withSubject(usuario.getUsuario())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaDeExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    public String getSubject(String token){
        if (token == null){
            throw new RuntimeException();
        }
        DecodedJWT decodedJWT = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);//validando la firma del token
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify an specific claim validations
                    .withIssuer("voll-med")//valida que el emisor sea el correcto
                    // reusable verifier instance
                    .build();

            decodedJWT = verifier.verify(token);//verifica el token
        } catch (JWTVerificationException exception){
            System.out.println(exception.toString());
        }
        if(decodedJWT.getSubject() == null){
            throw new RuntimeException("Verifier null");
        }
        return decodedJWT.getSubject();
    }


    public Instant generarFechaDeExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
