package com.intalio.cloud.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.intalio.cloud.android.common.Constants;
import com.intalio.cloud.android.view.R;

public class CustomExpandableListAdapter extends SimpleExpandableListAdapter {
	
	private final Context context;
	private final int groupLayout;
	private final int childLayout;
	private final LayoutInflater inflater;
	private final List<HashMap<String, String>> groupData;
	private final List<ArrayList<HashMap<String, String>>> childData;
	private final String[] groupFrom;
	private final String[] childFrom;
	private final int[] groupTo;
	private final int[] childTo;
	
	private static class GroupViewHolder {
		public ImageView groupImage;
		public TextView groupTitle;
	}
	
	private static class ChildViewHolder {
		public ImageView objectImage;
		public TextView objectTitle;
	}

	public CustomExpandableListAdapter(Context context,
			List<HashMap<String, String>> groupData, 
			int groupLayout,
			String[] groupFrom, 
			int[] groupTo,
			List<ArrayList<HashMap<String, String>>> childData,
			int childLayout, 
			String[] childFrom, 
			int[] childTo) {
		
		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
				childLayout, childFrom, childTo);
		
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.groupLayout = groupLayout;
		this.childLayout = childLayout;
		this.groupData = groupData;
		this.childData = childData;
		this.groupFrom = groupFrom;
		this.childFrom = childFrom;
		this.groupTo = groupTo;
		this.childTo = childTo;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if (convertView == null) {
        	convertView = inflater.inflate(groupLayout, parent, false);			
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.groupImage = (ImageView) convertView.findViewById(R.id.icon);
			groupViewHolder.groupTitle = (TextView) convertView.findViewById(R.id.groupname);
			convertView.setTag(groupViewHolder);
        } else {
        	groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        String groupName = groupData.get(groupPosition).get(Constants.OBJECT_GROUP);
		bindGroupView(groupViewHolder, groupName);
        return convertView;
    }
	
