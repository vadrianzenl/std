package pe.gob.congreso.controllers;

public class Urls {

    /* Se usan Urls no rest como /disable */
    public static final String ROOT = "/";
    public static final String API = "/api";
    public static final String VERSION = "/version";

    static class View {

        public static final String SIGNIN = "/acceso";
        public static final String MAIN = "/std";
        public static final String VIEWER_PDF = "/viewer_pdf";
    }

    static class Auth {

        public static final String BASE = API + "/auth";
        public static final String SIGNIN = "/signin";
        public static final String SIGNOUT = "/signout";
        public static final String SESSIONS = "/sesiones";
    }

    static class Usuario {

        public static final String BASE = API + "/usuarios";
        public static final String ME = "/yo";
        public static final String MODULES = "/modules";
        public static final String MENUS = "/menus";
    }

    static class Kpis {

        public static final String BASE = API + "/kpis";
    }

    static class Actividad {

        public static final String BASE = API + "/actividades";
    }

    static class Tipo {

        public static final String BASE = API + "/tipos";
    }

    static class Empleado {

        public static final String BASE = API + "/empleados";
    }

    static class CentroCosto {

        public static final String BASE = API + "/centrosCosto";
    }

    static class CentroCostoAdicional {

        public static final String BASE = API + "/centrosCostoAdicional";
    }

    static class ModalidadContrato {

        public static final String BASE = API + "/modalidades";
    }

    static class GrupoCentroCosto {

        public static final String BASE = API + "/gruposCentroCostos";
    }

    static class FichaDocumento {

        public static final String BASE = API + "/documentos";
    }

    static class Motivo {

        public static final String BASE = API + "/motivos";
    }

    static class Proveido {

        public static final String BASE = API + "/proveidos";
    }

    static class Grupo {

        public static final String BASE = API + "/grupos";
    }

    static class SubCategoria {

        public static final String BASE = API + "/subcategorias";
    }

    static class Categoria {

        public static final String BASE = API + "/categorias";
    }

    static class AnioLegislativo {

        public static final String BASE = API + "/anioLegislativos";
    }

    /* AEP - 31.05.2019 */
    static class ControlFirma {
        public static final String BASE = API + "/controlFirmas";
    }

    static class PeriodoLegislativo {

        public static final String BASE = API + "/periodoLegislativos";
    }

    static class Legislatura {

        public static final String BASE = API + "/legislaturas";
    }

    static class ProyectoLey {

        public static final String BASE = API + "/proyectoLeyes";
    }

    static class Adjunto {

        public static final String BASE = API + "/adjuntos";
    }

    static class TipoDocumento {

        public static final String BASE = API + "/tiposDocumento";
    }

    static class EstadoDeriva {

        public static final String BASE = API + "/estadosDeriva";
    }

    static class Deriva {

        public static final String BASE = API + "/derivados";
    }

    static class Relaciona {

        public static final String BASE = API + "/relacionados";
    }

    static class Persona {

        public static final String BASE = API + "/personas";
    }

    static class Estado {

        public static final String BASE = API + "/estados";
    }

    static class CentroCostoActual {

        public static final String BASE = API + "/centroCostosActuales";
    }

    static class Bitacora {

        public static final String BASE = API + "/bitacoras";
    }

    static class EnviadoExterno {

        public static final String BASE = API + "/enviadoExterno";
    }

    static class Ubigeo {

        public static final String BASE = API + "/ubigeos";
    }

    static class Responsable {

        public static final String BASE = API + "/responsables";
    }

    static class SistemaOpcionUsuario {

        public static final String BASE = API + "/sistemaOpcionUsuario";
    }

    static class Notificacion {

        public static final String BASE = API + "/notificaciones";
    }

    // MP - inicio
    static class GestionEnvio {

        public static final String BASE = API + "/gestionEnvio";
    }

    static class GestionCargo {

        public static final String BASE = API + "/gestionCargo";
    }

    static class GestionConsulta {

        public static final String BASE = API + "/gestionConsulta";
    }

    static class FichaProveido {

        public static final String BASE = API + "/fichaProveido";
    }

    static class EnvioMultiple {

        public static final String BASE = API + "/envioMultiple";
    }

    // MP - fin
    // Notas - Inicio
    static class Notas {

        public static final String BASE = API + "/notas";
    }
    // Notas - Fin

    static class Dependencias {

        public static final String BASE = API + "/dependencias";
    }

    static class Fisico {

        public static final String BASE = API + "/fisico";
    }

    static class EntidadesExternas {

        public static final String BASE = API + "/entidadesExternas";
    }

    static class FiltroAnio {

        public static final String BASE = API + "/filtroAnio";
    }

    static class Template {

        public static final String BASE = API + "/template";
    }

}
