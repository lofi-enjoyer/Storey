package me.lofienjoyer.Storey.regex;

import me.lofienjoyer.Storey.utils.StoreyRegex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegexTests {

    @Test
    public void testFileNameRegex() {
        assertTrue(StoreyRegex.validateFileName("foto.png"));
        assertTrue(StoreyRegex.validateFileName("FOTO_1.png"));
        assertTrue(StoreyRegex.validateFileName("cv-Clara.PDF"));

        assertFalse(StoreyRegex.validateFileName("./cv.pdf"));
        assertFalse(StoreyRegex.validateFileName("carpeta"));
    }

    @Test
    public void testPathRegex() {
        assertTrue(StoreyRegex.validatePath("carpeta"));
        assertTrue(StoreyRegex.validatePath("carpeta-1", "fotos"));
        assertTrue(StoreyRegex.validatePath("FOTOS_VIAJE", "hotel"));

        assertFalse(StoreyRegex.validatePath("FOTOS_VIAJE", "/hotel"));
        assertFalse(StoreyRegex.validatePath("mis fotos", "casa"));
        assertFalse(StoreyRegex.validatePath("mis.fotos"));
    }

}