	private void bindGroupView(GroupViewHolder groupViewHolder, String groupName){
		if (groupName.equals(Constants.OBJECT_GROUP_ACTIVITIES)) {
			groupViewHolder.groupImage.setImageDrawable(context.getResources().getDrawable(R.drawable.activities));
		} else if (groupName.equals(Constants.OBJECT_GROUP_PROCESSES)) {
			groupViewHolder.groupImage.setImageDrawable(context.getResources().getDrawable(R.drawable.processes));
		} else if (groupName.equals(Constants.OBJECT_GROUP_CONTENTS)) {
			groupViewHolder.groupImage.setImageDrawable(context.getResources().getDrawable(R.drawable.contents));
		} else if (groupName.equals(Constants.OBJECT_GROUP_MARKETING)) {
			groupViewHolder.groupImage.setImageDrawable(context.getResources().getDrawable(R.drawable.marketing));
		} else if (groupName.equals(Constants.OBJECT_GROUP_SALES)) {
			groupViewHolder.groupImage.setImageDrawable(context.getResources().getDrawable(R.drawable.sales));
		}  else if (groupName.equals(Constants.OBJECT_GROUP_SUPPORT)) {
			groupViewHolder.groupImage.setImageDrawable(context.getResources().getDrawable(R.drawable.support));
		}  else if (groupName.equals(Constants.OBJECT_GROUP_PERFORMANCE)) {
			groupViewHolder.groupImage.setImageDrawable(context.getResources().getDrawable(R.drawable.performance));		
		}	
		groupViewHolder.groupTitle.setText(groupName);
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder;
        if (convertView == null) {
        	convertView = inflater.inflate(childLayout, parent, false);			
        	childViewHolder = new ChildViewHolder();
        	childViewHolder.objectImage = (ImageView) convertView.findViewById(R.id.childimage);
        	childViewHolder.objectTitle = (TextView) convertView.findViewById(R.id.childname);
			convertView.setTag(childViewHolder);
        } else {
        	childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        String objectName = childData.get(groupPosition).get(childPosition).get(Constants.OBJECT);
        bindChildView(childViewHolder, objectName);
        return convertView;
    }
	
	private void bindChildView(ChildViewHolder childViewHolder, String objectName){
		if (objectName.equals(Constants.OBJECT_EVENTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.events));
		} else if (objectName.equals(Constants.OBJECT_TASKS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.tasks));
		} else if (objectName.equals(Constants.OBJECT_PROJECTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.projects));
		} else if (objectName.equals(Constants.OBJECT_BUSINESS_PROCESSES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.business_processes));
		} else if (objectName.equals(Constants.OBJECT_PROCESS_INSTANCE)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.process_instances));
		} else if (objectName.equals(Constants.OBJECT_FOLDERS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.folders));
		} else if (objectName.equals(Constants.OBJECT_DOCUMENTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.documents));
		} else if (objectName.equals(Constants.OBJECT_MAPS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.maps));
		} else if (objectName.equals(Constants.OBJECT_TOPICS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.topics));
		} else if (objectName.equals(Constants.OBJECT_LINKS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.links));
		} else if (objectName.equals(Constants.OBJECT_PAGES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.pages));
		} else if (objectName.equals(Constants.OBJECT_LAYOUTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.layouts));
		} else if (objectName.equals(Constants.OBJECT_FILTERS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.filters));
		} else if (objectName.equals(Constants.OBJECT_PARTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.parts));
		} else if (objectName.equals(Constants.OBJECT_SNIPPETS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.snippets));
		} else if (objectName.equals(Constants.OBJECT_ELEMENTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.elements));
		} else if (objectName.equals(Constants.OBJECT_MARKETING_LISTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.marketing_lists));
		} else if (objectName.equals(Constants.OBJECT_MARKETING_CAMPAIGNS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.marketing_campaigns));
		} else if (objectName.equals(Constants.OBJECT_LEADS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.leads));
		} else if (objectName.equals(Constants.OBJECT_OPPORTUNITIES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.opportunities));
		} else if (objectName.equals(Constants.OBJECT_OPPORTUNITY_ITEMS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.opportunity_items));
		} else if (objectName.equals(Constants.OBJECT_ACCOUNTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.accounts));
		} else if (objectName.equals(Constants.OBJECT_CONTACTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.contacts));
		} else if (objectName.equals(Constants.OBJECT_PRODUCTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.products));
		} else if (objectName.equals(Constants.OBJECT_CHARECTERISTICS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.charecteristics));
		} else if (objectName.equals(Constants.OBJECT_PRICE_LISTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.price_lists));
		} else if (objectName.equals(Constants.OBJECT_QUOTES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.quotes));
		} else if (objectName.equals(Constants.OBJECT_QUOTE_ITEMS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.quote_items));
		} else if (objectName.equals(Constants.OBJECT_CONTRACTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.contracts));
		} else if (objectName.equals(Constants.OBJECT_CONTRACT_ITEMS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.contract_items));
		} else if (objectName.equals(Constants.OBJECT_INVOICES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.invoices));
		} else if (objectName.equals(Constants.OBJECT_INVOICE_ITEMS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.invoice_items));
		} else if (objectName.equals(Constants.OBJECT_ASSETS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.assets));
		} else if (objectName.equals(Constants.OBJECT_CASES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.cases));
		} else if (objectName.equals(Constants.OBJECT_SOLUTIONS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.solutions));
		} else if (objectName.equals(Constants.OBJECT_REPORTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.reports));
		} else if (objectName.equals(Constants.OBJECT_CHARTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.charts));
		} else if (objectName.equals(Constants.OBJECT_CHART_TYPES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.chart_types));
		} else if (objectName.equals(Constants.OBJECT_TRACKINGS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.trackings));
		} else if (objectName.equals(Constants.OBJECT_TRACKING_VALUE)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.tracking_values));
		} else if (objectName.equals(Constants.OBJECT_TRACKING_FIELDS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.tracking_fields));
		} else if (objectName.equals(Constants.OBJECT_TRACKING_GROUPS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.tracking_groups));
		} else if (objectName.equals(Constants.OBJECT_TRACKING_POINTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.tracking_points));
		} else if (objectName.equals(Constants.OBJECT_TRACKING_SCHEDULER)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.tracking_schedulers));
		} else if (objectName.equals(Constants.OBJECT_TIMING_INTERVALS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.timing_intervals));
		} else if (objectName.equals(Constants.OBJECT_KEY_PERFORMANCE_INDICATORS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.key_performance_indicators));
		} else if (objectName.equals(Constants.OBJECT_SERVICE_LEVEL_AGREEMENTS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.service_level_agreements));
		} else if (objectName.equals(Constants.OBJECT_SIMULATION_DISTRIBUTIONS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.simulation_distributions));
		} else if (objectName.equals(Constants.OBJECT_SIMULATION_SAMPLES)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.simulation_samples));
		} else if (objectName.equals(Constants.OBJECT_SIMULATION_SCENARIOS)) {
			childViewHolder.objectImage.setImageDrawable(context.getResources().getDrawable(R.drawable.simulation_scenarios));
		}
		
        childViewHolder.objectTitle.setText(objectName);
	}
	
	
	
}
