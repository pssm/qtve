package pedromendes.tempodeespera.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pedromendes.tempodeespera.R;
import pedromendes.tempodeespera.data.Hospital;


public class HospitalListAdapter extends ArrayAdapter<Hospital> implements Filterable {

    private final List<Hospital> hospitals;
    private final Typeface typeFace;
    private List<Hospital> filteredHospitals;
    private HospitalFilter filter = new HospitalFilter();

    public HospitalListAdapter(Context context, int resource, List<Hospital> objects) {
        super(context, resource, objects);
        this.hospitals = objects;
        this.filteredHospitals = new ArrayList<>();
        this.typeFace = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Hospital hospital = filteredHospitals.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hospital_list_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_hospital_name);
        if (hospital.isShareStandbyTimes()) {
            name.setText(getContext().getString(R.string.clock));
        } else {
            name.setText(getContext().getString(R.string.times_circle));
        }
        name.append(" ");
        name.append(hospital.getName());
        name.setTypeface(typeFace);

        return convertView;
    }

    @Override
    public int getCount() {
        return filteredHospitals.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public Hospital getItem(int position) {
        return filteredHospitals.get(position);
    }

    private class HospitalFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<Hospital> newList = new ArrayList<>();
            for (Hospital hospital : hospitals) {
                if (hospital.isInDistrict(filterString)) {
                    newList.add(hospital);
                }
            }

            results.values = newList;
            results.count = newList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredHospitals = (ArrayList<Hospital>) results.values;
            notifyDataSetChanged();
        }

    }
}
