package com.intalio.cloud.android.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.intalio.cloud.android.common.Constants;
import com.intalio.cloud.android.model.Records;
import com.intalio.cloud.android.view.R;
import com.intalio.cloud.android.widget.LoadingViewAdapter;

/**
 * It manages the contents and view of Loading list.
 * 
 */
public class CustomAdapter extends LoadingViewAdapter {

	private List<String> cloudObjects;
	private int layout;
	private LayoutInflater inflater;
	private AsyncTask<Object, Void, Pair<Boolean, List<String>>> backgroundTask;
	private String objectName;
	private String primaryObjectName;
	private String xid;
	private String[] colors = new String[] { "#DFDFDF", "#F0F0F0" };
	private ListFilter listFilter;
	private List<String> cloudObjectsOriginalValues;
	private final Object mLock = new Object();

	private static class ViewHolder {
		public TextView objectTitle;
	}

	private static class ViewHolderForContacts {
		public TextView contactName;
		public TextView mobile;
		public TextView email;
	}

	public CustomAdapter(Context context, int layout, String objectName) {
		this.inflater = LayoutInflater.from(context);
		this.layout = layout;
		this.objectName = objectName;
		cloudObjectsOriginalValues = getRows(1, objectName).second;
		cloudObjects = getRows(1, objectName).second;
		if (cloudObjects == null) {
			Log.d("Custom Adapter", "cloudObjects is null");
			// hasMorePagesListener.noMorePages(); //Logic for disable loading.
		} else if (cloudObjects.size() == 0) {
			Log.d("Custom Adapter", "cloudObjects size is 0");
			// hasMorePagesListener.noMorePages(); //Logic for disable loading.
		}
	}

	public CustomAdapter(Context context, int layout, String relatedObject,
			String primaryObjectName, String xid) {
		this.inflater = LayoutInflater.from(context);
		this.layout = layout;
		this.objectName = relatedObject;
		this.primaryObjectName = primaryObjectName;
		this.xid = xid;
		cloudObjectsOriginalValues = getRows(1, objectName, primaryObjectName, xid).second;
		cloudObjects = getRows(1, objectName, primaryObjectName, xid).second;
		if (cloudObjects == null) {
			notifyNoMorePages();
		} else if (cloudObjects.size() == 0) {
			notifyNoMorePages();
		}
	}

	@Override
	public int getCount() {
		return cloudObjects.size();
	}

	@Override
	public String getItem(int position) {
		return cloudObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) {
	}

