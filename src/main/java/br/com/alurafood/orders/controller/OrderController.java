package br.com.alurafood.orders.controller;

import br.com.alurafood.orders.dto.OrderDTO;
import br.com.alurafood.orders.dto.StatusDTO;
import br.com.alurafood.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

        @Autowired
        private OrderService service;

        @GetMapping()
        public List<OrderDTO> findAll() {
            return service.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrderDTO> findById(@PathVariable @NotNull Long id) {
            OrderDTO dto = service.findById(id);

            return  ResponseEntity.ok(dto);
        }

        @PostMapping()
        public ResponseEntity<OrderDTO> orderRequest(@RequestBody @Valid OrderDTO dto, UriComponentsBuilder uriBuilder) {
            OrderDTO orderPlaced = service.createOrder(dto);

            URI address = uriBuilder.path("/orders/{id}").buildAndExpand(orderPlaced.getId()).toUri();

            return ResponseEntity.created(address).body(orderPlaced);

        }

        @PutMapping("/{id}/status")
        public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestBody StatusDTO status){
           OrderDTO dto = service.updateStatus(id, status);

            return ResponseEntity.ok(dto);
        }


        @PutMapping("/{id}/paid")
        public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
            service.approveOrderPayment(id);

            return ResponseEntity.ok().build();

        }
}
