package com.udacity.heather.timetobake.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.heather.timetobake.utilities.Constants;
import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.adapters.IngredientAdapter;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.databinding.FragmentIngredientBinding;

public class IngredientFragment extends Fragment {

    private Recipe currentRecipe = new Recipe();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentIngredientBinding ingredientBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient, container, false);
        View view = ingredientBinding.getRoot();
        currentRecipe = getArguments().getParcelable(Constants.CURRENT_RECIPE);
        if (currentRecipe != null) {
            LinearLayoutManager ingredientLayoutManager
                    = new LinearLayoutManager(getActivity());
            ingredientBinding.rvIngredientsList.setLayoutManager(ingredientLayoutManager);
            ingredientBinding.rvIngredientsList.setHasFixedSize(true);
            IngredientAdapter ingredientAdapter = new IngredientAdapter();
            ingredientBinding.rvIngredientsList.setAdapter(ingredientAdapter);
            ingredientAdapter.setIngredientData(currentRecipe.getIngredients());
        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (currentRecipe != null) getActivity().setTitle(currentRecipe.getName());
    }
}


