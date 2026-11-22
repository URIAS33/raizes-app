package com.raizesdonordeste.backend.web;

import com.raizesdonordeste.backend.dto.CardapioItemResponse;
import com.raizesdonordeste.backend.service.CardapioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final CardapioService cardapioService;

    public UnidadeController(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    @GetMapping("/{id}/cardapio")
    public List<CardapioItemResponse> cardapio(@PathVariable Long id) {
        return cardapioService.cardapioDaUnidade(id);
    }
}
