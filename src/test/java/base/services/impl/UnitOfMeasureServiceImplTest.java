package base.services.impl;

import base.commands.UnitOfMeasureCommand;
import base.converters.UnitOfMeasureToUnitOfMeasureCommand;
import base.model.UnitOfMeasure;
import base.repositories.UnitOfMeasureRepository;
import base.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService service;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {
        // GIVEN
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure uomOne = new UnitOfMeasure();
        uomOne.setId(1L);
        unitOfMeasures.add(uomOne);

        UnitOfMeasure uomTwo = new UnitOfMeasure();
        uomTwo.setId(2L);
        unitOfMeasures.add(uomTwo);

        // WHEN
        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);
        Set<UnitOfMeasureCommand> unitOfMeasureCommands = service.listAllUoms();

        // THEN
        assertEquals(2, unitOfMeasureCommands.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}