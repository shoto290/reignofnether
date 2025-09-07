import com.solegendary.reignofnether.config.JsonConfigManager;
import com.solegendary.reignofnether.config.JsonConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {
    private static final Logger LOGGER = LogManager.getLogger();

    @Test
    public void testConfigStructure() {
        JsonConfig config = JsonConfigManager.createDefaultConfig();
        
        assertNotNull(config.units);
        assertNotNull(config.buildings);
        assertNotNull(config.research);
        assertNotNull(config.enchantments);
        
        // Test that we have some data
        assertTrue(config.units.size() > 0);
        assertTrue(config.buildings.size() > 0);
        assertTrue(config.research.size() > 0);  
        assertTrue(config.enchantments.size() > 0);
        
        // Test that types are correct
        assertNotNull(config.units.get("creeper"));
        assertNotNull(config.buildings.get("beacon"));
        assertNotNull(config.research.get("golem_smithing"));
        assertNotNull(config.enchantments.get("maiming"));
        
        LOGGER.info("Config structure test passed!");
        LOGGER.info("Units: " + config.units.size());
        LOGGER.info("Buildings: " + config.buildings.size());
        LOGGER.info("Research: " + config.research.size());
        LOGGER.info("Enchantments: " + config.enchantments.size());
    }
}