/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author shree
 *
 */
public interface ProgrammeDal {
	boolean checkProgrammeName(String programmeDesc);

	CompasResponse UpdateProgramme(Programme programme);

	List<Programme> GetAllProgrammes();

	Programme GetProgrammeById(int programmeId);


}
