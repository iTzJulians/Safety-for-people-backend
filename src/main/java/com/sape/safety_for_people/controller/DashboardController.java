package com.sape.safety_for_people.controller;

import com.sape.safety_for_people.model.Sale;
import com.sape.safety_for_people.model.SaleDetail;
import com.sape.safety_for_people.model.Status;
import com.sape.safety_for_people.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final SaleRepository saleRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public DashboardController(SaleRepository saleRepository, StatusRepository statusRepository,
                               UserRepository userRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();

        BigDecimal totalVentas = saleRepository.sumTotalAmountOfActiveSales();
        if (totalVentas == null) totalVentas = BigDecimal.ZERO;
        long totalPedidos = saleRepository.countActiveSales();
        long totalUsuarios = userRepository.count();
        long totalProductos = productRepository.count();

        summary.put("ventasTotales", totalVentas);
        summary.put("totalPedidos", totalPedidos);
        summary.put("totalUsuarios", totalUsuarios);
        summary.put("totalProductos", totalProductos);
        summary.put("productosEnInventario", productRepository.findByActiveTrue().size());

        List<Object[]> ventasPorEstado = saleRepository.countSalesGroupedByStatus();
        Map<String, Long> ventasPorEstadoMap = new LinkedHashMap<>();
        for (Object[] row : ventasPorEstado) {
            ventasPorEstadoMap.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        summary.put("ventasPorEstado", ventasPorEstadoMap);

        summary.put("ventasDelDia", calcularVentasDelDia());
        summary.put("ventasDeLaSemana", calcularVentasDeLaSemana());
        summary.put("ventasDelMes", calcularVentasDelMes());
        summary.put("fechaActual", LocalDateTime.now().toString());

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/pedidos-recientes")
    public ResponseEntity<List<Map<String, Object>>> getRecentOrders() {
        List<Sale> sales = saleRepository.findRecentActiveSales();
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < Math.min(sales.size(), 10); i++) {
            Sale sale = sales.get(i);
            Map<String, Object> order = new HashMap<>();
            order.put("id", sale.getId());
            order.put("pedido", String.format("#%03d", sale.getId()));
            order.put("fecha", sale.getCreatedOn() != null ? sale.getCreatedOn().toString() : "N/A");
            order.put("estado", sale.getStatus() != null ? sale.getStatus().getName() : "Sin asignar");
            order.put("estadoId", sale.getStatus() != null ? sale.getStatus().getId() : null);
            order.put("cantidad", sale.getQuantity());
            order.put("total", sale.getTotalAmount());
            order.put("activo", sale.getActive());

            if (sale.getDetails() != null && !sale.getDetails().isEmpty()) {
                SaleDetail firstDetail = sale.getDetails().get(0);
                order.put("producto", firstDetail.getProduct() != null ? firstDetail.getProduct().getName() : "Desconocido");
            }

            result.add(order);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<List<Map<String, Object>>> getBestSellingProducts() {
        List<Map<String, Object>> bestSellers = new ArrayList<>();
        Map<Long, Map<String, Object>> aggregated = new LinkedHashMap<>();

        List<Sale> sales = saleRepository.findAll();
        for (Sale sale : sales) {
            if (sale.getActive() == null || !sale.getActive()) continue;
            if (sale.getDetails() == null) continue;
            for (SaleDetail detail : sale.getDetails()) {
                if (detail.getProduct() == null) continue;
                Long productId = detail.getProduct().getId();
                if (!aggregated.containsKey(productId)) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("productoId", productId);
                    item.put("productoNombre", detail.getProduct().getName());
                    item.put("cantidadVendida", 0);
                    item.put("total", BigDecimal.ZERO);
                    aggregated.put(productId, item);
                }
                Map<String, Object> item = aggregated.get(productId);
                int currentQty = (int) item.get("cantidadVendida");
                BigDecimal currentTotal = (BigDecimal) item.get("total");
                item.put("cantidadVendida", currentQty + detail.getQuantity());
                item.put("total", currentTotal.add(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))));
            }
        }

        List<Map<String, Object>> sorted = aggregated.values().stream()
                .sorted((a, b) -> ((int) b.get("cantidadVendida")) - ((int) a.get("cantidadVendida")))
                .limit(5)
                .collect(Collectors.toList());

        for (Map<String, Object> ps : sorted) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", ps.get("productoId"));
            item.put("nombre", ps.get("productoNombre"));
            item.put("cantidadVendida", ps.get("cantidadVendida"));
            item.put("total", ps.get("total"));
            bestSellers.add(item);
        }

        return ResponseEntity.ok(bestSellers);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<Object[]> ventasPorEstado = saleRepository.countSalesGroupedByStatus();
        Map<String, Long> ventasPorEstadoMap = new LinkedHashMap<>();
        for (Object[] row : ventasPorEstado) {
            ventasPorEstadoMap.put(row[0].toString(), ((Number) row[1]).longValue());
        }

        List<Status> allStatuses = statusRepository.findByActiveTrue();
        Map<String, Object> estadoInfo = new LinkedHashMap<>();
        for (Status s : allStatuses) {
            Map<String, Object> info = new HashMap<>();
            info.put("id", s.getId());
            info.put("nombre", s.getName());
            info.put("totalPedidos", ventasPorEstadoMap.getOrDefault(s.getName(), 0L));
            estadoInfo.put(s.getName(), info);
        }
        stats.put("estados", estadoInfo);
        stats.put("totalUsuarios", userRepository.count());
        stats.put("productosActivos", productRepository.findByActiveTrue().size());

        BigDecimal ingresoTotal = saleRepository.sumTotalAmountOfActiveSales();
        stats.put("ingresoTotal", ingresoTotal != null ? ingresoTotal : BigDecimal.ZERO);
        stats.put("pedidosEntregados", ventasPorEstadoMap.getOrDefault("Entregado", 0L));
        stats.put("pedidosEnviados", ventasPorEstadoMap.getOrDefault("Enviado", 0L));
        stats.put("pedidosPendientes", ventasPorEstadoMap.getOrDefault("Pendiente", 0L));

        return ResponseEntity.ok(stats);
    }

    private BigDecimal calcularVentasDelDia() {
        BigDecimal[] total = {BigDecimal.ZERO};
        saleRepository.findAll().forEach(s -> {
            if (s.getActive() != null && s.getActive()
                    && s.getCreatedOn() != null
                    && s.getCreatedOn().toLocalDate().equals(LocalDateTime.now().toLocalDate())
                    && s.getTotalAmount() != null) {
                total[0] = total[0].add(s.getTotalAmount());
            }
        });
        return total[0];
    }

    private BigDecimal calcularVentasDeLaSemana() {
        LocalDateTime semanaInicio = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        BigDecimal[] total = {BigDecimal.ZERO};
        saleRepository.findAll().forEach(s -> {
            if (s.getActive() != null && s.getActive()
                    && s.getCreatedOn() != null
                    && s.getCreatedOn().isAfter(semanaInicio)
                    && s.getTotalAmount() != null) {
                total[0] = total[0].add(s.getTotalAmount());
            }
        });
        return total[0];
    }

    private BigDecimal calcularVentasDelMes() {
        LocalDateTime mesInicio = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        BigDecimal[] total = {BigDecimal.ZERO};
        saleRepository.findAll().forEach(s -> {
            if (s.getActive() != null && s.getActive()
                    && s.getCreatedOn() != null
                    && s.getCreatedOn().isAfter(mesInicio)
                    && s.getTotalAmount() != null) {
                total[0] = total[0].add(s.getTotalAmount());
            }
        });
        return total[0];
    }
}