package com.raizesdonordeste.backend.service;

import com.raizesdonordeste.backend.domain.CardapioUnidade;
import com.raizesdonordeste.backend.dto.CardapioItemResponse;
import com.raizesdonordeste.backend.repository.CardapioUnidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardapioService {

    private final CardapioUnidadeRepository cardapioUnidadeRepository;

    public CardapioService(CardapioUnidadeRepository cardapioUnidadeRepository) {
        this.cardapioUnidadeRepository = cardapioUnidadeRepository;
    }

    @Transactional(readOnly = true)
    public List<CardapioItemResponse> cardapioDaUnidade(Long unidadeId) {
        int mesAtual = LocalDate.now().getMonthValue();

        return cardapioUnidadeRepository.findByUnidadeIdAndDisponivelTrue(unidadeId).stream()
                .filter(cardapio -> cardapio.getProduto().disponivelNoMes(mesAtual))
                .map(this::toResponse)
                .toList();
    }

    private CardapioItemResponse toResponse(CardapioUnidade cardapioUnidade) {
        return new CardapioItemResponse(
                cardapioUnidade.getProduto().getId(),
                cardapioUnidade.getProduto().getNome(),
                cardapioUnidade.getProduto().getDescricao(),
                cardapioUnidade.precoEfetivo()
        );
    }
}
