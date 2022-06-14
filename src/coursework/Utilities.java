package coursework;

public class Utilities {
    /**
     * Проверка, что первая буква - большая
     * @param str строка
     * @return первая буква большая
     */
    public static boolean isInCorrectWordCase(String str) {
        if (str.length() < 1) {
            return false;
        }
        if (Character.isUpperCase(str.charAt(0)))
            return true;
        return false;
    }

    /**
     * Убрать двойные пробелы со строки
     * @param string строка
     * @return строка без двойных пробелов
     */
    public static String removeDoubleWhitespaces(String string){
        return string.trim().replaceAll(" +", " ");
    }
}
