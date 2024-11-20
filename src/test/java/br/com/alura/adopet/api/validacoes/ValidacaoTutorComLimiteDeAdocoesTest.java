package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validador;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Mock
    private Tutor tutor;

    @Test
    void naoDeveriaPermitirSolicitacaoDeAdocaoTutorComAdocaoEmAndamento() {
        // Arrange
        var tutorId = 1L;
        when(dto.idTutor()).thenReturn(tutorId);
        when(tutorRepository.getReferenceById(tutorId)).thenReturn(tutor);

        var adocao1 = new Adocao();
        adocao1.setTutor(tutor);
        adocao1.setStatus(StatusAdocao.APROVADO);

        var adocao2 = new Adocao();
        adocao2.setTutor(tutor);
        adocao2.setStatus(StatusAdocao.APROVADO);

        var adocao3 = new Adocao();
        adocao3.setTutor(tutor);
        adocao3.setStatus(StatusAdocao.APROVADO);

        var adocao4 = new Adocao();
        adocao4.setTutor(tutor);
        adocao4.setStatus(StatusAdocao.APROVADO);

        var adocao5 = new Adocao();
        adocao5.setTutor(tutor);
        adocao5.setStatus(StatusAdocao.APROVADO);

        var adocoes = List.of(adocao1, adocao2, adocao3, adocao4, adocao5);
        when(adocaoRepository.findAll()).thenReturn(adocoes);

        // Act + Assert
        assertThrows(ValidacaoException.class, () -> validador.validar(dto));
    }

    @Test
    void deveriaPermitirSolicitacaoDeAdocaoTutorSemAdocaoEmAndamento() {
        // Arrange
        Long tutorId = 1L;
        when(dto.idTutor()).thenReturn(tutorId);
        when(tutorRepository.getReferenceById(tutorId)).thenReturn(tutor);
        when(adocaoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act + Assert
        assertDoesNotThrow(() -> validador.validar(dto));
    }
}