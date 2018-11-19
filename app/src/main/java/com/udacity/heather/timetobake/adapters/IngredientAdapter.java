package com.udacity.heather.timetobake.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.udacity.heather.timetobake.databinding.IngredientListItemBinding;
import com.udacity.heather.timetobake.models.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterImageViewHolder> {

    public List<Ingredient>  ingredients;


    public IngredientAdapter() {

    }

    @NonNull
    @Override
    public IngredientAdapterImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachToParentImmediately = false;
        IngredientListItemBinding itemBinding = IngredientListItemBinding.inflate(inflater, viewGroup, shouldAttachToParentImmediately);
        return new IngredientAdapterImageViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterImageViewHolder ingredientAdapterImageViewHolder, int position) {
        Ingredient currentIngredient = ingredients.get(position);
        ingredientAdapterImageViewHolder.bindItem(currentIngredient);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    class IngredientAdapterImageViewHolder  extends RecyclerView.ViewHolder {
        private IngredientListItemBinding itemBinding;

        IngredientAdapterImageViewHolder(IngredientListItemBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bindItem(Ingredient currentIngredient) {
            String ingredientQuantity = String.valueOf(currentIngredient.getQuantity());
            itemBinding.tvIngredientName.setText(currentIngredient.getIngredient());
            itemBinding.tvIngredientQuantity.setText(ingredientQuantity);
            itemBinding.tvIngredientMeasure.setText(currentIngredient.getMeasure());
            itemBinding.executePendingBindings();
        }
    }

    public void setIngredientData(List<Ingredient> ingredientData) {
        ingredients = ingredientData;
        notifyDataSetChanged();
    }
}
