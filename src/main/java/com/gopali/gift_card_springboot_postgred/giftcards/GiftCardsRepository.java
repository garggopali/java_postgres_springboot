package com.gopali.gift_card_springboot_postgred.giftcards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface GiftCardsRepository extends JpaRepository<GiftCards, UUID>{
    //Optional<GiftCards> findByCardNumber(String cardNumber);
    Optional<GiftCards> findByRedemptionCode(String redemptionCode);
}

