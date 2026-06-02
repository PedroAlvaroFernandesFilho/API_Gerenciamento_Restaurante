package com.example.demo.service;

import com.example.demo.Entities.Cardapio;
import com.example.demo.Entities.Pedido;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.mapper.PedidoMapper;
import com.example.demo.repository.ICardapioRepository;
import com.example.demo.repository.IPedidoRepository;
import com.example.demo.repository.IReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private IPedidoRepository pedidoRepository;

    @Mock
    private IReservaRepository reservaRepository;

    @Mock
    private ICardapioRepository itemRepository;

    @Mock
    private PedidoMapper pedidoMapper;

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
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setReservaId(1L);
        pedidoDTO.setItemId(1L);
        pedidoDTO.setQuantidade(2);

        Pedido entity = new Pedido();
        entity.setQuantidade(pedidoDTO.getQuantidade());

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(pedidoMapper.toEntity(eq(pedidoDTO))).thenReturn(entity);
        when(pedidoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedido = pedidoService.criarPedido(pedidoDTO);

        assertThat(pedido).isNotNull();
        assertThat(pedido.getReserva()).isEqualTo(reserva);
        assertThat(pedido.getItem()).isEqualTo(item);
        assertThat(pedido.getQuantidade()).isEqualTo(2);
        assertThat(pedido.getValorTotal()).isEqualByComparingTo(BigDecimal.valueOf(71.00));

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void criarPedido_deveLancarQuandoReservaNaoEncontrada() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setReservaId(1L);
        pedidoDTO.setItemId(1L);
        pedidoDTO.setQuantidade(1);

        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoDTO));

        assertThat(exception.getMessage()).contains("Reserva não encontrada");
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void criarPedido_deveLancarQuandoItemNaoEncontrado() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setReservaId(1L);
        pedidoDTO.setItemId(1L);
        pedidoDTO.setQuantidade(1);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoDTO));

        assertThat(exception.getMessage()).contains("Item não encontrado");
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void criarPedido_deveLancarQuandoQuantidadeInvalida() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setReservaId(1L);
        pedidoDTO.setItemId(1L);
        pedidoDTO.setQuantidade(0);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoDTO));

        assertThat(exception.getMessage()).contains("Quantidade inválida");
        verify(pedidoRepository, never()).save(any(Pedido.class));
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
        when(reservaRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.findByReservaId(1L)).thenReturn(List.of(pedido));

        List<Pedido> pedidos = pedidoService.listarPorReserva(1L);

        assertThat(pedidos).hasSize(1);
        assertThat(pedidos.get(0).getId()).isEqualTo(3L);
    }

    @Test
    void listarPorReserva_deveRetornarListaVaziaQuandoNaoExistiremPedidos() {
        when(reservaRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.findByReservaId(1L)).thenReturn(List.of());

        List<Pedido> pedidos = pedidoService.listarPorReserva(1L);

        assertThat(pedidos).isEmpty();
    }

    @Test
    void deletar_deveCancelarPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(4L);
        pedido.setStatus(com.example.demo.Enums.StatusPedido.CONFIRMADO);
        when(pedidoRepository.findById(4L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0, Pedido.class));

        pedidoService.deletar(4L);

        assertThat(pedido.getStatus()).isEqualTo(com.example.demo.Enums.StatusPedido.CANCELADO);
        verify(pedidoRepository, times(1)).save(pedido);
    }
}
