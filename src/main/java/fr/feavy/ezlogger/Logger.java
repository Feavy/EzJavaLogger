package fr.feavy.ezlogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static final DateTimeFormatter TODAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter NOW_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<String> filteredPackage = new ArrayList<>();
    private static boolean showPackages = false;

    private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.SHOW_HIDDEN_FRAMES);

    /**
     * Active l'affichage du nom des package des classes appelantes.
     */
    public static void showPackages() {
        showPackages = true;
    }

    /**
     * Log un message de débogage
     * @param message message à afficher
     */
    public static void debug(String message, Object... args) {
        addMessage(null, "DEBUG", message, args);
    }

    /**
     * Log un message d'information
     * @param message message à afficher
     */
    public static void info(String message, Object... args) {
        addMessage("34", "INFO", message, args);
    }

    /**
     * Log un message de danger
     * @param message message à afficher
     */
    public static void warning(String message, Object... args) {
        addMessage("33", "WARNING", message, args);
    }

    /**
     * Log un message d'erreur
     * @param message message à afficher
     */
    public static void error(String message, Object... args) {
        addMessage("31", "ERROR", message, args);
    }

    /**
     * Log un message de succès
     * @param message message à afficher
     */
    public static void success(String message, Object... args) {
        addMessage("32", "SUCCESS", message, args);
    }

    /**
     * Ajoute un message (sur la sortie standard et dans un fichier).
     * @param color Numéro du code ANSI d'une couleur ou null pour pas de couleur.
     * @param type Type du message à ajouter. Ex: WARNING
     * @param message Message à ajouter.
     */
    private static void addMessage(String color, String type, String message, Object... args) {
        StackWalker.StackFrame trace = WALKER.walk(s -> s.skip(2).findFirst().get());

        String clazz = trace.getClassName();
        if(!matchFilters(clazz)) {
            return;
        }
        if(args.length > 0) {
            for (Object arg : args) {
                message = message.replaceFirst("%s", arg == null ? "null" : arg.toString().replace("$", "§"));
            }
        }
        StringBuilder messageBuilder = new StringBuilder(message);
        StringBuilder prefix = new StringBuilder("[" + type + "] " + NOW_FORMATTER.format(LocalDateTime.now()) + " ");
        if(showPackages) {
            prefix.append(clazz);
        }else{
            String[] parts = clazz.split("\\.");
            prefix.append(parts[parts.length-1]);
        }
        prefix.append(".").append(trace.getMethodName()).append("(").append(trace.getFileName()).append(":").append(trace.getLineNumber()).append(")").append(" : ");
        messageBuilder.insert(0, prefix);
        if (color != null) {
            messageBuilder.insert(0, "\u001B[" + color + "m");
            messageBuilder.append("\u001B[0m");
        }
        System.out.println(messageBuilder);
    }

    private static boolean matchFilters(String clazz) {
        if(filteredPackage.isEmpty()) {
            return true;
        }
        for(String pack : filteredPackage) {
            if(clazz.startsWith(pack)) {
                return true;
            }
        }
        return false;
    }
}