package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.DashBoard;

public interface DashBoardBal {
	 List<DashBoard> GetDashBoardCountDetail();
	 List<DashBoard> GetTransChartDetail();
	 List<DashBoard> GetDashBoardAmountDetail() ;
	 List<DashBoard> GetFlowChartCountDetail();
	 List<DashBoard> GetDashBoardCollectionDetail();
	 List<DashBoard> GetDashBoardAgentDetail();
	 List<DashBoard> GetPenNotifications();
}
