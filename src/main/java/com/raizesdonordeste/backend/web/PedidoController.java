package com.raizesdonordeste.backend.web;

import com.raizesdonordeste.backend.dto.PedidoRequest;
import com.raizesdonordeste.backend.dto.PedidoResponse;
import com.raizesdonordeste.backend.dto.StatusPedidoResponse;
import com.raizesdonordeste.backend.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse pedido = pedidoService.criar(request);
        return ResponseEntity.created(URI.create("/pedidos/" + pedido.id())).body(pedido);
    }

    @GetMapping("/{id}/status")
    public StatusPedidoResponse status(@PathVariable Long id) {
        return pedidoService.consultarStatus(id);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String motivo = body != null ? body.getOrDefault("motivo", "Cancelado pelo cliente/atendente") : "Cancelado pelo cliente/atendente";
        pedidoService.cancelar(id, motivo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
