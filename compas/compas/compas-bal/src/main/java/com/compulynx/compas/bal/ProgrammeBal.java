/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;


/**
 * @author Anita
 *
 */
public interface ProgrammeBal {
	CompasResponse UpdateProgramme(Programme programme);

	List<Programme> GetAllProgrammes();
	
	
}
