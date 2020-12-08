package pe.gob.congreso.model.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.MpGestionEnvio;
import pe.gob.congreso.util.Constantes;

public class Util {

	public static Boolean indicadorCambioCampos(FichaProveido fp, FichaProveido fichaActual) {
		Boolean cambios = false;
		if ( fichaActual != null ) {
    		if ( fp.getNumero() == null) {
    			fp.setNumero(fichaActual.getNumero());
    		} else if ( fp.getNumero() != null && fichaActual.getNumero() != null && !fp.getNumero().equals(fichaActual.getNumero()) ) {
    			cambios = true;
    		}
    		if ( fp.getSumilla() == null ) {
    			fp.setSumilla(fichaActual.getSumilla());
    		} else if ( fp.getSumilla() != null && fichaActual.getSumilla() != null && !fp.getSumilla().equals(fichaActual.getSumilla()) ) {
    			cambios = true;
    		}
    		if ( fp.getPpinicio() != null ) {
    			fp.setPpinicio(fichaActual.getPpinicio());
    		} else if ( fp.getPpinicio() != null && fichaActual.getPpinicio() != null && !fp.getPpinicio().equals(fichaActual.getPpinicio()) ) {
    			cambios = true;
    		}
    		if ( fp.getPpfin() != null ) {
    			fp.setPpfin(fichaActual.getPpfin());
    		} else if ( fp.getPpfin() != null && fichaActual.getPpfin() != null && !fp.getPpfin().equals(fichaActual.getPpfin()) ) {
    			cambios = true;
    		}
    		if ( fp.getHabilitado() != null ) {
    			fp.setHabilitado(fichaActual.getHabilitado());
    		} else if ( fp.getHabilitado() != null && fichaActual.getHabilitado() != null && !fp.getHabilitado().equals(fichaActual.getHabilitado()) ) {
    			cambios = true;
    		}
    		if ( fp.getEstado() != null ) {
    			fp.setEstado(fichaActual.getEstado());
    		} else if ( fp.getEstado() != null && fichaActual.getEstado() != null && !fp.getEstado().equals(fichaActual.getEstado()) ) {
    			cambios = true;
    		}
    	}
		return cambios;
	}
	
	public static FichaProveido cambiarCamposNulosPorActuales(FichaProveido fp, FichaProveido fichaActual) {
		if ( fichaActual != null ) {
    		if ( fp.getNumero() == null && fichaActual.getNumero() != null) {
    			fp.setNumero(fichaActual.getNumero());
    		}
    		if ( fp.getSumilla() == null && fichaActual.getSumilla() != null) {
    			fp.setSumilla(fichaActual.getSumilla());
    		}
    		if ( fp.getPpinicio() == null && fichaActual.getPpinicio() != null) {
    			fp.setPpinicio(fichaActual.getPpinicio());
    		}
    		if ( fp.getPpfin() == null && fichaActual.getPpfin() != null) {
    			fp.setPpfin(fichaActual.getPpfin());
    		}
    		if ( fp.getHabilitado() == null && fichaActual.getHabilitado() != null) {
    			fp.setHabilitado(fichaActual.getHabilitado());
    		}
    		if ( fp.getEstado() == null && fichaActual.getEstado() != null) {
    			fp.setEstado(fichaActual.getEstado());
    		}
    		if ( fp.getUsuarioCrea() == null && fichaActual.getUsuarioCrea() != null) {
    			fp.setUsuarioCrea(fichaActual.getUsuarioCrea());
    		}
    		if ( fp.getFechaCrea() == null && fichaActual.getFechaCrea() != null) {
    			fp.setFechaCrea(fichaActual.getFechaCrea());
    		}
    		if ( fp.getUsuarioModifica() == null && fichaActual.getUsuarioModifica() != null) {
    			fp.setUsuarioModifica(fichaActual.getUsuarioModifica());
    		}
    		if ( fp.getFechaModifica() == null && fichaActual.getFechaModifica() != null) {
    			fp.setFechaModifica(fichaActual.getFechaModifica());
    		}
    	}
		return fp;
	}
	
