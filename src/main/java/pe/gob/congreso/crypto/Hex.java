package pe.gob.congreso.crypto;

/**
 *
 */
public class Hex {

    private static final char[] HEX = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static char[] encode(byte[] bytes) {
        final int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];

        int j = 0;
        for (byte b : bytes) {
            // Char for top 4 bits
            result[j++] = HEX[(0xF0 & b) >>> 4];
            // Bottom 4
            result[j++] = HEX[(0x0F & b)];
        }

        return result;
    }

    public static byte[] decode(CharSequence s) {
        int nChars = s.length();

        if (nChars % 2 != 0) {
            throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
        }

        byte[] result = new byte[nChars / 2];

        for (int i = 0; i < nChars; i += 2) {
            int msb = Character.digit(s.charAt(i), 16);
            int lsb = Character.digit(s.charAt(i + 1), 16);

            if (msb < 0 || lsb < 0) {
                throw new IllegalArgumentException("Non-hex character in input: " + s);
            }
            result[i / 2] = (byte) ((msb << 4) | lsb);
        }
        return result;
    }

    public static String decodeSiga(String par_input) {
        String resultado = "";

        if (par_input.trim().length() == 0) {
            resultado = "";
        } else {
            String tabla = "5736082914";
            String output = "";
            int k;
            int m;
            int pos;
            int length = par_input.trim().length();

            if (length == 36) {
                length = (int) Math.sqrt(Double.parseDouble(par_input.substring(30, 33)) - 100);
                k = 1;

                while (k <= length) {
                    String aux = "00" + String.valueOf(k);
                    pos = tabla.indexOf(aux.substring(aux.length() - 2, aux.length()).substring(1)) + 1;
                    m = (int) Double.parseDouble(par_input.substring((pos - 1) * 3, (pos - 1) * 3 + 3));
                    m = m - k * k;
                    output = output + (char) m;
                    k = k + 1;
                }

            } else if (length == 66) {
                length = (int) Math.sqrt(Double.parseDouble(par_input.substring(60, 63)) - 100);
                k = 1;

                while (k <= length) {
                    if (k > 10) {
                        m = k - 10;
                    } else {
                        m = k;
                    }

                    String aux = "00" + String.valueOf(m);
                    pos = tabla.indexOf(aux.substring(aux.length() - 2, aux.length()).substring(1)) + 1;

                    if (k > 10) {
                        pos = pos + 10;
                    }

                    m = (int) Double.parseDouble(par_input.substring((pos - 1) * 3, (pos - 1) * 3 + 3));
                    m = m - k * k;
                    output = output + (char) m;
                    k = k + 1;
                }
            }
            resultado = output;
        }

        return resultado;
    }

    public static String encodeSiga(String par_input) {
        String resultado = "";

        if (par_input.trim().length() == 0) {
            resultado = "";
        } else {
            String tabla = "5736082914";
            String output = "";
            int k;
            int m;
            int pos;
            int length = par_input.trim().length();

            if (length <= 10) {
                k = 1;

                while (k <= 10) {
                    double var = (Math.random() * (k * 25 - 1) + 1);

                    int aux1 = (int) var;
                    String aux2 = "000" + String.valueOf(aux1);
                    output = output + aux2.substring(aux2.length() - 3, aux2.length());
                    k = k + 1;
                }

                int aux3 = (int) (Math.random() * 255 + 1);

                String aux4 = "000" + String.valueOf(aux3).trim();
                output = output + String.valueOf(length * length + 100) + aux4.substring(aux4.length() - 3, aux4.length());
                k = 1;

                while (k <= length) {
                    String aux5 = "00" + String.valueOf(k);
                    pos = tabla.indexOf(aux5.substring(aux5.length() - 2, aux5.length()).substring(1)) + 1;
                    String aux6 = "000" + String.valueOf(par_input.substring(k - 1, k).codePointAt(0) + k * k);
                    output = output.substring(0, (pos - 1) * 3) + aux6.substring(aux6.length() - 3) + output.substring((pos * 3), output.length());
                    k = k + 1;
                }

            } else {
                k = 1;

                while (k <= 20) {
                    int aux1 = (int) (Math.random() * (k * 25 - 1) + 1);
                    String aux2 = "000" + String.valueOf(aux1);
                    output = output + aux2.substring(aux2.length() - 3, aux2.length());
                    k = k + 1;
                }

                int aux3 = (int) (Math.random() * 255 + 1);
                String aux4 = "000" + String.valueOf(aux3).trim();

                output = output + String.valueOf(length * length + 100) + aux4.substring(aux4.length() - 3, aux4.length());

                k = 1;

                while (k <= length) {
                    if (k > 10) {
                        m = k - 10;
                    } else {
                        m = k;
                    }

                    String aux = "00" + String.valueOf(m);
                    pos = tabla.indexOf(aux.substring(aux.length() - 2, aux.length()).substring(1)) + 1;

                    if (k > 10) {
                        pos = pos + 10;
                    }

                    String aux6 = "000" + String.valueOf(par_input.substring(k - 1, k).codePointAt(0) + k * k);
                    output = output.substring(0, (pos - 1) * 3) + aux6.substring(aux6.length() - 3) + output.substring((pos * 3), output.length());

                    k = k + 1;

                }

            }
            resultado = output;
        }

        return resultado;
    }
}
