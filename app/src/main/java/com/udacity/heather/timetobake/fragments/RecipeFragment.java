package com.udacity.heather.timetobake.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.adapters.StepAdapter;
import com.udacity.heather.timetobake.databinding.FragmentRecipeBinding;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class RecipeFragment extends Fragment implements  StepAdapter.StepAdapterOnClickHandler {
   private OnCurrentRecipeClickListener mCallback;


    @Override
    public void onClick(int position) {
        mCallback.onStepSelected(position);
    }

    public interface OnCurrentRecipeClickListener {
        void onStepSelected(int position);

        void onIngredientSelected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnCurrentRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCurrentRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        FragmentRecipeBinding recipeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe, container, false);
        View view = recipeBinding.getRoot();

        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity());
        recipeBinding.rvStepList.setLayoutManager(stepLayoutManager);
        recipeBinding.rvStepList.setHasFixedSize(true);
        StepAdapter stepAdapter = new StepAdapter(this);
        recipeBinding.rvStepList.setAdapter(stepAdapter);
        Bundle bundle = getArguments();
        Recipe currentRecipe = bundle.getParcelable(Constants.CURRENT_RECIPE);


        if (currentRecipe != null) {

            stepAdapter.setStepData(currentRecipe.getSteps());
            recipeBinding.tvRecipeHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onIngredientSelected();
                }

            });
        }
        return view;
    }
}