	public static Integer[] convertIntegers(List<Integer> integers)
	{
	    Integer[] result = new Integer[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < result.length; i++) {
	        result[i] = iterator.next().intValue();
	    }
	    return result;
	}

	public static boolean contienePalabras(String loQueQuieroBuscar, String cadenaDondeBuscar) {
		if ( cadenaDondeBuscar == null ) {
			return false;
		}
		Boolean busFp = false;
		String[] palabras = loQueQuieroBuscar.split("\\s+");
		for (String palabra : palabras) {
		    if (cadenaDondeBuscar.contains(palabra)) {
		        busFp = true;
		    }
		}
		if ( !busFp ) {
			palabras = loQueQuieroBuscar.split("\\W+");
			for (String palabra : palabras) {
			    if (cadenaDondeBuscar.contains(palabra)) {
			        busFp = true;
			    }
			}
		}
		return busFp;
	}

	public static boolean indicadorCambioCampos(EnvioMultiple em, EnvioMultiple envioMultiple) {
		Boolean cambios = false;
		if ( envioMultiple != null ) {
    		if ( em.getFichaDocumentoId() == null) {
    			em.setFichaDocumentoId(envioMultiple.getFichaDocumentoId());
    		} else if ( em.getFichaDocumentoId() != null && envioMultiple.getFichaDocumentoId() != null && !em.getFichaDocumentoId().equals(envioMultiple.getFichaDocumentoId()) ) {
    			cambios = true;
    		}
    		if ( em.getProveidoId() == null) {
    			em.setProveidoId(envioMultiple.getProveidoId());
    		} else if ( em.getProveidoId() != null && envioMultiple.getProveidoId() != null && !em.getProveidoId().equals(envioMultiple.getProveidoId()) ) {
    			cambios = true;
    		}
    		if ( em.getCanalEnvio() == null) {
    			em.setCanalEnvio(envioMultiple.getCanalEnvio());
    		} else if ( em.getCanalEnvio() != null && envioMultiple.getCanalEnvio() != null && !em.getCanalEnvio().equals(envioMultiple.getCanalEnvio()) ) {
    			cambios = true;
    		}
    		if ( em.getMultiple() != null ) {
    			em.setMultiple(envioMultiple.getMultiple());
    		} else if ( em.getMultiple() != null && envioMultiple.getMultiple() != null && !em.getMultiple().equals(envioMultiple.getMultiple()) ) {
    			cambios = true;
    		}
    		if ( em.getTipoReporte() != null ) {
    			em.setTipoReporte(envioMultiple.getTipoReporte());
    		} else if ( em.getTipoReporte() != null && envioMultiple.getTipoReporte() != null && !em.getTipoReporte().equals(envioMultiple.getTipoReporte()) ) {
    			cambios = true;
    		}
    		if ( em.getCentroCostoId() != null ) {
    			em.setCentroCostoId(envioMultiple.getCentroCostoId());
    		} else if ( em.getCentroCostoId() != null && envioMultiple.getCentroCostoId() != null && !em.getCentroCostoId().equals(envioMultiple.getCentroCostoId()) ) {
    			cambios = true;
    		}
    		if ( em.getReporte() != null ) {
    			em.setReporte(envioMultiple.getReporte());
    		} else if ( em.getReporte() != null && envioMultiple.getReporte() != null && !em.getReporte().equals(envioMultiple.getReporte()) ) {
    			cambios = true;
    		}
    		if ( em.getEstado() != null ) {
    			em.setEstado(envioMultiple.getEstado());
    		} else if ( em.getEstado() != null && envioMultiple.getEstado() != null && !em.getEstado().equals(envioMultiple.getEstado()) ) {
    			cambios = true;
    		}
    		if ( em.getHabilitado() != null ) {
    			em.setHabilitado(envioMultiple.getHabilitado());
    		} else if ( em.getHabilitado() != null && envioMultiple.getHabilitado() != null && !em.getHabilitado().equals(envioMultiple.getHabilitado()) ) {
    			cambios = true;
    		}
    		if ( em.getSubsanado() != null ) {
    			em.setSubsanado(envioMultiple.getSubsanado());
    		} else if ( em.getSubsanado() != null && envioMultiple.getSubsanado() != null && !em.getSubsanado().equals(envioMultiple.getSubsanado()) ) {
    			cambios = true;
    		}
    	}
		return cambios;
	}

