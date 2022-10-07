package com.guggio.etherwallet.repository.balance;

import com.guggio.etherwallet.entity.balance.Erc20Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Erc20BalanceRepository extends JpaRepository<Erc20Balance, Long> {
}