package dev.jeep.Lookpay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.CardResponseDTO;
import dev.jeep.Lookpay.enums.CardTypeEnum;
import dev.jeep.Lookpay.models.CDCardModel;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.PaymentMethodModel;
import dev.jeep.Lookpay.repository.CDCardRepository;
import dev.jeep.Lookpay.repository.ClientRepository;
import dev.jeep.Lookpay.repository.PaymentMethodRepository;

@Service
public class CardService {
    @Autowired
    private CDCardRepository cdCardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<CDCardModel> getAllCCardsByClientId(Long clientId) {
        return cdCardRepository.findCDCardsByClientId(clientId);
    }

    public CDCardModel getCardById(Long id) {
        return cdCardRepository.findByCardId(id);
    }

    public List<CDCardModel> getAllDCardsByClientId(Long clientId) {
        return cdCardRepository.findDDCardsByClientId(clientId);
    }

    public List<CDCardModel> getAllUserCards(Long clientId) {
        return cdCardRepository.findAllByClientId(clientId);
    }

    public CDCardModel updateCard(CDCardModel card) {
        return cdCardRepository.save(card);
    }

    public CDCardModel updateCard(CardResponseDTO card) {
        try {
            CDCardModel cdCard = this.getCardById(card.getId());

            if (cdCard == null) {
                return null;
            }

            PaymentMethodModel paymentMethod = cdCard.getPaymentMethod();

            if (card.getName() != null && card.getName() != "")
                paymentMethod.setName(card.getName());

            if (card.getCardHolderName() != null && card.getCardHolderName() != "")
                cdCard.setCardHolderName(card.getCardHolderName());

            if (card.getCardType() != null && card.getCardType() != "")
                cdCard.setCardType(CardTypeEnum.fromString(card.getCardType()));

            if (card.getCvv() != null && card.getCvv() != "")
                cdCard.setCvv(card.getCvv());

            if (card.getExpirationDate() != null && card.getExpirationDate() != "")
                cdCard.setExpirationDate(card.getExpirationDate());

            if (card.getNumber() != null && card.getNumber() != "")
                cdCard.setNumber(card.getNumber());

            paymentMethodRepository.save(paymentMethod);
            return cdCardRepository.save(cdCard);

        } catch (Exception e) {
            System.out.println("=======================" + e.getMessage());
            return null;
        }
    }

    public boolean validateIfExistsCard(String number) {
        try {
            CDCardModel card = cdCardRepository.findByNumber(number);
            return card != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteCard(Long id) {
        try {
            CDCardModel card = this.getCardById(id);

            if (card == null) {
                return false;
            }

            PaymentMethodModel clientPreferedPaymentMethod = this.getCardById(id).getPaymentMethod()
                    .getClient().getPreferedAccount();

            if (clientPreferedPaymentMethod != null && clientPreferedPaymentMethod.getCdCard().getId() == id) {
                ClientModel client = clientPreferedPaymentMethod.getClient();
                client.setPreferedAccount(null);

                clientRepository.save(client);
            }

            PaymentMethodModel paymentMethod = card.getPaymentMethod();
            paymentMethodRepository.delete(paymentMethod);
            cdCardRepository.delete(card);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
