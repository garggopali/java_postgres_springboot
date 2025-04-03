package com.gopali.gift_card_springboot_postgred.giftcards;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

@Service

public class GiftCardsService {

	@Autowired
	private GiftCardsRepository giftCardsRepository;

	public GiftCardsService(GiftCardsRepository giftCardsRepository) {
        this.giftCardsRepository = giftCardsRepository;
    }
	
    public GiftCards createGiftCards(BigDecimal initialBalance, String currency) {
        GiftCards giftCards = new GiftCards();
        giftCards.generateCardNumber();
        giftCards.generateRedemptionCode();
        giftCards.setBalance(initialBalance);
        giftCards.setCurrency(currency);
        giftCards.setStatus(GiftCardsStatus.ACTIVE);

        return giftCardsRepository.save(giftCards);
    }

    // Get card using redemptionCode eg. http://localhost:9092/api/giftcards/ABCD1234EFG
    public GiftCards findByRedemptionCode(String redemptionCode) {
        return giftCardsRepository.findByRedemptionCode(redemptionCode)
                .orElse(null);
    }
    
    // Get balance using redemptionCode eg. http://localhost:9092/api/giftcards/ABCD1234EFG/balance
    public BigDecimal getGiftCardBalance(String redemptionCode) {
        GiftCards giftCard = findByRedemptionCode(redemptionCode);
        return giftCard.getBalance();
    }
    
    
	
    public List<GiftCards> getAllGiftCards() {
        return giftCardsRepository.findAll();
    }

    public Optional<GiftCards> getGiftCardById(UUID id) {
        return giftCardsRepository.findById(id);
    }
    
    public GiftCards createGiftCards(GiftCards giftCards) {
        return giftCardsRepository.save(giftCards);
    }

    public void deleteGiftCards(UUID id) {
        giftCardsRepository.deleteById(id);
    }
    

    
}
