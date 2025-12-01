package tn.esprit.studentmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import tn.esprit.studentmanagement.services.DepartmentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentManagementApplicationTests {

    @MockBean
    private DepartmentRepository departmentRepository;

    // Vous devez injecter le DepartmentService ou le créer manuellement
    // Option 1: Si DepartmentService est un bean Spring
    // @Autowired
    // private DepartmentService departmentService;
    
    // Option 2: Créer manuellement
    private DepartmentService departmentService;
    
    private Department department1;
    private Department department2;

    @BeforeEach
    void setUp() {
        // Initialiser le service avec le mock repository
        departmentService = new DepartmentService();
        
        // Pour injecter le mock dans le service, vous avez besoin d'un setter
        // ou de la réflexion. Voici avec la réflexion :
        try {
            var field = DepartmentService.class.getDeclaredField("departmentRepository");
            field.setAccessible(true);
            field.set(departmentService, departmentRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        // Initialisation des données de test
        department1 = new Department();
        department1.setId(1L);
        department1.setName("Informatique");
        
        department2 = new Department();
        department2.setId(2L);
        department2.setName("Mathématiques");
    }

    @Test
    void contextLoads() {
        // Test d'intégration existant
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
        assertEquals(1L, result.getId());
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
        assertEquals(1L, result.getId());
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
