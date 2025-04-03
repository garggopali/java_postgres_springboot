package com.gopali.gift_card_springboot_postgred.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface TransactionRepository extends JpaRepository<Transaction, UUID>{
	List<Transaction> findByCardNumber(String cardNumber);    
}

