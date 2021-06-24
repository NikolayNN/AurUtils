package by.nhorushko.aurutils;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IpUtilsTest {

    @Test
    void handle1() {
        Set<String> actual = Ipv4Utils.decode(new String[]{"192.168.1.*"});
        Set<String> expected = Set.of("192.168.1");
        assertEquals(expected, actual);
    }

    @Test
    void handle2() {
        Set<String> actual = Ipv4Utils.decode(new String[]{"192.168.*.*"});
        Set<String> expected = Set.of("192.168");
        assertEquals(expected, actual);
    }

    @Test
    void handle3() {
        Set<String> actual = Ipv4Utils.decode(new String[]{"192.*.*.*"});
        Set<String> expected = Set.of("192");
        assertEquals(expected, actual);
    }

    @Test
    void handle4() {
        Set<String> actual = Ipv4Utils.decode(new String[]{"*.*.*.*"});
        Set<String> expected = Set.of();
        assertEquals(expected, actual);
    }

    @Test
    void handle5() {
        Set<String> actual = Ipv4Utils.decode(null);
        Set<String> expected = Set.of();
        assertEquals(expected, actual);
    }

    @Test
    void validate() {
        Ipv4Utils.validate("192.168.1.0");
        Ipv4Utils.validate("192.168.1.*");
        Ipv4Utils.validate("192.168.*.*");
        Ipv4Utils.validate("192.*.*.*");
        Ipv4Utils.validate("*.*.*.*");
        // do not throw exception
    }

    @Test
    void validate2() {
        assertEquals("ip can't be null",
                assertThrows(NullPointerException.class,
                        () -> Ipv4Utils.validate(null)).getMessage()
        );

        assertEquals("IP: '192.168.' expect 4 ip section, but was: 2",
                assertThrows(IllegalArgumentException.class,
                        () -> Ipv4Utils.validate("192.168.")).getMessage()
        );

        assertEquals("IP: '192.168.*.0' wrong order after section with *, not available section with number",
                assertThrows(IllegalArgumentException.class,
                        () -> Ipv4Utils.validate("192.168.*.0")).getMessage()
        );

        assertEquals("IP: '*.168.*.0' wrong order after section with *, not available section with number",
                assertThrows(IllegalArgumentException.class,
                        () -> Ipv4Utils.validate("*.168.*.0")).getMessage()
        );

        assertEquals("IP: '*.*.*.0' wrong order after section with *, not available section with number",
                assertThrows(IllegalArgumentException.class,
                        () -> Ipv4Utils.validate("*.*.*.0")).getMessage()
        );

        assertEquals("IP: '1000.1.1.0' expect value in the section between 0 and 255 but was: 1000",
                assertThrows(IllegalArgumentException.class,
                        () -> Ipv4Utils.validate("1000.1.1.0")).getMessage()
        );

        assertEquals("IP: '1.1.1.1000' expect value in the section between 0 and 255 but was: 1000",
                assertThrows(IllegalArgumentException.class,
                        () -> Ipv4Utils.validate("1.1.1.1000")).getMessage()
        );
    }

    @Test
    void match() {
        assertTrue(Ipv4Utils.match(Set.of("192.168"), "192.168.1.0"));
        assertFalse(Ipv4Utils.match(Set.of("192.168"), "192.167.1.0"));
    }
}
