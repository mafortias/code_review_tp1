/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import fr.isima.zz2.f5.ai.checkpassword.AwesomePasswordChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 *
 * @author Ryadh B. and Matheo F.
 */
public class AwesomePasswordCheckerTest {

    private AwesomePasswordChecker checker;

    @BeforeEach
    public void setUp() throws IOException {
        String path = "src/test/resources/cluster_centers_HAC_aff.csv";
        checker = AwesomePasswordChecker.getInstance(Files.newFileInputStream(Paths.get(path)));
    }

    @Test
    public void testGetDistance() {
        // Test de la méthode getDistance
        String password = "romainaznar";
        
        double distance = checker.getDistance(password);
        
        // Vérifiez que la distance est une valeur valide, non infinie
        assertNotEquals(Double.MAX_VALUE, distance, "La distance ne doit pas être infinie.");
        assertTrue(distance >= 0, "La distance doit être positive.");
    }

    @Test
    public void testComputeMd5() {
        // Test de la méthode computeMd5
        String input = "romainaznar";
        String expectedMd5 = "03eafefc3b5a148e10aefeb82b7a6d28";  // MD5 attendu pour "romainaznar"
        
        String md5 = AwesomePasswordChecker.computeMd5(input);
        
        assertEquals(expectedMd5, md5, "L'empreinte MD5 calculée ne correspond pas à celle attendue.");
    }

    @Test
    public void testSingletonInstance() throws IOException {
        // Test pour vérifier que le singleton fonctionne
        AwesomePasswordChecker instance1 = AwesomePasswordChecker.getInstance();
        AwesomePasswordChecker instance2 = AwesomePasswordChecker.getInstance();
        
        assertSame(instance1, instance2, "Les instances ne sont pas les mêmes.");
    }
}
