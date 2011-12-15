package com.intalio.cloud.android.model;


/**
 * This class defines the cloud objects with their names
 * also it defines the group names which holds the cloud
 * objects.
 */
public class CloudObjects {
	
	public String[] getCloudObjectGroups(){
		String[] cloudObjectGroups = {
				  "Activities",
				  "Processes",
				  "Contents",
				  "Marketing",
				  "Sales",
				  "Support",
				  "Performance"
				};
		return cloudObjectGroups;
	}
	
	public String[][] getCloudObjects() {
		String[][] cloudObjects = {
			//Activities
				  {
					"Events",
					"Tasks",
					"Projects"
				  },
			//Processes
				  {
					"Business Process",
					"Process Instance"
				  },
			//Contents
				  {
					"Folders",
					"Documents",
					"Maps",
					"Topics",
					"Links",
					"Pages",
					"Layouts",
					"Filters",
					"Parts",
					"Snippets",
					"Elements"		
				  },
			//Marketing
				  {
					"Marketing Lists",
					"Marketing Campaigns"
				  },
			//Sales
				  {
					  "Leads",
					  "Opportunities",
					  "Opportunity Items",
					  "Accounts",
					  "Contacts",
					  "Products",
					  "Charecteristics",
					  "Price Lists",
					  "Quotes",
					  "Quote Items",
					  "Contracts",
					  "Contract Items",
					  "Invoices",
					  "Invoice Items"
				  },
			//Support
				  {
					  "Assets",
					  "Cases",
					  "Solutions"
				  },
			//Performance
				  {
					  "Reports",
					  "Charts",
					  "Chart Types",
					  "Trackings",
					  "Tracking Value",
					  "Tracking Fields",
					  "Tracking Groups",
					  "Tracking Points",
					  "Tracking Scheduler",
					  "Timing Intervals",
					  "Key Performance Indicators",
					  "Service Level Agreements",
					  "Simulation Distributions",
					  "Simulation Samples",
					  "Simulation Scenarios"
				  }
			    };
		return cloudObjects;
	}

}
