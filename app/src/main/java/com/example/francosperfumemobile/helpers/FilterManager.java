package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
            updatedList.addAll(originalList);
        }
        return updatedList;
    }

    // this - for activity, requireContext() - for fragments. For first argument (context)
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
