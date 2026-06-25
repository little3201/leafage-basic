package top.leafage.hypervisor.assets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import top.leafage.hypervisor.assets.domain.Template;
import top.leafage.hypervisor.assets.domain.dto.TemplateDTO;
import top.leafage.hypervisor.assets.domain.vo.TemplateVO;
import top.leafage.hypervisor.assets.repository.SectionRepository;
import top.leafage.hypervisor.assets.repository.TemplateRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TemplateServiceImplTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private TemplateServiceImpl templateService;

    private TemplateDTO dto;
    private Template entity;

    @BeforeEach
    void setUp() {
        dto = new TemplateDTO();
        dto.setName("test");
        dto.setType("word");

        entity = TemplateDTO.toEntity(dto);
    }

    @Test
    void retrieve() {
        Page<Template> page = new PageImpl<>(List.of(entity));
        when(templateRepository.findAll(ArgumentMatchers.<Specification<Template>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<TemplateVO> voPage = templateService.retrieve(0, 2, "id", true, "name:like:test");
        assertEquals(1, voPage.getTotalElements());
        verify(templateRepository).findAll(ArgumentMatchers.<Specification<Template>>any(), any(Pageable.class));
    }

    @Test
    void fetch() {
        when(templateRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        TemplateVO vo = templateService.fetch(anyLong());
        assertNotNull(vo);
        assertEquals("test", vo.name());
    }

    @Test
    void create() {
        when(templateRepository.existsByName("test")).thenReturn(false);
        when(templateRepository.save(any(Template.class))).thenReturn(entity);

        TemplateVO vo = templateService.create(dto);
        assertNotNull(vo);
        assertEquals("test", vo.name());
    }

    @Test
    void modify() {
        when(templateRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(templateRepository.existsByName("demo")).thenReturn(false);
        when(templateRepository.save(any(Template.class))).thenReturn(entity);

        dto.setName("demo");
        TemplateVO vo = templateService.modify(1L, dto);
        assertNotNull(vo);
        assertEquals("demo", vo.name());
    }

    @Test
    void enable() {
        when(templateRepository.existsById(anyLong())).thenReturn(true);
        when(templateRepository.enableById(anyLong())).thenReturn(1);

        assertTrue(templateService.enable(anyLong()));
    }

    @Test
    void remove_not_found() {
        when(templateRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> templateService.remove(anyLong())
        );
        assertEquals("template not found: 0", exception.getMessage());
    }
}
