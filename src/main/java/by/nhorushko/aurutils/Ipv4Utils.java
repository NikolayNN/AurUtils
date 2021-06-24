package by.nhorushko.aurutils;

import java.util.HashSet;
import java.util.Set;

public abstract class Ipv4Utils {

    /**
     * @param preparedIps contains ips prepared by {@link Ipv4Utils#decode(String[])}
     * @param ip
     */
    public static boolean match(Set<String> preparedIps, String ip) {
        for (String preparedIp : preparedIps) {
            if (ip.startsWith(preparedIp)) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> decode(String[] ips) {
        if (ips == null || ips.length == 0) {
            return Set.of();
        }
        Set<String> result = new HashSet<>();
        for (String ip : ips) {
            validate(ip);
            if (ip == null) {
                continue;
            }
            String replace = ip.trim().replace(".*", "");
            if (replace.equals("*")) {
                continue;
            }
            if (replace.length() > 0) {
                result.add(replace);
            }
        }
        return result;
    }

    public static void validate(String ip) {
        if (ip == null) {
            throw new NullPointerException("ip can't be null");
        }

        String[] split = ip.trim().split("\\.");
        if (split.length != 4) {
            throw new IllegalArgumentException(String.format("IP: '%s' expect 4 ip section, but was: %s", ip, split.length));
        }

        String prev = null;
        for (String s : split) {
            if (!s.equals("*")) {
                int n = Integer.parseInt(s);
                if (n < 0 || n > 255) {
                    throw new IllegalArgumentException(
                            String.format("IP: '%s' expect value in the section between 0 and 255 but was: %s", ip, s));
                }
            }
            if (prev != null && prev.equals("*") && !s.equals("*")) {
                throw new IllegalArgumentException(
                        String.format("IP: '%s' wrong order after section with *, not available section with number", ip));
            }
            prev = s;
        }
    }
}
