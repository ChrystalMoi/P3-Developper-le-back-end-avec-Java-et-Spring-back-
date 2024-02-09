package com.openclassrooms.projet3.security;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.repositories.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;

    private final UserRepository userRepository;

    private static final Logger LOGGER = Logger.getLogger(JwtFilter.class);

    public JwtFilter(JwtDecoder jwtDecoder, UserRepository userRepository) {
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
    }

    /**
     * Cette méthode est responsable de la gestion de l'authentification des requêtes entrantes. <br>
     * Elle vérifie la présence et la validité du jeton d'authentification dans l'en-tête de la requête. <br>  <br>
     * Si le jeton est valide, elle extrait l'identifiant de l'utilisateur à partir du jeton, recherche l'utilisateur correspondant dans la base de données,
     * puis authentifie cet utilisateur dans le contexte de sécurité Spring.  <br>
     * Si le jeton est invalide ou s'il n'est pas présent, elle laisse passer la requête sans authentification. <br> <br>
     * En cas d'erreur lors de la validation du jeton ou lors de l'initialisation du contexte de sécurité, elle enregistre un message d'erreur mais laisse passer la requête. <br>
     *
     * @param request     HttpServletRequest - La requête HTTP entrante.
     * @param response    HttpServletResponse - La réponse HTTP sortante.
     * @param filterChain FilterChain - Le filtre de chaîne utilisé pour passer la demande au filtre suivant.
     * @throws ServletException Si une erreur de servlet se produit.
     * @throws IOException      Si une erreur d'entrée/sortie se produit.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recuperation du header (du début) de la requête http
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si le header est vide Ou si le header ne commence pas par "Bearer "
        // Alors, on passe au filtre suivant
        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // On extrait le token du header
        final String token = header.split(" ")[1].trim();

        try {
            // On utilise la méthode decode pour décoder le token (on stocke dans jwt)
            Jwt jwt = jwtDecoder.decode(token);

            // Dans idToken, on stocke l'utilisateur avec le même id que celui du claim
            int idToken = Integer.parseInt(jwt.getClaimAsString("id"));

            // On cherche dans la bdd un user avec le même id que celui du token
            Optional<UserEntity> optionalUser = userRepository.findById(idToken);

            // Si l'user existe (présent dans la bdd avec l'id)
            if (optionalUser.isPresent()) {
                // On crée une authentification par rapport à User Entity (de la bdd)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        optionalUser.get().getId(),
                        null,
                        List.of()
                );

                LOGGER.info("Authentication : " + authentication);

                // On ajoute aux détails de l'authentification les détails de la requête.
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Définition de l'authentification par rapport au context de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);

            // Sinon (= user non présent en bdd) on affiche le message
            } else {
                LOGGER.error("Erreur lors de l'initialisation du context de sécurité");
            }

            // On passe au filtre suivant
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            LOGGER.error("Erreur lors de la validation du token : {}");
            filterChain.doFilter(request, response);
        }
    }
}
