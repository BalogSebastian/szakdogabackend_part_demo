// hu/szakdolgozat/azonositas/dns/OrtizHeurisztika.java
package hu.szakdolgozat.azonositas.dns;

import java.util.*;

public final class OrtizHeurisztika {
    private OrtizHeurisztika() {}

    private static final Map<String, List<String>> KW = Map.of(
            "Boldog", List.of("happy","joy","smile","dance","sun","good","party","boldog","öröm","mosoly","tánc"),
            "Szomorú", List.of("sad","cry","blue","tears","alone","lonely","szomorú","sírok","könny","magány"),
            "Gyász", List.of("grief","loss","requiem","mourn","funeral","gyász","veszteség"),
            "Szerelmi bánat", List.of("heartbreak","broken","ex","leave","goodbye","unlove","bánat","szakítás","elhagytál"),
            "Nyugodt", List.of("calm","chill","sleep","ocean","quiet","peace","nyugodt","csend","laza"),
            "Energetikus", List.of("power","energy","fire","fast","run","pump","energetic","pörgés","tűz")
    );

    public static Map<String, Double> profil(List<String> cimLista, List<String> eloadoLista) {
        Map<String, Double> pont = new LinkedHashMap<>();
        for (String k : Hangulatok.ALAP) pont.put(k, 0.0);

        for (int i = 0; i < cimLista.size(); i++) {
            String c = (cimLista.get(i) == null ? "" : cimLista.get(i)).toLowerCase(Locale.ROOT);
            String e = (i < eloadoLista.size() ? Objects.toString(eloadoLista.get(i), "") : "").toLowerCase(Locale.ROOT);

            for (String hang : Hangulatok.ALAP) {
                double inc = 0.0;
                for (String w : KW.getOrDefault(hang, List.of())) {
                    if (c.contains(w)) inc += 1.0;
                    if (e.contains(w)) inc += 0.3;
                }
                pont.put(hang, pont.get(hang) + inc);
            }
        }

        double ossz = pont.values().stream().mapToDouble(Double::doubleValue).sum();
        if (ossz <= 0.0001) {
            double egys = 1.0 / Hangulatok.ALAP.size();
            for (String k : pont.keySet()) pont.put(k, egys);
            return pont;
        }
        for (String k : pont.keySet()) pont.put(k, pont.get(k) / ossz);
        return pont;
    }

    public static String foHangulat(Map<String, Double> profil) {
        return profil.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Nyugodt");
    }

    public static List<String> kulcsszavai(String celHangulat) {
        return switch (celHangulat) {
            case "Boldog" -> List.of("happy", "joy", "dance");
            case "Szomorú" -> List.of("sad", "cry", "alone");
            case "Gyász" -> List.of("requiem", "grief", "mourn");
            case "Szerelmi bánat" -> List.of("heartbreak", "broken", "goodbye");
            case "Energetikus" -> List.of("energetic", "power", "fire");
            default -> List.of("calm", "chill", "peace"); // Nyugodt + fallback
        };
    }
}
