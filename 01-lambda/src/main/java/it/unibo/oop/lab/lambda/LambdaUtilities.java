package it.unibo.oop.lab.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//import static java.util.Collections.emptyList;
//import static java.util.Collections.emptyMap;

/**
 * Questa classe conterrà quattro funzioni di utilità su liste e mappe, di cui la prima è fornita a titolo di esempio.
 * <br>
 * Tutti questi metodi accettano come secondo argomento un'interfaccia funzionale della libreria Java (java.util.function).
 * Ciò consente di richiamarli utilizzando la sintassi lambda concisa, come avviene nella funzione main.
 * <br>
 * Realizza i tre metodi **SENZA** utilizzare la libreria Stream, ma sfruttando esclusivamente le lambda.
 *
 */
public final class LambdaUtilities {

    private LambdaUtilities() {
    }

    /**
     * @param list
     *            the input list
     * @param op
     *            the process to run on each element
     * @param <T>
     *            element type
     * @return a new list containing, for each element of list, the element and
     *         a processed version
     */
    //Duplica ogni elemento, aggiungendo anche il risultato di una funzione
    public static <T> List<T> dup(final List<T> list, final UnaryOperator<T> op) {
        final List<T> l = new ArrayList<>(list.size() * 2);
        list.forEach(t -> {
            l.add(t);
            l.add(op.apply(t));
        });
        return l;
    }

    /**
     * @param list
     *            input list
     * @param pre
     *            predicate to execute
     * @param <T>
     *            element type
     * @return a list where each value is an Optional, holding the previous
     *         value only if the predicate passes, and an Empty optional
     *         otherwise.
     */
    //Per ogni elemento, restituisce un Optional pieno o vuoto in base a una condizione
    public static <T> List<Optional<T>> optFilter(final List<T> list, final Predicate<T> pre) {
        // Optional.empty();
       // Optional.of("1");
        /*
         * Suggestion: consider Optional.filter
         */
        final List<Optional<T>> l = new ArrayList<>(list.size());
        list.forEach(t -> {
            if(pre.test(t)) {
                l.add(Optional.of(t));
            } else {
                l.add(Optional.empty());
            }
        });
        return l;
    }

    /**
     * @param list
     *            input list
     * @param op
     *            a function that, for each element, computes a key
     * @param <T>
     *            element type
     * @param <R>
     *            key type
     * @return a map that groups into categories each element of the input list,
     *         based on the mapping done by the function
     */
    //Raggruppa gli elementi in una mappa in base a una funzione. Divide gli elementi della lista in categorie.
    public static <R, T> Map<R, Set<T>> group(final List<T> list, final Function<T, R> op) {
        /*
         * Suggestion: consider Map.merge
         */

        //Crea una mappa vuota
        final Map<R, Set<T>> result = new HashMap<>(); //ogni chiave (R) deve contenere un Set<T> con tutti gli elementi appartenenti a quel gruppo. Cioè una chiave R identifica un insieme di elementi T.

        list.forEach(t -> {
            R key = op.apply(t); //Applica la lambda all'elemento t e ottiene la chiave.

            result.putIfAbsent(key, new HashSet<>()); //Se la chiave non esiste, crea un Set vuoto e lo associa alla chiave.

            result.get(key).add(t); //Recupera il Set associato alla chiave e ci aggiunge l'elemento t.
        });

        return result; //Restituisce la mappa completa
    }

    /** 
     * @param map
     *            input map
     * @param def
     *            the supplier
     * @param <V>
     *            element type
     * @param <K>
     *            key type
     * @return a map whose non-present values are filled with the value provided
     *         by the supplier
     */
    //Riempie gli Optional vuoti di una mappa con valori generati da un Supplier
    public static <K, V> Map<K, V> fill(final Map<K, Optional<V>> map, final Supplier<V> def) {
        /*
         * Suggestion: consider Optional.orElse
         *
         * Keep in mind that a map can be iterated through its forEach method
         */

        //Crea una nuova mappa vuota
        final Map<K, V> result = new HashMap<>();

        map.forEach((k, v) -> { //Scorre la mappa originale: k = chiave. v = valore (Optional).
            result.put(k, v.orElse(def.get())); //Inserisce nella nuova mappa:
            // Se l'Optional contiene un valore, usa quello.
            // Se è vuoto, usa def.get() per generare un valore sostitutivo.
        });

        return result; //Restituisce la nuova mappa
    }

    /**
     * @param args
     *            ignored
     */
    @SuppressWarnings("PMD.SystemPrintln")
    // Nel main() vengono chiamati i metodi passando loro le lambda.
    public static void main(final String[] args) {
        final List<Integer> li = IntStream.range(1, 8).boxed().collect(Collectors.toList());
        // [1,2,3,4,5,6,7]

        System.out.println(dup(li, x -> x + 100)); //lambda
        //dup() è il metodo che esegue l'operazione.
        // x -> x + 100 è la lambda che gli dice cosa fare.
        /*
         * [1, 101, 2, 102, 3, 103, 4, 104, 5, 105, 6, 106, 7, 107]
         */
        System.out.println(group(li, x -> x % 2 == 0 ? "even" : "odd")); //lambda
        /*
         * {odd=[1, 3, 5, 7], even=[2, 4, 6]}
         */
        final List<Optional<Integer>> opt = optFilter(li, x -> x % 3 == 0); //lambda
        System.out.println(opt);
        /*
         * [Optional.empty, Optional.empty, Optional[3], Optional.empty,
         * Optional.empty, Optional[6], Optional.empty]
         */
        final Map<Integer, Optional<Integer>> map = new HashMap<>();
        for (int i = 0; i < opt.size(); i++) {
            map.put(i, opt.get(i));
        }
        System.out.println(fill(map, () -> (int) (-Math.random() * 10))); //lambda
        /*
         * {0=-2, 1=-7, 2=3, 3=-3, 4=-7, 5=6, 6=-3}
         */

        //Queste lambda sono funzioni che passi come argomento ai metodi
    }
}
