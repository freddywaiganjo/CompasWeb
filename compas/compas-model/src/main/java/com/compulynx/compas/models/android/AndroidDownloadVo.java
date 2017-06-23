/**
 * 
 */
package com.compulynx.compas.models.android;

import java.util.List;

import com.compulynx.compas.models.Category;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.User;

/**
 * @author Anita
 *
 */
public class AndroidDownloadVo {

	public List<AndroidUsers> users;
	public AndroidDownloadVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<AndroidCategories> categories;
	public List<AndroidProducts> products;
	public List<AndroidProgrammes> programmes;
	public List<AndroidTopup> topupDetails;
	public List<AndroidBnfgrp> bnfGrps;
	public List<Customer> beneficiaries;
	public int respCode;
	public List<ColorPin> colors;
}
