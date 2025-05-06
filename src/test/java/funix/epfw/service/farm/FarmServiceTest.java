package funix.epfw.service.farm;

import funix.epfw.model.farm.Farm;
import funix.epfw.repository.farm.FarmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FarmServiceTest {

    @Mock
    private FarmRepository farmRepository;

    @InjectMocks
    private FarmService farmService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // khởi tạo các @Mock và @InjectMocks
    }

    @Test
    void testFindByUserId() {
        Farm farm1 = new Farm();
        farm1.setId(1L);
        farm1.setFarmName("Farm A");

        Farm farm2 = new Farm();
        farm2.setId(2L);
        farm2.setFarmName("Farm B");

        when(farmRepository.findByUserId(10L)).thenReturn(Arrays.asList(farm1, farm2));

        List<Farm> farms = farmService.findByUserId(10L);

        assertEquals(2, farms.size());
        verify(farmRepository, times(1)).findByUserId(10L);
    }

    @Test
    void testSaveFarm() {
        Farm farm = new Farm();
        farm.setFarmName("New Farm");

        farmService.saveFarm(farm);

        verify(farmRepository, times(1)).save(farm);
    }

    @Test
    void testFindById_Found() {
        Farm farm = new Farm();
        farm.setId(1L);

        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));

        Farm found = farmService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(farmRepository.findById(999L)).thenReturn(Optional.empty());

        Farm found = farmService.findById(999L);

        assertNull(found);
    }

    @Test
    void testFindAll() {
        when(farmRepository.findAll()).thenReturn(Arrays.asList(new Farm(), new Farm()));

        List<Farm> farms = farmService.findAll();

        assertEquals(2, farms.size());
        verify(farmRepository, times(1)).findAll();
    }

    @Test
    void testDeleteFarmById() {
        farmService.deleteFarmById(5L);

        verify(farmRepository, times(1)).deleteById(5L);
    }
}