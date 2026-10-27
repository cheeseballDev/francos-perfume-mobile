package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.InventorySearchFilterDTO;

import java.util.ArrayList;
import java.util.List;

public class FilterManager<T> {

    public static InventorySearchFilterDTO createDefaultFilter() {
        InventorySearchFilterDTO filter = new InventorySearchFilterDTO();
        filter.setPageCount(1);
        filter.setPageSize(20);
        filter.setSearch(null);
        filter.setProductType(null);
        filter.setProductGender(null);
        filter.setBranch(null);
        filter.setFromDate(null);
        filter.setToDate(null);
        return filter;
    }

    private final T filterDTO;
    private final OnFilterChangeListener<T> listener;

    public interface OnFilterChangeListener<T> {
        void onFilterChanged(T filter);
    }

    public interface FilterAction<T> {
        void apply(T filter);
    }

    public FilterManager(T initialDTO, OnFilterChangeListener<T> listener) {
        this.filterDTO = initialDTO;
        this.listener = listener;
    }

    public T getFilter() {
        return filterDTO;
    }

    public void updateFilter(FilterAction<T> action) {
        if (action != null) {
            action.apply(filterDTO);
            if (listener != null) {
                listener.onFilterChanged(filterDTO);
            }
        }
    }

    public static List<String> addDefaultOption(List<String> originalList, String defaultLabel) {
        List<String> updatedList = new ArrayList<>();
        updatedList.add(defaultLabel);
        if (originalList != null) {
            for (String item : originalList) {
                if (item != null && !item.trim().isEmpty() && !updatedList.contains(item)) {
                    updatedList.add(item);
                }
            }
        }
        return updatedList;
    }

    // ----------------------------------------------------------- dropdown filters

    /** Value picked in a filter dropdown; null means the "All ..." option. */
    public interface OnFilterSelected {
        void onSelected(String valueOrNull);
    }

    /**
     * Fills an exposed dropdown (MaterialAutoCompleteTextView inside a TextInputLayout)
     * with "All ..." + the given values. The listener only fires when the user picks something.
     */
    public static void setupDropdown(Context context, AutoCompleteTextView dropdown, List<String> items,
                                     String allLabel, OnFilterSelected listener) {
        if (context == null || dropdown == null) return;

        List<String> options = addDefaultOption(items, allLabel);
        dropdown.setAdapter(new NoFilterAdapter(context, options));
        dropdown.setText(allLabel, false);

        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            String value = position == 0 ? null : options.get(position);
            if (listener != null) listener.onSelected(value);
        });
    }

    /**
     * ArrayAdapter that never filters. A normal one filters by the text in the box,
     * so after picking "Unisex" the list would only show "Unisex" next time.
     */
    public static class NoFilterAdapter extends ArrayAdapter<String> {
        private final List<String> items;

        public NoFilterAdapter(Context context, List<String> items) {
            super(context, R.layout.item_dropdown, new ArrayList<>(items));
            this.items = new ArrayList<>(items);
        }

        @Override
        public android.widget.Filter getFilter() {
            return new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    results.values = items;
                    results.count = items.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }

                @Override
                public CharSequence convertResultToString(Object resultValue) {
                    return resultValue == null ? "" : resultValue.toString();
                }
            };
        }
    }

    /** Puts a dropdown back on its "All ..." option without firing the listener. */
    public static void resetDropdown(AutoCompleteTextView dropdown, String allLabel) {
        if (dropdown != null) dropdown.setText(allLabel, false);
    }

    // ------------------------------------------------- old Spinner API (still works)

    public static void setupSpinner(Context context, Spinner spinner, List<String> items, String defaultLabel) {
        if (context == null || spinner == null) return;

        List<String> options = addDefaultOption(items, defaultLabel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                options
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static abstract class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public abstract void onSelected(int position, String value);

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedValue = parent.getItemAtPosition(position).toString();
            onSelected(position, selectedValue);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Unused, but required by AdapterView.OnItemSelectedListener
        }
    }
}
