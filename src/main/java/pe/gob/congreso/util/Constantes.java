package pe.gob.congreso.util;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Constantes {

	public static final String PERFIL_RESPONSABLE_MESA_PARTES = "STD-RESMEP";
	public static final String PERFIL_OPERADOR_MESA_PARTES = "STD-OPEMEP";
	public static final String PERFIL_RESPONSABLE_DEL_AREA = "STD-ADMRES";
	public static final String PERFIL_USUARIO_DEL_STD = "STD-USUARIO";
	public static final String PERFIL_ADMINISTRADOR_DEL_STD = "STD-ADMINI";
	public static final Integer PRIMER_REGISTRO = new Integer(1);
	public static final Boolean SUBSANADO = true;
	public static final Boolean NO_SUBSANADO = false;
	public static final String ESTADO_ACTIVO_CANAL_ENVIO_ASOCIADO = "01";
	public static final String ESTADO_ACTIVO = "1";
	public static final String ESTADO_INACTIVO = "0";
	public static final String ESTADO_CARGO_ENVIADO = "0";
	public static final String ESTADO_CARGO_CONFIRMADO = "1";
	public static final String ESTADO_FICHA_PROVEIDO_CREADO = "0";
	
	public static final Boolean HABILITADO = true;
	public static final Boolean NO_HABILITADO = false;
	public static final String SELECCIONADO_ALL_USERS = "0";
	public static final String SEMAFORO_BLANCO = "01";
	public static final String SEMAFORO_CELESTE = "02";
	public static final String SEMAFORO_AMARILLO = "03";
	public static final String SEMAFORO_VERDE = "04";
	public static final String SEMAFORO_ROJO = "05";
	public static final Integer ENVIO_PARA_MESA_DE_PARTES = 1;
	public static final Integer ENVIO_PARA_ESTAFETA = 1;
	public static final Integer ENVIO_PARA_CASILLERO = 2;
	public static final String ENVIO_PARA_DEPENDENCIA = "03";
	public static final String ENVIO_PARA_TRAMITE_INTERNO = "04";
	public static final String ETAPA_REGISTRADO = "01";
	public static final String ETAPA_EN_BANDEJA_DE_ENVIO = "02";
	public static final String ETAPA_EN_REPORTE = "03";
	public static final String ETAPA_CONFIRMADO = "04";
	public static final String REPORTE_CREADO = "Reporte creado correctamente.";
	public static final String ETAPA_CREADA = "Etapa creada correctamente.";
	public static final Boolean NO_SELECCIONADO = false;
	public static final String FP_PENDIENTE_DE_REPORTE = "0";
	public static final String FP_CON_REPORTE = "1";
	public static final String FP_CON_REPORTE_MULTIPLE_ESTAFETA = "2";
	public static final String FP_CON_REPORTE_MULTIPLE_CASILLERO = "3";
	public static final Integer RECIBIDO_FISICO = 3;
	public static final String OPERACION_ACTUALIZAR = "ACTUALIZAR";
	public static final String OPERACION_CREAR = "CREAR";
	public static final String DESPACHOS_CONGRESALES = "DESPACHOS CONGRESALES";
	public static final String CONGRESISTA = "CONG. ";
	public static final String TIPO_CASILLERO = "TIPO_CASILLERO";
	public static final Integer ID_PROVEIDO_MESA_DE_PARTES = new Integer (1);
	public static final String GRUPO_CENTRO_COSTO_DESPACHOS_CONGRESALES = "19";
	public static final Boolean REPORTE_PENDIENTE_ENVIO_MULTIPLE = false;
	public static final Boolean REPORTE_REALIZADO_ENVIO_MULTIPLE = true;
	public static final String TITULO_REPORTE_GENERADO = "REPORTE PARA ";
	public static final String TITULO_REPORTE_CONSULTA = "REPORTE DE CONSULTAS";
	public static final Integer ESTADO_REGISTRADO = 7;
	public static final Integer ESTADO_ENVIADO = 8;
	public static final Integer ESTADO_ANULADO = 11;
	public static final Boolean INDICADA_ENVIO_MULTIPLE = true;
	public static final Boolean NO_INDICADA_ENVIO_MULTIPLE = false;
	public static final Integer ESTADO_DOCUMENTO_ENVIADO = 8;
	public static final Integer ESTADO_LEIDO = 9;
	public static final Boolean SIN_REPORTE = false;
	public static final Boolean CON_REPORTE = true;
	public static final Integer UN_SOLO_REGISTRO_ELIMINADO = 1;
	public static final Integer NUEVO_REGISTRO = null;
	public static final String SIN_TIPO_REPORTE = null;
	public static final Date SIN_FECHA = null;
	public static final String SIN_USUARIO = null;
	public static final Boolean ES_RESPONSABLE = true;
	public static final Boolean ES_MESA_DE_PARTES = true;
	public static final Boolean NO_ES_MESA_DE_PARTES = false;
	public static final Boolean IND_CANAL_ESTAFETA = true;
	public static final Boolean IND_CANAL_CASILLERO = false;
	public static final String VACIO = "";
	public static final String PUNTO = ".";
	public static final String VINIETA_PUNTO = "•";
	public static final String ESPACIO_EN_BLANCO = " ";
	public static final String SEPARADOR_REPORTE = ".\n";
	public static final boolean ES_CONFIGURABLE = true;
	public static final boolean NO_ES_CONFIGURABLE = false;
	public static final String PALOTE = "|";
	public static final Integer ENVIADOS = 1;
	public static final Boolean RESPONSABLE = true;
	public static final String PUEDE_VISUALIZAR_NOTA = "1";
	public static final String NO_PUEDE_VISUALIZAR_NOTA = "0";
	public static final String PUEDE_EDITAR_NOTA = "1";
	public static final String NO_PUEDE_EDITAR_NOTA = "0";
	public static final String BANDEJA_DE_RECIBIDOS = "1";
	public static final String BANDEJA_DE_ENVIADOS = "2";
	public static final Object MENSAJE_OBJETO_INCORRECTO = "No se grabo los dirigidos externos. Por favor regrese a su bandeja de envios e intentelo nuevamente.";
	public static final String DOCUMENTO_ENVIADO = "1";
	public static final String DOCUMENTO_RECIBIDO = "2";
	public static final String DOCUMENTO_REGISTRADO_POR_MESA_DE_PARTES = "1";
	public static final String HORA_INICIAL_DIA_MILISEGUNDOS = " 00:00:00.000";
	public static final String HORA_FINAL_DIA_MILISEGUNDOS = " 23:59:59.999";
	public static final int CERO = 0;
	public static final Object NULO = null;
	public static final boolean VERDADERO = true;
	public static final boolean FALSO = false;
	public static final String CODIGO_SEPARADOR = "@@";
	public static final String FORMATO_FECHA_USUARIO_DE_MESA_PARTES = "dd-MM-yyyy HH:mm aa";
	
	public static final String GUION = "-";
	public static final String PENDIENTE_RECIBIR = "00";
	public static final String CON_FISICO_RECIBIDO = "0";
	public static final String SIN_FISICO_RECIBIDO = "1";
	public static final String A_MINUSCULA = "a";
	public static final String A_MINUSCULA_CON_TILDE = "á";
	public static final String COMODIN_A_MINUSCULA_CON_Y_SIN_TILDE = "[a-á]";
	public static final String E_MINUSCULA = "e";
	public static final String E_MINUSCULA_CON_TILDE = "é";
	public static final String COMODIN_E_MINUSCULA_CON_Y_SIN_TILDE = "[e-é]";
	public static final String I_MINUSCULA = "i";
	public static final String I_MINUSCULA_CON_TILDE = "í";
	public static final String COMODIN_I_MINUSCULA_CON_Y_SIN_TILDE = "[i-í]";
	public static final String O_MINUSCULA = "o";
	public static final String O_MINUSCULA_CON_TILDE = "ó";
	public static final String COMODIN_O_MINUSCULA_CON_Y_SIN_TILDE = "[o-ó]";
	public static final String U_MINUSCULA = "u";
	public static final String U_MINUSCULA_CON_TILDE = "ú";
	public static final String COMODIN_U_MINUSCULA_CON_Y_SIN_TILDE = "[u-ú]";
	public static final String A_MAYUSCULA = "A";
	public static final String A_MAYUSCULA_CON_TILDE = "Á";
	public static final String COMODIN_A_MAYUSCULA_CON_Y_SIN_TILDE = "[A-Á]";
	public static final String E_MAYUSCULA = "E";
	public static final String E_MAYUSCULA_CON_TILDE = "É";
	public static final String COMODIN_E_MAYUSCULA_CON_Y_SIN_TILDE = "[E-É]";
	public static final String I_MAYUSCULA = "I";
	public static final String I_MAYUSCULA_CON_TILDE = "Í";
	public static final String COMODIN_I_MAYUSCULA_CON_Y_SIN_TILDE = "[I-Í]";
	public static final String O_MAYUSCULA = "O";
	public static final String O_MAYUSCULA_CON_TILDE = "Ó";
	public static final String COMODIN_O_MAYUSCULA_CON_Y_SIN_TILDE = "[O-Ó]";
	public static final String U_MAYUSCULA = "U";
	public static final String U_MAYUSCULA_CON_TILDE = "Ú";
	public static final String COMODIN_U_MAYUSCULA_CON_Y_SIN_TILDE = "[U-Ú]";
	public static final String UBICACION_Y_NOMBRE_REPORTE_PARA_CASILLERO = "/assets/rptPendientesMesaPartesCasillero.jrxml";
	public static final String UBICACION_Y_NOMBRE_REPORTE_PARA_ESTAFETA = "/assets/rptPendientesMesaPartes.jrxml";
	public static final String PAGINADO_MESA_PARTES = "1000";
	public static final String CLASE_DEVIRA_UTIL = "DerivaUtil";
	public static final String ACTIVO_RECIBI_CONFORME = "01";
	public static final String INDICADOR_DIRIGIDO = "1";
	public static final String INDICADOR_DERIVADO = "0";
	public static final String INDICADOR_FISICO = "3";
	public static final CharSequence AM = "AM";
	public static final CharSequence PM = "PM";
	public static final Integer DOCUMENTO_RECIBIDO_FISICO = 3;
	public static final Integer ENVIADO_AL_RESPONSABLE = 1;
	public static final int ANIO_INICIO_MODULO_DE_MESA_PARTES = 2018;
	public static final int TOTAL_SEMANAS_EN_UN_ANIO = 53;
	public static final String TIENE_REPORTE_EMITIDO = "1";
	
	public static final String RECIBIDO_USUARIO = "1";
	public static final String RECIBIDO_DEPENDENCIA = "0";
	public static final Integer OBTENER_FECHA_DATE = 1;
	public static final Integer OBTENER_FECHA_STRING = 2;
	public static final Integer MENSAJE_DE_ERROR = 3;
	public static final String MENSAJE_DE_FECHA_VACIA = "La fecha esta vacia.";
	public static final String MENSAJE_DE_FORMATO_VACIO = "El formato de fecha esta vacio.";
	public static final String FORMATO_FECHA_PARA_REPORTE_MP = "dd-MM-yyyy HH:mm aa";

	public static final String LISTENER_PROTOCOL = "http://";
	public static final String LISTENER_PORT = ":8090";
	public static final String LISTENER_API = "/api/template/";
	public static final String OPEN_TEMPLATE_API = LISTENER_API + "open";
	public static final String UPLOAD_TEMPLATE_API = LISTENER_API + "upload";
	public static final int MAX_HEADER_TEMPLATE = 43;

	public static final int FIRMADO_DEPENDENCIA = 340;
	public static final int FIRMADO_EMPLEADO = 341;
	public static final int ANIO_ADMINISTRATIVO = 342;
	public static final int ANIO_LEGISLATIVO = 343;
	public static final Integer DOCUMENTO_PRINCIPAL = 344;
	public static final Integer DOCUMENTO_ANEXO = 345;
	public static final Integer DOCUMENTO_PROVEIDO = 356;
	public static final Integer DOCUMENTO_VISTOS_BUENOS = 361;
	public static final Integer ARCHIVO_WORD = 346;
	public static final Integer ARCHIVO_PDF = 347;
	public static final Integer ESTADO_FIRMA_REGISTRADO = 351;
	public static final Integer ESTADO_FIRMA_PARA_FIRMAR = 352;
	public static final Integer ESTADO_FIRMA_FIRMADO = 353;
	public static final boolean ARCHIVO_CON_FIRMA = true;
	public static final boolean ARCHIVO_SIN_FIRMA = false;
	public static final Integer ESTADO_PROVEIDO_FIRMADO = 359;
	public static final Integer ESTADO_VISTOS_FIRMADO = 355;
	public static final Integer TIPO_ADJUNTO_ARCHIVO = 3;
	public static final String MIME_TYPE_PDF = "application/pdf";
	public static final String MIME_TYPE_WORD = "application/vnd.ms-word";
	public static final String RUTA_ASSETS = "/assets/";
	public static final String RUTA_REPORTE_PROVEIDO_JRXML = "/assets/rptProveido.jrxml";

	public static final int CARGO_CONGRESISTA = 1;
	public static final int CARGO_DIRECTIVO_PORTAVOZ = 2;
	public static final int CARGO_DIRECTOR = 3;
	public static final int CARGO_JEFE = 4;
	public static final int CARGO_OFICIAL_MAYOR = 5;
	public static final int CARGO_PRESIDENTE = 6;
	public static final int CARGO_RESPONSABLE = 7;
	public static final int CARGO_VICEPRESIDENTE = 8;

	public static final int ENVIADO_EXTERNO_ENTIDAD = 142;
	public static final int ENVIADO_EXTERNO_PERSONA = 143;

	public static Map<String, String> SEMAFORO_REGISTRO = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("01", "BLANCO");
        aMap.put("02", "CELESTE");
        aMap.put("03", "AMARILLO");
        aMap.put("04", "VERDE");
        SEMAFORO_REGISTRO = Collections.unmodifiableMap(aMap);
    }
	public static Map<String, String> SEMAFORO_REGISTRO_ROJO = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("01", "ROJO");
        aMap.put("02", "ROJO");
        aMap.put("03", "ROJO");
        aMap.put("04", "VERDE");
        SEMAFORO_REGISTRO_ROJO = Collections.unmodifiableMap(aMap);
    }
	public static Map<String, String> GRUPO_ENVIO_MESA_PARTES = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("01", "PARA ESTAFETA");
        aMap.put("02", "PARA CASILLERO");
        aMap.put("03", "PARA DEPENDENCIA");
        aMap.put("04", "PARA TRAMITE INTERNO");
        GRUPO_ENVIO_MESA_PARTES = Collections.unmodifiableMap(aMap);
    }
	public static Map<String, String> ETAPA_ENVIO_MESA_PARTES = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("01", "Registrado");
        aMap.put("02", "En Bandeja de Envio");
        aMap.put("03", "En Reporte");
        aMap.put("04", "Entregado");
        ETAPA_ENVIO_MESA_PARTES = Collections.unmodifiableMap(aMap);
    }
	
	public static Map<String, String> ESTADO_REPORTE = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("0", "ENVIADO A");
        aMap.put("1", "CONFIRMADO POR");
        ESTADO_REPORTE = Collections.unmodifiableMap(aMap);
    }
	
	
	public static Map<String, String> GRUPO_REPORTES = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("1", "ESTAFETA");
        aMap.put("2", "CASILLERO");
        aMap.put("3", "TRAMITE INTERNO");
        aMap.put("4", "DEPENDENCIA DIRECTA");
        GRUPO_REPORTES = Collections.unmodifiableMap(aMap);
    }
	
	
	public static Map<String, String> COMISIONES = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("18", "COMISIONES ORDINARIAS");
        aMap.put("21", "COMISIONES ESPECIALES");
        aMap.put("22", "GRUPOS PARLAMENTARIOS");
        aMap.put("23", "COMISIONES OTRAS");
        COMISIONES = Collections.unmodifiableMap(aMap);
    }
	
	public static Map<String, String> CONGRESISTAS_PARLAMENTO_ANDINO = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("26", "CONGRESISTAS DEL PARLAMENTO ANDINO");
        COMISIONES = Collections.unmodifiableMap(aMap);
    }
	
	public static Map<String, String> ESTADO_RECIBIR_DOC_FISICO = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("00", "00");
        aMap.put("01", "01");
        ESTADO_RECIBIR_DOC_FISICO = Collections.unmodifiableMap(aMap);
    }
	
	public static Map<String, String> DES_ESTADO_RECIBIR_DOC_FISICO = new HashMap<String, String>();
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("00", "SIN CONFIRMAR");
        aMap.put("01", "RECIBIDO");
        DES_ESTADO_RECIBIR_DOC_FISICO = Collections.unmodifiableMap(aMap);
    }
	
	
}