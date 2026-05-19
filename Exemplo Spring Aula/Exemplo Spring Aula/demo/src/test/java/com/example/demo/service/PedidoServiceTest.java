package com.example.demo.service;

import com.example.demo.Entities.Cardapio;
import com.example.demo.Entities.Pedido;
import com.example.demo.Entities.Reserva;
import com.example.demo.repository.ICardapioRepository;
import com.example.demo.repository.IPedidoRepository;
import com.example.demo.repository.IReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private IPedidoRepository pedidoRepository;

    @Mock
    private IReservaRepository reservaRepository;

    @Mock
    private ICardapioRepository itemRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Reserva reserva;
    private Cardapio item;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setCliente(null);
        reserva.setMesa_Id(null);
        reserva.setDataReserva(LocalDate.now());
        reserva.setHoraReserva(LocalTime.now());
        reserva.setStatus(null);

        item = new Cardapio();
        item.setId(1L);
        item.setNome("Pizza");
        item.setDescricao("Mussarela");
        item.setPreco(BigDecimal.valueOf(35.50));
    }

    @Test
    void criarPedido_deveSalvarPedidoQuandoDadosValidos() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedido = pedidoService.criarPedido(1L, 1L, 2);

        assertThat(pedido).isNotNull();
        assertThat(pedido.getReserva()).isEqualTo(reserva);
        assertThat(pedido.getItem()).isEqualTo(item);
        assertThat(pedido.getQuantidade()).isEqualTo(2);
        assertThat(pedido.getValorTotal()).isEqualByComparingTo(BigDecimal.valueOf(71.00));

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void criarPedido_deveLancarQuandoReservaNaoEncontrada() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(1L, 1L, 1));

        assertThat(exception.getMessage()).contains("Reserva não encontrada");
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void criarPedido_deveLancarQuandoItemNaoEncontrado() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(1L, 1L, 1));

        assertThat(exception.getMessage()).contains("Item não encontrado");
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void criarPedido_deveLancarQuandoQuantidadeInvalida() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(1L, 1L, 0));

        assertThat(exception.getMessage()).contains("Quantidade inválida");
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void listarTodos_deveRetornarListaDePedidos() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setReserva(reserva);
        pedido.setItem(item);
        pedido.setQuantidade(1);
        pedido.setValorTotal(BigDecimal.valueOf(35.50));

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> pedidos = pedidoService.listarTodos();

        assertThat(pedidos).hasSize(1);
        assertThat(pedidos.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_deveRetornarPedidoExistente() {
        Pedido pedido = new Pedido();
        pedido.setId(2L);
        when(pedidoRepository.findById(2L)).thenReturn(Optional.of(pedido));

        Pedido result = pedidoService.buscarPorId(2L);

        assertThat(result.getId()).isEqualTo(2L);
    }

    @Test
    void buscarPorId_deveLancarQuandoNaoEncontrado() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.buscarPorId(2L));

        assertThat(exception.getMessage()).contains("Pedido não encontrado");
    }

    @Test
    void listarPorReserva_deveRetornarPedidosFiltrados() {
        Pedido pedido = new Pedido();
        pedido.setId(3L);
        when(pedidoRepository.findByReservaId(1L)).thenReturn(List.of(pedido));

        List<Pedido> pedidos = pedidoService.listarPorReserva(1L);

        assertThat(pedidos).hasSize(1);
        assertThat(pedidos.get(0).getId()).isEqualTo(3L);
    }

    @Test
    void deletar_deveChamarRepositorio() {
        doNothing().when(pedidoRepository).deleteById(4L);

        pedidoService.deletar(4L);

        verify(pedidoRepository, times(1)).deleteById(4L);
    }
}