	@Override
	public void configurePinnedHeader(View arg0, int arg1, int arg2) {
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
		if (primaryObjectName != null
				&& objectName.equals(Constants.OBJECT_CONTACTS)) {
			ViewHolderForContacts viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.related_object_list_composer, parent, false);
				viewHolder = new ViewHolderForContacts();
				viewHolder.contactName = (TextView) convertView
						.findViewById(R.id.contactName);
				viewHolder.mobile = (TextView) convertView
						.findViewById(R.id.mobile);
				viewHolder.email = (TextView) convertView
						.findViewById(R.id.email);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderForContacts) convertView.getTag();
			}
			setAlternateBackgroundColor(position, convertView);
			String[] recordOfContact = cloudObjects.get(position).split(
					Constants.DELIMITER);
			viewHolder.contactName.setText(recordOfContact[0]);
			viewHolder.mobile.setText(recordOfContact[1]);
			viewHolder.email.setText(recordOfContact[2]);
		} else {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(layout, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.objectTitle = (TextView) convertView
						.findViewById(R.id.itemName);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (primaryObjectName != null) {
				setAlternateBackgroundColor(position, convertView);
			}
			String[] recordNameWithXid = cloudObjects.get(position).split(
					Constants.DELIMITER);
			Log.d("CustomAdapter", "position is: " + position);
			String recordName = recordNameWithXid[1];
			Log.d("CustomAdapter", "recordName is: " + recordName);
			viewHolder.objectTitle.setText(recordName);
		}
		return convertView;
	}

	private void setAlternateBackgroundColor(int position, View convertView) {
		int colorPos = position % colors.length;
		convertView.setBackgroundColor(Color.parseColor(colors[colorPos]));
	}

	@Override
	public int getPositionForSection(int section) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	protected void onNextPageRequested(int page) {
		Log.d("CustomAdapter", "Got onNextPageRequested page=" + page);

		if (backgroundTask != null) {
			backgroundTask.cancel(false);
		}

		if (primaryObjectName != null) {
			backgroundTask = new AsyncTask<Object, Void, Pair<Boolean, List<String>>>() {
				@Override
				protected Pair<Boolean, List<String>> doInBackground(
						Object... params) {
					int page = (Integer) params[0];
					String objectName = (String) params[1];
					String primaryObjectName = (String) params[2];
					String xid = (String) params[3];
					return getRows(page, objectName, primaryObjectName, xid);
				}

				@Override
				protected void onPostExecute(Pair<Boolean, List<String>> result) {
					if (isCancelled())
						return;
					cloudObjectsOriginalValues.addAll(result.second);
					cloudObjects.addAll(result.second);
					nextPage();
					notifyDataSetChanged();

					if (result.first) {
						// still have more pages
						notifyMayHaveMorePages();
					} else {
						notifyNoMorePages();
					}
				};
			}.execute(page, objectName, primaryObjectName, xid);
		} else {
			backgroundTask = new AsyncTask<Object, Void, Pair<Boolean, List<String>>>() {
				@Override
				protected Pair<Boolean, List<String>> doInBackground(
						Object... params) {
					int page = (Integer) params[0];
					String objectName = (String) params[1];
					return getRows(page, objectName);
				}

				@Override
				protected void onPostExecute(Pair<Boolean, List<String>> result) {
					if (isCancelled())
						return;
					cloudObjectsOriginalValues.addAll(result.second);
					cloudObjects.addAll(result.second);
					nextPage();
					notifyDataSetChanged();

					if (result.first) {
						// still have more pages
						notifyMayHaveMorePages();
					} else {
						notifyNoMorePages();
					}
				};
			}.execute(page, objectName);
		}

	}

	// TODO: Combine both getRows method.
	// TODO: Now we are fetching data based only on limit. we can add offset
	// also.
	public Pair<Boolean, List<String>> getRows(int page, String objectName) {
		Log.d("CustomAdaptor", "Page is: " + page);
		List<String> flattenedData = getRecords(objectName, page);
		if (flattenedData != null) {
			if (page == 1) {
				return new Pair<Boolean, List<String>>(true, flattenedData);
			} else if ((page - 1) * Constants.RECORD_LIMIT < flattenedData
					.size()) {
				SystemClock.sleep(2000); // simulate loading
				Log.d("CustomAdaptor",
						"Size of data is: " + flattenedData.size());
				return new Pair<Boolean, List<String>>(
						page * Constants.RECORD_LIMIT <= flattenedData.size(),
						flattenedData.subList((page - 1)
								* Constants.RECORD_LIMIT, Math.min(page
								* Constants.RECORD_LIMIT, flattenedData.size())));
			}
		}
		return new Pair<Boolean, List<String>>(false, new ArrayList<String>());
	}

	private Pair<Boolean, List<String>> getRows(int page, String objectName,
			String primaryObjectName, String xid) {
		Log.d("CustomAdaptor", "Page is: " + page);
		List<String> flattenedData = getRecords(objectName, primaryObjectName,
				xid, page);
		if (flattenedData != null) {
			if (page == 1) {
				return new Pair<Boolean, List<String>>(true, flattenedData);
			} else if ((page - 1) * Constants.RECORD_LIMIT < flattenedData
					.size()) {
				SystemClock.sleep(2000); // simulate loading
				Log.d("CustomAdaptor",
						"Size of data is: " + flattenedData.size());
				return new Pair<Boolean, List<String>>(
						page * Constants.RECORD_LIMIT <= flattenedData.size(),
						flattenedData.subList((page - 1)
								* Constants.RECORD_LIMIT, Math.min(page
								* Constants.RECORD_LIMIT, flattenedData.size())));
			}
		}
		return new Pair<Boolean, List<String>>(false, new ArrayList<String>());
	}

	private List<String> getRecords(String objectName,
			String primaryObjectName, String xid, int page) {
		List<String> records = null;
		try {
			records = new Records().getRecordNames(objectName,
					primaryObjectName, xid, page);
		} catch (JSONException e) {
			Log.d("CustomAdapter", "Exception is: " + e.getMessage());
			// hasMorePagesListener.noMorePages(); //Logic for disable loading.
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

	private List<String> getRecords(String objectName, int page) {
		List<String> records = null;
		try {
			records = new Records().getRecordNames(objectName, page);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

	@Override
	public Filter getFilter() {
		if (listFilter == null) {
			listFilter = new ListFilter();
		}
		return listFilter;
	}
	
	
	//TODO: Fix bug: When search is invoked at the time page is loading then we get:
	//java.lang.IllegalStateException: The content of the adapter has changed but ListView 
	//did not receive a notification. Make sure the content of your adapter is not modified 
	//from a background thread, but only from the UI thread.
	private class ListFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					results.values = cloudObjectsOriginalValues;
					results.count = cloudObjectsOriginalValues.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
				final List<String> values = cloudObjectsOriginalValues;
                final int count = values.size();

                final List<String> newValues = new ArrayList<String>(count);
                
                for (int i = 0; i < count; i++) {
                    final String value = values.get(i);
                    Log.d("CustomAdapter", "Checking value: " + value);
                    final String[] words = value.toLowerCase().split(Constants.DELIMITER);
                    final int wordCount = words.length;

                    for (int k = 0; k < wordCount; k++) {
                        if (words[k].startsWith(prefixString)) {
                            newValues.add(value);
                            break;
                        }
                    }                 
                }
                results.values = newValues;
                results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			cloudObjects = (List<String>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}

}