	public static EnvioMultiple cambiarCamposNulosPorActuales(EnvioMultiple em, EnvioMultiple envioMultiple) {
		if ( envioMultiple != null ) {
    		if ( em.getFichaDocumentoId() == null && envioMultiple.getFichaDocumentoId() != null) {
    			em.setFichaDocumentoId(envioMultiple.getFichaDocumentoId());
    		}
    		if ( em.getProveidoId() == null && envioMultiple.getProveidoId() != null) {
    			em.setProveidoId(envioMultiple.getProveidoId());
    		}
    		if ( em.getCanalEnvio() == null && envioMultiple.getCanalEnvio() != null) {
    			em.setCanalEnvio(envioMultiple.getCanalEnvio());
    		}
    		if ( em.getMultiple() == null && envioMultiple.getMultiple() != null) {
    			em.setMultiple(envioMultiple.getMultiple());
    		}
    		if ( em.getTipoReporte() == null && envioMultiple.getTipoReporte() != null) {
    			em.setTipoReporte(envioMultiple.getTipoReporte());
    		}
    		if ( em.getCentroCostoId() == null && envioMultiple.getCentroCostoId() != null) {
    			em.setCentroCostoId(envioMultiple.getCentroCostoId());
    		}
    		if ( em.getReporte() == null && envioMultiple.getReporte() != null) {
    			em.setReporte(envioMultiple.getReporte());
    		}
    		if ( em.getEstado() == null && envioMultiple.getEstado() != null) {
    			em.setEstado(envioMultiple.getEstado());
    		}
    		if ( em.getHabilitado() == null && envioMultiple.getHabilitado() != null) {
    			em.setHabilitado(envioMultiple.getHabilitado());
    		}
    		if ( em.getSubsanado() == null && envioMultiple.getSubsanado() != null) {
    			em.setSubsanado(envioMultiple.getSubsanado());
    		}
    	}
		return em;
	}
	
	public static EnvioMultiple buscarEnviosAnterioresXCentroCosto(List<EnvioMultiple> listEm, EnvioMultiple envioMultiple) {
		for (EnvioMultiple em : listEm) {
			if ( em.getCentroCostoId().trim().equals(envioMultiple.getCentroCostoId().trim()) ) {
				return em;
			}
		}
		return null;
	}

	public static EnvioMultiple validarIdEnvioMultiple(EnvioMultiple[] enviosMultiples) {
	    EnvioMultiple primerEnvio = null;
		if( enviosMultiples != null && enviosMultiples.length >0 ) {
    		primerEnvio = enviosMultiples[0];
    		if ( primerEnvio.getFichaDocumentoId() != null && primerEnvio.getProveidoId() != null ) {
    			return primerEnvio;
	    	}
		}
	    return primerEnvio;
	}
	
	public static MpGestionEnvio compararGestionEnvio(MpGestionEnvio actual, MpGestionEnvio gestionEnvio) {
		if ( gestionEnvio != null ) {
    		if ( gestionEnvio.getUsuarioGeneracion() == null && actual.getUsuarioGeneracion() != null) {
    			gestionEnvio.setUsuarioGeneracion(actual.getUsuarioGeneracion());
    		}
    		if ( gestionEnvio.getSubsanado() == null && actual.getSubsanado() != null) {
    			gestionEnvio.setSubsanado(actual.getSubsanado());
    		}
    		if ( gestionEnvio.getCantidadRegistros() == null && actual.getCantidadRegistros() != null) {
    			gestionEnvio.setCantidadRegistros(actual.getCantidadRegistros());
    		}
    		if ( gestionEnvio.getCantidadFisicos() == null && actual.getCantidadFisicos() != null) {
    			gestionEnvio.setCantidadFisicos(actual.getCantidadFisicos());
    		}
    		if ( gestionEnvio.getTipoReporte() == null && actual.getTipoReporte() != null) {
    			gestionEnvio.setTipoReporte(actual.getTipoReporte());
    		}
    		if ( gestionEnvio.getGrupoEnvio() == null && actual.getGrupoEnvio() != null) {
    			gestionEnvio.setGrupoEnvio(actual.getGrupoEnvio());
    		}
    		if ( gestionEnvio.getEstado() == null && actual.getEstado() != null) {
    			gestionEnvio.setEstado(actual.getEstado());
    		}
    		if ( gestionEnvio.getHabilitado() == null && actual.getHabilitado() != null) {
    			gestionEnvio.setHabilitado(actual.getHabilitado());
    		}
    		if ( gestionEnvio.getObservaciones() == null && actual.getObservaciones() != null) {
    			gestionEnvio.setObservaciones(actual.getObservaciones());
    		}
    		if ( gestionEnvio.getUsuarioCrea() == null && actual.getUsuarioCrea() != null) {
    			gestionEnvio.setUsuarioCrea(actual.getUsuarioCrea());
    		}
    		if ( gestionEnvio.getUsuarioModifica() == null && actual.getUsuarioModifica() != null) {
    			gestionEnvio.setUsuarioModifica(actual.getUsuarioModifica());
    		}
    	}
		return gestionEnvio;
		
	}
	
