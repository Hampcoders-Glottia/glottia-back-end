package com.hampcoders.glottia.platform.api.venue.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.venue.domain.model.aggregates.Partner;

import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de Partner.
 * Extiende JpaRepository para obtener operaciones CRUD automáticas.
 * Esta interfaz conecta el dominio con la infraestructura de persistencia.
 */
@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    /**
     * Guarda o actualiza un Partner.
     * @param partner El agregado Partner a persistir.
     * @return El Partner persistido con su ID generado.
     */
    Partner save(Partner partner);

    /**
     * Busca un Partner por su ID.
     * @param id El ID del Partner.
     * @return Un Optional con el Partner si existe.
     */
    Optional<Partner> findById(Long id);

    /**
     * Busca un Partner por el User ID.
     * @param userId El ID del usuario asociado.
     * @return Un Optional con el Partner si existe.
     */
    Optional<Partner> findByUserId(Long userId);

    /**
     * Obtiene todos los Partners.
     * @return Lista de todos los Partners.
     */
    List<Partner> findAll();

    /**
     * Elimina un Partner.
     * @param partner El Partner a eliminar.
     */
    void delete(Partner partner);

    /**
     * Verifica si existe un Partner con el userId dado.
     * @param userId El ID del usuario.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByUserId(Long userId);
}

