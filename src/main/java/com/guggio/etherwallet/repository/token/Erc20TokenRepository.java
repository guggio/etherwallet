package com.guggio.etherwallet.repository.token;

import com.guggio.etherwallet.entity.token.Erc20Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Erc20TokenRepository extends JpaRepository<Erc20Token, String> {
}