	public static String replaceAllTildes(String frase){
		if ( frase != null && !frase.isEmpty() ) {
			frase = replaceAllPorComodinConTildes(frase, Constantes.A_MINUSCULA, Constantes.A_MINUSCULA_CON_TILDE, Constantes.COMODIN_A_MINUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.E_MINUSCULA, Constantes.E_MINUSCULA_CON_TILDE, Constantes.COMODIN_E_MINUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.I_MINUSCULA, Constantes.I_MINUSCULA_CON_TILDE, Constantes.COMODIN_I_MINUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.O_MINUSCULA, Constantes.O_MINUSCULA_CON_TILDE, Constantes.COMODIN_O_MINUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.U_MINUSCULA, Constantes.U_MINUSCULA_CON_TILDE, Constantes.COMODIN_U_MINUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.A_MAYUSCULA, Constantes.A_MAYUSCULA_CON_TILDE, Constantes.COMODIN_A_MAYUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.E_MAYUSCULA, Constantes.E_MAYUSCULA_CON_TILDE, Constantes.COMODIN_E_MAYUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.I_MAYUSCULA, Constantes.I_MAYUSCULA_CON_TILDE, Constantes.COMODIN_I_MAYUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.O_MAYUSCULA, Constantes.O_MAYUSCULA_CON_TILDE, Constantes.COMODIN_O_MAYUSCULA_CON_Y_SIN_TILDE);
			frase = replaceAllPorComodinConTildes(frase, Constantes.U_MAYUSCULA, Constantes.U_MAYUSCULA_CON_TILDE, Constantes.COMODIN_U_MAYUSCULA_CON_Y_SIN_TILDE);
		}
		return frase;
		
	}
	
	public static String replaceAllPorComodinConTildes(String frase, String vocal, String vocalConTilde, String comodin){		
		comodin = comodin.replaceAll("\\-"+vocalConTilde+"\\]", "\\-"+vocal+"\\]");
		frase = frase.replaceAll(vocal, comodin);
		frase = frase.replaceAll(vocalConTilde, comodin);
		frase = frase.replaceAll("\\-"+vocal+"\\]", "\\-"+vocalConTilde+"\\]");
		return frase;
		
	}
	
	public static Map deStringADate(String dateInString, SimpleDateFormat formatter) {        
        Map map = new HashMap<>();
        if ( dateInString == null || dateInString.isEmpty() ) {
        	map.put(3,Constantes.MENSAJE_DE_FECHA_VACIA);
        }
        if ( formatter == null ) {
        	map.put(3,Constantes.MENSAJE_DE_FORMATO_VACIO);
        }
        try {
            Date date = formatter.parse(dateInString);
            map.put(1,date);
            map.put(2,formatter.format(date));
        } catch (Exception e) {
            map.put(3,e.getMessage());
        }
        return map;

    }
	
	public static String deDateAString(Date date, String formatter) {
		return new SimpleDateFormat(formatter).format(date);

    }

}
