package com.mapfre.mifel.vida.repository.bancos;

import com.mapfre.mifel.vida.entity.banco.Bancos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BancosRepository extends JpaRepository<Bancos, Long>, BancosRepositoryCustom{

}
