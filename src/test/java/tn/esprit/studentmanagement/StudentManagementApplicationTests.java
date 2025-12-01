package tn.esprit.studentmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import tn.esprit.studentmanagement.services.DepartmentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class StudentManagementApplicationTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;
    
    private Department department1;
    private Department department2;

    @BeforeEach
    void setUp() {
        // Créer les objets Department avec le constructeur
        // Puisque vous avez @AllArgsConstructor, vous pouvez utiliser new Department(...)
        department1 = new Department(1L, "Informatique", "Bâtiment A", "0123456789", "Dr. Smith", null);
        department2 = new Department(2L, "Mathématiques", "Bâtiment B", "0987654321", "Dr. Johnson", null);
    }

    @Test
    void contextLoads() {
        // Test d'intégration existant
        assertTrue(true);
    }

    @Test
    void testGetAllDepartments() {
        // Arrange
        List<Department> departments = Arrays.asList(department1, department2);
        when(departmentRepository.findAll()).thenReturn(departments);

        // Act
        List<Department> result = departmentService.getAllDepartments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Informatique", result.get(0).getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));

        // Act
        Department result = departmentService.getDepartmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdDepartment()); // Utilisez getIdDepartment() ici
        assertEquals("Informatique", result.getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        // Arrange
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(java.util.NoSuchElementException.class, () -> {
            departmentService.getDepartmentById(99L);
        });
        verify(departmentRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveDepartment() {
        // Arrange
        when(departmentRepository.save(department1)).thenReturn(department1);

        // Act
        Department result = departmentService.saveDepartment(department1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdDepartment()); // Utilisez getIdDepartment() ici
        assertEquals("Informatique", result.getName());
        verify(departmentRepository, times(1)).save(department1);
    }

    @Test
    void testDeleteDepartment() {
        // Arrange
        doNothing().when(departmentRepository).deleteById(1L);

        // Act
        departmentService.deleteDepartment(1L);

        // Assert
        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
